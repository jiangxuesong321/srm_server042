package com.cmoc.modules.srm.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.cmoc.modules.srm.entity.ContractObject;
import com.cmoc.modules.srm.entity.ProjectBomChild;
import com.cmoc.modules.srm.entity.ProjectCategory;
import com.cmoc.modules.srm.entity.PurchasePayInovice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 发票登记
 * @Author: jeecg-boot
 * @Date:   2022-06-20
 * @Version: V1.0
 */
public interface PurchasePayInoviceMapper extends BaseMapper<PurchasePayInovice> {
    /**
     * 开票明细
     * @param id
     * @return
     */
    List<ContractObject> queryPurPayInvoiceDetailByMainId(@Param("id") String id);

    /**
     * 通过合同id查询
     * @param id
     * @return
     */
    List<PurchasePayInovice> queryPurPayInvoiceByContractId(@Param("id") String id);

    /**
     * 发票列表
     * @param page
     * @param purchasePayInovice
     * @return
     */
    IPage<PurchasePayInovice> queryPageList(Page<PurchasePayInovice> page, @Param("query") PurchasePayInovice purchasePayInovice);

    /**
     * 开票金额
     * @param invoice
     * @return
     */
    List<PurchasePayInovice> fetchInvoiceByProjId(@Param("query") PurchasePayInovice invoice);

    /**
     * 设备类型开票
     * @param bomChild
     * @return
     */
    List<PurchasePayInovice> fetchAmountByModel(@Param("query") ProjectBomChild bomChild);

    /**
     * 资金分类
     * @param category
     * @return
     */
    List<PurchasePayInovice> fetchAmountByCategory(@Param("query") ProjectCategory category);

    /**
     * 导出
     * @param purchasePayInovice
     * @return
     */
    List<PurchasePayInovice> queryList(@Param("query") PurchasePayInovice purchasePayInovice);
}
