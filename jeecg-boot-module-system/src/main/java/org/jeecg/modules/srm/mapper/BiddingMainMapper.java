package org.jeecg.modules.srm.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.srm.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.entity.SysUser;

/**
 * @Description: 招标主表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface BiddingMainMapper extends BaseMapper<BiddingMain> {
    /**
     * 招标列表
     * @param page
     * @param biddingMain
     * @return
     */
    IPage<BiddingMain> queryPageList(Page<BiddingMain> page, @Param("query") BiddingMain biddingMain);

    /**
     * 评标管理
     * @param page
     * @param biddingMain
     * @return
     */
    IPage<BiddingMain> evaluateList(Page<BiddingMain> page, @Param("query") BiddingMain biddingMain);

    /**
     * 评标明细
     * @param biddingMain
     * @return
     */
    List<BiddingRecord> fetchRecordList(@Param("query") BiddingMain biddingMain);

    /**
     * 招标列表
     * @param biddingMain
     * @return
     */
    List<BiddingRecord> fetchRecordTwoList(@Param("query") BiddingMain biddingMain);

    /**
     * 中标供应商信息
     * @param page
     * @return
     */
    BasSupplier getSuppInfo(@Param("query") BiddingMain page);

    /**
     * 报价信息
     * @param param
     * @return
     */
    List<BiddingMain> fetchQuote(@Param("query") BiddingMain param);

    /**
     * 交期排名
     * @param id
     * @return
     */
    List<String> getLeadTimeRank(@Param("id") String id);

    /**
     * 报价排名
     * @param id
     * @return
     */
    List<String> getPriceRank(@Param("id") String id);

    /**
     * 招标列表
     * @param page
     * @param biddingMain
     * @return
     */
    IPage<BiddingMain> pageList(Page<BiddingMain> page, @Param("query") BiddingMain biddingMain);

    /**
     * 获取专家信息
     * @param id
     * @return
     */
    List<SysUser> fetchProFessionals(@Param("id") String id);

    /**
     * 查询评标人员
     * @param id
     * @return
     */
    List<BiddingProfessionals> fetchHasProfessionals(@Param("id") String id);

    /**
     * 获取评标记录
     * @param biddingProfessionals
     * @return
     */
    List<BiddingRecordToProfessionals> fetchRecordToProfessionals(@Param("query") BiddingProfessionals biddingProfessionals);

    /**
     * 招标供应商
     * @param id
     * @return
     */
    List<BasSupplier> fetchSuppList(@Param("id") String id);

    /**
     * 参与评标人员
     * @param id
     * @return
     */
    List<String> fetchProFessionalsEmail(@Param("id") String id);
}
