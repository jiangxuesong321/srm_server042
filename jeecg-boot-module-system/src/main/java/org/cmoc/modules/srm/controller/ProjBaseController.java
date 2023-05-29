package org.cmoc.modules.srm.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.cmoc.common.api.vo.Result;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.common.system.query.QueryGenerator;
import org.cmoc.common.system.vo.LoginUser;
import org.cmoc.modules.srm.entity.*;
import org.cmoc.modules.srm.service.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.cmoc.modules.srm.utils.JeecgEntityExcel;
import org.cmoc.modules.srm.vo.BasMaterialImport;
import org.cmoc.modules.srm.vo.FileParam;
import org.cmoc.modules.system.entity.SysUser;
import org.cmoc.modules.system.service.ISysUserService;
import org.cmoc.common.system.base.controller.JeecgController;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.ApiOperation;
import org.cmoc.common.aspect.annotation.AutoLog;

 /**
 * @Description: proj_base
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/projBase")
@Slf4j
public class ProjBaseController extends JeecgController<ProjBase, IProjBaseService> {
	@Autowired
	private IProjBaseService projBaseService;
	@Autowired
	private IProjectBomRelationService iProjectBomRelationService;
	@Autowired
	private ISysUserService iSysUserService;
	@Autowired
	private IPurchaseRequestMainService iPurchaseRequestMainService;
	@Autowired
	private IProjectBomChildService iProjectBomChildService;
	@Autowired
	private IProjectCategoryService iProjectCategoryService;
	@Autowired
	private IProjectExchangeRateService iProjectExchangeRateService;

	 @Autowired
	 private IProjectCategoryPayService iProjectCategoryPayService;
	
	/**
	 * 分页列表查询
	 *
	 * @param projBase
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "proj_base-分页列表查询")
	@ApiOperation(value="proj_base-分页列表查询", notes="proj_base-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ProjBase>> queryPageList(ProjBase projBase,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		//判断当前用户权限
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		String deptId = loginUser.getDepartIds();
		if("pageList".equals(projBase.getSource())){
			String permission = iSysUserService.fetchPermission(username);
			String auth = "owner";
			String auther = username;
			if(StringUtils.isNotEmpty(permission)){
				if(permission.contains("proj:all")){
					auth = "all";
				}else if(permission.contains("proj:subject")){
					auth = "subject";
					//当前用户属于那个主体
					String subject = loginUser.getRelTenantIds();
					auther = subject;
				}else if(permission.contains("proj:dept")){
					auth = "dept";
					auther = deptId;
				}
			}

			projBase.setAuth(auth);
			projBase.setAuther(auther);
			projBase.setCreateUser(username);
		}

		Page<ProjBase> page = new Page<ProjBase>(pageNo, pageSize);
		IPage<ProjBase> pageList = projBaseService.pageList(page, projBase);

		return Result.OK(pageList);
	}

	 /**
	  * 根据主体获取项目
	  *
	  * @param projBase
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "proj_base-分页列表查询")
	 @ApiOperation(value="proj_base-分页列表查询", notes="proj_base-分页列表查询")
	 @GetMapping(value = "/fetchProjList")
	 public Result<List<ProjBase>> fetchProjList(ProjBase projBase,
												  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												  HttpServletRequest req) {
		 //判断当前用户权限
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String subject = loginUser.getRelTenantIds();
		 List<ProjBase> pageList = new ArrayList<>();
		 if(StringUtils.isNotEmpty(subject)){
		 	List<String> subjectIds = Arrays.asList(subject.split(","));
		 	pageList = projBaseService.list(Wrappers.<ProjBase>query().lambda().
					eq(ProjBase :: getDelFlag,CommonConstant.DEL_FLAG_0).
					in(ProjBase :: getSubject,subjectIds));
		 }

		 return Result.OK(pageList);
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param projBase
	  * @return
	  */
	 //@AutoLog(value = "proj_base-分页列表查询")
	 @ApiOperation(value="proj_base-分页列表查询", notes="proj_base-分页列表查询")
	 @GetMapping(value = "/queryNoPageList")
	 public List<ProjBase> queryNoPageList(ProjBase projBase,
												  HttpServletRequest req) {
		 QueryWrapper<ProjBase> queryWrapper = QueryGenerator.initQueryWrapper(projBase, req.getParameterMap());
		 List<ProjBase> pageList = projBaseService.list(queryWrapper);
		 return pageList;
	 }
	
	/**
	 *   添加
	 *
	 * @param projBase
	 * @return
	 */
	@AutoLog(value = "proj_base-添加")
	@ApiOperation(value="proj_base-添加", notes="proj_base-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ProjBase projBase) throws Exception {
		projBaseService.addProjBase(projBase);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param projBase
	 * @return
	 */
	@AutoLog(value = "proj_base-编辑")
	@ApiOperation(value="proj_base-编辑", notes="proj_base-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ProjBase projBase) throws Exception {
		projBaseService.editProjBase(projBase);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "proj_base-通过id删除")
	@ApiOperation(value="proj_base-通过id删除", notes="proj_base-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		projBaseService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "proj_base-批量删除")
	@ApiOperation(value="proj_base-批量删除", notes="proj_base-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.projBaseService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "proj_base-通过id查询")
	@ApiOperation(value="proj_base-通过id查询", notes="proj_base-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ProjBase> queryById(@RequestParam(name="id",required=true) String id) {
		ProjBase projBase = projBaseService.getById(id);
		if(projBase==null) {
			return Result.error("未找到对应数据");
		}
		//立项人
		SysUser user = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().
				eq(SysUser :: getUsername,projBase.getApplyUserId()));
		if(user != null){
			projBase.setApplyUserName(user.getRealname());
		}

		//使用金额
		PurchaseRequestMain param = new PurchaseRequestMain();
		param.setProjectId(id);
		PurchaseRequestMain main = iPurchaseRequestMainService.fetchRequestByProjId(param);
		projBase.setRemainAmount(BigDecimal.ZERO);
		projBase.setUsedAmount(BigDecimal.ZERO);
		if(main != null){
			projBase.setUsedAmount(main.getOrderTotalAmountTax());
			projBase.setRemainAmount(projBase.getBudgetAmount().subtract(projBase.getUsedAmount()));
		}
		return Result.OK(projBase);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param projBase
    */
//    @RequestMapping(value = "/exportXls")
//    public ModelAndView exportXls(HttpServletRequest request, ProjBase projBase) {
//        return super.exportXls(request, projBase, ProjBase.class, "proj_base");
//    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return projBaseService.importExcel(request, response, BasMaterialImport.class);
    }

	 /**
	  * 设备清单
	  *
	  * @param projBase
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @ApiOperation(value="proj_base-分页列表查询", notes="proj_base-分页列表查询")
	 @GetMapping(value = "/fetchEqpPageList")
	 public Result<IPage<ProjectBomRelation>> fetchEqpPageList(ProjectBomRelation projBase,
												  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												  HttpServletRequest req) {
		 Page<ProjectBomRelation> page = new Page<ProjectBomRelation>(pageNo, pageSize);
		 IPage<ProjectBomRelation> pageList = iProjectBomRelationService.fetchEqpPageList(page, projBase);
		 return Result.OK(pageList);
	 }

	 /**
	  * 设备清单
	  *
	  * @param projBase
	  * @param req
	  * @return
	  */
	 @ApiOperation(value="proj_base-分页列表查询", notes="proj_base-分页列表查询")
	 @GetMapping(value = "/fetchTotalQty")
	 public Result<Map<String,String>> fetchTotalQty(ProjectBomRelation projBase,
															   HttpServletRequest req) {
		 Map<String,String> pageList = iProjectBomRelationService.fetchTotalQty(projBase);
		 return Result.OK(pageList);
	 }
	 /**
	  * 设备清单预算汇总
	  *
	  * @param projBase
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/fetchTotalEqp")
	 public Result<?> fetchTotalEqp(ProjectBomRelation projBase,
															   HttpServletRequest req) {
		 ProjectBomRelation pageList = iProjectBomRelationService.fetchTotalEqp(projBase);
		 return Result.OK(pageList);
	 }

	 /**
	  * 项目下的设备
	  *
	  * @param projBase
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @ApiOperation(value="proj_base-分页列表查询", notes="proj_base-分页列表查询")
	 @GetMapping(value = "/fetchEqpPageListByProjId")
	 public Result<IPage<ProjectBomRelation>> fetchEqpPageListByProjId(ProjectBomRelation projBase,
															   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															   HttpServletRequest req) {
		 Page<ProjectBomRelation> page = new Page<ProjectBomRelation>(pageNo, pageSize);
		 IPage<ProjectBomRelation> pageList = iProjectBomRelationService.fetchEqpPageListByProjId(page, projBase);
		 return Result.OK(pageList);
	 }

	 /**
	  * 通过id查询
	  *
	  * @param projBase
	  * @return
	  */
	 //@AutoLog(value = "proj_base-通过id查询")
	 @ApiOperation(value="proj_base-通过id查询", notes="proj_base-通过id查询")
	 @GetMapping(value = "/fetchProjById")
	 public Result<ProjBase> fetchProjById(ProjBase projBase) {
		 ProjBase entity = projBaseService.fetchProjById(projBase);
		 return Result.OK(entity);
	 }

	 /**
	  * 配套Tab页查询
	  *
	  * @param pmain
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "proj_base-分页列表查询")
	 @ApiOperation(value="proj_base-分页列表查询", notes="proj_base-分页列表查询")
	 @GetMapping(value = "/fetchChildList")
	 public Result<IPage<PurchaseRequestMain>> fetchChildList(PurchaseRequestMain pmain,
												   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												   HttpServletRequest req) {
		 Page<PurchaseRequestMain> page = new Page<PurchaseRequestMain>(pageNo, pageSize);
		 IPage<PurchaseRequestMain> pageList = iPurchaseRequestMainService.fetchChildList(page, pmain);
		 return Result.OK(pageList);
	 }

	 /**
	  * 项目子项
	  *
	  * @param child
	  * @return
	  */
	 @GetMapping(value = "/fetchBomChild")
	 public Result<?> fetchBomChild(ProjectBomChild child,
												HttpServletRequest req) {
		 List<ProjectBomChild> pageList = iProjectBomChildService.list(Wrappers.<ProjectBomChild>query().lambda().
				 eq(ProjectBomChild :: getDelFlag,CommonConstant.DEL_FLAG_0).
				 eq(ProjectBomChild :: getProjectId,child.getProjectId()));
		 return Result.OK(pageList);
	 }

	 /**
	  * 分类
	  *
	  * @param category
	  * @return
	  */
	 @GetMapping(value = "/fetchCategory")
	 public Result<?> fetchCategory(ProjectCategory category,
									HttpServletRequest req) {
		 List<ProjectCategory> pageList = iProjectCategoryService.fetchCategory(category);
		 return Result.OK(pageList);
	 }

	 /**
	  * 分类
	  *
	  * @param category
	  * @return
	  */
	 @GetMapping(value = "/fetchCategoryToAmount")
	 public Result<?> fetchCategoryToAmount(ProjectCategory category,
									HttpServletRequest req) {
		 List<ProjectCategory> pageList = iProjectCategoryService.fetchCategoryToAmount(category);
		 return Result.OK(pageList);
	 }

	 /**
	  * 末级分类可选择
	  *
	  * @param category
	  * @return
	  */
	 @GetMapping(value = "/fetchLastCategory")
	 public Result<?> fetchLastCategory(ProjectCategory category,
									HttpServletRequest req) {
		 List<ProjectCategory> pageList = iProjectCategoryService.fetchLastCategory(category);
		 return Result.OK(pageList);
	 }

	 /**
	  * 项目汇率
	  *
	  * @param rate
	  * @return
	  */
	 @GetMapping(value = "/fetchExchangeRate")
	 public Result<?> fetchExchangeRate(ProjectExchangeRate rate,
										HttpServletRequest req) {
		 List<ProjectExchangeRate> pageList = iProjectExchangeRateService.list(Wrappers.<ProjectExchangeRate>query().lambda().
				 eq(ProjectExchangeRate :: getProjectId,rate.getProjectId()));
		 return Result.OK(pageList);
	 }

	 /**
	  * 项目总产能
	  *
	  * @param projBase
	  * @return
	  */
	 @GetMapping(value = "/fetchCapacityByProjId")
	 public Result<?> fetchCapacityByProjId(ProjBase projBase,
										HttpServletRequest req) {
		 ProjBase pageList = projBaseService.fetchCapacityByProjId(projBase);
		 return Result.OK(pageList);
	 }

	 /**
	  * 资金类型饼图(已付、合同金额)
	  *
	  * @param projBase
	  * @return
	  */
	 @GetMapping(value = "/fetchCategoryAmount")
	 public Result<?> fetchCategoryAmount(ProjBase projBase,
											HttpServletRequest req) {
		 Map<String,List<Map<String,String>>> pageList = projBaseService.fetchCategoryAmount(projBase);
		 return Result.OK(pageList);
	 }

	 /**
	  * 资金类型饼图(已付、合同金额)
	  *
	  * @param bomChild
	  * @return
	  */
	 @GetMapping(value = "/fetchChildProgress")
	 public Result<?> fetchChildProgress(ProjectBomChild bomChild,
										  HttpServletRequest req) {
		 List<Map<String, Object>> pageList = iProjectBomChildService.fetchChildProgress(bomChild);
		 return Result.OK(pageList);
	 }
	 /**
	  * 资金类型饼图(已付、合同金额)
	  *
	  * @param bomChild
	  * @return
	  */
	 @GetMapping(value = "/fetchChildProgress1")
	 public Result<?> fetchChildProgress1(ProjectBomChild bomChild,
										 HttpServletRequest req) {
		 List<Map<String, Object>> pageList = iProjectBomChildService.fetchChildProgress1(bomChild);
		 return Result.OK(pageList);
	 }

	 /**
	  * 项目子类资金统计
	  *
	  * @param bomChild
	  * @return
	  */
	 @GetMapping(value = "/fetchChildAmount")
	 public Result<?> fetchChildAmount(ProjectBomChild bomChild,
										 HttpServletRequest req) {
		 List<ProjectBomChild> pageList = iProjectBomChildService.fetchChildAmount(bomChild);
		 return Result.OK(pageList);
	 }

	 /**
	  *  编辑
	  *
	  * @param projBase
	  * @return
	  */
	 @AutoLog(value = "编辑项目")
	 @RequestMapping(value = "/updateRate", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> updateRate(@RequestBody List<ProjBase> projBase) {
		 projBaseService.updateRate(projBase);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  * 项目总投
	  *
	  * @param projBase
	  * @return
	  */
	 @GetMapping(value = "/fetchProjectAmount")
	 public Result<?> fetchProjectAmount(ProjBase projBase,
											HttpServletRequest req) {
		 ProjBase pageList = projBaseService.fetchProjectAmount(projBase);
		 return Result.OK(pageList);
	 }

	 /**
	  * 项目总投
	  *
	  * @param projBase
	  * @return
	  */
	 @GetMapping(value = "/fetchProjectAmountByType")
	 public Result<?> fetchProjectAmountByType(ProjBase projBase,
										 HttpServletRequest req) {
		 List<Map<String,Object>> pageList = projBaseService.fetchProjectAmountByType(projBase);
		 return Result.OK(pageList);
	 }

	 /**
	  * 首页 - 金额支出情况
	  *
	  * @param projBase
	  * @return
	  */
	 @GetMapping(value = "/fetchAmount")
	 public Map<String,BigDecimal> fetchAmount(ProjBase projBase,HttpServletRequest req) {
		 //判断当前用户权限
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String subject = loginUser.getRelTenantIds();
		 projBase.setSubject(subject);
		 Map<String,BigDecimal> pageList = projBaseService.fetchAmount(projBase);
		 return pageList;
	 }

	 /**
	  * 首页 - 设备采购情况
	  *
	  * @param projBase
	  * @return
	  */
	 @GetMapping(value = "/fetchQty")
	 public Map<String,BigDecimal> fetchQty(ProjBase projBase,HttpServletRequest req) {
		 //判断当前用户权限
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String subject = loginUser.getRelTenantIds();
		 projBase.setSubject(subject);
		 Map<String,BigDecimal> pageList = projBaseService.fetchQty(projBase);
		 return pageList;
	 }

	 /**
	  * 分页列表查询
	  *
	  * @param projBase
	  * @return
	  */
	 @GetMapping(value = "/fetchCapacity")
	 public List<ProjectBomRelation> fetchCapacity(ProjBase projBase,
										   HttpServletRequest req) {
		 List<ProjectBomRelation> pageList = projBaseService.fetchCapacity(projBase);
		 return pageList;
	 }

	 /**
	  *   维护Neck产能
	  *
	  * @param projBase
	  * @return
	  */
	 @AutoLog(value = "维护Neck产能")
	 @PostMapping(value = "/updateNeck")
	 public Result<String> updateNeck(@RequestBody List<ProjectBomChild> projBase) {
		 iProjectBomChildService.updateBatchById(projBase);
		 return Result.OK("添加成功！");
	 }

	 /**
	  *   修改文件名
	  *
	  * @param fileParam
	  * @return
	  */
	 @AutoLog(value = "修改文件名")
	 @PostMapping(value = "/changeFileName")
	 public Result<String> changeFileName(@RequestBody FileParam fileParam) throws Exception {
		 String url = projBaseService.changeFileName(fileParam);
		 return Result.OK("提交成功",url);
	 }
	 /**
	  * 获取特殊分类已付
	  *
	  * @param id
	  * @return
	  */
//	 @AutoLog(value = "获取特殊分类已付")
	 @GetMapping(value = "/fetchHasPayByCategoryId")
	 public Result<Map<String, BigDecimal>> fetchHasPayByCategoryId(@RequestParam(name="id",required=true) String id,@RequestParam(name="type",required=true) String type) {
		 Map<String,BigDecimal> map = iProjectCategoryPayService.fetchHasPayByCategoryId(id,type);
		 return Result.OK(map);
	 }

	 /**
	  *   费用支出维护
	  *
	  * @param category
	  * @return
	  */
	 @AutoLog(value = "费用支出维护")
	 @PostMapping(value = "/submitCategoryPay")
	 public Result<String> submitCategoryPay(@RequestBody ProjectCategory category) {
		 iProjectCategoryPayService.submitCategoryPay(category);
		 return Result.OK("提交成功！");
	 }

	 /**
	  * 获取项目下得子项
	  *
	  * @param projBase
	  * @return
	  */
	 @GetMapping(value = "/fetchModelByProjId")
	 public Result<?> fetchModelByProjId(ProjBase projBase,
												 HttpServletRequest req) {
		 List<Map<String,String>> pageList = projBaseService.fetchModelByProjId(projBase);
		 return Result.OK(pageList);
	 }

	 /**
	  * 项目数量统计
	  *
	  * @param projBase
	  * @return
	  */
	 @GetMapping(value = "/fetchProjNum")
	 public Result<?> fetchProjNum(ProjBase projBase,
										 HttpServletRequest req) {
		 Map<String,BigDecimal> entity = projBaseService.fetchProjNum(projBase);
		 return Result.OK(entity);
	 }

	 /**
	  * 项目类型统计
	  *
	  * @param projBase
	  * @return
	  */
	 @GetMapping(value = "/fetchProjType")
	 public Result<?> fetchProjType(ProjBase projBase,
								   HttpServletRequest req) {
		 List<Map<String,Object>> entity = projBaseService.fetchProjType(projBase);
		 return Result.OK(entity);
	 }

	 /**
	  * 项目地区统计
	  *
	  * @param projBase
	  * @return
	  */
	 @GetMapping(value = "/fetchProjArea")
	 public Result<?> fetchProjArea(ProjBase projBase,
									HttpServletRequest req) {
		 List<Map<String,Object>> entity = projBaseService.fetchProjArea(projBase);
		 return Result.OK(entity);
	 }

	 /**
	  * 设备模板导出
	  *
	  * @param request
	  */
	 @RequestMapping(value = "/exportXls")
	 public void exportXls(HttpServletRequest request, ProjBase projBase,HttpServletResponse response) throws IOException {
		 projBaseService.exportXls(request, projBase, BasMaterialImport.class, "设备模板",response);
	 }

	 /**
	  * 项目列表导出
	  *
	  * @param request
	  */
	 @RequestMapping(value = "/exportXlsByList")
	 public ModelAndView exportXlsByList(HttpServletRequest request, ProjBase projBase,HttpServletResponse response) throws IOException {
		 //判断当前用户权限
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String username = loginUser.getUsername();
		 String deptId = loginUser.getDepartIds();
		 if("pageList".equals(projBase.getSource())){
			 String permission = iSysUserService.fetchPermission(username);
			 String auth = "owner";
			 String auther = username;
			 if(StringUtils.isNotEmpty(permission)){
				 if(permission.contains("proj:all")){
					 auth = "all";
				 }else if(permission.contains("proj:subject")){
					 auth = "subject";
					 //当前用户属于那个主体
					 String subject = loginUser.getRelTenantIds();
					 auther = subject;
				 }else if(permission.contains("proj:dept")){
					 auth = "dept";
					 auther = deptId;
				 }
			 }

			 projBase.setAuth(auth);
			 projBase.setAuther(auther);
			 projBase.setCreateUser(username);
		 }

		 List<ProjBase> pageList = projBaseService.exportXlsByList(projBase);

		 // Step.4 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcel());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "项目列表");
		 mv.addObject(NormalExcelConstants.CLASS, ProjBase.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("项目列表", "导出人:"+loginUser.getRealname(), "项目列表"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
		 return mv;

	 }

	 /**
	  * 项目主体统计
	  *
	  * @param projBase
	  * @return
	  */
	 @GetMapping(value = "/fetchProjAmountBySubject")
	 public Result<?> fetchProjAmountBySubject(ProjBase projBase,
									HttpServletRequest req) {
		 List<Map<String,Object>> entity = projBaseService.fetchProjAmountBySubject(projBase);
		 return Result.OK(entity);
	 }

	 /**
	  * 子项产能汇总
	  *
	  * @param projBase
	  * @return
	  */
	 @GetMapping(value = "/fetchModelBySubject")
	 public Result<?> fetchModelBySubject(ProjBase projBase,
											   HttpServletRequest req) {
		 List<ProjectBomChild> entity = projBaseService.fetchModelBySubject(projBase);
		 return Result.OK(entity);
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/fetchCategoryById")
	 public Result<ProjectCategory> fetchCategoryById(@RequestParam(name="id",required=true) String id) {
		 ProjectCategory prc = iProjectCategoryService.getById(id);
		 return Result.OK(prc);
	 }
 }
