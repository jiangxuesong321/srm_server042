package org.cmoc.modules.srm.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.shiro.SecurityUtils;
import org.cmoc.common.api.vo.Result;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.common.system.query.QueryGenerator;
import org.cmoc.common.system.vo.LoginUser;
import org.cmoc.common.util.oConvertUtils;
import org.cmoc.modules.srm.entity.ClickMenuHistory;
import org.cmoc.modules.srm.service.IClickMenuHistoryService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.cmoc.modules.system.entity.SysPermission;
import org.cmoc.modules.system.service.ISysPermissionService;
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
 * @Description: click_menu_history
 * @Author: jeecg-boot
 * @Date:   2022-10-21
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/clickMenuHistory")
@Slf4j
public class ClickMenuHistoryController extends JeecgController<ClickMenuHistory, IClickMenuHistoryService> {
	@Autowired
	private IClickMenuHistoryService clickMenuHistoryService;
	@Autowired
	private ISysPermissionService iSysPermissionService;
	
	/**
	 * 分页列表查询
	 *
	 * @param clickMenuHistory
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<List<ClickMenuHistory>> queryList(ClickMenuHistory clickMenuHistory,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		clickMenuHistory.setUsername(username);
		List<ClickMenuHistory> pageList = clickMenuHistoryService.queryList(clickMenuHistory);
		return Result.OK(pageList);
	}

	
	/**
	 *   添加
	 *
	 * @param clickMenuHistory
	 * @return
	 */
	@AutoLog(value = "click_menu_history-添加")
	@ApiOperation(value="click_menu_history-添加", notes="click_menu_history-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ClickMenuHistory clickMenuHistory) {
		List<SysPermission> permissions = iSysPermissionService.list(Wrappers.<SysPermission>query().lambda().
				eq(SysPermission :: getDelFlag, CommonConstant.DEL_FLAG_0).
				eq(SysPermission :: getUrl,clickMenuHistory.getUrl()));

		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();

		if(permissions != null && permissions.size() > 0){
			SysPermission permission = permissions.get(0);
			if(!"首页".equals(permission)){
				clickMenuHistory.setId(String.valueOf(IdWorker.getId()));
				clickMenuHistory.setUsername(username);
				clickMenuHistory.setName(permission.getName());
				clickMenuHistory.setUrl(clickMenuHistory.getUrl());
				clickMenuHistory.setDelFlag(CommonConstant.NO_READ_FLAG);
				clickMenuHistory.setUpdateTime(nowTime);
				clickMenuHistory.setUpdateUser(username);
				clickMenuHistory.setCreateTime(nowTime);
				clickMenuHistory.setCreateUser(username);
				clickMenuHistory.setIconName(permission.getIconName());
				clickMenuHistoryService.save(clickMenuHistory);
			}
		}
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param clickMenuHistory
	 * @return
	 */
	@AutoLog(value = "click_menu_history-编辑")
	@ApiOperation(value="click_menu_history-编辑", notes="click_menu_history-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ClickMenuHistory clickMenuHistory) {
		clickMenuHistoryService.updateById(clickMenuHistory);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "click_menu_history-通过id删除")
	@ApiOperation(value="click_menu_history-通过id删除", notes="click_menu_history-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		clickMenuHistoryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "click_menu_history-批量删除")
	@ApiOperation(value="click_menu_history-批量删除", notes="click_menu_history-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.clickMenuHistoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "click_menu_history-通过id查询")
	@ApiOperation(value="click_menu_history-通过id查询", notes="click_menu_history-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ClickMenuHistory> queryById(@RequestParam(name="id",required=true) String id) {
		ClickMenuHistory clickMenuHistory = clickMenuHistoryService.getById(id);
		if(clickMenuHistory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(clickMenuHistory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param clickMenuHistory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ClickMenuHistory clickMenuHistory) {
        return super.exportXls(request, clickMenuHistory, ClickMenuHistory.class, "click_menu_history");
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
        return super.importExcel(request, response, ClickMenuHistory.class);
    }

}
