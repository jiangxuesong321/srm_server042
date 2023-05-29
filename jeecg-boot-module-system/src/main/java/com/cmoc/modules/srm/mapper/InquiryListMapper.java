package com.cmoc.modules.srm.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.cmoc.modules.srm.entity.BasSupplier;
import com.cmoc.modules.srm.entity.InquiryList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 询价单主表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface InquiryListMapper extends BaseMapper<InquiryList> {
    /**
     * 询比价
     * @param page
     * @param inquiryList
     * @return
     */
    IPage<InquiryList> queryPageList(Page<InquiryList> page, @Param("query") InquiryList inquiryList);

    /**
     * 合同生成
     * @param page
     * @param inquiryList
     * @return
     */
    IPage<InquiryList> queryPageToDetailList(Page<InquiryList> page, @Param("query") InquiryList inquiryList);

    /**
     * 询价供应商
     * @param id
     * @return
     */
    List<BasSupplier> fetchSuppList(@Param("id") String id);
}
