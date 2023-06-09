package org.cmoc.modules.srm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.cmoc.modules.srm.entity.ContractObjectQty;
import org.cmoc.modules.srm.entity.StkIoBill;
import org.cmoc.modules.srm.entity.StkIoBillEntry;

import java.util.List;

/**
 * @Description: 入库单
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
public interface StkIoBillMapper extends BaseMapper<StkIoBill> {
    /**
     * 设备到厂数统计
     * @param entry
     * @return
     */
    StkIoBillEntry fetchArrivalQty(@Param("query") StkIoBillEntry entry);

    /**
     * 库存
     * @param page
     * @param stkIoBill
     * @return
     */
    IPage<StkIoBillEntry> queryDetailPageList(Page<StkIoBillEntry> page, @Param("query") StkIoBillEntry stkIoBill);

    /**
     * 分页
     * @param page
     * @param stkIoBill
     * @return
     */
    IPage<StkIoBill> queryPageList(Page<StkIoBill> page, @Param("query") StkIoBill stkIoBill);

    /**
     * @param sk
     * @return ContractObjectQty
     * @author Kevin.Wang
     * @date : 2023/2/13 14:45
     **/
    ContractObjectQty queryOtherDetailsById(@Param("query") StkIoBill sk);

    /**
     * 导出
     * @param stkIoBill
     * @return
     */
    List<StkIoBill> exportXls(@Param("query") StkIoBill stkIoBill);

    /**
     *
     * @param ids
     * @return
     */
    List<ContractObjectQty> queryOtherDetailsByIds(@Param("ids") List<String> ids);

    /**
     * 入库明细
     * @param page
     * @param stkIoBill
     * @return
     */
    IPage<StkIoBillEntry> fetchDetailPageList(Page<StkIoBill> page, @Param("query") StkIoBill stkIoBill);
}
