package org.jeecg.modules.srm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.srm.entity.ApproveSetting;
import org.jeecg.modules.srm.entity.ApproveSetting;
import org.jeecg.modules.srm.entity.PurchaseRequestMainApprove;
import org.jeecg.modules.srm.service.IApproveSettingService;
import org.jeecg.modules.srm.service.IAttachmentFileRecordService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* @Description: 附件记录表
* @Author: jeecg-boot
* @Date:   2022-06-18
* @Version: V1.0
*/
@RestController
@RequestMapping("/srm/approveSetting")
@Slf4j
public class ApproveSettingController extends JeecgController<ApproveSetting, IApproveSettingService> {
   @Autowired
   private IApproveSettingService iApproveSettingService;
   @Autowired
   private ISysUserService iSysUserService;
   
   /**
    * 分页列表查询
    *
    * @param approveSetting
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "附件记录表-分页列表查询")
   @ApiOperation(value="附件记录表-分页列表查询", notes="附件记录表-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<ApproveSetting>> queryPageList(ApproveSetting approveSetting,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
//       QueryWrapper<ApproveSetting> queryWrapper = QueryGenerator.initQueryWrapper(approveSetting, req.getParameterMap());
//       queryWrapper.lambda().eq(ApproveSetting :: getDelFlag, CommonConstant.DEL_FLAG_0);
       Page<ApproveSetting> page = new Page<ApproveSetting>(pageNo, pageSize);
       IPage<ApproveSetting> pageList = iApproveSettingService.queryPageList(page, approveSetting);
       return Result.OK(pageList);
   }
   
   /**
    *   添加
    *
    * @param approveSetting
    * @return
    */
   @AutoLog(value = "附件记录表-添加")
   @ApiOperation(value="附件记录表-添加", notes="附件记录表-添加")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody ApproveSetting approveSetting) {
       LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       String username = loginUser.getUsername();

       approveSetting.setUpdateTime(new Date());
       approveSetting.setUpdateBy(username);
       iApproveSettingService.saveEntity(approveSetting);
//       iApproveSettingService.save(approveSetting);
       return Result.OK("添加成功！");
   }
   
   /**
    *  编辑
    *
    * @param approveSetting
    * @return
    */
   @AutoLog(value = "附件记录表-编辑")
   @ApiOperation(value="附件记录表-编辑", notes="附件记录表-编辑")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody ApproveSetting approveSetting) {
       LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       String username = loginUser.getUsername();

       approveSetting.setUpdateTime(new Date());
       approveSetting.setUpdateBy(username);
       iApproveSettingService.updateById(approveSetting);
       return Result.OK("编辑成功!");
   }
   
   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "附件记录表-通过id删除")
   @ApiOperation(value="附件记录表-通过id删除", notes="附件记录表-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<String> delete(@RequestParam(name="id",required=true) String id) {
       LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       String username = loginUser.getUsername();

       ApproveSetting setting = iApproveSettingService.getById(id);
       setting.setDelFlag(CommonConstant.HAS_READ_FLAG);
       setting.setUpdateTime(new Date());
       setting.setUpdateBy(username);
       iApproveSettingService.updateById(setting);
       return Result.OK("删除成功!");
   }
   
   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "附件记录表-批量删除")
   @ApiOperation(value="附件记录表-批量删除", notes="附件记录表-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.iApproveSettingService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }
   
   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   //@AutoLog(value = "附件记录表-通过id查询")
   @ApiOperation(value="附件记录表-通过id查询", notes="附件记录表-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<ApproveSetting> queryById(@RequestParam(name="id",required=true) String id) {
       ApproveSetting approveSetting = iApproveSettingService.getById(id);
       if(approveSetting==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(approveSetting);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param approveSetting
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, ApproveSetting approveSetting) {
       return super.exportXls(request, approveSetting, ApproveSetting.class, "附件记录表");
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
       return super.importExcel(request, response, ApproveSetting.class);
   }

    /**
     * 分页列表查询
     *
     * @param approveSetting
     * @param req
     * @return
     */
    @ApiOperation(value="附件记录表-分页列表查询", notes="附件记录表-分页列表查询")
    @GetMapping(value = "/checkApprove")
    public Result<?> queryPageList(ApproveSetting approveSetting,HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        List<ApproveSetting> setList = iApproveSettingService.list(Wrappers.<ApproveSetting>query().lambda().
                eq(ApproveSetting :: getDelFlag,CommonConstant.DEL_FLAG_0).
                eq(ApproveSetting :: getUsername,username).
                eq(ApproveSetting :: getType,approveSetting.getType()));
        if(setList == null || setList.size() == 0){
            return Result.OK(false);
        }else{
            return Result.OK(true);
        }
    }

    /**
     * 获取流程发起人
     *
     * @param approveSetting
     * @param req
     * @return
     */
    @ApiOperation(value="附件记录表-分页列表查询", notes="附件记录表-分页列表查询")
    @GetMapping(value = "/fetchProcessUser")
    public Result<?> fetchProcessUser(ApproveSetting approveSetting,HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<ApproveSetting> setList = iApproveSettingService.list(Wrappers.<ApproveSetting>query().lambda().
                eq(ApproveSetting :: getDelFlag,CommonConstant.DEL_FLAG_0).
                eq(ApproveSetting :: getCompany,approveSetting.getCompany()).
                eq(ApproveSetting :: getType,approveSetting.getType()));
        if(setList == null || setList.size() == 0){
            return Result.OK(false);
        }else{
            //获取第一条
            ApproveSetting ast = setList.get(0);
            SysUser sysUser = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,ast.getUsername()));
            return Result.OK(sysUser);
        }
    }
}
