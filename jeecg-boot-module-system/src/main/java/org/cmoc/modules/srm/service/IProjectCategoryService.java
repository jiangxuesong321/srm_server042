package org.cmoc.modules.srm.service;

import org.cmoc.modules.srm.entity.ProjectCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: project_category
 * @Author: jeecg-boot
 * @Date:   2022-09-23
 * @Version: V1.0
 */
public interface IProjectCategoryService extends IService<ProjectCategory> {
    /**
     * 分类
     * @param category
     * @return
     */
    List<ProjectCategory> fetchCategory(ProjectCategory category);

    /**
     * 末级分类可选择
     * @param category
     * @return
     */
    List<ProjectCategory> fetchLastCategory(ProjectCategory category);

    /**
     * 分类资金统计
     * @param category
     * @return
     */
    List<ProjectCategory> fetchCategoryToAmount(ProjectCategory category);
}
