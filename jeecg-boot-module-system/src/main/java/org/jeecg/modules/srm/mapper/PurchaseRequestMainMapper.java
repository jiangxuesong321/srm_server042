package org.jeecg.modules.srm.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.srm.entity.PurchaseRequestMain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: purchase_request_main
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
public interface PurchaseRequestMainMapper extends BaseMapper<PurchaseRequestMain> {
    /**
     * 配套tab页
     * @param page
     * @param pmain
     * @return
     */
    IPage<PurchaseRequestMain> fetchChildList(Page<PurchaseRequestMain> page, @Param("query") PurchaseRequestMain pmain);

    /**
     * 以项目统计需求金额
     * @param purchaseRequestMain
     * @return
     */
    PurchaseRequestMain fetchRequestByProjId(@Param("query") PurchaseRequestMain purchaseRequestMain);

    /**
     * 分页查询
     * @param page
     * @param purchaseRequestMain
     * @return
     */
    IPage<PurchaseRequestMain> pageList(Page<PurchaseRequestMain> page, @Param("query") PurchaseRequestMain purchaseRequestMain);

    /**
     * 获取分类最大金额
     * @param purchaseRequestMain
     * @return
     */
    PurchaseRequestMain fetchMaxAmount(@Param("query") PurchaseRequestMain purchaseRequestMain);

    /**
     * 分页查询
     * @param page
     * @param purchaseRequestMain
     * @return
     */
    IPage<PurchaseRequestMain> queryPageList(Page<PurchaseRequestMain> page, @Param("query") PurchaseRequestMain purchaseRequestMain);

    IPage<PurchaseRequestMain> queryPageGoodsList(Page<PurchaseRequestMain> page, @Param("query") PurchaseRequestMain purchaseRequestMain);

    /**
     * 报价附件
     * @param purchaseRequestMain
     * @return
     */
    List<Map<String, String>> fetchQuoteFile(@Param("query") PurchaseRequestMain purchaseRequestMain);
}
