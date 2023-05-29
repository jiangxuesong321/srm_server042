package org.cmoc.modules.srm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.cmoc.common.api.vo.Result;
import org.cmoc.common.aspect.annotation.AutoLog;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.common.system.query.QueryGenerator;
import org.cmoc.common.system.vo.DictModelMany;
import org.cmoc.common.system.vo.LoginUser;
import org.cmoc.modules.srm.entity.*;
import org.cmoc.modules.srm.service.*;
import org.cmoc.modules.srm.utils.JeecgEntityExcel;
import org.cmoc.modules.srm.vo.ContractBasePage;
import org.cmoc.modules.system.entity.PurchaseOrderMain;
import org.cmoc.modules.system.entity.SysDepart;
import org.cmoc.modules.system.mapper.PurchaseOrderMainMapper;
import org.cmoc.modules.system.service.ISysDepartService;
import org.cmoc.modules.system.service.ISysDictService;
import org.cmoc.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 合同基本信息表
 * @Author: jeecg-boot
 * @Date: 2022-06-21
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/contractBase")
@Slf4j
public class ContractBaseController {
    @Autowired
    private IContractBaseService contractBaseService;
    @Autowired
    private IContractObjectService contractObjectService;
    @Autowired
    private IContractObjectQtyService iContractObjectQtyService;
    @Autowired
    private IContractPayStepService contractPayStepService;
    @Autowired
    private IContractTermsService contractTermsService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private IApproveRecordService iApproveRecordService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private IProjBaseService iProjBaseService;
    @Autowired
    private IBasSupplierContactService iBasSupplierContactService;
    @Autowired
    private ISysDepartService iSysDepartService;

    @Autowired
    private PurchaseOrderMainMapper purchaseOrderMainMapper;

    /**
     * 分页列表查询
     *
     * @param contractBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "合同基本信息表-分页列表查询")
    @ApiOperation(value = "合同基本信息表-分页列表查询", notes = "合同基本信息表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ContractBase>> queryPageList(ContractBase contractBase,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                     HttpServletRequest req) {
        //判断当前用户权限
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        String deptId = loginUser.getDepartIds();

        String permission = iSysUserService.fetchPermission(username);
        String auth = "owner";
        String auther = username;
        if (StringUtils.isNotEmpty(permission)) {
            if (permission.contains("contract:all")) {
                auth = "all";
            } else if (permission.contains("contract:subject")) {
                auth = "subject";
                //当前用户属于那个主体
                String subject = loginUser.getRelTenantIds();
                auther = subject;
            } else if (permission.contains("contract:dept")) {
                auth = "dept";
                auther = deptId;
            }
        }

        contractBase.setAuth(auth);
        contractBase.setAuther(auther);
        contractBase.setCreateUser(username);

        Page<ContractBase> page = new Page<ContractBase>(pageNo, pageSize);
        IPage<ContractBase> pageList = contractBaseService.queryPageList(page, contractBase);
        //阶梯价子合同
        for (ContractBase record : pageList.getRecords()) {
            LambdaQueryWrapper<PurchaseOrderMain> qy = new LambdaQueryWrapper<>();
            qy.eq(PurchaseOrderMain::getContactId, record.getId());
            PurchaseOrderMain purchaseOrderMain = purchaseOrderMainMapper.selectOne(qy);
            if (purchaseOrderMain != null) {
                record.setSapPo(purchaseOrderMain.getSapPo());
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param contractBasePage
     * @return
     */
    @AutoLog(value = "合同基本信息表-添加")
    @ApiOperation(value = "合同基本信息表-添加", notes = "合同基本信息表-添加")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody ContractBasePage contractBasePage) throws Exception {
        ContractBase contractBase = new ContractBase();
        BeanUtils.copyProperties(contractBasePage, contractBase);
        contractBaseService.saveMain(contractBase, contractBasePage.getContractObjectList(), contractBasePage.getContractTermsList(), contractBasePage.getContractPayStepList());
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param contractBasePage
     * @return
     */
    @AutoLog(value = "合同基本信息表-添加")
    @ApiOperation(value = "合同基本信息表-添加", notes = "合同基本信息表-添加")
    @PostMapping(value = "/addChild")
    public Result<String> addChild(@RequestBody ContractBasePage contractBasePage) throws Exception {
        ContractBase contractBase = new ContractBase();
        BeanUtils.copyProperties(contractBasePage, contractBase);
        contractBaseService.saveMainChild(contractBase, contractBasePage.getContractObjectList());
        return Result.OK("添加成功！");
    }

    /**
     * 添加
     *
     * @param contractBasePage
     * @return
     */
    @AutoLog(value = "合同基本信息表-添加")
    @ApiOperation(value = "合同基本信息表-添加", notes = "合同基本信息表-添加")
    @PostMapping(value = "/draft")
    public Result<String> draft(@RequestBody ContractBasePage contractBasePage) throws Exception {
        ContractBase contractBase = new ContractBase();
        BeanUtils.copyProperties(contractBasePage, contractBase);
        contractBaseService.draft(contractBase, contractBasePage.getContractObjectList(), contractBasePage.getContractTermsList(), contractBasePage.getContractPayStepList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param contractBase
     * @return
     */
    @AutoLog(value = "合同基本信息表-编辑")
    @ApiOperation(value = "合同基本信息表-编辑", notes = "合同基本信息表-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody ContractBase contractBase) throws Exception {
        contractBaseService.updateContract(contractBase);
        return Result.OK("编辑成功!");
    }

    /**
     * 编辑
     *
     * @param contractBase
     * @return
     */
    @AutoLog(value = "合同基本信息表-编辑")
    @ApiOperation(value = "合同基本信息表-编辑", notes = "合同基本信息表-编辑")
    @RequestMapping(value = "/reject", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> reject(@RequestBody ContractBase contractBase) {
        contractBaseService.updateById(contractBase);

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        Date nowTime = new Date();
        ApproveRecord approve = new ApproveRecord();
        approve.setApprover(contractBase.getApprover());
        approve.setApproveComment(contractBase.getApproveComment());
        approve.setCreateTime(nowTime);
        approve.setUpdateTime(nowTime);
        approve.setUpdateUser(username);
        approve.setCreateUser(username);
        approve.setBusinessId(contractBase.getId());
        approve.setDelFlag(CommonConstant.NO_READ_FLAG);
        approve.setType("contract_zh");
        iApproveRecordService.save(approve);

        //发送邮件
        List<BasSupplierContact> contactList = iBasSupplierContactService.list(Wrappers.<BasSupplierContact>query().lambda().
                eq(BasSupplierContact::getSupplierId, contractBase.getContractSecondPartyId()).
                eq(BasSupplierContact::getDelFlag, CommonConstant.DEL_FLAG_0).
                eq(BasSupplierContact::getIsDefault, CommonConstant.ACT_SYNC_1));
//		 if(contactList != null && contactList.size() > 0){
//			 List<String> emails = new ArrayList<>();
//			 emails.add(contactList.get(0).getContacterEmail());
//			 EmailSendMsgHandle emailHandle=new EmailSendMsgHandle();
//			 String context = "[" + contractBase.getContractSecondParty() + "]:" +
//					 "<br>&nbsp;&nbsp;&nbsp;&nbsp;你好！" +
//					 "<br>&nbsp;&nbsp;&nbsp;&nbsp;合同[" + contractBase.getContractName() + "] 存在问题,现已驳回,请重新修改提交" +
//					 "<br>"+
//					 "<br><span style='margin-left:310px'>["+contractBase.getContractFirstParty()+"]</span>";
//			 emailHandle.sendTemplateMail("合同驳回",context,emails,null,"0");
//		 }
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "合同基本信息表-通过id删除")
    @ApiOperation(value = "合同基本信息表-通过id删除", notes = "合同基本信息表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        contractBaseService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "合同基本信息表-批量删除")
    @ApiOperation(value = "合同基本信息表-批量删除", notes = "合同基本信息表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.contractBaseService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "合同基本信息表-通过id查询")
    @ApiOperation(value = "合同基本信息表-通过id查询", notes = "合同基本信息表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<ContractBase> queryById(@RequestParam(name = "id", required = true) String id) {
        ContractBase contractBase = contractBaseService.getById(id);
        if (contractBase == null) {
            return Result.error("未找到对应数据");
        }
        ProjBase projBase = iProjBaseService.getById(contractBase.getProjectId());
        contractBase.setProjName(projBase.getProjName());
        contractBase.setProjCode(projBase.getProjCode());

        //获取配置地址
        SysDepart depart = iSysDepartService.getById(contractBase.getContractFirstPartyId());
        if (depart != null) {
            contractBase.setAddress(depart.getAddress());
        }
        return Result.OK(contractBase);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "合同标的通过主表ID查询")
    @ApiOperation(value = "合同标的主表ID查询", notes = "合同标的-通主表ID查询")
    @GetMapping(value = "/queryContractObjectByMainId")
    public Result<List<ContractObject>> queryContractObjectListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<ContractObject> contractObjectList = contractObjectService.selectByMainId(id);
        return Result.OK(contractObjectList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "合同付款阶段通过主表ID查询")
    @ApiOperation(value = "合同付款阶段主表ID查询", notes = "合同付款阶段-通主表ID查询")
    @GetMapping(value = "/queryContractPayStepByMainId")
    public Result<List<ContractPayStep>> queryContractPayStepListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<ContractPayStep> contractPayStepList = contractPayStepService.selectByMainId(id);
        return Result.OK(contractPayStepList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "合同条款通过主表ID查询")
    @ApiOperation(value = "合同条款主表ID查询", notes = "合同条款-通主表ID查询")
    @GetMapping(value = "/queryContractTermsByMainId")
    public Result<List<ContractTerms>> queryContractTermsListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<ContractTerms> contractTermsList = contractTermsService.selectByMainId(id);
        return Result.OK(contractTermsList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param contractBase
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ContractBase contractBase) {
        // Step.1 组装查询条件查询数据
//      QueryWrapper<ContractBase> queryWrapper = QueryGenerator.initQueryWrapper(contractBase, request.getParameterMap());
//      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        String deptId = loginUser.getDepartIds();

        String permission = iSysUserService.fetchPermission(username);
        String auth = "owner";
        String auther = username;
        if (StringUtils.isNotEmpty(permission)) {
            if (permission.contains("contract:all")) {
                auth = "all";
            } else if (permission.contains("contract:subject")) {
                auth = "subject";
                //当前用户属于那个主体
                String subject = loginUser.getRelTenantIds();
                auther = subject;
            } else if (permission.contains("contract:dept")) {
                auth = "dept";
                auther = deptId;
            }
        }

        contractBase.setAuth(auth);
        contractBase.setAuther(auther);
        contractBase.setCreateUser(username);

        List<ContractBase> pageList = contractBaseService.queryExportList(contractBase);


        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcel());
        mv.addObject(NormalExcelConstants.FILE_NAME, "合同列表");
        mv.addObject(NormalExcelConstants.CLASS, ContractBase.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("合同列表", "导出人:" + loginUser.getRealname(), "合同列表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
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
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<ContractBasePage> list = ExcelImportUtil.importExcel(file.getInputStream(), ContractBasePage.class, params);
                for (ContractBasePage page : list) {
                    ContractBase po = new ContractBase();
                    BeanUtils.copyProperties(page, po);
                    contractBaseService.saveMain(po, page.getContractObjectList(), page.getContractTermsList(), page.getContractPayStepList());
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }

    /**
     * 合同明细
     *
     * @param contractBase
     * @param req
     * @return
     */
    //@AutoLog(value = "合同基本信息表-分页列表查询")
    @ApiOperation(value = "合同基本信息表-分页列表查询", notes = "合同基本信息表-分页列表查询")
    @GetMapping(value = "/getContractDetailList")
    public Result<List<ContractObject>> getContractDetailList(ContractBase contractBase,
                                                              HttpServletRequest req) {
        List<ContractObject> pageList = contractObjectService.getContractDetailList(contractBase);
        return Result.OK(pageList);
    }

    /**
     * 合同下的设备信息
     *
     * @param contractObject
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchEqpByContract")
    public Result<List<ContractObject>> fetchEqpByContract(ContractObject contractObject, HttpServletRequest req) {
        List<ContractObject> pageList = contractObjectService.fetchEqpByContract(contractObject);
        return Result.OK(pageList);
    }

    /**
     * 合同下的设备价格
     *
     * @param contractObject
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchEqpPriceByContract")
    public Result<List<ContractObject>> fetchEqpPriceByContract(ContractObject contractObject, HttpServletRequest req) {
        List<ContractObject> pageList = contractObjectService.fetchEqpPriceByContract(contractObject);
        return Result.OK(pageList);
    }

    /**
     * 供货供应商
     *
     * @param contractObject
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchEqpSuppByContract")
    public Result<List<BasSupplier>> fetchEqpSuppByContract(ContractObject contractObject, HttpServletRequest req) {
        List<BasSupplier> pageList = contractObjectService.fetchEqpSuppByContract(contractObject);

        List<String> codeList = new ArrayList<>();
        codeList.add("supp_type");
        List<DictModelMany> dictList = iSysDictService.getDictItemsByCodeList(codeList);
        Map<String, String> map = dictList.stream().collect(Collectors.toMap(DictModelMany::getValue, DictModelMany::getText));
        if (pageList != null && pageList.size() > 0) {
            for (BasSupplier bs : pageList) {
                if (map != null && !map.isEmpty()) {
                    List<String> suppTypes = new ArrayList<>();
                    String[] types = bs.getSupplierType().split(",");
                    for (String str : types) {
                        String value = map.get(str);
                        if (StringUtils.isNotEmpty(value)) {
                            suppTypes.add(value);
                        }
                    }
                    if (suppTypes != null && suppTypes.size() > 0) {
                        bs.setSupplierTypeDict(String.join(",", suppTypes));
                    }
                }
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 供应商合同
     *
     * @param contractBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchContractBySupp")
    public Result<IPage<ContractBase>> fetchContractBySupp(ContractBase contractBase,
                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                           HttpServletRequest req) {
//		 QueryWrapper<ContractBase> queryWrapper = new QueryWrapper<>();
//		 queryWrapper.lambda().eq(ContractBase :: getDelFlag, CommonConstant.DEL_FLAG_0);
//		 queryWrapper.lambda().eq(ContractBase :: getContractSecondPartyId,contractBase.getContractSecondPartyId());
//		 queryWrapper.lambda().eq(ContractBase :: getContractStatus,"4");
        Page<ContractBase> page = new Page<ContractBase>(pageNo, pageSize);
        IPage<ContractBase> pageList = contractBaseService.fetchContractBySupp(page, contractBase);
        return Result.OK(pageList);
    }

    /**
     * 以项目为单位统计合同总额
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchContractByProjId")
    public Result<?> fetchContractByProjId(ContractBase contractBase, HttpServletRequest req) {
        ContractBase entity = contractBaseService.fetchContractByProjId(contractBase);
        return Result.OK(entity);
    }

    /**
     * 以项目为单位统计合同总额
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchContractByProjType")
    public Result<?> fetchContractByProjType(ContractBase contractBase, HttpServletRequest req) {
        List<Map<String, Object>> entity = contractBaseService.fetchContractByProjType(contractBase);
        return Result.OK(entity);
    }

    /**
     * 分页列表查询
     *
     * @param contractBase
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "合同基本信息表-分页列表查询")
    @ApiOperation(value = "合同基本信息表-分页列表查询", notes = "合同基本信息表-分页列表查询")
    @GetMapping(value = "/fetchContractListByProjId")
    public Result<IPage<ContractBase>> fetchContractListByProjId(ContractBase contractBase,
                                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                 HttpServletRequest req) {
        Page<ContractBase> page = new Page<ContractBase>(pageNo, pageSize);
        IPage<ContractBase> pageList = contractBaseService.fetchContractListByProjId(page, contractBase);
        return Result.OK(pageList);
    }

    /**
     * 获取审核原因
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryApprove")
    public Result<ApproveRecord> queryApprove(@RequestParam(name = "id", required = true) String id,
                                              @RequestParam(name = "type", required = false) String type) {
        ApproveRecord record = new ApproveRecord();
        QueryWrapper<ApproveRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApproveRecord::getBusinessId, id);
        queryWrapper.lambda().eq(ApproveRecord::getDelFlag, CommonConstant.DEL_FLAG_0);
        if (StringUtils.isNotEmpty(type)) {
            queryWrapper.lambda().eq(ApproveRecord::getType, type);
        }
        queryWrapper.lambda().orderByDesc(ApproveRecord::getCreateTime);

        List<ApproveRecord> recordList = iApproveRecordService.list(queryWrapper);
        if (recordList != null && recordList.size() > 0) {
            record = recordList.get(0);
        } else {
            record = null;
        }
        return Result.OK(record);
    }

    /**
     * 根据对象里面的属性值作in查询 属性可能会变 用户组件用到
     *
     * @param obj
     * @return
     */
    @GetMapping("/getMultiContract")
    public List<ContractBase> getMultiContract(ContractBase obj) {
        List<ContractBase> pageList = contractBaseService.list(Wrappers.<ContractBase>query().lambda().
                in(ContractBase::getId, obj.getId().split(",")));
        return pageList;
    }

    /**
     * 分页列表查询
     *
     * @param obj
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "合同基本信息表-分页列表查询")
    @ApiOperation(value = "合同基本信息表-分页列表查询", notes = "合同基本信息表-分页列表查询")
    @GetMapping(value = "/listByPage")
    public Result<IPage<ContractBase>> listByPage(ContractBase obj,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  HttpServletRequest req) {
        QueryWrapper<ContractBase> queryWrapper = QueryGenerator.initQueryWrapper(obj, req.getParameterMap());
        Page<ContractBase> page = new Page<ContractBase>(pageNo, pageSize);
        queryWrapper.lambda().eq(ContractBase::getContractStatus, "4");
        IPage<ContractBase> pageList = contractBaseService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param obj
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "合同基本信息表-分页列表查询")
    @ApiOperation(value = "合同基本信息表-分页列表查询", notes = "合同基本信息表-分页列表查询")
    @GetMapping(value = "/listByDetailList")
    public Result<IPage<ContractObject>> listByDetailList(ContractObject obj,
                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                          HttpServletRequest req) {
        Page<ContractObject> page = new Page<ContractObject>(pageNo, pageSize);
        IPage<ContractObject> pageList = contractObjectService.listByDetailList(page, obj);
        return Result.OK(pageList);
    }

    /**
     * OA审批
     *
     * @return
     */
    @AutoLog(value = "合同编辑")
    @RequestMapping(value = "/toApprove", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> toApprove(@RequestBody ContractBase contractBase) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        Date nowTime = new Date();
        if ("4".equals(contractBase.getContractStatus())) {
            contractBase.setContractValidDate(nowTime);
        }
        contractBase.setProcessNode(contractBase.getName());
        contractBaseService.updateById(contractBase);

        ApproveRecord approve = new ApproveRecord();
        approve.setApprover(contractBase.getApprover());
        approve.setApproveComment(null);
        approve.setCreateTime(nowTime);
        approve.setUpdateTime(nowTime);
        approve.setUpdateUser(username);
        approve.setCreateUser(username);
        approve.setBusinessId(contractBase.getId());
        approve.setDelFlag(CommonConstant.NO_READ_FLAG);
        approve.setType("OA");
        approve.setName(contractBase.getName());
        approve.setStatus(contractBase.getContractStatus());
        approve.setCode(contractBase.getProcessCode());
        iApproveRecordService.save(approve);
        return Result.OK("编辑成功!");
    }

    /**
     * 分页列表查询
     *
     * @param req
     * @return
     */
    //@AutoLog(value = "合同基本信息表-分页列表查询")
    @ApiOperation(value = "合同基本信息表-分页列表查询", notes = "合同基本信息表-分页列表查询")
    @GetMapping(value = "/fetchApproveList")
    public Result<List<ApproveRecord>> fetchApproveList(ApproveRecord approveRecord,
                                                        HttpServletRequest req) {
        List<ApproveRecord> pageList = iApproveRecordService.list(Wrappers.<ApproveRecord>query().lambda().
                eq(ApproveRecord::getType, approveRecord.getType()).
                eq(ApproveRecord::getBusinessId, approveRecord.getBusinessId()).orderByDesc(ApproveRecord::getCreateTime));
        return Result.OK(pageList);
    }


    /**
     * 合同数量
     *
     * @param contractBase
     * @return
     */
    @ApiOperation(value = "合同基本信息表-分页列表查询", notes = "合同基本信息表-分页列表查询")
    @GetMapping(value = "/fetchContractQty")
    public Result<ContractObject> fetchContractQty(ContractBase contractBase,
                                                   HttpServletRequest req) {
        ContractObject obj = contractObjectService.fetchContractQty(contractBase);
        return Result.OK(obj);
    }

    /**
     * 查询汇率
     *
     * @param projectId
     * @return
     */
    @GetMapping(value = "/getExchangeRate")
    public Result<BigDecimal> getExchangeRate(@RequestParam(name = "projectId", required = true) String projectId,
                                              @RequestParam(name = "currency", required = true) String currency) throws Exception {
        BigDecimal exchangeRate = contractBaseService.getExchangeRate(projectId, currency);
        if (exchangeRate == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(exchangeRate);

    }

    public static void main(String[] args) {
        String str = "1-14";
        List<String> numbers = new ArrayList<>();
        String[] list = str.split("-");
        Integer start = Integer.valueOf(list[0]);
        Integer end = Integer.valueOf(list[1]);
        for (int i = start; i <= end; i++) {
            numbers.add(i + "");
        }
        System.out.println(String.join(",", numbers));
    }

    /**
     * OA附件回传
     *
     * @return
     */
    @AutoLog(value = "OA附件回传")
    @RequestMapping(value = "/uploadOaAttachment", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> uploadOaAttachment(@RequestBody ContractBase contractBase) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        Date nowTime = new Date();
        contractBase.setUpdateUser(username);
        contractBase.setUpdateTime(nowTime);
        contractBaseService.updateById(contractBase);

        return Result.OK("编辑成功!");
    }


    /**
     * 合同分类统计金额
     *
     * @param contractBase
     * @return
     */
    @ApiOperation(value = "合同基本信息表-分页列表查询", notes = "合同基本信息表-分页列表查询")
    @GetMapping(value = "/fetchContractAmountByType")
    public Result<List<Map<String, Object>>> fetchContractAmountByType(ContractBase contractBase,
                                                                       HttpServletRequest req) {
        List<Map<String, Object>> obj = contractBaseService.fetchContractAmountByType(contractBase);
        return Result.OK(obj);
    }

    /**
     * 合同数量
     *
     * @param contractBase
     * @return
     */
    @ApiOperation(value = "合同基本信息表-分页列表查询", notes = "合同基本信息表-分页列表查询")
    @GetMapping(value = "/fetchContractNum")
    public Result<Map<String, Object>> fetchContractNum(ContractBase contractBase,
                                                        HttpServletRequest req) {
        Map<String, Object> obj = contractBaseService.fetchContractNum(contractBase);
        return Result.OK(obj);
    }

    /**
     * 每个月合同数量统计
     *
     * @param contractBase
     * @return
     */
    @ApiOperation(value = "合同基本信息表-分页列表查询", notes = "合同基本信息表-分页列表查询")
    @GetMapping(value = "/fetchContractNumByMonth")
    public Result<Map<String, Object>> fetchContractNumByMonth(ContractBase contractBase,
                                                               HttpServletRequest req) {
        Map<String, Object> obj = contractBaseService.fetchContractNumByMonth(contractBase);
        return Result.OK(obj);
    }

    /**
     * 台套数
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchQtyNum")
    public Result<Map<String, Object>> fetchQtyNum(ContractBase contractBase,
                                                   HttpServletRequest req) {
        //判断当前用户权限
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        String deptId = loginUser.getDepartIds();

        String permission = iSysUserService.fetchPermission(username);
        String auth = "owner";
        String auther = username;
        if (StringUtils.isNotEmpty(permission)) {
            if (permission.contains("contract:all")) {
                auth = "all";
            } else if (permission.contains("contract:subject")) {
                auth = "subject";
                //当前用户属于那个主体
                String subject = loginUser.getRelTenantIds();
                auther = subject;
            } else if (permission.contains("contract:dept")) {
                auth = "dept";
                auther = deptId;
            }
        }

        contractBase.setAuth(auth);
        contractBase.setAuther(auther);
        contractBase.setCreateUser(username);

        Map<String, Object> pageList = contractBaseService.fetchQtyNum(contractBase);

        return Result.OK(pageList);
    }

    /**
     * 合同金额
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchContractAmountTaxLocal")
    public Result<Map<String, Object>> fetchContractAmountTaxLocal(ContractBase contractBase,
                                                                   HttpServletRequest req) {
        //判断当前用户权限
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        String deptId = loginUser.getDepartIds();

        String permission = iSysUserService.fetchPermission(username);
        String auth = "owner";
        String auther = username;
        if (StringUtils.isNotEmpty(permission)) {
            if (permission.contains("contract:all")) {
                auth = "all";
            } else if (permission.contains("contract:subject")) {
                auth = "subject";
                //当前用户属于那个主体
                String subject = loginUser.getRelTenantIds();
                auther = subject;
            } else if (permission.contains("contract:dept")) {
                auth = "dept";
                auther = deptId;
            }
        }

        contractBase.setAuth(auth);
        contractBase.setAuther(auther);
        contractBase.setCreateUser(username);

        Map<String, Object> pageList = contractBaseService.fetchContractAmountTaxLocal(contractBase);

        return Result.OK(pageList);
    }

    /**
     * 已付金额
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchPayAmountTaxLocal")
    public Result<Map<String, Object>> fetchPayAmountTaxLocal(ContractBase contractBase,
                                                              HttpServletRequest req) {
        //判断当前用户权限
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        String deptId = loginUser.getDepartIds();

        String permission = iSysUserService.fetchPermission(username);
        String auth = "owner";
        String auther = username;
        if (StringUtils.isNotEmpty(permission)) {
            if (permission.contains("contract:all")) {
                auth = "all";
            } else if (permission.contains("contract:subject")) {
                auth = "subject";
                //当前用户属于那个主体
                String subject = loginUser.getRelTenantIds();
                auther = subject;
            } else if (permission.contains("contract:dept")) {
                auth = "dept";
                auther = deptId;
            }
        }

        contractBase.setAuth(auth);
        contractBase.setAuther(auther);
        contractBase.setCreateUser(username);

        Map<String, Object> pageList = contractBaseService.fetchPayAmountTaxLocal(contractBase);

        return Result.OK(pageList);
    }

    /**
     * 已开票金额
     *
     * @param contractBase
     * @param req
     * @return
     */
    @GetMapping(value = "/fetchInvoiceAmountTaxLocal")
    public Result<Map<String, Object>> fetchInvoiceAmountTaxLocal(ContractBase contractBase,
                                                                  HttpServletRequest req) {
        //判断当前用户权限
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        String deptId = loginUser.getDepartIds();

        String permission = iSysUserService.fetchPermission(username);
        String auth = "owner";
        String auther = username;
        if (StringUtils.isNotEmpty(permission)) {
            if (permission.contains("contract:all")) {
                auth = "all";
            } else if (permission.contains("contract:subject")) {
                auth = "subject";
                //当前用户属于那个主体
                String subject = loginUser.getRelTenantIds();
                auther = subject;
            } else if (permission.contains("contract:dept")) {
                auth = "dept";
                auther = deptId;
            }
        }

        contractBase.setAuth(auth);
        contractBase.setAuther(auther);
        contractBase.setCreateUser(username);

        Map<String, Object> pageList = contractBaseService.fetchInvoiceAmountTaxLocal(contractBase);

        return Result.OK(pageList);
    }

    /**
     * 展开子项
     *
     * @param contractBase
     * @param req
     * @return
     */
    //@AutoLog(value = "合同基本信息表-分页列表查询")
    @ApiOperation(value = "合同基本信息表-分页列表查询", notes = "合同基本信息表-分页列表查询")
    @GetMapping(value = "/fetchChildList")
    public Result<List<ContractObject>> fetchChildList(ContractBase contractBase,
                                                       HttpServletRequest req) {
        List<ContractObject> pageList = contractBaseService.fetchChildList(contractBase);
        return Result.OK(pageList);
    }

    /**
     * 展开子项
     *
     * @param contractBase
     * @param req
     * @return
     */
    //@AutoLog(value = "合同基本信息表-分页列表查询")
    @ApiOperation(value = "合同基本信息表-分页列表查询", notes = "合同基本信息表-分页列表查询")
    @GetMapping(value = "/fetchChildQtyList")
    public Result<List<ContractObject>> fetchChildQtyList(ContractBase contractBase,
                                                          HttpServletRequest req) {
        List<ContractObject> pageList = contractBaseService.fetchChildQtyList(contractBase);
        return Result.OK(pageList);
    }


}
