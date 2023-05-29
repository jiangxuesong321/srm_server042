package com.cmoc.modules.srm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmoc.modules.srm.entity.BasSupplierBank;

import java.util.List;

/**
 * @Description: 供应商资质证书
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface IBasSupplierBankService extends IService<BasSupplierBank> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<BasSupplierQualification>
	 */
	public List<BasSupplierBank> selectByMainId(String mainId);

	boolean deleteByMainId(String mainId);
}
