package org.jeecg.modules.srm.service;

import org.jeecg.modules.srm.entity.BiddingRecordToProfessionals;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: bidding_requistion_relation
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface IBiddingRecordToProfessionalsService extends IService<BiddingRecordToProfessionals> {
    /**
     * 评标结果
     * @param id
     * @return
     */
    List<BiddingRecordToProfessionals> fetchResult(String id);
}
