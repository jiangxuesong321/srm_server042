package org.cmoc.modules.srm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.cmoc.common.api.vo.Result;
import org.cmoc.common.aspect.annotation.AutoLog;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.common.system.query.QueryGenerator;
import org.cmoc.common.system.vo.LoginUser;
import org.cmoc.common.util.oConvertUtils;
import org.cmoc.modules.srm.entity.ApproveSetting;
import org.cmoc.modules.srm.entity.PurchaseRequestDetailApprove;
import org.cmoc.modules.srm.entity.PurchaseRequestMainApprove;
import org.cmoc.modules.srm.service.IApproveSettingService;
import org.cmoc.modules.srm.service.IPurchaseRequestDetailApproveService;
import org.cmoc.modules.srm.service.IPurchaseRequestMainApproveService;
import org.cmoc.modules.srm.vo.PurchaseRequestMainApprovePage;
import org.cmoc.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @Description: purchase_request_main
* @Author: jeecg-boot
* @Date:   2022-06-17
* @Version: V1.0
*/
@RestController
@RequestMapping("/srm/purchaseRequestMainApprove")
@Slf4j
public class PurchaseRequestMainApproveController {
   @Autowired
   private IPurchaseRequestMainApproveService purchaseRequestMainApproveService;
   @Autowired
   private IPurchaseRequestDetailApproveService purchaseRequestDetailApproveService;
   @Autowired
   private IApproveSettingService iApproveSettingService;
   @Autowired
   private ISysUserService iSysUserService;

   /**
    * 分页列表查询
    *
    * @param purchaseRequestMain
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "purchase_request_main-分页列表查询")
   @ApiOperation(value="purchase_request_main-分页列表查询", notes="purchase_request_main-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<PurchaseRequestMainApprove>> queryPageList(PurchaseRequestMainApprove purchaseRequestMain,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       //审核人 权限
       QueryWrapper<PurchaseRequestMainApprove> queryWrapper = QueryGenerator.initQueryWrapper(purchaseRequestMain, req.getParameterMap());

       //判断当前用户权限
       LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       String username = loginUser.getUsername();
       String deptId = loginUser.getDepartIds();

       String permission = iSysUserService.fetchPermission(username);
       String auth = "owner";
       String auther = username;
       if(StringUtils.isNotEmpty(permission)){
           if(permission.contains("pur:all")){
               auth = "all";
           }else if(permission.contains("pur:subject")){
               //当前用户属于那个主体
               String subject = loginUser.getRelTenantIds();
               queryWrapper.lambda().inSql(PurchaseRequestMainApprove :: getProjectId,
                       "select pb.id from proj_base pb where find_in_set(pb.subject,'" + subject + "')  ");
           }else if(permission.contains("pur:dept")){
               queryWrapper.lambda().eq(PurchaseRequestMainApprove :: getReqOrgId,auther);
           }else{
               queryWrapper.lambda().eq(PurchaseRequestMainApprove :: getCreateUser,username);
           }
       }

       Page<PurchaseRequestMainApprove> page = new Page<PurchaseRequestMainApprove>(pageNo, pageSize);
       IPage<PurchaseRequestMainApprove> pageList = purchaseRequestMainApproveService.page(page, queryWrapper);
       return Result.OK(pageList);
   }
   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   //@AutoLog(value = "purchase_request_main-通过id查询")
   @ApiOperation(value="purchase_request_main-通过id查询", notes="purchase_request_main-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<PurchaseRequestMainApprove> queryById(@RequestParam(name="id",required=true) String id) {
       PurchaseRequestMainApprove purchaseRequestMain = purchaseRequestMainApproveService.getById(id);
       if(purchaseRequestMain==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(purchaseRequestMain);
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   //@AutoLog(value = "purchase_request_detail通过主表ID查询")
   @ApiOperation(value="purchase_request_detail主表ID查询", notes="purchase_request_detail-通主表ID查询")
   @GetMapping(value = "/queryPurchaseRequestDetailByMainId")
   public List<PurchaseRequestDetailApprove> queryPurchaseRequestDetailListByMainId(@RequestParam(name="id",required=true) String id) {
       List<PurchaseRequestDetailApprove> purchaseRequestDetailList = purchaseRequestDetailApproveService.selectByMainId(id);
       return purchaseRequestDetailList;
   }
}
