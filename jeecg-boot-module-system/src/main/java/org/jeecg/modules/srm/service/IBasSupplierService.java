package org.jeecg.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.srm.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.srm.vo.BasSupplierPage;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商基本信息
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface IBasSupplierService extends IService<BasSupplier> {

	/**
	 * 添加一对多
	 *
	 * @param basSupplier
	 * @param basSupplierContactList
	 * @param basSupplierQualificationList
	 */
	public void saveMain(BasSupplier basSupplier, List<BasSupplierContact> basSupplierContactList, List<BasSupplierQualification> basSupplierQualificationList,
						 List<BasSupplierBank> basSupplierBankList, List<BasSupplierFast> basSupplierFastList) ;
	
	/**
	 * 修改一对多
	 *
   * @param basSupplier
   * @param basSupplierContactList
   * @param basSupplierQualificationList
	 */
	public void updateMain(BasSupplier basSupplier,List<BasSupplierContact> basSupplierContactList,List<BasSupplierQualification> basSupplierQualificationList,
						   List<BasSupplierBank> basSupplierBankList, List<BasSupplierFast> basSupplierFastList);
	
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
	 * 批量删除一对多
	 *
	 * @param basSupplierPage
	 */
	public Boolean createSrmAccount (BasSupplierPage basSupplierPage) throws Exception;

	/**
	 * 分页
	 * @param page
	 * @param basSupplier
	 * @return
	 */
    IPage<BasSupplier> pageList(Page<BasSupplier> page, BasSupplier basSupplier);

	/**
	 * 重置密码
	 * @param basSupplierPage
	 */
    void resetSrmAccount(BasSupplierPage basSupplierPage);

	/**
	 * 续费
	 * @param basSupplierPage
	 */
	void reNew(BasSupplierPage basSupplierPage);

	/**
	 * 供应商属性统计
	 * @param basSupplier
	 * @return
	 */
	List<Map<String,Object>> fetchSuppCategory(BasSupplier basSupplier);

	/**
	 * 供应商属性统计
	 * @param basSupplier
	 * @return
	 */
	List<Map<String, Object>> fetchSuppType(BasSupplier basSupplier);

	/**
	 * 供应商合同
	 * @param basSupplier
	 * @return
	 */
	Map<String,Object> fetchSuppContract(BasSupplier basSupplier);

	/**
	 * 供应商合同
	 * @param basSupplier
	 * @return
	 */
	Map<String, Object> fetchSuppContracting(BasSupplier basSupplier);

	/**
	 * 供应商状态
	 * @param basSupplier
	 * @return
	 */
	List<Map<String, Object>> fetchSuppStatus(BasSupplier basSupplier);

	/**
	 * 活跃供应商
	 * @param basSupplier
	 * @return
	 */
	Map<String, Object> fetchSuppActive(BasSupplier basSupplier);
}
