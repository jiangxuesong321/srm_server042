package org.jeecg.modules.srm.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.message.handle.impl.EmailSendMsgHandle;
import org.jeecg.modules.srm.entity.*;
import org.jeecg.modules.srm.service.*;
import org.jeecg.modules.srm.vo.BiddingSupplierPage;
import org.jeecg.modules.srm.vo.BiddingTemplatePage;
import org.jeecg.modules.srm.vo.FixBiddingPage;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.srm.vo.BiddingMainPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 招标主表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/biddingMain")
@Slf4j
public class BiddingMainController {
	@Autowired
	private IBiddingMainService biddingMainService;
	@Autowired
	private IBiddingRecordService biddingRecordService;
	@Autowired
	private IBiddingSupplierService biddingSupplierService;
	@Autowired
	private IBiddingProfessionalsService biddingProfessionalsService;
	@Autowired
	private IPurchaseRequestDetailService iPurchaseRequestDetailService;
	@Autowired
	private ISysUserService iSysUserService;
	@Autowired
	private IApproveSettingService iApproveSettingService;
	@Autowired
	private IContractBaseService iContractBaseService;
	@Autowired
	private IBiddingRecordSupplierService iBiddingRecordSupplierService;
	@Autowired
	private IBasSupplierService iBasSupplierService;
	@Autowired
	private ISupplierAccountService iSupplierAccountService;
	@Autowired
	private IPurchaseRequestMainService iPurchaseRequestMainService;
	@Autowired
	private ISysDepartService iSysDepartService;
	@Autowired
	private IProjBaseService iProjBaseService;

	 private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	 /**
	  * 分页列表查询
	  *
	  * @param biddingMain
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "招标主表-分页列表查询")
	 @ApiOperation(value="招标主表-分页列表查询", notes="招标主表-分页列表查询")
	 @GetMapping(value = "/pageList")
	 public Result<IPage<BiddingMain>> pageList(BiddingMain biddingMain,
													 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													 HttpServletRequest req) {
		 //判断当前用户权限
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String username = loginUser.getUsername();
		 String deptId = loginUser.getDepartIds();

		 String permission = iSysUserService.fetchPermission(username);
		 String auth = "owner";
		 String auther = username;
		 if(StringUtils.isNotEmpty(permission)){
			 if(permission.contains("bidding:all")){
				 auth = "all";
			 }else if(permission.contains("bidding:subject")){
				 auth = "subject";
				 //当前用户属于那个主体
				 String subject = loginUser.getRelTenantIds();
				 auther = subject;
			 }else if(permission.contains("bidding:dept")){
				 auth = "dept";
				 auther = deptId;
			 }
		 }

		 biddingMain.setAuth(auth);
		 biddingMain.setAuther(auther);

		 Page<BiddingMain> page = new Page<BiddingMain>(pageNo, pageSize);
		 IPage<BiddingMain> pageList = biddingMainService.pageList(page, biddingMain);
		 return Result.OK(pageList);
	 }

	/**
	 * 分页列表查询
	 *
	 * @param biddingMain
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "招标主表-分页列表查询")
	@ApiOperation(value="招标主表-分页列表查询", notes="招标主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BiddingMain>> queryPageList(BiddingMain biddingMain,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		//如果来源是todo，则需要判断是否权限
//		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//		String username = loginUser.getUsername();
//		if("todo".equals(biddingMain.getSource())){
//			Boolean flag = iSysUserService.checkPermission("todo:all",username);
//			if(!flag){
//				List<ApproveSetting> setList = iApproveSettingService.list(Wrappers.<ApproveSetting>query().lambda().
//						eq(ApproveSetting :: getDelFlag, CommonConstant.DEL_FLAG_0).
//						eq(ApproveSetting :: getUsername,username).
//						eq(ApproveSetting :: getType,"contract"));
//				if(setList == null || setList.size() == 0){
//					return Result.OK(new Page<>());
//				}
//			}
//		}
		if("todo".equals(biddingMain.getSource())){
			//判断当前用户权限
			LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			String username = loginUser.getUsername();
			String deptId = loginUser.getDepartIds();

			String permission = iSysUserService.fetchPermission(username);
			String auth = "owner";
			String auther = username;
			if(StringUtils.isNotEmpty(permission)){
				if(permission.contains("contract:all")){
					auth = "all";
				}else if(permission.contains("contract:subject")){
					auth = "subject";
					//当前用户属于那个主体
					String subject = loginUser.getRelTenantIds();
					auther = subject;
				}else if(permission.contains("contract:dept")){
					auth = "dept";
					auther = deptId;
				}
			}

			biddingMain.setAuth(auth);
			biddingMain.setAuther(auther);
		}

		Page<BiddingMain> page = new Page<BiddingMain>(pageNo, pageSize);
		IPage<BiddingMain> pageList = biddingMainService.queryPageList(page, biddingMain);
		for(BiddingMain bm : pageList.getRecords()){
			bm.setBiddingCurrency(bm.getCurrency());
		}
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param biddingMainPage
	 * @return
	 */
	@AutoLog(value = "招标主表-添加")
	@ApiOperation(value="招标主表-添加", notes="招标主表-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BiddingMainPage biddingMainPage) {
		BiddingMain biddingMain = new BiddingMain();
		BeanUtils.copyProperties(biddingMainPage, biddingMain);
		biddingMainService.saveMain(biddingMain, biddingMainPage.getRecordList(),biddingMainPage.getSuppList(),biddingMainPage.getTemplateList(),biddingMainPage.getTemplateList1());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param biddingMainPage
	 * @return
	 */
	@AutoLog(value = "招标主表-编辑")
	@ApiOperation(value="招标主表-编辑", notes="招标主表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BiddingMainPage biddingMainPage) {
		BiddingMain biddingMain = new BiddingMain();
		BeanUtils.copyProperties(biddingMainPage, biddingMain);
		BiddingMain biddingMainEntity = biddingMainService.getById(biddingMain.getId());
		if(biddingMainEntity==null) {
			return Result.error("未找到对应数据");
		}
		biddingMainService.updateMain(biddingMain, biddingMainPage.getRecordList(),biddingMainPage.getSuppList(),biddingMainPage.getTemplateList(),biddingMainPage.getTemplateList1());
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "招标主表-通过id查询")
	@ApiOperation(value="招标主表-通过id查询", notes="招标主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BiddingMain> queryById(@RequestParam(name="id",required=true) String id) {
		BiddingMain biddingMain = biddingMainService.getById(id);
		if(biddingMain==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(biddingMain);

	}

	 /**
	  * 招标明细
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/queryRecordList")
	 public Result<List<BiddingRecord>> queryRecordList(@RequestParam(name="id",required=true) String id) {
		 List<BiddingRecord> biddingRecordList = biddingRecordService.queryRecordList(id);
		 return Result.OK(biddingRecordList);
	 }

	 /**
	  * 招标明细
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/queryRecordListByMainId")
	 public Result<List<BiddingRecord>> queryRecordListByMainId(@RequestParam(name="id",required=true) String id) {
		 List<BiddingRecord> biddingRecordList = biddingRecordService.queryRecordListByMainId(id);
		 return Result.OK(biddingRecordList);
	 }

	 /**
	  * 招标供应商
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/querySuppList")
	 public Result<List<BiddingSupplier>> querySuppList(@RequestParam(name="id",required=true) String id) {
		 List<BiddingSupplier> biddingRecordList = biddingRecordService.querySuppList(id);
		 return Result.OK(biddingRecordList);
	 }

	 /**
	  * 评标模板
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/queryTemplateList")
	 public Result<List<BiddingProfessionals>> queryTemplateList(@RequestParam(name="id",required=true) String id) {
		 List<BiddingProfessionals> biddingRecordList = biddingRecordService.queryTemplateList(id);
		 return Result.OK(biddingRecordList);
	 }

	 /**
	  *  编辑
	  *
	  * @param biddingMain
	  * @return
	  */
	 @AutoLog(value = "招标主表-编辑")
	 @ApiOperation(value="招标主表-编辑", notes="招标主表-编辑")
	 @RequestMapping(value = "/editEntity", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> editEntity(@RequestBody BiddingMain biddingMain) throws Exception {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String username = loginUser.getUsername();
		 Date nowTime = new Date();

		 biddingMain.setUpdateUser(username);
		 biddingMain.setUpdateTime(nowTime);
		 biddingMainService.updateById(biddingMain);
		 //如果是废标,则需要把数量还回去
		 if("4".equals(biddingMain.getBiddingStatus())){
		 	//判断合同是否生成
		 	List<ContractBase> cbList = iContractBaseService.list(Wrappers.<ContractBase>query().lambda().
				 eq(ContractBase :: getDelFlag,CommonConstant.DEL_FLAG_0).
				 eq(ContractBase :: getRequestId,biddingMain.getId()));
		 	if(cbList != null && cbList.size() > 0){
			 throw new Exception("已生成合同,不能作废");
		 	}

			List<BiddingRecord> recordList = biddingRecordService.list(Wrappers.<BiddingRecord>query().lambda().
					eq(BiddingRecord :: getBiddingId,biddingMain.getId()).
					eq(BiddingRecord :: getDelFlag,CommonConstant.DEL_FLAG_0));
			List<String> ids = new ArrayList<>();
			Map<String, BigDecimal> map = new HashMap<>();
			for(BiddingRecord br : recordList){
				ids.add(br.getToRecordId());

				map.put(br.getToRecordId(),br.getQty());
			}
			List<PurchaseRequestDetail> detailList = iPurchaseRequestDetailService.listByIds(ids);
			for(PurchaseRequestDetail prd : detailList){
				BigDecimal qty = map.get(prd.getId());
				prd.setPurcQty(prd.getPurcQty().subtract(qty));
			}
			 iPurchaseRequestDetailService.updateBatchById(detailList);
		 }
		 //开标
		 else if("1".equals(biddingMain.getBiddingStatus())){
		 	//发送邮件通知专家
		    BiddingMain bMain = biddingMainService.getById(biddingMain.getId());
			List<SysUser> userList = biddingMainService.fetchProFessionals(biddingMain.getId());
			List<BiddingRecord> recordList = biddingRecordService.list(Wrappers.<BiddingRecord>query().lambda().
					eq(BiddingRecord :: getBiddingId,bMain.getId()).
					eq(BiddingRecord :: getDelFlag,CommonConstant.DEL_FLAG_0));

			//实施地点
			 SysDepart depart1 = iSysDepartService.getById(bMain.getOpenBiddingAddress());
			 if(depart1 == null){
				 depart1 = new SysDepart();
			 }

//			for(SysUser su : userList){
//				List<String> emails = new ArrayList<>();
//				emails.add(su.getEmail());
//				EmailSendMsgHandle emailHandle=new EmailSendMsgHandle();
//				String context = "";
//				context = "["+su.getRealname()+"]:" +
//						"<br>&nbsp;&nbsp;&nbsp;&nbsp;你好！" +
//						"<br>&nbsp;&nbsp;&nbsp;&nbsp;我司拟对如下项目进行开标,具体如下：";
//
//				for(BiddingRecord br : recordList){
//					String text = "<br>&nbsp;&nbsp;&nbsp;&nbsp;项目标的：["+br.getProdName()+"]；" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;标的数量：["+br.getQty().stripTrailingZeros().toPlainString()+"]；" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;标的交期：["+sdf.format(br.getLeadTime())+"]；" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;实施地点：["+depart1.getDepartName()+"]；" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;请你务必于开标日["+sdf.format(bMain.getBiddingStartTime()) + "至" + sdf.format(bMain.getBiddingEndTime())+"]内尽快完成评比定标的确认，谢谢！";
//					context = context + text;
//				}
//				context = context + "<br><span style='margin-left:300px'>[中环领先项目中心]</span>";
//				if(StringUtils.isNotEmpty(su.getEmail())){
//					emailHandle.sendTemplateMail("开标通知书",context,emails,null,"0");
//				}else{
//					 log.error("开标邮件发送专家评标,该专家没有设置邮箱地址");
//				 }
//			 }
		 }
		 return Result.OK("编辑成功!");
	 }

	 /**
	  * 评标管理
	  *
	  * @param biddingMain
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "招标主表-分页列表查询")
	 @ApiOperation(value="招标主表-分页列表查询", notes="招标主表-分页列表查询")
	 @GetMapping(value = "/evaluateList")
	 public Result<IPage<BiddingMain>> evaluateList(BiddingMain biddingMain,
													@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													HttpServletRequest req) {
//		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//		 String username = loginUser.getUsername();
//		 biddingMain.setCreateUser(username);

		 //判断当前用户权限
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String username = loginUser.getUsername();
		 String deptId = loginUser.getDepartIds();

		 String permission = iSysUserService.fetchPermission(username);
		 String auth = "owner";
		 String auther = username;
		 if(StringUtils.isNotEmpty(permission)){
			 if(permission.contains("bidding:all")){
				 auth = "all";
			 }else if(permission.contains("bidding:subject")){
				 auth = "subject";
				 //当前用户属于那个主体
				 String subject = loginUser.getRelTenantIds();
				 auther = subject;
			 }else if(permission.contains("bidding:dept")){
				 auth = "dept";
				 auther = deptId;
			 }
		 }

		 biddingMain.setAuth(auth);
		 biddingMain.setAuther(auther);

		 Page<BiddingMain> page = new Page<BiddingMain>(pageNo, pageSize);
		 IPage<BiddingMain> pageList = biddingMainService.evaluateList(page, biddingMain);
		 return Result.OK(pageList);
	 }

	 /**
	  * 进入评标 - 招标明细
	  *
	  * @param biddingMain
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "招标主表-分页列表查询")
	 @ApiOperation(value="招标主表-分页列表查询", notes="招标主表-分页列表查询")
	 @GetMapping(value = "/fetchRecordList")
	 public Result<List<BiddingRecord>> fetchRecordList(BiddingMain biddingMain,
													 HttpServletRequest req) {
		 List<BiddingRecord> pageList = biddingMainService.fetchRecordList(biddingMain);
		 return Result.OK(pageList);
	 }

	 /**
	  * 进入评标 - 获取招标模板
	  *
	  * @param biddingMain
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "招标主表-分页列表查询")
	 @ApiOperation(value="招标主表-分页列表查询", notes="招标主表-分页列表查询")
	 @GetMapping(value = "/fetchTemplateList")
	 public Result<List<BiddingProfessionals>> fetchTemplateList(BiddingMain biddingMain,
														HttpServletRequest req) {

		 //获取评分模板
		 List<BiddingProfessionals> templateList = biddingProfessionalsService.list(Wrappers.<BiddingProfessionals>query().lambda().
				 eq(BiddingProfessionals :: getDelFlag,CommonConstant.DEL_FLAG_0).
				 eq(BiddingProfessionals :: getBiddingId,biddingMain.getId()).
				 eq(BiddingProfessionals :: getProfessionalId,biddingMain.getProfessionalId()));

		 return Result.OK(templateList);
	 }

	 /**
	  * 招标列表
	  *
	  * @param biddingMain
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "招标主表-分页列表查询")
	 @ApiOperation(value="招标主表-分页列表查询", notes="招标主表-分页列表查询")
	 @GetMapping(value = "/fetchRecordTwoList")
	 public Result<List<BiddingRecord>> fetchRecordTwoList(BiddingMain biddingMain,
														HttpServletRequest req) {
		 List<BiddingRecord> pageList = biddingMainService.fetchRecordTwoList(biddingMain);
		 return Result.OK(pageList);
	 }

	 /**
	  * 招标供应商
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "招标邀请供应商通过主表ID查询")
	 @ApiOperation(value="招标邀请供应商主表ID查询", notes="招标邀请供应商-通主表ID查询")
	 @GetMapping(value = "/fetchBiddingSuppList")
	 public Result<List<BiddingSupplier>> fetchBiddingSuppList(@RequestParam(name="id",required=true) String id) {
		 List<BiddingSupplier> biddingSupplierList = biddingSupplierService.fetchBiddingSuppList(id);
		 return Result.OK(biddingSupplierList);
	 }

	 /**
	  * 评标管理 - 该专家的评标记录
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "招标邀请供应商通过主表ID查询")
	 @ApiOperation(value="招标邀请供应商主表ID查询", notes="招标邀请供应商-通主表ID查询")
	 @GetMapping(value = "/fetchHasBiddingSuppList")
	 public Result<List<BiddingSupplier>> fetchHasBiddingSuppList(@RequestParam(name="id",required=true) String id,
																  @RequestParam(name="ids",required=true) String ids) {
		 List<BiddingSupplier> biddingSupplierList = biddingSupplierService.fetchHasBiddingSuppList(id,ids,null);
		 return Result.OK(biddingSupplierList);
	 }

	 /**
	  * 评标专家
	  *
	  * @return
	  */
	 @GetMapping(value = "/fetchBiddingExpertList")
	 public Result<List<BiddingProfessionals>> fetchBiddingExpertList(BiddingSupplier biddingSupplier) {
		 List<BiddingProfessionals> biddingSupplierList = biddingProfessionalsService.fetchBiddingExpertList(biddingSupplier);
		 return Result.OK(biddingSupplierList);
	 }

	 /**
	  * 评标记录
	  *
	  * @return
	  */
	 @GetMapping(value = "/fetchBiddingExpertListByCount")
	 public Result<List<Map<String,Object>>> fetchBiddingExpertListByCount(BiddingSupplier biddingSupplier) {
		 List<Map<String,Object>> biddingSupplierList = biddingProfessionalsService.fetchBiddingExpertListByCount(biddingSupplier);
		 return Result.OK(biddingSupplierList);
	 }

	 /**
	  * 评标记录
	  *
	  * @return
	  */
	 @GetMapping(value = "/fetchBiddingExpertListByJsCount")
	 public Result<Map<String,Object>> fetchBiddingExpertListByJsCount(BiddingSupplier biddingSupplier) {
		 Map<String,Object> biddingSupplierList = biddingProfessionalsService.fetchBiddingExpertListByJsCount(biddingSupplier);
		 return Result.OK(biddingSupplierList);
	 }

	 /**
	  *  提交评标
	  *
	  * @param page
	  * @return
	  */
	 @AutoLog(value = "提交评标")
	 @RequestMapping(value = "/submitTemplate", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> submitTemplate(@RequestBody BiddingTemplatePage page) {
		 biddingMainService.submitTemplate(page);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  *  保存评标
	  *
	  * @param page
	  * @return
	  */
	 @AutoLog(value = "保存评标")
	 @RequestMapping(value = "/draftTemplate", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> draftTemplate(@RequestBody BiddingTemplatePage page) {
		 biddingMainService.draftTemplate(page);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  * 查询该专家评标状态
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/groupByTemplateList")
	 public Result<List<BiddingProfessionals>> groupByTemplateList(@RequestParam(name="id",required=true) String id,
																   @RequestParam(name="professionalId",required=true) String professionalId) {
		 List<BiddingProfessionals> biddingSupplierList = biddingProfessionalsService.groupByTemplateList(id,professionalId);
		 return Result.OK(biddingSupplierList);
	 }

	 /**
	  * 评标结果
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/fetchResult")
	 public Result<List<BiddingSupplierPage>> fetchResult(@RequestParam(name="id",required=true) String id) {
		 List<BiddingSupplierPage> pageList = biddingSupplierService.fetchResult(id);
		 return Result.OK(pageList);
	 }

	 /**
	  * 进入定标
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/fetchFixBiddingList")
	 public Result<List<BiddingRecord>> fetchFixBiddingList(@RequestParam(name="id",required=true) String id) {
		 List<BiddingRecord> pageList = biddingSupplierService.fetchFixBiddingList(id);
		 return Result.OK(pageList);
	 }

	 /**
	  *  定标
	  *
	  * @param page
	  * @return
	  */
	 @AutoLog(value = "定标")
	 @ApiOperation(value="招标主表-编辑", notes="招标主表-编辑")
	 @RequestMapping(value = "/fixBidding", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> fixBidding(@RequestBody BiddingSupplier page) {
		 biddingMainService.fixBidding(page);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  *  获取中标供应商信息
	  *
	  * @param page
	  * @return
	  */
	 @GetMapping(value = "/getSuppInfo")
	 public Result<BasSupplier> getSuppInfo(BiddingMain page) {
		 BasSupplier supp = biddingMainService.getSuppInfo(page);
		 return Result.OK(supp);
	 }

	 /**
	  *  专家评标记录
	  *
	  * @param param
	  * @return
	  */
	 @GetMapping(value = "/fetchBidEvaluation")
	 public Result<?> fetchBidEvaluation(PurchaseRequestMain param,
														   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		 Page<BiddingMain> page = new Page<BiddingMain>(pageNo, pageSize);
		 IPage<PurchaseRequestMain> supp = biddingProfessionalsService.fetchBidEvaluation(page,param);
		 return Result.OK(supp);
	 }

	 /**
	  *  报价信息
	  *
	  * @param param
	  * @return
	  */
	 @GetMapping(value = "/fetchQuote")
	 public Result<?> fetchQuote(BiddingMain param) {
		 List<BiddingMain> supp = biddingMainService.fetchQuote(param);
		 return Result.OK(supp);
	 }

	 /**
	  *  发送系统公告
	  *
	  * @param biddingMain
	  * @return
	  */
	 @AutoLog(value = "发送系统公告")
	 @RequestMapping(value = "/sendNotice", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> sendNotice(@RequestBody BiddingMain biddingMain) {
	 	 BiddingMain main = biddingMainService.getById(biddingMain.getId());
		 PurchaseRequestMain pmain = iPurchaseRequestMainService.getById(main.getRequestId());
		 ProjBase projBase = iProjBaseService.getById(pmain.getProjectId());
		 SysUser user = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,pmain.getApplyUserId()));
		 SysDepart depart = iSysDepartService.getById(projBase.getSubject());

		 List<BiddingRecord> recordList = biddingRecordService.list(Wrappers.<BiddingRecord>query().lambda().
				 eq(BiddingRecord :: getBiddingId,biddingMain.getId()).
				 eq(BiddingRecord :: getDelFlag,CommonConstant.DEL_FLAG_0));
		 List<BiddingRecordSupplier> suppList = iBiddingRecordSupplierService.list(Wrappers.<BiddingRecordSupplier>query().lambda()
				 .eq(BiddingRecordSupplier :: getBiddingId,biddingMain.getId())
				 .eq(BiddingRecordSupplier :: getIsRecommend,"1"));
		 Set<String> suppIds = new HashSet<>();
		 for(BiddingRecordSupplier bs : suppList) {
			 suppIds.add(bs.getSupplierId());
		 }
		 List<BasSupplier> supList = iBasSupplierService.listByIds(suppIds);
		 List<SupplierAccount> accountList = iSupplierAccountService.list(Wrappers.<SupplierAccount>query().lambda().in(SupplierAccount :: getSupplierId,suppIds));
		 Map<String,SupplierAccount> actMap = accountList.stream().collect(Collectors.toMap(SupplierAccount :: getSupplierId, sp->sp));
		 Map<String,BasSupplier> supMap = supList.stream().collect(Collectors.toMap(BasSupplier :: getId,sp->sp));
		 //实施地点
		 SysDepart depart1 = iSysDepartService.getById(main.getOpenBiddingAddress());
		 if(depart1 == null){
			 depart1 = new SysDepart();
		 }
		 for(BiddingRecord br : recordList){
			 for(BiddingRecordSupplier sp : suppList){
				 if(br.getId().equals(sp.getRecordId())){
					 BasSupplier supp = supMap.get(sp.getSupplierId());
					 String context = "["+supp.getName()+"]:" +
							 "<br>&nbsp;&nbsp;&nbsp;&nbsp;你好！" +
							 "<br>&nbsp;&nbsp;&nbsp;&nbsp;恭喜贵司中标如下项目：" +
							 "<br>&nbsp;&nbsp;&nbsp;&nbsp;项目标的：["+br.getProdName()+"]；" +
							 "<br>&nbsp;&nbsp;&nbsp;&nbsp;标的数量：["+br.getQty()+"]；" +
							 "<br>&nbsp;&nbsp;&nbsp;&nbsp;标的交期：["+sdf.format(br.getLeadTime())+"]；" +
							 "<br>&nbsp;&nbsp;&nbsp;&nbsp;实施地点：["+depart1.getDepartName()+"];"+
							 "<br>&nbsp;&nbsp;&nbsp;&nbsp;中标单号：["+main.getBiddingNo()+"];"+
							 "<br>&nbsp;&nbsp;&nbsp;&nbsp;相关合同及文件确认请联系["+user.getRealname()+"]，电话["+(user.getPhone() == null ? "" : user.getTelephone())+"];" +
							 "<br>&nbsp;&nbsp;&nbsp;&nbsp;请贵司务必于1个月内尽快完成合同文本及最终成交价格的确认，否则将视为自动弃权，谢谢！" +
							 "<br><span style='margin-left:450px'>["+depart.getDepartName()+"]</span>";

					 SupplierAccount account = actMap.get(sp.getSupplierId());
					 if(account != null){
						 biddingMainService.sendNotice(context,account.getId(),depart.getDepartName());
					 }
				 }
			 }
		 }
//		 biddingMainService.sendNotice(biddingMain);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  * 查询评标人员
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/fetchHasProfessionals")
	 public Result<List<BiddingProfessionals>> fetchHasProfessionals(@RequestParam(name="id",required=true) String id) {
		 List<BiddingProfessionals> biddingMain = biddingMainService.fetchHasProfessionals(id);
		 return Result.OK(biddingMain);

	 }

	 /**
	  *   退回评标人员
	  *
	  * @return
	  */
	 @PostMapping(value = "/toProfessionals")
	 public Result<String> toProfessionals(@RequestBody BiddingProfessionals biddingProfessionals) {
		 biddingMainService.toProfessionals(biddingProfessionals);
		 return Result.OK("添加成功！");
	 }
 }
