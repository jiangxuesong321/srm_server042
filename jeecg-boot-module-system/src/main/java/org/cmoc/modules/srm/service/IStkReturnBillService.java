package org.cmoc.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cmoc.modules.srm.entity.StkIoBillEntry;
import org.cmoc.modules.srm.entity.StkReturnBill;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: stk_return_bill
 * @Author: jeecg-boot
 * @Date:   2022-10-10
 * @Version: V1.0
 */
public interface IStkReturnBillService extends IService<StkReturnBill> {
    /**
     * 退货
     * @param stkReturnBill
     */
    void saveMain(StkReturnBill stkReturnBill);

    /**
     * 编辑
     * @param stkReturnBill
     */
    void editMain(StkReturnBill stkReturnBill);

    /**
     * 明细
     * @param id
     * @return
     */
    List<StkIoBillEntry> queryDetailListByMainId(String id);

    /**
     * 分页
     * @param page
     * @param stkReturnBill
     * @return
     */
    IPage<StkReturnBill> queryPageList(Page<StkReturnBill> page, StkReturnBill stkReturnBill);
}
