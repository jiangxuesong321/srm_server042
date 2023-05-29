package com.cmoc.modules.srm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.util.DateUtils;
import com.cmoc.modules.srm.service.IContractBaseService;
import com.cmoc.modules.srm.service.IProjBaseService;
import com.cmoc.modules.srm.service.IPurPayPlanService;
import com.cmoc.modules.srm.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
* @Description: 合同基本信息表
* @Author: jeecg-boot
* @Date:   2022-06-21
* @Version: V1.0
*/
@RestController
@RequestMapping("/srm/report")
@Slf4j
public class ReportController {
    @Autowired
    private IContractBaseService iContractBaseService;
    @Autowired
    private IPurPayPlanService iPurPayPlanService;
    @Autowired
    private IProjBaseService iProjBaseService;
    /**
     * 项目合同进度报表
     *
     * @param contractBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchContractProgressPageList")
    public Result<IPage<ContractProgress>> fetchContractProgressPageList(ContractProgress contractBase,
                                                                         @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                         HttpServletRequest req) {
        Page<ContractProgress> page = new Page<ContractProgress>(pageNo, pageSize);
        IPage<ContractProgress> pageList = iContractBaseService.fetchContractProgressPageList(page, contractBase);
        return Result.OK(pageList);
    }

    /**
     * 项目合同进度报表导出
     *
     * @param contractBase
     * @return
     */
    @RequestMapping(value = "/exportContractProgressPageList")
    public ModelAndView exportContractProgressPageList(HttpServletRequest request, ContractProgress contractBase) {
        return iContractBaseService.exportContractProgressPageList(request, contractBase, ContractProgress.class, "项目合同进度报表");
    }


    /**
     * 付款流程节点报表
     *
     * @param contractBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchPayProgressPageList")
    public Result<IPage<PayProgress>> fetchPayProgressPageList(PayProgress contractBase,
                                                               @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                               HttpServletRequest req) {
        Page<PayProgress> page = new Page<PayProgress>(pageNo, pageSize);
        IPage<PayProgress> pageList = iPurPayPlanService.fetchPayProgressPageList(page, contractBase);
        return Result.OK(pageList);
    }

    /**
     * 付款流程节点报表导出
     *
     * @param contractBase
     * @return
     */
    @RequestMapping(value = "/exportPayProgressPageList")
    public ModelAndView exportPayProgressPageList(HttpServletRequest request, PayProgress contractBase) {
        return iPurPayPlanService.exportPayProgressPageList(request, contractBase, PayProgress.class, "付款流程节点报表");
    }

    /**
     * 项目预算购置表
     *
     * @param contractBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchProjBudgetPageList")
    public Result<IPage<ProjBudget>> fetchProjBudgetPageList(ProjBudget contractBase,
                                                             @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                             HttpServletRequest req) {
        Page<ProjBudget> page = new Page<ProjBudget>(pageNo, pageSize);
        IPage<ProjBudget> pageList = iProjBaseService.fetchProjBudgetPageList(page, contractBase);
        return Result.OK(pageList);
    }

    /**
     * 项目预算购置表汇总
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchTotalProjBudgetPageList")
    public Result<ProjBudget> fetchTotalProjBudgetPageList(ProjBudget contractBase,
                                                             HttpServletRequest req) {

        ProjBudget pageList = iProjBaseService.fetchTotalProjBudgetPageList(contractBase);
        return Result.OK(pageList);
    }

    /**
     * 项目预算购置表导出
     *
     * @param contractBase
     * @return
     */
    @RequestMapping(value = "/exportProjBudgetPageList")
    public void exportProjBudgetPageList(HttpServletRequest request, ProjBudget contractBase,HttpServletResponse response) {
        iProjBaseService.exportProjBudgetPageList(request, contractBase, ProjBudget.class, "项目预算购置表",response);
    }

    /**
     * 资金计划报表
     *
     * @param contractBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchAmountPlanPageList")
    public Result<IPage<AmountPlan>> fetchAmountPlanPageList(AmountPlan contractBase,
                                                             @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                             HttpServletRequest req) {
        Page<AmountPlan> page = new Page<AmountPlan>(pageNo, pageSize);
        IPage<AmountPlan> pageList = iPurPayPlanService.fetchAmountPlanPageList(page, contractBase);
        return Result.OK(pageList);
    }

    /**
     * 资金计划报表-分组统计
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchAmountByTypePlanPageList")
    public Result<List<AmountPlan>> fetchAmountByTypePlanPageList(AmountPlan contractBase,
                                                             HttpServletRequest req) {
        List<AmountPlan> pageList = iPurPayPlanService.fetchAmountByTypePlanPageList(contractBase);
        return Result.OK(pageList);
    }

    /**
     * 资金计划报表金额分组汇总
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchGroupAmountPlanPageList")
    public Result<Map<String,BigDecimal>> fetchGroupAmountPlanPageList(AmountPlan contractBase,
                                                             HttpServletRequest req) {

        Map<String,BigDecimal> pageList = iPurPayPlanService.fetchGroupAmountPlanPageList(contractBase);
        return Result.OK(pageList);
    }

    /**
     * 资金计划报表导出
     *
     * @param contractBase
     * @return
     */
    @GetMapping(value = "/exportAmountPlanPageList")
    public ModelAndView exportAmountPlanPageList(HttpServletRequest request, AmountPlan contractBase) {
        return iPurPayPlanService.exportAmountPlanPageList(request, contractBase, AmountPlan.class, "资金计划报表");
    }

    /**
     * 目标完成情况数据
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchPlanAmountByYear")
    public Result<List<AmountPlanToYear>> fetchPlanAmountByYear(AmountPlanToYear contractBase,
                                                                HttpServletRequest req) {
        List<AmountPlanToYear> pageList = iPurPayPlanService.fetchPlanAmountByYear(contractBase);
        return Result.OK(pageList);
    }

    /**
     * 目标完成情况数据导出
     *
     * @param contractBase
     * @return
     */
    @GetMapping(value = "/exportPlanAmountByYear")
    public ModelAndView exportPlanAmountByYear(HttpServletRequest request, AmountPlanToYear contractBase) {
        return iPurPayPlanService.exportPlanAmountByYear(request, contractBase, AmountPlanToYear.class, "目标完成情况数据");
    }

    /**
     * 月份返回接口
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchMonthList")
    public Result<List<String>> fetchMonthList(AmountPlanToYear contractBase,
                                                                HttpServletRequest req) {
        List<String> monthList = DateUtils.toDateList(contractBase.getStartMonth(),contractBase.getEndMonth());
        return Result.OK(monthList);
    }

    /**
     * 完成率&占比
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchPlanAmountRate")
    public Result<Map<String,Object>> fetchPlanAmountRate(AmountPlanToYear contractBase,
                                                                HttpServletRequest req) {
        Map<String,Object> pageList = iPurPayPlanService.fetchPlanAmountRate(contractBase);
        return Result.OK(pageList);
    }

    /**
     * 采购资金目标与完成分析
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchPlanAmountProgress")
    public Result<Map<String,Object>> fetchPlanAmountProgress(AmountPlanToYear contractBase,
                                                          HttpServletRequest req) {
        Map<String,Object> pageList = iPurPayPlanService.fetchPlanAmountProgress(contractBase);
        return Result.OK(pageList);
    }

    /**
     * 计划完成与目标差额分析
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchPlanAmountDiff")
    public Result<Map<String,Object>> fetchPlanAmountDiff(AmountPlanToYear contractBase,
                                                              HttpServletRequest req) {
        Map<String,Object> pageList = iPurPayPlanService.fetchPlanAmountDiff(contractBase);
        return Result.OK(pageList);
    }

    /**
     * 合同发票管理统计表
     *
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchAmountTotal")
    public Result<Map<String,BigDecimal>> fetchAmountTotal(@RequestParam Map<String,String> map,
                                                           HttpServletRequest req) {

        Map<String, BigDecimal> entity = iContractBaseService.fetchAmountTotal(map);
        return Result.OK(entity);
    }

    /**
     * 合同发票明细列表
     *
     * @param contractBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchContractToInvoice")
    public Result<IPage<ContractToInvoice>> fetchContractToInvoice(ContractToInvoice contractBase,
                                                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                   HttpServletRequest req) {
        Page<ContractToInvoice> page = new Page<ContractToInvoice>(pageNo, pageSize);
        IPage<ContractToInvoice> pageList = iContractBaseService.fetchContractToInvoice(page, contractBase);
        return Result.OK(pageList);
    }

    /**
     * 合同发票明细列表导出
     *
     * @param contractBase
     * @return
     */
    @GetMapping(value = "/exportContractToInvoice")
    public ModelAndView exportContractToInvoice(HttpServletRequest request, ContractToInvoice contractBase) {
        return iContractBaseService.exportContractToInvoice(request, contractBase, ContractToInvoice.class, "合同发票明细列表");
    }

    /**
     * 合同发票明细列表按月汇总
     *
     * @param contractBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchContractToInvoiceByMonth")
    public Result<List<ContractToInvoice>> fetchContractToInvoiceByMonth(ContractToInvoice contractBase,
                                                                         @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                         HttpServletRequest req) {
        List<ContractToInvoice> pageList = iContractBaseService.fetchContractToInvoiceByMonth(contractBase);
        return Result.OK(pageList);
    }

    /**
     * 年度项目费用明细表
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchProjAmountByMonth")
    public Result<List<AmountPlanToYear>> fetchProjAmountByMonth(AmountPlanToYear contractBase,
                                                                         HttpServletRequest req) {
        List<AmountPlanToYear> pageList = iPurPayPlanService.fetchProjAmountByMonth(contractBase);
        return Result.OK(pageList);
    }

    /**
     * 年度项目费用明细表 - 子项
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchProjChildAmountByMonth")
    public Result<List<AmountPlanToYear>> fetchProjChildAmountByMonth(AmountPlanToYear contractBase,
                                                                 HttpServletRequest req) {
        List<AmountPlanToYear> pageList = iPurPayPlanService.fetchProjChildAmountByMonth(contractBase);
        return Result.OK(pageList);
    }

    /**
     * 年度项目费用明细表导出
     *
     * @param contractBase
     * @return
     */
    @GetMapping(value = "/exportProjAmountByMonth")
    public ModelAndView exportProjAmountByMonth(HttpServletRequest request, AmountPlanToYear contractBase) {
        return iPurPayPlanService.exportProjAmountByMonth(request, contractBase, AmountPlanToYear.class, "年度项目费用明细表");
    }

    /**
     * 年度项目费用明细表
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchProjAmountBar")
    public Result<Map<String,Object>> fetchProjAmountBar(AmountPlanToYear contractBase,
                                                                 HttpServletRequest req) {
        Map<String,Object> pageList = iPurPayPlanService.fetchProjAmountBar(contractBase);
        return Result.OK(pageList);
    }

    /**
     * 年度项目费用明细表
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchProjAmountRound")
    public Result<List<Map<String,Object>>> fetchProjAmountRound(AmountPlanToYear contractBase,
                                                         HttpServletRequest req) {
        //近12个月
        if("twelve".equals(contractBase.getMonth())){
            List<String> monthList = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
            for(int i=0; i<12; i++) {
                cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1); //逐次往前推1个月
                monthList.add(String.valueOf(cal.get(Calendar.YEAR))
                        + "-" + (cal.get(Calendar.MONTH) + 1 < 10 ? "0" +
                        (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)));
            }
            Collections.sort(monthList);
            contractBase.setStartMonth(monthList.get(0));
            contractBase.setEndMonth(monthList.get(monthList.size() - 1));
        }
        List<Map<String,Object>> pageList = iPurPayPlanService.fetchProjAmountRound(contractBase);
        return Result.OK(pageList);
    }


    /**
     * 合同管理及支付明细报表
     *
     * @param param
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchContractAndPay")
    public Result<Map<String,Object>> fetchContractAndPay(AmountPlanToYear param,
                                                                 HttpServletRequest req) {
        Map<String,Object> pageList = iContractBaseService.fetchContractAndPay(param);
        return Result.OK(pageList);
    }

    /**
     * 合同管理及支付明细报表
     *
     * @param contractBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchContractAndPayPageList")
    public Result<?> fetchContractAndPayPageList(ContractAndPay contractBase,
                                                 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                 HttpServletRequest req) {

        Page<ContractAndPay> page = new Page<ContractAndPay>(pageNo, pageSize);
        IPage<ContractAndPay> pageList = iContractBaseService.fetchContractAndPayPageList(page, contractBase);
        return Result.OK(pageList);
    }

    /**
     * 合同管理及支付明细报表导出
     *
     * @param contractBase
     * @return
     */
    @GetMapping(value = "/exportContractAndPayPageList")
    public ModelAndView exportContractAndPayPageList(HttpServletRequest request, ContractAndPay contractBase) {
        return iContractBaseService.exportContractAndPayPageList(request, contractBase, ContractAndPay.class, "合同管理及支付明细报表");
    }
}
