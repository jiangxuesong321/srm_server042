package org.cmoc.modules.srm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.cmoc.common.system.vo.LoginUser;
import org.cmoc.modules.srm.entity.*;
import org.cmoc.modules.srm.mapper.InquiryRecordMapper;
import org.cmoc.modules.srm.mapper.PurchaseRequestDetailMapper;
import org.cmoc.modules.srm.mapper.PurchaseRequestMainMapper;
import org.cmoc.modules.srm.service.*;
import org.cmoc.modules.srm.vo.BiddingMainPage;
import org.cmoc.modules.system.entity.PurchaseOrderMain;
import org.cmoc.modules.system.mapper.PurchaseOrderMainMapper;
import org.cmoc.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @Description: project progress
 * @Author: atom
 * @Date: 2023-05-11
 * @Version: V1.0
 */
@Api(tags = "projProgress")
@RestController
@RequestMapping("/srm/projProgress")
@Slf4j
public class ProjProgressController {

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private IPurchaseRequestMainService purchaseRequestMainService;

    @Autowired
    private IInquiryListService inquiryListService;

    @Autowired
    private InquiryRecordMapper inquiryRecordMapper;

    @Autowired
    private PurchaseRequestDetailMapper purchaseRequestDetailMapper;

    @Autowired
    private PurchaseRequestMainMapper purchaseRequestMainMapper;

    @Autowired
    private IBiddingMainService biddingMainService;

    @Autowired
    private IApproveRecordService iApproveRecordService;

    @Autowired
    private IContractBaseService contractBaseService;

    @Autowired
    private PurchaseOrderMainMapper purchaseOrderMainMapper;

    @Autowired
    private IProjectCategoryService iProjectCategoryService;

    @Autowired
    private IBiddingRecordService biddingRecordService;

    @Autowired
    private IInquiryRecordService inquiryRecordService;

    /**
     * 需求pr分页列表查询
     *
     * @param purchaseRequestMain
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "purchase_request_main-分页列表查询")
    @ApiOperation(value = "purchase_request_main-分页列表查询", notes = "purchase_request_main-分页列表查询")
    @GetMapping(value = "/request_main/list")
    public Result<IPage<PurchaseRequestMain>> queryPageList(PurchaseRequestMain purchaseRequestMain,
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
            if (permission.contains("pur:all")) {
                auth = "all";
            } else if (permission.contains("pur:subject")) {
                auth = "subject";
                //当前用户属于那个主体
                String subject = loginUser.getRelTenantIds();
                auther = subject;
            } else if (permission.contains("pur:dept")) {
                auth = "dept";
                auther = deptId;
            }
        }

        purchaseRequestMain.setAuth(auth);
        purchaseRequestMain.setAuther(auther);
        purchaseRequestMain.setCreateUser(username);

        Page<PurchaseRequestMain> page = new Page<PurchaseRequestMain>(pageNo, pageSize);
        IPage<PurchaseRequestMain> pageList = purchaseRequestMainService.queryPageList(page, purchaseRequestMain);
        List<PurchaseRequestMain> pageListNew = new ArrayList<>();
        pageListNew = pageList.getRecords().stream().filter(e -> e.getCategoryId() != null && e.getCategoryId().equals(purchaseRequestMain.getCategoryId()))
                .collect(Collectors.toList());
        pageList.setRecords(pageListNew);
        return Result.OK(pageList);
    }

    @ApiOperation(value = "询价单主表-分页列表查询", notes = "询价单主表-分页列表查询")
    @GetMapping(value = "/xj/list")
    public Result<IPage<InquiryList>> queryPageList(InquiryList inquiryList,
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
            if (permission.contains("inquiry:all")) {
                auth = "all";
            } else if (permission.contains("inquiry:subject")) {
                auth = "subject";
                //当前用户属于那个主体
                String subject = loginUser.getRelTenantIds();
                auther = subject;
            } else if (permission.contains("inquiry:dept")) {
                auth = "dept";
                auther = deptId;
            }
        }

        inquiryList.setAuth(auth);
        inquiryList.setAuther(auther);

        Page<InquiryList> page = new Page<InquiryList>(pageNo, pageSize);
        IPage<InquiryList> pageList = inquiryListService.queryPageList(page, inquiryList);
        List<InquiryList> pageListNew = new ArrayList<>();
        for (InquiryList record : pageList.getRecords()) {
            LambdaQueryWrapper<InquiryRecord> query = new LambdaQueryWrapper<>();
            query.eq(InquiryRecord::getInquiryId, record.getId());
            List<InquiryRecord> inquiryRecordList = inquiryRecordMapper.selectList(query);
            for (InquiryRecord inquiryRecord : inquiryRecordList) {

                LambdaQueryWrapper<PurchaseRequestDetail> query1 = new LambdaQueryWrapper<>();
                query1.eq(PurchaseRequestDetail::getId, inquiryRecord.getToRecordId());
                List<PurchaseRequestDetail> purchaseRequestDetailList = purchaseRequestDetailMapper.selectList(query1);
                for (PurchaseRequestDetail purchaseRequestDetail : purchaseRequestDetailList) {
                    PurchaseRequestMain purchaseRequestMain = purchaseRequestMainMapper.selectById(purchaseRequestDetail.getReqId());
                    if (inquiryList.getCategoryId() != null && record.getCategoryId() != null && record.getCategoryId().equals(inquiryList.getCategoryId())){

                        List<InquiryRecord> iRList = inquiryRecordService.queryRecordList(record.getId());
                        record.setInquiryRecordList(iRList);
                        record.setReqCode(purchaseRequestMain.getReqCode());
                        pageListNew.add(record);
                    }
                }
            }
        }
        pageList.setRecords(pageListNew);
        return Result.OK(pageList);
    }

    @ApiOperation(value="招标主表-分页列表查询", notes="招标主表-分页列表查询")
    @GetMapping(value = "/zb/list")
    public Result<IPage<BiddingMain>> evaluateList(BiddingMain biddingMain,
                                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        //判断当前用户权限
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        String deptId = loginUser.getDepartIds();

        String permission = iSysUserService.fetchPermission(username);
        String auth = "owner";
        String auther = username;
        if(StringUtils.isNotEmpty(permission)){
            if(permission.contains("bidding:all")){
                auth = "all";
            }else if(permission.contains("bidding:subject")){
                auth = "subject";
                //当前用户属于那个主体
                String subject = loginUser.getRelTenantIds();
                auther = subject;
            }else if(permission.contains("bidding:dept")){
                auth = "dept";
                auther = deptId;
            }
        }

        biddingMain.setAuth(auth);
        biddingMain.setAuther(auther);
        pageSize = 100;
        Page<BiddingMain> page = new Page<BiddingMain>(pageNo, pageSize);
        IPage<BiddingMain> pageList = biddingMainService.evaluateList(page, biddingMain);
        List<BiddingMain> pageListNew = new ArrayList<>();
        for (BiddingMain record : pageList.getRecords()) {
            PurchaseRequestMain purchaseRequestMain = purchaseRequestMainMapper.selectById(record.getRequestId());
            if (biddingMain.getCategoryId() != null && record.getCategoryId() != null &&  biddingMain.getCategoryId().equals(record.getCategoryId())){
                List<BiddingSupplier> biddingRecordList = biddingRecordService.querySuppList(record.getId());
                record.setSuppList(biddingRecordList);
                record.setReqCode(purchaseRequestMain.getReqCode());
                pageListNew.add(record);
            }

        }
        pageListNew = pageListNew.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(BiddingMain::getBiddingNo))), ArrayList::new));
        if (StringUtils.isNotEmpty(biddingMain.getCategoryId())) {
            pageList.setRecords(pageListNew);
        }
        return Result.OK(pageList);
    }

    @ApiOperation(value="合同基本信息表-分页列表查询", notes="合同基本信息表-分页列表查询")
    @GetMapping(value = "/ht/list")
    public Result<IPage<ContractBase>> queryPageList(ContractBase contractBase,
                                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                     HttpServletRequest req) {
        //判断当前用户权限
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        String deptId = loginUser.getDepartIds();

        String permission = iSysUserService.fetchPermission(username);
        String auth = "owner";
        String auther = username;
        if(StringUtils.isNotEmpty(permission)){
            if(permission.contains("contract:all")){
                auth = "all";
            }else if(permission.contains("contract:subject")){
                auth = "subject";
                //当前用户属于那个主体
                String subject = loginUser.getRelTenantIds();
                auther = subject;
            }else if(permission.contains("contract:dept")){
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
        List<ContractBase> pageListNew = new ArrayList<>();
//        for (ContractBase record : pageList.getRecords()) {
//            PurchaseRequestMain purchaseRequestMain = purchaseRequestMainMapper.selectById(record.getRequestId());
//            if (purchaseRequestMain != null && purchaseRequestMain.getCategoryId() != null && purchaseRequestMain.getCategoryId().equals(contractBase.getCategoryId())){
//                pageListNew.add(record);
//            }
//        }
//        pageList.setRecords(pageListNew);
        return Result.OK(pageList);
    }

    @GetMapping(value = "/fetchCategory")
    public Result<?> fetchCategory(ProjectCategory category,
                                   HttpServletRequest req) {
        List<ProjectCategory> pageList = iProjectCategoryService.fetchCategory(category);
//        for (ProjectCategory projectCategory : pageList) {
//            projectCategory.setDisabled(null);
//        }
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param biddingMainPage
     * @return
     */
    @AutoLog(value = "招标主表-添加")
    @ApiOperation(value="招标主表-添加", notes="招标主表-添加")
    @PostMapping(value = "/zb/add")
    public Result<String> add(@RequestBody BiddingMainPage biddingMainPage) {
        BiddingMain biddingMain = new BiddingMain();
        BeanUtils.copyProperties(biddingMainPage, biddingMain);
        biddingMainService.saveMain(biddingMain, biddingMainPage.getRecordList(),biddingMainPage.getSuppList(),biddingMainPage.getTemplateList(),biddingMainPage.getTemplateList1());
        return Result.OK("添加成功！");
    }

}
