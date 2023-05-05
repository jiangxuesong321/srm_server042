package org.jeecg.modules.srm.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sap.conn.jco.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.message.handle.impl.EmailSendMsgHandle;
import org.jeecg.modules.srm.entity.*;
import org.jeecg.modules.srm.mapper.PurchaseOrderAndSapDocumentMapper;
import org.jeecg.modules.srm.mapper.StkIoBillEntryMapper;
import org.jeecg.modules.srm.service.*;
import org.jeecg.modules.srm.utils.JeecgEntityExcel;
import org.jeecg.modules.srm.utils.SAPConnUtil;
import org.jeecg.modules.srm.utils.SapConn;
import org.jeecg.modules.srm.vo.StkIoBillPage;
import org.jeecg.modules.srm.vo.StkIoBillVo;
import org.jeecg.modules.system.entity.PurchaseOrderMain;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.mapper.PurchaseOrderMainMapper;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

 /**
 * @Description: 入库单
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/stkIoBill")
@Slf4j
public class StkIoBillController {
	@Autowired
	private IStkIoBillService stkIoBillService;
	@Autowired
	private IStkIoBillEntryService stkIoBillEntryService;
	@Autowired
	private IApproveRecordService iApproveRecordService;
	@Autowired
	private IStkOoBillService iStkOoBillService;
	@Autowired
	private IStkOoBillDeliveryService iStkOoBillDeliveryService;
	@Autowired
	private ISysUserService iSysUserService;
	@Autowired
	private IBasSupplierContactService iBasSupplierContactService;

	 @Autowired
	 private StkIoBillEntryMapper stkIoBillEntryMapper;

	 @Autowired
	 private PurchaseOrderMainMapper purchaseOrderMainMapper;

	 @Autowired
	 private PurchaseOrderAndSapDocumentMapper purchaseOrderAndSapDocumentMapper;

	/**
	 * 分页列表查询
	 *
	 * @param stkIoBill
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "入库单-分页列表查询")
	@ApiOperation(value="入库单-分页列表查询", notes="入库单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StkIoBill>> queryPageList(StkIoBill stkIoBill,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		//判断当前用户权限
		if("1".equals(stkIoBill.getStatus())){
			LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			String username = loginUser.getUsername();
			String deptId = loginUser.getDepartIds();

			String permission = iSysUserService.fetchPermission(username);
			String auth = "subject";
			String auther = loginUser.getRelTenantIds();
			if(StringUtils.isNotEmpty(permission)){
				if(permission.contains("stk:all")){
					auth = "all";
				}
			}

			stkIoBill.setAuth(auth);
			stkIoBill.setAuther(auther);
		}
		Page<StkIoBill> page = new Page<StkIoBill>(pageNo, pageSize);
		IPage<StkIoBill> pageList = stkIoBillService.queryPageList(page, stkIoBill);
		if (pageList!=null){
			List<StkIoBill> stkIoBillList = pageList.getRecords();
			if (stkIoBillList!=null&& stkIoBillList.size()>0){
				for (StkIoBill sk: stkIoBillList){
					ContractObjectQty contractObjectQty = stkIoBillService.queryOtherDetailsById(sk);
					if (contractObjectQty!=null){
                        sk.setDeviceName(contractObjectQty.getProdName());
						sk.setDeviceSerialNumber(contractObjectQty.getSort());
						sk.setQty(contractObjectQty.getDeviceQty());
						sk.setContractQty(contractObjectQty.getContractQty());
					}
				}
			}
		}
		return Result.OK(pageList);
	}

	 /**
	  * 根据对象里面的属性值作in查询 属性可能会变 用户组件用到
	  * @param obj
	  * @return
	  */
	 @GetMapping("/getMultiContract")
	 public List<StkIoBill> getMultiContract(StkIoBill obj){
		 List<StkIoBill> pageList = stkIoBillService.list(Wrappers.<StkIoBill>query().lambda().
				 in(StkIoBill :: getId,obj.getId().split(",")));
		 return pageList;
	 }
	
	/**
	 *   添加
	 *
	 * @param stkIoBillPage
	 * @return
	 */
	@AutoLog(value = "入库单-添加")
	@ApiOperation(value="入库单-添加", notes="入库单-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody StkIoBillPage stkIoBillPage) {
		StkIoBill stkIoBill = new StkIoBill();
		BeanUtils.copyProperties(stkIoBillPage, stkIoBill);
		stkIoBillService.saveMain(stkIoBill, stkIoBillPage.getStkIoBillEntryList());
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param stkIoBillPage
	 * @return
	 */
	@AutoLog(value = "入库单-编辑")
	@ApiOperation(value="入库单-编辑", notes="入库单-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody StkIoBillPage stkIoBillPage) {
		StkIoBill stkIoBill = new StkIoBill();
		BeanUtils.copyProperties(stkIoBillPage, stkIoBill);
		StkIoBill stkIoBillEntity = stkIoBillService.getById(stkIoBill.getId());
		if(stkIoBillEntity==null) {
			return Result.error("未找到对应数据");
		}
		stkIoBillService.updateMain(stkIoBill, stkIoBillPage.getStkIoBillEntryList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "入库单-通过id删除")
	@ApiOperation(value="入库单-通过id删除", notes="入库单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		stkIoBillService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "入库单-批量删除")
	@ApiOperation(value="入库单-批量删除", notes="入库单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.stkIoBillService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "入库单-通过id查询")
	@ApiOperation(value="入库单-通过id查询", notes="入库单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StkIoBill> queryById(@RequestParam(name="id",required=true) String id) {
		StkIoBill stkIoBill = stkIoBillService.getById(id);
		if(stkIoBill==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(stkIoBill);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "入库单明细通过主表ID查询")
	@ApiOperation(value="入库单明细主表ID查询", notes="入库单明细-通主表ID查询")
	@GetMapping(value = "/queryStkIoBillEntryByMainId")
	public Result<List<StkIoBillEntry>> queryStkIoBillEntryListByMainId(@RequestParam(name="id",required=true) String id) {
		List<StkIoBillEntry> stkIoBillEntryList = stkIoBillEntryService.selectByMainId(id);
		return Result.OK(stkIoBillEntryList);
	}

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "入库单明细通过主表ID查询")
	 @ApiOperation(value="入库单明细主表ID查询", notes="入库单明细-通主表ID查询")
	 @GetMapping(value = "/queryDetailListByMainId")
	 public Result<List<StkIoBillEntry>> queryDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		 List<StkIoBillEntry> stkIoBillEntryList = stkIoBillEntryService.queryDetailListByMainId(id);
		 return Result.OK(stkIoBillEntryList);
	 }

    /**
    * 入库导出excel
    *
    * @param request
    * @param stkIoBill
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StkIoBill stkIoBill) {
		//判断当前用户权限
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		String deptId = loginUser.getDepartIds();

		String permission = iSysUserService.fetchPermission(username);
		String auth = "subject";
		String auther = loginUser.getRelTenantIds();
		if(StringUtils.isNotEmpty(permission)){
			if(permission.contains("stk:all")){
				auth = "all";
			}
		}

		stkIoBill.setAuth(auth);
		stkIoBill.setAuther(auther);

		List<StkIoBill> pageList = stkIoBillService.exportXls(stkIoBill);
		if (pageList !=null && pageList.size() > 0){
			List<String> ids = new ArrayList<>();
			for (StkIoBill sk: pageList){
				ids.add(sk.getId());
			}


			List<ContractObjectQty> qtyList = stkIoBillService.queryOtherDetailsByIds(ids);

			for (StkIoBill stk: pageList){
				for(ContractObjectQty coq : qtyList){
					if(stk.getId().equals(coq.getMid())){
						stk.setDeviceName(coq.getProdName());
						stk.setDeviceSerialNumber(coq.getSort());
						stk.setQty(coq.getDeviceQty());
						stk.setContractQty(coq.getContractQty());
						break;
					}
				}
			}
		}

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcel());
      mv.addObject(NormalExcelConstants.FILE_NAME, "入库列表");
      mv.addObject(NormalExcelConstants.CLASS, StkIoBill.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("入库列表", "导出人:"+loginUser.getRealname(), "入库列表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

	 /**
	  * 收货导出excel
	  *
	  * @param request
	  * @param stkIoBill
	  */
	 @RequestMapping(value = "/exportXls1")
	 public ModelAndView exportXls1(HttpServletRequest request, StkIoBill stkIoBill) {
		 //判断当前用户权限
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String username = loginUser.getUsername();
		 String deptId = loginUser.getDepartIds();

		 String permission = iSysUserService.fetchPermission(username);
		 String auth = "subject";
		 String auther = loginUser.getRelTenantIds();
		 if(StringUtils.isNotEmpty(permission)){
			 if(permission.contains("stk:all")){
				 auth = "all";
			 }
		 }

		 stkIoBill.setAuth(auth);
		 stkIoBill.setAuther(auther);

		 List<StkIoBill> pageList = stkIoBillService.exportXls(stkIoBill);
		 List<StkIoBillVo> exportList = new ArrayList<>();
 		 if (pageList !=null && pageList.size() > 0){
			 List<String> ids = new ArrayList<>();
			 for (StkIoBill sk: pageList){
				 ids.add(sk.getId());
			 }


			 List<ContractObjectQty> qtyList = stkIoBillService.queryOtherDetailsByIds(ids);

			 for (StkIoBill stk: pageList){
				 StkIoBillVo vo = new StkIoBillVo();
				 for(ContractObjectQty coq : qtyList){
					 if(stk.getId().equals(coq.getMid())){
						 stk.setDeviceName(coq.getProdName());
						 stk.setDeviceSerialNumber(coq.getSort());
						 stk.setQty(coq.getDeviceQty());
						 stk.setContractQty(coq.getContractQty());
						 break;
					 }
				 }
				 if("0".equals(stk.getSendStatus())){
					 stk.setSendStatus("待审核");
				 }else if("1".equals(stk.getSendStatus())){
					 stk.setSendStatus("驳回");
				 }else if("2".equals(stk.getSendStatus())){
					 stk.setSendStatus("审核通过");
				 }
				 BeanUtils.copyProperties(stk,vo);
				 exportList.add(vo);
			 }
		 }

		 // Step.4 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcel());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "收货管理");
		 mv.addObject(NormalExcelConstants.CLASS, StkIoBillVo.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("收货管理", "导出人:"+loginUser.getRealname(), "收货管理"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          // 获取上传文件对象
          MultipartFile file = entity.getValue();
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<StkIoBillPage> list = ExcelImportUtil.importExcel(file.getInputStream(), StkIoBillPage.class, params);
              for (StkIoBillPage page : list) {
                  StkIoBill po = new StkIoBill();
                  BeanUtils.copyProperties(page, po);
                  stkIoBillService.saveMain(po, page.getStkIoBillEntryList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

	 /**
	  * 设备到厂数量统计
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "入库单-分页列表查询")
	 @ApiOperation(value="入库单-分页列表查询", notes="入库单-分页列表查询")
	 @GetMapping(value = "/fetchArrivalQty")
	 public Result<?> fetchArrivalQty(StkIoBillEntry entry,
												   HttpServletRequest req) {
		 StkIoBillEntry pageList = stkIoBillService.fetchArrivalQty(entry);
		 return Result.OK(pageList);
	 }

	 /**
	  * 库存信息
	  *
	  * @param stkIoBill
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/queryDetailPageList")
	 public Result<IPage<StkIoBillEntry>> queryDetailPageList(StkIoBillEntry stkIoBill,
												   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												   HttpServletRequest req) {
		 Page<StkIoBillEntry> page = new Page<StkIoBillEntry>(pageNo, pageSize);
		 IPage<StkIoBillEntry> pageList = stkIoBillService.queryDetailPageList(page, stkIoBill);
		 return Result.OK(pageList);
	 }
	 /**
	  *  驳回
	  *
	  * @return
	  */
	 @AutoLog(value = "入库单-驳回")
	 @RequestMapping(value = "/toReject", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> toReject(@RequestBody StkIoBill stkIoBill) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String username = loginUser.getUsername();
		 Date nowTime = new Date();

		 stkIoBill.setApproverId("prod_line");
		 stkIoBill.setUpdateBy(username);
		 stkIoBill.setUpdateTime(nowTime);
		 stkIoBillService.updateById(stkIoBill);
		 //发送邮件
//		 if(CommonConstant.HAS_SEND.equals(stkIoBill.getIsSendMail())){
//		 	//获取供应商邮箱
//			 String supplierId = stkIoBill.getSuppId();
//			 List<BasSupplierContact> contactList = iBasSupplierContactService.list(Wrappers.<BasSupplierContact>query().lambda().
//					 eq(BasSupplierContact :: getDelFlag,CommonConstant.DEL_FLAG_0).
//					 eq(BasSupplierContact :: getIsDefault,CommonConstant.ACT_SYNC_1).
//					 eq(BasSupplierContact :: getSupplierId,supplierId));
//			 if(contactList != null && contactList.size() > 0){
//				 List<String> emails = new ArrayList<>();
//				 emails.add(contactList.get(0).getContacterEmail());
//				 EmailSendMsgHandle emailHandle=new EmailSendMsgHandle();
//				 String context = "发货申请已驳回,请重新上传签收单";
//				 emailHandle.sendTemplateMail("发货申请",context,emails,null,"job");
//			 }
//		 }
		 return Result.OK("编辑成功!");
	 }


	 /**
	  *  编辑
	  *
	  * @return
	  */
	 @AutoLog(value = "入库单-审批")
	 @RequestMapping(value = "/toApprove", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> toApprove(@RequestBody StkIoBill stkIoBill) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String username = loginUser.getUsername();
		 Date nowTime = new Date();
		 StkIoBill exist = new StkIoBill();
		 BeanUtils.copyProperties(stkIoBill,exist);

		 if("prod_line".equals(exist.getApproverId())){
			 exist.setApproverId("project_center");
			 exist.setCreateBy(username);

			 exist.setCheckStatus("0");
			 exist.setCheckApprove("project_center");

			 List<StkIoBillEntry> recordList = stkIoBill.getRecordList();
			 stkIoBillEntryService.updateBatchById(recordList);
		 }else if("project_center".equals(exist.getApproverId())){
			 exist.setApproverId("");
			 exist.setStatus("1");
			 exist.setIsOut(CommonConstant.HAS_SEND);
		 }
		 exist.setOtherAttachment(stkIoBill.getOtherAttachment());
		 exist.setAttachment(stkIoBill.getAttachment());
		 exist.setWhId(stkIoBill.getWhId());
		 exist.setActualArrivalDate(stkIoBill.getActualArrivalDate());
		 exist.setDeliveryDate(stkIoBill.getDeliveryDate());
		 exist.setEstimatedTime(stkIoBill.getEstimatedTime());

		 exist.setUpdateBy(username);
		 exist.setUpdateTime(nowTime);
		 //审批通过默认自动出库
		 if("1".equals(exist.getStatus())){
			 //生成出库记录
			 JSONObject formData = new JSONObject();
			 formData.put("prefix", "DO");
			 String code = (String) FillRuleUtil.executeRule("OO_CODE", formData);

			 StkOoBill bill = new StkOoBill();
			 BeanUtils.copyProperties(exist,bill);
			 bill.setBillNo(code);
			 bill.setCreateTime(nowTime);
			 bill.setCreateBy(exist.getCreateBy());
			 bill.setUpdateTime(nowTime);
			 bill.setUpdateBy(exist.getCreateBy());
			 bill.setBillDate(nowTime);
			 bill.setStockIoType("0");
			 bill.setDelFlag(CommonConstant.NO_READ_FLAG);
			 iStkOoBillService.save(bill);

			 List<StkIoBillEntry> detailList = stkIoBillEntryService.list(Wrappers.<StkIoBillEntry>query().lambda().
					 eq(StkIoBillEntry :: getDelFlag,CommonConstant.DEL_FLAG_0).
					 eq(StkIoBillEntry :: getMid,exist.getId()));

			 List<StkOoBillDelivery> ooList = new ArrayList<>();
			 for(StkIoBillEntry se : detailList){
				 StkOoBillDelivery sd = new StkOoBillDelivery();
				 BeanUtils.copyProperties(se,sd);
				 sd.setMid(bill.getId());
				 sd.setCreateBy(exist.getCreateBy());
				 sd.setCreateTime(nowTime);
				 sd.setUpdateTime(nowTime);
				 sd.setUpdateBy(exist.getCreateBy());
				 sd.setDelFlag(CommonConstant.NO_READ_FLAG);
				 ooList.add(sd);
			 }
			 iStkOoBillDeliveryService.saveBatch(ooList);
		 }
		 stkIoBillService.updateById(exist);
		 //费用分摊
		 BigDecimal exchangeRate = stkIoBill.getExchangeRate();

		 //金额分摊到每一条明细
		 BigDecimal totalAmount = stkIoBill.getTotalAmount().subtract(stkIoBill.getContractAmountTaxLocal());
		 if(totalAmount.compareTo(new BigDecimal(0)) == 1){
			 List<StkIoBillEntry> stkIoBillEntryList = stkIoBillEntryService.list(Wrappers.<StkIoBillEntry>query().lambda().
					 eq(StkIoBillEntry :: getMid,exist.getId()).
					 eq(StkIoBillEntry :: getDelFlag,CommonConstant.DEL_FLAG_0));
			 int size = stkIoBillEntryList.size();
			 BigDecimal avgAmount = totalAmount.divide(new BigDecimal(size),2,BigDecimal.ROUND_HALF_UP);
			 BigDecimal amount = BigDecimal.ZERO;
			 int i = 1;

			 for(StkIoBillEntry sie : stkIoBillEntryList){
				 //金额分摊
				 if(totalAmount.compareTo(BigDecimal.ZERO) == 1){
					 if(i == size){
						 sie.setOtherAmountLocal(totalAmount.subtract(amount).setScale(2,BigDecimal.ROUND_HALF_UP));
					 }else{
						 sie.setOtherAmountLocal(avgAmount);
					 }
					 BigDecimal otherAmount = sie.getOtherAmountLocal().multiply(exchangeRate).setScale(2,BigDecimal.ROUND_HALF_UP);
					 sie.setOtherAmount(otherAmount);
					 amount = amount.add(avgAmount);
					 i++;
				 }
				 //根据汇率重新计算合同金额
				 BigDecimal priceLocal = sie.getContractPrice().divide(exchangeRate,4,BigDecimal.ROUND_HALF_UP);
				 sie.setContractPriceLocal(priceLocal);

				 BigDecimal priceTaxLocal = sie.getContractPriceTax().divide(exchangeRate,4,BigDecimal.ROUND_HALF_UP);
				 sie.setContractPriceTaxLocal(priceTaxLocal);

				 BigDecimal amountLocal = sie.getContractAmount().divide(exchangeRate,2,BigDecimal.ROUND_HALF_UP);
				 sie.setContractAmountLocal(amountLocal);

				 BigDecimal amountTaxLocal = sie.getContractAmountTax().divide(exchangeRate,2,BigDecimal.ROUND_HALF_UP);
				 sie.setContractAmountTaxLocal(amountTaxLocal);

			 }
			 stkIoBillEntryService.updateBatchById(stkIoBillEntryList);
		 }
		 //SRM 推送接口sap收货
		 if (stkIoBill.getApproverId() != null && stkIoBill.getApproverId().equals("project_center")) {
			 this.sendPOToSap(stkIoBill);
		 }
		 return Result.OK("编辑成功!");
	 }

	 public void sendPOToSap(StkIoBill stkIoBill) {
		 //查询收货单列表
		 List<StkIoBillEntry> sbeList = stkIoBillEntryMapper.selectByMainId(stkIoBill.getId());

		 //查找po号
		 try {
			 LambdaQueryWrapper<PurchaseOrderMain> qy = new LambdaQueryWrapper<>();
			 qy.eq(PurchaseOrderMain::getContactId, stkIoBill.getContractId());
			 PurchaseOrderMain purchaseOrderMain = purchaseOrderMainMapper.selectOne(qy);
			 String po = purchaseOrderMain.getSapPo();

			 String JCO_HOST = "192.168.1.20";
			 String JCO_SYNSNR = "00";
			 String JCO_CLIENT = "200";
			 String JCO_USER = "DLW_PDA";
			 String JCO_PASSWD = "Delaware.001";
			 String JCO_LANG = "ZH";
			 String JCO_POOL_CAPACITY = "30";
			 String JCO_PEAK_LIMIT = "100";
			 String JCO_SAPROUTER = "/H/112.103.135.101/S/3299/W/Dch2017";

			 SapConn con = new SapConn(JCO_HOST, JCO_SYNSNR, JCO_CLIENT, JCO_USER, JCO_PASSWD, JCO_LANG, JCO_POOL_CAPACITY, JCO_PEAK_LIMIT, JCO_SAPROUTER);
			 JCoDestination jCoDestination = SAPConnUtil.connect(con);

			 // 获取调用 RFC 函数对象
			 JCoFunction func = jCoDestination.getRepository().getFunction("ZCFIFUN_MM_PO_DELIVERY");
			 // 配置传入参数抬头信息
			 JCoParameterList importParameterList = func.getImportParameterList();
			 JCoStructure sc = importParameterList.getStructure("IS_HEAD");
			 sc.setValue("EBELN", po);

			 //行项目
			 JCoParameterList tableList =   func.getTableParameterList();
			 JCoTable item_table =  tableList.getTable("IT_ITEM");
			 for (int i = 0; i< sbeList.size();i++){
				 item_table.appendRow();
				 item_table.setValue("ERFMG", sbeList.get(i).getQty());
//					item_table.setValue("MATNR", "10000045");
				 item_table.setValue("EBELN", po);
				 item_table.setValue("MATNR", sbeList.get(i).getProdCode());

				 System.out.println("EBELN"+ po);
				 System.out.println("MATNR"+ sbeList.get(i).getProdCode());
				 System.out.println("ERFMG"+ sbeList.get(i).getQty());
			 }
			 // 调用并获取返回值
			 func.execute(jCoDestination);

			 //获取返回值
			 String materialDocument = func.getExportParameterList().getString("EV_MBLNR");
			 String year = func.getExportParameterList().getString("EV_MJAHR");
			 if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotEmpty(materialDocument)) {
				 PurchaseOrderAndSapDocument purchaseOrderAndSapDocument = new PurchaseOrderAndSapDocument();
				 BeanUtils.copyProperties(purchaseOrderMain, purchaseOrderAndSapDocument);
				 purchaseOrderAndSapDocument.setMaterialDocument(materialDocument);
				 purchaseOrderAndSapDocument.setYear(year);
				 purchaseOrderAndSapDocument.setBillNo(stkIoBill.getBillNo());
				 purchaseOrderAndSapDocumentMapper.insert(purchaseOrderAndSapDocument);
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	 }


	 /**
	  *  编辑
	  *
	  * @return
	  */
	 @AutoLog(value = "入库单-验收")
	 @RequestMapping(value = "/toApproveCheck", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> toApproveCheck(@RequestBody StkIoBill stkIoBill) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String username = loginUser.getUsername();
		 Date nowTime = new Date();
		 StkIoBill exist = new StkIoBill();
		 BeanUtils.copyProperties(stkIoBill,exist);

		 if("prod_line".equals(exist.getCheckApprove())){
			 exist.setCheckApprove("project_center");
			 exist.setCreateBy(username);

		 }else if("project_center".equals(exist.getCheckApprove())){
			 exist.setCheckApprove("");
			 exist.setCheckStatus("1");
		 }

		 exist.setUpdateBy(username);
		 exist.setUpdateTime(nowTime);

		 stkIoBillService.updateById(exist);

		 return Result.OK("编辑成功!");
	 }

	 /**
	  * 获取审核原因
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/queryApprove")
	 public Result<ApproveRecord> queryApprove(@RequestParam(name="id",required=true) String id) {
		 ApproveRecord record = new ApproveRecord();
		 List<ApproveRecord> recordList = iApproveRecordService.list(Wrappers.<ApproveRecord>query().lambda().
				 eq(ApproveRecord :: getBusinessId,id).
				 eq(ApproveRecord :: getDelFlag,CommonConstant.DEL_FLAG_0).
				 orderByDesc(ApproveRecord :: getCreateTime));
		 if(recordList != null && recordList.size() > 0){
			 record = recordList.get(0);
		 }
		 return Result.OK(record);
	 }

	 /**
	  *   发货审批通过
	  *
	  * @param stkIoBill
	  * @return
	  */
	 @AutoLog(value = "发货审批通过")
	 @PostMapping(value = "/handleSendPass")
	 public Result<String> handleSendPass(@RequestBody StkIoBill stkIoBill) {
		 stkIoBillService.handleSendPass(stkIoBill);

		 return Result.OK("提交成功！");
	 }

	 /**
	  *   发货驳回
	  *
	  * @param stkIoBill
	  * @return
	  */
	 @AutoLog(value = "发货审批通过")
	 @PostMapping(value = "/handleSendReject")
	 public Result<String> handleSendReject(@RequestBody StkIoBill stkIoBill) {
		 stkIoBillService.handleSendReject(stkIoBill);

		 return Result.OK("提交成功！");
	 }

	 /**
	  *  指定审核人
	  *
	  * @return
	  */
	 @AutoLog(value = "指定审核人")
	 @RequestMapping(value = "/toApproverUser", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> toApproverUser(@RequestBody StkIoBill stkIoBill) throws Exception {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String username = loginUser.getUsername();
		 Date nowTime = new Date();
		 //判断使用部门是否已经审批过了
		 ApproveRecord ar = iApproveRecordService.getOne(Wrappers.<ApproveRecord>query().lambda().
				 eq(ApproveRecord :: getDelFlag,CommonConstant.DEL_FLAG_0).
				 eq(ApproveRecord :: getBusinessId,stkIoBill.getSendProcessId()).
				 eq(ApproveRecord :: getType,"send").
				 eq(ApproveRecord :: getName,"prod_line"));

		 if(ar != null){
		 	throw new Exception("使用部门已审批,不能更换");
		 }
		 stkIoBill.setUpdateBy(username);
		 stkIoBill.setUpdateTime(nowTime);
		 stkIoBillService.updateById(stkIoBill);

		 //发送邮件
//		 SysUser sysUser = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,stkIoBill.getApproverUser()));
//		 List<String> emails = new ArrayList<>();
//		 if(sysUser != null){
//			 emails.add(sysUser.getEmail());
//			 EmailSendMsgHandle emailHandle=new EmailSendMsgHandle();
//			 String context = "您已被指定为收货审核人,请前往收货管理列表进行审核";
//			 emailHandle.sendTemplateMail("收货审核",context,emails,null,"job");
//		 }

		 return Result.OK("提交成功!");
	 }

 }
