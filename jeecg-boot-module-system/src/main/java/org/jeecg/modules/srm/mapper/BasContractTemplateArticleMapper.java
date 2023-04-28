package org.jeecg.modules.srm.mapper;

import java.util.List;
import org.jeecg.modules.srm.entity.BasContractTemplateArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 合同条款表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
public interface BasContractTemplateArticleMapper extends BaseMapper<BasContractTemplateArticle> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<BasContractTemplateArticle>
   */
	public List<BasContractTemplateArticle> selectByMainId(@Param("mainId") String mainId);
}
