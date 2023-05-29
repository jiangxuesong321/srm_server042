package com.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import com.cmoc.common.system.vo.LoginUser;
import com.cmoc.modules.srm.entity.BasWarehouse;
import com.cmoc.modules.srm.mapper.BasWarehouseMapper;
import com.cmoc.modules.srm.service.IBasWarehouseService;
import com.cmoc.modules.srm.utils.JeecgEntityExcel;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 仓库
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Service
public class BasWarehouseServiceImpl extends ServiceImpl<BasWarehouseMapper, BasWarehouse> implements IBasWarehouseService {
    @Value("${jeecg.path.upload}")
    private String upLoadPath;

    /**
     * 仓库列表
     * @param page
     * @param basWarehouse
     * @return
     */
    @Override
    public IPage<BasWarehouse> queryPageList(Page<BasWarehouse> page, BasWarehouse basWarehouse) {
        return baseMapper.queryPageList(page,basWarehouse);
    }

    /**
     * 导出
     * @param request
     * @param basWarehouse
     * @param clazz
     * @param title
     * @return
     */
    @Override
    public ModelAndView exportXls(HttpServletRequest request, BasWarehouse basWarehouse, Class<BasWarehouse> clazz, String title) {

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<BasWarehouse> pageList = baseMapper.exportXls(basWarehouse);
        List<BasWarehouse> exportList = null;

        // 过滤选中数据
        exportList = pageList;


        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcel());
        //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.FILE_NAME, title);
        mv.addObject(NormalExcelConstants.CLASS, clazz);
        //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
        ExportParams exportParams=new ExportParams(title, "导出人:" + sysUser.getRealname(), title);
        exportParams.setImageBasePath(upLoadPath);
        //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
        mv.addObject(NormalExcelConstants.PARAMS,exportParams);

        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }
}
