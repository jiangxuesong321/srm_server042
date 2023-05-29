package org.cmoc.modules.quartz.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.modules.message.handle.impl.EmailSendMsgHandle;
import org.cmoc.modules.srm.entity.*;
import org.cmoc.modules.srm.service.*;
import org.cmoc.modules.system.entity.SysDepart;
import org.cmoc.modules.system.entity.SysUser;
import org.cmoc.modules.system.service.ISysDepartService;
import org.cmoc.modules.system.service.ISysUserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 供应商到期
 * 
 * @Author Scott
 */
@Slf4j
public class SendInquiryMsgJob implements Job {

	/**
	 * 若参数变量名修改 QuartzJobController中也需对应修改
	 */
	private String parameter;

	@Autowired
	private IInquiryListService inquiryListService;
	@Autowired
	private IPurchaseRequestMainService iPurchaseRequestMainService;
	@Autowired
	private IProjBaseService iProjBaseService;
	@Autowired
	private ISysUserService iSysUserService;
	@Autowired
	private ISysDepartService iSysDepartService;
	@Autowired
	private IInquiryRecordService inquiryRecordService;
	@Autowired
	private IInquirySupplierService inquirySupplierService;

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
		log.info(" 招投标发送邮件给 项目立项人员、参与评标的人员、采购人：");
		List<InquiryList> iList = inquiryListService.list(Wrappers.<InquiryList>query().lambda().
				eq(InquiryList :: getDelFlag, CommonConstant.DEL_FLAG_0).
				eq(InquiryList :: getIsMsg,"0"));

		for(InquiryList il : iList){
			String reqId = il.getRequestId();
			//采购需求
			PurchaseRequestMain main = iPurchaseRequestMainService.getById(reqId);
			//项目
			ProjBase projBase = iProjBaseService.getById(main.getProjectId());
			//采购员
			SysUser reqUser = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,main.getBuyerId()));
			//项目立项人
			SysUser projUser = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,projBase.getApplyUserId()));
			List<String> emails = new ArrayList<>();
			if(StringUtils.isNotEmpty(reqUser.getEmail())){
				emails.add(reqUser.getEmail());
			}
			if(StringUtils.isNotEmpty(projUser.getEmail())){
				emails.add(projUser.getEmail());
			}
			//主体
			SysDepart depart = iSysDepartService.getById(projBase.getSubject());
			//询价明细
			List<InquiryRecord> inquiryRecordList = inquiryRecordService.list(Wrappers.<InquiryRecord>query().lambda().
					eq(InquiryRecord :: getDelFlag,CommonConstant.DEL_FLAG_0).
					eq(InquiryRecord :: getInquiryId,il.getId()));
			//查询询价单供应商信息
			List<BasSupplier> suppList = inquiryListService.fetchSuppList(il.getId());
			Map<String,BasSupplier> supMap = suppList.stream().collect(Collectors.toMap(BasSupplier::getId, sp->sp));
//			if(emails != null && emails.size() > 0){
//				for(InquiryRecord entity:inquiryRecordList) {
//					List<InquirySupplier> recordSuppList = inquirySupplierService.list(Wrappers.<InquirySupplier>query().lambda().
//							eq(InquirySupplier :: getDelFlag,CommonConstant.DEL_FLAG_0).
//							eq(InquirySupplier :: getRecordId,entity.getId()));
//					for(InquirySupplier iss : recordSuppList){
//						BasSupplier sp = supMap.get(iss.getSupplierId());
//						EmailSendMsgHandle emailHandle=new EmailSendMsgHandle();
//						String context = "["+sp.getName()+"]:" +
//								"<br>    &nbsp;&nbsp;&nbsp;&nbsp;你好！" +
//								"<br>    &nbsp;&nbsp;&nbsp;&nbsp;我司拟对如下设备进行询价,具体如下：" +
//								"<br>    &nbsp;&nbsp;&nbsp;&nbsp;项目标的：["+entity.getProdName()+"];" +
//								"<br>    &nbsp;&nbsp;&nbsp;&nbsp;标的数量：["+entity.getQty().stripTrailingZeros().toPlainString()+"];" +
//								"<br>    &nbsp;&nbsp;&nbsp;&nbsp;标的交期：["+sdf.format(entity.getLeadTime())+"];" +
//								"<br>    &nbsp;&nbsp;&nbsp;&nbsp;相关标的需求请联系["+reqUser.getRealname()+"],电话["+reqUser.getPhone()+"];" +
//								"<br>    &nbsp;&nbsp;&nbsp;&nbsp;请贵司务必于["+sdf.format(il.getQuotationDeadline())+"]前完成规格确认及报价提交，谢谢！" +
//								"<br><span style='margin-left:300px'>["+depart.getDepartName()+"]</span>";
//						emailHandle.sendTemplateMail("询价通知书",context,emails,null,"job");
//					}
//				}
//			}
			il.setIsMsg("1");
			inquiryListService.updateById(il);
		}
	}
}
