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
import org.cmoc.common.system.vo.SelectTreeModel;
import org.cmoc.modules.srm.entity.BasMaterialCategory;
import org.cmoc.modules.srm.service.IBasMaterialCategoryService;

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
 * @Description: 设备分类
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/basMaterialCategory")
@Slf4j
public class BasMaterialCategoryController extends JeecgController<BasMaterialCategory, IBasMaterialCategoryService>{
	@Autowired
	private IBasMaterialCategoryService basMaterialCategoryService;

	/**
	 * 分页列表查询
	 *
	 * @param basMaterialCategory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "设备分类-分页列表查询")
	@ApiOperation(value="设备分类-分页列表查询", notes="设备分类-分页列表查询")
	@GetMapping(value = "/rootList")
	public Result<IPage<BasMaterialCategory>> queryPageList(BasMaterialCategory basMaterialCategory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String hasQuery = req.getParameter("hasQuery");
        if(hasQuery != null && "true".equals(hasQuery)){
            QueryWrapper<BasMaterialCategory> queryWrapper =  QueryGenerator.initQueryWrapper(basMaterialCategory, req.getParameterMap());
            List<BasMaterialCategory> list = basMaterialCategoryService.queryTreeListNoPage(queryWrapper);
            IPage<BasMaterialCategory> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        }else{
            String parentId = basMaterialCategory.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            basMaterialCategory.setPid(null);
            QueryWrapper<BasMaterialCategory> queryWrapper = QueryGenerator.initQueryWrapper(basMaterialCategory, req.getParameterMap());
            // 使用 eq 防止模糊查询
            queryWrapper.eq("pid", parentId);
            Page<BasMaterialCategory> page = new Page<BasMaterialCategory>(pageNo, pageSize);
            IPage<BasMaterialCategory> pageList = basMaterialCategoryService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
	}

	 /**
	  * 【vue3专用】加载节点的子数据
	  *
	  * @param pid
	  * @return
	  */
	 @RequestMapping(value = "/loadTreeChildren", method = RequestMethod.GET)
	 public Result<List<SelectTreeModel>> loadTreeChildren(@RequestParam(name = "pid") String pid) {
		 Result<List<SelectTreeModel>> result = new Result<>();
		 try {
			 List<SelectTreeModel> ls = basMaterialCategoryService.queryListByPid(pid);
			 result.setResult(ls);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 e.printStackTrace();
			 result.setMessage(e.getMessage());
			 result.setSuccess(false);
		 }
		 return result;
	 }

	 /**
	  * 【vue3专用】加载一级节点/如果是同步 则所有数据
	  *
	  * @param async
	  * @param pcode
	  * @return
	  */
	 @RequestMapping(value = "/loadTreeRoot", method = RequestMethod.GET)
	 public Result<List<SelectTreeModel>> loadTreeRoot(@RequestParam(name = "async") Boolean async, @RequestParam(name = "pcode") String pcode) {
		 Result<List<SelectTreeModel>> result = new Result<>();
		 try {
			 List<SelectTreeModel> ls = basMaterialCategoryService.queryListByCode(pcode);
			 if (!async) {
				 loadAllChildren(ls);
			 }
			 result.setResult(ls);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 e.printStackTrace();
			 result.setMessage(e.getMessage());
			 result.setSuccess(false);
		 }
		 return result;
	 }

	 /**
	  * 【vue3专用】递归求子节点 同步加载用到
	  *
	  * @param ls
	  */
	 private void loadAllChildren(List<SelectTreeModel> ls) {
		 for (SelectTreeModel tsm : ls) {
			 List<SelectTreeModel> temp = basMaterialCategoryService.queryListByPid(tsm.getKey());
			 if (temp != null && temp.size() > 0) {
				 tsm.setChildren(temp);
				 loadAllChildren(temp);
			 }
		 }
	 }

	 /**
      * 获取子数据
      * @param basMaterialCategory
      * @param req
      * @return
      */
	//@AutoLog(value = "设备分类-获取子数据")
	@ApiOperation(value="设备分类-获取子数据", notes="设备分类-获取子数据")
	@GetMapping(value = "/childList")
	public Result<IPage<BasMaterialCategory>> queryPageList(BasMaterialCategory basMaterialCategory,HttpServletRequest req) {
		QueryWrapper<BasMaterialCategory> queryWrapper = QueryGenerator.initQueryWrapper(basMaterialCategory, req.getParameterMap());
		List<BasMaterialCategory> list = basMaterialCategoryService.list(queryWrapper);
		IPage<BasMaterialCategory> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
		return Result.OK(pageList);
	}

    /**
      * 批量查询子节点
      * @param parentIds 父ID（多个采用半角逗号分割）
      * @return 返回 IPage
      * @param parentIds
      * @return
      */
	//@AutoLog(value = "设备分类-批量获取子数据")
    @ApiOperation(value="设备分类-批量获取子数据", notes="设备分类-批量获取子数据")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<BasMaterialCategory> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<BasMaterialCategory> list = basMaterialCategoryService.list(queryWrapper);
            IPage<BasMaterialCategory> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }
	
	/**
	 *   添加
	 *
	 * @param basMaterialCategory
	 * @return
	 */
	@AutoLog(value = "设备分类-添加")
	@ApiOperation(value="设备分类-添加", notes="设备分类-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BasMaterialCategory basMaterialCategory) throws Exception {
		basMaterialCategoryService.addBasMaterialCategory(basMaterialCategory);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param basMaterialCategory
	 * @return
	 */
	@AutoLog(value = "设备分类-编辑")
	@ApiOperation(value="设备分类-编辑", notes="设备分类-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BasMaterialCategory basMaterialCategory) throws Exception {
		basMaterialCategoryService.updateBasMaterialCategory(basMaterialCategory);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "设备分类-通过id删除")
	@ApiOperation(value="设备分类-通过id删除", notes="设备分类-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) throws Exception {
		basMaterialCategoryService.deleteBasMaterialCategory(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "设备分类-批量删除")
	@ApiOperation(value="设备分类-批量删除", notes="设备分类-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.basMaterialCategoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "设备分类-通过id查询")
	@ApiOperation(value="设备分类-通过id查询", notes="设备分类-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BasMaterialCategory> queryById(@RequestParam(name="id",required=true) String id) {
		BasMaterialCategory basMaterialCategory = basMaterialCategoryService.getById(id);
		if(basMaterialCategory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(basMaterialCategory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param basMaterialCategory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasMaterialCategory basMaterialCategory) {
		return super.exportXls(request, basMaterialCategory, BasMaterialCategory.class, "设备分类");
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
		return super.importExcel(request, response, BasMaterialCategory.class);
    }

}
