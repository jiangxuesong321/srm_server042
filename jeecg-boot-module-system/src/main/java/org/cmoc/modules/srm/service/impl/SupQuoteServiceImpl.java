package org.cmoc.modules.srm.service.impl;

import org.cmoc.modules.srm.entity.SupQuote;
import org.cmoc.modules.srm.mapper.SupQuoteMapper;
import org.cmoc.modules.srm.service.ISupQuoteService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: sup_quote
 * @Author: jeecg-boot
 * @Date:   2022-09-27
 * @Version: V1.0
 */
@Service
public class SupQuoteServiceImpl extends ServiceImpl<SupQuoteMapper, SupQuote> implements ISupQuoteService {
    /**
     * 历史报价
     * @param supQuote
     * @return
     */
    @Override
    public List<SupQuote> fetchPriceHistory(SupQuote supQuote) {
        return baseMapper.fetchPriceHistory(supQuote);
    }

    /**
     * 议价记录
     * @return
     */
    @Override
    public List<SupQuote> fetchPriceHistoryById(List<String> ids) {
        return baseMapper.fetchPriceHistoryById(ids);
    }
}
