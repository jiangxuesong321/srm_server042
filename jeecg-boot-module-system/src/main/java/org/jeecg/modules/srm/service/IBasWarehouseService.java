package org.jeecg.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.srm.entity.BasWarehouse;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 仓库
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface IBasWarehouseService extends IService<BasWarehouse> {
    /**
     * 仓库列表
     * @param page
     * @param basWarehouse
     * @return
     */
    IPage<BasWarehouse> queryPageList(Page<BasWarehouse> page, BasWarehouse basWarehouse);

    /**
     * 导出
     * @param request
     * @param basWarehouse
     * @param basWarehouseClass
     * @param title
     * @return
     */
    ModelAndView exportXls(HttpServletRequest request, BasWarehouse basWarehouse, Class<BasWarehouse> basWarehouseClass, String title);
}
