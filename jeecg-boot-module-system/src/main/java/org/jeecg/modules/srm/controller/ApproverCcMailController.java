package org.jeecg.modules.srm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.srm.entity.ApproverCcMail;
import org.jeecg.modules.srm.service.IApproverCcMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: approver_cc_mail
 * @Author: jeecg-boot
 * @Date:   2023-02-20
 * @Version: V1.0
 */
@Api(tags="approver_cc_mail")
@RestController
@RequestMapping("/srm/approverCcMail")
@Slf4j
public class ApproverCcMailController extends JeecgController<ApproverCcMail, IApproverCcMailService> {
	@Autowired
	private IApproverCcMailService approverCcMailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param approverCcMail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "approver_cc_mail-分页列表查询")
	@ApiOperation(value="approver_cc_mail-分页列表查询", notes="approver_cc_mail-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ApproverCcMail>> queryPageList(ApproverCcMail approverCcMail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ApproverCcMail> queryWrapper = QueryGenerator.initQueryWrapper(approverCcMail, req.getParameterMap());
		Page<ApproverCcMail> page = new Page<ApproverCcMail>(pageNo, pageSize);
		IPage<ApproverCcMail> pageList = approverCcMailService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param approverCcMail
	 * @return
	 */
	@AutoLog(value = "approver_cc_mail-添加")
	@ApiOperation(value="approver_cc_mail-添加", notes="approver_cc_mail-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ApproverCcMail approverCcMail) {
		/*if(StringUtils.isNotBlank(approverCcMail.getApprover())){
			//校验是否已经存在有效的配置信息
			long count = approverCcMailService.count(Wrappers.<ApproverCcMail>query().lambda()
					*//*.eq(ApproverCcMail::getApprover, approverCcMail.getApprover())*//*
					.eq(ApproverCcMail::getDelFlag, CommonConstant.STATUS_0));
			if (count>0){
				return Result.error("存在已经配置数据，请直接编辑！");
			}
		}*/

		//校验是否已经存在有效的配置信息
		long count = approverCcMailService.count(Wrappers.<ApproverCcMail>query().lambda()
				/*.eq(ApproverCcMail::getApprover, approverCcMail.getApprover())*/
				.eq(ApproverCcMail::getDelFlag, CommonConstant.STATUS_0));
		if (count>0){
			return Result.error("存在已经配置数据，请直接编辑！");
		}

		approverCcMailService.save(approverCcMail);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param approverCcMail
	 * @return
	 */
	@AutoLog(value = "approver_cc_mail-编辑")
	@ApiOperation(value="approver_cc_mail-编辑", notes="approver_cc_mail-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ApproverCcMail approverCcMail) {
		approverCcMailService.updateById(approverCcMail);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "approver_cc_mail-通过id删除")
	@ApiOperation(value="approver_cc_mail-通过id删除", notes="approver_cc_mail-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		approverCcMailService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "approver_cc_mail-批量删除")
	@ApiOperation(value="approver_cc_mail-批量删除", notes="approver_cc_mail-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.approverCcMailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "approver_cc_mail-通过id查询")
	@ApiOperation(value="approver_cc_mail-通过id查询", notes="approver_cc_mail-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ApproverCcMail> queryById(@RequestParam(name="id",required=true) String id) {
		ApproverCcMail approverCcMail = approverCcMailService.getById(id);
		if(approverCcMail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(approverCcMail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param approverCcMail
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ApproverCcMail approverCcMail) {
        return super.exportXls(request, approverCcMail, ApproverCcMail.class, "approver_cc_mail");
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
        return super.importExcel(request, response, ApproverCcMail.class);
    }

}
