package com.cmoc.modules.srm.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.modules.srm.entity.BiddingRecordToProfessionals;
import com.cmoc.modules.srm.service.IBiddingRecordToProfessionalsService;

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
 * @Description: bidding_record_to_professionals
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/biddingRecordToProfessionals")
@Slf4j
public class BiddingRecordToProfessionalsController extends JeecgController<BiddingRecordToProfessionals, IBiddingRecordToProfessionalsService> {
	@Autowired
	private IBiddingRecordToProfessionalsService biddingRecordToProfessionalsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param biddingRecordToProfessionals
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "bidding_requistion_relation-分页列表查询")
	@ApiOperation(value="bidding_requistion_relation-分页列表查询", notes="bidding_requistion_relation-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BiddingRecordToProfessionals>> queryPageList(BiddingRecordToProfessionals biddingRecordToProfessionals,
																	 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																	 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																	 HttpServletRequest req) {
		QueryWrapper<BiddingRecordToProfessionals> queryWrapper = QueryGenerator.initQueryWrapper(biddingRecordToProfessionals, req.getParameterMap());
		Page<BiddingRecordToProfessionals> page = new Page<BiddingRecordToProfessionals>(pageNo, pageSize);
		IPage<BiddingRecordToProfessionals> pageList = biddingRecordToProfessionalsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param biddingRecordToProfessionals
	 * @return
	 */
	@AutoLog(value = "bidding_requistion_relation-添加")
	@ApiOperation(value="bidding_requistion_relation-添加", notes="bidding_requistion_relation-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BiddingRecordToProfessionals biddingRecordToProfessionals) {
		biddingRecordToProfessionalsService.save(biddingRecordToProfessionals);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param biddingRecordToProfessionals
	 * @return
	 */
	@AutoLog(value = "bidding_requistion_relation-编辑")
	@ApiOperation(value="bidding_requistion_relation-编辑", notes="bidding_requistion_relation-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BiddingRecordToProfessionals biddingRecordToProfessionals) {
		biddingRecordToProfessionalsService.updateById(biddingRecordToProfessionals);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bidding_requistion_relation-通过id删除")
	@ApiOperation(value="bidding_requistion_relation-通过id删除", notes="bidding_requistion_relation-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		biddingRecordToProfessionalsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bidding_requistion_relation-批量删除")
	@ApiOperation(value="bidding_requistion_relation-批量删除", notes="bidding_requistion_relation-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.biddingRecordToProfessionalsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "bidding_requistion_relation-通过id查询")
	@ApiOperation(value="bidding_requistion_relation-通过id查询", notes="bidding_requistion_relation-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BiddingRecordToProfessionals> queryById(@RequestParam(name="id",required=true) String id) {
		BiddingRecordToProfessionals biddingRecordToProfessionals = biddingRecordToProfessionalsService.getById(id);
		if(biddingRecordToProfessionals ==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(biddingRecordToProfessionals);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param biddingRecordToProfessionals
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BiddingRecordToProfessionals biddingRecordToProfessionals) {
        return super.exportXls(request, biddingRecordToProfessionals, BiddingRecordToProfessionals.class, "bidding_requistion_relation");
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
        return super.importExcel(request, response, BiddingRecordToProfessionals.class);
    }

}
