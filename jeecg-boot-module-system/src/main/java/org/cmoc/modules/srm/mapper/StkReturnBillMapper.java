package org.cmoc.modules.srm.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.cmoc.modules.srm.entity.StkIoBillEntry;
import org.cmoc.modules.srm.entity.StkReturnBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: stk_return_bill
 * @Author: jeecg-boot
 * @Date:   2022-10-10
 * @Version: V1.0
 */
public interface StkReturnBillMapper extends BaseMapper<StkReturnBill> {
    /**
     * 明细
     * @param id
     * @return
     */
    List<StkIoBillEntry> queryDetailListByMainId(@Param("id") String id);

    /**
     * 分页
     * @param page
     * @param stkReturnBill
     * @return
     */
    IPage<StkReturnBill> queryPageList(Page<StkReturnBill> page, @Param("query") StkReturnBill stkReturnBill);
}
