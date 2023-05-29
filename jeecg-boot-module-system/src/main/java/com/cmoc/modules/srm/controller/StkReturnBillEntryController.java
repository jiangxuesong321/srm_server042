package com.cmoc.modules.srm.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.modules.srm.entity.StkReturnBillEntry;
import com.cmoc.modules.srm.service.IStkReturnBillEntryService;

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
 * @Description: stk_return_bill_entry
 * @Author: jeecg-boot
 * @Date:   2022-10-10
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/stkReturnBillEntry")
@Slf4j
public class StkReturnBillEntryController extends JeecgController<StkReturnBillEntry, IStkReturnBillEntryService> {
	@Autowired
	private IStkReturnBillEntryService stkReturnBillEntryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param stkReturnBillEntry
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "stk_return_bill_entry-分页列表查询")
	@ApiOperation(value="stk_return_bill_entry-分页列表查询", notes="stk_return_bill_entry-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StkReturnBillEntry>> queryPageList(StkReturnBillEntry stkReturnBillEntry,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<StkReturnBillEntry> queryWrapper = QueryGenerator.initQueryWrapper(stkReturnBillEntry, req.getParameterMap());
		Page<StkReturnBillEntry> page = new Page<StkReturnBillEntry>(pageNo, pageSize);
		IPage<StkReturnBillEntry> pageList = stkReturnBillEntryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param stkReturnBillEntry
	 * @return
	 */
	@AutoLog(value = "stk_return_bill_entry-添加")
	@ApiOperation(value="stk_return_bill_entry-添加", notes="stk_return_bill_entry-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody StkReturnBillEntry stkReturnBillEntry) {
		stkReturnBillEntryService.save(stkReturnBillEntry);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param stkReturnBillEntry
	 * @return
	 */
	@AutoLog(value = "stk_return_bill_entry-编辑")
	@ApiOperation(value="stk_return_bill_entry-编辑", notes="stk_return_bill_entry-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody StkReturnBillEntry stkReturnBillEntry) {
		stkReturnBillEntryService.updateById(stkReturnBillEntry);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "stk_return_bill_entry-通过id删除")
	@ApiOperation(value="stk_return_bill_entry-通过id删除", notes="stk_return_bill_entry-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		stkReturnBillEntryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "stk_return_bill_entry-批量删除")
	@ApiOperation(value="stk_return_bill_entry-批量删除", notes="stk_return_bill_entry-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.stkReturnBillEntryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "stk_return_bill_entry-通过id查询")
	@ApiOperation(value="stk_return_bill_entry-通过id查询", notes="stk_return_bill_entry-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StkReturnBillEntry> queryById(@RequestParam(name="id",required=true) String id) {
		StkReturnBillEntry stkReturnBillEntry = stkReturnBillEntryService.getById(id);
		if(stkReturnBillEntry==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(stkReturnBillEntry);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param stkReturnBillEntry
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StkReturnBillEntry stkReturnBillEntry) {
        return super.exportXls(request, stkReturnBillEntry, StkReturnBillEntry.class, "stk_return_bill_entry");
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
        return super.importExcel(request, response, StkReturnBillEntry.class);
    }

}
