package com.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.shiro.SecurityUtils;
import com.cmoc.common.constant.CommonConstant;
import com.cmoc.common.system.vo.LoginUser;
import com.cmoc.modules.srm.entity.InquirySupplier;
import com.cmoc.modules.srm.entity.SupBargain;
import com.cmoc.modules.srm.mapper.SupBargainMapper;
import com.cmoc.modules.srm.service.IInquirySupplierService;
import com.cmoc.modules.srm.service.ISupBargainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: sup_bargain
 * @Author: jeecg-boot
 * @Date:   2022-09-28
 * @Version: V1.0
 */
@Service
public class SupBargainServiceImpl extends ServiceImpl<SupBargainMapper, SupBargain> implements ISupBargainService {
    @Autowired
    private IInquirySupplierService inquirySupplierService;

    /**
     * 议价
     * @param supBargain
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBargain(SupBargain supBargain) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        Date nowTime = new Date();

        //更新 询价供应商状态
        String ispId = supBargain.getId();
        InquirySupplier supp = inquirySupplierService.getById(ispId);
        //中标之后在次议价
        if("4".equals(supp.getStatus())){
            supp.setIsBargin("1");
        }
        //进行中议价
        else{
            supp.setStatus("2");
        }
        inquirySupplierService.updateById(supp);

        //保存议价信息
        String id = String.valueOf(IdWorker.getId());
        SupBargain entity = new SupBargain();
        entity.setId(id);
        entity.setSuppId(supp.getSupplierId());
        entity.setQuoteId(supBargain.getQuoteId());
        entity.setRecordId(supBargain.getRecordId());
        entity.setDelFlag(CommonConstant.NO_READ_FLAG);
        entity.setCreateTime(nowTime);
        entity.setCreateUser(username);
        entity.setUpdateTime(nowTime);
        entity.setUpdateUser(username);
        entity.setQty(supBargain.getQty());
        entity.setTaxRate(supBargain.getTaxRate());
        //议价运费
        entity.setFareAmount(supBargain.getBgFareAmount());
        //议价含税单价
        entity.setOrderPriceTax(supBargain.getBgOrderPriceTax());
        BigDecimal orderPriceTax = entity.getOrderPriceTax();
        //数量
        BigDecimal qty = entity.getQty();
        //议价含税总额
        BigDecimal orderAmountTax = orderPriceTax.multiply(qty);
        entity.setOrderAmountTax(orderAmountTax);

        //税率
        BigDecimal taxRate = entity.getTaxRate();
        if(taxRate.compareTo(new BigDecimal(100)) == 0){
            taxRate = BigDecimal.ZERO;
        }
        taxRate = taxRate.divide(new BigDecimal(100)).add(new BigDecimal(1));

        //议价未税总额
        BigDecimal orderAmount = orderAmountTax.divide(taxRate,2,BigDecimal.ROUND_HALF_UP);
        entity.setOrderAmount(orderAmount);

        //议价未税单价
        BigDecimal orderPrice = orderAmount.divide(qty,4,BigDecimal.ROUND_HALF_UP);
        entity.setOrderPrice(orderPrice);

        this.save(entity);

    }

    /**
     * 批量议价
     * @param supBargains
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBargainAll(List<SupBargain> supBargains) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        Date nowTime = new Date();

        List<String> ispIds = new ArrayList<>();
        List<SupBargain> entityList = new ArrayList<>();
        for(SupBargain sb : supBargains){
            ispIds.add(sb.getId());

            //保存议价信息
            String id = String.valueOf(IdWorker.getId());
            SupBargain entity = new SupBargain();
            entity.setId(id);
            entity.setSuppId(sb.getSuppId());
            entity.setQuoteId(sb.getQuoteId());
            entity.setRecordId(sb.getRecordId());
            entity.setDelFlag(CommonConstant.NO_READ_FLAG);
            entity.setCreateTime(nowTime);
            entity.setCreateUser(username);
            entity.setUpdateTime(nowTime);
            entity.setUpdateUser(username);
            entity.setQty(sb.getQty());
            entity.setTaxRate(sb.getTaxRate());
            //议价运费
            entity.setFareAmount(sb.getBgFareAmount());
            //议价含税单价
            entity.setOrderPriceTax(sb.getBgOrderPriceTax());
            BigDecimal orderPriceTax = entity.getOrderPriceTax();
            //数量
            BigDecimal qty = entity.getQty();
            //议价含税总额
            BigDecimal orderAmountTax = orderPriceTax.multiply(qty);
            entity.setOrderAmountTax(orderAmountTax);

            //税率
            BigDecimal taxRate = entity.getTaxRate();
            if(taxRate.compareTo(new BigDecimal(100)) == 0){
                taxRate = BigDecimal.ZERO;
            }
            taxRate = taxRate.divide(new BigDecimal(100)).add(new BigDecimal(1));

            //议价未税总额
            BigDecimal orderAmount = orderAmountTax.divide(taxRate,2,BigDecimal.ROUND_HALF_UP);
            entity.setOrderAmount(orderAmount);

            //议价未税单价
            BigDecimal orderPrice = orderAmount.divide(qty,4,BigDecimal.ROUND_HALF_UP);
            entity.setOrderPrice(orderPrice);
            entityList.add(entity);
        }
        //更新 询价供应商状态
        UpdateWrapper<InquirySupplier> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id",ispIds);
        updateWrapper.set("status","2");
        inquirySupplierService.update(updateWrapper);

        //保存议价信息
        this.saveBatch(entityList);
    }
}
