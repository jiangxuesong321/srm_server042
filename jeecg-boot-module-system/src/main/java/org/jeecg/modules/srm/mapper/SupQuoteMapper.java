package org.jeecg.modules.srm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.srm.entity.SupQuote;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: sup_quote
 * @Author: jeecg-boot
 * @Date:   2022-09-27
 * @Version: V1.0
 */
public interface SupQuoteMapper extends BaseMapper<SupQuote> {
    /**
     * 历史报价
     * @param supQuote
     * @return
     */
    List<SupQuote> fetchPriceHistory(@Param("query") SupQuote supQuote);

    /**
     * 议价记录
     * @return
     */
    List<SupQuote> fetchPriceHistoryById(@Param("ids") List<String> ids);
}
