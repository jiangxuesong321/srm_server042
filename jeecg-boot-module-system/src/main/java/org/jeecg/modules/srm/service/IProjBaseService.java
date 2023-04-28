package org.jeecg.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.srm.entity.BasMaterial;
import org.jeecg.modules.srm.entity.ProjBase;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.srm.entity.ProjectBomChild;
import org.jeecg.modules.srm.entity.ProjectBomRelation;
import org.jeecg.modules.srm.vo.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: proj_base
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface IProjBaseService extends IService<ProjBase> {
    /**
     * 编辑
     * @param projBase
     */
    void editProjBase(ProjBase projBase) throws Exception;

    /**
     * 项目
     * @param projBase
     * @return
     */
    ProjBase fetchProjById(ProjBase projBase);

    /**
     * 分页
     * @param page
     * @param projBase
     * @return
     */
    IPage<ProjBase> pageList(Page<ProjBase> page, ProjBase projBase);

    /**
     * 新增
     * @param projBase
     */
    void addProjBase(ProjBase projBase) throws Exception;

    /**
     * 导入
     * @param request
     * @param response
     * @param clazz
     * @return
     */
    Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, Class<BasMaterialImport> clazz);

    /**
     * 项目总产能
     * @param projBase
     * @return
     */
    ProjBase fetchCapacityByProjId(ProjBase projBase);

    /**
     * 资金类型饼图(已付、合同金额)
     * @param projBase
     * @return
     */
    Map<String, List<Map<String, String>>> fetchCategoryAmount(ProjBase projBase);

    /**
     * 维护汇率
     * @param projBase
     */
    void updateRate(List<ProjBase> projBase);

    /**
     * 项目总投
     * @param projBase
     * @return
     */
    ProjBase fetchProjectAmount(ProjBase projBase);

    /**
     * 首页 - 金额支出情况
     * @param projBase
     * @return
     */
    Map<String, BigDecimal> fetchAmount(ProjBase projBase);

    /**
     * 首页 - 设备采购情况
     * @param projBase
     * @return
     */
    Map<String, BigDecimal> fetchQty(ProjBase projBase);

    /**
     * 项目总产能(合并同类项)
     * @param projBase
     * @return
     */
    List<ProjectBomRelation> fetchCapacity(ProjBase projBase);

    /**
     * 项目预算购置表
     * @param page
     * @param contractBase
     * @return
     */
    IPage<ProjBudget> fetchProjBudgetPageList(Page<ProjBudget> page, ProjBudget contractBase);

    /**
     * 项目预算购置表导出
     * @param request
     * @param contractBase
     * @param clazz
     * @param title
     * @return
     */
    void exportProjBudgetPageList(HttpServletRequest request, ProjBudget contractBase, Class<ProjBudget> clazz, String title,HttpServletResponse response);

    /**
     * 项目预算购置表汇总
     * @param contractBase
     * @return
     */
    ProjBudget fetchTotalProjBudgetPageList(ProjBudget contractBase);

    /**
     * 修改文件名
     * @param fileParam
     */
    String changeFileName(FileParam fileParam) throws Exception;

    /**
     * OA 项目立项
     * @param projBaseVo
     * @return
     */
    Result createProj(ProjBaseVo projBaseVo);

    /**
     * OA 项目变更
     * @param projBaseVo
     * @return
     */
    Result updateProj(ProjBaseVo projBaseVo);

    /**
     * 获取项目下得子项
     * @param projBase
     * @return
     */
    List<Map<String, String>> fetchModelByProjId(ProjBase projBase);

    /**
     * 项目数量统计
     * @param projBase
     * @return
     */
    Map<String, BigDecimal> fetchProjNum(ProjBase projBase);

    /**
     * 项目类型统计
     * @param projBase
     * @return
     */
    List<Map<String,Object>> fetchProjType(ProjBase projBase);

    /**
     * 项目地区
     * @param projBase
     * @return
     */
    List<Map<String, Object>> fetchProjArea(ProjBase projBase);

    /**
     * 设备模板导出
     * @param request
     * @param projBase
     * @param projectProdClass
     * @param title
     * @return
     */
    void exportXls(HttpServletRequest request, ProjBase projBase, Class<BasMaterialImport> projectProdClass, String title,HttpServletResponse response) throws IOException;

    /**
     * 项目列表导出
     * @param projBase
     * @return
     */
    List<ProjBase> exportXlsByList(ProjBase projBase);

    /**
     * 项目总投
     * @param projBase
     * @return
     */
    List<Map<String, Object>> fetchProjectAmountByType(ProjBase projBase);

    /**
     * 项目主体统计
     * @param projBase
     * @return
     */
    List<Map<String, Object>> fetchProjAmountBySubject(ProjBase projBase);

    /**
     *子项产能汇总
     * @param projBase
     * @return
     */
    List<ProjectBomChild> fetchModelBySubject(ProjBase projBase);
}
