package org.jeecg.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.srm.entity.PurPayApplyDetail;
import org.jeecg.modules.srm.entity.PurPayApply;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 付款申请
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface IPurPayApplyService extends IService<PurPayApply> {

	/**
	 * 添加一对多
	 *
	 * @param purPayApply
	 * @param purPayApplyDetailList
	 */
	public void saveMain(PurPayApply purPayApply,List<PurPayApplyDetail> purPayApplyDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param purPayApply
   * @param purPayApplyDetailList
	 */
	public void updateMain(PurPayApply purPayApply,List<PurPayApplyDetail> purPayApplyDetailList);
	
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
	 * 驳回
	 * @param purPayApply
	 */
    void toApprove(PurPayApply purPayApply) throws Exception;

	/**
	 * 付款申请
	 * @param page
	 * @param purPayApply
	 * @return
	 */
	IPage<PurPayApply> queryPageList(Page<PurPayApply> page, PurPayApply purPayApply);

	/**
	 * 币种汇总
	 * @param purPayApply
	 * @return
	 */
	List<PurPayApply> getTotalAmountByCurrency(PurPayApply purPayApply);

	/**
	 * 获取申请明细
	 * @param ids
	 * @return
	 */
    List<PurPayApplyDetail> fetchPayDetailList(List<String> ids);

	/**
	 * 应付管理导出
	 * @param purPayApply
	 * @return
	 */
    List<PurPayApply> queryList(PurPayApply purPayApply);

	/**
	 * 累计请款金额
	 * @param contractId
	 * @param ids
	 * @return
	 */
    List<PurPayApply> fetchHasPayDetailList(String contractId);
}
