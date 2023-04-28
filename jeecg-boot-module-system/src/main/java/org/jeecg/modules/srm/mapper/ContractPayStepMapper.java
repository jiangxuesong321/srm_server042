package org.jeecg.modules.srm.mapper;

import java.util.List;
import org.jeecg.modules.srm.entity.ContractPayStep;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 合同付款阶段
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
public interface ContractPayStepMapper extends BaseMapper<ContractPayStep> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<ContractPayStep>
   */
	public List<ContractPayStep> selectByMainId(@Param("mainId") String mainId);
}
