package com.cmoc.modules.srm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.constant.CommonConstant;
import com.cmoc.common.system.vo.DictModel;
import com.cmoc.common.system.vo.LoginUser;
import com.cmoc.common.util.FillRuleUtil;
import com.cmoc.common.util.ObjectCheck;
import com.cmoc.modules.srm.entity.*;
import com.cmoc.modules.srm.mapper.ProjBaseMapper;
import com.cmoc.modules.srm.service.*;
import com.cmoc.modules.srm.utils.ExportUtil;
import com.cmoc.modules.srm.vo.*;
import com.cmoc.modules.system.entity.SysDepart;
import com.cmoc.modules.system.entity.SysUser;
import com.cmoc.modules.system.service.ISysDepartService;
import com.cmoc.modules.system.service.ISysDictService;
import com.cmoc.modules.system.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: proj_base
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Service
public class ProjBaseServiceImpl extends ServiceImpl<ProjBaseMapper, ProjBase> implements IProjBaseService {
    @Autowired
    private IProjectBomRelationService iProjectBomRelationService;
    @Autowired
    private IProjectBomChildService iProjectBomChildService;
    @Autowired
    private IProjectCategoryService iProjectCategoryService;
    @Autowired
    private IBasMaterialService iBasMaterialService;
    @Autowired
    private IProjectExchangeRateService iProjectExchangeRateService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ISysDepartService iSysDepartService;
    @Autowired
    private IProjChangeService iProjChangeService;
    @Autowired
    private ISysDictService iSysDictService;
    @Value("${jeecg.path.upload}")
    private String upLoadPath;
    @Autowired
    private IProjectCategoryPayService iProjectCategoryPayService;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * OA 项目变更通知
     * @param projBaseVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateProj(ProjBaseVo projBaseVo) {
        String projectId = projBaseVo.getId();
        String username = projBaseVo.getUpdateUser();
        Date nowTime = new Date();
        Result result = new Result();

        ProjBase projBase = this.getById(projectId);
        if(projBase == null){
            result.setCode(500);
            result.setMessage("提交失败,系统不存在该项目");
            result.setResult(null);
            result.setSuccess(false);
            return result;
        }
//        BeanUtils.copyProperties(projBaseVo,projBase);
        projBase.setBudgetAmount(projBaseVo.getBudgetAmount());
        projBase.setProjBackground(projBaseVo.getProjBackground());
        projBase.setUpdateUser(projBaseVo.getUpdateUser());
        projBase.setUpdateTime(nowTime);

        this.updateById(projBase);

        result.setCode(200);
        result.setMessage("提交成功");
        result.setResult(projectId);
        result.setSuccess(true);
        return result;
    }

    /**
     * 获取项目下得子项
     * @param projBase
     * @return
     */
    @Override
    public List<Map<String, String>> fetchModelByProjId(ProjBase projBase) {
        return baseMapper.fetchModelByProjId(projBase);
    }

    /**
     * 项目数量统计
     * @param projBase
     * @return
     */
    @Override
    public Map<String, BigDecimal> fetchProjNum(ProjBase projBase) {
        Map<String,BigDecimal> map = new HashMap<>();
        map.put("projNum",BigDecimal.ZERO);
        map.put("projTypeNum",BigDecimal.ZERO);
        map.put("projAreaNum",BigDecimal.ZERO);

        QueryWrapper<ProjBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProjBase :: getDelFlag,CommonConstant.DEL_FLAG_0);
        if(StringUtils.isNotEmpty(projBase.getId())){
            queryWrapper.lambda().eq(ProjBase :: getId,projBase.getId());
        }
        if(StringUtils.isNotEmpty(projBase.getSubject())){
            queryWrapper.lambda().eq(ProjBase :: getSubject,projBase.getSubject());
        }

        List<ProjBase> projBases = this.list(queryWrapper);
        if(projBases != null && projBases.size() > 0){
            map.put("projNum",new BigDecimal(projBases.size()));

            List<ProjBase> areaList = new ArrayList<>();
            List<ProjBase> typeList = new ArrayList<>();
            for(ProjBase pb : projBases){
                if(StringUtils.isNotEmpty(pb.getSubject())){
                    areaList.add(pb);
                }
                if(StringUtils.isNotEmpty(pb.getProjType())){
                    typeList.add(pb);
                }
            }

            //地区统计
            Map<String,List<ProjBase>> areaMap = areaList.stream().collect(Collectors.groupingBy(ProjBase :: getSubject));
            if(areaMap != null){
                map.put("projAreaNum",new BigDecimal(areaMap.size()));
            }
            //类型
            Map<String,List<ProjBase>> typeMap = typeList.stream().collect(Collectors.groupingBy(ProjBase :: getProjType));
            if(typeMap != null){
                map.put("projTypeNum",new BigDecimal(typeMap.size()));
            }
        }
        return map;
    }

    /**
     * 项目类型统计
     * @param projBase
     * @return
     */
    @Override
    public List<Map<String,Object>> fetchProjType(ProjBase projBase) {
        return baseMapper.fetchProjType(projBase);
    }

    /**
     * 项目地区
     * @param projBase
     * @return
     */
    @Override
    public List<Map<String, Object>> fetchProjArea(ProjBase projBase) {
        return baseMapper.fetchProjArea(projBase);
    }

    /**
     * 设备模板导出
     * @param request
     * @param projBase
     * @param clazz
     * @param title
     * @return
     */
    @Override
    public void exportXls(HttpServletRequest request, ProjBase projBase, Class<BasMaterialImport> clazz, String title, HttpServletResponse response) throws IOException {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // Step.2 获取导出数据
        List<BasMaterialImport> exportList = baseMapper.exportProdList(projBase);
        List<BasMaterial> bmList = iBasMaterialService.list(Wrappers.<BasMaterial>query().lambda().eq(BasMaterial :: getIsEnabled,"1"));

        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title",getExportParams("设备模板"));//表格title
        map.put("entity",clazz);//表格对应实体
        map.put("data", exportList);
        listMap.add(map);

        List<BasMaterialExport> bmExportList = new ArrayList<>();
        for(BasMaterial bm : bmList){
            BasMaterialExport be = new BasMaterialExport();
            BeanUtils.copyProperties(bm,be);
            bmExportList.add(be);
        }
        map = new HashMap<String, Object>();
        map.put("title",getExportParams("设备列表"));//表格title
        map.put("entity",BasMaterialExport.class);//表格对应实体
        map.put("data", bmExportList);
        listMap.add(map);

        Workbook wb = ExcelExportUtil.exportExcel(listMap,ExcelType.XSSF);

        try {
            ExportUtil.insertWaterMarkTextToXlsx((XSSFWorkbook)wb,sysUser.getRealname() + "(" + sysUser.getUsername() + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServletOutputStream out = response.getOutputStream();
        wb.write(out);
        out.flush();
        out.close();
    }

    /**
     * 项目列表导出
     * @param projBase
     * @return
     */
    @Override
    public List<ProjBase> exportXlsByList(ProjBase projBase) {
        return baseMapper.exportXlsByList(projBase);
    }

    /**
     * 项目总投
     * @param projBase
     * @return
     */
    @Override
    public List<Map<String, Object>> fetchProjectAmountByType(ProjBase projBase) {
        return baseMapper.fetchProjectAmountByType(projBase);
    }

    /**
     * 项目主体统计
     * @param projBase
     * @return
     */
    @Override
    public List<Map<String, Object>> fetchProjAmountBySubject(ProjBase projBase) {
        return baseMapper.fetchProjAmountBySubject(projBase);
    }

    /**
     * 子项产能汇总
     * @param projBase
     * @return
     */
    @Override
    public List<ProjectBomChild> fetchModelBySubject(ProjBase projBase) {
        return baseMapper.fetchModelBySubject(projBase);
    }

    //导出参数
    public static ExportParams getExportParams(String name) {
        //表格名称,sheet名称,导出版本
        return  new ExportParams(name,name,ExcelType.XSSF);
    }



    /**
     * 编辑
     * @param projBase
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editProjBase(ProjBase projBase) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        Date nowTime = new Date();

        projBase.setUpdateTime(nowTime);
        projBase.setUpdateUser(username);


        String projectId = projBase.getId();
        //比较变更项
        JSONObject formData = new JSONObject();
        formData.put("prefix", "UPD");
        String code = (String) FillRuleUtil.executeRule("version_no", formData);

        ProjBase oldProj = this.getById(projBase.getId());
        List<ProjChange> changeList = new ArrayList<>();
        //基本信息、资金分类单独编辑
        if("0".equals(projBase.getTab())){
            //汇率
            List<ProjectExchangeRate> rateList = projBase.getRateList();
            for(ProjectExchangeRate rate : rateList){
                rate.setProjectId(projectId);
                rate.setUpdateTime(nowTime);
                rate.setCreateTime(nowTime);
                rate.setUpdateUser(username);
                rate.setCreateUser(username);
                rate.setDelFlag(CommonConstant.NO_READ_FLAG);
            }

            String content = "";
            if(!projBase.getProjName().equals(oldProj.getProjName())){
                content = content + "项目名称变更:由" + oldProj.getProjName() + "变更成" + projBase.getProjName() + ";";
            }

            if(!projBase.getSubject().equals(oldProj.getSubject())){
                SysDepart dept1 = iSysDepartService.getById(oldProj.getSubject());
                SysDepart dept2 = iSysDepartService.getById(projBase.getSubject());
                content = content + "项目主体变更:由" + dept1 + "变更成" + dept2 + ";";
            }

            if(!projBase.getProjType().equals(oldProj.getProjType())){
                Map<String,String> map = new HashMap<>();
                try {
                    List<DictModel> ls = iSysDictService.getDictItems("proj_type");
                    map = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));
                } catch (Exception ex) {
                    log.error("查询数据字典:项目类型出错" + ex.getMessage());
                }
                String type1 = null;
                if(StringUtils.isNotEmpty(oldProj.getProjType())){
                    type1 = map.get(oldProj.getProjType());
                }
                String type2 = "";
                if(StringUtils.isNotEmpty(projBase.getProjType())){
                    type2 = map.get(projBase.getProjType());
                }
                content = content + "项目类型变更:由" + type1 + "变更成" + type2 + ";";
            }
            if(oldProj.getProjInitiationDate() == null){
                String date2 = sdf.format(projBase.getProjInitiationDate());
                content = content + "项目立项日期变更:由空" + "变更成" + date2 + ";";
            }else{
                String date1 = sdf.format(oldProj.getProjInitiationDate());
                String date2 = sdf.format(projBase.getProjInitiationDate());
                if(!date1.equals(date2)){
                    content = content + "项目立项日期变更:由" + date1 + "变更成" + date2 + ";";
                }
            }
            if(StringUtils.isNotEmpty(oldProj.getApplyUserId())){
                if(!projBase.getApplyUserId().equals(oldProj.getApplyUserId())){
                    SysUser user1 = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,oldProj.getApplyUserId()));
                    SysUser user2 = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,projBase.getApplyUserId()));
                    content = content + "项目申请人变更:由" + user1.getRealname() + "变更成" + user2.getRealname() + ";";
                }
            }else{
                SysUser user2 = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,projBase.getApplyUserId()));
                content = content + "项目申请人变更:由空" + "变更成" + user2.getRealname() + ";";
            }

            if(StringUtils.isEmpty(oldProj.getApplyOrgId()) || StringUtils.isEmpty(projBase.getApplyOrgId())){
                if(StringUtils.isEmpty(oldProj.getApplyOrgId()) && StringUtils.isNotEmpty(projBase.getApplyOrgId())){
                    SysDepart dept2 = iSysDepartService.getById(projBase.getApplyOrgId());
                    content = content + "项目申请人部门变更:由空"  + "变更成" + dept2.getDepartName() + ";";
                }else if(StringUtils.isNotEmpty(oldProj.getApplyOrgId()) && StringUtils.isEmpty(projBase.getApplyOrgId())){
                    SysDepart dept1 = iSysDepartService.getById(oldProj.getApplyOrgId());
                    content = content + "项目申请人部门变更:由" + dept1.getDepartName() + "变更成空" + ";";
                }
            }else if(!oldProj.getApplyOrgId().equals(projBase.getApplyOrgId())){
                SysDepart dept1 = iSysDepartService.getById(oldProj.getApplyOrgId());
                SysDepart dept2 = iSysDepartService.getById(projBase.getApplyOrgId());
                content = content + "项目申请人部门变更:由" + dept1.getDepartName() + "变更成" + dept2.getDepartName() + ";";
            }

            if(StringUtils.isEmpty(oldProj.getProjBackground()) && StringUtils.isNotEmpty(projBase.getProjBackground())){
                content = content + "项目背景变更:由" + oldProj.getProjBackground() + "变更成" + projBase.getProjBackground() + ";";
            }
            else if(StringUtils.isNotEmpty(oldProj.getProjBackground()) && StringUtils.isEmpty(projBase.getProjBackground())){
                content = content + "项目背景变更:由" + oldProj.getProjBackground() + "变更成" + projBase.getProjBackground() + ";";
            }
            else if(StringUtils.isNotEmpty(oldProj.getProjBackground())
                    && StringUtils.isNotEmpty(projBase.getProjBackground())
                    && !oldProj.getProjBackground().equals(projBase.getProjBackground())){
                content = content + "项目背景变更:由" + oldProj.getProjBackground() + "变更成" + projBase.getProjBackground() + ";";
            }

            if(StringUtils.isNotEmpty(content)){
                ProjChange change = new ProjChange();
                change.setId(String.valueOf(IdWorker.getId()));
                change.setProjectId(projBase.getId());
                change.setDelFlag(CommonConstant.NO_READ_FLAG);
                change.setCreateTime(nowTime);
                change.setCreateUser(username);
                change.setUpdateTime(nowTime);
                change.setUpdateUser(username);
                content = content.replace("null","空");
                change.setContent(content);
                change.setType("项目基本信息变更");
                change.setSort(code);
                changeList.add(change);
            }

            BigDecimal changeAmount = null;
            content = "";
            if(projBase.getProjAmount().compareTo(oldProj.getProjAmount()) != 0){
                content = content + "项目立项金额变更:由" + oldProj.getProjAmount() + "变更成" + projBase.getProjAmount() + ";";
                changeAmount = oldProj.getProjAmount().subtract(projBase.getProjAmount());
            }
            if(StringUtils.isNotEmpty(content)){
                ProjChange change = new ProjChange();
                change.setId(String.valueOf(IdWorker.getId()));
                change.setProjectId(projBase.getId());
                change.setDelFlag(CommonConstant.NO_READ_FLAG);
                change.setCreateTime(nowTime);
                change.setCreateUser(username);
                change.setUpdateTime(nowTime);
                change.setUpdateUser(username);
                content = content.replace("null","空");
                change.setContent(content);
                change.setType("立项金额变更");
                change.setSort(code);
                if(changeAmount.compareTo(BigDecimal.ZERO) == 1){
                    change.setChangeAmount("-" + changeAmount);
                }else if(BigDecimal.ZERO.compareTo(changeAmount) == 1){
                    change.setChangeAmount("+" + BigDecimal.ZERO.subtract(changeAmount));
                }
                changeList.add(change);
            }

            changeAmount = null;
            content = "";
            if(projBase.getBudgetAmount().compareTo(oldProj.getBudgetAmount()) != 0){
                content = content + "项目预算金额变更:由" + oldProj.getBudgetAmount() + "变更成" + projBase.getBudgetAmount() + ";";
                changeAmount = oldProj.getBudgetAmount().subtract(projBase.getBudgetAmount());
            }
            if(StringUtils.isNotEmpty(content)){
                ProjChange change = new ProjChange();
                change.setId(String.valueOf(IdWorker.getId()));
                change.setProjectId(projBase.getId());
                change.setDelFlag(CommonConstant.NO_READ_FLAG);
                change.setCreateTime(nowTime);
                change.setCreateUser(username);
                change.setUpdateTime(nowTime);
                change.setUpdateUser(username);
                content = content.replace("null","空");
                change.setContent(content);
                change.setType("预算金额变更");
                change.setSort(code);
                if(changeAmount.compareTo(BigDecimal.ZERO) == 1){
                    change.setChangeAmount("-" + changeAmount);
                }else if(BigDecimal.ZERO.compareTo(changeAmount) == 1){
                    change.setChangeAmount("+" + BigDecimal.ZERO.subtract(changeAmount));
                }
                changeList.add(change);
            }

            content = "";
            List<ProjectExchangeRate> oldRateList = iProjectExchangeRateService.list(Wrappers.<ProjectExchangeRate>query().lambda().
                    eq(ProjectExchangeRate :: getProjectId,projectId).
                    eq(ProjectExchangeRate :: getDelFlag,CommonConstant.DEL_FLAG_0));

            if((oldRateList == null || oldRateList.size() == 0) && (rateList != null && rateList.size() > 0)){
                for(ProjectExchangeRate nw : rateList){
                    content = content + "新增项目汇率:RMB" + "与" + nw.getCurrencyB() + "的换算;";
                }
            }else if((oldRateList != null && oldRateList.size() > 0) && (rateList == null || rateList.size() == 0)){
                for(ProjectExchangeRate old : oldRateList){
                    content = content + "删除项目汇率:RMB" + "与" + old.getCurrencyB() + "的换算;";
                }
            }else if(oldRateList != null && oldRateList.size() > 0 && rateList != null && rateList.size() > 0){
                //新增
                for(ProjectExchangeRate nw : rateList){
                    if(StringUtils.isEmpty(nw.getId())){
                        content = content + "新增项目汇率:RMB" + "与" + nw.getCurrencyB() + "的换算;";
                    }
                }
                //编辑或删除
                for(ProjectExchangeRate old : oldRateList){
                    Boolean flag = false;
                    for(ProjectExchangeRate nw : rateList){
                        if(old.getId().equals(nw.getId())){
                            flag = true;
                            if(!old.getCurrencyB().equals(nw.getCurrencyB())){
                                content = content + "项目汇率币种变更:目标币种由" + old.getCurrencyB() + "变更成" + nw.getCurrencyB() + ";" ;
                            }
                            if(old.getValueB().compareTo(nw.getValueB()) != 0){
                                content = content + "项目汇率换算率变更:RMB" + "与" + old.getCurrencyB() + "换算率由" + old.getValueB() + "变更成" + nw.getValueB() + ";";
                            }
                            break;
                        }
                    }
                    if(!flag){
                        content = content + "删除项目汇率:RMB" + "与" + old.getCurrencyB() + "的换算;";
                    }
                }
            }
            if(StringUtils.isNotEmpty(content)){
                ProjChange change = new ProjChange();
                change.setId(String.valueOf(IdWorker.getId()));
                change.setProjectId(projBase.getId());
                change.setDelFlag(CommonConstant.NO_READ_FLAG);
                change.setCreateTime(nowTime);
                change.setCreateUser(username);
                change.setUpdateTime(nowTime);
                change.setUpdateUser(username);
                content = content.replace("null","空");
                change.setContent(content);
                change.setType("项目汇率变更");
                change.setSort(code);
                changeList.add(change);
            }

            //删除汇率
            iProjectExchangeRateService.remove(Wrappers.<ProjectExchangeRate>query().lambda().eq(ProjectExchangeRate :: getProjectId,projectId));

            iProjectExchangeRateService.saveOrUpdateBatch(rateList);
        }
        else{

            //分类
            List<ProjectCategory> categoryList = projBase.getCategoryList();
            List<ProjectCategory> pcList = new ArrayList<>();
            List<ProjectBomRelation> bomList = new ArrayList<>();
            //预算总额
            BigDecimal budgetAmount = BigDecimal.ZERO;

            //递归分类
            extracted(username, nowTime, projectId, categoryList, pcList,"0");
            for(ProjectCategory pc : pcList){
                if(pc.getLevel() == 0){
                    budgetAmount = budgetAmount.add(pc.getBudgetAmount());
                }
                if(pc.getIsEqp() == 1 && pc.getIsLast() == 1){
                    List<ProjectBomRelation> pbList = pc.getProdList();
                    if(pbList != null && pbList.size() > 0){
                        for(ProjectBomRelation pb : pbList){
                            pb.setDelFlag(CommonConstant.NO_READ_FLAG);
                            pb.setUpdateUser(username);
                            pb.setUpdateTime(nowTime);
                            pb.setCreateUser(username);
                            pb.setCreateTime(nowTime);
                            pb.setProjId(projectId);
                            pb.setCategoryId(pc.getId());
                            bomList.add(pb);
                        }
                    }
                }
            }
//            if(oldProj.getBudgetAmount().compareTo(budgetAmount) != 0){
//                throw new Exception("原预算金额与资金分类金额累加不一致");
//            }
            //判断预算金额 与 资金分类金额 一致
            projBase.setBudgetAmount(budgetAmount);

            //项目子项
            BigDecimal newAmount = BigDecimal.ZERO;
            BigDecimal oldAmount = BigDecimal.ZERO;

            List<ProjectBomChild> existList = projBase.getChildList();
            List<ProjectBomChild> childList = new ArrayList<>();
            if(existList != null){
                for(ProjectBomChild pb : existList){
                    if(StringUtils.isNotEmpty(pb.getModel())){
                        pb.setUpdateTime(nowTime);
                        pb.setCreateTime(nowTime);
                        pb.setUpdateUser(username);
                        pb.setCreateUser(username);
                        pb.setDelFlag(CommonConstant.NO_READ_FLAG);
                        pb.setProjectId(projectId);
                        childList.add(pb);
                    }
                    newAmount = newAmount.add(pb.getBudgetAmount());
                }
            }

            String content = "";
            List<ProjectBomChild> oldChildList = iProjectBomChildService.list(Wrappers.<ProjectBomChild>query().lambda().
                    eq(ProjectBomChild :: getProjectId,projectId).
                    eq(ProjectBomChild :: getDelFlag,CommonConstant.DEL_FLAG_0));


            //产品类别
            Map<String,String> map = new HashMap<>();
            try {
                List<DictModel> ls = iSysDictService.getDictItems("model");
                map = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));
            } catch (Exception ex) {
                log.error("查询数据字典:产品类别出错" + ex.getMessage());
            }

            if((oldChildList == null || oldChildList.size() == 0) && childList != null && childList.size() > 0){
                for(ProjectBomChild nw : childList){
                    String model = map.get(nw.getModel());
                    content = content + "新增项目子项:" + model + ";";
                }
            }else if((oldChildList != null && oldChildList.size() > 0) && (childList == null || childList.size() == 0)){
                oldAmount = BigDecimal.ZERO;
                for(ProjectBomChild old : oldChildList){
                    String model = map.get(old.getModel());
                    content = content + "删除项目子项:" + model + ";";
                    oldAmount = oldAmount.add(old.getBudgetAmount());
                }
            }else if(oldChildList != null && oldChildList.size() > 0 && childList != null && childList.size() > 0){
                oldAmount = BigDecimal.ZERO;

                for(ProjectBomChild nw :  childList){
                    Boolean flag = false;
                    for(ProjectBomChild old : oldChildList){
                        if(old.getModel().equals(nw.getModel())){
                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        String model = map.get(nw.getModel());
                        content = content + "新增项目子项:" + model + ";";
                    }
                }
                //编辑或删除
                for(ProjectBomChild old : oldChildList){
                    oldAmount = oldAmount.add(old.getBudgetAmount());
                    Boolean flag = false;
                    for(ProjectBomChild nw : childList){
                        if(old.getModel().equals(nw.getModel())){
                            String model1 = map.get(old.getModel());
                            String model2 = map.get(nw.getModel());
                            if(!old.getModel().equals(nw.getModel())){
                                content = content + "项目子项名称变更:由" + model1 + "变更成" + model2 +";";
                            }
                            if(old.getBudgetAmount().compareTo(nw.getBudgetAmount()) != 0){
                                content = content + "项目子项:" + model1 + "预算金额变更:由" + old.getBudgetAmount() + "变更成" + nw.getBudgetAmount() +";";
                            }
                            if(old.getIprogress() != null && nw.getIprogress() != null && old.getIprogress().compareTo(nw.getIprogress()) != 0){
                                content = content + "项目子项:" + model1 + "形象进度变更:由" + old.getIprogress() + "变更成" + nw.getIprogress() +";";
                            }

                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        String model = map.get(old.getModel());
                        content = content + "删除项目子项:" + model + ";";
                    }
                }
            }

            if(StringUtils.isNotEmpty(content)){
                ProjChange change = new ProjChange();
                change.setId(String.valueOf(IdWorker.getId()));
                change.setProjectId(projBase.getId());
                change.setDelFlag(CommonConstant.NO_READ_FLAG);
                change.setCreateTime(nowTime);
                change.setCreateUser(username);
                change.setUpdateTime(nowTime);
                change.setUpdateUser(username);
                content = content.replace("null","空");
                change.setContent(content);
                change.setSort(code);
                change.setType("项目子项变更");
                BigDecimal changeAmount = oldAmount.subtract(newAmount);
                if(changeAmount.compareTo(BigDecimal.ZERO) == 1){
                    change.setChangeAmount("-" + changeAmount);
                }else if(BigDecimal.ZERO.compareTo(changeAmount) == 1){
                    change.setChangeAmount("+" + BigDecimal.ZERO.subtract(changeAmount));
                }
                changeList.add(change);
            }

            content = "";

            newAmount = BigDecimal.ZERO;
            oldAmount = BigDecimal.ZERO;

            List<ProjectBomRelation> oldBomList = iProjectBomRelationService.list(Wrappers.<ProjectBomRelation>query().lambda().
                    eq(ProjectBomRelation :: getDelFlag,CommonConstant.DEL_FLAG_0).
                    eq(ProjectBomRelation :: getProjId,projectId));
            if((oldBomList == null || oldBomList.size() == 0) && (bomList != null && bomList.size() > 0)){
                newAmount = BigDecimal.ZERO;
                for(ProjectBomRelation nw : bomList){
                    content = content + "新增设备:" + nw.getName() + ";";

                    newAmount = newAmount.add(nw.getBudgetAmount());
                }
            }else if((oldBomList != null && oldBomList.size() > 0) && (bomList == null || bomList.size() == 0)){
                oldAmount = BigDecimal.ZERO;
                for(ProjectBomRelation old : oldBomList){
                    content = content + "删除设备:" + old.getName() + ";";

                    oldAmount = oldAmount.add(old.getBudgetAmount());
                }
            }else if(oldBomList != null && oldBomList.size() > 0 && bomList != null && bomList.size() > 0){
                //新增
//                for(ProjectBomRelation nw : bomList){
//                    if(StringUtils.isEmpty(nw.getId())){
//                        content = content + "新增设备:" + nw.getName() + ";";
//                    }
//                }
                newAmount = BigDecimal.ZERO;
                oldAmount = BigDecimal.ZERO;

                for(ProjectBomRelation nw : bomList){
                    newAmount = newAmount.add(nw.getBudgetAmount());

                    Boolean flag = false;
                    for(ProjectBomRelation old : oldBomList){
                        if(old.getId().equals(nw.getId())){
                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        content = content + "新增设备:" + nw.getName() + ";";
                    }
                }

                //编辑或删除
                for(ProjectBomRelation old : oldBomList){
                    oldAmount = oldAmount.add(old.getBudgetAmount());

                    Boolean flag = false;
                    for(ProjectBomRelation nw : bomList){
                        if(old.getId().equals(nw.getId())){
                            if(!old.getCapacity().equals(nw.getCapacity())){
                                content = content + "设备:"+old.getName()+",产能变更:由" + old.getCapacity() + "变更成" + nw.getCapacity() +";";
                            }
                            if(old.getQty().compareTo(nw.getQty()) != 0){
                                content = content + "设备:"+old.getName()+",预算数量变更:由" + old.getQty() + "变更成" + nw.getQty() +";";
                            }
                            if(old.getBudgetPrice().compareTo(nw.getBudgetPrice()) != 0){
                                content = content + "设备:"+old.getName()+",预算单价变更:由" + old.getBudgetPrice() + "变更成" + nw.getBudgetPrice() +";";
                            }
                            if(old.getBudgetAmount().compareTo(nw.getBudgetAmount()) != 0){
                                content = content + "设备:"+old.getName()+",预算金额变更:由" + old.getBudgetAmount() + "变更成" + nw.getBudgetAmount() +";";
                            }
                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        content = content + "删除设备:" + old.getName() + ";";
                    }
                }
            }
            if(StringUtils.isNotEmpty(content)){
                ProjChange change = new ProjChange();
                change.setId(String.valueOf(IdWorker.getId()));
                change.setProjectId(projBase.getId());
                change.setDelFlag(CommonConstant.NO_READ_FLAG);
                change.setCreateTime(nowTime);
                change.setCreateUser(username);
                change.setUpdateTime(nowTime);
                change.setUpdateUser(username);
                content = content.replace("null","空");
                change.setContent(content);
                change.setSort(code);
                change.setType("项目设备变更");
                BigDecimal changeAmount = oldAmount.subtract(newAmount);
                if(changeAmount.compareTo(BigDecimal.ZERO) == 1){
                    change.setChangeAmount("-" + changeAmount);
                }else if(BigDecimal.ZERO.compareTo(changeAmount) == 1){
                    change.setChangeAmount("+" + BigDecimal.ZERO.subtract(changeAmount));
                }
                changeList.add(change);
            }

            content = "";

            newAmount = BigDecimal.ZERO;
            oldAmount = BigDecimal.ZERO;

            List<ProjectCategory> oldCateList = iProjectCategoryService.list(Wrappers.<ProjectCategory>query().lambda().
                    eq(ProjectCategory :: getProjectId,projectId).
                    eq(ProjectCategory :: getDelFlag,CommonConstant.DEL_FLAG_0));

            if((oldCateList == null || oldCateList.size() == 0) && (pcList != null && pcList.size() > 0)){
                newAmount = BigDecimal.ZERO;

                for(ProjectCategory nw : pcList){
                    content = content + "新增费用分类:" + nw.getName() + ";";

                    newAmount = newAmount.add(nw.getBudgetAmount());
                }
            }else if((oldCateList != null && oldCateList.size() > 0) && (pcList == null || pcList.size() == 0)){
                oldAmount = BigDecimal.ZERO;

                for(ProjectCategory old : oldCateList){
                    content = content + "删除费用分类:" + old.getName() + ";";

                    oldAmount = oldAmount.add(old.getBudgetAmount());
                }
            }else if(oldCateList != null && oldCateList.size() > 0 && pcList != null && pcList.size() > 0){
                newAmount = BigDecimal.ZERO;
                oldAmount = BigDecimal.ZERO;

                //新增
                for(ProjectCategory nw : pcList){
                    newAmount = newAmount.add(nw.getBudgetAmount());

                    Boolean flag = false;
                    for(ProjectCategory old : oldCateList){
                        if(nw.getId().equals(old.getId())){
                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        content = content + "新增费用分类:" + nw.getName() + ";";
                    }
                }
                //编辑或删除
                for(ProjectCategory old : oldCateList){

                    oldAmount = oldAmount.add(old.getBudgetAmount());

                    Boolean flag = false;
                    for(ProjectCategory nw : pcList){
                        if(old.getId().equals(nw.getId())){
                            if(!old.getName().equals(nw.getName())){
                                content = content + "费用分类名称变更:由" + old.getName() + "变更成" + nw.getName() +";";
                            }
                            if(old.getBudgetAmount().compareTo(nw.getBudgetAmount()) != 0){
                                content = content + "费用分类:"+old.getName()+",预算费用变更:由" + old.getBudgetAmount() + "变更成" + nw.getBudgetAmount() +";";
                            }
                            if(old.getIsLast() != nw.getIsLast()){
                                content = content + "费用分类:"+old.getName()+",是否末级变更:由" + (old.getIsLast() == 1 ? "是" : "否") + "变更成" + (nw.getIsLast() == 1 ? "是" : "否") +";";
                            }
                            if(old.getIsEqp() != nw.getIsEqp()){
                                content = content + "费用分类:"+old.getName()+",是否是清单管理变更:由" + (old.getIsEqp() == 1 ? "是" : "否") + "变更成" + (old.getIsEqp() == 1 ? "是" : "否") +";";
                            }
                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        content = content + "删除费用分类:" + old.getName() + ";";
                    }
                }
            }
            if(StringUtils.isNotEmpty(content)){
                ProjChange change = new ProjChange();
                change.setId(String.valueOf(IdWorker.getId()));
                change.setProjectId(projBase.getId());
                change.setDelFlag(CommonConstant.NO_READ_FLAG);
                change.setCreateTime(nowTime);
                change.setCreateUser(username);
                change.setUpdateTime(nowTime);
                change.setUpdateUser(username);
                content = content.replace("null","空");
                change.setContent(content);
                change.setSort(code);
                change.setType("项目费用分类变更");
                BigDecimal changeAmount = oldAmount.subtract(newAmount);
                if(changeAmount.compareTo(BigDecimal.ZERO) == 1){
                    change.setChangeAmount("-" + changeAmount);
                }else if(BigDecimal.ZERO.compareTo(changeAmount) == 1){
                    change.setChangeAmount("+" + BigDecimal.ZERO.subtract(changeAmount));
                }

                changeList.add(change);
            }


            //删除分类
            UpdateWrapper<ProjectCategory> category = new UpdateWrapper<>();
            category.set("del_flag",CommonConstant.ACT_SYNC_1);
            category.eq("project_id",projectId);
            iProjectCategoryService.update(category);

            //删除子项
            UpdateWrapper<ProjectBomChild> child = new UpdateWrapper<>();
            child.set("del_flag",CommonConstant.ACT_SYNC_1);
            child.eq("project_id",projectId);
            iProjectBomChildService.update(child);

            //删除设备
            UpdateWrapper<ProjectBomRelation> bom = new UpdateWrapper<>();
            bom.set("del_flag",CommonConstant.ACT_SYNC_1);
            bom.eq("proj_id",projectId);
            iProjectBomRelationService.update(bom);

            iProjectCategoryService.saveOrUpdateBatch(pcList);
            iProjectBomRelationService.saveOrUpdateBatch(bomList);
            if(childList != null && childList.size() > 0){
                iProjectBomChildService.saveOrUpdateBatch(childList);
            }
            //删除分类维护的付款记录
            List<ProjectCategory> delList = iProjectCategoryService.list(Wrappers.<ProjectCategory>query().lambda().
                    eq(ProjectCategory :: getDelFlag,CommonConstant.ACT_SYNC_1).
                    eq(ProjectCategory :: getProjectId,projectId));
            if(delList != null && delList.size() > 0){
                List<String> ids = new ArrayList<>();
                for(ProjectCategory pc : delList){
                    ids.add(pc.getId());
                }
                UpdateWrapper<ProjectCategoryPay> updateWrapper = new UpdateWrapper();
                updateWrapper.eq("project_id",projectId);
                updateWrapper.in("category_id",ids);
                updateWrapper.set("del_flag","1");
                iProjectCategoryPayService.update(updateWrapper);
            }
        }
        if(changeList != null && changeList.size() > 0){
            iProjChangeService.saveBatch(changeList);

            projBase.setSort(projBase.getSort() + 1);
        }
        this.updateById(projBase);




    }

    /**
     * 递归树
     * @param username
     * @param nowTime
     * @param projectId
     * @param categoryList
     * @param pcList
     */
    private void extracted(String username, Date nowTime, String projectId, List<ProjectCategory> categoryList, List<ProjectCategory> pcList,String pid) {
        for(ProjectCategory pc : categoryList){
            String categoryId = String.valueOf(IdWorker.getId());
            if(StringUtils.isEmpty(pc.getId())){
                pc.setId(categoryId);
            }else{
                categoryId = pc.getId();
            }
            pc.setProjectId(projectId);
            pc.setCreateTime(nowTime);
            pc.setCreateUser(username);
            pc.setUpdateTime(nowTime);
            pc.setUpdateUser(username);
            pc.setDelFlag(CommonConstant.NO_READ_FLAG);
            pc.setParentId(pid);

            pcList.add(pc);

            List<ProjectCategory> children = pc.getChildren();
            if(children != null && children.size() > 0){
                this.extracted(username,nowTime,projectId,children,pcList,categoryId);
            }
        }
    }

    /**
     * 项目
     * @param projBase
     * @return
     */
    @Override
    public ProjBase fetchProjById(ProjBase projBase) {
        return baseMapper.fetchProjById(projBase);
    }

    /**
     * 分页
     * @param page
     * @param projBase
     * @return
     */
    @Override
    public IPage<ProjBase> pageList(Page<ProjBase> page, ProjBase projBase) {
        return baseMapper.pageList(page,projBase);
    }

    /**
     * OA 项目立项
     * @param projBaseVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createProj(ProjBaseVo projBaseVo) {
        Date nowTime = new Date();
        String username = projBaseVo.getCreateUser();
        if(StringUtils.isEmpty(username)){
            username = "admin";
        }

        JSONObject formData = new JSONObject();
        //根据主体生成编码
        if(StringUtils.isEmpty(projBaseVo.getSubject())){
            Result result = new Result();
            result.setCode(500);
            result.setMessage("提交失败,没有获取到主体");
            result.setResult(null);
            result.setSuccess(true);
            return result;
        }
        String subject = projBaseVo.getSubject();
        String prefix = null;
        if("1".equals(subject)){
            prefix = "YX";
        }else if("2".equals(subject)){
            prefix = "TJ";
        }else if("3".equals(subject)){
            prefix = "NM";
        }else if("4".equals(subject)){
            prefix = "XZ";
        }else if("6".equals(subject)){
            prefix = "ZH";
        }else{
            Result result = new Result();
            result.setCode(500);
            result.setMessage("提交失败,找不到对应的主体");
            result.setResult(null);
            result.setSuccess(true);
            return result;
        }
        formData.put("prefix", prefix);
        String code = (String) FillRuleUtil.executeRule("proj_code", formData);

        ProjBase projBase = new ProjBase();
        BeanUtils.copyProperties(projBaseVo,projBase);
        String projectId = projBaseVo.getId();
        if(StringUtils.isEmpty(projectId)){
            projectId = String.valueOf(IdWorker.getId());
        }
        projBase.setId(projectId);
        if(StringUtils.isEmpty(projBase.getProjCode())){
            projBase.setProjCode(code);
        }
        projBase.setDelFlag(CommonConstant.NO_READ_FLAG);
        projBase.setCreateTime(nowTime);
        projBase.setCreateUser(username);
        projBase.setUpdateTime(nowTime);
        projBase.setUpdateUser(username);
        projBase.setSort(0);
        projBase.setBudgetAmount(projBaseVo.getBudgetAmount());
        projBase.setProjAmount(projBaseVo.getBudgetAmount());

        this.save(projBase);



        Result result = new Result();
        result.setCode(200);
        result.setMessage("提交成功");
        result.setResult(projectId);
        result.setSuccess(true);
        return result;
    }



    /**
     * 新增
     * @param projBase
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addProjBase(ProjBase projBase) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        Date nowTime = new Date();

        String subject = projBase.getSubject();
        String prefix = null;
        if("1".equals(subject)){
            prefix = "YX";
        }else if("2".equals(subject)){
            prefix = "TJ";
        }else if("3".equals(subject)){
            prefix = "NM";
        }else if("4".equals(subject)){
            prefix = "XZ";
        }else if("6".equals(subject)){
            prefix = "ZH";
        }


        JSONObject formData = new JSONObject();
        formData.put("prefix", prefix);
        String code = (String) FillRuleUtil.executeRule("proj_code", formData);

        String id = String.valueOf(IdWorker.getId());
        projBase.setId(id);
        projBase.setProjCode(code);
        projBase.setDelFlag(CommonConstant.NO_READ_FLAG);
        projBase.setCreateTime(nowTime);
        projBase.setCreateUser(username);
        projBase.setUpdateTime(nowTime);
        projBase.setUpdateUser(username);
        projBase.setSort(0);

        String projectId = projBase.getId();

        //分类
        List<ProjectCategory> categoryList = projBase.getCategoryList();
        List<ProjectCategory> pcList = new ArrayList<>();
        List<ProjectBomRelation> bomList = new ArrayList<>();
        //递归分类
        extracted(username, nowTime, projectId, categoryList, pcList,"0");
        //总产能
//        BigDecimal capacity = BigDecimal.ZERO;
        //预算总额
        BigDecimal budgetAmount = BigDecimal.ZERO;

        for(ProjectCategory pc : pcList){
            if(pc.getLevel() == 0){
                budgetAmount = budgetAmount.add(pc.getBudgetAmount());
            }
            if(pc.getIsEqp() == 1 && pc.getIsLast() == 1){
                List<ProjectBomRelation> pbList = pc.getProdList();
                if(pbList != null && pbList.size() > 0){
                    for(ProjectBomRelation pb : pbList){
                        pb.setId(String.valueOf(IdWorker.getId()));
                        pb.setDelFlag(CommonConstant.NO_READ_FLAG);
                        pb.setUpdateUser(username);
                        pb.setUpdateTime(nowTime);
                        pb.setCreateUser(username);
                        pb.setCreateTime(nowTime);
                        pb.setProjId(projectId);
                        pb.setCategoryId(pc.getId());
                        bomList.add(pb);

//                        BigDecimal childCapacity = new BigDecimal(pb.getCapacity()).multiply(pb.getQty());
//                        capacity = capacity.add(childCapacity);
                    }
                }
            }
        }
//        projBase.setProjCapacity(capacity.toString());
//        if(projBase.getBudgetAmount().compareTo(budgetAmount) != 0){
//            throw new Exception("原预算金额与资金分类金额累加不一致");
//        }
        projBase.setBudgetAmount(budgetAmount);

        this.save(projBase);
        iProjectCategoryService.saveOrUpdateBatch(pcList);
        iProjectBomRelationService.saveOrUpdateBatch(bomList);

        //项目子项
        List<ProjectBomChild> childList = projBase.getChildList();
        List<ProjectBomChild> addChildList = new ArrayList<>();
        for(ProjectBomChild pb : childList){
            if(StringUtils.isNotEmpty(pb.getModel())){
                pb.setId(String.valueOf(IdWorker.getId()));
                pb.setUpdateTime(nowTime);
                pb.setCreateTime(nowTime);
                pb.setUpdateUser(username);
                pb.setCreateUser(username);
                pb.setDelFlag(CommonConstant.NO_READ_FLAG);
                pb.setProjectId(projectId);

                addChildList.add(pb);
            }

        }
        if (addChildList != null && addChildList.size() > 0){
            iProjectBomChildService.saveOrUpdateBatch(addChildList);
        }

        //汇率
        List<ProjectExchangeRate> rateList = projBase.getRateList();
        for(ProjectExchangeRate rate : rateList){
            rate.setProjectId(projectId);
            rate.setUpdateTime(nowTime);
            rate.setCreateTime(nowTime);
            rate.setUpdateUser(username);
            rate.setCreateUser(username);
            rate.setDelFlag(CommonConstant.NO_READ_FLAG);
        }
        iProjectExchangeRateService.saveBatch(rateList);
    }

    /**
     * 导入
     * @param request
     * @param response
     * @param clazz
     * @return
     */
    @Override
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, Class<BasMaterialImport> clazz) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            params.setHeadRows(1);
            params.setNeedSave(false);
            params.setSheetNum(1);
            try {
                int i = 1;
                List<BasMaterialImport> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
                List<BasMaterialImport> bmList = new ArrayList<>();
                if(list != null && list.size() > 0){
                    List<String> codes = new ArrayList<>();
                    for(BasMaterialImport bm : list){
                        if(ObjectCheck.checkObjAllFieldsIsNull(bm)){
                            continue;
                        }
                        String code = bm.getCode();
                        if(StringUtils.isEmpty(code)){
                            return Result.error("文件导入失败:第" + i + "行,设备编码不能为空" );
                        }

                        codes.add(bm.getCode().trim());
                    }
                    List<BasMaterial> existList = iBasMaterialService.list(Wrappers.<BasMaterial>query().lambda().in(BasMaterial :: getCode,codes));
                    if(existList == null || existList.size() == 0){
                        return Result.error("文件导入失败:设备编码在系统中不存在");
                    }
                    //集合转map
                    Map<String,BasMaterial> map = existList.stream().collect(Collectors.toMap(BasMaterial:: getCode , bm -> bm));

                    for(BasMaterialImport bm : list){
                        if(ObjectCheck.checkObjAllFieldsIsNull(bm)){
                            continue;
                        }
                        BigDecimal projQty = bm.getProjQty();
                        if(projQty == null){
                            return Result.error("文件导入失败:第" + i + "行立项数量为空" );
                        }
                        BigDecimal qty = bm.getQty();
                        if(qty == null){
                            return Result.error("文件导入失败:第" + i + "行,执行数量为空" );
                        }
                        BigDecimal budgetPrice = bm.getBudgetPrice();
                        if(budgetPrice == null){
                            return Result.error("文件导入失败:第" + i + "行,执行单价为空" );
                        }
                        BigDecimal projPrice = bm.getProjPrice();
                        if(projPrice == null){
                            return Result.error("文件导入失败:第" + i + "行,立项单价为空" );
                        }
                        BigDecimal budgetAmount = bm.getBudgetPrice().multiply(bm.getQty());
                        BigDecimal projAmount = bm.getProjPrice().max(bm.getProjQty());
                        BasMaterial exist = map.get(bm.getCode());
                        BasMaterialImport bmi = new BasMaterialImport();
                        BeanUtils.copyProperties(exist,bmi);
                        if(exist == null){
                            return Result.error("文件导入失败:第" + i + "行,设备编码在系统中不存在" );
                        }
                        bmi.setCapacity(bm.getCapacity());
                        bmi.setMaterialId(exist.getId());
                        bmi.setQty(qty);
                        bmi.setProjQty(projQty);
                        bmi.setBudgetPrice(budgetPrice);
                        bmi.setProjPrice(projPrice);
                        bmi.setCapacity(bm.getCapacity());
                        bmi.setBudgetAmount(budgetAmount);
                        bmi.setProjAmount(projAmount);
                        bmList.add(bmi);
                        i++;
                    }

                }else{
                    return Result.error("文件导入失败:文件为空" );
                }
                return Result.ok(bmList);
            } catch (Exception e) {
                log.error(e.getMessage());
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    /**
     * 项目总产能
     * @param projBase
     * @return
     */
    @Override
    public ProjBase fetchCapacityByProjId(ProjBase projBase) {
        return baseMapper.fetchCapacityByProjId(projBase);
    }

    /**
     * 资金类型饼图(已付、合同金额)
     * @param projBase
     * @return
     */
    @Override
    public Map<String, List<Map<String, String>>> fetchCategoryAmount(ProjBase projBase) {
        List<Map<String,String>> dataList = baseMapper.fetchCategoryAmount(projBase);
        Map<String,List<Map<String,String>>> map = new HashMap<>();

        //合同
        List<Map<String,String>> contract = new ArrayList<>();
        //付款
        List<Map<String,String>> pay = new ArrayList<>();
        //分类预算
        List<Map<String,String>> budget = new ArrayList<>();
        if(dataList != null && dataList.size() > 0){
            for(Map<String,String> mp : dataList){
                //合同
                Object object = mp.get("contractAmount");
                String ccount = String.valueOf(object);
                if(!"0.00".equals(ccount)){
                    Map<String,String> mp1 = new HashMap<>();
                    mp1.put("id",mp.get("id"));
                    mp1.put("name",mp.get("name"));
                    mp1.put("value",mp.get("contractAmount"));
                    contract.add(mp1);
                }
                object = mp.get("payAmount");
                ccount = String.valueOf(object);
                if(!"0.00".equals(ccount)){
                    //付款
                    Map<String,String> mp2 = new HashMap<>();
                    mp2.put("id",mp.get("id"));
                    mp2.put("name",mp.get("name"));
                    mp2.put("value",mp.get("payAmount"));
                    pay.add(mp2);
                }

                object = mp.get("budgetAmount");
                ccount = String.valueOf(object);
                if(!"0.00".equals(ccount)){
                    //预算
                    Map<String,String> mp2 = new HashMap<>();
                    mp2.put("id",mp.get("id"));
                    mp2.put("name",mp.get("name"));
                    mp2.put("value",mp.get("budgetAmount"));
                    budget.add(mp2);
                }
            }
        }
        map.put("contract",contract);
        map.put("pay",pay);
        map.put("budget",budget);
        return map;
    }

    /**
     * 维护汇率
     * @param projBases
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRate(List<ProjBase> projBases) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        Date nowTime = new Date();
        List<ProjectExchangeRate> rateList = new ArrayList<>();
        for(ProjBase pb : projBases){
            ProjectExchangeRate rate = iProjectExchangeRateService.getOne(Wrappers.<ProjectExchangeRate>query().lambda().
                    eq(ProjectExchangeRate :: getDelFlag,CommonConstant.DEL_FLAG_0).
                    eq(ProjectExchangeRate :: getProjectId,pb.getId()).
                    eq(ProjectExchangeRate :: getCurrencyB,pb.getCurrencyB()));
            if(rate == null){
                rate = new ProjectExchangeRate();
                rate.setId(String.valueOf(IdWorker.getId()));
                rate.setProjectId(pb.getId());
                rate.setUpdateTime(nowTime);
                rate.setCreateTime(nowTime);
                rate.setUpdateUser(username);
                rate.setCreateUser(username);
                rate.setDelFlag(CommonConstant.NO_READ_FLAG);
                rate.setCurrencyA("RMB");
                rate.setValueA(BigDecimal.ONE);
                rate.setCurrencyB(pb.getCurrencyB());
                rate.setValueB(pb.getValueB());
                rateList.add(rate);
            }else{
                rate.setValueB(pb.getValueB());
                rateList.add(rate);
            }
        }
        iProjectExchangeRateService.saveOrUpdateBatch(rateList);
    }

    /**
     * 项目总投
     * @param projBase
     * @return
     */
    @Override
    public ProjBase fetchProjectAmount(ProjBase projBase) {
        return baseMapper.fetchProjectAmount(projBase);
    }

    /**
     * 首页 - 金额支出情况
     * @param projBase
     * @return
     */
    @Override
    public Map<String,BigDecimal> fetchAmount(ProjBase projBase) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = loginUser.getDepartIds();

        //如果是部门职员进入则看该部门数据汇总
//        if("dept".equals(projBase.getSource())){
//            projBase.setDeptId(deptId);
//        }
        //统计所有项目的总预算
        BigDecimal budgetAmount = baseMapper.fetchBudgetAmountByProjId(projBase);
        if(budgetAmount == null ){
            budgetAmount = BigDecimal.ZERO;
        }
        //合同金额
        BigDecimal contractAmount = baseMapper.fetchContractAmountByProjId(projBase);
        if(contractAmount == null){
            contractAmount = BigDecimal.ZERO;
        }
        //剩余金额
        BigDecimal remainAmount = budgetAmount.subtract(contractAmount).setScale(2,BigDecimal.ROUND_HALF_UP);
        //支出率
        BigDecimal ratio = BigDecimal.ZERO;
        if(budgetAmount.compareTo(BigDecimal.ZERO) == 0){

        }else{
            ratio = contractAmount.divide(budgetAmount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        }

        Map<String,BigDecimal> map = new HashMap<>();
        map.put("budgetAmount",budgetAmount == null ? BigDecimal.ZERO : budgetAmount);
        map.put("contractAmount",contractAmount == null ? BigDecimal.ZERO : contractAmount);
        map.put("remainAmount",remainAmount == null ? BigDecimal.ZERO : remainAmount);
        map.put("percentNum",ratio == null ? BigDecimal.ZERO : ratio);

        return map;
    }

    /**
     * 首页 - 设备采购情况
     * @param projBase
     * @return
     */
    @Override
    public Map<String, BigDecimal> fetchQty(ProjBase projBase) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = loginUser.getDepartIds();

        //如果是部门职员进入则看该部门数据汇总
//        if("dept".equals(projBase.getSource())){
//            projBase.setDeptId(deptId);
//        }

        //统计所有项目设备总数
        BigDecimal budgetQty = baseMapper.fetchBudgetQtyByProjId(projBase);
        if(budgetQty == null ){
            budgetQty = BigDecimal.ZERO;
        }
        //合同数量
        BigDecimal contractQty = baseMapper.fetchContractQtyByProjId(projBase);
        if(contractQty == null){
            contractQty = BigDecimal.ZERO;
        }

        //剩余数量
        BigDecimal remainQty = budgetQty.subtract(contractQty).setScale(2,BigDecimal.ROUND_HALF_UP);
        //支出率
        BigDecimal ratio = BigDecimal.ZERO;
        if(BigDecimal.ZERO.compareTo(budgetQty) == 0){

        }else{
            ratio = contractQty.divide(budgetQty,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        }

        Map<String,BigDecimal> map = new HashMap<>();
        map.put("budgetQty",budgetQty);
        map.put("contractQty",contractQty);
        map.put("remainQty",remainQty);
        map.put("percent",ratio);
        return map;
    }

    /**
     * 项目总产能(合并同类项)
     * @param projBase
     * @return
     */
    @Override
    public List<ProjectBomRelation> fetchCapacity(ProjBase projBase) {
        return baseMapper.fetchCapacity(projBase);
    }

    /**
     * 项目预算购置表
     * @param page
     * @param contractBase
     * @return
     */
    @Override
    public IPage<ProjBudget> fetchProjBudgetPageList(Page<ProjBudget> page, ProjBudget contractBase) {
        return baseMapper.fetchProjBudgetPageList(page,contractBase);
    }

    /**
     * 项目预算购置表导出
     * @param request
     * @param contractBase
     * @param clazz
     * @param title
     * @return
     */
    @Override
    public void exportProjBudgetPageList(HttpServletRequest request, ProjBudget contractBase, Class<ProjBudget> clazz, String title,HttpServletResponse response) {


        List<ProjBudget> exportList = baseMapper.exportProjBudgetPageList(contractBase);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        List<Map<String, Object>> list = new ArrayList<>();

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("title",getExportParams(title));//表格title
        map.put("entity",clazz);//表格对应实体
        map.put("data", exportList);
        list.add(map);
        Workbook wb = ExcelExportUtil.exportExcel(list,ExcelType.XSSF);

        try {
            ExportUtil.insertWaterMarkTextToXlsx((XSSFWorkbook)wb,sysUser.getRealname() + "(" + sysUser.getUsername() + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        ModelAndView mv = new ModelAndView(new JeecgEntityExcel());
//        mv.addObject(NormalExcelConstants.FILE_NAME, title);
//        mv.addObject(NormalExcelConstants.CLASS, clazz);
//        ExportParams exportParams=new ExportParams(title, "导出人:" + sysUser.getRealname(), title);
//        exportParams.setImageBasePath(upLoadPath);
//        mv.addObject(NormalExcelConstants.PARAMS,exportParams);
//        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
//        return mv;
    }

    /**
     * 项目预算购置表汇总
     * @param contractBase
     * @return
     */
    @Override
    public ProjBudget fetchTotalProjBudgetPageList(ProjBudget contractBase) {
        return baseMapper.fetchTotalProjBudgetPageList(contractBase);
    }

    /**
     * 修改文件名称
     * @param fileParam
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String changeFileName(FileParam fileParam) throws Exception {
        List<Map<String,String>> urlList = fileParam.getUrlList();
        Boolean flag = false;
        List<String> allUrl = new ArrayList<>();
        for(Map<String,String> map : urlList){
            String oldUrl = map.get("oldUrl");
            String newUrl = map.get("newUrl");
            if(StringUtils.isNotEmpty(newUrl)){
                flag = true;
                //获取原文件后缀
                String suffix = oldUrl.substring(oldUrl.lastIndexOf("."),oldUrl.length());
                newUrl = newUrl + suffix;
                map.put("newUrl",newUrl);
                allUrl.add(newUrl);
            }else{
                allUrl.add(oldUrl);
            }
        }
        //如果存在文件名修改则更新数据库
        if(flag){
            //更新数据库
            if(StringUtils.isNotEmpty(fileParam.getId())){
                fileParam.setUrl(String.join(",",allUrl));
                baseMapper.changeFileName(fileParam);
            }

            //更新服务器
            for(Map<String,String> map : urlList){
                String oldUrl = upLoadPath + File.separator + map.get("oldUrl");
                String newUrl = upLoadPath + File.separator + map.get("newUrl");
                if(StringUtils.isNotEmpty(map.get("newUrl"))){
                    reNameFile(oldUrl, newUrl);
                }
            }
        }
        return String.join(",",allUrl);
    }



    public static void reNameFile(String oldPath, String newPath) throws Exception {
        File file = new File(newPath);
        if(file.exists()){
            throw new Exception("文件名已在服务器上存在,请重新命名");
        }
        boolean result = new File(oldPath).renameTo(new File(newPath));
        System.out.println("重命名的结果：" + result);
    }

}
