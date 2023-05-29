package com.cmoc.modules.srm.mapper;

import java.util.List;
import com.cmoc.modules.srm.entity.PurPayApplyDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 付款申请明细
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface PurPayApplyDetailMapper extends BaseMapper<PurPayApplyDetail> {

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
   * @return List<PurPayApplyDetail>
   */
	public List<PurPayApplyDetail> selectByMainId(@Param("mainId") String mainId);
}
