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
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.common.system.query.QueryGenerator;
import org.cmoc.common.util.oConvertUtils;
import org.cmoc.modules.srm.entity.SupQuoteChild;
import org.cmoc.modules.srm.service.ISupQuoteChildService;

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
 * @Description: sup_quote_child
 * @Author: jeecg-boot
 * @Date:   2022-10-26
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/supQuoteChild")
@Slf4j
public class SupQuoteChildController extends JeecgController<SupQuoteChild, ISupQuoteChildService> {
	@Autowired
	private ISupQuoteChildService supQuoteChildService;
	
	/**
	 * 分页列表查询
	 *
	 * @param supQuoteChild
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "sup_quote_child-分页列表查询")
	@ApiOperation(value="sup_quote_child-分页列表查询", notes="sup_quote_child-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SupQuoteChild>> queryPageList(SupQuoteChild supQuoteChild,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SupQuoteChild> queryWrapper = QueryGenerator.initQueryWrapper(supQuoteChild, req.getParameterMap());
		Page<SupQuoteChild> page = new Page<SupQuoteChild>(pageNo, pageSize);
		IPage<SupQuoteChild> pageList = supQuoteChildService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param supQuoteChild
	 * @return
	 */
	@AutoLog(value = "sup_quote_child-添加")
	@ApiOperation(value="sup_quote_child-添加", notes="sup_quote_child-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SupQuoteChild supQuoteChild) {
		supQuoteChildService.save(supQuoteChild);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param supQuoteChild
	 * @return
	 */
	@AutoLog(value = "sup_quote_child-编辑")
	@ApiOperation(value="sup_quote_child-编辑", notes="sup_quote_child-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SupQuoteChild supQuoteChild) {
		supQuoteChildService.updateById(supQuoteChild);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sup_quote_child-通过id删除")
	@ApiOperation(value="sup_quote_child-通过id删除", notes="sup_quote_child-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		supQuoteChildService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sup_quote_child-批量删除")
	@ApiOperation(value="sup_quote_child-批量删除", notes="sup_quote_child-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.supQuoteChildService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "sup_quote_child-通过id查询")
	@ApiOperation(value="sup_quote_child-通过id查询", notes="sup_quote_child-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SupQuoteChild> queryById(@RequestParam(name="id",required=true) String id) {
		SupQuoteChild supQuoteChild = supQuoteChildService.getById(id);
		if(supQuoteChild==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(supQuoteChild);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param supQuoteChild
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SupQuoteChild supQuoteChild) {
        return super.exportXls(request, supQuoteChild, SupQuoteChild.class, "sup_quote_child");
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
        return super.importExcel(request, response, SupQuoteChild.class);
    }

	 /**
	  * 分页列表查询
	  *
	  * @param supQuoteChild
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/queryList")
	 public Result<List<SupQuoteChild>> queryList(SupQuoteChild supQuoteChild,
													   HttpServletRequest req) {
		 QueryWrapper<SupQuoteChild> queryWrapper = QueryGenerator.initQueryWrapper(supQuoteChild, req.getParameterMap());
		 queryWrapper.lambda().eq(SupQuoteChild :: getDelFlag, CommonConstant.DEL_FLAG_0);
		 List<SupQuoteChild> pageList = supQuoteChildService.list(queryWrapper);
		 return Result.OK(pageList);
	 }

}
