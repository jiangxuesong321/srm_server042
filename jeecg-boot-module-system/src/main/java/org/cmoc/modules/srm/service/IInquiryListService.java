package org.cmoc.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cmoc.modules.srm.entity.BasSupplier;
import org.cmoc.modules.srm.entity.InquiryRecord;
import org.cmoc.modules.srm.entity.InquirySupplier;
import org.cmoc.modules.srm.entity.InquiryList;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cmoc.modules.srm.vo.InquiryListPage;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 询价单主表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface IInquiryListService extends IService<InquiryList> {

	/**
	 * 添加一对多
	 *
	 * @param inquiryList
	 * @param inquiryRecordList
	 */
	public void saveMain(InquiryList inquiryList,List<InquiryRecord> inquiryRecordList) ;
	
	/**
	 * 修改一对多
	 *
   * @param inquiryList
   * @param inquiryRecordList
	 */
	public void updateMain(InquiryList inquiryList,List<InquiryRecord> inquiryRecordList);
	
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
	 * 询比价列表
	 * @param page
	 * @param inquiryList
	 * @return
	 */
    IPage<InquiryList> queryPageList(Page<InquiryList> page, InquiryList inquiryList);

	/**
	 * 再次发布需求
	 * @param inquiryList
	 */
    void handlePush(InquiryList inquiryList);

	/**
	 * 提交
	 * @param inquiryList
	 */
	void toRecommend(InquiryListPage inquiryList);

	/**
	 * 合同生成列表
	 * @param page
	 * @param inquiryList
	 * @return
	 */
    IPage<InquiryList> queryPageToDetailList(Page<InquiryList> page, InquiryList inquiryList);

	/**
	 * 结束报价
	 * @param inquiryList
	 */
    void toRecommand(InquiryListPage inquiryList);

	/**
	 * 发送公告
	 */
    void sendNotice(String content,String accountId,String subject);

	/**
	 * 编辑
	 * @param inquiryList
	 */
    void editEntity(InquiryList inquiryList) throws Exception;

	/**
	 * 询价供应商
	 * @param id
	 * @return
	 */
    List<BasSupplier> fetchSuppList(String id);
}
