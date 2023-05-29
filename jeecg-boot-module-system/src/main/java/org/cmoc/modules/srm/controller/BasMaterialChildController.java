package org.cmoc.modules.srm.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.cmoc.common.api.vo.Result;
import org.cmoc.common.system.query.QueryGenerator;
import org.cmoc.common.util.oConvertUtils;
import org.cmoc.modules.srm.entity.BasMaterialChild;
import org.cmoc.modules.srm.service.IBasMaterialChildService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.cmoc.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cmoc.common.aspect.annotation.AutoLog;

 /**
 * @Description: bas_material_child
 * @Author: jeecg-boot
 * @Date:   2022-12-06
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/basMaterialChild")
@Slf4j
public class BasMaterialChildController extends JeecgController<BasMaterialChild, IBasMaterialChildService> {
	@Autowired
	private IBasMaterialChildService basMaterialChildService;
	
	/**
	 * 分页列表查询
	 *
	 * @param basMaterialChild
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "bas_material_child-分页列表查询")
	@ApiOperation(value="bas_material_child-分页列表查询", notes="bas_material_child-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BasMaterialChild>> queryPageList(BasMaterialChild basMaterialChild,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BasMaterialChild> queryWrapper = QueryGenerator.initQueryWrapper(basMaterialChild, req.getParameterMap());
		Page<BasMaterialChild> page = new Page<BasMaterialChild>(pageNo, pageSize);
		IPage<BasMaterialChild> pageList = basMaterialChildService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param basMaterialChild
	 * @return
	 */
	@AutoLog(value = "bas_material_child-添加")
	@ApiOperation(value="bas_material_child-添加", notes="bas_material_child-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BasMaterialChild basMaterialChild) {
		basMaterialChildService.save(basMaterialChild);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param basMaterialChild
	 * @return
	 */
	@AutoLog(value = "bas_material_child-编辑")
	@ApiOperation(value="bas_material_child-编辑", notes="bas_material_child-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BasMaterialChild basMaterialChild) {
		basMaterialChildService.updateById(basMaterialChild);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bas_material_child-通过id删除")
	@ApiOperation(value="bas_material_child-通过id删除", notes="bas_material_child-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		basMaterialChildService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bas_material_child-批量删除")
	@ApiOperation(value="bas_material_child-批量删除", notes="bas_material_child-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.basMaterialChildService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "bas_material_child-通过id查询")
	@ApiOperation(value="bas_material_child-通过id查询", notes="bas_material_child-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BasMaterialChild> queryById(@RequestParam(name="id",required=true) String id) {
		BasMaterialChild basMaterialChild = basMaterialChildService.getById(id);
		if(basMaterialChild==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(basMaterialChild);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param basMaterialChild
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasMaterialChild basMaterialChild) {
        return super.exportXls(request, basMaterialChild, BasMaterialChild.class, "bas_material_child");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BasMaterialChild.class);
    }

}
