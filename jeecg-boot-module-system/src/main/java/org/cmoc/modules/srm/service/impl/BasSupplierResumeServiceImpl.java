package org.cmoc.modules.srm.service.impl;

import org.cmoc.modules.srm.entity.BasSupplierResume;
import org.cmoc.modules.srm.mapper.BasSupplierResumeMapper;
import org.cmoc.modules.srm.service.IBasSupplierResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: bas_supplier_resume
 * @Author: jeecg-boot
 * @Date:   2022-10-25
 * @Version: V1.0
 */
@Service
public class BasSupplierResumeServiceImpl extends ServiceImpl<BasSupplierResumeMapper, BasSupplierResume> implements IBasSupplierResumeService {


    @Autowired
    private BasSupplierResumeMapper basSupplierResumeMapper;

    @Override
    public List<BasSupplierResume> selectByMainId(String mainId) {
        return basSupplierResumeMapper.selectByMainId(mainId);
    }

    @Override
    public boolean deleteByMainId(String mainId) {
        return basSupplierResumeMapper.deleteByMainId(mainId);
    }
}
