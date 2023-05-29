package com.cmoc.modules.srm.service;

import com.cmoc.modules.srm.entity.SupQuote;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: sup_quote
 * @Author: jeecg-boot
 * @Date:   2022-09-27
 * @Version: V1.0
 */
public interface ISupQuoteService extends IService<SupQuote> {
    /**
     * 历史报价
     * @param supQuote
     * @return
     */
    List<SupQuote> fetchPriceHistory(SupQuote supQuote);

    /**
     * 议价记录
     * @return
     */
    List<SupQuote> fetchPriceHistoryById(List<String> ids);
}
