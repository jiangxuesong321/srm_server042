package org.cmoc.modules.srm.mapper;

import java.util.List;
import org.cmoc.modules.srm.entity.BiddingSupplier;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cmoc.modules.srm.vo.FixBiddingPage;

/**
 * @Description: 招标邀请供应商
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface BiddingSupplierMapper extends BaseMapper<BiddingSupplier> {

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
   * @return List<BiddingSupplier>
   */
	public List<BiddingSupplier> selectByMainId(@Param("mainId") String mainId);

	/**
	 * 投标供应商
	 * @param id
	 * @return
	 */
    List<BiddingSupplier> fetchBiddingSuppList(@Param("id") String id);

	/**
	 * 评标记录
	 * @param id
	 * @return
	 */
	List<BiddingSupplier> fetchHasBiddingSuppList(@Param("id") String id,@Param("supplierId") String supplierId);

	/**
	 * 报价信息
	 * @param id
	 * @return
	 */
    List<BiddingSupplier> fetchSuppList(@Param("id") String id);

	/**
	 * 进入定标
	 * @param id
	 * @return
	 */
    List<FixBiddingPage> fetchFixBiddingList(@Param("id") String id);
}
