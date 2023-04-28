package org.jeecg.modules.srm.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.srm.entity.ContractBase;
import org.jeecg.modules.srm.entity.PurPayApply;
import org.jeecg.modules.srm.entity.PurPayPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.srm.entity.PurPayPlanDetail;
import org.jeecg.modules.srm.vo.AmountPlan;
import org.jeecg.modules.srm.vo.AmountPlanToYear;
import org.jeecg.modules.srm.vo.PayProgress;

/**
 * @Description: 付款计划
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
public interface PurPayPlanMapper extends BaseMapper<PurPayPlan> {
    /**
     * 币种汇总
     * @param purPayPlan
     * @return
     */
    PurPayPlan getTotalAmountByCurrency(@Param("query") PurPayPlan purPayPlan);

    /**
     * 以项目为单位统计已付款金额
     * @param purPayPlan
     * @return
     */
    PurPayPlan fetchPayAmountByProjId(@Param("query") PurPayPlan purPayPlan);

    /**
     * 分页
     * @param page
     * @param purPayPlan
     * @return
     */
    IPage<PurPayPlan> queryPageList(Page<PurPayPlan> page, @Param("query") PurPayPlan purPayPlan);

    /**
     * 每个月的资金计划
     * @param purPayPlan
     * @return
     */
    List<PurPayPlan> fetchPlanAmount(@Param("query") PurPayPlan purPayPlan);

    /**
     * 付款详情
     * @param page
     * @param purPayPlan
     * @return
     */
    IPage<PurPayPlan> fetchPageList(Page<PurPayPlan> page, @Param("query") PurPayPlan purPayPlan);

    /**
     * 付款流程节点报表
     * @param page
     * @param contractBase
     * @return
     */
    IPage<PayProgress> fetchPayProgressPageList(Page<PayProgress> page, @Param("query") PayProgress contractBase);

    /**
     * 按年统计每个月的申请数量
     * @param param
     * @return
     */
    List<Map<String, Object>> fetchPlanByYear(@Param("query") AmountPlanToYear param);

    /**
     * 按年统计每个月的完成数量
     * @param param
     * @return
     */
    List<Map<String, Object>> fetchCompletePlanByYear(@Param("query") AmountPlanToYear param);

    /**
     * 年度项目费用明细表
     * @param param
     * @return
     */
    List<AmountPlanToYear> fetchProjAmountByMonth(@Param("query") AmountPlanToYear param);

    /**
     * 月份各项目分析图
     * @param param
     * @return
     */
    List<AmountPlanToYear> fetchProjAmountBar(@Param("query") AmountPlanToYear param);

    /**
     * 全年项目分析图
     * @param param
     * @return
     */
    List<Map<String, Object>> fetchProjAmountRound(@Param("query") AmountPlanToYear param);

    /**
     * 资金计划报表
     * @param contractBase
     * @return
     */
    IPage<AmountPlan> fetchAmountPlanPageList(Page<AmountPlan> page,@Param("query") AmountPlan contractBase);

    /**
     * 付款流程节点报表导出
     * @param contractBase
     * @return
     */
    List<PayProgress> exportPayProgressPageList(@Param("query") PayProgress contractBase);

    /**
     * 资金计划报表导出
     * @param contractBase
     * @return
     */
    List<AmountPlan> exportAmountPlanPageList(@Param("query") AmountPlan contractBase);

    /**
     * 年度项目费用明细表
     * @param param
     * @return
     */
    List<AmountPlanToYear> fetchPlanProjAmountByMonth(@Param("query") AmountPlanToYear param);

    /**
     * 资金计划报表金额分组汇总
     * @param contractBase
     * @return
     */
    List<PurPayApply> fetchGroupAmountPlanPageList(@Param("query") AmountPlan contractBase);

    /**
     * 付款明细
     * @param purPayPlan
     * @return
     */
    List<PurPayPlanDetail> fetchDetailList(@Param("query") PurPayPlan purPayPlan);

    /**
     * 计划金额
     * @param param
     * @return
     */
    List<AmountPlanToYear> fetchPlanProjAmount(@Param("query") AmountPlanToYear param);

    /**
     * 计划付款金额
     * @param param
     * @return
     */
    List<AmountPlanToYear> fetchPlanProjChildAmountByMonth(@Param("query") AmountPlanToYear param);

    /**
     * 付款金额
     * @param param
     * @return
     */
    List<AmountPlanToYear> fetchProjChildAmountByMonth(@Param("query") AmountPlanToYear param);

    /**
     * 资金计划 - 币种汇总
     * @param contractBase
     * @return
     */
    List<AmountPlan> fetchAmountPlanByCurrency(@Param("query") AmountPlan contractBase);

    /**
     * 资金计划 - 支付方式汇总
     * @param contractBase
     * @return
     */
    List<AmountPlan> fetchAmountPlanByPayMethod(@Param("query") AmountPlan contractBase);

    /**
     * 资金计划 - 地区
     * @param contractBase
     * @return
     */
    List<AmountPlan> fetchAmountPlanByArea(@Param("query") AmountPlan contractBase);

    /**
     * 付款计划
     * @param purPayPlan
     * @return
     */
    List<PurPayPlan> queryList(@Param("query") PurPayPlan purPayPlan);

    /**
     * 按照项目类型统计金额
     * @param purPayPlan
     * @return
     */
    List<Map<String,Object>> fetchProjTypeAmountByProjId(@Param("query") PurPayPlan purPayPlan);

    /**
     *
     * @param contractBase
     * @return
     */
    List<PurPayPlan> fetchPayAmountByMonth(@Param("query") ContractBase contractBase);
}
