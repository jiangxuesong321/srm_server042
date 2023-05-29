package com.cmoc.modules.srm.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.modules.srm.entity.ProjectCategoryPay;
import com.cmoc.modules.srm.service.IProjectCategoryPayService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import com.cmoc.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.ApiOperation;
import com.cmoc.common.aspect.annotation.AutoLog;

 /**
 * @Description: project_category_pay
 * @Author: jeecg-boot
 * @Date:   2022-12-05
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/projectCategoryPay")
@Slf4j
public class ProjectCategoryPayController extends JeecgController<ProjectCategoryPay, IProjectCategoryPayService> {
	@Autowired
	private IProjectCategoryPayService projectCategoryPayService;
	
	/**
	 * 分页列表查询
	 *
	 * @param projectCategoryPay
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "project_category_pay-分页列表查询")
	@ApiOperation(value="project_category_pay-分页列表查询", notes="project_category_pay-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ProjectCategoryPay>> queryPageList(ProjectCategoryPay projectCategoryPay,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProjectCategoryPay> queryWrapper = QueryGenerator.initQueryWrapper(projectCategoryPay, req.getParameterMap());
		Page<ProjectCategoryPay> page = new Page<ProjectCategoryPay>(pageNo, pageSize);
		IPage<ProjectCategoryPay> pageList = projectCategoryPayService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param projectCategoryPay
	 * @return
	 */
	@AutoLog(value = "project_category_pay-添加")
	@ApiOperation(value="project_category_pay-添加", notes="project_category_pay-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ProjectCategoryPay projectCategoryPay) {
		projectCategoryPayService.save(projectCategoryPay);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param projectCategoryPay
	 * @return
	 */
	@AutoLog(value = "project_category_pay-编辑")
	@ApiOperation(value="project_category_pay-编辑", notes="project_category_pay-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ProjectCategoryPay projectCategoryPay) {
		projectCategoryPayService.updateById(projectCategoryPay);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "project_category_pay-通过id删除")
	@ApiOperation(value="project_category_pay-通过id删除", notes="project_category_pay-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		projectCategoryPayService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "project_category_pay-批量删除")
	@ApiOperation(value="project_category_pay-批量删除", notes="project_category_pay-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.projectCategoryPayService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "project_category_pay-通过id查询")
	@ApiOperation(value="project_category_pay-通过id查询", notes="project_category_pay-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ProjectCategoryPay> queryById(@RequestParam(name="id",required=true) String id) {
		ProjectCategoryPay projectCategoryPay = projectCategoryPayService.getById(id);
		if(projectCategoryPay==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(projectCategoryPay);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param projectCategoryPay
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProjectCategoryPay projectCategoryPay) {
        return super.exportXls(request, projectCategoryPay, ProjectCategoryPay.class, "project_category_pay");
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
        return super.importExcel(request, response, ProjectCategoryPay.class);
    }

}
