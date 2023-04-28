package org.jeecg.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.srm.entity.ContractObjectQty;
import org.jeecg.modules.srm.entity.StkIoBillEntry;
import org.jeecg.modules.srm.entity.StkIoBill;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 入库单
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
public interface IStkIoBillService extends IService<StkIoBill> {

	/**
	 * 添加一对多
	 *
	 * @param stkIoBill
	 * @param stkIoBillEntryList
	 */
	public void saveMain(StkIoBill stkIoBill,List<StkIoBillEntry> stkIoBillEntryList) ;
	
	/**
	 * 修改一对多
	 *
   * @param stkIoBill
   * @param stkIoBillEntryList
	 */
	public void updateMain(StkIoBill stkIoBill,List<StkIoBillEntry> stkIoBillEntryList);
	
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
	 * 设备到厂数统计
	 * @param entry
	 * @return
	 */
    StkIoBillEntry fetchArrivalQty(StkIoBillEntry entry);

	/**
	 * 库存
	 * @param page
	 * @param stkIoBill
	 * @return
	 */
    IPage<StkIoBillEntry> queryDetailPageList(Page<StkIoBillEntry> page, StkIoBillEntry stkIoBill);

	/**
	 * 分页
	 * @param page
	 * @param stkIoBill
	 * @return
	 */
    IPage<StkIoBill> queryPageList(Page<StkIoBill> page, StkIoBill stkIoBill);
	/**
	 *
	 * @author Kevin.Wang
	 * @date : 2023/2/13 14:44
	 * @param sk
	 * @return ContractObjectQty
	 **/
    ContractObjectQty queryOtherDetailsById(StkIoBill sk);

	/**
	 * 导出
	 * @param stkIoBill
	 * @return
	 */
    List<StkIoBill> exportXls(StkIoBill stkIoBill);

	/**
	 *
	 * @param ids
	 * @return
	 */
	List<ContractObjectQty> queryOtherDetailsByIds(List<String> ids);

	/**
	 * 发货审批
	 * @param stkIoBill
	 */
    void handleSendPass(StkIoBill stkIoBill);
	/**
	 * 发货审批
	 * @param stkIoBill
	 */
	void handleSendReject(StkIoBill stkIoBill);
}
