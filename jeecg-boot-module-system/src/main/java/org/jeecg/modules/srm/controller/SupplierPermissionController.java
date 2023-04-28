package org.jeecg.modules.srm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.srm.entity.SupplierPermission;
import org.jeecg.modules.srm.model.SupplierPermissionTree;
import org.jeecg.modules.srm.service.ISupplierPermissionService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.model.SysPermissionTree;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 供应商功能菜单表
 * @Author: jeecg-boot
 * @Date:   2022-06-20
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/supplierPermission")
@Slf4j
public class SupplierPermissionController extends JeecgController<SupplierPermission, ISupplierPermissionService> {
	@Autowired
	private ISupplierPermissionService supplierPermissionService;
	
	/**
	 * 分页列表查询
	 *
	 * @param supplierPermission
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "供应商功能菜单表-分页列表查询")
	@ApiOperation(value="供应商功能菜单表-分页列表查询", notes="供应商功能菜单表-分页列表查询")
	@GetMapping(value = "/queryPageList")
	public Result<IPage<SupplierPermission>> queryPageList(SupplierPermission supplierPermission,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SupplierPermission> queryWrapper = QueryGenerator.initQueryWrapper(supplierPermission, req.getParameterMap());
		Page<SupplierPermission> page = new Page<SupplierPermission>(pageNo, pageSize);
		IPage<SupplierPermission> pageList = supplierPermissionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 /**
	  * 加载数据节点
	  *
	  * @return
	  */
	 @RequestMapping(value = "/list", method = RequestMethod.GET)
	 public Result<List<SupplierPermissionTree>> list() {
		 long start = System.currentTimeMillis();
		 Result<List<SupplierPermissionTree>> result = new Result<>();
		 try {
			 LambdaQueryWrapper<SupplierPermission> query = new LambdaQueryWrapper<SupplierPermission>();
			 query.eq(SupplierPermission::getDelFlag, CommonConstant.DEL_FLAG_0);
			 query.orderByAsc(SupplierPermission::getSortNo);
			 List<SupplierPermission> list = supplierPermissionService.list(query);
			 List<SupplierPermissionTree> treeList = new ArrayList<>();
			 getTreeList(treeList, list, null);
			 result.setResult(treeList);
			 result.setSuccess(true);
			 log.info("======获取全部菜单数据=====耗时:" + (System.currentTimeMillis() - start) + "毫秒");
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
		 }
		 return result;
	 }
	/**
	 *   添加
	 *
	 * @param supplierPermission
	 * @return
	 */
	@AutoLog(value = "供应商功能菜单表-添加")
	@ApiOperation(value="供应商功能菜单表-添加", notes="供应商功能菜单表-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SupplierPermission supplierPermission) {
		supplierPermissionService.save(supplierPermission);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param supplierPermission
	 * @return
	 */
	@AutoLog(value = "供应商功能菜单表-编辑")
	@ApiOperation(value="供应商功能菜单表-编辑", notes="供应商功能菜单表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SupplierPermission supplierPermission) {
		supplierPermissionService.updateById(supplierPermission);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "供应商功能菜单表-通过id删除")
	@ApiOperation(value="供应商功能菜单表-通过id删除", notes="供应商功能菜单表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		supplierPermissionService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "供应商功能菜单表-批量删除")
	@ApiOperation(value="供应商功能菜单表-批量删除", notes="供应商功能菜单表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.supplierPermissionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "供应商功能菜单表-通过id查询")
	@ApiOperation(value="供应商功能菜单表-通过id查询", notes="供应商功能菜单表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SupplierPermission> queryById(@RequestParam(name="id",required=true) String id) {
		SupplierPermission supplierPermission = supplierPermissionService.getById(id);
		if(supplierPermission==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(supplierPermission);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param supplierPermission
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SupplierPermission supplierPermission) {
        return super.exportXls(request, supplierPermission, SupplierPermission.class, "供应商功能菜单表");
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
        return super.importExcel(request, response, SupplierPermission.class);
    }

	 private void getTreeList(List<SupplierPermissionTree> treeList, List<SupplierPermission> metaList, SupplierPermissionTree temp) {
		 for (SupplierPermission permission : metaList) {
			 String tempPid = permission.getParentId();
			 SupplierPermissionTree tree = new SupplierPermissionTree(permission);
			 if (temp == null && oConvertUtils.isEmpty(tempPid)) {
				 treeList.add(tree);
				 if (!tree.getIsLeaf()) {
					 getTreeList(treeList, metaList, tree);
				 }
			 } else if (temp != null && tempPid != null && tempPid.equals(temp.getId())) {
				 temp.getChildren().add(tree);
				 if (!tree.getIsLeaf()) {
					 getTreeList(treeList, metaList, tree);
				 }
			 }

		 }
	 }
}
