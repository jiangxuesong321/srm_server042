package org.jeecg.modules.srm.service;

import org.jeecg.modules.srm.entity.BiddingRecord;
import org.jeecg.modules.srm.entity.BiddingSupplier;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.srm.vo.BiddingSupplierPage;
import org.jeecg.modules.srm.vo.FixBiddingPage;

import java.util.List;

/**
 * @Description: 招标邀请供应商
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface IBiddingSupplierService extends IService<BiddingSupplier> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<BiddingSupplier>
	 */
	public List<BiddingSupplier> selectByMainId(String mainId);

	/**
	 * 投标供应商
	 * @param id
	 * @return
	 */
    List<BiddingSupplier> fetchBiddingSuppList(String id);

	/**
	 * 评标记录
	 * @param id
	 * @return
	 */
	List<BiddingSupplier> fetchHasBiddingSuppList(String id,String ids,String supplierId);

	/**
	 * 评标结果
	 * @param id
	 * @return
	 */
    List<BiddingSupplierPage> fetchResult(String id);

	/**
	 * 进入定标
	 * @param id
	 * @return
	 */
    List<BiddingRecord> fetchFixBiddingList(String id);
}
