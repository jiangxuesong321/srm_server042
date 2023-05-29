package com.cmoc.modules.srm.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.modules.srm.entity.BiddingRecordSupplier;
import com.cmoc.modules.srm.service.IBiddingRecordSupplierService;

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
 * @Description: bidding_record_supplier
 * @Author: jeecg-boot
 * @Date:   2022-10-19
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/biddingRecordSupplier")
@Slf4j
public class BiddingRecordSupplierController extends JeecgController<BiddingRecordSupplier, IBiddingRecordSupplierService> {
	@Autowired
	private IBiddingRecordSupplierService biddingRecordSupplierService;
	
	/**
	 * 分页列表查询
	 *
	 * @param biddingRecordSupplier
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "bidding_record_supplier-分页列表查询")
	@ApiOperation(value="bidding_record_supplier-分页列表查询", notes="bidding_record_supplier-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BiddingRecordSupplier>> queryPageList(BiddingRecordSupplier biddingRecordSupplier,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BiddingRecordSupplier> queryWrapper = QueryGenerator.initQueryWrapper(biddingRecordSupplier, req.getParameterMap());
		Page<BiddingRecordSupplier> page = new Page<BiddingRecordSupplier>(pageNo, pageSize);
		IPage<BiddingRecordSupplier> pageList = biddingRecordSupplierService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param biddingRecordSupplier
	 * @return
	 */
	@AutoLog(value = "bidding_record_supplier-添加")
	@ApiOperation(value="bidding_record_supplier-添加", notes="bidding_record_supplier-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BiddingRecordSupplier biddingRecordSupplier) {
		biddingRecordSupplierService.save(biddingRecordSupplier);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param biddingRecordSupplier
	 * @return
	 */
	@AutoLog(value = "bidding_record_supplier-编辑")
	@ApiOperation(value="bidding_record_supplier-编辑", notes="bidding_record_supplier-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BiddingRecordSupplier biddingRecordSupplier) {
		biddingRecordSupplierService.updateById(biddingRecordSupplier);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bidding_record_supplier-通过id删除")
	@ApiOperation(value="bidding_record_supplier-通过id删除", notes="bidding_record_supplier-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		biddingRecordSupplierService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bidding_record_supplier-批量删除")
	@ApiOperation(value="bidding_record_supplier-批量删除", notes="bidding_record_supplier-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.biddingRecordSupplierService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "bidding_record_supplier-通过id查询")
	@ApiOperation(value="bidding_record_supplier-通过id查询", notes="bidding_record_supplier-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BiddingRecordSupplier> queryById(@RequestParam(name="id",required=true) String id) {
		BiddingRecordSupplier biddingRecordSupplier = biddingRecordSupplierService.getById(id);
		if(biddingRecordSupplier==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(biddingRecordSupplier);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param biddingRecordSupplier
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BiddingRecordSupplier biddingRecordSupplier) {
        return super.exportXls(request, biddingRecordSupplier, BiddingRecordSupplier.class, "bidding_record_supplier");
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
        return super.importExcel(request, response, BiddingRecordSupplier.class);
    }

}
