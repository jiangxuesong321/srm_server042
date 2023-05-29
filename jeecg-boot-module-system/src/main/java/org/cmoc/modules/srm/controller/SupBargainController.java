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
import org.cmoc.modules.srm.entity.SupBargain;
import org.cmoc.modules.srm.service.ISupBargainService;

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
 * @Description: sup_bargain
 * @Author: jeecg-boot
 * @Date:   2022-09-28
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/supBargain")
@Slf4j
public class SupBargainController extends JeecgController<SupBargain, ISupBargainService> {
	@Autowired
	private ISupBargainService supBargainService;
	
	/**
	 * 分页列表查询
	 *
	 * @param supBargain
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "sup_bargain-分页列表查询")
	@ApiOperation(value="sup_bargain-分页列表查询", notes="sup_bargain-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SupBargain>> queryPageList(SupBargain supBargain,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SupBargain> queryWrapper = QueryGenerator.initQueryWrapper(supBargain, req.getParameterMap());
		Page<SupBargain> page = new Page<SupBargain>(pageNo, pageSize);
		IPage<SupBargain> pageList = supBargainService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param supBargain
	 * @return
	 */
	@AutoLog(value = "sup_bargain-添加")
	@ApiOperation(value="sup_bargain-添加", notes="sup_bargain-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SupBargain supBargain) {
		supBargainService.saveBargain(supBargain);
		return Result.OK("提交成功！");
	}

	 /**
	  *   添加
	  *
	  * @param supBargain
	  * @return
	  */
	 @AutoLog(value = "sup_bargain-添加")
	 @ApiOperation(value="sup_bargain-添加", notes="sup_bargain-添加")
	 @PostMapping(value = "/addAll")
	 public Result<String> addAll(@RequestBody List<SupBargain> supBargain) {
		 supBargainService.saveBargainAll(supBargain);
		 return Result.OK("添加成功！");
	 }
	
	/**
	 *  编辑
	 *
	 * @param supBargain
	 * @return
	 */
	@AutoLog(value = "sup_bargain-编辑")
	@ApiOperation(value="sup_bargain-编辑", notes="sup_bargain-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SupBargain supBargain) {
		supBargainService.updateById(supBargain);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sup_bargain-通过id删除")
	@ApiOperation(value="sup_bargain-通过id删除", notes="sup_bargain-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		supBargainService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sup_bargain-批量删除")
	@ApiOperation(value="sup_bargain-批量删除", notes="sup_bargain-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.supBargainService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "sup_bargain-通过id查询")
	@ApiOperation(value="sup_bargain-通过id查询", notes="sup_bargain-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SupBargain> queryById(@RequestParam(name="id",required=true) String id) {
		SupBargain supBargain = supBargainService.getById(id);
		if(supBargain==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(supBargain);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param supBargain
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SupBargain supBargain) {
        return super.exportXls(request, supBargain, SupBargain.class, "sup_bargain");
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
        return super.importExcel(request, response, SupBargain.class);
    }

}
