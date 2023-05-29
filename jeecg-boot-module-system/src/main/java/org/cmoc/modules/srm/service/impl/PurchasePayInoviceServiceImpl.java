package org.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.common.system.query.QueryGenerator;
import org.cmoc.common.system.vo.LoginUser;
import org.cmoc.common.util.oConvertUtils;
import org.cmoc.modules.srm.entity.*;
import org.cmoc.modules.srm.mapper.PurchasePayInoviceMapper;
import org.cmoc.modules.srm.service.*;
import org.cmoc.modules.srm.utils.JeecgEntityExcel;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 发票登记
 * @Author: jeecg-boot
 * @Date:   2022-06-20
 * @Version: V1.0
 */
@Service
public class PurchasePayInoviceServiceImpl extends ServiceImpl<PurchasePayInoviceMapper, PurchasePayInovice> implements IPurchasePayInoviceService {
    @Autowired
    private IPurchasePayInvoiceDetailService iPurchasePayInvoiceDetailService;
    @Autowired
    private IContractObjectService iContractObjectService;
    @Autowired
    private IStkIoBillEntryService iStkIoBillEntryService;
    @Autowired
    private IBasSupplierService iBasSupplierService;

    @Value("${jeecg.path.upload}")
    private String upLoadPath;
    /**
     * 发票登记
     * @param purchasePayInovice
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInvoice(PurchasePayInovice purchasePayInovice) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        Date nowTime = new Date();

        //保存发票
        String id = String.valueOf(IdWorker.getId());

        BasSupplier bs = iBasSupplierService.getById(purchasePayInovice.getSupplierId());

        purchasePayInovice.setId(id);
        purchasePayInovice.setDelFlag(CommonConstant.NO_READ_FLAG);
        purchasePayInovice.setCreateBy(username);
        purchasePayInovice.setCreateTime(nowTime);
        purchasePayInovice.setUpdateBy(username);
        purchasePayInovice.setUpdateTime(nowTime);
        purchasePayInovice.setStatus("1");
        purchasePayInovice.setSupplierId(bs.getId());
        purchasePayInovice.setSupplierName(bs.getName());

        this.save(purchasePayInovice);



        List<StkIoBillEntry> goodList = new ArrayList<>();
        List<PurchasePayInvoiceDetail> detailList = purchasePayInovice.getDetailList();
        for(PurchasePayInvoiceDetail pid : detailList){
            pid.setId(String.valueOf(IdWorker.getId()));
            pid.setInvoiceId(id);
            pid.setDelFlag(CommonConstant.NO_READ_FLAG);
            pid.setCreateBy(username);
            pid.setCreateTime(nowTime);
            pid.setUpdateBy(username);
            pid.setUpdateTime(nowTime);

            StkIoBillEntry sibe = iStkIoBillEntryService.getById(pid.getBillDetailId());
            sibe.setInvoiceQty(sibe.getInvoiceQty().add(pid.getQty()));
            goodList.add(sibe);
        }
        iPurchasePayInvoiceDetailService.saveOrUpdateBatch(detailList);
        iStkIoBillEntryService.updateBatchById(goodList);
    }

    /**
     * 发票登记
     * @param purchasePayInovice
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editInvoice(PurchasePayInovice purchasePayInovice) {

    }

    /**
     * 发票登记
     * @param purchasePayInovice
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void draftInvoice(PurchasePayInovice purchasePayInovice) {

    }

    /**
     * 开票明细
     * @param id
     * @return
     */
    @Override
    public List<ContractObject> queryPurPayInvoiceDetailByMainId(String id) {
        return baseMapper.queryPurPayInvoiceDetailByMainId(id);
    }

    /**
     * 通过合同id查询
     * @param id
     * @return
     */
    @Override
    public List<PurchasePayInovice> queryPurPayInvoiceByContractId(String id) {
        return baseMapper.queryPurPayInvoiceByContractId(id);
    }

    /**
     * 发票列表
     * @param page
     * @param purchasePayInovice
     * @return
     */
    @Override
    public IPage<PurchasePayInovice> queryPageList(Page<PurchasePayInovice> page, PurchasePayInovice purchasePayInovice) {
        return baseMapper.queryPageList(page,purchasePayInovice);
    }

    /**
     * 开票金额
     * @param invoice
     * @return
     */
    @Override
    public List<PurchasePayInovice> fetchInvoiceByProjId(PurchasePayInovice invoice) {
        return baseMapper.fetchInvoiceByProjId(invoice);
    }

    /**
     * 设备类型开票
     * @param bomChild
     * @return
     */
    @Override
    public List<PurchasePayInovice> fetchAmountByModel(ProjectBomChild bomChild) {
        return baseMapper.fetchAmountByModel(bomChild);
    }

    /**
     * 资金分类开票
     * @param category
     * @return
     */
    @Override
    public List<PurchasePayInovice> fetchAmountByCategory(ProjectCategory category) {
        return baseMapper.fetchAmountByCategory(category);
    }

    /**
     * 导出
     * @param request
     * @param purchasePayInovice
     * @param clazz
     * @param title
     * @return
     */
    @Override
    public ModelAndView exportXls(HttpServletRequest request, PurchasePayInovice purchasePayInovice, Class<PurchasePayInovice> clazz, String title) {

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // Step.2 获取导出数据
        List<PurchasePayInovice> exportList = baseMapper.queryList(purchasePayInovice);;

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcel());
        //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.FILE_NAME, title);
        mv.addObject(NormalExcelConstants.CLASS, clazz);
        //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
        ExportParams exportParams=new ExportParams(title, "导出人:" + sysUser.getRealname(), title);
        exportParams.setImageBasePath(upLoadPath);
        //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
        mv.addObject(NormalExcelConstants.PARAMS,exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        mv.addObject(NormalExcelConstants.EXPORT_FIELDS, request.getParameter("field"));
        return mv;
    }
}
