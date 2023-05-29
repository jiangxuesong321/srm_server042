package com.cmoc.modules.srm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cmoc.modules.system.service.ISysDictService;
import org.apache.commons.lang3.StringUtils;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.common.system.vo.DictModelMany;
import com.cmoc.modules.srm.entity.BasSupplierAssessment;
import com.cmoc.modules.srm.service.IBasSupplierAssessmentService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import com.cmoc.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cmoc.common.aspect.annotation.AutoLog;

 /**
 * @Description: 供应商细项目考核指标表
 * @Author: jeecg-boot
 * @Date:   2023-05-04
 * @Version: V1.0
 */
@Api(tags="供应商细项目考核指标表")
@RestController
@RequestMapping("/srm/basSupplierAssessment")
@Slf4j
public class BasSupplierAssessmentController extends JeecgController<BasSupplierAssessment, IBasSupplierAssessmentService> {
	@Autowired
	private IBasSupplierAssessmentService basSupplierAssessmentService;

	 @Autowired
	 private ISysDictService iSysDictService;
	/**
	 * 分页列表查询
	 *
	 * @param basSupplierAssessment
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "供应商细项目考核指标表-分页列表查询")
	@ApiOperation(value="供应商细项目考核指标表-分页列表查询", notes="供应商细项目考核指标表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BasSupplierAssessment>> queryPageList(BasSupplierAssessment basSupplierAssessment,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BasSupplierAssessment> queryWrapper = QueryGenerator.initQueryWrapper(basSupplierAssessment, req.getParameterMap());
		Page<BasSupplierAssessment> page = new Page<BasSupplierAssessment>(pageNo, pageSize);
		IPage<BasSupplierAssessment> pageList = basSupplierAssessmentService.page(page, queryWrapper);
		List<String> codeList = new ArrayList<>();
		codeList.add("supp_appraise_rule");
		List<DictModelMany> dictList = iSysDictService.getDictItemsByCodeList(codeList);
		Map<String,String> map = dictList.stream().collect(Collectors.toMap(DictModelMany::getValue, DictModelMany::getText));
		for (BasSupplierAssessment record : pageList.getRecords()) {
			if (record.getAssessmentCategory() != null){
				String assessmentCategoryDict = map.get(record.getAssessmentCategory());
				if(StringUtils.isNotEmpty(assessmentCategoryDict)){
					record.setAssessmentCategoryDict(assessmentCategoryDict);
				}
			}
		}

		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param basSupplierAssessment
	 * @return
	 */
	@AutoLog(value = "供应商细项目考核指标表-添加")
	@ApiOperation(value="供应商细项目考核指标表-添加", notes="供应商细项目考核指标表-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BasSupplierAssessment basSupplierAssessment) {
		basSupplierAssessmentService.save(basSupplierAssessment);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param basSupplierAssessment
	 * @return
	 */
	@AutoLog(value = "供应商细项目考核指标表-编辑")
	@ApiOperation(value="供应商细项目考核指标表-编辑", notes="供应商细项目考核指标表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BasSupplierAssessment basSupplierAssessment) {
		basSupplierAssessmentService.updateById(basSupplierAssessment);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "供应商细项目考核指标表-通过id删除")
	@ApiOperation(value="供应商细项目考核指标表-通过id删除", notes="供应商细项目考核指标表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		basSupplierAssessmentService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "供应商细项目考核指标表-批量删除")
	@ApiOperation(value="供应商细项目考核指标表-批量删除", notes="供应商细项目考核指标表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.basSupplierAssessmentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "供应商细项目考核指标表-通过id查询")
	@ApiOperation(value="供应商细项目考核指标表-通过id查询", notes="供应商细项目考核指标表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BasSupplierAssessment> queryById(@RequestParam(name="id",required=true) String id) {
		BasSupplierAssessment basSupplierAssessment = basSupplierAssessmentService.getById(id);
		if(basSupplierAssessment==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(basSupplierAssessment);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param basSupplierAssessment
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasSupplierAssessment basSupplierAssessment) {
        return super.exportXls(request, basSupplierAssessment, BasSupplierAssessment.class, "供应商细项目考核指标表");
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
        return super.importExcel(request, response, BasSupplierAssessment.class);
    }

}
