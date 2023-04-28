package org.jeecg.modules.srm.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.srm.entity.ContractObjectQty;
import org.jeecg.modules.srm.entity.PurPayApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.srm.entity.PurPayApplyDetail;

/**
 * @Description: 付款申请
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface PurPayApplyMapper extends BaseMapper<PurPayApply> {
    /**
     * 付款申请
     * @param page
     * @param purPayApply
     * @return
     */
    IPage<PurPayApply> queryPageList(Page<PurPayApply> page, @Param("query") PurPayApply purPayApply);

    /**
     * 币种汇总
     * @param purPayApply
     * @return
     */
    List<PurPayApply> getTotalAmountByCurrency(@Param("query") PurPayApply purPayApply);

    /**
     * 付款申请明细
     * @param id
     * @return
     */
    List<ContractObjectQty> getDetailList(@Param("id") String id);

    /**
     * 获取支付明细
     * @param ids
     * @return
     */
    List<PurPayApplyDetail> fetchPayDetailList(@Param("ids") List<String> ids);

    /**
     * 应付管理导出
     * @param purPayApply
     * @return
     */
    List<PurPayApply> queryList(@Param("query") PurPayApply purPayApply);

    /**
     * 累计请款金额
     * @param contractId
     * @param ids
     * @return
     */
    List<PurPayApply> fetchHasPayDetailList(@Param("id") String contractId);
}
