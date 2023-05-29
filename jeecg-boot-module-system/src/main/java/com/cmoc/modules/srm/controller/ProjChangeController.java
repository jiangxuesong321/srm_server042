package com.cmoc.modules.srm.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.modules.srm.entity.ProjChange;
import com.cmoc.modules.srm.service.IProjChangeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import com.cmoc.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.ApiOperation;
import com.cmoc.common.aspect.annotation.AutoLog;

 /**
 * @Description: proj_change
 * @Author: jeecg-boot
 * @Date:   2022-10-09
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/projChange")
@Slf4j
public class ProjChangeController extends JeecgController<ProjChange, IProjChangeService> {
	@Autowired
	private IProjChangeService projChangeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param projChange
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "proj_change-分页列表查询")
	@ApiOperation(value="proj_change-分页列表查询", notes="proj_change-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ProjChange>> queryPageList(ProjChange projChange,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProjChange> queryWrapper = QueryGenerator.initQueryWrapper(projChange, req.getParameterMap());
		Page<ProjChange> page = new Page<ProjChange>(pageNo, pageSize);
		if(StringUtils.isNotEmpty(projChange.getSort())){
			queryWrapper.lambda().like(ProjChange :: getSort,projChange.getSort());
		}
		IPage<ProjChange> pageList = projChangeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param projChange
	 * @return
	 */
	@AutoLog(value = "proj_change-添加")
	@ApiOperation(value="proj_change-添加", notes="proj_change-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ProjChange projChange) {
		projChangeService.save(projChange);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param projChange
	 * @return
	 */
	@AutoLog(value = "proj_change-编辑")
	@ApiOperation(value="proj_change-编辑", notes="proj_change-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ProjChange projChange) {
		projChangeService.updateById(projChange);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "proj_change-通过id删除")
	@ApiOperation(value="proj_change-通过id删除", notes="proj_change-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		projChangeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "proj_change-批量删除")
	@ApiOperation(value="proj_change-批量删除", notes="proj_change-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.projChangeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "proj_change-通过id查询")
	@ApiOperation(value="proj_change-通过id查询", notes="proj_change-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ProjChange> queryById(@RequestParam(name="id",required=true) String id) {
		ProjChange projChange = projChangeService.getById(id);
		if(projChange==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(projChange);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param projChange
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProjChange projChange) {
        return super.exportXls(request, projChange, ProjChange.class, "项目变更");
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
        return super.importExcel(request, response, ProjChange.class);
    }


}
