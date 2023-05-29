package com.cmoc.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.cmoc.common.api.vo.Result;
import com.cmoc.modules.srm.entity.ContractBase;
import com.cmoc.modules.srm.entity.PurPayPlanDetail;
import com.cmoc.modules.srm.entity.PurPayPlan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmoc.modules.srm.vo.AmountPlan;
import com.cmoc.modules.srm.vo.AmountPlanToYear;
import com.cmoc.modules.srm.vo.PayProgress;
import com.cmoc.modules.srm.vo.ProcessVo;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 付款计划
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
public interface IPurPayPlanService extends IService<PurPayPlan> {

	/**
	 * 添加一对多
	 *
	 * @param purPayPlan
	 * @param purPayPlanDetailList
	 */
	public void saveMain(PurPayPlan purPayPlan,List<PurPayPlanDetail> purPayPlanDetailList) throws UnsupportedEncodingException, Exception;
	
	/**
	 * 修改一对多
	 *
   * @param purPayPlan
   * @param purPayPlanDetailList
	 */
	public void updateMain(PurPayPlan purPayPlan,List<PurPayPlanDetail> purPayPlanDetailList);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	/**
	 * 币种汇总
	 * @param purPayPlan
	 * @return
	 */
    PurPayPlan getTotalAmountByCurrency(PurPayPlan purPayPlan);

	/**
	 * 以项目为单位统计已付款金额
	 * @param purPayPlan
	 * @return
	 */
	PurPayPlan fetchPayAmountByProjId(PurPayPlan purPayPlan);

	/**
	 * 分页查询
	 * @param page
	 * @param purPayPlan
	 * @return
	 */
    IPage<PurPayPlan> queryPageList(Page<PurPayPlan> page, PurPayPlan purPayPlan);

	/**
	 * 首页 - 资金计划
	 * @param purPayPlan
	 * @return
	 */
    List<Map<String, Object>> fetchPlanAmount(PurPayPlan purPayPlan);

	/**
	 * 付款详情
	 * @param page
	 * @param purPayPlan
	 * @return
	 */
    IPage<PurPayPlan> fetchPageList(Page<PurPayPlan> page, PurPayPlan purPayPlan);

	/**
	 * 付款流程进度
	 * @param page
	 * @param contractBase
	 * @return
	 */
    IPage<PayProgress> fetchPayProgressPageList(Page<PayProgress> page, PayProgress contractBase);

	/**
	 * 目标完成情况数据
	 * @param contractBase
	 * @return
	 */
    List<AmountPlanToYear> fetchPlanAmountByYear(AmountPlanToYear contractBase);

	/**
	 * 完成率&占比
	 * @param contractBase
	 * @return
	 */
	Map<String,Object> fetchPlanAmountRate(AmountPlanToYear contractBase);

	/**
	 * 采购资金目标与完成分析
	 * @param contractBase
	 * @return
	 */
	Map<String, Object> fetchPlanAmountProgress(AmountPlanToYear contractBase);

	/**
	 * 一季度差异
	 * @param contractBase
	 * @return
	 */
	Map<String, Object> fetchPlanAmountDiff(AmountPlanToYear contractBase);

	/**
	 * 年度项目费用明细表
	 * @param contractBase
	 * @return
	 */
    List<AmountPlanToYear> fetchProjAmountByMonth(AmountPlanToYear contractBase);

	/**
	 * 月份各项目分析图
	 * @param contractBase
	 * @return
	 */
	Map<String, Object> fetchProjAmountBar(AmountPlanToYear contractBase);

	/**
	 * 全年项目分析图
	 * @param contractBase
	 * @return
	 */
	List<Map<String, Object>> fetchProjAmountRound(AmountPlanToYear contractBase);

	/**
	 * 资金计划报表
	 * @param page
	 * @param contractBase
	 * @return
	 */
    IPage<AmountPlan> fetchAmountPlanPageList(Page<AmountPlan> page, AmountPlan contractBase);

	/**
	 * 付款流程节点报表导出
	 * @param request
	 * @param contractBase
	 * @param payProgressClass
	 * @param title
	 * @return
	 */
    ModelAndView exportPayProgressPageList(HttpServletRequest request, PayProgress contractBase, Class<PayProgress> payProgressClass, String title);

	/**
	 * 资金计划报表导出
	 * @param request
	 * @param contractBase
	 * @param clazz
	 * @param title
	 * @return
	 */
	ModelAndView exportAmountPlanPageList(HttpServletRequest request, AmountPlan contractBase, Class<AmountPlan> clazz, String title);

	/**
	 * 目标完成情况数据导出
	 * @param request
	 * @param contractBase
	 * @param clazz
	 * @param title
	 * @return
	 */
    ModelAndView exportPlanAmountByYear(HttpServletRequest request, AmountPlanToYear contractBase, Class<AmountPlanToYear> clazz, String title);

	/**
	 * 年度项目费用明细表导出
	 * @param request
	 * @param contractBase
	 * @param clazz
	 * @param title
	 * @return
	 */
	ModelAndView exportProjAmountByMonth(HttpServletRequest request, AmountPlanToYear contractBase, Class<AmountPlanToYear> clazz, String title);

	/**
	 * 资金计划报表金额分组汇总
	 * @param contractBase
	 * @return
	 */
	Map<String,BigDecimal> fetchGroupAmountPlanPageList(AmountPlan contractBase);

	/**
	 * 发起OA审批
	 * @param purPayPlan
	 */
    void toOa(PurPayPlan purPayPlan) throws Exception;

	/**
	 * 审批进度
	 * @param processVo
	 * @return
	 */
    Result processSpeed(ProcessVo processVo);

	/**
	 * 年度项目费用明细表 - 子项
	 * @param contractBase
	 * @return
	 */
    List<AmountPlanToYear> fetchProjChildAmountByMonth(AmountPlanToYear contractBase);

	/**
	 * 资金计划 - 分组统计
	 * @param contractBase
	 * @return
	 */
    List<AmountPlan> fetchAmountByTypePlanPageList(AmountPlan contractBase);

	/**
	 * 付款计划导出
	 * @param purPayPlan
	 * @return
	 */
    List<PurPayPlan> queryList(PurPayPlan purPayPlan);

	/**
	 * 按照项目类型统计金额
	 * @param purPayPlan
	 * @return
	 */
	List<Map<String,Object>> fetchProjTypeAmountByProjId(@Param("query") PurPayPlan purPayPlan);

	/**
	 * 每个月付款金额
	 * @param contractBase
	 * @return
	 */
    Map<String, Object> fetchPayAmountByMonth(ContractBase contractBase);
}
