package org.cmoc.modules.srm.service.impl;

import org.cmoc.modules.srm.entity.PurchaseApplyInvoice;
import org.cmoc.modules.srm.mapper.PurchaseApplyInvoiceMapper;
import org.cmoc.modules.srm.service.IPurchaseApplyInvoiceService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: purchase_apply_invoice
 * @Author: jeecg-boot
 * @Date:   2023-02-15
 * @Version: V1.0
 */
@Service
public class PurchaseApplyInvoiceServiceImpl extends ServiceImpl<PurchaseApplyInvoiceMapper, PurchaseApplyInvoice> implements IPurchaseApplyInvoiceService {
    /**
     * 累计开票金额
     * @param contractId
     * @param ids
     * @return
     */
    @Override
    public PurchaseApplyInvoice fetchDetailListByContractId(String contractId, List<String> ids) {
        return baseMapper.fetchDetailListByContractId(contractId,ids);
    }

    /**
     * 本次开票金额
     * @param ids
     * @return
     */
    @Override
    public List<PurchaseApplyInvoice> fetchDetailListByApplyids(List<String> ids) {
        return baseMapper.fetchDetailListByApplyids(ids);
    }
}
