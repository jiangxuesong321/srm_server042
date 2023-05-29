package com.cmoc.modules.srm.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cmoc.modules.system.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.system.vo.LoginUser;
import com.cmoc.modules.srm.entity.ContractObject;
import com.cmoc.modules.srm.entity.PurchasePayInovice;
import com.cmoc.modules.srm.service.IPurchasePayInoviceService;

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
 * @Description: 发票登记
 * @Author: jeecg-boot
 * @Date:   2022-06-20
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/purchasePayInovice")
@Slf4j
public class PurchasePayInoviceController extends JeecgController<PurchasePayInovice, IPurchasePayInoviceService> {
	@Autowired
	private IPurchasePayInoviceService purchasePayInoviceService;
	@Autowired
	private ISysUserService iSysUserService;

	/**
	 * 分页列表查询
	 *
	 * @param purchasePayInovice
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "发票登记-分页列表查询")
	@ApiOperation(value="发票登记-分页列表查询", notes="发票登记-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PurchasePayInovice>> queryPageList(PurchasePayInovice purchasePayInovice,
														   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();

		Page<PurchasePayInovice> page = new Page<PurchasePayInovice>(pageNo, pageSize);


//		String permission = iSysUserService.fetchPermission(username);
//		String auth = "subject";
//		String auther = loginUser.getRelTenantIds();
//		if(StringUtils.isNotEmpty(permission)){
//			if(permission.contains("invoice:all")){
//				auth = "all";
//			}
//		}
//		purchasePayInovice.setAuth(auth);
//		purchasePayInovice.setAuther(auther);

		IPage<PurchasePayInovice> pageList = purchasePayInoviceService.queryPageList(page, purchasePayInovice);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param purchasePayInovice
	 * @return
	 */
	@AutoLog(value = "发票登记-添加")
	@ApiOperation(value="发票登记-添加", notes="发票登记-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PurchasePayInovice purchasePayInovice) {
		purchasePayInoviceService.saveInvoice(purchasePayInovice);
		return Result.OK("添加成功！");
	}

	/**
	 *   添加
	 *
	 * @param purchasePayInovice
	 * @return
	 */
	@AutoLog(value = "发票登记-添加")
	@ApiOperation(value="发票登记-添加", notes="发票登记-添加")
	@PostMapping(value = "/draft")
	public Result<String> draft(@RequestBody PurchasePayInovice purchasePayInovice) {
		purchasePayInoviceService.draftInvoice(purchasePayInovice);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param purchasePayInovice
	 * @return
	 */
	@AutoLog(value = "发票登记-编辑")
	@ApiOperation(value="发票登记-编辑", notes="发票登记-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PurchasePayInovice purchasePayInovice) {
		purchasePayInoviceService.editInvoice(purchasePayInovice);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "发票登记-通过id删除")
	@ApiOperation(value="发票登记-通过id删除", notes="发票登记-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		purchasePayInoviceService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "发票登记-批量删除")
	@ApiOperation(value="发票登记-批量删除", notes="发票登记-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.purchasePayInoviceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "发票登记-通过id查询")
	@ApiOperation(value="发票登记-通过id查询", notes="发票登记-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PurchasePayInovice> queryById(@RequestParam(name="id",required=true) String id) {
		PurchasePayInovice purchasePayInovice = purchasePayInoviceService.getById(id);
		if(purchasePayInovice==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(purchasePayInovice);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param purchasePayInovice
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, PurchasePayInovice purchasePayInovice) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();

		String permission = iSysUserService.fetchPermission(username);
		String auth = "subject";
		String auther = loginUser.getRelTenantIds();
		if(StringUtils.isNotEmpty(permission)){
			if(permission.contains("invoice:all")){
				auth = "all";
			}
		}
		purchasePayInovice.setAuth(auth);
		purchasePayInovice.setAuther(auther);
		return purchasePayInoviceService.exportXls(request, purchasePayInovice, PurchasePayInovice.class, "发票列表");
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
		return super.importExcel(request, response, PurchasePayInovice.class);
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "发票登记-通过id查询")
	@ApiOperation(value="发票登记-通过id查询", notes="发票登记-通过id查询")
	@GetMapping(value = "/queryPurPayInvoiceDetailByMainId")
	public Result<?> queryPurPayInvoiceDetailByMainId(@RequestParam(name="id",required=true) String id) {
		List<ContractObject> pageList = purchasePayInoviceService.queryPurPayInvoiceDetailByMainId(id);
		if(pageList==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(pageList);
	}

	/**
	 * 通过合同id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryPurPayInvoiceByContractId")
	public Result<?> queryPurPayInvoiceByContractId(@RequestParam(name="id",required=true) String id) {
		List<PurchasePayInovice> pageList = purchasePayInoviceService.queryPurPayInvoiceByContractId(id);
		if(pageList==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(pageList);
	}

	/**
	 * 通过合同id查询
	 *
	 * @param invoice
	 * @return
	 */
	@GetMapping(value = "/fetchInvoiceByProjId")
	public Result<?> fetchInvoiceByProjId(PurchasePayInovice invoice) {
		List<PurchasePayInovice> pageList = purchasePayInoviceService.fetchInvoiceByProjId(invoice);
		if(pageList==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(pageList);
	}

}
