package com.cmoc.modules.srm.service;

import com.cmoc.modules.srm.entity.BasSupplierQualification;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 供应商资质证书
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface IBasSupplierQualificationService extends IService<BasSupplierQualification> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<BasSupplierQualification>
	 */
	public List<BasSupplierQualification> selectByMainId(String mainId);

	boolean deleteByMainId(String mainId);
}
