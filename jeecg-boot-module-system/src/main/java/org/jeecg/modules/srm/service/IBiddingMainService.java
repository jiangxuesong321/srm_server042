package org.jeecg.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.srm.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.srm.vo.BiddingTemplatePage;
import org.jeecg.modules.system.entity.SysUser;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 招标主表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface IBiddingMainService extends IService<BiddingMain> {

	/**
	 * 添加一对多
	 *
	 * @param biddingMain
	 * @param biddingRecordList
	 * @param biddingSupplierList
	 * @param biddingProfessionalsList
	 */
	public void saveMain(BiddingMain biddingMain,List<BiddingRecord> biddingRecordList,List<BiddingSupplier> biddingSupplierList,List<BiddingProfessionals> biddingProfessionalsList,List<BiddingProfessionals> biddingProfessionalsList1) ;
	
	/**
	 * 修改一对多
	 *
   * @param biddingMain
   * @param biddingRecordList
   * @param biddingSupplierList
   * @param biddingProfessionalsList
	 */
	public void updateMain(BiddingMain biddingMain,List<BiddingRecord> biddingRecordList,List<BiddingSupplier> biddingSupplierList,List<BiddingProfessionals> biddingProfessionalsList,List<BiddingProfessionals> biddingProfessionalsList1);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	/**
	 * 招标列表
	 * @param page
	 * @param biddingMain
	 * @return
	 */
	IPage<BiddingMain> queryPageList(Page<BiddingMain> page, BiddingMain biddingMain);

	/**
	 * 评标管理
	 * @param page
	 * @param biddingMain
	 * @return
	 */
	IPage<BiddingMain> evaluateList(Page<BiddingMain> page, BiddingMain biddingMain);

	/**
	 * 评标明细
	 * @param biddingMain
	 * @return
	 */
	List<BiddingRecord> fetchRecordList(BiddingMain biddingMain);

	/**
	 * 提交评分
	 * @param page
	 */
	void submitTemplate(BiddingTemplatePage page);

	/**
	 * 保存评分
	 * @param page
	 */
    void draftTemplate(BiddingTemplatePage page);

	/**
	 * 招标列表
	 * @param biddingMain
	 * @return
	 */
	List<BiddingRecord> fetchRecordTwoList(BiddingMain biddingMain);

	/**
	 * 定标
	 * @param page
	 */
    void fixBidding(BiddingSupplier page);

	/**
	 * 中标供应商信息
	 * @param page
	 * @return
	 */
    BasSupplier getSuppInfo(BiddingMain page);

	/**
	 * 报价信息
	 * @param param
	 * @return
	 */
    List<BiddingMain> fetchQuote(BiddingMain param);

	/**
	 * 发布公告
	 */
    void sendNotice(String msg, String accountId,String subject);

	/**
	 * 招标列表
	 * @param page
	 * @param biddingMain
	 * @return
	 */
    IPage<BiddingMain> pageList(Page<BiddingMain> page, BiddingMain biddingMain);

	/**
	 * 获取专家信息
	 * @param id
	 * @return
	 */
    List<SysUser> fetchProFessionals(String id);

	/**
	 * 查询评标人员
	 * @param id
	 * @return
	 */
    List<BiddingProfessionals> fetchHasProfessionals(String id);

	/**
	 * 退回评标人员
	 * @param biddingProfessionals
	 */
	void toProfessionals(BiddingProfessionals biddingProfessionals);

	/**
	 * 招标供应商
	 * @param id
	 * @return
	 */
    List<BasSupplier> fetchSuppList(String id);

	/**
	 * 参与评标人员
	 * @param id
	 * @return
	 */
	List<String> fetchProFessionalsEmail(String id);
}
