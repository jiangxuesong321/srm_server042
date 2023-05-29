package com.cmoc.modules.srm.mapper;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.cmoc.modules.srm.entity.ProjectCategoryPay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: project_category_pay
 * @Author: jeecg-boot
 * @Date:   2022-12-05
 * @Version: V1.0
 */
public interface ProjectCategoryPayMapper extends BaseMapper<ProjectCategoryPay> {
    /**
     * 获取特殊分类已付
     * @param id
     * @return
     */
    Map<String, BigDecimal> fetchHasPayByCategoryId(@Param("id") String id,@Param("type") String type);
}
