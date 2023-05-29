package org.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.shiro.SecurityUtils;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.common.system.vo.LoginUser;
import org.cmoc.modules.srm.entity.BiddingBarginRecord;
import org.cmoc.modules.srm.entity.BiddingRecord;
import org.cmoc.modules.srm.entity.BiddingRecordSupplier;
import org.cmoc.modules.srm.mapper.BiddingBarginRecordMapper;
import org.cmoc.modules.srm.service.IBiddingBarginRecordService;
import org.cmoc.modules.srm.service.IBiddingRecordSupplierService;
import org.cmoc.modules.srm.vo.FixBiddingPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description: bidding_bargin_record
 * @Author: jeecg-boot
 * @Date:   2022-11-30
 * @Version: V1.0
 */
@Service
public class BiddingBarginRecordServiceImpl extends ServiceImpl<BiddingBarginRecordMapper, BiddingBarginRecord> implements IBiddingBarginRecordService {
    @Autowired
    private IBiddingRecordSupplierService iBiddingRecordSupplierService;
    /**
     * 保存议价
     * @param page
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBargin(FixBiddingPage page) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        Date nowTime = new Date();

        //生成议价记录
        BiddingBarginRecord bargin = new BiddingBarginRecord();
        bargin.setId(String.valueOf(IdWorker.getId()));
        bargin.setQuoteRecordId(page.getQuoteRecordId());
        bargin.setSuppId(page.getSupplierId());
        bargin.setBiddingId(page.getBiddingId());
        bargin.setRecordId(page.getRecordId());
        bargin.setBrsId(page.getId());
        bargin.setDelFlag(CommonConstant.NO_READ_FLAG);
        bargin.setCreateTime(nowTime);
        bargin.setCreateUser(username);
        bargin.setUpdateTime(nowTime);
        bargin.setUpdateUser(username);
        bargin.setBgPriceTax(page.getBgPriceTax());
        bargin.setQty(page.getQty());
        this.save(bargin);

        //更新状态
        BiddingRecordSupplier rs = iBiddingRecordSupplierService.getById(page.getId());
        rs.setIsBargin("1");
        iBiddingRecordSupplierService.updateById(rs);
    }

    /**
     * 议价记录
     * @param id
     * @return
     */
    @Override
    public List<BiddingBarginRecord> fetchHistoryPrice(String id) {
        return baseMapper.fetchHistoryPrice(id);
    }

    /**
     * 历史价格
     * @param record
     * @return
     */
    @Override
    public List<BiddingBarginRecord> fetchPriceHistory(BiddingBarginRecord record) {
        return baseMapper.fetchPriceHistory(record);
    }
}
