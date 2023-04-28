package org.jeecg.modules.srm.controller;

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
import org.jeecg.modules.srm.entity.StkOoBillDelivery;
import org.jeecg.modules.srm.entity.StkOoBill;
import org.jeecg.modules.srm.vo.StkOoBillPage;
import org.jeecg.modules.srm.service.IStkOoBillService;
import org.jeecg.modules.srm.service.IStkOoBillDeliveryService;
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
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 出库单
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/stkOoBill")
@Slf4j
public class StkOoBillController {
	@Autowired
	private IStkOoBillService stkOoBillService;
	@Autowired
	private IStkOoBillDeliveryService stkOoBillDeliveryService;
	@Autowired
	private ISysUserService iSysUserService;
	
	/**
	 * 分页列表查询
	 *
	 * @param stkOoBill
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "出库单-分页列表查询")
	@ApiOperation(value="出库单-分页列表查询", notes="出库单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StkOoBill>> queryPageList(StkOoBill stkOoBill,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
//		QueryWrapper<StkOoBill> queryWrapper = QueryGenerator.initQueryWrapper(stkOoBill, req.getParameterMap());
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

		stkOoBill.setAuth(auth);
		stkOoBill.setAuther(auther);

		Page<StkOoBill> page = new Page<StkOoBill>(pageNo, pageSize);
		IPage<StkOoBill> pageList = stkOoBillService.queryPageList(page, stkOoBill);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param stkOoBillPage
	 * @return
	 */
	@AutoLog(value = "出库单-添加")
	@ApiOperation(value="出库单-添加", notes="出库单-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody StkOoBillPage stkOoBillPage) {
		StkOoBill stkOoBill = new StkOoBill();
		BeanUtils.copyProperties(stkOoBillPage, stkOoBill);
		stkOoBillService.saveMain(stkOoBill, stkOoBillPage.getStkOoBillDeliveryList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param stkOoBillPage
	 * @return
	 */
	@AutoLog(value = "出库单-编辑")
	@ApiOperation(value="出库单-编辑", notes="出库单-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody StkOoBillPage stkOoBillPage) {
		StkOoBill stkOoBill = new StkOoBill();
		BeanUtils.copyProperties(stkOoBillPage, stkOoBill);
		StkOoBill stkOoBillEntity = stkOoBillService.getById(stkOoBill.getId());
		if(stkOoBillEntity==null) {
			return Result.error("未找到对应数据");
		}
		stkOoBillService.updateMain(stkOoBill, stkOoBillPage.getStkOoBillDeliveryList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "出库单-通过id删除")
	@ApiOperation(value="出库单-通过id删除", notes="出库单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		stkOoBillService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "出库单-批量删除")
	@ApiOperation(value="出库单-批量删除", notes="出库单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.stkOoBillService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "出库单-通过id查询")
	@ApiOperation(value="出库单-通过id查询", notes="出库单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StkOoBill> queryById(@RequestParam(name="id",required=true) String id) {
		StkOoBill stkOoBill = stkOoBillService.getById(id);
		if(stkOoBill==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(stkOoBill);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "出库明细通过主表ID查询")
	@ApiOperation(value="出库明细主表ID查询", notes="出库明细-通主表ID查询")
	@GetMapping(value = "/queryStkOoBillDeliveryByMainId")
	public Result<List<StkOoBillDelivery>> queryStkOoBillDeliveryListByMainId(@RequestParam(name="id",required=true) String id) {
		List<StkOoBillDelivery> stkOoBillDeliveryList = stkOoBillDeliveryService.selectByMainId(id);
		return Result.OK(stkOoBillDeliveryList);
	}


    /**
    * 导出excel
    *
    * @param request
    * @param stkOoBill
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StkOoBill stkOoBill) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<StkOoBill> queryWrapper = QueryGenerator.initQueryWrapper(stkOoBill, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<StkOoBill> queryList = stkOoBillService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<StkOoBill> stkOoBillList = new ArrayList<StkOoBill>();
      if(oConvertUtils.isEmpty(selections)) {
          stkOoBillList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          stkOoBillList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<StkOoBillPage> pageList = new ArrayList<StkOoBillPage>();
      for (StkOoBill main : stkOoBillList) {
          StkOoBillPage vo = new StkOoBillPage();
          BeanUtils.copyProperties(main, vo);
          List<StkOoBillDelivery> stkOoBillDeliveryList = stkOoBillDeliveryService.selectByMainId(main.getId());
          vo.setStkOoBillDeliveryList(stkOoBillDeliveryList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "出库单列表");
      mv.addObject(NormalExcelConstants.CLASS, StkOoBillPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("出库单数据", "导出人:"+sysUser.getRealname(), "出库单"));
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
              List<StkOoBillPage> list = ExcelImportUtil.importExcel(file.getInputStream(), StkOoBillPage.class, params);
              for (StkOoBillPage page : list) {
                  StkOoBill po = new StkOoBill();
                  BeanUtils.copyProperties(page, po);
                  stkOoBillService.saveMain(po, page.getStkOoBillDeliveryList());
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

}
