package org.jeecg.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.srm.entity.*;
import org.jeecg.modules.srm.mapper.ContractBaseMapper;
import org.jeecg.modules.srm.mapper.ProjBaseMapper;
import org.jeecg.modules.srm.mapper.ProjectBomChildMapper;
import org.jeecg.modules.srm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: project_bom_child
 * @Author: jeecg-boot
 * @Date:   2022-09-23
 * @Version: V1.0
 */
@Service
public class ProjectBomChildServiceImpl extends ServiceImpl<ProjectBomChildMapper, ProjectBomChild> implements IProjectBomChildService {
    @Autowired
    private IPurchaseRequestDetailService iPurchaseRequestDetailService;
    @Autowired
    private IContractObjectService iContractObjectService;
    @Autowired
    private IPurPayPlanDetailService iPurPayPlanDetailService;
    @Autowired
    private IPurchasePayInoviceService iPurchasePayInoviceService;
    /**
     * 项目子项进度
     * @param bomChild
     * @return
     */
    @Override
    public List<Map<String, Object>> fetchChildProgress1(ProjectBomChild bomChild) {
        List<Map<String,Object>> mapList = new ArrayList<>();
        List<ProjectBomRelation> diffList = baseMapper.fetchDiffCapacity(bomChild);
        List<ProjectBomChild> bomChild1List = baseMapper.fetchBomChildList(bomChild);

        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal contractAmount = BigDecimal.ZERO;

        for(ProjectBomRelation pbr : diffList){
            contractAmount = contractAmount.add(pbr.getBudgetAmount());
        }
        for(ProjectBomChild pbc : bomChild1List){
            totalAmount = totalAmount.add(pbc.getBudgetAmount());
        }
        Map<String,Object> map = new HashMap<>();
        if(totalAmount.compareTo(BigDecimal.ZERO) == 0){
            map.put("moneyCapacity",totalAmount);
        }else {
            BigDecimal moneyCapacity = contractAmount.divide(totalAmount,4,BigDecimal.ROUND_HALF_UP);
            map.put("moneyCapacity",moneyCapacity.multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP));
        }


        mapList.add(map);
        return mapList;
    }
    /**
     * 项目子项进度
     * @param bomChild
     * @return
     */
    @Override
    public List<Map<String, Object>> fetchChildProgress(ProjectBomChild bomChild) {
        List<Map<String,Object>> mapList = new ArrayList<>();
        //子项总产能
        List<ProjectBomRelation> bomEntityList = baseMapper.fetchTotalCapacity(bomChild);
        //Neck产能 = 设备产能与合同签订时候的产能的差额
        List<ProjectBomRelation> diffList = baseMapper.fetchDiffCapacity(bomChild);
        if(diffList == null || diffList.size() == 0){
            diffList = new ArrayList<>();
        }
        //形象进度、Neck产能
        List<ProjectBomChild> childList = this.list(Wrappers.<ProjectBomChild>query().lambda().
                eq(ProjectBomChild :: getDelFlag,CommonConstant.DEL_FLAG_0));
        //项目总投
        List<ProjectBomChild> bomChild1List = baseMapper.fetchBomChildList(bomChild);


        for(ProjectBomRelation bomEntity : bomEntityList){
            Map<String,Object> map = new HashMap<>();

            map.put("projId",bomEntity.getProjId());
            map.put("model",bomEntity.getModel());
            map.put("name",bomEntity.getName());
            map.put("text",bomEntity.getText());

            BigDecimal budgetAmount = BigDecimal.ZERO;
            for(ProjectBomRelation diff : diffList){
                if(bomEntity.getProjId().equals(diff.getProjId()) && bomEntity.getModel().equals(diff.getModel())){
                    budgetAmount = diff.getBudgetAmount();
                    break;
                }
            }
            BigDecimal prodCapacity = BigDecimal.ZERO;
            BigDecimal neck = BigDecimal.ZERO;
            BigDecimal capacity = BigDecimal.ZERO;
            //形象进度
            BigDecimal iprogress = BigDecimal.ZERO;
            for(ProjectBomChild child : childList){
                if(bomEntity.getProjId().equals(child.getProjectId()) && bomEntity.getModel().equals(child.getModel())){
                    iprogress = child.getIprogress();
                    neck = child.getNeck();
                    capacity = child.getCapacity();
                    break;
                }
            }
            map.put("imageCapacity",iprogress);
            if(capacity != null && capacity.compareTo(BigDecimal.ZERO) == 1){
                prodCapacity = neck.divide(capacity,2,BigDecimal.ROUND_HALF_UP);
            }
            map.put("prodCapacity",prodCapacity.multiply(new BigDecimal(100)));

            //项目总投
            BigDecimal totalAmount = BigDecimal.ZERO;
            for(ProjectBomChild bomChild1 : bomChild1List){
                if(bomEntity.getProjId().equals(bomChild1.getProjectId()) && bomEntity.getModel().equals(bomChild1.getModel())){
                    totalAmount = bomChild1.getBudgetAmount();
                    break;
                }
            }

            BigDecimal contractAmount = budgetAmount == null ? BigDecimal.ZERO : budgetAmount;
            //资金进度 = 项目已投/项目总投
            BigDecimal moneyCapacity = contractAmount.divide(totalAmount,4,BigDecimal.ROUND_HALF_UP);
            map.put("moneyCapacity",moneyCapacity.multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP));

            mapList.add(map);
        }

        return mapList;
    }

    /**
     * 项目子类资金统计
     * @param bomChild
     * @return
     */
    @Override
    public List<ProjectBomChild> fetchChildAmount(ProjectBomChild bomChild) {
        List<ProjectBomChild> pageList = this.list(Wrappers.<ProjectBomChild>query().lambda().
                eq(ProjectBomChild :: getDelFlag,CommonConstant.DEL_FLAG_0).
                eq(ProjectBomChild :: getProjectId,bomChild.getProjectId()));

        //执行预算
        List<PurchaseRequestDetail> reqList = iPurchaseRequestDetailService.fetchAmountByModel(bomChild);
        //合同金额
        List<ContractObject> objList = iContractObjectService.fetchAmountByModel(bomChild);
        //已付金额
        List<PurPayPlanDetail> payList = iPurPayPlanDetailService.fetchAmountByModel(bomChild);
        //已开票金额
        List<PurchasePayInovice> invoiceList = iPurchasePayInoviceService.fetchAmountByModel(bomChild);

        for(ProjectBomChild bom : pageList){
            bom.setReqAmount(BigDecimal.ZERO);
            bom.setContractAmount(BigDecimal.ZERO);
            bom.setPayAmount(BigDecimal.ZERO);

            for(PurchaseRequestDetail req : reqList){
                if(bom.getModel().equals(req.getModel())){
                    bom.setReqAmount(req.getOrderAmountTax());
                    break;
                }
            }

            for(ContractObject obj : objList){
                if(bom.getModel().equals(obj.getModel())){
                    bom.setContractAmount(obj.getContractAmountTaxLocal());
                    break;
                }
            }

            for(PurPayPlanDetail pay : payList){
                if(bom.getModel().equals(pay.getModel())){
                    bom.setPayAmount(pay.getPayAmount());
                    break;
                }
            }

            for(PurchasePayInovice ppi : invoiceList){
                if(bom.getModel().equals(ppi.getModel())){
                    bom.setInvoiceAmount(ppi.getInvoiceAmountTaxLocal());
                    break;
                }
            }

            //可用金额
            bom.setUsedAmount(bom.getBudgetAmount().subtract(bom.getContractAmount()));
        }

        return pageList;
    }


}
