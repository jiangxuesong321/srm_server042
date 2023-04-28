package org.jeecg.modules.srm.service;

import org.jeecg.modules.srm.entity.BasContractTemplateArticle;
import org.jeecg.modules.srm.entity.BasContractTemplatePay;
import org.jeecg.modules.srm.entity.BasContractTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 合同模板主表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
public interface IBasContractTemplateService extends IService<BasContractTemplate> {

	/**
	 * 添加一对多
	 *
	 * @param basContractTemplate
	 * @param basContractTemplateArticleList
	 * @param basContractTemplatePayList
	 */
	public void saveMain(BasContractTemplate basContractTemplate,List<BasContractTemplateArticle> basContractTemplateArticleList,List<BasContractTemplatePay> basContractTemplatePayList) throws Exception;
	
	/**
	 * 修改一对多
	 *
   * @param basContractTemplate
   * @param basContractTemplateArticleList
   * @param basContractTemplatePayList
	 */
	public void updateMain(BasContractTemplate basContractTemplate,List<BasContractTemplateArticle> basContractTemplateArticleList,List<BasContractTemplatePay> basContractTemplatePayList) throws Exception;
	
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
