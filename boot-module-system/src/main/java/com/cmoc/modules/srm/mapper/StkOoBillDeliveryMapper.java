package com.cmoc.modules.srm.mapper;

import java.util.List;
import com.cmoc.modules.srm.entity.StkOoBillDelivery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 出库明细
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
public interface StkOoBillDeliveryMapper extends BaseMapper<StkOoBillDelivery> {

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
   * @return List<StkOoBillDelivery>
   */
	public List<StkOoBillDelivery> selectByMainId(@Param("mainId") String mainId);
}
