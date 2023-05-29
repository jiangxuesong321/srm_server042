package org.cmoc.modules.srm.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.cmoc.modules.srm.entity.PurchaseRequestMain;
import org.cmoc.modules.srm.utils.JeecgEntityExcel;
import org.cmoc.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.cmoc.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.cmoc.common.api.vo.Result;
import org.cmoc.common.system.query.QueryGenerator;
import org.cmoc.common.util.oConvertUtils;
import org.cmoc.modules.srm.entity.PurPayApplyDetail;
import org.cmoc.modules.srm.entity.PurPayApply;
import org.cmoc.modules.srm.vo.PurPayApplyPage;
import org.cmoc.modules.srm.service.IPurPayApplyService;
import org.cmoc.modules.srm.service.IPurPayApplyDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.cmoc.common.aspect.annotation.AutoLog;

 /**
 * @Description: 付款申请
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/purPayApply")
@Slf4j
public class PurPayApplyController {
	@Autowired
	private IPurPayApplyService purPayApplyService;
	@Autowired
	private IPurPayApplyDetailService purPayApplyDetailService;
	@Autowired
	private ISysUserService iSysUserService;
	
	/**
	 * 分页列表查询
	 *
	 * @param purPayApply
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "付款申请-分页列表查询")
	@ApiOperation(value="付款申请-分页列表查询", notes="付款申请-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PurPayApply>> queryPageList(PurPayApply purPayApply,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = sysUser.getUsername();
		Page<PurPayApply> page = new Page<PurPayApply>(pageNo, pageSize);
		purPayApply.setApprovalUserId(username);

		String permission = iSysUserService.fetchPermission(username);
		String auth = "subject";
		String auther = sysUser.getRelTenantIds();
		if(StringUtils.isNotEmpty(permission)){
			if(permission.contains("pay:all")){
				auth = "all";
			}
		}
		purPayApply.setAuth(auth);
		purPayApply.setAuther(auther);

		IPage<PurPayApply> pageList = purPayApplyService.queryPageList(page, purPayApply);
		return Result.OK(pageList);
	}

	 /**
	  * 分页列表查询
	  *
	  * @param purPayApply
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "付款申请-分页列表查询")
	 @ApiOperation(value="付款申请-分页列表查询", notes="付款申请-分页列表查询")
	 @GetMapping(value = "/fetchListBySuppId")
	 public Result<IPage<PurPayApply>> fetchListBySuppId(PurPayApply purPayApply,
													 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													 HttpServletRequest req) {
		 Page<PurPayApply> page = new Page<PurPayApply>(pageNo, pageSize);
		 IPage<PurPayApply> pageList = purPayApplyService.queryPageList(page, purPayApply);
		 return Result.OK(pageList);
	 }
	
	/**
	 *   添加
	 *
	 * @param purPayApplyPage
	 * @return
	 */
	@AutoLog(value = "付款申请-添加")
	@ApiOperation(value="付款申请-添加", notes="付款申请-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PurPayApplyPage purPayApplyPage) {
		PurPayApply purPayApply = new PurPayApply();
		BeanUtils.copyProperties(purPayApplyPage, purPayApply);
		purPayApplyService.saveMain(purPayApply, purPayApplyPage.getPurPayApplyDetailList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param purPayApplyPage
	 * @return
	 */
	@AutoLog(value = "付款申请-编辑")
	@ApiOperation(value="付款申请-编辑", notes="付款申请-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PurPayApplyPage purPayApplyPage) {
		PurPayApply purPayApply = new PurPayApply();
		BeanUtils.copyProperties(purPayApplyPage, purPayApply);
		PurPayApply purPayApplyEntity = purPayApplyService.getById(purPayApply.getId());
		if(purPayApplyEntity==null) {
			return Result.error("未找到对应数据");
		}
		purPayApplyService.updateMain(purPayApply, purPayApplyPage.getPurPayApplyDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "付款申请-通过id删除")
	@ApiOperation(value="付款申请-通过id删除", notes="付款申请-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		purPayApplyService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "付款申请-批量删除")
	@ApiOperation(value="付款申请-批量删除", notes="付款申请-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.purPayApplyService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "付款申请-通过id查询")
	@ApiOperation(value="付款申请-通过id查询", notes="付款申请-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PurPayApply> queryById(@RequestParam(name="id",required=true) String id) {
		PurPayApply purPayApply = purPayApplyService.getById(id);
		if(purPayApply==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(purPayApply);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "付款申请明细通过主表ID查询")
	@ApiOperation(value="付款申请明细主表ID查询", notes="付款申请明细-通主表ID查询")
	@GetMapping(value = "/queryPurPayApplyDetailByMainId")
	public Result<List<PurPayApplyDetail>> queryPurPayApplyDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PurPayApplyDetail> purPayApplyDetailList = purPayApplyDetailService.selectByMainId(id);
		return Result.OK(purPayApplyDetailList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param purPayApply
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PurPayApply purPayApply) {
      // Step.1 组装查询条件查询数据
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

	  String username = sysUser.getUsername();
	  purPayApply.setApprovalUserId(username);

	  String permission = iSysUserService.fetchPermission(username);
	  String auth = "subject";
	  String auther = sysUser.getRelTenantIds();
	  if(StringUtils.isNotEmpty(permission)){
		if(permission.contains("pay:all")){
			auth = "all";
		}
	  }
	  purPayApply.setAuth(auth);
	  purPayApply.setAuther(auther);

	  List<PurPayApply> pageList = purPayApplyService.queryList(purPayApply);

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcel());
      mv.addObject(NormalExcelConstants.FILE_NAME, "应付管理");
      mv.addObject(NormalExcelConstants.CLASS, PurPayApply.class);
	  mv.addObject(NormalExcelConstants.EXPORT_FIELDS,request.getParameter("field"));
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("应付管理", "导出人:"+sysUser.getRealname(), "应付管理"));
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
              List<PurPayApplyPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PurPayApplyPage.class, params);
              for (PurPayApplyPage page : list) {
                  PurPayApply po = new PurPayApply();
                  BeanUtils.copyProperties(page, po);
                  purPayApplyService.saveMain(po, page.getPurPayApplyDetailList());
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
	  *  审批
	  *
	  * @param purPayApply
	  * @return
	  */
	 @RequestMapping(value = "/toApprove", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> toApprove(@RequestBody PurPayApply purPayApply) throws Exception {
		 purPayApplyService.toApprove(purPayApply);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  * 币种汇总
	  *
	  * @param purPayApply
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/getTotalAmountByCurrency")
	 public Result<List<PurPayApply>> getTotalAmountByCurrency(PurPayApply purPayApply,
													 HttpServletRequest req) {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 String username = sysUser.getUsername();

		 String permission = iSysUserService.fetchPermission(username);
		 String auth = "subject";
		 String auther = sysUser.getRelTenantIds();
		 if(StringUtils.isNotEmpty(permission)){
			 if(permission.contains("pay:all")){
				 auth = "all";
			 }
		 }
		 purPayApply.setAuth(auth);
		 purPayApply.setAuther(auther);

		 List<PurPayApply> pageList = purPayApplyService.getTotalAmountByCurrency(purPayApply);
		 return Result.OK(pageList);
	 }

}
