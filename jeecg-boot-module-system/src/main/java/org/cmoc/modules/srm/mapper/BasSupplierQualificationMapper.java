package org.cmoc.modules.srm.mapper;

import java.util.List;
import org.cmoc.modules.srm.entity.BasSupplierQualification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 供应商资质证书
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface BasSupplierQualificationMapper extends BaseMapper<BasSupplierQualification> {

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
   * @return List<BasSupplierQualification>
   */
	public List<BasSupplierQualification> selectByMainId(@Param("mainId") String mainId);
}
