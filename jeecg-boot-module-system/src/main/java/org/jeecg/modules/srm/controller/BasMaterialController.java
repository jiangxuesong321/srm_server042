package org.jeecg.modules.srm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.srm.entity.*;
import org.jeecg.modules.srm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

 /**
 * @Description: 设备管理
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/basMaterial")
@Slf4j
public class BasMaterialController extends JeecgController<BasMaterial, IBasMaterialService> {
	@Autowired
	private IBasMaterialService basMaterialService;
	@Autowired
	private IContractObjectService iContractObjectService;
	@Autowired
	private IBasMaterialChildService iBasMaterialChildService;
	@Autowired
	private IBasMaterialFieldService iBasMaterialFieldService;
	
	/**
	 * 分页列表查询
	 *
	 * @param basMaterial
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "设备管理-分页列表查询")
	@ApiOperation(value="设备管理-分页列表查询", notes="设备管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BasMaterial>> queryPageList(BasMaterial basMaterial,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String code = "";
		String name = "";
		if(StringUtils.isNotEmpty(basMaterial.getCode())){
			code = basMaterial.getCode();
			basMaterial.setCode(null);
		}
		if(StringUtils.isNotEmpty(basMaterial.getName())){
			name = basMaterial.getName();
			basMaterial.setName(null);
		}
		QueryWrapper<BasMaterial> queryWrapper = QueryGenerator.initQueryWrapper(basMaterial, req.getParameterMap());
		Page<BasMaterial> page = new Page<BasMaterial>(pageNo, pageSize);

		if(StringUtils.isNotEmpty(code)){
			queryWrapper.lambda().like(BasMaterial :: getCode,code);
		}
		if(StringUtils.isNotEmpty(name)){
			queryWrapper.lambda().like(BasMaterial :: getName,name);
		}

		IPage<BasMaterial> iPage = basMaterialService.page(page, queryWrapper);
		List<BasMaterial> pageList = iPage.getRecords();
		if(pageList != null && pageList.size() > 0){
			List<String> prodCodes = new ArrayList<>();
			for(BasMaterial bm : pageList){
				bm.setCategoryIds(bm.getCategoryId().split(","));
				prodCodes.add(bm.getCode());
			}
			List<ContractObject> objList = iContractObjectService.fetchTotalByEqp(prodCodes);
			for(BasMaterial bm : pageList){
				for(ContractObject co : objList){
					if(bm.getCode().equals(co.getProdCode())){
						bm.setQty(co.getQty());
						break;
					}
				}
			}
		}
		return Result.OK(iPage);
	}
	
	/**
	 *   添加
	 *
	 * @param basMaterial
	 * @return
	 */
	@AutoLog(value = "设备管理-添加")
	@ApiOperation(value="设备管理-添加", notes="设备管理-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BasMaterial basMaterial) throws Exception {
		basMaterialService.saveEntity(basMaterial);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param basMaterial
	 * @return
	 */
	@AutoLog(value = "设备管理-编辑")
	@ApiOperation(value="设备管理-编辑", notes="设备管理-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BasMaterial basMaterial) {
		basMaterialService.updateEntity(basMaterial);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "设备管理-通过id删除")
	@ApiOperation(value="设备管理-通过id删除", notes="设备管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		basMaterialService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "设备管理-批量删除")
	@ApiOperation(value="设备管理-批量删除", notes="设备管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.basMaterialService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "设备管理-通过id查询")
	@ApiOperation(value="设备管理-通过id查询", notes="设备管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BasMaterial> queryById(@RequestParam(name="id",required=true) String id) {
		BasMaterial basMaterial = basMaterialService.getById(id);
		if(basMaterial==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(basMaterial);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param basMaterial
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasMaterial basMaterial) {

        return basMaterialService.exportXls(request, basMaterial, BasMaterial.class, "设备管理");
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
        return basMaterialService.importExcel(request, response, BasMaterial.class);
    }

	 /**
	  * 历史报价计量
	  *
	  * @param basMaterial
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/fetchHistoryQuote")
	 public Result<IPage<BiddingRecord>> fetchHistoryQuote(BasMaterial basMaterial,
													 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													 HttpServletRequest req) {
		 Page<BasMaterial> page = new Page<BasMaterial>(pageNo, pageSize);
		 IPage<BiddingRecord> iPage = basMaterialService.fetchHistoryQuote(page, basMaterial);
		 return Result.OK(iPage);
	 }

	 /**
	  * 通过id查询
	  *
	  * @param prodId
	  * @return
	  */
	 @GetMapping(value = "/fetchChildList")
	 public Result<List<BasMaterialChild>> fetchChildList(@RequestParam(name="prodId",required=true) String prodId) {
		 List<BasMaterialChild> childList = iBasMaterialChildService.list(Wrappers.<BasMaterialChild>query().lambda().eq(BasMaterialChild :: getProdId,prodId));
		 return Result.OK(childList);
	 }

	 /**
	  * 通过id查询
	  *
	  * @param prodId
	  * @return
	  */
	 @GetMapping(value = "/fetchFieldList")
	 public Result<List<BasMaterialField>> fetchFieldList(@RequestParam(name="prodId",required=true) String prodId,
															 @RequestParam(name="type",required=true) String type) {
		 List<BasMaterialField> childList = iBasMaterialFieldService.
				 list(Wrappers.<BasMaterialField>query().lambda().
						 eq(BasMaterialField :: getProdId,prodId).
						 eq(BasMaterialField :: getType,type));
		 return Result.OK(childList);
	 }

}
