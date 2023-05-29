package com.cmoc.modules.srm.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.modules.srm.entity.BasProfessionals;
import com.cmoc.modules.srm.service.IBasProfessionalsService;

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
 * @Description: 专家列表
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/basProfessionals")
@Slf4j
public class BasProfessionalsController extends JeecgController<BasProfessionals, IBasProfessionalsService> {
	@Autowired
	private IBasProfessionalsService basProfessionalsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param basProfessionals
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "专家列表-分页列表查询")
	@ApiOperation(value="专家列表-分页列表查询", notes="专家列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BasProfessionals>> queryPageList(BasProfessionals basProfessionals,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BasProfessionals> queryWrapper = QueryGenerator.initQueryWrapper(basProfessionals, req.getParameterMap());
		queryWrapper.lambda().eq(BasProfessionals :: getIsEnabled,"1");
		Page<BasProfessionals> page = new Page<BasProfessionals>(pageNo, pageSize);
		IPage<BasProfessionals> pageList = basProfessionalsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param basProfessionals
	 * @return
	 */
	@AutoLog(value = "专家列表-添加")
	@ApiOperation(value="专家列表-添加", notes="专家列表-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BasProfessionals basProfessionals) {
		//删除存在记录
		basProfessionalsService.remove(Wrappers.<BasProfessionals>query().lambda().eq(BasProfessionals :: getCode,basProfessionals.getCode()).eq(BasProfessionals :: getIsEnabled,"0"));
		basProfessionalsService.save(basProfessionals);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param basProfessionals
	 * @return
	 */
	@AutoLog(value = "专家列表-编辑")
	@ApiOperation(value="专家列表-编辑", notes="专家列表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BasProfessionals basProfessionals) {
		basProfessionalsService.updateById(basProfessionals);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "专家列表-通过id删除")
	@ApiOperation(value="专家列表-通过id删除", notes="专家列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		basProfessionalsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "专家列表-批量删除")
	@ApiOperation(value="专家列表-批量删除", notes="专家列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.basProfessionalsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "专家列表-通过id查询")
	@ApiOperation(value="专家列表-通过id查询", notes="专家列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BasProfessionals> queryById(@RequestParam(name="id",required=true) String id) {
		BasProfessionals basProfessionals = basProfessionalsService.getById(id);
		if(basProfessionals==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(basProfessionals);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param basProfessionals
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasProfessionals basProfessionals) {
        return super.exportXls(request, basProfessionals, BasProfessionals.class, "专家列表");
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
        return super.importExcel(request, response, BasProfessionals.class);
    }

}
