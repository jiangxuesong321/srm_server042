package org.cmoc.modules.srm.mapper;

import java.util.List;

import org.cmoc.modules.srm.entity.ProjectBomChild;
import org.cmoc.modules.srm.entity.ProjectCategory;
import org.cmoc.modules.srm.entity.PurPayApply;
import org.cmoc.modules.srm.entity.PurPayPlanDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 付款计划明细
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
public interface PurPayPlanDetailMapper extends BaseMapper<PurPayPlanDetail> {

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
   * @return List<PurPayPlanDetail>
   */
	public List<PurPayApply> selectByMainId(@Param("mainId") String mainId);

	/**
	 * 已付金额
	 * @param bomChild
	 * @return
	 */
    List<PurPayPlanDetail> fetchAmountByModel(@Param("query") ProjectBomChild bomChild);

	/**
	 * 已付金额
	 * @param category
	 * @return
	 */
	List<PurPayPlanDetail> fetchAmountByCategory(@Param("query")  ProjectCategory category);
}
