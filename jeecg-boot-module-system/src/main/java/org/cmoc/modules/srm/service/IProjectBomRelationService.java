package org.cmoc.modules.srm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cmoc.modules.srm.entity.ApproveSetting;
import org.cmoc.modules.srm.entity.ProjBase;
import org.cmoc.modules.srm.entity.ProjectBomRelation;

import java.util.List;
import java.util.Map;

/**
 * @Description: 附件记录表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface IProjectBomRelationService extends IService<ProjectBomRelation> {

    /**
     * 设备清单
     * @param page
     * @param projBase
     * @return
     */
    IPage<ProjectBomRelation> fetchEqpPageList(Page<ProjectBomRelation> page, ProjectBomRelation projBase);

    /**
     * 项目下的设备
     * @param page
     * @param projBase
     * @return
     */
    IPage<ProjectBomRelation> fetchEqpPageListByProjId(Page<ProjectBomRelation> page, ProjectBomRelation projBase);

    /**
     * 设备清单预算汇总
     * @param projBase
     * @return
     */
    ProjectBomRelation fetchTotalEqp(ProjectBomRelation projBase);

    /**
     *
     * @param ids
     * @return
     */
    List<ProjectBomRelation> fetchBomList(List<String> ids);

    /**
     * 项目下的设备
     * @param prm
     * @return
     */
    List<ProjectBomRelation> fetchEqpList(ProjBase prm);

    /**
     * 数量合计
     * @param projBase
     * @return
     */
    Map<String, String> fetchTotalQty(ProjectBomRelation projBase);
}
