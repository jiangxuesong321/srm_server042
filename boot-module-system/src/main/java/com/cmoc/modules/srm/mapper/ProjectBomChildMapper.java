package com.cmoc.modules.srm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.cmoc.modules.srm.entity.ContractBase;
import com.cmoc.modules.srm.entity.ProjectBomChild;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmoc.modules.srm.entity.ProjectBomRelation;

/**
 * @Description: project_bom_child
 * @Author: jeecg-boot
 * @Date:   2022-09-23
 * @Version: V1.0
 */
public interface ProjectBomChildMapper extends BaseMapper<ProjectBomChild> {
    /**
     * 子项总产能
     * @param bomChild
     * @return
     */
    List<ProjectBomRelation> fetchTotalCapacity(@Param("query") ProjectBomChild bomChild);

    /**
     * Neck产能
     * @param bomChild
     * @return
     */
    List<ProjectBomRelation> fetchDiffCapacity(@Param("query") ProjectBomChild bomChild);

    /**
     * 项目已投
     * @param param
     * @return
     */
    ProjectBomChild fetchContractByProjId(@Param("query") ContractBase param);

    /**
     * 子项执行预算总额
     * @return
     */
    List<ProjectBomChild> fetchBomChildList(@Param("query") ProjectBomChild bomChild);

    /**
     * 项目金额
     * @param bomChild
     * @return
     */
    ProjectBomRelation fetchDiffCapacityTotal(@Param("query") ProjectBomChild bomChild);
}
