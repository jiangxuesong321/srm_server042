package org.cmoc.modules.srm.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.cmoc.common.api.vo.Result;
import org.cmoc.common.system.query.QueryGenerator;
import org.cmoc.common.util.oConvertUtils;
import org.cmoc.modules.srm.entity.ContractObjectChild;
import org.cmoc.modules.srm.service.IContractObjectChildService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.cmoc.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cmoc.common.aspect.annotation.AutoLog;

 /**
 * @Description: contract_object_child
 * @Author: jeecg-boot
 * @Date:   2023-02-21
 * @Version: V1.0
 */
@Api(tags="contract_object_child")
@RestController
@RequestMapping("/srm/contractObjectChild")
@Slf4j
public class ContractObjectChildController extends JeecgController<ContractObjectChild, IContractObjectChildService> {
	@Autowired
	private IContractObjectChildService contractObjectChildService;
	
	/**
	 * 分页列表查询
	 *
	 * @param contractObjectChild
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "contract_object_child-分页列表查询")
	@ApiOperation(value="contract_object_child-分页列表查询", notes="contract_object_child-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ContractObjectChild>> queryPageList(ContractObjectChild contractObjectChild,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ContractObjectChild> queryWrapper = QueryGenerator.initQueryWrapper(contractObjectChild, req.getParameterMap());
		Page<ContractObjectChild> page = new Page<ContractObjectChild>(pageNo, pageSize);
		IPage<ContractObjectChild> pageList = contractObjectChildService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param contractObjectChild
	 * @return
	 */
	@AutoLog(value = "contract_object_child-添加")
	@ApiOperation(value="contract_object_child-添加", notes="contract_object_child-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ContractObjectChild contractObjectChild) {
		contractObjectChildService.save(contractObjectChild);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param contractObjectChild
	 * @return
	 */
	@AutoLog(value = "contract_object_child-编辑")
	@ApiOperation(value="contract_object_child-编辑", notes="contract_object_child-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ContractObjectChild contractObjectChild) {
		contractObjectChildService.updateById(contractObjectChild);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "contract_object_child-通过id删除")
	@ApiOperation(value="contract_object_child-通过id删除", notes="contract_object_child-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		contractObjectChildService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "contract_object_child-批量删除")
	@ApiOperation(value="contract_object_child-批量删除", notes="contract_object_child-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.contractObjectChildService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "contract_object_child-通过id查询")
	@ApiOperation(value="contract_object_child-通过id查询", notes="contract_object_child-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ContractObjectChild> queryById(@RequestParam(name="id",required=true) String id) {
		ContractObjectChild contractObjectChild = contractObjectChildService.getById(id);
		if(contractObjectChild==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(contractObjectChild);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param contractObjectChild
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ContractObjectChild contractObjectChild) {
        return super.exportXls(request, contractObjectChild, ContractObjectChild.class, "contract_object_child");
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
        return super.importExcel(request, response, ContractObjectChild.class);
    }

}
