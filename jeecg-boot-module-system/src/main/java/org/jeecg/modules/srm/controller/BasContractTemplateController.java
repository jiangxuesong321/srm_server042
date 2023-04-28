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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.modules.srm.entity.ContractBase;
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
import org.jeecg.modules.srm.entity.BasContractTemplateArticle;
import org.jeecg.modules.srm.entity.BasContractTemplatePay;
import org.jeecg.modules.srm.entity.BasContractTemplate;
import org.jeecg.modules.srm.vo.BasContractTemplatePage;
import org.jeecg.modules.srm.service.IBasContractTemplateService;
import org.jeecg.modules.srm.service.IBasContractTemplateArticleService;
import org.jeecg.modules.srm.service.IBasContractTemplatePayService;
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
 * @Description: 合同模板主表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/basContractTemplate")
@Slf4j
public class BasContractTemplateController {
	@Autowired
	private IBasContractTemplateService basContractTemplateService;
	@Autowired
	private IBasContractTemplateArticleService basContractTemplateArticleService;
	@Autowired
	private IBasContractTemplatePayService basContractTemplatePayService;
	
	/**
	 * 分页列表查询
	 *
	 * @param basContractTemplate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "合同模板主表-分页列表查询")
	@ApiOperation(value="合同模板主表-分页列表查询", notes="合同模板主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BasContractTemplate>> queryPageList(BasContractTemplate basContractTemplate,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BasContractTemplate> queryWrapper = QueryGenerator.initQueryWrapper(basContractTemplate, req.getParameterMap());
		Page<BasContractTemplate> page = new Page<BasContractTemplate>(pageNo, pageSize);
		IPage<BasContractTemplate> pageList = basContractTemplateService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param basContractTemplatePage
	 * @return
	 */
	@AutoLog(value = "合同模板主表-添加")
	@ApiOperation(value="合同模板主表-添加", notes="合同模板主表-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BasContractTemplatePage basContractTemplatePage) throws Exception {
		BasContractTemplate basContractTemplate = new BasContractTemplate();
		BeanUtils.copyProperties(basContractTemplatePage, basContractTemplate);
		basContractTemplateService.saveMain(basContractTemplate, basContractTemplatePage.getBasContractTemplateArticleList(),basContractTemplatePage.getBasContractTemplatePayList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param basContractTemplatePage
	 * @return
	 */
	@AutoLog(value = "合同模板主表-编辑")
	@ApiOperation(value="合同模板主表-编辑", notes="合同模板主表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BasContractTemplatePage basContractTemplatePage) throws Exception {
		BasContractTemplate basContractTemplate = new BasContractTemplate();
		BeanUtils.copyProperties(basContractTemplatePage, basContractTemplate);
		BasContractTemplate basContractTemplateEntity = basContractTemplateService.getById(basContractTemplate.getId());
		if(basContractTemplateEntity==null) {
			return Result.error("未找到对应数据");
		}
		basContractTemplateService.updateMain(basContractTemplate, basContractTemplatePage.getBasContractTemplateArticleList(),basContractTemplatePage.getBasContractTemplatePayList());
		return Result.OK("编辑成功!");
	}

	 /**
	  *  编辑
	  *
	  * @param entity
	  * @return
	  */
	 @AutoLog(value = "合同模板主表-编辑")
	 @ApiOperation(value="合同模板主表-编辑", notes="合同模板主表-编辑")
	 @RequestMapping(value = "/editById", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> editById(@RequestBody BasContractTemplate entity) throws Exception {
		 basContractTemplateService.updateById(entity);
		 return Result.OK("编辑成功!");
	 }
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "合同模板主表-通过id删除")
	@ApiOperation(value="合同模板主表-通过id删除", notes="合同模板主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		basContractTemplateService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "合同模板主表-批量删除")
	@ApiOperation(value="合同模板主表-批量删除", notes="合同模板主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.basContractTemplateService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "合同模板主表-通过id查询")
	@ApiOperation(value="合同模板主表-通过id查询", notes="合同模板主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BasContractTemplate> queryById(@RequestParam(name="id",required=true) String id) {
		BasContractTemplate basContractTemplate = basContractTemplateService.getById(id);
		if(basContractTemplate==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(basContractTemplate);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "合同条款表通过主表ID查询")
	@ApiOperation(value="合同条款表主表ID查询", notes="合同条款表-通主表ID查询")
	@GetMapping(value = "/queryBasContractTemplateArticleByMainId")
	public Result<List<BasContractTemplateArticle>> queryBasContractTemplateArticleListByMainId(@RequestParam(name="id",required=true) String id) {
		List<BasContractTemplateArticle> basContractTemplateArticleList = basContractTemplateArticleService.selectByMainId(id);
		return Result.OK(basContractTemplateArticleList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "合同付款周期通过主表ID查询")
	@ApiOperation(value="合同付款周期主表ID查询", notes="合同付款周期-通主表ID查询")
	@GetMapping(value = "/queryBasContractTemplatePayByMainId")
	public Result<List<BasContractTemplatePay>> queryBasContractTemplatePayListByMainId(@RequestParam(name="id",required=true) String id) {
		List<BasContractTemplatePay> basContractTemplatePayList = basContractTemplatePayService.selectByMainId(id);
		return Result.OK(basContractTemplatePayList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param basContractTemplate
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasContractTemplate basContractTemplate) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<BasContractTemplate> queryWrapper = QueryGenerator.initQueryWrapper(basContractTemplate, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<BasContractTemplate> queryList = basContractTemplateService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<BasContractTemplate> basContractTemplateList = new ArrayList<BasContractTemplate>();
      if(oConvertUtils.isEmpty(selections)) {
          basContractTemplateList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          basContractTemplateList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<BasContractTemplatePage> pageList = new ArrayList<BasContractTemplatePage>();
      for (BasContractTemplate main : basContractTemplateList) {
          BasContractTemplatePage vo = new BasContractTemplatePage();
          BeanUtils.copyProperties(main, vo);
          List<BasContractTemplateArticle> basContractTemplateArticleList = basContractTemplateArticleService.selectByMainId(main.getId());
          vo.setBasContractTemplateArticleList(basContractTemplateArticleList);
          List<BasContractTemplatePay> basContractTemplatePayList = basContractTemplatePayService.selectByMainId(main.getId());
          vo.setBasContractTemplatePayList(basContractTemplatePayList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "合同模板主表列表");
      mv.addObject(NormalExcelConstants.CLASS, BasContractTemplatePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("合同模板主表数据", "导出人:"+sysUser.getRealname(), "合同模板主表"));
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
              List<BasContractTemplatePage> list = ExcelImportUtil.importExcel(file.getInputStream(), BasContractTemplatePage.class, params);
              for (BasContractTemplatePage page : list) {
                  BasContractTemplate po = new BasContractTemplate();
                  BeanUtils.copyProperties(page, po);
                  basContractTemplateService.saveMain(po, page.getBasContractTemplateArticleList(),page.getBasContractTemplatePayList());
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
	  * 根据对象里面的属性值作in查询 属性可能会变 用户组件用到
	  * @param obj
	  * @return
	  */
	 @GetMapping("/getMultiContract")
	 public List<BasContractTemplate> getMultiContract(BasContractTemplate obj){
		 List<BasContractTemplate> pageList = basContractTemplateService.list(Wrappers.<BasContractTemplate>query().lambda().
				 in(BasContractTemplate :: getId,obj.getId().split(",")));
		 return pageList;
	 }

}
