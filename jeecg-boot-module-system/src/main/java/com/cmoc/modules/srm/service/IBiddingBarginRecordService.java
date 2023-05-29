package com.cmoc.modules.srm.service;

import com.cmoc.modules.srm.entity.BiddingBarginRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmoc.modules.srm.vo.FixBiddingPage;

import java.util.List;

/**
 * @Description: bidding_bargin_record
 * @Author: jeecg-boot
 * @Date:   2022-11-30
 * @Version: V1.0
 */
public interface IBiddingBarginRecordService extends IService<BiddingBarginRecord> {
    /**
     * 保存议价
     * @param biddingBarginRecord
     */
    void saveBargin(FixBiddingPage biddingBarginRecord);

    /**
     * 议价记录
     * @param id
     * @return
     */
    List<BiddingBarginRecord> fetchHistoryPrice(String id);

    /**
     * 历史价格
     * @param biddingBarginRecord
     * @return
     */
    List<BiddingBarginRecord> fetchPriceHistory(BiddingBarginRecord biddingBarginRecord);
}
