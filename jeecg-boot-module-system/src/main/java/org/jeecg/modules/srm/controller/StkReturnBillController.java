package org.jeecg.modules.srm.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.modules.srm.entity.*;
import org.jeecg.modules.srm.service.*;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

 /**
 * @Description: stk_return_bill
 * @Author: jeecg-boot
 * @Date:   2022-10-10
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/stkReturnBill")
@Slf4j
public class StkReturnBillController extends JeecgController<StkReturnBill, IStkReturnBillService> {
	@Autowired
	private IStkReturnBillService stkReturnBillService;
	@Autowired
	private IApproveRecordService iApproveRecordService;
	@Autowired
	private IStkOoBillService iStkOoBillService;
	@Autowired
	private IStkReturnBillEntryService iStkReturnBillEntryService;
	@Autowired
	private IStkOoBillDeliveryService iStkOoBillDeliveryService;
	@Autowired
	private IContractBaseService iContractBaseService;
	@Autowired
	private ISysUserService iSysUserService;
	
	/**
	 * 分页列表查询
	 *
	 * @param stkReturnBill
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="stk_return_bill-分页列表查询", notes="stk_return_bill-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StkReturnBill>> queryPageList(StkReturnBill stkReturnBill,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
//		QueryWrapper<StkReturnBill> queryWrapper = QueryGenerator.initQueryWrapper(stkReturnBill, req.getParameterMap());
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		String deptId = loginUser.getDepartIds();

		String permission = iSysUserService.fetchPermission(username);
		String auth = "owner";
		String auther = username;
		if(StringUtils.isNotEmpty(permission)){
			if(permission.contains("stk:all")){
				auth = "all";
			}else if(permission.contains("stk:subject")){
				auth = "subject";
				//当前用户属于那个主体
				String subject = loginUser.getRelTenantIds();
				auther = subject;
			}else if(permission.contains("stk:dept")){
				auth = "dept";
				auther = deptId;
			}
		}

		stkReturnBill.setAuth(auth);
		stkReturnBill.setAuther(auther);

		Page<StkReturnBill> page = new Page<StkReturnBill>(pageNo, pageSize);
		IPage<StkReturnBill> pageList = stkReturnBillService.queryPageList(page, stkReturnBill);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param stkReturnBill
	 * @return
	 */
	@AutoLog(value = "stk_return_bill-添加")
	@ApiOperation(value="stk_return_bill-添加", notes="stk_return_bill-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody StkReturnBill stkReturnBill) {
		stkReturnBillService.saveMain(stkReturnBill);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param stkReturnBill
	 * @return
	 */
	@AutoLog(value = "stk_return_bill-编辑")
	@ApiOperation(value="stk_return_bill-编辑", notes="stk_return_bill-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody StkReturnBill stkReturnBill) {
		stkReturnBillService.editMain(stkReturnBill);
		return Result.OK("编辑成功!");
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
		 List<StkIoBillEntry> stkIoBillEntryList = stkReturnBillService.queryDetailListByMainId(id);
		 return Result.OK(stkIoBillEntryList);
	 }
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "stk_return_bill-通过id删除")
	@ApiOperation(value="stk_return_bill-通过id删除", notes="stk_return_bill-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		stkReturnBillService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "stk_return_bill-批量删除")
	@ApiOperation(value="stk_return_bill-批量删除", notes="stk_return_bill-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.stkReturnBillService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "stk_return_bill-通过id查询")
	@ApiOperation(value="stk_return_bill-通过id查询", notes="stk_return_bill-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StkReturnBill> queryById(@RequestParam(name="id",required=true) String id) {
		StkReturnBill stkReturnBill = stkReturnBillService.getById(id);
		if(stkReturnBill==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(stkReturnBill);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param stkReturnBill
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StkReturnBill stkReturnBill) {
        return super.exportXls(request, stkReturnBill, StkReturnBill.class, "stk_return_bill");
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
        return super.importExcel(request, response, StkReturnBill.class);
    }

	 /**
	  *  编辑
	  *
	  * @return
	  */
	 @AutoLog(value = "退货审批")
	 @RequestMapping(value = "/toApprove", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> toApprove(@RequestBody StkReturnBill stkReturnBill) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String username = loginUser.getUsername();
		 Date nowTime = new Date();

		 StkReturnBill exist = stkReturnBillService.getById(stkReturnBill.getId());
		 String status = stkReturnBill.getStatus();
		 stkReturnBill.setUpdateBy(username);
		 stkReturnBill.setUpdateTime(nowTime);
		 stkReturnBill.setApproverId("finance");
		 //审批通过默认自动出库
		 if("1".equals(stkReturnBill.getStatus()) && "finance".equals(exist.getApproverId())){
			 stkReturnBill.setStatus("1");
			 stkReturnBill.setApproverId("");
			 //生成出库记录
			 JSONObject formData = new JSONObject();
			 formData.put("prefix", "DO");
			 String code = (String) FillRuleUtil.executeRule("OO_CODE", formData);

			 //合同
			 ContractBase contractBase = iContractBaseService.getById(exist.getContractId());

			 StkOoBill bill = new StkOoBill();
			 BeanUtils.copyProperties(exist,bill);
			 bill.setBillNo(code);
			 bill.setCreateTime(nowTime);
			 bill.setCreateBy(exist.getCreateBy());
			 bill.setUpdateTime(nowTime);
			 bill.setUpdateBy(exist.getCreateBy());
			 bill.setBillDate(nowTime);
			 bill.setStockIoType("1");
			 bill.setDelFlag(CommonConstant.NO_READ_FLAG);
			 bill.setContractNumber(contractBase.getContractNumber());
			 iStkOoBillService.save(bill);


			 List<StkReturnBillEntry> detailList = iStkReturnBillEntryService.list(Wrappers.<StkReturnBillEntry>query().lambda().
					 eq(StkReturnBillEntry :: getDelFlag,CommonConstant.DEL_FLAG_0).
					 eq(StkReturnBillEntry :: getMid,exist.getId()));

			 List<StkOoBillDelivery> ooList = new ArrayList<>();
			 for(StkReturnBillEntry se : detailList){
				 StkOoBillDelivery sd = new StkOoBillDelivery();
				 BeanUtils.copyProperties(se,sd);
				 sd.setMid(bill.getId());
				 sd.setCreateBy(exist.getCreateBy());
				 sd.setCreateTime(nowTime);
				 sd.setUpdateTime(nowTime);
				 sd.setUpdateBy(exist.getCreateBy());
				 sd.setDelFlag(CommonConstant.NO_READ_FLAG);
				 sd.setOrderDetailId(se.getBillDetailId());
				 sd.setOrderId(bill.getContractId());
				 ooList.add(sd);
			 }
			 iStkOoBillDeliveryService.saveBatch(ooList);
		 }else{
			 stkReturnBill.setStatus("0");
		 }
		 stkReturnBillService.updateById(stkReturnBill);

		 ApproveRecord approve = new ApproveRecord();
		 approve.setApprover(stkReturnBill.getApprover());
		 approve.setApproveComment(stkReturnBill.getApproveComment());
		 approve.setCreateTime(nowTime);
		 approve.setUpdateTime(nowTime);
		 approve.setUpdateUser(username);
		 approve.setCreateUser(username);
		 approve.setSort(exist.getVersion());
		 approve.setStatus(status);
		 approve.setBusinessId(stkReturnBill.getId());
		 approve.setDelFlag(CommonConstant.NO_READ_FLAG);
		 iApproveRecordService.save(approve);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  * 获取审核原因
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/queryApprove")
	 public Result<?> queryApprove(@RequestParam(name="id",required=true) String id) {
	 	StkReturnBill bill = stkReturnBillService.getById(id);
		 List<ApproveRecord> recordList = iApproveRecordService.list(Wrappers.<ApproveRecord>query().lambda().
				 eq(ApproveRecord :: getBusinessId,id).
				 eq(ApproveRecord :: getSort,bill.getVersion()).
				 orderByDesc(ApproveRecord :: getCreateTime));
		 return Result.OK(recordList);
	 }

}
