package org.cmoc.modules.srm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.common.exception.JeecgBootException;
import org.cmoc.common.system.vo.LoginUser;
import org.cmoc.common.util.FillRuleUtil;
import org.cmoc.modules.srm.entity.ContractObjectQty;
import org.cmoc.modules.srm.entity.StkIoBillEntry;
import org.cmoc.modules.srm.entity.StkReturnBill;
import org.cmoc.modules.srm.entity.StkReturnBillEntry;
import org.cmoc.modules.srm.mapper.StkReturnBillMapper;
import org.cmoc.modules.srm.service.IContractObjectQtyService;
import org.cmoc.modules.srm.service.IStkReturnBillEntryService;
import org.cmoc.modules.srm.service.IStkReturnBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: stk_return_bill
 * @Author: jeecg-boot
 * @Date:   2022-10-10
 * @Version: V1.0
 */
@Service
public class StkReturnBillServiceImpl extends ServiceImpl<StkReturnBillMapper, StkReturnBill> implements IStkReturnBillService {

    @Autowired
    private IStkReturnBillEntryService iStkReturnBillEntryService;
    @Autowired
    private IContractObjectQtyService iContractObjectQtyService;

    /**
     * 退货
     * @param stkReturnBill
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(StkReturnBill stkReturnBill) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        Date nowTime = new Date();

        JSONObject formData = new JSONObject();
        formData.put("prefix", "RT");
        String code = (String) FillRuleUtil.executeRule("return_code", formData);

        stkReturnBill.setCode(code);
        stkReturnBill.setCreateTime(nowTime);
        stkReturnBill.setCreateBy(username);
        stkReturnBill.setUpdateBy(username);
        stkReturnBill.setUpdateTime(nowTime);
        stkReturnBill.setDelFlag(CommonConstant.NO_READ_FLAG);
        stkReturnBill.setStatus("0");
        stkReturnBill.setApproverId("project_center");
        stkReturnBill.setVersion(0);
        this.save(stkReturnBill);

        List<String> ids = new ArrayList<>();
        List<StkReturnBillEntry> entryList = stkReturnBill.getDetailList();
        //更新库存
        for(StkReturnBillEntry entity:entryList) {
            //外键设置
            entity.setId(String.valueOf(IdWorker.getId()));
            entity.setMid(stkReturnBill.getId());
            entity.setCreateTime(nowTime);
            entity.setCreateBy(username);
            entity.setUpdateBy(username);
            entity.setUpdateTime(nowTime);
            entity.setDelFlag(CommonConstant.NO_READ_FLAG);
            entity.setQty(entity.getQty());

            ids.add(entity.getBillDetailId());
        }
        iStkReturnBillEntryService.saveBatch(entryList);

        //更新退货数量
        UpdateWrapper<ContractObjectQty> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("to_return_qty",1);
        updateWrapper.in("id",ids);
        iContractObjectQtyService.update(updateWrapper);
    }

    /**
     * 编辑
     * @param stkReturnBill
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editMain(StkReturnBill stkReturnBill) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        Date nowTime = new Date();

        stkReturnBill.setUpdateBy(username);
        stkReturnBill.setUpdateTime(nowTime);
        stkReturnBill.setDelFlag(CommonConstant.NO_READ_FLAG);
        stkReturnBill.setStatus("0");
        stkReturnBill.setApproverId("project_center");
        stkReturnBill.setVersion(stkReturnBill.getVersion() + 1);
        this.updateById(stkReturnBill);

        //1.先删除子表数据
        iStkReturnBillEntryService.remove(Wrappers.<StkReturnBillEntry>query().lambda().eq(StkReturnBillEntry :: getMid,stkReturnBill.getId()));

        List<String> ids = new ArrayList<>();
        List<StkReturnBillEntry> entryList = stkReturnBill.getDetailList();
        for(StkReturnBillEntry entity:entryList) {
            //外键设置
            entity.setMid(stkReturnBill.getId());
            entity.setCreateTime(nowTime);
            entity.setCreateBy(username);
            entity.setUpdateBy(username);
            entity.setUpdateTime(nowTime);
            entity.setDelFlag(CommonConstant.NO_READ_FLAG);
            entity.setQty(entity.getQty());

            ids.add(entity.getBillDetailId());
        }
        iStkReturnBillEntryService.saveBatch(entryList);

        //更新退货数量
        UpdateWrapper<ContractObjectQty> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("to_return_qty",1);
        updateWrapper.in("id",ids);
        iContractObjectQtyService.update(updateWrapper);
    }

    /**
     * 明细
     * @param id
     * @return
     */
    @Override
    public List<StkIoBillEntry> queryDetailListByMainId(String id) {
        return baseMapper.queryDetailListByMainId(id);
    }

    /**
     * 分页
     * @param page
     * @param stkReturnBill
     * @return
     */
    @Override
    public IPage<StkReturnBill> queryPageList(Page<StkReturnBill> page, StkReturnBill stkReturnBill) {
        return baseMapper.queryPageList(page,stkReturnBill);
    }
}
