package com.cmoc.modules.srm.mapper;

import java.util.List;
import com.cmoc.modules.srm.entity.ContractTerms;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 合同条款
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
public interface ContractTermsMapper extends BaseMapper<ContractTerms> {

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
   * @return List<ContractTerms>
   */
	public List<ContractTerms> selectByMainId(@Param("mainId") String mainId);
}
