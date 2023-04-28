package org.jeecg.modules.srm.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.srm.entity.SupQuote;
import org.jeecg.modules.srm.service.ISupQuoteService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: sup_quote
 * @Author: jeecg-boot
 * @Date:   2022-09-27
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/supQuote")
@Slf4j
public class SupQuoteController extends JeecgController<SupQuote, ISupQuoteService> {
	@Autowired
	private ISupQuoteService supQuoteService;
	
	/**
	 * 分页列表查询
	 *
	 * @param supQuote
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "sup_quote-分页列表查询")
	@ApiOperation(value="sup_quote-分页列表查询", notes="sup_quote-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SupQuote>> queryPageList(SupQuote supQuote,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SupQuote> queryWrapper = QueryGenerator.initQueryWrapper(supQuote, req.getParameterMap());
		Page<SupQuote> page = new Page<SupQuote>(pageNo, pageSize);
		IPage<SupQuote> pageList = supQuoteService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param supQuote
	 * @return
	 */
	@AutoLog(value = "sup_quote-添加")
	@ApiOperation(value="sup_quote-添加", notes="sup_quote-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SupQuote supQuote) {
		supQuoteService.save(supQuote);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param supQuote
	 * @return
	 */
	@AutoLog(value = "sup_quote-编辑")
	@ApiOperation(value="sup_quote-编辑", notes="sup_quote-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SupQuote supQuote) {
		supQuoteService.updateById(supQuote);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sup_quote-通过id删除")
	@ApiOperation(value="sup_quote-通过id删除", notes="sup_quote-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		supQuoteService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sup_quote-批量删除")
	@ApiOperation(value="sup_quote-批量删除", notes="sup_quote-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.supQuoteService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "sup_quote-通过id查询")
	@ApiOperation(value="sup_quote-通过id查询", notes="sup_quote-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SupQuote> queryById(@RequestParam(name="id",required=true) String id) {
		SupQuote supQuote = supQuoteService.getById(id);
		if(supQuote==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(supQuote);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param supQuote
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SupQuote supQuote) {
        return super.exportXls(request, supQuote, SupQuote.class, "sup_quote");
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
        return super.importExcel(request, response, SupQuote.class);
    }

	 /**
	  * 历史报价
	  *
	  * @param supQuote
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "sup_quote-分页列表查询")
	 @ApiOperation(value="sup_quote-分页列表查询", notes="sup_quote-分页列表查询")
	 @GetMapping(value = "/fetchPriceHistory")
	 public Result<List<SupQuote>> fetchPriceHistory(SupQuote supQuote,
												  HttpServletRequest req) {
		 List<SupQuote> pageList = supQuoteService.fetchPriceHistory(supQuote);
		 return Result.OK(pageList);
	 }

}
