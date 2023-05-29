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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.cmoc.common.api.vo.Result;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.common.system.query.QueryGenerator;
import org.cmoc.common.util.FillRuleUtil;
import org.cmoc.common.util.oConvertUtils;
import org.cmoc.modules.srm.entity.BasWarehouse;
import org.cmoc.modules.srm.entity.StkIoBill;
import org.cmoc.modules.srm.entity.StkIoBillEntry;
import org.cmoc.modules.srm.service.IBasWarehouseService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.cmoc.modules.srm.service.IStkIoBillEntryService;
import org.cmoc.modules.srm.service.IStkIoBillService;
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
 * @Description: 仓库
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/basWarehouse")
@Slf4j
public class BasWarehouseController extends JeecgController<BasWarehouse, IBasWarehouseService> {
	@Autowired
	private IBasWarehouseService basWarehouseService;
	@Autowired
	private IStkIoBillService iStkIoBillService;
	
	/**
	 * 分页列表查询
	 *
	 * @param basWarehouse
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "仓库-分页列表查询")
	@ApiOperation(value="仓库-分页列表查询", notes="仓库-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BasWarehouse>> queryPageList(BasWarehouse basWarehouse,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
//		QueryWrapper<BasWarehouse> queryWrapper = QueryGenerator.initQueryWrapper(basWarehouse, req.getParameterMap());
		Page<BasWarehouse> page = new Page<BasWarehouse>(pageNo, pageSize);
		IPage<BasWarehouse> pageList = basWarehouseService.queryPageList(page, basWarehouse);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param basWarehouse
	 * @return
	 */
	@AutoLog(value = "仓库-添加")
	@ApiOperation(value="仓库-添加", notes="仓库-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BasWarehouse basWarehouse) throws Exception {
		JSONObject formData = new JSONObject();
		formData.put("prefix", "PROJ");
		String code = (String) FillRuleUtil.executeRule("wh_code", formData);
		basWarehouse.setCode(code);
		basWarehouseService.save(basWarehouse);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param basWarehouse
	 * @return
	 */
	@AutoLog(value = "仓库-编辑")
	@ApiOperation(value="仓库-编辑", notes="仓库-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BasWarehouse basWarehouse) {
		basWarehouseService.updateById(basWarehouse);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "仓库-通过id删除")
	@ApiOperation(value="仓库-通过id删除", notes="仓库-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) throws Exception {
		//判断是否能删除
		List<StkIoBill> sibList = iStkIoBillService.list(Wrappers.<StkIoBill>query().lambda().
				eq(StkIoBill :: getWhId,id));
		if(sibList != null && sibList.size() > 0){
			throw new Exception("存在入出库记录,不能删除");
		}
		BasWarehouse bw = basWarehouseService.getById(id);
		bw.setDelFlag(CommonConstant.HAS_READ_FLAG);
		basWarehouseService.updateById(bw);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "仓库-批量删除")
	@ApiOperation(value="仓库-批量删除", notes="仓库-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.basWarehouseService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "仓库-通过id查询")
	@ApiOperation(value="仓库-通过id查询", notes="仓库-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BasWarehouse> queryById(@RequestParam(name="id",required=true) String id) {
		BasWarehouse basWarehouse = basWarehouseService.getById(id);
		if(basWarehouse==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(basWarehouse);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param basWarehouse
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasWarehouse basWarehouse) {
        return basWarehouseService.exportXls(request, basWarehouse, BasWarehouse.class, "仓库");
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
        return super.importExcel(request, response, BasWarehouse.class);
    }

}
