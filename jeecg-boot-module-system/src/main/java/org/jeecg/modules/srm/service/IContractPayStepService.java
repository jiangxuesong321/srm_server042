package org.jeecg.modules.srm.service;

import org.jeecg.modules.srm.entity.ContractPayStep;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 合同付款阶段
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
public interface IContractPayStepService extends IService<ContractPayStep> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<ContractPayStep>
	 */
	public List<ContractPayStep> selectByMainId(String mainId);
}
