package org.jeecg.modules.srm.service;

import org.jeecg.modules.srm.entity.ProjectBomChild;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.srm.entity.ProjectCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: project_bom_child
 * @Author: jeecg-boot
 * @Date:   2022-09-23
 * @Version: V1.0
 */
public interface IProjectBomChildService extends IService<ProjectBomChild> {
    /**
     * 项目子项进度
     * @param bomChild
     * @return
     */
    List<Map<String, Object>> fetchChildProgress(ProjectBomChild bomChild);

    /**
     * 项目子类资金统计
     * @param bomChild
     * @return
     */
    List<ProjectBomChild> fetchChildAmount(ProjectBomChild bomChild);

    /**
     * 合同进度
     * @param bomChild
     * @return
     */
    List<Map<String, Object>> fetchChildProgress1(ProjectBomChild bomChild);
}
