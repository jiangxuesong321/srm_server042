package com.cmoc.modules.srm.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.modules.srm.entity.OperateRecord;
import com.cmoc.modules.srm.service.IOperateRecordService;

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
 * @Description: 操作记录表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/operateRecord")
@Slf4j
public class OperateRecordController extends JeecgController<OperateRecord, IOperateRecordService> {
	@Autowired
	private IOperateRecordService operateRecordService;
	
	/**
	 * 分页列表查询
	 *
	 * @param operateRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "操作记录表-分页列表查询")
	@ApiOperation(value="操作记录表-分页列表查询", notes="操作记录表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<OperateRecord>> queryPageList(OperateRecord operateRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<OperateRecord> queryWrapper = QueryGenerator.initQueryWrapper(operateRecord, req.getParameterMap());
		Page<OperateRecord> page = new Page<OperateRecord>(pageNo, pageSize);
		IPage<OperateRecord> pageList = operateRecordService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param operateRecord
	 * @return
	 */
	@AutoLog(value = "操作记录表-添加")
	@ApiOperation(value="操作记录表-添加", notes="操作记录表-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody OperateRecord operateRecord) {
		operateRecordService.save(operateRecord);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param operateRecord
	 * @return
	 */
	@AutoLog(value = "操作记录表-编辑")
	@ApiOperation(value="操作记录表-编辑", notes="操作记录表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody OperateRecord operateRecord) {
		operateRecordService.updateById(operateRecord);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "操作记录表-通过id删除")
	@ApiOperation(value="操作记录表-通过id删除", notes="操作记录表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		operateRecordService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "操作记录表-批量删除")
	@ApiOperation(value="操作记录表-批量删除", notes="操作记录表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.operateRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "操作记录表-通过id查询")
	@ApiOperation(value="操作记录表-通过id查询", notes="操作记录表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<OperateRecord> queryById(@RequestParam(name="id",required=true) String id) {
		OperateRecord operateRecord = operateRecordService.getById(id);
		if(operateRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(operateRecord);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param operateRecord
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, OperateRecord operateRecord) {
        return super.exportXls(request, operateRecord, OperateRecord.class, "操作记录表");
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
        return super.importExcel(request, response, OperateRecord.class);
    }

}
