package org.jeecg.modules.srm.service.impl;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.srm.entity.BiddingRecordToProfessionals;
import org.jeecg.modules.srm.mapper.BiddingRecordToProfessionalsMapper;
import org.jeecg.modules.srm.service.IBiddingRecordToProfessionalsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: bidding_requistion_relation
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Service
public class BiddingRecordToProfessionalsServiceImpl extends ServiceImpl<BiddingRecordToProfessionalsMapper, BiddingRecordToProfessionals> implements IBiddingRecordToProfessionalsService {
    /**
     * 评标结果
     * @param id
     * @return
     */
    @Override
    public List<BiddingRecordToProfessionals> fetchResult(String id) {
        return baseMapper.fetchResult(id);
    }
}
