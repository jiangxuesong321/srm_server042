package com.cmoc.modules.srm.service;

import com.cmoc.modules.srm.entity.BasSupplierResume;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: bas_supplier_resume
 * @Author: jeecg-boot
 * @Date:   2022-10-25
 * @Version: V1.0
 */
public interface IBasSupplierResumeService extends IService<BasSupplierResume> {
    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<BasSupplierResume>
     */
    public List<BasSupplierResume> selectByMainId(String mainId);

    boolean deleteByMainId(String mainId);
}
