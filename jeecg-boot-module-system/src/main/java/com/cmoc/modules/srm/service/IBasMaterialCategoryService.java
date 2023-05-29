package com.cmoc.modules.srm.service;

import com.cmoc.common.system.vo.SelectTreeModel;
import com.cmoc.modules.srm.entity.BasMaterialCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmoc.common.exception.JeecgBootException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * @Description: 设备分类
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface IBasMaterialCategoryService extends IService<BasMaterialCategory> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**
	 * 新增节点
	 *
	 * @param basMaterialCategory
	 */
	void addBasMaterialCategory(BasMaterialCategory basMaterialCategory) throws Exception;
	
	/**
   * 修改节点
   *
   * @param basMaterialCategory
   * @throws JeecgBootException
   */
	void updateBasMaterialCategory(BasMaterialCategory basMaterialCategory) throws Exception;
	
	/**
	 * 删除节点
	 *
	 * @param id
   * @throws JeecgBootException
	 */
	void deleteBasMaterialCategory(String id) throws Exception;

	  /**
	   * 查询所有数据，无分页
	   *
	   * @param queryWrapper
	   * @return List<BasMaterialCategory>
	   */
    List<BasMaterialCategory> queryTreeListNoPage(QueryWrapper<BasMaterialCategory> queryWrapper);

	/**
	 * 【vue3专用】根据父级编码加载分类字典的数据
	 *
	 * @param parentCode
	 * @return
	 */
	List<SelectTreeModel> queryListByCode(String parentCode);

	/**
	 * 【vue3专用】根据pid查询子节点集合
	 *
	 * @param pid
	 * @return
	 */
	List<SelectTreeModel> queryListByPid(String pid);

	/**
	 * 分类
	 * @return
	 */
    List<BasMaterialCategory> getCategoryList();
}
