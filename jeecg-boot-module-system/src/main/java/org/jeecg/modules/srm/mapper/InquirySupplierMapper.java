package org.jeecg.modules.srm.mapper;

import java.util.List;
import org.jeecg.modules.srm.entity.InquirySupplier;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 询价供应商表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface InquirySupplierMapper extends BaseMapper<InquirySupplier> {

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
   * @return List<InquirySupplier>
   */
	public List<InquirySupplier> selectByMainId(@Param("mainId") String mainId);

	/**
	 * 中标供应商
	 * @param id
	 * @return
	 */
    InquirySupplier getSuppInfo(@Param("id") String id);
}
