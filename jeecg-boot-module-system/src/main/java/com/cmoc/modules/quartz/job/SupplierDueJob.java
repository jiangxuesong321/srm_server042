package com.cmoc.modules.quartz.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.cmoc.modules.srm.entity.BasSupplier;
import com.cmoc.modules.srm.entity.BasSupplierPay;
import com.cmoc.modules.srm.entity.SupplierAccount;
import com.cmoc.modules.srm.service.IBasSupplierPayService;
import com.cmoc.modules.srm.service.IBasSupplierService;
import com.cmoc.modules.srm.service.ISupplierAccountService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 供应商到期
 * 
 * @Author Scott
 */
@Slf4j
public class SupplierDueJob implements Job {

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

	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	@SneakyThrows
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info(" 供应商到期Job开始执行：");

		String nowTime = sdf.format(new Date());
		Date nowDate = sdf.parse(nowTime);

		List<BasSupplier> suppList = iBasSupplierService.list(Wrappers.<BasSupplier>query().lambda().
				eq(BasSupplier :: getStatus,"6"));

		for(BasSupplier sup : suppList){
			BasSupplierPay pay = iBasSupplierPayService.getOne(Wrappers.<BasSupplierPay>query().lambda().
					eq(BasSupplierPay :: getIsEnabled,"1").
					eq(BasSupplierPay :: getSupplierId,sup.getId()));
			if(pay != null){
				Date endTime = pay.getEndTime();
				int cp = nowDate.compareTo(endTime);
				if(cp == 1){
					sup.setStatus("7");
					iBasSupplierService.updateById(sup);
					//账号冻结
					SupplierAccount account = iSupplierAccountService.getOne(Wrappers.<SupplierAccount>query().lambda().eq(SupplierAccount :: getSupplierId,sup.getId()));
					account.setStatus(2);
					iSupplierAccountService.updateById(account);
				}
			}
		}
	}
}
