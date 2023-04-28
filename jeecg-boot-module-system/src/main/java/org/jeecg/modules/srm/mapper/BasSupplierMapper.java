package org.jeecg.modules.srm.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.srm.entity.BasSupplier;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 供应商基本信息
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface BasSupplierMapper extends BaseMapper<BasSupplier> {
    /**
     * 分页
     * @param page
     * @param basSupplier
     * @return
     */
    IPage<BasSupplier> pageList(Page<BasSupplier> page, @Param("query") BasSupplier basSupplier);

    /**
     * 供应商属性统计
     * @param basSupplier
     * @return
     */
    List<Map<String,Object>> fetchSuppCategory(@Param("query") BasSupplier basSupplier);

    /**
     * 供应商分类
     * @param basSupplier
     * @return
     */
    List<Map<String, Object>> fetchSuppType(@Param("query") BasSupplier basSupplier);

    /**
     * 供应商合同
     * @param basSupplier
     * @return
     */
    Map<String,Object> fetchSuppContract(@Param("query") BasSupplier basSupplier);

    /**
     * 执行中合同数量
     * @param basSupplier
     * @return
     */
    Map<String, Object> fetchSuppContracting(@Param("query") BasSupplier basSupplier);

    /**
     * 供应商状态
     * @param basSupplier
     * @return
     */
    List<Map<String, Object>> fetchSuppStatus(@Param("query") BasSupplier basSupplier);

    /**
     * 活跃供应商
     * @param basSupplier
     * @return
     */
    Map<String, Object> fetchSuppActive(@Param("query") BasSupplier basSupplier);
}
