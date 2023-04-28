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
import org.jeecg.modules.srm.entity.ProjectBomChild;
import org.jeecg.modules.srm.service.IProjectBomChildService;

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
 * @Description: project_bom_child
 * @Author: jeecg-boot
 * @Date:   2022-09-23
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/projectBomChild")
@Slf4j
public class ProjectBomChildController extends JeecgController<ProjectBomChild, IProjectBomChildService> {
	@Autowired
	private IProjectBomChildService projectBomChildService;
	
	/**
	 * 分页列表查询
	 *
	 * @param projectBomChild
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "project_bom_child-分页列表查询")
	@ApiOperation(value="project_bom_child-分页列表查询", notes="project_bom_child-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ProjectBomChild>> queryPageList(ProjectBomChild projectBomChild,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProjectBomChild> queryWrapper = QueryGenerator.initQueryWrapper(projectBomChild, req.getParameterMap());
		Page<ProjectBomChild> page = new Page<ProjectBomChild>(pageNo, pageSize);
		IPage<ProjectBomChild> pageList = projectBomChildService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param projectBomChild
	 * @return
	 */
	@AutoLog(value = "project_bom_child-添加")
	@ApiOperation(value="project_bom_child-添加", notes="project_bom_child-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ProjectBomChild projectBomChild) {
		projectBomChildService.save(projectBomChild);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param projectBomChild
	 * @return
	 */
	@AutoLog(value = "project_bom_child-编辑")
	@ApiOperation(value="project_bom_child-编辑", notes="project_bom_child-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ProjectBomChild projectBomChild) {
		projectBomChildService.updateById(projectBomChild);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "project_bom_child-通过id删除")
	@ApiOperation(value="project_bom_child-通过id删除", notes="project_bom_child-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		projectBomChildService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "project_bom_child-批量删除")
	@ApiOperation(value="project_bom_child-批量删除", notes="project_bom_child-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.projectBomChildService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "project_bom_child-通过id查询")
	@ApiOperation(value="project_bom_child-通过id查询", notes="project_bom_child-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ProjectBomChild> queryById(@RequestParam(name="id",required=true) String id) {
		ProjectBomChild projectBomChild = projectBomChildService.getById(id);
		if(projectBomChild==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(projectBomChild);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param projectBomChild
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProjectBomChild projectBomChild) {
        return super.exportXls(request, projectBomChild, ProjectBomChild.class, "project_bom_child");
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
        return super.importExcel(request, response, ProjectBomChild.class);
    }

}
