package org.cmoc.modules.srm.service.impl;

import org.cmoc.modules.srm.entity.BasSupplierQualification;
import org.cmoc.modules.srm.mapper.BasSupplierQualificationMapper;
import org.cmoc.modules.srm.service.IBasSupplierQualificationService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 供应商资质证书
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Service
public class BasSupplierQualificationServiceImpl extends ServiceImpl<BasSupplierQualificationMapper, BasSupplierQualification> implements IBasSupplierQualificationService {
	
	@Autowired
	private BasSupplierQualificationMapper basSupplierQualificationMapper;
	
	@Override
	public List<BasSupplierQualification> selectByMainId(String mainId) {
		return basSupplierQualificationMapper.selectByMainId(mainId);
	}

	@Override
	public boolean deleteByMainId(String mainId) {
		return basSupplierQualificationMapper.deleteByMainId(mainId);
	}
}
