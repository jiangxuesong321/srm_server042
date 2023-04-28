package org.jeecg.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.srm.entity.StkOoBill;
import org.jeecg.modules.srm.entity.StkOoBillDelivery;
import org.jeecg.modules.srm.mapper.StkOoBillDeliveryMapper;
import org.jeecg.modules.srm.mapper.StkOoBillMapper;
import org.jeecg.modules.srm.service.IStkOoBillService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 出库单
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Service
public class StkOoBillServiceImpl extends ServiceImpl<StkOoBillMapper, StkOoBill> implements IStkOoBillService {

	@Autowired
	private StkOoBillMapper stkOoBillMapper;
	@Autowired
	private StkOoBillDeliveryMapper stkOoBillDeliveryMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(StkOoBill stkOoBill, List<StkOoBillDelivery> stkOoBillDeliveryList) {
		stkOoBillMapper.insert(stkOoBill);
		if(stkOoBillDeliveryList!=null && stkOoBillDeliveryList.size()>0) {
			for(StkOoBillDelivery entity:stkOoBillDeliveryList) {
				//外键设置
				entity.setMid(stkOoBill.getId());
				stkOoBillDeliveryMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(StkOoBill stkOoBill,List<StkOoBillDelivery> stkOoBillDeliveryList) {
		stkOoBillMapper.updateById(stkOoBill);
		
		//1.先删除子表数据
		stkOoBillDeliveryMapper.deleteByMainId(stkOoBill.getId());
		
		//2.子表数据重新插入
		if(stkOoBillDeliveryList!=null && stkOoBillDeliveryList.size()>0) {
			for(StkOoBillDelivery entity:stkOoBillDeliveryList) {
				//外键设置
				entity.setMid(stkOoBill.getId());
				stkOoBillDeliveryMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		stkOoBillDeliveryMapper.deleteByMainId(id);
		stkOoBillMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			stkOoBillDeliveryMapper.deleteByMainId(id.toString());
			stkOoBillMapper.deleteById(id);
		}
	}

	/**
	 * 分页
	 * @param page
	 * @param stkOoBill
	 * @return
	 */
	@Override
	public IPage<StkOoBill> queryPageList(Page<StkOoBill> page, StkOoBill stkOoBill) {
		return baseMapper.queryPageList(page,stkOoBill);
	}

}
