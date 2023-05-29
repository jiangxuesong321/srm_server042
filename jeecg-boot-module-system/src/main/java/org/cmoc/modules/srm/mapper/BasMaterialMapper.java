package org.cmoc.modules.srm.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.cmoc.modules.srm.entity.BasMaterial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cmoc.modules.srm.entity.BiddingRecord;

/**
 * @Description: 设备管理
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface BasMaterialMapper extends BaseMapper<BasMaterial> {
    /**
     * 每个分类的最大值
     * @return
     */
    List<BasMaterial> getMaxCode();

    /**
     * 报价历史
     * @param page
     * @param basMaterial
     * @return
     */
    IPage<BiddingRecord> fetchHistoryQuote(Page<BasMaterial> page, @Param("query") BasMaterial basMaterial);

    /**
     * 获取分类最大值
     * @param code
     * @return
     */
    List<BasMaterial> getMaxCodeByCode(@Param("code") String code);
}
