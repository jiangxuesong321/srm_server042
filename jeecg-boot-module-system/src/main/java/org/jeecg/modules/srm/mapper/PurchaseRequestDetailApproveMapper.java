package org.jeecg.modules.srm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.srm.entity.PurchaseRequestDetail;
import org.jeecg.modules.srm.entity.PurchaseRequestDetailApprove;

import java.util.List;

/**
 * @Description: purchase_request_detail
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
public interface PurchaseRequestDetailApproveMapper extends BaseMapper<PurchaseRequestDetailApprove> {

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
   * @return List<PurchaseRequestDetail>
   */
	public List<PurchaseRequestDetailApprove> selectByMainId(@Param("mainId") String mainId);
}
