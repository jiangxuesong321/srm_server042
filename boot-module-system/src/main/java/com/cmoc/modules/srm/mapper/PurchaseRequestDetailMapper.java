package com.cmoc.modules.srm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.cmoc.modules.srm.entity.ProjectBomChild;
import com.cmoc.modules.srm.entity.ProjectCategory;
import com.cmoc.modules.srm.entity.PurchaseRequestDetail;

import java.util.List;

/**
 * @Description: purchase_request_detail
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
public interface PurchaseRequestDetailMapper extends BaseMapper<PurchaseRequestDetail> {

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
	public List<PurchaseRequestDetail> selectByMainId(@Param("mainId") String mainId);

	/**
	 * 执行预算
	 * @param bomChild
	 * @return
	 */
    List<PurchaseRequestDetail> fetchAmountByModel(@Param("query") ProjectBomChild bomChild);

	/**
	 * 执行预算
	 * @param category
	 * @return
	 */
	List<PurchaseRequestDetail> fetchAmountByCategory(@Param("query") ProjectCategory category);

	/**
	 * 需求明细
	 * @param id
	 * @return
	 */
    List<PurchaseRequestDetail> queryPurchaseRequestDetail(@Param("mainId") String id);

	/**
	 * @param id
	 * @return PurchaseRequestDetail
	 * @author Kevin.Wang
	 * @date : 2023/2/10 18:40
	 **/
	PurchaseRequestDetail countInfo(@Param("id") String id);

	/**
	 * 获取采购明细
	 * @param id
	 * @return
	 */
    List<PurchaseRequestDetail> fetchBatchRecordList(@Param("ids") String id);
}
