package com.cmoc.modules.quartz.job;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import com.cmoc.modules.srm.entity.BasSupplier;
import com.cmoc.modules.srm.entity.SupplierAccount;
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
 * 供应商解冻
 * 
 * @Author Scott
 */
@Slf4j
public class SupplierToThawJob implements Job {

	/**
	 * 若参数变量名修改 QuartzJobController中也需对应修改
	 */
	private String parameter;

	@Autowired
	private IBasSupplierService iBasSupplierService;
	@Autowired
	private ISupplierAccountService iSupplierAccountService;

	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info(" 供应商解冻Job开始执行：");

		String nowTime = sdf.format(new Date());
		List<BasSupplier> suppList = iBasSupplierService.list(Wrappers.<BasSupplier>query().lambda().
				lt(BasSupplier :: getFnEndTime,nowTime).
				eq(BasSupplier :: getStatus,"3"));
		for(BasSupplier sup : suppList){
			UpdateWrapper<BasSupplier> updateWrapper = new UpdateWrapper<>();
			updateWrapper.set("fn_start_time",null);
			updateWrapper.set("fn_end_time",null);
			updateWrapper.set("status","6");
			updateWrapper.eq("id",sup.getId());
			iBasSupplierService.update(updateWrapper);

			//账号冻结
			SupplierAccount account = iSupplierAccountService.getOne(Wrappers.<SupplierAccount>query().lambda().eq(SupplierAccount :: getSupplierId,sup.getId()));
			account.setStatus(1);
			iSupplierAccountService.updateById(account);
		}
	}
}
