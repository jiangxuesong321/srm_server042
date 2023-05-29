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
import org.cmoc.modules.srm.entity.BasSupplierResume;
import org.cmoc.modules.srm.service.IBasSupplierResumeService;

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
 * @Description: bas_supplier_resume
 * @Author: jeecg-boot
 * @Date:   2022-10-25
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/basSupplierResume")
@Slf4j
public class BasSupplierResumeController extends JeecgController<BasSupplierResume, IBasSupplierResumeService> {
	@Autowired
	private IBasSupplierResumeService basSupplierResumeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param basSupplierResume
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "bas_supplier_resume-分页列表查询")
	@ApiOperation(value="bas_supplier_resume-分页列表查询", notes="bas_supplier_resume-分页列表查询")
	@GetMapping(value = "/list")
	public Result<List<BasSupplierResume>> queryPageList(BasSupplierResume basSupplierResume,
								   HttpServletRequest req) {
		QueryWrapper<BasSupplierResume> queryWrapper = QueryGenerator.initQueryWrapper(basSupplierResume, req.getParameterMap());
		List<BasSupplierResume> pageList = basSupplierResumeService.list(queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param basSupplierResume
	 * @return
	 */
	@AutoLog(value = "bas_supplier_resume-添加")
	@ApiOperation(value="bas_supplier_resume-添加", notes="bas_supplier_resume-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BasSupplierResume basSupplierResume) {
		basSupplierResumeService.save(basSupplierResume);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param basSupplierResume
	 * @return
	 */
	@AutoLog(value = "bas_supplier_resume-编辑")
	@ApiOperation(value="bas_supplier_resume-编辑", notes="bas_supplier_resume-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BasSupplierResume basSupplierResume) {
		basSupplierResumeService.updateById(basSupplierResume);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bas_supplier_resume-通过id删除")
	@ApiOperation(value="bas_supplier_resume-通过id删除", notes="bas_supplier_resume-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		basSupplierResumeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bas_supplier_resume-批量删除")
	@ApiOperation(value="bas_supplier_resume-批量删除", notes="bas_supplier_resume-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.basSupplierResumeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "bas_supplier_resume-通过id查询")
	@ApiOperation(value="bas_supplier_resume-通过id查询", notes="bas_supplier_resume-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BasSupplierResume> queryById(@RequestParam(name="id",required=true) String id) {
		BasSupplierResume basSupplierResume = basSupplierResumeService.getById(id);
		if(basSupplierResume==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(basSupplierResume);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param basSupplierResume
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasSupplierResume basSupplierResume) {
        return super.exportXls(request, basSupplierResume, BasSupplierResume.class, "bas_supplier_resume");
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
        return super.importExcel(request, response, BasSupplierResume.class);
    }

}
