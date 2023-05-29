package com.cmoc.modules.srm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.cmoc.modules.srm.entity.ProjBase;
import com.cmoc.modules.srm.entity.ProjectBomRelation;

import java.util.List;
import java.util.Map;

/**
 * @Description: 附件记录表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface ProjectBomRelationMapper extends BaseMapper<ProjectBomRelation> {

    /**
     * 设备清单
     * @param page
     * @param projBase
     * @return
     */
    IPage<ProjectBomRelation> fetchEqpPageList(Page<ProjectBomRelation> page, @Param("query") ProjectBomRelation projBase);

    /**
     * 项目下的设备
     * @param page
     * @param projBase
     * @return
     */
    IPage<ProjectBomRelation> fetchEqpPageListByProjId(Page<ProjectBomRelation> page, @Param("query") ProjectBomRelation projBase);

    /**
     * 设备清单预算汇总
     * @param projBase
     * @return
     */
    ProjectBomRelation fetchTotalEqp(@Param("query") ProjectBomRelation projBase);

    /**
     * 设备清单
     * @param ids
     * @return
     */
    List<ProjectBomRelation> fetchBomList(@Param("ids") List<String> ids);

    /**
     * 项目设备
     * @param prm
     * @return
     */
    List<ProjectBomRelation> fetchEqpList(@Param("query") ProjBase prm);

    /**
     * 数量合计
     * @param projBase
     * @return
     */
    Map<String, String> fetchTotalQty(@Param("query") ProjectBomRelation projBase);
}
