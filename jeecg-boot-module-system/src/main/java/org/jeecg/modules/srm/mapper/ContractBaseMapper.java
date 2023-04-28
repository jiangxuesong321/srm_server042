package org.jeecg.modules.srm.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.srm.entity.ContractBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.srm.entity.ContractObject;
import org.jeecg.modules.srm.entity.PurPayPlan;
import org.jeecg.modules.srm.entity.PurchasePayInovice;
import org.jeecg.modules.srm.vo.ContractAndPay;
import org.jeecg.modules.srm.vo.ContractProgress;
import org.jeecg.modules.srm.vo.ContractToInvoice;

/**
 * @Description: 合同基本信息表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
public interface ContractBaseMapper extends BaseMapper<ContractBase> {
    /**
     * 合同列表
     * @param page
     * @param contractBase
     * @return
     */
    IPage<ContractBase> queryPageList(Page<ContractBase> page, @Param("query") ContractBase contractBase);

    /**
     * 以项目为单位统计合同总额
     * @param contractBase
     * @return
     */
    ContractBase fetchContractByProjId(@Param("query") ContractBase contractBase);

    /**
     * 合同清单
     * @param page
     * @param contractBase
     * @return
     */
    IPage<ContractBase> fetchContractListByProjId(Page<ContractBase> page, @Param("query") ContractBase contractBase);

    /**
     * 供应商合同信息
     * @param page
     * @param contractBase
     * @return
     */
    IPage<ContractBase> fetchContractBySupp(Page<ContractBase> page, @Param("query") ContractBase contractBase);

    /**
     * 项目合同进度报表
     * @param page
     * @param contractBase
     * @return
     */
    IPage<ContractProgress> fetchContractProgressPageList(Page<ContractProgress> page, @Param("query") ContractProgress contractBase);

    /**
     * 合同金额
     * @param map
     * @return
     */
    ContractObject fetchContractAmount(@Param("query") Map<String, String> map);

    /**
     * 开票金额
     * @param param
     * @return
     */
    PurchasePayInovice fetchPayInvoiceAmount(@Param("query") Map<String, String> param);

    /**
     * 普票
     * @param param
     * @return
     */
    PurchasePayInovice fetchPayNormalInvoiceAmount(@Param("query") Map<String, String> param);

    /**
     * 专票
     * @param param
     * @return
     */
    PurchasePayInovice fetchPaySpecialInvoiceAmount(@Param("query") Map<String, String> param);

    /**
     * 合同发票明细列表
     * @param page
     * @param contractBase
     * @return
     */
    IPage<ContractToInvoice> fetchContractToInvoice(Page<ContractToInvoice> page, @Param("query") ContractToInvoice contractBase);

    /**
     * 合同发票明细列表按月汇总
     * @param contractBase
     * @return
     */
    List<ContractToInvoice> fetchContractToInvoiceByMonth(@Param("query") ContractToInvoice contractBase);

    /**
     * 已收发票
     * @param param
     * @return
     */
    PurchasePayInovice fetchHasPayInvoiceAmount(@Param("query")  Map<String, String> param);

    /**
     * 合同签订金额
     * @param pam
     * @return
     */
    ContractObject fetchContractSignAmount(@Param("query") Map<String, String> pam);

    /**
     * 付款金额
     * @param pam
     * @return
     */
    PurPayPlan fetchPayAmount(@Param("query") Map<String, String> pam);

    /**
     * 合同管理及支付明细报表
     * @param page
     * @param contractBase
     * @return
     */
    IPage<ContractAndPay> fetchContractAndPayPageList(Page<ContractAndPay> page, @Param("query") ContractAndPay contractBase);

    /**
     * 付款明细(合同、子项分组)
     * @param contractBase
     * @return
     */
    List<ContractAndPay> fetchPayAmountList(@Param("query") ContractAndPay contractBase);

    /**
     * 项目合同进度报表导出
     * @param contractBase
     * @return
     */
    List<ContractProgress> exportContractProgressPageList(@Param("query") ContractProgress contractBase);

    /**
     * 合同发票明细列表导出
     * @param contractBase
     * @return
     */
    List<ContractToInvoice> exportContractToInvoice(@Param("query") ContractToInvoice contractBase);

    /**
     * 合同管理及支付明细报表导出
     * @param contractBase
     * @return
     */
    List<ContractAndPay> exportContractAndPayPageList(@Param("query") ContractAndPay contractBase);

    /**
     * 导出
     * @param contractBase
     * @return
     */
    List<ContractBase> queryExportList(@Param("query") ContractBase contractBase);

    /**
     * 合同总额
     * @param contractBase
     * @return
     */
    List<Map<String, Object>> fetchContractByProjType(@Param("query") ContractBase contractBase);

    /**
     * 合同分类统计金额
     * @param contractBase
     * @return
     */
    List<Map<String, Object>> fetchContractAmountByType(@Param("query") ContractBase contractBase);

    /**
     * 合同数量
     * @param contractBase
     * @return
     */
    Map<String, Object> fetchContractNum(@Param("query") ContractBase contractBase);

    /**
     * 每个月项目数量
     * @param contractBase
     * @return
     */
    List<ContractBase> fetchContractNumByMonth(@Param("query") ContractBase contractBase);

    /**
     * 合同台套数
     * @param contractBase
     * @return
     */
    Map<String,Object> fetchQtyNum(@Param("query") ContractBase contractBase);

    /**
     * 合同金额
     * @param contractBase
     * @return
     */
    Map<String, Object> fetchContractAmountTaxLocal(@Param("query") ContractBase contractBase);

    /**
     * 已付金额
     * @param contractBase
     * @return
     */
    Map<String, Object> fetchPayAmountTaxLocal(@Param("query") ContractBase contractBase);

    /**
     * 已开票金额
     * @param contractBase
     * @return
     */
    Map<String, Object> fetchInvoiceAmountTaxLocal(@Param("query") ContractBase contractBase);

    /**
     * 子项
     * @param contractBase
     * @return
     */
    List<ContractObject> fetchChildList(@Param("query") ContractBase contractBase);

    /**
     * 子项
     * @param contractBase
     * @return
     */
    List<ContractObject> fetchChildQtyList(@Param("query") ContractBase contractBase);
}
