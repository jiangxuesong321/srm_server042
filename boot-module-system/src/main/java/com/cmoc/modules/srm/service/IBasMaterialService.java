package com.cmoc.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.cmoc.common.api.vo.Result;
import com.cmoc.modules.srm.entity.BasMaterial;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmoc.modules.srm.entity.BiddingRecord;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 设备管理
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface IBasMaterialService extends IService<BasMaterial> {
    /**
     * 添加设备
     * @param basMaterial
     */
    void saveEntity(BasMaterial basMaterial) throws Exception;

    /**
     * 导入
     * @param request
     * @param response
     * @param basMaterialClass
     * @return
     */
    Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, Class<BasMaterial> basMaterialClass);

    /**
     * 历史报价计量
     * @param page
     * @param basMaterial
     * @return
     */
    IPage<BiddingRecord> fetchHistoryQuote(Page<BasMaterial> page, @Param("query") BasMaterial basMaterial);

    /**
     * 更新
     * @param basMaterial
     */
    void updateEntity(BasMaterial basMaterial);

    /**
     * 导出
     * @param request
     * @param basMaterial
     * @param basMaterialClass
     * @param title
     * @return
     */
    ModelAndView exportXls(HttpServletRequest request, BasMaterial basMaterial, Class<BasMaterial> basMaterialClass, String title);
}
