package com.cmoc.modules.srm.mapper;

import org.apache.ibatis.annotations.Param;
import com.cmoc.modules.srm.entity.BiddingRecordToProfessionals;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Description: bidding_requistion_relation
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface BiddingRecordToProfessionalsMapper extends BaseMapper<BiddingRecordToProfessionals> {
    /**
     * 评标结果
     * @param id
     * @return
     */
    List<BiddingRecordToProfessionals> fetchResult(@Param("id") String id);
}
