package com.cmoc.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmoc.modules.srm.entity.ContractObject;
import com.cmoc.modules.srm.entity.ProjectBomChild;
import com.cmoc.modules.srm.entity.ProjectCategory;
import com.cmoc.modules.srm.entity.PurchasePayInovice;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 发票登记
 * @Author: jeecg-boot
 * @Date:   2022-06-20
 * @Version: V1.0
 */
public interface IPurchasePayInoviceService extends IService<PurchasePayInovice> {
    /**
     * 发票登记
     * @param purchasePayInovice
     */
    void saveInvoice(PurchasePayInovice purchasePayInovice);

    /**
     * 发票登记
     * @param purchasePayInovice
     */
    void editInvoice(PurchasePayInovice purchasePayInovice);

    /**
     * 发票登记
     * @param purchasePayInovice
     */
    void draftInvoice(PurchasePayInovice purchasePayInovice);

    /**
     * 开票明细
     * @param id
     * @return
     */
    List<ContractObject> queryPurPayInvoiceDetailByMainId(String id);

    /**
     * 通过合同id查询
     * @param id
     * @return
     */
    List<PurchasePayInovice> queryPurPayInvoiceByContractId(String id);

    /**
     * 发票列表
     * @param page
     * @param purchasePayInovice
     * @return
     */
    IPage<PurchasePayInovice> queryPageList(Page<PurchasePayInovice> page, PurchasePayInovice purchasePayInovice);

    /**
     * 项目开票
     * @param invoice
     * @return
     */
    List<PurchasePayInovice> fetchInvoiceByProjId(PurchasePayInovice invoice);

    /**
     * 设备类型开票
     * @param bomChild
     * @return
     */
    List<PurchasePayInovice> fetchAmountByModel(ProjectBomChild bomChild);

    /**
     * 资金分类开票
     * @param category
     * @return
     */
    List<PurchasePayInovice> fetchAmountByCategory(ProjectCategory category);

    /**
     * 发票列表
     * @param request
     * @param purchasePayInovice
     * @param clazz
     * @param title
     * @return
     */
    ModelAndView exportXls(HttpServletRequest request, PurchasePayInovice purchasePayInovice, Class<PurchasePayInovice> clazz, String title);
}
