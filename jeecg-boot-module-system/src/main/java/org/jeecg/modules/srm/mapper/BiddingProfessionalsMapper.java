package org.jeecg.modules.srm.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.srm.entity.BiddingProfessionals;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.srm.entity.BiddingSupplier;
import org.jeecg.modules.srm.entity.PurchaseRequestMain;

/**
 * @Description: bidding_professionals
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface BiddingProfessionalsMapper extends BaseMapper<BiddingProfessionals> {

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
   * @return List<BiddingProfessionals>
   */
	public List<BiddingProfessionals> selectByMainId(@Param("mainId") String mainId);

	/**
	 * 模板
	 * @param id
	 * @return
	 */
    List<BiddingProfessionals> groupByTemplateList(@Param("id") String id,@Param("username") String username);

	/**
	 * 评标专家
	 * @param id
	 * @return
	 */
	List<BiddingProfessionals> fetchBiddingExpertList(@Param("id") String id);

	/**
	 * 参与评标的专家
	 * @param id
	 * @return
	 */
    List<BiddingProfessionals> countNum(@Param("id") String id);

	/**
	 * 专家评标记录
	 * @param page
	 * @return
	 */
    IPage<PurchaseRequestMain> fetchBidEvaluation(Page page, @Param("query") PurchaseRequestMain param);

	/**
	 * 产品+供应商分组计算总分
	 * @param biddingSupplier
	 * @return
	 */
    List<Map<String, Object>> fetchExpertTotalScore(@Param("query") BiddingSupplier biddingSupplier);

	/**
	 * 产品+供应商分组计算技术总分
	 * @param biddingSupplier
	 * @return
	 */
    List<Map<String, Object>> fetchExpertJsTotalScore(@Param("query") BiddingSupplier biddingSupplier);

	/**
	 *
	 * @param type
	 * @return
	 */
	List<Map<String, Object>> fetchScore(@Param("type") String type,@Param("biddingId") String biddingId);

	/**
	 * 评标人员
	 * @param biddingSupplier
	 * @return
	 */
	Integer countExpert(@Param("query") BiddingSupplier biddingSupplier);
}
