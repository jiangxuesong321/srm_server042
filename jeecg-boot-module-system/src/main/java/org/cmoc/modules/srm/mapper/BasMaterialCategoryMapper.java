package org.cmoc.modules.srm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cmoc.common.system.vo.SelectTreeModel;
import org.cmoc.modules.srm.entity.BasMaterialCategory;

import java.util.List;
import java.util.Map;

/**
 * @Description: 设备分类
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface BasMaterialCategoryMapper extends BaseMapper<BasMaterialCategory> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

	/**
	 * 【vue3专用】根据父级ID查询树节点数据
	 *
	 * @param pid
	 * @param query
	 * @return
	 */
	List<SelectTreeModel> queryListByPid(@Param("pid") String pid, @Param("query") Map<String, String> query);

	/**
	 * 分类
	 * @return
	 */
    List<BasMaterialCategory> getCategoryList();
}
