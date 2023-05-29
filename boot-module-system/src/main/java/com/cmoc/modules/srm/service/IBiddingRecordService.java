package com.cmoc.modules.srm.service;

import com.cmoc.modules.srm.entity.BiddingProfessionals;
import com.cmoc.modules.srm.entity.BiddingRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmoc.modules.srm.entity.BiddingSupplier;

import java.util.List;

/**
 * @Description: 招标明细表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface IBiddingRecordService extends IService<BiddingRecord> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<BiddingRecord>
	 */
	public List<BiddingRecord> selectByMainId(String mainId);

	/**
	 * 招标明细
	 * @param id
	 * @return
	 */
    List<BiddingRecord> queryRecordList(String id);

	/**
	 * 招标供应商
	 * @param id
	 * @return
	 */
	List<BiddingSupplier> querySuppList(String id);

	/**
	 * 评标模板
	 * @param id
	 * @return
	 */
	List<BiddingProfessionals> queryTemplateList(String id);

	/**
	 * 招标明细
	 * @param id
	 * @return
	 */
    List<BiddingRecord> queryRecordListByMainId(String id);
}
