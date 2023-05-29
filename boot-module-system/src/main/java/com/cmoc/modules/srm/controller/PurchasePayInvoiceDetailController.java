package com.cmoc.modules.srm.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.modules.srm.entity.PurchasePayInvoiceDetail;
import com.cmoc.modules.srm.service.IPurchasePayInvoiceDetailService;

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
 * @Description: purchase_pay_invoice_detail
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/purchasePayInvoiceDetail")
@Slf4j
public class PurchasePayInvoiceDetailController extends JeecgController<PurchasePayInvoiceDetail, IPurchasePayInvoiceDetailService> {
	@Autowired
	private IPurchasePayInvoiceDetailService purchasePayInvoiceDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param purchasePayInvoiceDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "purchase_pay_invoice_detail-分页列表查询")
	@ApiOperation(value="purchase_pay_invoice_detail-分页列表查询", notes="purchase_pay_invoice_detail-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PurchasePayInvoiceDetail>> queryPageList(PurchasePayInvoiceDetail purchasePayInvoiceDetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PurchasePayInvoiceDetail> queryWrapper = QueryGenerator.initQueryWrapper(purchasePayInvoiceDetail, req.getParameterMap());
		Page<PurchasePayInvoiceDetail> page = new Page<PurchasePayInvoiceDetail>(pageNo, pageSize);
		IPage<PurchasePayInvoiceDetail> pageList = purchasePayInvoiceDetailService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param purchasePayInvoiceDetail
	 * @return
	 */
	@AutoLog(value = "purchase_pay_invoice_detail-添加")
	@ApiOperation(value="purchase_pay_invoice_detail-添加", notes="purchase_pay_invoice_detail-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PurchasePayInvoiceDetail purchasePayInvoiceDetail) {
		purchasePayInvoiceDetailService.save(purchasePayInvoiceDetail);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param purchasePayInvoiceDetail
	 * @return
	 */
	@AutoLog(value = "purchase_pay_invoice_detail-编辑")
	@ApiOperation(value="purchase_pay_invoice_detail-编辑", notes="purchase_pay_invoice_detail-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PurchasePayInvoiceDetail purchasePayInvoiceDetail) {
		purchasePayInvoiceDetailService.updateById(purchasePayInvoiceDetail);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "purchase_pay_invoice_detail-通过id删除")
	@ApiOperation(value="purchase_pay_invoice_detail-通过id删除", notes="purchase_pay_invoice_detail-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		purchasePayInvoiceDetailService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "purchase_pay_invoice_detail-批量删除")
	@ApiOperation(value="purchase_pay_invoice_detail-批量删除", notes="purchase_pay_invoice_detail-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.purchasePayInvoiceDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "purchase_pay_invoice_detail-通过id查询")
	@ApiOperation(value="purchase_pay_invoice_detail-通过id查询", notes="purchase_pay_invoice_detail-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PurchasePayInvoiceDetail> queryById(@RequestParam(name="id",required=true) String id) {
		PurchasePayInvoiceDetail purchasePayInvoiceDetail = purchasePayInvoiceDetailService.getById(id);
		if(purchasePayInvoiceDetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(purchasePayInvoiceDetail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param purchasePayInvoiceDetail
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PurchasePayInvoiceDetail purchasePayInvoiceDetail) {
        return super.exportXls(request, purchasePayInvoiceDetail, PurchasePayInvoiceDetail.class, "purchase_pay_invoice_detail");
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
        return super.importExcel(request, response, PurchasePayInvoiceDetail.class);
    }

}
