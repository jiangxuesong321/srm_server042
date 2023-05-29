package com.cmoc.modules.quartz.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.cmoc.common.constant.CommonConstant;
import com.cmoc.modules.srm.entity.BasSupplier;
import com.cmoc.modules.srm.entity.BasSupplierContact;
import com.cmoc.modules.srm.entity.BasSupplierPay;
import com.cmoc.modules.srm.service.IBasSupplierContactService;
import com.cmoc.modules.srm.service.IBasSupplierPayService;
import com.cmoc.modules.srm.service.IBasSupplierService;
import com.cmoc.modules.srm.service.ISupplierAccountService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 供应商到期
 * 
 * @Author Scott
 */
@Slf4j
public class SupplierEmailJob implements Job {

	/**
	 * 若参数变量名修改 QuartzJobController中也需对应修改
	 */
	private String parameter;

	@Autowired
	private IBasSupplierService iBasSupplierService;
	@Autowired
	private ISupplierAccountService iSupplierAccountService;
	@Autowired
	private IBasSupplierPayService iBasSupplierPayService;
	@Autowired
	private IBasSupplierContactService iBasSupplierContactService;

	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	@SneakyThrows
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info(" 供应商到期邮件提醒Job开始执行：");

		String nowTime = sdf.format(new Date());
		Date nowDate = sdf.parse(nowTime);

		List<BasSupplier> suppList = iBasSupplierService.list(Wrappers.<BasSupplier>query().lambda().
				eq(BasSupplier :: getStatus,"6"));

		for(BasSupplier sup : suppList){
			BasSupplierPay pay = iBasSupplierPayService.getOne(Wrappers.<BasSupplierPay>query().lambda().
					eq(BasSupplierPay :: getIsEnabled,"1").
					eq(BasSupplierPay :: getSupplierId,sup.getId()));

			List<BasSupplierContact> contactList = iBasSupplierContactService.list(Wrappers.<BasSupplierContact>query().lambda().
					eq(BasSupplierContact :: getSupplierId,sup.getId()).
					eq(BasSupplierContact :: getDelFlag, CommonConstant.DEL_FLAG_0).
					eq(BasSupplierContact :: getIsDefault,CommonConstant.ACT_SYNC_1));

			if(pay != null){
				Date endTime = pay.getEndTime();
				int day = differentDays(nowDate,endTime);
//				if(day < 30 && day > 0){
//					if(contactList != null && contactList.size() > 0){
//						List<String> emails = new ArrayList<>();
//						emails.add(contactList.get(0).getContacterEmail());
//						EmailSendMsgHandle emailHandle=new EmailSendMsgHandle();
//						String context = "[" + sup.getName() + "]:" +
//								"<br>&nbsp;&nbsp;&nbsp;&nbsp;你好！" +
//								"<br>&nbsp;&nbsp;&nbsp;&nbsp;贵司会员即将于["+sdf.format(endTime)+"]到期，请贵司及时续费，以免影响后续与中环的商务对接事宜，谢谢！" +
//								"<br>"+
//								"<br>"+
//								"<br><span style='margin-left:410px'>[中环领先半导体材料有限公司]</span>";
//						emailHandle.sendTemplateMail("会员续费",context,emails,null,"job");
//					}else{
//						log.error("供应商到期日续费job提醒,该供应商没有设置邮箱地址");
//					}
//				}
			}
		}
	}

	/**
	 * 2个日期相差天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int differentDays(Date date1,Date date2){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);

		int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
		int day2 = calendar2.get(Calendar.DAY_OF_YEAR);
		int year1 = calendar1.get(Calendar.YEAR);
		int year2 = calendar2.get(Calendar.YEAR);

		if (year1 != year2)  //不同年
		{
			int timeDistance = 0;
			for (int i = year1 ; i < year2 ;i++){ //闰年
				if (i%4==0 && i%100!=0||i%400==0){
					timeDistance += 366;
				}else { // 不是闰年
					timeDistance += 365;
				}
			}
			return  timeDistance + (day2-day1);
		}else{// 同年
			return day2-day1;
		}

	}
}
