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
import org.cmoc.modules.srm.entity.BasSupplierContract;
import org.cmoc.modules.srm.service.IBasSupplierContractService;

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
 * @Description: 供应商合同
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/basSupplierContract")
@Slf4j
public class BasSupplierContractController extends JeecgController<BasSupplierContract, IBasSupplierContractService> {
	@Autowired
	private IBasSupplierContractService basSupplierContractService;
	
	/**
	 * 分页列表查询
	 *
	 * @param basSupplierContract
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "供应商合同-分页列表查询")
	@ApiOperation(value="供应商合同-分页列表查询", notes="供应商合同-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BasSupplierContract>> queryPageList(BasSupplierContract basSupplierContract,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BasSupplierContract> queryWrapper = QueryGenerator.initQueryWrapper(basSupplierContract, req.getParameterMap());
		Page<BasSupplierContract> page = new Page<BasSupplierContract>(pageNo, pageSize);
		IPage<BasSupplierContract> pageList = basSupplierContractService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param basSupplierContract
	 * @return
	 */
	@AutoLog(value = "供应商合同-添加")
	@ApiOperation(value="供应商合同-添加", notes="供应商合同-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BasSupplierContract basSupplierContract) {
		basSupplierContractService.save(basSupplierContract);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param basSupplierContract
	 * @return
	 */
	@AutoLog(value = "供应商合同-编辑")
	@ApiOperation(value="供应商合同-编辑", notes="供应商合同-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BasSupplierContract basSupplierContract) {
		basSupplierContractService.updateById(basSupplierContract);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "供应商合同-通过id删除")
	@ApiOperation(value="供应商合同-通过id删除", notes="供应商合同-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		basSupplierContractService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "供应商合同-批量删除")
	@ApiOperation(value="供应商合同-批量删除", notes="供应商合同-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.basSupplierContractService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "供应商合同-通过id查询")
	@ApiOperation(value="供应商合同-通过id查询", notes="供应商合同-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BasSupplierContract> queryById(@RequestParam(name="id",required=true) String id) {
		BasSupplierContract basSupplierContract = basSupplierContractService.getById(id);
		if(basSupplierContract==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(basSupplierContract);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param basSupplierContract
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasSupplierContract basSupplierContract) {
        return super.exportXls(request, basSupplierContract, BasSupplierContract.class, "供应商合同");
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
        return super.importExcel(request, response, BasSupplierContract.class);
    }

}
