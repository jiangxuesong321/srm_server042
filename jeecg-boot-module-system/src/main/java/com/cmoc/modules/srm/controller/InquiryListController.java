package com.cmoc.modules.srm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.aspect.annotation.AutoLog;
import com.cmoc.common.constant.CommonConstant;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.common.system.vo.LoginUser;
import com.cmoc.common.util.oConvertUtils;
import com.cmoc.modules.srm.entity.*;
import com.cmoc.modules.srm.service.*;
import com.cmoc.modules.srm.vo.InquiryListPage;
import com.cmoc.modules.system.entity.SysDepart;
import com.cmoc.modules.system.entity.SysUser;
import com.cmoc.modules.system.service.ISysDepartService;
import com.cmoc.modules.system.service.ISysUserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

 /**
 * @Description: 询价单主表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/inquiryList")
@Slf4j
public class InquiryListController {
	@Autowired
	private IInquiryListService inquiryListService;
	@Autowired
	private IInquiryRecordService inquiryRecordService;
	@Autowired
	private IInquirySupplierService inquirySupplierService;
 	@Autowired
 	private ISysUserService iSysUserService;
 	@Autowired
 	private IPurchaseRequestMainService iPurchaseRequestMainService;
 	@Autowired
 	private IProjBaseService iProjBaseService;
 	@Autowired
 	private ISysDepartService iSysDepartService;
 	@Autowired
 	private IBasSupplierService iBasSupplierService;
 	@Autowired
 	private ISupplierAccountService iSupplierAccountService;
 	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 分页列表查询
	 *
	 * @param inquiryList
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "询价单主表-分页列表查询")
	@ApiOperation(value="询价单主表-分页列表查询", notes="询价单主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<InquiryList>> queryPageList(InquiryList inquiryList,
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
			if(permission.contains("inquiry:all")){
				auth = "all";
			}else if(permission.contains("inquiry:subject")){
				auth = "subject";
				//当前用户属于那个主体
				String subject = loginUser.getRelTenantIds();
				auther = subject;
			}else if(permission.contains("inquiry:dept")){
				auth = "dept";
				auther = deptId;
			}
		}

		inquiryList.setAuth(auth);
		inquiryList.setAuther(auther);

		Page<InquiryList> page = new Page<InquiryList>(pageNo, pageSize);
		IPage<InquiryList> pageList = inquiryListService.queryPageList(page, inquiryList);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param inquiryListPage
	 * @return
	 */
	@AutoLog(value = "询价单主表-添加")
	@ApiOperation(value="询价单主表-添加", notes="询价单主表-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody InquiryListPage inquiryListPage) {
		InquiryList inquiryList = new InquiryList();
		BeanUtils.copyProperties(inquiryListPage, inquiryList);
		inquiryListService.saveMain(inquiryList, inquiryListPage.getRecordList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param inquiryListPage
	 * @return
	 */
	@AutoLog(value = "询价单主表-编辑")
	@ApiOperation(value="询价单主表-编辑", notes="询价单主表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody InquiryListPage inquiryListPage) {
		InquiryList inquiryList = new InquiryList();
		BeanUtils.copyProperties(inquiryListPage, inquiryList);
		InquiryList inquiryListEntity = inquiryListService.getById(inquiryList.getId());
		if(inquiryListEntity==null) {
			return Result.error("未找到对应数据");
		}
		inquiryListService.updateMain(inquiryList, inquiryListPage.getRecordList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "询价单主表-通过id删除")
	@ApiOperation(value="询价单主表-通过id删除", notes="询价单主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		inquiryListService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "询价单主表-批量删除")
	@ApiOperation(value="询价单主表-批量删除", notes="询价单主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.inquiryListService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "询价单主表-通过id查询")
	@ApiOperation(value="询价单主表-通过id查询", notes="询价单主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<InquiryList> queryById(@RequestParam(name="id",required=true) String id) {
		InquiryList inquiryList = inquiryListService.getById(id);
		if(inquiryList==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(inquiryList);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "询价单明细表通过主表ID查询")
	@ApiOperation(value="询价单明细表主表ID查询", notes="询价单明细表-通主表ID查询")
	@GetMapping(value = "/queryInquiryRecordByMainId")
	public Result<List<InquiryRecord>> queryInquiryRecordListByMainId(@RequestParam(name="id",required=true) String id) {
		List<InquiryRecord> inquiryRecordList = inquiryRecordService.selectByMainId(id);
		return Result.OK(inquiryRecordList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "询价供应商表通过主表ID查询")
	@ApiOperation(value="询价供应商表主表ID查询", notes="询价供应商表-通主表ID查询")
	@GetMapping(value = "/queryInquirySupplierByMainId")
	public Result<List<InquirySupplier>> queryInquirySupplierListByMainId(@RequestParam(name="id",required=true) String id) {
		List<InquirySupplier> inquirySupplierList = inquirySupplierService.selectByMainId(id);
		return Result.OK(inquirySupplierList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param inquiryList
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, InquiryList inquiryList) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<InquiryList> queryWrapper = QueryGenerator.initQueryWrapper(inquiryList, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<InquiryList> queryList = inquiryListService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<InquiryList> inquiryListList = new ArrayList<InquiryList>();
      if(oConvertUtils.isEmpty(selections)) {
          inquiryListList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          inquiryListList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<InquiryListPage> pageList = new ArrayList<InquiryListPage>();
      for (InquiryList main : inquiryListList) {
          InquiryListPage vo = new InquiryListPage();
          BeanUtils.copyProperties(main, vo);
          List<InquiryRecord> inquiryRecordList = inquiryRecordService.selectByMainId(main.getId());
          vo.setRecordList(inquiryRecordList);
//          List<InquirySupplier> inquirySupplierList = inquirySupplierService.selectByMainId(main.getId());
//          vo.setInquirySupplierList(inquirySupplierList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "询价单主表列表");
      mv.addObject(NormalExcelConstants.CLASS, InquiryListPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("询价单主表数据", "导出人:"+sysUser.getRealname(), "询价单主表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
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
              List<InquiryListPage> list = ExcelImportUtil.importExcel(file.getInputStream(), InquiryListPage.class, params);
              for (InquiryListPage page : list) {
                  InquiryList po = new InquiryList();
                  BeanUtils.copyProperties(page, po);
                  inquiryListService.saveMain(po, page.getRecordList());
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
	  * 查看询价
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "询价单明细表通过主表ID查询")
	 @GetMapping(value = "/queryRecordList")
	 public Result<List<InquiryRecord>> queryRecordList(@RequestParam(name="id",required=true) String id) {
		 List<InquiryRecord> inquiryRecordList = inquiryRecordService.queryRecordList(id);
		 return Result.OK(inquiryRecordList);
	 }

	 /**
	  *  编辑
	  *
	  * @param inquiryList
	  * @return
	  */
	 @AutoLog(value = "询价单主表-编辑")
	 @ApiOperation(value="询价单主表-编辑", notes="询价单主表-编辑")
	 @RequestMapping(value = "/editEntity", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> editEntity(@RequestBody InquiryList inquiryList) throws Exception {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String username = loginUser.getUsername();
		 Date nowTime = new Date();

		 inquiryList.setUpdateTime(nowTime);
		 inquiryList.setUpdateUser(username);
		 inquiryListService.editEntity(inquiryList);

		 return Result.OK("编辑成功!");
	 }

	 /**
	  *  再次发布需求
	  *
	  * @param inquiryList
	  * @return
	  */
	 @AutoLog(value = "再次发布需求")
	 @ApiOperation(value="询价单主表-编辑", notes="询价单主表-编辑")
	 @RequestMapping(value = "/handlePush", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> handlePush(@RequestBody InquiryList inquiryList) {
		 inquiryListService.handlePush(inquiryList);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  *  提交
	  *
	  * @param inquiryList
	  * @return
	  */
	 @AutoLog(value = "提交询价")
	 @ApiOperation(value="询价单主表-编辑", notes="询价单主表-编辑")
	 @RequestMapping(value = "/toRecommend", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> toRecommend(@RequestBody InquiryListPage inquiryList) {
		 inquiryListService.toRecommend(inquiryList);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  * 合同生成列表
	  *
	  * @param inquiryList
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "询价单主表-分页列表查询")
	 @ApiOperation(value="询价单主表-分页列表查询", notes="询价单主表-分页列表查询")
	 @GetMapping(value = "/queryPageToDetailList")
	 public Result<IPage<InquiryList>> queryPageToDetailList(InquiryList inquiryList,
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

		 inquiryList.setAuth(auth);
		 inquiryList.setAuther(auther);

		 Page<InquiryList> page = new Page<InquiryList>(pageNo, pageSize);
		 IPage<InquiryList> pageList = inquiryListService.queryPageToDetailList(page, inquiryList);
		 return Result.OK(pageList);
	 }

	 /**
	  * 生成合同查询询价明细
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/getRecordById")
	 public Result<?> getRecordById(@RequestParam(name="id",required=true) String id) {
		 List<InquiryRecord> inquiryRecordList = inquiryRecordService.getRecordById(id);
		 return Result.OK(inquiryRecordList);
	 }


	 /**
	  * 生成合同查询询价供应商
	  *
	  * @param id
	  * @return
	  */
	 //@AutoLog(value = "询价单明细表通过主表ID查询")
	 @GetMapping(value = "/getSuppInfo")
	 public Result<?> getSuppInfo(@RequestParam(name="id",required=true) String id) {
		 InquirySupplier supp = inquirySupplierService.getSuppInfo(id);
		 return Result.OK(supp);
	 }

	 /**
	  *  结束报价
	  *
	  * @param inquiryList
	  * @return
	  */
	 @AutoLog(value = "询价单主表-编辑")
	 @ApiOperation(value="询价单主表-编辑", notes="询价单主表-编辑")
	 @RequestMapping(value = "/toRecommand", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> toRecommand(@RequestBody InquiryListPage inquiryList) {

		 inquiryListService.toRecommand(inquiryList);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  *  发送系统公告
	  *
	  * @param param
	  * @return
	  */
	 @RequestMapping(value = "/sendNotice", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> sendNotice(@RequestBody InquiryList param) {
		 InquiryList inquiryList = inquiryListService.getById(param.getId());
		 List<InquirySupplier> suppList = inquirySupplierService.list(Wrappers.<InquirySupplier>query().lambda().
				 eq(InquirySupplier :: getInquiryId,param.getId()).
				 eq(InquirySupplier :: getIsRecommend,"1").
				 eq(InquirySupplier :: getDelFlag,CommonConstant.DEL_FLAG_0));
		 Set<String> suppIds = new HashSet<>();
		 for(InquirySupplier is : suppList){
			 suppIds.add(is.getSupplierId());
		 }
		 List<InquiryRecord> recordList = inquiryRecordService.list(Wrappers.<InquiryRecord>query().lambda().
				 eq(InquiryRecord :: getInquiryId,param.getId()).
				 eq(InquiryRecord :: getDelFlag,CommonConstant.DEL_FLAG_0));

		 PurchaseRequestMain main = iPurchaseRequestMainService.getById(inquiryList.getRequestId());
		 ProjBase projBase = iProjBaseService.getById(main.getProjectId());
		 SysUser user = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,main.getApplyUserId()));
		 SysDepart depart = iSysDepartService.getById(projBase.getSubject());
		 List<BasSupplier> supList = iBasSupplierService.listByIds(suppIds);
		 List<SupplierAccount> accountList = iSupplierAccountService.list(Wrappers.<SupplierAccount>query().lambda().in(SupplierAccount :: getSupplierId,suppIds));
		 Map<String,BasSupplier> supMap = supList.stream().collect(Collectors.toMap(BasSupplier::getId, sp->sp));
		 Map<String,SupplierAccount> actMap = accountList.stream().collect(Collectors.toMap(SupplierAccount::getSupplierId, sp->sp));
		 for(InquiryRecord ir : recordList){
			 for(InquirySupplier isp : suppList){
				 if(ir.getId().equals(isp.getRecordId())){
					 BasSupplier supp = supMap.get(isp.getSupplierId());
						 String context = "["+supp.getName()+"]:" +
								 "<br>&nbsp;&nbsp;&nbsp;&nbsp;你好！" +
								 "<br>&nbsp;&nbsp;&nbsp;&nbsp;恭喜贵司中标如下项目：" +
								 "<br>&nbsp;&nbsp;&nbsp;&nbsp;项目标的：["+ir.getProdName()+"]；" +
								 "<br>&nbsp;&nbsp;&nbsp;&nbsp;标的数量：["+ir.getQty()+"]；" +
								 "<br>&nbsp;&nbsp;&nbsp;&nbsp;标的交期：["+sdf.format(ir.getLeadTime())+"]；" +
								 "<br>&nbsp;&nbsp;&nbsp;&nbsp;相关合同及文件确认请联系["+user.getRealname()+"]，电话["+user.getPhone()+"];" +
								 "<br>&nbsp;&nbsp;&nbsp;&nbsp;请贵司务必于1个月内尽快完成合同文本及最终成交价格的确认，否则将视为自动弃权，谢谢！" +
								 "<br><span style='margin-left:450px'>["+depart.getDepartName()+"]</span>";

						 //发布公告
						 String accountId = actMap.get(isp.getSupplierId()).getId();
					 	inquiryListService.sendNotice(context,accountId,depart.getDepartName());
				 }
			 }
		 }
		 return Result.OK("编辑成功!");
	 }
}
