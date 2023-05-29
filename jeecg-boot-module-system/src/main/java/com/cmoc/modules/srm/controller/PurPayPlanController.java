package com.cmoc.modules.srm.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.aspect.annotation.AutoLog;
import com.cmoc.common.constant.CommonConstant;
import com.cmoc.common.system.vo.LoginUser;
import com.cmoc.modules.srm.entity.*;
import com.cmoc.modules.srm.service.IApproveRecordService;
import com.cmoc.modules.srm.service.IPurPayPlanDetailService;
import com.cmoc.modules.srm.service.IPurPayPlanService;
import com.cmoc.modules.srm.vo.PurPayPlanPage;
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
import java.util.*;

 /**
 * @Description: 付款计划
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/purPayPlan")
@Slf4j
public class PurPayPlanController {
	@Autowired
	private IPurPayPlanService purPayPlanService;
	@Autowired
	private IPurPayPlanDetailService purPayPlanDetailService;
	@Autowired
	private IApproveRecordService iApproveRecordService;
	@Autowired
	private ISysUserService iSysUserService;
	
	/**
	 * 分页列表查询
	 *
	 * @param purPayPlan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "付款计划-分页列表查询")
	@ApiOperation(value="付款计划-分页列表查询", notes="付款计划-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PurPayPlan>> queryPageList(PurPayPlan purPayPlan,
                                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
		Page<PurPayPlan> page = new Page<PurPayPlan>(pageNo, pageSize);

		//判断当前用户权限
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		String deptId = loginUser.getDepartIds();
		if(!"report".equals(purPayPlan.getSource())){
			String permission = iSysUserService.fetchPermission(username);
			String auth = "owner";
			String auther = username;
			if(StringUtils.isNotEmpty(permission)){
				if(permission.contains("pay:all")){
					auth = "all";
				}else if(permission.contains("pay:subject")){
					auth = "subject";
					//当前用户属于那个主体
					String subject = loginUser.getRelTenantIds();
					auther = subject;
				}else if(permission.contains("pay:dept")){
					auth = "dept";
					auther = deptId;
				}
			}

			purPayPlan.setAuth(auth);
			purPayPlan.setAuther(auther);
		}


		IPage<PurPayPlan> pageList = purPayPlanService.queryPageList(page, purPayPlan);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param purPayPlan
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "付款计划-分页列表查询")
	 @ApiOperation(value="付款计划-分页列表查询", notes="付款计划-分页列表查询")
	 @GetMapping(value = "/getTotalAmountByCurrency")
	 public Result<?> getTotalAmountByCurrency(PurPayPlan purPayPlan,
													HttpServletRequest req) {
		 //判断当前用户权限
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String username = loginUser.getUsername();
		 String deptId = loginUser.getDepartIds();

		 String permission = iSysUserService.fetchPermission(username);
		 String auth = "owner";
		 String auther = username;
		 if(StringUtils.isNotEmpty(permission)){
			 if(permission.contains("pay:all")){
				 auth = "all";
			 }else if(permission.contains("pay:subject")){
				 auth = "subject";
				 //当前用户属于那个主体
				 String subject = loginUser.getRelTenantIds();
				 auther = subject;
			 }else if(permission.contains("pay:dept")){
				 auth = "dept";
				 auther = deptId;
			 }
		 }

		 purPayPlan.setAuth(auth);
		 purPayPlan.setAuther(auther);

		 PurPayPlan entity = purPayPlanService.getTotalAmountByCurrency(purPayPlan);
		 List<PurPayPlan> pageList = new ArrayList<>();
		 if(entity != null){
			 pageList.add(entity);
		 }
		 return Result.OK(pageList);
	 }
	
	/**
	 *   添加
	 *
	 * @param purPayPlanPage
	 * @return
	 */
	@AutoLog(value = "付款计划-添加")
	@ApiOperation(value="付款计划-添加", notes="付款计划-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PurPayPlanPage purPayPlanPage) throws Exception {
		PurPayPlan purPayPlan = new PurPayPlan();
		BeanUtils.copyProperties(purPayPlanPage, purPayPlan);
		purPayPlanService.saveMain(purPayPlan, purPayPlanPage.getPurPayPlanDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param purPayPlanPage
	 * @return
	 */
	@AutoLog(value = "付款计划-编辑")
	@ApiOperation(value="付款计划-编辑", notes="付款计划-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PurPayPlanPage purPayPlanPage) {
		PurPayPlan purPayPlan = new PurPayPlan();
		BeanUtils.copyProperties(purPayPlanPage, purPayPlan);
		PurPayPlan purPayPlanEntity = purPayPlanService.getById(purPayPlan.getId());
		if(purPayPlanEntity==null) {
			return Result.error("未找到对应数据");
		}
		purPayPlanService.updateMain(purPayPlan, purPayPlanPage.getPurPayPlanDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "付款计划-通过id删除")
	@ApiOperation(value="付款计划-通过id删除", notes="付款计划-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		UpdateWrapper<PurPayPlan> updateWrapper = new UpdateWrapper<>();
		updateWrapper.set("del_flag",CommonConstant.ACT_SYNC_1);
		updateWrapper.eq("id",id);
		purPayPlanService.update(updateWrapper);

		UpdateWrapper<PurPayPlanDetail> child = new UpdateWrapper<>();
		child.set("del_flag",CommonConstant.ACT_SYNC_1);
		child.eq("pay_plan_id",id);
		purPayPlanDetailService.update(child);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "付款计划-批量删除")
	@ApiOperation(value="付款计划-批量删除", notes="付款计划-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.purPayPlanService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "付款计划-通过id查询")
	@ApiOperation(value="付款计划-通过id查询", notes="付款计划-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PurPayPlan> queryById(@RequestParam(name="id",required=true) String id) {
		PurPayPlan purPayPlan = purPayPlanService.getById(id);
		if(purPayPlan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(purPayPlan);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "付款计划明细通过主表ID查询")
	@ApiOperation(value="付款计划明细主表ID查询", notes="付款计划明细-通主表ID查询")
	@GetMapping(value = "/queryPurPayPlanDetailByMainId")
	public Result<List<PurPayApply>> queryPurPayPlanDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PurPayApply> purPayPlanDetailList = purPayPlanDetailService.selectByMainId(id);
		return Result.OK(purPayPlanDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param purPayPlan
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PurPayPlan purPayPlan) {

		//判断当前用户权限
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		String deptId = loginUser.getDepartIds();
		if(!"report".equals(purPayPlan.getSource())){
			String permission = iSysUserService.fetchPermission(username);
			String auth = "owner";
			String auther = username;
			if(StringUtils.isNotEmpty(permission)){
				if(permission.contains("pay:all")){
					auth = "all";
				}else if(permission.contains("pay:subject")){
					auth = "subject";
					//当前用户属于那个主体
					String subject = loginUser.getRelTenantIds();
					auther = subject;
				}else if(permission.contains("pay:dept")){
					auth = "dept";
					auther = deptId;
				}
			}

			purPayPlan.setAuth(auth);
			purPayPlan.setAuther(auther);
		}


		List<PurPayPlan> pageList = purPayPlanService.queryList(purPayPlan);

		  // Step.4 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "付款计划");
		mv.addObject(NormalExcelConstants.CLASS, PurPayPlan.class);
		mv.addObject(NormalExcelConstants.EXPORT_FIELDS,request.getParameter("field"));
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("付款计划", "导出人:"+loginUser.getRealname(), "付款计划"));
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
              List<PurPayPlanPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PurPayPlanPage.class, params);
              for (PurPayPlanPage page : list) {
                  PurPayPlan po = new PurPayPlan();
                  BeanUtils.copyProperties(page, po);
                  purPayPlanService.saveMain(po, page.getPurPayPlanDetailList());
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
	  * 以项目为单位统计已付款金额
	  *
	  * @param purPayPlan
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/fetchPayAmountByProjId")
	 public Result<?> fetchPayAmountByProjId(PurPayPlan purPayPlan,HttpServletRequest req) {
		 PurPayPlan entity = purPayPlanService.fetchPayAmountByProjId(purPayPlan);
		 return Result.OK(entity);
	 }

	 /**
	  * 以项目为单位统计已付款金额
	  *
	  * @param purPayPlan
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/fetchProjTypeAmountByProjId")
	 public Result<?> fetchProjTypeAmountByProjId(PurPayPlan purPayPlan,HttpServletRequest req) {
		 List<Map<String,Object>> entity = purPayPlanService.fetchProjTypeAmountByProjId(purPayPlan);
		 return Result.OK(entity);
	 }

	 /**
	  *  OA审批
	  *
	  * @return
	  */
	 @RequestMapping(value = "/toApprove", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> toApprove(@RequestBody PurPayPlan purPayPlan) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String username = loginUser.getUsername();
		 Date nowTime = new Date();
		 purPayPlan.setProcessNode(purPayPlan.getName());
		 purPayPlan.setProcessStatus(purPayPlan.getPayStatus());
		 if("2".equals(purPayPlan.getPayStatus())){
			 purPayPlan.setPayTime(new Date());
		 }
		 purPayPlanService.updateById(purPayPlan);

		 ApproveRecord approve = new ApproveRecord();
		 approve.setApprover(purPayPlan.getApprover());
		 approve.setApproveComment(null);
		 approve.setCreateTime(nowTime);
		 approve.setUpdateTime(nowTime);
		 approve.setUpdateUser(username);
		 approve.setCreateUser(username);
		 approve.setBusinessId(purPayPlan.getId());
		 approve.setDelFlag(CommonConstant.NO_READ_FLAG);
		 approve.setType("OA_PLAN");
		 approve.setName(purPayPlan.getName());
		 approve.setStatus(purPayPlan.getPayStatus());
		 approve.setCode(purPayPlan.getProcessCode());
		 iApproveRecordService.save(approve);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  * 首页 - 资金计划
	  *
	  * @param purPayPlan
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/fetchPlanAmount")
	 public Result<List<Map<String,Object>>> fetchPlanAmount(PurPayPlan purPayPlan,
													HttpServletRequest req) {
		 //判断当前用户权限
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String subject = loginUser.getRelTenantIds();
		 purPayPlan.setSubject(subject);
		 List<Map<String, Object>> pageList = purPayPlanService.fetchPlanAmount(purPayPlan);
		 return Result.OK(pageList);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param purPayPlan
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "付款计划-分页列表查询")
	 @ApiOperation(value="付款计划-分页列表查询", notes="付款计划-分页列表查询")
	 @GetMapping(value = "/fetchPageList")
	 public Result<IPage<PurPayPlan>> fetchPageList(PurPayPlan purPayPlan,
													@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													HttpServletRequest req) {
		 Page<PurPayPlan> page = new Page<PurPayPlan>(pageNo, pageSize);
		 IPage<PurPayPlan> pageList = purPayPlanService.fetchPageList(page, purPayPlan);
		 return Result.OK(pageList);
	 }

	 /**
	  *  编辑
	  *
	  * @param purPayPlan
	  * @return
	  */
	 @AutoLog(value = "付款计划-编辑")
	 @ApiOperation(value="付款计划-编辑", notes="付款计划-编辑")
	 @RequestMapping(value = "/toOa", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> toOa(@RequestBody PurPayPlan purPayPlan) throws Exception {
		 purPayPlanService.toOa(purPayPlan);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  *  OA附件回传
	  *
	  * @return
	  */
	 @AutoLog(value = "OA附件回传")
	 @RequestMapping(value = "/uploadOaAttachment", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> uploadOaAttachment(@RequestBody PurPayPlan purPayPlan) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String username = loginUser.getUsername();
		 Date nowTime = new Date();
		 purPayPlan.setUpdateUser(username);
		 purPayPlan.setUpdateTime(nowTime);
		 purPayPlanService.updateById(purPayPlan);

		 return Result.OK("编辑成功!");
	 }


	 /**
	  * 每个月付款金额
	  *
	  * @param contractBase
	  * @return
	  */
	 @ApiOperation(value="每个月付款金额", notes="每个月付款金额")
	 @GetMapping(value = "/fetchPayAmountByMonth")
	 public Result<Map<String,Object>> fetchPayAmountByMonth(ContractBase contractBase,
                                                             HttpServletRequest req) {
		 Map<String,Object> obj = purPayPlanService.fetchPayAmountByMonth(contractBase);
		 return Result.OK(obj);
	 }
}
