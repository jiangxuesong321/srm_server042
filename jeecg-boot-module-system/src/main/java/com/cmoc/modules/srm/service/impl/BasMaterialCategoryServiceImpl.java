package com.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cmoc.common.constant.CommonConstant;
import com.cmoc.common.exception.JeecgBootException;
import com.cmoc.common.util.oConvertUtils;
import com.cmoc.common.system.vo.SelectTreeModel;
import com.cmoc.modules.srm.entity.BasMaterial;
import com.cmoc.modules.srm.entity.BasMaterialCategory;
import com.cmoc.modules.srm.mapper.BasMaterialCategoryMapper;
import com.cmoc.modules.srm.mapper.BasMaterialMapper;
import com.cmoc.modules.srm.service.IBasMaterialCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 设备分类
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Service
public class BasMaterialCategoryServiceImpl extends ServiceImpl<BasMaterialCategoryMapper, BasMaterialCategory> implements IBasMaterialCategoryService {
    @Autowired
    private BasMaterialMapper basMaterialMapper;

	@Override
	public void addBasMaterialCategory(BasMaterialCategory basMaterialCategory) throws Exception {
	   //新增时设置hasChild为0
	    basMaterialCategory.setHasChild(IBasMaterialCategoryService.NOCHILD);
		if(oConvertUtils.isEmpty(basMaterialCategory.getPid())){
			basMaterialCategory.setPid(IBasMaterialCategoryService.ROOT_PID_VALUE);
			//层级
            basMaterialCategory.setVersion(CommonConstant.ACT_SYNC_0);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			BasMaterialCategory parent = baseMapper.selectById(basMaterialCategory.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
            //层级
            basMaterialCategory.setVersion(parent.getVersion() + 1);
		}
		//判断当前层级名称是否有重复的
        List<BasMaterialCategory> existList = this.list(Wrappers.<BasMaterialCategory>query().lambda().
                eq(BasMaterialCategory :: getName,basMaterialCategory.getName()).
                eq(BasMaterialCategory :: getVersion,basMaterialCategory.getVersion()));
		if(existList != null && existList.size() > 0){
		    throw new Exception("同一级分类名称不能重复");
        }

        existList = this.list(Wrappers.<BasMaterialCategory>query().lambda().
                eq(BasMaterialCategory :: getCode,basMaterialCategory.getCode()));
        if(existList != null && existList.size() > 0){
            throw new Exception("已存在重复编码");
        }

		baseMapper.insert(basMaterialCategory);
	}
	
	@Override
	public void updateBasMaterialCategory(BasMaterialCategory basMaterialCategory) throws Exception {
		BasMaterialCategory entity = this.getById(basMaterialCategory.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = basMaterialCategory.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				basMaterialCategory.setPid(IBasMaterialCategoryService.ROOT_PID_VALUE);
			}
			if(!IBasMaterialCategoryService.ROOT_PID_VALUE.equals(basMaterialCategory.getPid())) {
				baseMapper.updateTreeNodeStatus(basMaterialCategory.getPid(), IBasMaterialCategoryService.HASCHILD);
			}
		}
        //判断当前层级名称是否有重复的
        List<BasMaterialCategory> existList = this.list(Wrappers.<BasMaterialCategory>query().lambda().
                eq(BasMaterialCategory :: getName,basMaterialCategory.getName()).
                eq(BasMaterialCategory :: getVersion,basMaterialCategory.getVersion()).
                eq(BasMaterialCategory :: getPid,new_pid).
                ne(BasMaterialCategory :: getId,basMaterialCategory.getId()));
        if(existList != null && existList.size() > 0){
            throw new Exception("同一级分类名称不能重复");
        }
		baseMapper.updateById(basMaterialCategory);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBasMaterialCategory(String id) throws Exception {
		//查询选中节点下所有子节点一并删除
//        id = this.queryTreeChildIds(id);
//        if(id.indexOf(",")>0) {
//            StringBuffer sb = new StringBuffer();
//            String[] idArr = id.split(",");
//            for (String idVal : idArr) {
//                if(idVal != null){
//                    BasMaterialCategory basMaterialCategory = this.getById(idVal);
//                    String pidVal = basMaterialCategory.getPid();
//                    //查询此节点上一级是否还有其他子节点
//                    List<BasMaterialCategory> dataList = baseMapper.selectList(new QueryWrapper<BasMaterialCategory>().eq("pid", pidVal).notIn("id",Arrays.asList(idArr)));
//                    boolean flag = (dataList == null || dataList.size() == 0) && !Arrays.asList(idArr).contains(pidVal) && !sb.toString().contains(pidVal);
//                    if(flag){
//                        //如果当前节点原本有子节点 现在木有了，更新状态
//                        sb.append(pidVal).append(",");
//                    }
//                }
//            }
//            //批量删除节点
//            baseMapper.deleteBatchIds(Arrays.asList(idArr));
//            //修改已无子节点的标识
//            String[] pidArr = sb.toString().split(",");
//            for(String pid : pidArr){
//                this.updateOldParentNode(pid);
//            }
//        }else{
//            BasMaterialCategory basMaterialCategory = this.getById(id);
//            if(basMaterialCategory==null) {
//                throw new JeecgBootException("未找到对应实体");
//            }
//            updateOldParentNode(basMaterialCategory.getPid());
//            baseMapper.deleteById(id);
//        }
        String[] ids = id.split(",");
        List<BasMaterialCategory> categoryList = this.listByIds(Arrays.asList(ids));
        //判断该分类中是否存在 子类或该分类正在使用中
        List<BasMaterial> bmList = basMaterialMapper.selectList(Wrappers.<BasMaterial>query().lambda().in(BasMaterial :: getLastCategoryId,ids));
        StringBuffer sb = new StringBuffer();
        for(BasMaterialCategory bmc : categoryList){
            Boolean flag = false;
            if(CommonConstant.HAS_SEND.equals(bmc.getHasChild())){
                throw new Exception(bmc.getName() + ",存在子类,不能删除");
            }
            for(BasMaterial bm : bmList){
                if(bmc.getId().equals(bm.getLastCategoryId())){
                    flag = true;
                    break;
                }
            }
            if(flag){
                throw new Exception(bmc.getName() + ",正在使用中,不能删除");
            }
            String pidVal = bmc.getPid();
            //查询父类是否还有其他子节点,如果没有 更新 无子节点的标识
            List<BasMaterialCategory> childList = this.list(Wrappers.<BasMaterialCategory>query().lambda().
                    eq(BasMaterialCategory :: getPid,bmc.getPid()).
                    notIn(BasMaterialCategory :: getId,Arrays.asList(ids)));
            Boolean exist = (childList == null || childList.size() == 0) && !Arrays.asList(ids).contains(pidVal) && !sb.toString().contains(pidVal);
            if(exist){
                //如果当前节点原本有子节点 现在木有了，更新状态
                sb.append(pidVal).append(",");
            }
        }
        //批量删除节点
        baseMapper.deleteBatchIds(Arrays.asList(ids));

        String[] pidArr = sb.toString().split(",");
        UpdateWrapper<BasMaterialCategory> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("has_child",CommonConstant.DEL_FLAG_0);
        updateWrapper.in("id",pidArr);
        this.update(updateWrapper);
	}
	
	@Override
    public List<BasMaterialCategory> queryTreeListNoPage(QueryWrapper<BasMaterialCategory> queryWrapper) {
        List<BasMaterialCategory> dataList = baseMapper.selectList(queryWrapper);
        List<BasMaterialCategory> mapList = new ArrayList<>();
        for(BasMaterialCategory data : dataList){
            String pidVal = data.getPid();
            //递归查询子节点的根节点
            if(pidVal != null && !IBasMaterialCategoryService.NOCHILD.equals(pidVal)){
                BasMaterialCategory rootVal = this.getTreeRoot(pidVal);
                if(rootVal != null && !mapList.contains(rootVal)){
                    mapList.add(rootVal);
                }
            }else{
                if(!mapList.contains(data)){
                    mapList.add(data);
                }
            }
        }
        return mapList;
    }

    @Override
    public List<SelectTreeModel> queryListByCode(String parentCode) {
        String pid = ROOT_PID_VALUE;
        if (oConvertUtils.isNotEmpty(parentCode)) {
            LambdaQueryWrapper<BasMaterialCategory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BasMaterialCategory::getPid, parentCode);
            List<BasMaterialCategory> list = baseMapper.selectList(queryWrapper);
            if (list == null || list.size() == 0) {
                throw new JeecgBootException("该编码【" + parentCode + "】不存在，请核实!");
            }
            if (list.size() > 1) {
                throw new JeecgBootException("该编码【" + parentCode + "】存在多个，请核实!");
            }
            pid = list.get(0).getId();
        }
        return baseMapper.queryListByPid(pid, null);
    }

    @Override
    public List<SelectTreeModel> queryListByPid(String pid) {
        if (oConvertUtils.isEmpty(pid)) {
            pid = ROOT_PID_VALUE;
        }
        return baseMapper.queryListByPid(pid, null);
    }

    /**
     * 分类
     * @return
     */
    @Override
    public List<BasMaterialCategory> getCategoryList() {
        return baseMapper.getCategoryList();
    }

    /**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IBasMaterialCategoryService.ROOT_PID_VALUE.equals(pid)) {
			Long count = baseMapper.selectCount(new QueryWrapper<BasMaterialCategory>().eq("pid", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IBasMaterialCategoryService.NOCHILD);
			}
		}
	}

	/**
     * 递归查询节点的根节点
     * @param pidVal
     * @return
     */
    private BasMaterialCategory getTreeRoot(String pidVal){
        BasMaterialCategory data =  baseMapper.selectById(pidVal);
        if(data != null && !IBasMaterialCategoryService.ROOT_PID_VALUE.equals(data.getPid())){
            return this.getTreeRoot(data.getPid());
        }else{
            return data;
        }
    }

    /**
     * 根据id查询所有子节点id
     * @param ids
     * @return
     */
    private String queryTreeChildIds(String ids) {
        //获取id数组
        String[] idArr = ids.split(",");
        StringBuffer sb = new StringBuffer();
        for (String pidVal : idArr) {
            if(pidVal != null){
                if(!sb.toString().contains(pidVal)){
                    if(sb.toString().length() > 0){
                        sb.append(",");
                    }
                    sb.append(pidVal);
                    this.getTreeChildIds(pidVal,sb);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 递归查询所有子节点
     * @param pidVal
     * @param sb
     * @return
     */
    private StringBuffer getTreeChildIds(String pidVal,StringBuffer sb){
        List<BasMaterialCategory> dataList = baseMapper.selectList(new QueryWrapper<BasMaterialCategory>().eq("pid", pidVal));
        if(dataList != null && dataList.size()>0){
            for(BasMaterialCategory tree : dataList) {
                if(!sb.toString().contains(tree.getId())){
                    sb.append(",").append(tree.getId());
                }
                this.getTreeChildIds(tree.getId(),sb);
            }
        }
        return sb;
    }

}
