package com.cmoc.modules.srm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.cmoc.modules.srm.entity.PurchaseApplyInvoice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: purchase_apply_invoice
 * @Author: jeecg-boot
 * @Date:   2023-02-15
 * @Version: V1.0
 */
public interface PurchaseApplyInvoiceMapper extends BaseMapper<PurchaseApplyInvoice> {
    /**
     * 累计开票金额
     * @param contractId
     * @param ids
     * @return
     */
    PurchaseApplyInvoice fetchDetailListByContractId(@Param("contractId") String contractId, @Param("ids") List<String> ids);

    /**
     * 本次开票金额
     * @param ids
     * @return
     */
    List<PurchaseApplyInvoice> fetchDetailListByApplyids(@Param("ids") List<String> ids);
}
