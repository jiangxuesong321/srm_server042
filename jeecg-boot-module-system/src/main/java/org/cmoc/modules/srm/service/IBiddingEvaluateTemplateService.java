package org.cmoc.modules.srm.service;

import org.cmoc.modules.srm.entity.BiddingEvaluateTemplateItem;
import org.cmoc.modules.srm.entity.BiddingEvaluateTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 评标模板表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface IBiddingEvaluateTemplateService extends IService<BiddingEvaluateTemplate> {

	/**
	 * 添加一对多
	 *
	 * @param biddingEvaluateTemplate
	 * @param biddingEvaluateTemplateItemList
	 */
	public void saveMain(BiddingEvaluateTemplate biddingEvaluateTemplate,List<BiddingEvaluateTemplateItem> biddingEvaluateTemplateItemList) ;
	
	/**
	 * 修改一对多
	 *
   * @param biddingEvaluateTemplate
   * @param biddingEvaluateTemplateItemList
	 */
	public void updateMain(BiddingEvaluateTemplate biddingEvaluateTemplate,List<BiddingEvaluateTemplateItem> biddingEvaluateTemplateItemList);
	
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
	
}
