package org.cmoc.modules.srm.service;

import org.cmoc.modules.srm.entity.BasContractTemplateArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 合同条款表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
public interface IBasContractTemplateArticleService extends IService<BasContractTemplateArticle> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<BasContractTemplateArticle>
	 */
	public List<BasContractTemplateArticle> selectByMainId(String mainId);
}
