package com.cmoc.modules.srm.service;

import com.cmoc.modules.srm.entity.InquiryRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 询价单明细表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface IInquiryRecordService extends IService<InquiryRecord> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<InquiryRecord>
	 */
	public List<InquiryRecord> selectByMainId(String mainId);

	/**
	 * 查看询价
	 * @param id
	 * @return
	 */
    List<InquiryRecord> queryRecordList(String id);

	/**
	 *
	 * @param id
	 * @return
	 */
	List<InquiryRecord> getRecordById(String id);
}
