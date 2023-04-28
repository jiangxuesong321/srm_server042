package org.jeecg.modules.quartz.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.message.handle.impl.EmailSendMsgHandle;
import org.jeecg.modules.srm.entity.*;
import org.jeecg.modules.srm.service.*;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 供应商到期
 * 
 * @Author Scott
 */
@Slf4j
public class SendBiddingMsgJob implements Job {

	/**
	 * 若参数变量名修改 QuartzJobController中也需对应修改
	 */
	private String parameter;

	@Autowired
	private IBiddingMainService iBiddingMainService;
	@Autowired
	private IPurchaseRequestMainService iPurchaseRequestMainService;
	@Autowired
	private IProjBaseService iProjBaseService;
	@Autowired
	private ISysUserService iSysUserService;
	@Autowired
	private ISysDepartService iSysDepartService;
	@Autowired
	private IBiddingRecordService iBiddingRecordService;


	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * 项目立项人员、参与评标的人员、采购人 发送邮件
	 * @param jobExecutionContext
	 * @throws JobExecutionException
	 */
	@SneakyThrows
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info(" 询价发送邮件给 项目立项人员、参与评标的人员、采购人：");
		List<BiddingMain> mList = iBiddingMainService.list(Wrappers.<BiddingMain>query().lambda().
				eq(BiddingMain :: getDelFlag,CommonConstant.DEL_FLAG_0).
				eq(BiddingMain :: getIsMsg,"0"));


		for(BiddingMain il : mList){
			String reqId = il.getRequestId();
			//采购需求
			PurchaseRequestMain main = iPurchaseRequestMainService.getById(reqId);
			//项目
			ProjBase projBase = iProjBaseService.getById(main.getProjectId());
			//采购员
			SysUser reqUser = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,main.getBuyerId()));
			//项目立项人
			SysUser projUser = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,projBase.getApplyUserId()));
			//参与评标人员
			List<String> emailList = iBiddingMainService.fetchProFessionalsEmail(il.getId());
			//采购需求人
			SysUser reqUser1 = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,main.getApplyUserId()));

			List<String> emails = new ArrayList<>();
			if(StringUtils.isNotEmpty(reqUser.getEmail())){
				emails.add(reqUser.getEmail());
			}
			if(StringUtils.isNotEmpty(reqUser1.getEmail())){
				emails.add(reqUser1.getEmail());
			}
			if(StringUtils.isNotEmpty(projUser.getEmail())){
				emails.add(projUser.getEmail());
			}
			for(String em : emailList){
				emails.add(em);
			}
			//主体
			SysDepart depart = iSysDepartService.getById(projBase.getSubject());
			//实施地点
			SysDepart depart1 = iSysDepartService.getById(il.getOpenBiddingAddress());
			if(depart1 == null){
				depart1 = new SysDepart();
			}
			//招标供应商
			List<BasSupplier> suppList = iBiddingMainService.fetchSuppList(il.getId());

			//招标明细
			List<BiddingRecord> recordList = iBiddingRecordService.list(Wrappers.<BiddingRecord>query().lambda().
					eq(BiddingRecord :: getDelFlag,CommonConstant.DEL_FLAG_0).
					eq(BiddingRecord :: getBiddingId,il.getId()));
//			if(emails != null && emails.size() > 0){
//				for(BasSupplier supp : suppList){
//					String context = "["+supp.getName()+"]:" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;你好！" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;我司拟对如下设备进行邀标,具体如下：";
//					for(BiddingRecord entity:recordList) {
//						String text = "<br>&nbsp;&nbsp;&nbsp;&nbsp;项目标的：["+entity.getProdName()+"]；" +
//								"<br>&nbsp;&nbsp;&nbsp;&nbsp;标的数量：["+entity.getQty().stripTrailingZeros().toPlainString()+"]；" +
//								"<br>&nbsp;&nbsp;&nbsp;&nbsp;标的交期：["+sdf.format(entity.getLeadTime())+"]；" +
//								"<br>&nbsp;&nbsp;&nbsp;&nbsp;实施地点：["+depart1.getDepartName()+"];";
//						context = context + text;
//					}
//					context = context + "<br>&nbsp;&nbsp;&nbsp;&nbsp;相关标的需求请联系["+reqUser.getRealname()+"]，电话["+reqUser.getPhone()+"];" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;请贵司务必于开标日["+sdf.format(il.getBiddingDeadline())+"]前完成规格确认及报价提交，谢谢！" +
//							"<br><span style='margin-left:300px'>["+depart.getDepartName()+"]</span>";
//					EmailSendMsgHandle emailHandle = new EmailSendMsgHandle();
//					emailHandle.sendTemplateMail("招标通知书",context,emails,null,"job");
//				}
//			}
			il.setIsMsg("1");
			iBiddingMainService.updateById(il);
		}
	}
}
