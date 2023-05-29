package com.cmoc.modules.srm.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.modules.srm.entity.BasSupplierFast;
import com.cmoc.modules.srm.service.IBasSupplierFastService;

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
 * @Description: bas_supplier_fast
 * @Author: jeecg-boot
 * @Date:   2022-12-02
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/basSupplierFast")
@Slf4j
public class BasSupplierFastController extends JeecgController<BasSupplierFast, IBasSupplierFastService> {
	@Autowired
	private IBasSupplierFastService basSupplierFastService;
	
	/**
	 * 分页列表查询
	 *
	 * @param basSupplierFast
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "bas_supplier_fast-分页列表查询")
	@ApiOperation(value="bas_supplier_fast-分页列表查询", notes="bas_supplier_fast-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BasSupplierFast>> queryPageList(BasSupplierFast basSupplierFast,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BasSupplierFast> queryWrapper = QueryGenerator.initQueryWrapper(basSupplierFast, req.getParameterMap());
		Page<BasSupplierFast> page = new Page<BasSupplierFast>(pageNo, pageSize);
		IPage<BasSupplierFast> pageList = basSupplierFastService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param basSupplierFast
	 * @return
	 */
	@AutoLog(value = "bas_supplier_fast-添加")
	@ApiOperation(value="bas_supplier_fast-添加", notes="bas_supplier_fast-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BasSupplierFast basSupplierFast) {
		basSupplierFastService.save(basSupplierFast);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param basSupplierFast
	 * @return
	 */
	@AutoLog(value = "bas_supplier_fast-编辑")
	@ApiOperation(value="bas_supplier_fast-编辑", notes="bas_supplier_fast-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BasSupplierFast basSupplierFast) {
		basSupplierFastService.updateById(basSupplierFast);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bas_supplier_fast-通过id删除")
	@ApiOperation(value="bas_supplier_fast-通过id删除", notes="bas_supplier_fast-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		basSupplierFastService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bas_supplier_fast-批量删除")
	@ApiOperation(value="bas_supplier_fast-批量删除", notes="bas_supplier_fast-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.basSupplierFastService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "bas_supplier_fast-通过id查询")
	@ApiOperation(value="bas_supplier_fast-通过id查询", notes="bas_supplier_fast-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BasSupplierFast> queryById(@RequestParam(name="id",required=true) String id) {
		BasSupplierFast basSupplierFast = basSupplierFastService.getById(id);
		if(basSupplierFast==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(basSupplierFast);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param basSupplierFast
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasSupplierFast basSupplierFast) {
        return super.exportXls(request, basSupplierFast, BasSupplierFast.class, "bas_supplier_fast");
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
        return super.importExcel(request, response, BasSupplierFast.class);
    }

}
