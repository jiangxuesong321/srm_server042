package com.cmoc.modules.srm.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.modules.srm.entity.BasMaterialField;
import com.cmoc.modules.srm.service.IBasMaterialFieldService;

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
 * @Description: bas_material_field
 * @Author: jeecg-boot
 * @Date:   2022-12-07
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/basMaterialField")
@Slf4j
public class BasMaterialFieldController extends JeecgController<BasMaterialField, IBasMaterialFieldService> {
	@Autowired
	private IBasMaterialFieldService basMaterialFieldService;
	
	/**
	 * 分页列表查询
	 *
	 * @param basMaterialField
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "bas_material_field-分页列表查询")
	@ApiOperation(value="bas_material_field-分页列表查询", notes="bas_material_field-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BasMaterialField>> queryPageList(BasMaterialField basMaterialField,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BasMaterialField> queryWrapper = QueryGenerator.initQueryWrapper(basMaterialField, req.getParameterMap());
		Page<BasMaterialField> page = new Page<BasMaterialField>(pageNo, pageSize);
		IPage<BasMaterialField> pageList = basMaterialFieldService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param basMaterialField
	 * @return
	 */
	@AutoLog(value = "bas_material_field-添加")
	@ApiOperation(value="bas_material_field-添加", notes="bas_material_field-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BasMaterialField basMaterialField) {
		basMaterialFieldService.save(basMaterialField);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param basMaterialField
	 * @return
	 */
	@AutoLog(value = "bas_material_field-编辑")
	@ApiOperation(value="bas_material_field-编辑", notes="bas_material_field-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BasMaterialField basMaterialField) {
		basMaterialFieldService.updateById(basMaterialField);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bas_material_field-通过id删除")
	@ApiOperation(value="bas_material_field-通过id删除", notes="bas_material_field-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		basMaterialFieldService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bas_material_field-批量删除")
	@ApiOperation(value="bas_material_field-批量删除", notes="bas_material_field-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.basMaterialFieldService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "bas_material_field-通过id查询")
	@ApiOperation(value="bas_material_field-通过id查询", notes="bas_material_field-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BasMaterialField> queryById(@RequestParam(name="id",required=true) String id) {
		BasMaterialField basMaterialField = basMaterialFieldService.getById(id);
		if(basMaterialField==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(basMaterialField);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param basMaterialField
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasMaterialField basMaterialField) {
        return super.exportXls(request, basMaterialField, BasMaterialField.class, "bas_material_field");
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
        return super.importExcel(request, response, BasMaterialField.class);
    }

}
