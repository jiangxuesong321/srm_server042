package com.cmoc.modules.srm.service;

import com.cmoc.modules.srm.entity.PurchaseApplyInvoice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: purchase_apply_invoice
 * @Author: jeecg-boot
 * @Date:   2023-02-15
 * @Version: V1.0
 */
public interface IPurchaseApplyInvoiceService extends IService<PurchaseApplyInvoice> {
    /**
     * 累计开票金额
     * @param contractId
     * @param ids
     * @return
     */
    PurchaseApplyInvoice fetchDetailListByContractId(String contractId, List<String> ids);

    /**
     * 本次开票金额
     * @param ids
     * @return
     */
    List<PurchaseApplyInvoice> fetchDetailListByApplyids(List<String> ids);
}
