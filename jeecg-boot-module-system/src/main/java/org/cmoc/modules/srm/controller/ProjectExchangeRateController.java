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
import org.cmoc.modules.srm.entity.ProjectExchangeRate;
import org.cmoc.modules.srm.service.IProjectExchangeRateService;

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
 * @Description: project_exchange_rate
 * @Author: jeecg-boot
 * @Date:   2022-09-30
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/projectExchangeRate")
@Slf4j
public class ProjectExchangeRateController extends JeecgController<ProjectExchangeRate, IProjectExchangeRateService> {
	@Autowired
	private IProjectExchangeRateService projectExchangeRateService;
	
	/**
	 * 分页列表查询
	 *
	 * @param projectExchangeRate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "project_exchange_rate-分页列表查询")
	@ApiOperation(value="project_exchange_rate-分页列表查询", notes="project_exchange_rate-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ProjectExchangeRate>> queryPageList(ProjectExchangeRate projectExchangeRate,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProjectExchangeRate> queryWrapper = QueryGenerator.initQueryWrapper(projectExchangeRate, req.getParameterMap());
		Page<ProjectExchangeRate> page = new Page<ProjectExchangeRate>(pageNo, pageSize);
		IPage<ProjectExchangeRate> pageList = projectExchangeRateService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param projectExchangeRate
	 * @return
	 */
	@AutoLog(value = "project_exchange_rate-添加")
	@ApiOperation(value="project_exchange_rate-添加", notes="project_exchange_rate-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ProjectExchangeRate projectExchangeRate) {
		projectExchangeRateService.save(projectExchangeRate);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param projectExchangeRate
	 * @return
	 */
	@AutoLog(value = "project_exchange_rate-编辑")
	@ApiOperation(value="project_exchange_rate-编辑", notes="project_exchange_rate-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ProjectExchangeRate projectExchangeRate) {
		projectExchangeRateService.updateById(projectExchangeRate);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "project_exchange_rate-通过id删除")
	@ApiOperation(value="project_exchange_rate-通过id删除", notes="project_exchange_rate-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		projectExchangeRateService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "project_exchange_rate-批量删除")
	@ApiOperation(value="project_exchange_rate-批量删除", notes="project_exchange_rate-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.projectExchangeRateService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "project_exchange_rate-通过id查询")
	@ApiOperation(value="project_exchange_rate-通过id查询", notes="project_exchange_rate-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ProjectExchangeRate> queryById(@RequestParam(name="id",required=true) String id) {
		ProjectExchangeRate projectExchangeRate = projectExchangeRateService.getById(id);
		if(projectExchangeRate==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(projectExchangeRate);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param projectExchangeRate
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProjectExchangeRate projectExchangeRate) {
        return super.exportXls(request, projectExchangeRate, ProjectExchangeRate.class, "project_exchange_rate");
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
        return super.importExcel(request, response, ProjectExchangeRate.class);
    }

}
