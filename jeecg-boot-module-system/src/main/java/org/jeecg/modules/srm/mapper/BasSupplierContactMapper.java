package org.jeecg.modules.srm.mapper;

import java.util.List;
import org.jeecg.modules.srm.entity.BasSupplierContact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 供应商联系人
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface BasSupplierContactMapper extends BaseMapper<BasSupplierContact> {

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
   * @return List<BasSupplierContact>
   */
	public List<BasSupplierContact> selectByMainId(@Param("mainId") String mainId);
}
