package org.jeecg.modules.srm.service;

import org.jeecg.modules.srm.entity.BiddingEvaluateTemplateItem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 评标模板评分项表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface IBiddingEvaluateTemplateItemService extends IService<BiddingEvaluateTemplateItem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<BiddingEvaluateTemplateItem>
	 */
	public List<BiddingEvaluateTemplateItem> selectByMainId(String mainId);
}
