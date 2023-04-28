package org.jeecg.modules.srm.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.srm.entity.*;
import org.jeecg.modules.srm.service.*;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.srm.vo.BasSupplierPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 供应商基本信息
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/basSupplier")
@Slf4j
public class BasSupplierController {
	@Autowired
	private IBasSupplierService basSupplierService;
	@Autowired
	private IBasSupplierContactService basSupplierContactService;
	@Autowired
	private IBasSupplierQualificationService basSupplierQualificationService;
	@Autowired
	private IBasSupplierBankService basSupplierBankService;
	@Autowired
	private ISupplierAccountService iSupplierAccountService;
	@Autowired
	private IBasSupplierFastService iBasSupplierFastService;

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 分页列表查询
	 *
	 * @param basSupplier
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "供应商基本信息-分页列表查询")
	@ApiOperation(value="供应商基本信息-分页列表查询", notes="供应商基本信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BasSupplier>> queryPageList(BasSupplier basSupplier,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
//		QueryWrapper<BasSupplier> queryWrapper = QueryGenerator.initQueryWrapper(basSupplier, req.getParameterMap());
		Page<BasSupplier> page = new Page<BasSupplier>(pageNo, pageSize);
		IPage<BasSupplier> pageList = basSupplierService.pageList(page, basSupplier);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param basSupplier
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "供应商基本信息-分页列表查询")
	 @ApiOperation(value="供应商基本信息-分页列表查询", notes="供应商基本信息-分页列表查询")
	 @GetMapping(value = "/fetchList")
	 public Result<List<BasSupplier>> fetchList(BasSupplier basSupplier,
													 HttpServletRequest req) {
	 	 List<String> status = new ArrayList<>();
		 status.add("6");
		 status.add("1");
		 List<BasSupplier> pageList = basSupplierService.list(Wrappers.<BasSupplier>query().lambda().in(BasSupplier :: getStatus,status));
		 return Result.OK(pageList);
	 }

	 /**
	  * 根据对象里面的属性值作in查询 属性可能会变 供应商组件用到
	  * @param basSupplier
	  * @return
	  */
	 @GetMapping("/getMultiSupp")
	 public List<BasSupplier> getMultiSupp(BasSupplier basSupplier){
		 QueryWrapper<BasSupplier> queryWrapper = QueryGenerator.initQueryWrapper(basSupplier, null);
		 //update-begin---author:wangshuai ---date:20220104  for：[JTC-297]已冻结用户仍可设置为代理人------------
		 queryWrapper.eq("status",Integer.parseInt(CommonConstant.STATUS_1));
		 //update-end---author:wangshuai ---date:20220104  for：[JTC-297]已冻结用户仍可设置为代理人------------
		 List<BasSupplier> ls = basSupplierService.list(queryWrapper);
		 return ls;
	 }
	
	/**
	 *   添加
	 *
	 * @param basSupplierPage
	 * @return
	 */
	@AutoLog(value = "供应商基本信息-添加")
	@ApiOperation(value="供应商基本信息-添加", notes="供应商基本信息-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BasSupplierPage basSupplierPage) {
		BasSupplier basSupplier = new BasSupplier();
		BeanUtils.copyProperties(basSupplierPage, basSupplier);
		basSupplierService.saveMain(basSupplier, basSupplierPage.getBasSupplierContactList(),basSupplierPage.getBasSupplierQualificationList(),
				basSupplierPage.getBasSupplierBankList(),basSupplierPage.getBasSupplierFastList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param basSupplierPage
	 * @return
	 */
	@AutoLog(value = "供应商基本信息-编辑")
	@ApiOperation(value="供应商基本信息-编辑", notes="供应商基本信息-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BasSupplierPage basSupplierPage) {
		BasSupplier basSupplier = new BasSupplier();
		BeanUtils.copyProperties(basSupplierPage, basSupplier);
		BasSupplier basSupplierEntity = basSupplierService.getById(basSupplier.getId());
		if(basSupplierEntity==null) {
			return Result.error("未找到对应数据");
		}
		basSupplierService.updateMain(basSupplier, basSupplierPage.getBasSupplierContactList(),basSupplierPage.getBasSupplierQualificationList(),basSupplierPage.getBasSupplierBankList(),basSupplierPage.getBasSupplierFastList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "供应商基本信息-通过id删除")
	@ApiOperation(value="供应商基本信息-通过id删除", notes="供应商基本信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		basSupplierService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "供应商基本信息-批量删除")
	@ApiOperation(value="供应商基本信息-批量删除", notes="供应商基本信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.basSupplierService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "供应商基本信息-通过id查询")
	@ApiOperation(value="供应商基本信息-通过id查询", notes="供应商基本信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BasSupplier> queryById(@RequestParam(name="id",required=true) String id) {
		BasSupplier basSupplier = basSupplierService.getById(id);
		if(basSupplier==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(basSupplier);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "供应商联系人通过主表ID查询")
	@ApiOperation(value="供应商联系人主表ID查询", notes="供应商联系人-通主表ID查询")
	@GetMapping(value = "/queryBasSupplierContactByMainId")
	public Result<List<BasSupplierContact>> queryBasSupplierContactListByMainId(@RequestParam(name="id",required=true) String id) {
		List<BasSupplierContact> basSupplierContactList = basSupplierContactService.selectByMainId(id);
		return Result.OK(basSupplierContactList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "供应商资质证书通过主表ID查询")
	@ApiOperation(value="供应商资质证书主表ID查询", notes="供应商资质证书-通主表ID查询")
	@GetMapping(value = "/queryBasSupplierQualificationByMainId")
	public Result<List<BasSupplierQualification>> queryBasSupplierQualificationListByMainId(@RequestParam(name="id",required=true) String id) {
		List<BasSupplierQualification> basSupplierQualificationList = basSupplierQualificationService.selectByMainId(id);
		return Result.OK(basSupplierQualificationList);
	}

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @ApiOperation(value="银行账号", notes="银行账号")
	 @GetMapping(value = "/queryBasSupplierBankByMainId")
	 public Result<List<BasSupplierBank>> queryBasSupplierBankByMainId(@RequestParam(name="id",required=true) String id) {
		 List<BasSupplierBank> basSupplierBankList = basSupplierBankService.selectByMainId(id);
		 return Result.OK(basSupplierBankList);
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @ApiOperation(value="寄件信息", notes="寄件信息")
	 @GetMapping(value = "/queryBasSupplierFastByMainId")
	 public Result<List<BasSupplierFast>> queryBasSupplierFastByMainId(@RequestParam(name="id",required=true) String id) {
		 List<BasSupplierFast> basSupplierFastList = iBasSupplierFastService.list(Wrappers.<BasSupplierFast>query().lambda().
				 eq(BasSupplierFast :: getSuppId,id).
				 eq(BasSupplierFast :: getDelFlag, CommonConstant.DEL_FLAG_0));
		 for(BasSupplierFast fast : basSupplierFastList){
			 fast.setAreaList(Arrays.asList(fast.getArea().split(",")));
		 }
		 return Result.OK(basSupplierFastList);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param basSupplier
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasSupplier basSupplier) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<BasSupplier> queryWrapper = new QueryWrapper<>();
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
      //Step.2 获取导出数据
	  if(StringUtils.isNotEmpty(basSupplier.getName())){
		  queryWrapper.lambda().like(BasSupplier :: getName,basSupplier.getName());
	  }
	  if(StringUtils.isNotEmpty(basSupplier.getCode())){
		queryWrapper.lambda().like(BasSupplier :: getCode,basSupplier.getCode());
	  }
	  if(StringUtils.isNotEmpty(basSupplier.getSupplierProp())){
		queryWrapper.lambda().like(BasSupplier :: getSupplierProp,basSupplier.getSupplierProp());
	  }
	  if(StringUtils.isNotEmpty(basSupplier.getSupplierType())){
		queryWrapper.lambda().in(BasSupplier :: getSupplierType,basSupplier.getSupplierType().split(","));
	  }
	  if(StringUtils.isNotEmpty(basSupplier.getStatus())){
		queryWrapper.lambda().like(BasSupplier :: getStatus,basSupplier.getStatus());
	  }
      List<BasSupplier> queryList = basSupplierService.list(queryWrapper);

      List<BasSupplierContact> contactList = basSupplierContactService.list(Wrappers.<BasSupplierContact>query().lambda().
			  eq(BasSupplierContact :: getDelFlag,CommonConstant.DEL_FLAG_0).
			  eq(BasSupplierContact :: getIsDefault,"1"));

      // Step.3 组装pageList
      List<BasSupplierPage> pageList = new ArrayList<BasSupplierPage>();
      for (BasSupplier main : queryList) {
          BasSupplierPage vo = new BasSupplierPage();
          BeanUtils.copyProperties(main, vo);
          for(BasSupplierContact bc : contactList){
          	if(main.getId().equals(bc.getSupplierId())){
				vo.setContacter(bc.getContacter());
				vo.setContacterTel(bc.getContacterTel());
				vo.setContacterEmail(bc.getContacterEmail());
				break;
			}
		  }

          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "供应商基本信息列表");
      mv.addObject(NormalExcelConstants.CLASS, BasSupplierPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("供应商基本信息数据", "导出人:"+sysUser.getRealname(), "供应商基本信息"));
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
              List<BasSupplierPage> list = ExcelImportUtil.importExcel(file.getInputStream(), BasSupplierPage.class, params);
              for (BasSupplierPage page : list) {
                  BasSupplier po = new BasSupplier();
                  BeanUtils.copyProperties(page, po);
                  basSupplierService.saveMain(po, page.getBasSupplierContactList(),page.getBasSupplierQualificationList(),page.getBasSupplierBankList(),page.getBasSupplierFastList());
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
	  *   开通SRM账号
	  *
	  * @param basSupplierPage
	  * @return
	  */
	 @AutoLog(value = "开通SRM账号")
	 @ApiOperation(value="开通SRM账号", notes="开通SRM账号")
	 @PostMapping(value = "/createSrmAccount")
	 public Result<String> createSrmAccount(@RequestBody BasSupplierPage basSupplierPage) throws Exception {
		 //BasSupplier basSupplier = new BasSupplier();
		 //BeanUtils.copyProperties(basSupplierPage, basSupplier);
		 basSupplierService.createSrmAccount(basSupplierPage);
		 return Result.OK("添加成功！");
	 }

	 /**
	  *   批量开通账号
	  *
	  * @return
	  */
	 @AutoLog(value = "开通SRM账号")
	 @ApiOperation(value="开通SRM账号", notes="开通SRM账号")
	 @PostMapping(value = "/createBatchSrmAccount")
	 public Result<String> createBatchSrmAccount() throws Exception {
	 	List<BasSupplier> suppList = basSupplierService.list(Wrappers.<BasSupplier>query().lambda().eq(BasSupplier :: getStatus,"1"));
	 	for(BasSupplier bs : suppList){
			BasSupplierPage page = new BasSupplierPage();
			BeanUtils.copyProperties(bs,page);

			page.setSysAccount(bs.getCode());
			page.setSysPwd(random());
			Date startTime = sdf.parse("2023-02-01");
			page.setStartTime(startTime);

			Date endTime = sdf.parse("2024-02-01");
			page.setEndTime(endTime);
			basSupplierService.createSrmAccount(page);
		}

	 	return Result.OK("添加成功！");
	 }

	 private static final String BASIC = "123456789qwertyuiopasdfghjklzxcvbnm";

	 public static String random(){
		 char[] basicArray = BASIC.toCharArray();
		 Random random = new Random();
		 char[] result = new char[6];
		 for (int i = 0; i < result.length; i++) {
			 int index = random.nextInt(100) % (basicArray.length);
			 result[i] = basicArray[index];
		 }
		 return new String(result);
	 }

	 /**
	  *   开通SRM账号
	  *
	  * @param basSupplierPage
	  * @return
	  */
	 @AutoLog(value = "续费")
	 @ApiOperation(value="续费", notes="续费")
	 @PostMapping(value = "/reNew")
	 public Result<String> reNew(@RequestBody BasSupplierPage basSupplierPage) throws Exception {
		 //BasSupplier basSupplier = new BasSupplier();
		 //BeanUtils.copyProperties(basSupplierPage, basSupplier);
		 basSupplierService.reNew(basSupplierPage);
		 return Result.OK("添加成功！");
	 }

	 /**
	  *   开通SRM账号
	  *
	  * @param basSupplierPage
	  * @return
	  */
	 @AutoLog(value = "重置SRM账号")
	 @ApiOperation(value="重置SRM账号", notes="重置SRM账号")
	 @PostMapping(value = "/resetSrmAccount")
	 public Result<String> resetSrmAccount(@RequestBody BasSupplierPage basSupplierPage) {
		 basSupplierService.resetSrmAccount(basSupplierPage);
		 return Result.OK("添加成功！");
	 }

	 /**
	  *  编辑
	  *
	  * @param basSupplier
	  * @return
	  */
	 @AutoLog(value = "供应商基本信息-编辑")
	 @ApiOperation(value="供应商基本信息-编辑", notes="供应商基本信息-编辑")
	 @RequestMapping(value = "/editEntity", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> editEntity(@RequestBody BasSupplier basSupplier) {
	 	//供应商状态变更
	 	basSupplierService.updateById(basSupplier);
	 	if("4".equals(basSupplier.getStatus()) || "3".equals(basSupplier.getStatus())){
			//账号冻结
			SupplierAccount account = iSupplierAccountService.getOne(Wrappers.<SupplierAccount>query().lambda().eq(SupplierAccount :: getSupplierId,basSupplier.getId()));
			account.setStatus(2);
			iSupplierAccountService.updateById(account);
		}else if("6".equals(basSupplier.getStatus())){
			//账号冻结
			SupplierAccount account = iSupplierAccountService.getOne(Wrappers.<SupplierAccount>query().lambda().eq(SupplierAccount :: getSupplierId,basSupplier.getId()));
			account.setStatus(1);
			iSupplierAccountService.updateById(account);
		}

		return Result.OK("编辑成功!");
	 }

	 /**
	  * 供应商统计
	  *
	  * @param basSupplier
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/fetchSuppCategory")
	 public Result<List<Map<String,Object>>> fetchSuppCategory(BasSupplier basSupplier,
													 HttpServletRequest req) {
		 List<Map<String,Object>> pageList = basSupplierService.fetchSuppCategory(basSupplier);
		 return Result.OK(pageList);
	 }

	 /**
	  * 供应商统计
	  *
	  * @param basSupplier
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/fetchSuppType")
	 public Result<List<Map<String,Object>>> fetchSuppType(BasSupplier basSupplier,
															   HttpServletRequest req) {
		 List<Map<String,Object>> pageList = basSupplierService.fetchSuppType(basSupplier);
		 return Result.OK(pageList);
	 }

	 /**
	  * 供应商合同
	  *
	  * @param basSupplier
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/fetchSuppContract")
	 public Result<Map<String,Object>> fetchSuppContract(BasSupplier basSupplier,
														   HttpServletRequest req) {
		 Map<String,Object> pageList = basSupplierService.fetchSuppContract(basSupplier);
		 return Result.OK(pageList);
	 }

	 /**
	  * 供应商执行中合同
	  *
	  * @param basSupplier
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/fetchSuppContracting")
	 public Result<Map<String,Object>> fetchSuppContracting(BasSupplier basSupplier,
														 HttpServletRequest req) {
		 Map<String,Object> pageList = basSupplierService.fetchSuppContracting(basSupplier);
		 return Result.OK(pageList);
	 }

	 /**
	  * 供应商状态
	  *
	  * @param basSupplier
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/fetchSuppStatus")
	 public Result<List<Map<String,Object>>> fetchSuppStatus(BasSupplier basSupplier,
															HttpServletRequest req) {
		 List<Map<String,Object>> pageList = basSupplierService.fetchSuppStatus(basSupplier);
		 return Result.OK(pageList);
	 }

	 /**
	  * 活跃供应商数量
	  *
	  * @param basSupplier
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/fetchSuppActive")
	 public Result<Map<String,Object>> fetchSuppActive(BasSupplier basSupplier,
															 HttpServletRequest req) {
		 Map<String,Object> pageList = basSupplierService.fetchSuppActive(basSupplier);
		 return Result.OK(pageList);
	 }
}
