package org.jeecg.modules.srm.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.srm.entity.ProjBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.srm.entity.ProjectBomChild;
import org.jeecg.modules.srm.entity.ProjectBomRelation;
import org.jeecg.modules.srm.vo.BasMaterialImport;
import org.jeecg.modules.srm.vo.FileParam;
import org.jeecg.modules.srm.vo.ProjBudget;

/**
 * @Description: proj_base
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface ProjBaseMapper extends BaseMapper<ProjBase> {
    /**
     * 项目
     * @param projBase
     * @return
     */
    ProjBase fetchProjById(@Param("query") ProjBase projBase);

    /**
     * 分页
     * @param page
     * @param projBase
     * @return
     */
    IPage<ProjBase> pageList(Page<ProjBase> page, @Param("query") ProjBase projBase);

    /**
     * 项目总产能
     * @param projBase
     * @return
     */
    ProjBase fetchCapacityByProjId(@Param("query") ProjBase projBase);

    /**
     * 资金类型饼图(已付、合同金额)
     * @param projBase
     * @return
     */
    List<Map<String, String>> fetchCategoryAmount(@Param("query") ProjBase projBase);

    /**
     * 项目总投
     * @param projBase
     * @return
     */
    ProjBase fetchProjectAmount(@Param("query") ProjBase projBase);

    /**
     * 项目总预算
     * @param projBase
     * @return
     */
    BigDecimal fetchBudgetAmountByProjId(@Param("query") ProjBase projBase);

    /**
     * 合同金额
     * @param projBase
     * @return
     */
    BigDecimal fetchContractAmountByProjId(@Param("query") ProjBase projBase);

    /**
     * 项目总数量
     * @param projBase
     * @return
     */
    BigDecimal fetchBudgetQtyByProjId(@Param("query") ProjBase projBase);

    /**
     * 合同总数量
     * @param projBase
     * @return
     */
    BigDecimal fetchContractQtyByProjId(@Param("query") ProjBase projBase);

    /**
     * 项目总产能(合并同类项)
     * @param projBase
     * @return
     */
    List<ProjectBomRelation> fetchCapacity(@Param("query") ProjBase projBase);

    /**
     * 项目预算购置表
     * @param page
     * @param contractBase
     * @return
     */
    IPage<ProjBudget> fetchProjBudgetPageList(Page<ProjBudget> page, @Param("query") ProjBudget contractBase);

    /**
     * 项目预算购置表导出
     * @param contractBase
     * @return
     */
    List<ProjBudget> exportProjBudgetPageList(@Param("query") ProjBudget contractBase);

    /**
     * 项目预算购置表汇总
     * @param contractBase
     * @return
     */
    ProjBudget fetchTotalProjBudgetPageList(@Param("query") ProjBudget contractBase);

    /**
     *
     * @param fileParam
     */
    void changeFileName(@Param("query") FileParam fileParam);

    /**
     * 获取项目下得子项
     * @param projBase
     * @return
     */
    List<Map<String, String>> fetchModelByProjId(@Param("query") ProjBase projBase);

    /**
     * 项目类型统计
     * @param projBase
     * @return
     */
    List<Map<String,Object>> fetchProjType(@Param("query") ProjBase projBase);

    /**
     * 项目地区
     * @param projBase
     * @return
     */
    List<Map<String, Object>> fetchProjArea(@Param("query") ProjBase projBase);

    /**
     * 设备导出
     * @param projBase
     * @return
     */
    List<BasMaterialImport> exportProdList(@Param("query") ProjBase projBase);

    /**
     * 项目列表导出
     * @param projBase
     * @return
     */
    List<ProjBase> exportXlsByList(@Param("query") ProjBase projBase);

    /**
     * 项目总投
     * @param projBase
     * @return
     */
    List<Map<String, Object>> fetchProjectAmountByType(@Param("query") ProjBase projBase);

    /**
     * 项目主体统计
     * @param projBase
     * @return
     */
    List<Map<String, Object>> fetchProjAmountBySubject(@Param("query") ProjBase projBase);

    /**
     * 子项产能汇总
     * @param projBase
     * @return
     */
    List<ProjectBomChild> fetchModelBySubject(@Param("query") ProjBase projBase);
}
