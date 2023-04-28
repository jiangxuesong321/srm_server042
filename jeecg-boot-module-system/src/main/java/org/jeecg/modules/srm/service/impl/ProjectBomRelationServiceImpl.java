package org.jeecg.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.srm.entity.ApproveSetting;
import org.jeecg.modules.srm.entity.ProjBase;
import org.jeecg.modules.srm.entity.ProjectBomRelation;
import org.jeecg.modules.srm.mapper.ApproveSettingMapper;
import org.jeecg.modules.srm.mapper.ProjectBomRelationMapper;
import org.jeecg.modules.srm.service.IApproveSettingService;
import org.jeecg.modules.srm.service.IProjectBomRelationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 附件记录表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Service
public class ProjectBomRelationServiceImpl extends ServiceImpl<ProjectBomRelationMapper, ProjectBomRelation> implements IProjectBomRelationService {

    /**
     * 设备清单
     * @param page
     * @param projBase
     * @return
     */
    @Override
    public IPage<ProjectBomRelation> fetchEqpPageList(Page<ProjectBomRelation> page, ProjectBomRelation projBase) {
        return baseMapper.fetchEqpPageList(page,projBase);
    }

    /**
     * 项目下的设备
     * @param page
     * @param projBase
     * @return
     */
    @Override
    public IPage<ProjectBomRelation> fetchEqpPageListByProjId(Page<ProjectBomRelation> page, ProjectBomRelation projBase) {
        return baseMapper.fetchEqpPageListByProjId(page,projBase);
    }

    /**
     * 设备清单预算汇总
     * @param projBase
     * @return
     */
    @Override
    public ProjectBomRelation fetchTotalEqp(ProjectBomRelation projBase) {
        return baseMapper.fetchTotalEqp(projBase);
    }

    /**
     * 设备清单
     * @param ids
     * @return
     */
    @Override
    public List<ProjectBomRelation> fetchBomList(List<String> ids) {
        return baseMapper.fetchBomList(ids);
    }

    /**
     * 项目设备
     * @param prm
     * @return
     */
    @Override
    public List<ProjectBomRelation> fetchEqpList(ProjBase prm) {
        return baseMapper.fetchEqpList(prm);
    }

    /**
     * 数量合计
     * @param projBase
     * @return
     */
    @Override
    public Map<String, String> fetchTotalQty(ProjectBomRelation projBase) {
        return baseMapper.fetchTotalQty(projBase);
    }
}
