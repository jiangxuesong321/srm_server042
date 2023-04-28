package org.jeecg.modules.srm.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.srm.entity.BasWarehouse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 仓库
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface BasWarehouseMapper extends BaseMapper<BasWarehouse> {
    /**
     * 仓库列表
     * @param page
     * @param basWarehouse
     * @return
     */
    IPage<BasWarehouse> queryPageList(Page<BasWarehouse> page, @Param("query") BasWarehouse basWarehouse);

    /**
     * 导出
     * @param basWarehouse
     * @return
     */
    List<BasWarehouse> exportXls(@Param("query") BasWarehouse basWarehouse);
}
