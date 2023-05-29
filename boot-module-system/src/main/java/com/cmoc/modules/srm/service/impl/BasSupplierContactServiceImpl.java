package com.cmoc.modules.srm.service.impl;

import com.cmoc.modules.srm.entity.BasSupplierContact;
import com.cmoc.modules.srm.mapper.BasSupplierContactMapper;
import com.cmoc.modules.srm.service.IBasSupplierContactService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 供应商联系人
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Service
public class BasSupplierContactServiceImpl extends ServiceImpl<BasSupplierContactMapper, BasSupplierContact> implements IBasSupplierContactService {
	
	@Autowired
	private BasSupplierContactMapper basSupplierContactMapper;
	
	@Override
	public List<BasSupplierContact> selectByMainId(String mainId) {
		return basSupplierContactMapper.selectByMainId(mainId);
	}

	@Override
	public boolean deleteByMainId(String mainId) {
		return basSupplierContactMapper.deleteByMainId(mainId);
	}
}
