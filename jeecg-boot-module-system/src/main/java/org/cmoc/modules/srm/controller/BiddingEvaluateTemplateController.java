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
import org.cmoc.modules.srm.entity.BiddingEvaluateTemplateItem;
import org.cmoc.modules.srm.entity.BiddingEvaluateTemplate;
import org.cmoc.modules.srm.vo.BiddingEvaluateTemplatePage;
import org.cmoc.modules.srm.service.IBiddingEvaluateTemplateService;
import org.cmoc.modules.srm.service.IBiddingEvaluateTemplateItemService;
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
 * @Description: 评标模板表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/biddingEvaluateTemplate")
@Slf4j
public class BiddingEvaluateTemplateController {
	@Autowired
	private IBiddingEvaluateTemplateService biddingEvaluateTemplateService;
	@Autowired
	private IBiddingEvaluateTemplateItemService biddingEvaluateTemplateItemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param biddingEvaluateTemplate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "评标模板表-分页列表查询")
	@ApiOperation(value="评标模板表-分页列表查询", notes="评标模板表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BiddingEvaluateTemplate>> queryPageList(BiddingEvaluateTemplate biddingEvaluateTemplate,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BiddingEvaluateTemplate> queryWrapper = QueryGenerator.initQueryWrapper(biddingEvaluateTemplate, req.getParameterMap());
		Page<BiddingEvaluateTemplate> page = new Page<BiddingEvaluateTemplate>(pageNo, pageSize);
		IPage<BiddingEvaluateTemplate> pageList = biddingEvaluateTemplateService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param biddingEvaluateTemplatePage
	 * @return
	 */
	@AutoLog(value = "评标模板表-添加")
	@ApiOperation(value="评标模板表-添加", notes="评标模板表-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BiddingEvaluateTemplatePage biddingEvaluateTemplatePage) {
		BiddingEvaluateTemplate biddingEvaluateTemplate = new BiddingEvaluateTemplate();
		BeanUtils.copyProperties(biddingEvaluateTemplatePage, biddingEvaluateTemplate);
		biddingEvaluateTemplateService.saveMain(biddingEvaluateTemplate, biddingEvaluateTemplatePage.getBiddingEvaluateTemplateItemList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param biddingEvaluateTemplatePage
	 * @return
	 */
	@AutoLog(value = "评标模板表-编辑")
	@ApiOperation(value="评标模板表-编辑", notes="评标模板表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BiddingEvaluateTemplatePage biddingEvaluateTemplatePage) {
		BiddingEvaluateTemplate biddingEvaluateTemplate = new BiddingEvaluateTemplate();
		BeanUtils.copyProperties(biddingEvaluateTemplatePage, biddingEvaluateTemplate);
		BiddingEvaluateTemplate biddingEvaluateTemplateEntity = biddingEvaluateTemplateService.getById(biddingEvaluateTemplate.getId());
		if(biddingEvaluateTemplateEntity==null) {
			return Result.error("未找到对应数据");
		}
		biddingEvaluateTemplateService.updateMain(biddingEvaluateTemplate, biddingEvaluateTemplatePage.getBiddingEvaluateTemplateItemList());
		return Result.OK("编辑成功!");
	}

	 /**
	  *  编辑
	  *
	  * @param entity
	  * @return
	  */
	 @AutoLog(value = "评标模板表-编辑")
	 @ApiOperation(value="评标模板表-编辑", notes="评标模板表-编辑")
	 @RequestMapping(value = "/editById", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> editById(@RequestBody BiddingEvaluateTemplate entity) {
		 biddingEvaluateTemplateService.updateById(entity);
		 return Result.OK("编辑成功!");
	 }
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "评标模板表-通过id删除")
	@ApiOperation(value="评标模板表-通过id删除", notes="评标模板表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		biddingEvaluateTemplateService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "评标模板表-批量删除")
	@ApiOperation(value="评标模板表-批量删除", notes="评标模板表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.biddingEvaluateTemplateService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "评标模板表-通过id查询")
	@ApiOperation(value="评标模板表-通过id查询", notes="评标模板表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BiddingEvaluateTemplate> queryById(@RequestParam(name="id",required=true) String id) {
		BiddingEvaluateTemplate biddingEvaluateTemplate = biddingEvaluateTemplateService.getById(id);
		if(biddingEvaluateTemplate==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(biddingEvaluateTemplate);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "评标模板评分项表通过主表ID查询")
	@ApiOperation(value="评标模板评分项表主表ID查询", notes="评标模板评分项表-通主表ID查询")
	@GetMapping(value = "/queryBiddingEvaluateTemplateItemByMainId")
	public Result<List<BiddingEvaluateTemplateItem>> queryBiddingEvaluateTemplateItemListByMainId(@RequestParam(name="id",required=true) String id) {
		List<BiddingEvaluateTemplateItem> biddingEvaluateTemplateItemList = biddingEvaluateTemplateItemService.selectByMainId(id);
		return Result.OK(biddingEvaluateTemplateItemList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param biddingEvaluateTemplate
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BiddingEvaluateTemplate biddingEvaluateTemplate) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<BiddingEvaluateTemplate> queryWrapper = QueryGenerator.initQueryWrapper(biddingEvaluateTemplate, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<BiddingEvaluateTemplate> queryList = biddingEvaluateTemplateService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<BiddingEvaluateTemplate> biddingEvaluateTemplateList = new ArrayList<BiddingEvaluateTemplate>();
      if(oConvertUtils.isEmpty(selections)) {
          biddingEvaluateTemplateList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          biddingEvaluateTemplateList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<BiddingEvaluateTemplatePage> pageList = new ArrayList<BiddingEvaluateTemplatePage>();
      for (BiddingEvaluateTemplate main : biddingEvaluateTemplateList) {
          BiddingEvaluateTemplatePage vo = new BiddingEvaluateTemplatePage();
          BeanUtils.copyProperties(main, vo);
          List<BiddingEvaluateTemplateItem> biddingEvaluateTemplateItemList = biddingEvaluateTemplateItemService.selectByMainId(main.getId());
          vo.setBiddingEvaluateTemplateItemList(biddingEvaluateTemplateItemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "评标模板表列表");
      mv.addObject(NormalExcelConstants.CLASS, BiddingEvaluateTemplatePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("评标模板表数据", "导出人:"+sysUser.getRealname(), "评标模板表"));
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
              List<BiddingEvaluateTemplatePage> list = ExcelImportUtil.importExcel(file.getInputStream(), BiddingEvaluateTemplatePage.class, params);
              for (BiddingEvaluateTemplatePage page : list) {
                  BiddingEvaluateTemplate po = new BiddingEvaluateTemplate();
                  BeanUtils.copyProperties(page, po);
                  biddingEvaluateTemplateService.saveMain(po, page.getBiddingEvaluateTemplateItemList());
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
