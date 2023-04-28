package org.jeecg.modules.srm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.srm.entity.BiddingBarginRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: bidding_bargin_record
 * @Author: jeecg-boot
 * @Date:   2022-11-30
 * @Version: V1.0
 */
public interface BiddingBarginRecordMapper extends BaseMapper<BiddingBarginRecord> {
    /**
     * 议价记录
     * @param id
     * @return
     */
    List<BiddingBarginRecord> fetchHistoryPrice(@Param("id") String id);

    /**
     * 历史价格
     * @param record
     * @return
     */
    List<BiddingBarginRecord> fetchPriceHistory(@Param("query") BiddingBarginRecord record);
}
