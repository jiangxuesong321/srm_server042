package com.cmoc.modules.srm.controller;

import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.constant.CommonConstant;
import com.cmoc.common.system.query.QueryGenerator;
import com.cmoc.common.util.PasswordUtil;
import com.cmoc.common.util.oConvertUtils;
import com.cmoc.modules.base.service.BaseCommonService;
import com.cmoc.modules.srm.entity.SupplierAccount;
import com.cmoc.modules.srm.service.ISupplierAccountService;

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
 * @Description: 供应商账号
 * @Author: jeecg-boot
 * @Date:   2022-06-19
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/supplierAccount")
@Slf4j
public class SupplierAccountController extends JeecgController<SupplierAccount, ISupplierAccountService> {
	@Autowired
	private ISupplierAccountService supplierAccountService;
	 @Autowired
	 private BaseCommonService baseCommonService;
	/**
	 * 分页列表查询
	 *
	 * @param supplierAccount
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "供应商账号-分页列表查询")
	@ApiOperation(value="供应商账号-分页列表查询", notes="供应商账号-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SupplierAccount>> queryPageList(SupplierAccount supplierAccount,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SupplierAccount> queryWrapper = QueryGenerator.initQueryWrapper(supplierAccount, req.getParameterMap());
		Page<SupplierAccount> page = new Page<SupplierAccount>(pageNo, pageSize);
		IPage<SupplierAccount> pageList = supplierAccountService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param supplierAccount
	 * @return
	 */
	@AutoLog(value = "供应商账号-添加")
	@ApiOperation(value="供应商账号-添加", notes="供应商账号-添加")
	@PostMapping(value = "/add")
	public Result<SupplierAccount> add(@RequestBody SupplierAccount supplierAccount) {
		Result<SupplierAccount> result = new Result<SupplierAccount>();

		try {
			supplierAccount.setCreateTime(new Date());//设置创建时间
			String salt = oConvertUtils.randomGen(8);
			supplierAccount.setSalt(salt);
			String passwordEncode = PasswordUtil.encrypt(supplierAccount.getUsername(), supplierAccount.getPassword(), salt);
			supplierAccount.setPassword(passwordEncode);
			supplierAccount.setStatus(1);
			supplierAccount.setDelFlag(CommonConstant.DEL_FLAG_0);
			// 保存用户走一个service 保证事务
			supplierAccountService.save(supplierAccount);
			baseCommonService.addLog("添加用户，username： " + supplierAccount.getUsername() ,CommonConstant.LOG_TYPE_2, 2);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	 *  编辑
	 *
	 * @param supplierAccount
	 * @return
	 */
	@AutoLog(value = "供应商账号-编辑")
	@ApiOperation(value="供应商账号-编辑", notes="供应商账号-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<SupplierAccount> edit(@RequestBody SupplierAccount supplierAccount) {
		Result<SupplierAccount> result = new Result<SupplierAccount>();
		try {
			SupplierAccount sysUser = supplierAccountService.getById(supplierAccount.getId());
			baseCommonService.addLog("编辑用户，username： " +sysUser.getUsername() ,CommonConstant.LOG_TYPE_2, 2);
			if(sysUser==null) {
				result.error500("未找到对应实体");
			}else {
				supplierAccount.setUpdateTime(new Date());
				//String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), sysUser.getSalt());
				supplierAccount.setPassword(sysUser.getPassword());
				// 修改用户走一个service 保证事务
				supplierAccountService.updateById(supplierAccount);
				result.success("修改成功!");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "供应商账号-通过id删除")
	@ApiOperation(value="供应商账号-通过id删除", notes="供应商账号-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		supplierAccountService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "供应商账号-批量删除")
	@ApiOperation(value="供应商账号-批量删除", notes="供应商账号-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.supplierAccountService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "供应商账号-通过id查询")
	@ApiOperation(value="供应商账号-通过id查询", notes="供应商账号-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SupplierAccount> queryById(@RequestParam(name="id",required=true) String id) {
		SupplierAccount supplierAccount = supplierAccountService.getById(id);
		if(supplierAccount==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(supplierAccount);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param supplierAccount
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SupplierAccount supplierAccount) {
        return super.exportXls(request, supplierAccount, SupplierAccount.class, "供应商账号");
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
        return super.importExcel(request, response, SupplierAccount.class);
    }

}
