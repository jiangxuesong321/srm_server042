package com.cmoc.modules.srm.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.constant.CommonConstant;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.common.system.vo.LoginUser;
import com.cmoc.modules.srm.entity.BasTrade;
import com.cmoc.modules.srm.service.IBasTradeService;

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
 * @Description: bas_trade
 * @Author: jeecg-boot
 * @Date:   2022-11-25
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/basTrade")
@Slf4j
public class BasTradeController extends JeecgController<BasTrade, IBasTradeService> {
	@Autowired
	private IBasTradeService basTradeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param basTrade
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "bas_trade-分页列表查询")
	@ApiOperation(value="bas_trade-分页列表查询", notes="bas_trade-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BasTrade>> queryPageList(BasTrade basTrade,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BasTrade> queryWrapper = QueryGenerator.initQueryWrapper(basTrade, req.getParameterMap());
		Page<BasTrade> page = new Page<BasTrade>(pageNo, pageSize);
		IPage<BasTrade> pageList = basTradeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param basTrade
	 * @return
	 */
	@AutoLog(value = "bas_trade-添加")
	@ApiOperation(value="bas_trade-添加", notes="bas_trade-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BasTrade basTrade) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();

		basTrade.setCreateTime(nowTime);
		basTrade.setUpdateTime(nowTime);
		basTrade.setCreateUser(username);
		basTrade.setUpdateUser(username);

		basTrade.setDelFlag(CommonConstant.NO_READ_FLAG);
		if(basTrade.getAddTaxBas() != null){
			basTrade.setAddTax(basTrade.getAddTaxBas().add(new BigDecimal(100)));
		}
		if(basTrade.getCustomsTaxBas() != null){
			basTrade.setCustomsTax(basTrade.getCustomsTaxBas().add(new BigDecimal(100)));
		}
		basTradeService.save(basTrade);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param basTrade
	 * @return
	 */
	@AutoLog(value = "bas_trade-编辑")
	@ApiOperation(value="bas_trade-编辑", notes="bas_trade-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BasTrade basTrade) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();

		basTrade.setUpdateTime(nowTime);
		basTrade.setUpdateUser(username);

		if(basTrade.getAddTaxBas() != null){
			basTrade.setAddTax(basTrade.getAddTaxBas().add(new BigDecimal(100)));
		}
		if(basTrade.getCustomsTaxBas() != null){
			basTrade.setCustomsTax(basTrade.getCustomsTaxBas().add(new BigDecimal(100)));
		}
		basTradeService.updateById(basTrade);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bas_trade-通过id删除")
	@ApiOperation(value="bas_trade-通过id删除", notes="bas_trade-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		basTradeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bas_trade-批量删除")
	@ApiOperation(value="bas_trade-批量删除", notes="bas_trade-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.basTradeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "bas_trade-通过id查询")
	@ApiOperation(value="bas_trade-通过id查询", notes="bas_trade-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BasTrade> queryById(@RequestParam(name="id",required=true) String id) {
		BasTrade basTrade = basTradeService.getById(id);
		if(basTrade==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(basTrade);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param basTrade
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasTrade basTrade) {
        return super.exportXls(request, basTrade, BasTrade.class, "bas_trade");
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
        return super.importExcel(request, response, BasTrade.class);
    }

}
