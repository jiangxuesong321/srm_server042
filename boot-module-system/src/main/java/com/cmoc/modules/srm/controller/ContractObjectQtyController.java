package com.cmoc.modules.srm.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.constant.CommonConstant;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.modules.srm.entity.ContractObjectQty;
import com.cmoc.modules.srm.service.IContractObjectQtyService;

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
 * @Description: contract_object_qty
 * @Author: jeecg-boot
 * @Date:   2022-10-10
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/contractObjectQty")
@Slf4j
public class ContractObjectQtyController extends JeecgController<ContractObjectQty, IContractObjectQtyService> {
	@Autowired
	private IContractObjectQtyService contractObjectQtyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param contractObjectQty
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "contract_object_qty-分页列表查询")
	@ApiOperation(value="contract_object_qty-分页列表查询", notes="contract_object_qty-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ContractObjectQty>> queryPageList(ContractObjectQty contractObjectQty,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ContractObjectQty> queryWrapper = QueryGenerator.initQueryWrapper(contractObjectQty, req.getParameterMap());
		Page<ContractObjectQty> page = new Page<ContractObjectQty>(pageNo, pageSize);
		IPage<ContractObjectQty> pageList = contractObjectQtyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param contractObjectQty
	 * @return
	 */
	@AutoLog(value = "contract_object_qty-添加")
	@ApiOperation(value="contract_object_qty-添加", notes="contract_object_qty-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ContractObjectQty contractObjectQty) {
		contractObjectQtyService.save(contractObjectQty);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param contractObjectQty
	 * @return
	 */
	@AutoLog(value = "contract_object_qty-编辑")
	@ApiOperation(value="contract_object_qty-编辑", notes="contract_object_qty-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ContractObjectQty contractObjectQty) {
		contractObjectQtyService.updateById(contractObjectQty);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "contract_object_qty-通过id删除")
	@ApiOperation(value="contract_object_qty-通过id删除", notes="contract_object_qty-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		contractObjectQtyService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "contract_object_qty-批量删除")
	@ApiOperation(value="contract_object_qty-批量删除", notes="contract_object_qty-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.contractObjectQtyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "contract_object_qty-通过id查询")
	@ApiOperation(value="contract_object_qty-通过id查询", notes="contract_object_qty-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ContractObjectQty> queryById(@RequestParam(name="id",required=true) String id) {
		ContractObjectQty contractObjectQty = contractObjectQtyService.getById(id);
		if(contractObjectQty==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(contractObjectQty);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param contractObjectQty
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ContractObjectQty contractObjectQty) {
        return super.exportXls(request, contractObjectQty, ContractObjectQty.class, "contract_object_qty");
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
        return super.importExcel(request, response, ContractObjectQty.class);
    }

	 /**
	  * 分页列表查询
	  *
	  * @param contractObjectQty
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "contract_object_qty-分页列表查询")
	 @ApiOperation(value="contract_object_qty-分页列表查询", notes="contract_object_qty-分页列表查询")
	 @GetMapping(value = "/fetchList")
	 public Result<Page<ContractObjectQty>> fetchList(ContractObjectQty contractObjectQty,
														  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														   HttpServletRequest req) {
		 contractObjectQty.setSuppId(null);
		 QueryWrapper<ContractObjectQty> queryWrapper = QueryGenerator.initQueryWrapper(contractObjectQty, req.getParameterMap());
		 queryWrapper.lambda().orderByAsc(ContractObjectQty :: getSort);
		 queryWrapper.lambda().eq(ContractObjectQty :: getDelFlag, CommonConstant.DEL_FLAG_0);
		 Page<ContractObjectQty> page = new Page<ContractObjectQty>(pageNo, pageSize);
		 Page<ContractObjectQty> pageList = contractObjectQtyService.page(page,queryWrapper);
		 return Result.OK(pageList);
	 }

}