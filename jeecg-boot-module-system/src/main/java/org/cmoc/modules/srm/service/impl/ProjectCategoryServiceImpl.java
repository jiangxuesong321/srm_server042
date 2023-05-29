package org.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.common.system.vo.DictModelMany;
import org.cmoc.common.util.oConvertUtils;
import org.cmoc.modules.srm.entity.*;
import org.cmoc.modules.srm.mapper.ProjectCategoryMapper;
import org.cmoc.modules.srm.service.*;
import org.cmoc.modules.system.model.DepartIdModel;
import org.cmoc.modules.system.model.SysDepartTreeModel;
import org.cmoc.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: project_category
 * @Author: jeecg-boot
 * @Date:   2022-09-23
 * @Version: V1.0
 */
@Service
public class ProjectCategoryServiceImpl extends ServiceImpl<ProjectCategoryMapper, ProjectCategory> implements IProjectCategoryService {
    @Autowired
    private IProjectBomRelationService iProjectBomRelationService;
    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private IPurchaseRequestDetailService iPurchaseRequestDetailService;
    @Autowired
    private IContractObjectService iContractObjectService;
    @Autowired
    private IPurPayPlanDetailService iPurPayPlanDetailService;
    @Autowired
    private IPurchasePayInoviceService iPurchasePayInoviceService;
    /**
     * 分类
     * @param category
     * @return
     */
    @Override
    public List<ProjectCategory> fetchCategory(ProjectCategory category) {
        List<ProjectCategory> categoryList = this.list(Wrappers.<ProjectCategory>query().lambda().
                eq(ProjectCategory :: getDelFlag, CommonConstant.DEL_FLAG_0).
                eq(ProjectCategory :: getProjectId,category.getProjectId()));

        List<ProjectCategory> pageList = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        if(categoryList != null && categoryList.size() > 0){
            for(ProjectCategory pc : categoryList){
                if(pc.getIsLast() == 1 && pc.getIsEqp() == 1){
                    ids.add(pc.getId());
                }
            }
            //末级设备清单
            List<String> codeList = new ArrayList<>();
            codeList.add("unit");
            codeList.add("model");
            List<DictModelMany> dictList = iSysDictService.getDictItemsByCodeList(codeList);
            Map<String,String> map = dictList.stream().collect(Collectors.toMap(DictModelMany::getValue, DictModelMany::getText));

            List<ProjectBomRelation> bomList = new ArrayList<>();
            if(ids != null && ids.size() > 0){
                bomList = iProjectBomRelationService.fetchBomList(ids);
            }
            for(ProjectCategory pc : categoryList){
                if(pc.getIsLast() == 1 && pc.getIsEqp() == 1){
                    List<ProjectBomRelation> prodList = new ArrayList<>();
                    for(ProjectBomRelation bom : bomList){
                        if(pc.getId().equals(bom.getCategoryId())){
                            //设备类型
                            String model = map.get(bom.getModel());
                            bom.setModel_dictText(model);
                            //设备单位
                            String unit = map.get(bom.getUnitId());
                            bom.setUnitId_dictText(unit);

                            bom.setId(bom.getMaterialId());
                            prodList.add(bom);
                        }
                    }
                    pc.setProdList(prodList);
                }
            }
            pageList = findChildren(categoryList);
        }
        return pageList;
    }

    /**
     * 末级分类可选择
     * @param category
     * @return
     */
    @Override
    public List<ProjectCategory> fetchLastCategory(ProjectCategory category) {
        List<ProjectCategory> categoryList = null;
        if(StringUtils.isNotEmpty(category.getProjectId())){
            categoryList = this.list(Wrappers.<ProjectCategory>query().lambda().
                    eq(ProjectCategory :: getDelFlag, CommonConstant.DEL_FLAG_0).
                    eq(ProjectCategory :: getProjectId,category.getProjectId()));
        }else{
            categoryList = this.list(Wrappers.<ProjectCategory>query().lambda().
                    eq(ProjectCategory :: getDelFlag, CommonConstant.DEL_FLAG_0));
        }
        if(categoryList != null && categoryList.size() > 0){
            for(ProjectCategory pc : categoryList){
                if(pc.getIsLast() == 0 && category.getDisabled()){
                    pc.setDisabled(true);
                }
            }
            List<ProjectCategory> pageList = findChildren(categoryList);
            return pageList;
        }else{
            return new ArrayList<>();
        }
    }

    /**
     * 分类资金统计
     * @param category
     * @return
     */
    @Override
    public List<ProjectCategory> fetchCategoryToAmount(ProjectCategory category) {
        List<ProjectCategory> categoryList = this.list(Wrappers.<ProjectCategory>query().lambda().
                eq(ProjectCategory :: getDelFlag, CommonConstant.DEL_FLAG_0).
                eq(ProjectCategory :: getProjectId,category.getProjectId()));

        //执行预算
//        List<PurchaseRequestDetail> reqList = iPurchaseRequestDetailService.fetchAmountByCategory(category);
        //合同金额
        List<ContractObject> objList = iContractObjectService.fetchAmountByCategory(category);
        //已付金额
        List<PurPayPlanDetail> payList = iPurPayPlanDetailService.fetchAmountByCategory(category);
        //已开票金额
        List<PurchasePayInovice> invoiceList = iPurchasePayInoviceService.fetchAmountByCategory(category);

        //统计总额
        BigDecimal budgetAmount = BigDecimal.ZERO;
        BigDecimal contractAmount = BigDecimal.ZERO;
        BigDecimal payAmount = BigDecimal.ZERO;
        BigDecimal invoiceAmount = BigDecimal.ZERO;

        for(ProjectCategory pc : categoryList){
//            for(PurchaseRequestDetail req : reqList){
//                if(pc.getId().equals(req.getCategoryId())){
//                    pc.setReqAmount(req.getOrderAmountTax());
//                    break;
//                }
//            }
            pc.setContractAmount(BigDecimal.ZERO);
            pc.setPayAmount(BigDecimal.ZERO);
            pc.setInvoiceAmount(BigDecimal.ZERO);

            pc.setBudgetAmountLPercent(BigDecimal.ZERO);
            pc.setContractAmountLPercent(BigDecimal.ZERO);
            pc.setPayAmountLPercent(BigDecimal.ZERO);
            pc.setInvoiceAmountLPercent(BigDecimal.ZERO);

            pc.setContractAmountHPercent(BigDecimal.ZERO);
            pc.setPayAmountHPercent(BigDecimal.ZERO);
            pc.setInvoiceAmountHPercent(BigDecimal.ZERO);
            for(ContractObject obj : objList){
                if(pc.getId().equals(obj.getCategoryId())){
                    pc.setContractAmount(obj.getContractAmountTaxLocal());
                    break;
                }
            }

            for(PurPayPlanDetail pay : payList){
                if(pc.getId().equals(pay.getCategoryId())){
                    pc.setPayAmount(pay.getPayAmount());
                    break;
                }
            }
            for(PurchasePayInovice ppi : invoiceList){
                if(pc.getId().equals(ppi.getCategoryId())){
                    pc.setInvoiceAmount(ppi.getInvoiceAmountTaxLocal());
                    break;
                }
            }
            if ("0".equals(pc.getParentId())) {
                budgetAmount = budgetAmount.add(pc.getBudgetAmount());
            }
            contractAmount = contractAmount.add(pc.getContractAmount());
            payAmount = payAmount.add(pc.getPayAmount());
            invoiceAmount = invoiceAmount.add(pc.getInvoiceAmount());
        }

        List<ProjectCategory> pageList = new ArrayList<>();
        if(categoryList != null && categoryList.size() > 0){
            //构建总额,计算占比用
            ProjectCategory pc = new ProjectCategory();
            pc.setId("0");
            pc.setParentId("-1");
            pc.setInvoiceAmount(invoiceAmount);
            pc.setPayAmount(payAmount);
            pc.setContractAmount(contractAmount);
            pc.setBudgetAmount(budgetAmount);
            categoryList.add(pc);

            pageList = findChildren(categoryList);
        }
        return pageList;
    }

    /**
     * 递归树
     * @param categoryList
     */
    private static List<ProjectCategory> findChildren(List<ProjectCategory> categoryList) {
        List<ProjectCategory> pageList = new ArrayList<>();
        //树插槽
        Map<String,String> map = new HashMap<>();
        map.put("title","custom");

        //获取总额
        ProjectCategory amount = categoryList.get(categoryList.size() - 1);

        for (int i = 0; i < categoryList.size(); i++) {
            ProjectCategory category = categoryList.get(i);
            if ("0".equals(category.getParentId())) {
                category.setTitle(category.getName());
                category.setKey(category.getId());
                category.setValue(category.getId());
                category.setPname("");
                category.setScopedSlots(map);
                List<ProjectCategory> childList = getChild(category,categoryList);
                //循环 计算 执行预算、合同金额、已付金额
                if(childList != null && childList.size() > 0){
                    BigDecimal reqAmount = BigDecimal.ZERO;
                    BigDecimal contractAmount = BigDecimal.ZERO;
                    BigDecimal payAmount = BigDecimal.ZERO;
                    BigDecimal invoiceAmount = BigDecimal.ZERO;

                    for(ProjectCategory pc :childList ){
                        if(pc.getReqAmount() != null){
                            reqAmount = reqAmount.add(pc.getReqAmount());
                        }
                        if(pc.getContractAmount() != null){
                            contractAmount = contractAmount.add(pc.getContractAmount());
                        }
                        if(pc.getPayAmount() != null){
                            payAmount = payAmount.add(pc.getPayAmount());
                        }
                        if(pc.getInvoiceAmount() != null){
                            invoiceAmount = invoiceAmount.add(pc.getInvoiceAmount());
                        }
                    }

                    category.setReqAmount(reqAmount);
                    category.setContractAmount(contractAmount);
                    category.setPayAmount(payAmount);
                    category.setInvoiceAmount(invoiceAmount);

                }


                if(amount != null && "-1".equals(amount.getParentId())){
                    //计算列占比
                    if(amount.getBudgetAmount() != null && amount.getBudgetAmount().compareTo(BigDecimal.ZERO) == 1){
                        BigDecimal budgetAmountLPercent = category.getBudgetAmount().divide(amount.getBudgetAmount(),4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                        category.setBudgetAmountLPercent(budgetAmountLPercent);
                    }
                    if(amount.getContractAmount() != null && amount.getContractAmount().compareTo(BigDecimal.ZERO) == 1){
                        BigDecimal contractAmountLPercent = category.getContractAmount().divide(amount.getContractAmount(),4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                        category.setContractAmountLPercent(contractAmountLPercent);
                    }
                    if(amount.getPayAmount() != null && amount.getPayAmount().compareTo(BigDecimal.ZERO) == 1){
                        BigDecimal payAmountLPercent = category.getPayAmount().divide(amount.getPayAmount(),4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                        category.setPayAmountLPercent(payAmountLPercent);
                    }
                    if(amount.getInvoiceAmount() != null && amount.getInvoiceAmount().compareTo(BigDecimal.ZERO) == 1){
                        BigDecimal invoiceAmountLPercent = category.getInvoiceAmount().divide(amount.getInvoiceAmount(),4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                        category.setInvoiceAmountLPercent(invoiceAmountLPercent);
                    }

                    //计算行占比
                    if(category.getBudgetAmount() != null && category.getBudgetAmount().compareTo(BigDecimal.ZERO) == 1){
                        BigDecimal contractAmountHPercent = category.getContractAmount().divide(category.getBudgetAmount(),4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                        category.setContractAmountHPercent(contractAmountHPercent);
                    }
                    if(category.getContractAmount() != null && category.getContractAmount().compareTo(BigDecimal.ZERO) == 1){
                        BigDecimal payAmountHPercent = category.getPayAmount().divide(category.getContractAmount(),4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                        category.setPayAmountHPercent(payAmountHPercent);
                    }
                    if(category.getContractAmount() != null && category.getContractAmount().compareTo(BigDecimal.ZERO) == 1){
                        BigDecimal invoiceAmountHPercent = category.getInvoiceAmount().divide(category.getContractAmount(),4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                        category.setInvoiceAmountHPercent(invoiceAmountHPercent);
                    }
                }

                category.setChildren(childList);
                pageList.add(category);
            }
        }
        return pageList;
    }

    private static List<ProjectCategory> getChild(ProjectCategory category, List<ProjectCategory> records) {
        //树插槽
        Map<String,String> map = new HashMap<>();
        map.put("title","custom");

        List<ProjectCategory> childrenList = new ArrayList<>();
        for (ProjectCategory pc : records) {
            if (category.getId().equals(pc.getParentId())) {
                pc.setTitle(pc.getName());
                pc.setKey(pc.getId());
                pc.setValue(pc.getId());
                pc.setPname(category.getName());
                pc.setScopedSlots(map);

                List<ProjectCategory> childList = getChild(pc, records);
                //循环 计算 执行预算、合同金额、已付金额
                if(childList != null && childList.size() > 0 && 0 == pc.getIsLast()){
                    BigDecimal reqAmount = BigDecimal.ZERO;
                    BigDecimal contractAmount = BigDecimal.ZERO;
                    BigDecimal payAmount = BigDecimal.ZERO;
                    BigDecimal invoiceAmount = BigDecimal.ZERO;
                    for(ProjectCategory cd :childList ){
                        if(cd.getReqAmount() != null){
                            reqAmount = reqAmount.add(cd.getReqAmount());
                        }
                        if(cd.getContractAmount() != null){
                            contractAmount = contractAmount.add(cd.getContractAmount());
                        }
                        if(cd.getPayAmount() != null){
                            payAmount = payAmount.add(cd.getPayAmount());
                        }
                        if(cd.getInvoiceAmount() != null){
                            invoiceAmount = invoiceAmount.add(cd.getInvoiceAmount());
                        }
                    }
                    pc.setReqAmount(reqAmount);
                    pc.setContractAmount(contractAmount);
                    pc.setPayAmount(payAmount);
                    pc.setInvoiceAmount(invoiceAmount);
                }
                pc.setChildren(childList);
                childrenList.add(pc);
            }
        }
        return childrenList;
    }

}
