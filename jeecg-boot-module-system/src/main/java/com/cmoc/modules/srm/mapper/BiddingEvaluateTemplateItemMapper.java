package com.cmoc.modules.srm.mapper;

import java.util.List;
import com.cmoc.modules.srm.entity.BiddingEvaluateTemplateItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 评标模板评分项表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface BiddingEvaluateTemplateItemMapper extends BaseMapper<BiddingEvaluateTemplateItem> {

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
   * @return List<BiddingEvaluateTemplateItem>
   */
	public List<BiddingEvaluateTemplateItem> selectByMainId(@Param("mainId") String mainId);
}
