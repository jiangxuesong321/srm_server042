package com.cmoc.modules.srm.service;

import com.cmoc.modules.srm.entity.ProjectCategory;
import com.cmoc.modules.srm.entity.ProjectCategoryPay;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Description: project_category_pay
 * @Author: jeecg-boot
 * @Date:   2022-12-05
 * @Version: V1.0
 */
public interface IProjectCategoryPayService extends IService<ProjectCategoryPay> {
    /**
     * 获取特殊分类已付
     * @param id
     * @return
     */
    Map<String, BigDecimal> fetchHasPayByCategoryId(String id,String type);

    /**
     * 提交分类支出
     * @param category
     */
    void submitCategoryPay(ProjectCategory category);
}
