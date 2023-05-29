package com.cmoc.modules.srm.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.modules.srm.entity.BasExpertCategory;
import com.cmoc.modules.srm.service.IBasExpertCategoryService;

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
 * @Description: 专家分类
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/basExpertCategory")
@Slf4j
public class BasExpertCategoryController extends JeecgController<BasExpertCategory, IBasExpertCategoryService> {
	@Autowired
	private IBasExpertCategoryService basExpertCategoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param basExpertCategory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "专家分类-分页列表查询")
	@ApiOperation(value="专家分类-分页列表查询", notes="专家分类-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BasExpertCategory>> queryPageList(BasExpertCategory basExpertCategory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BasExpertCategory> queryWrapper = QueryGenerator.initQueryWrapper(basExpertCategory, req.getParameterMap());
		Page<BasExpertCategory> page = new Page<BasExpertCategory>(pageNo, pageSize);
		IPage<BasExpertCategory> pageList = basExpertCategoryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param basExpertCategory
	 * @return
	 */
	@AutoLog(value = "专家分类-添加")
	@ApiOperation(value="专家分类-添加", notes="专家分类-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BasExpertCategory basExpertCategory) {
		basExpertCategoryService.save(basExpertCategory);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param basExpertCategory
	 * @return
	 */
	@AutoLog(value = "专家分类-编辑")
	@ApiOperation(value="专家分类-编辑", notes="专家分类-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BasExpertCategory basExpertCategory) {
		basExpertCategoryService.updateById(basExpertCategory);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "专家分类-通过id删除")
	@ApiOperation(value="专家分类-通过id删除", notes="专家分类-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		basExpertCategoryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "专家分类-批量删除")
	@ApiOperation(value="专家分类-批量删除", notes="专家分类-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.basExpertCategoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "专家分类-通过id查询")
	@ApiOperation(value="专家分类-通过id查询", notes="专家分类-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BasExpertCategory> queryById(@RequestParam(name="id",required=true) String id) {
		BasExpertCategory basExpertCategory = basExpertCategoryService.getById(id);
		if(basExpertCategory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(basExpertCategory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param basExpertCategory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasExpertCategory basExpertCategory) {
        return super.exportXls(request, basExpertCategory, BasExpertCategory.class, "专家分类");
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
        return super.importExcel(request, response, BasExpertCategory.class);
    }

}
