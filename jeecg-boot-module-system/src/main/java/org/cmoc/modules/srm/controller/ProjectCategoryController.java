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
import org.cmoc.modules.srm.entity.ProjectCategory;
import org.cmoc.modules.srm.service.IProjectCategoryService;

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
 * @Description: project_category
 * @Author: jeecg-boot
 * @Date:   2022-09-23
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/projectCategory")
@Slf4j
public class ProjectCategoryController extends JeecgController<ProjectCategory, IProjectCategoryService> {
	@Autowired
	private IProjectCategoryService projectCategoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param projectCategory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "project_category-分页列表查询")
	@ApiOperation(value="project_category-分页列表查询", notes="project_category-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ProjectCategory>> queryPageList(ProjectCategory projectCategory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProjectCategory> queryWrapper = QueryGenerator.initQueryWrapper(projectCategory, req.getParameterMap());
		Page<ProjectCategory> page = new Page<ProjectCategory>(pageNo, pageSize);
		IPage<ProjectCategory> pageList = projectCategoryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param projectCategory
	 * @return
	 */
	@AutoLog(value = "project_category-添加")
	@ApiOperation(value="project_category-添加", notes="project_category-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ProjectCategory projectCategory) {
		projectCategoryService.save(projectCategory);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param projectCategory
	 * @return
	 */
	@AutoLog(value = "project_category-编辑")
	@ApiOperation(value="project_category-编辑", notes="project_category-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ProjectCategory projectCategory) {
		projectCategoryService.updateById(projectCategory);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "project_category-通过id删除")
	@ApiOperation(value="project_category-通过id删除", notes="project_category-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		projectCategoryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "project_category-批量删除")
	@ApiOperation(value="project_category-批量删除", notes="project_category-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.projectCategoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "project_category-通过id查询")
	@ApiOperation(value="project_category-通过id查询", notes="project_category-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ProjectCategory> queryById(@RequestParam(name="id",required=true) String id) {
		ProjectCategory projectCategory = projectCategoryService.getById(id);
		if(projectCategory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(projectCategory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param projectCategory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProjectCategory projectCategory) {
        return super.exportXls(request, projectCategory, ProjectCategory.class, "project_category");
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
        return super.importExcel(request, response, ProjectCategory.class);
    }

}
