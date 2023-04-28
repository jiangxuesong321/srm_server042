package org.jeecg.modules.srm.service;

import org.jeecg.modules.srm.entity.InquirySupplier;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 询价供应商表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface IInquirySupplierService extends IService<InquirySupplier> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<InquirySupplier>
	 */
	public List<InquirySupplier> selectByMainId(String mainId);

	/**
	 * 中标供应商
	 * @param id
	 * @return
	 */
    InquirySupplier getSuppInfo(String id);
}
