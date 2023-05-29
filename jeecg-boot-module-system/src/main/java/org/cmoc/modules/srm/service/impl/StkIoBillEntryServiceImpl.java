package org.cmoc.modules.srm.service.impl;

import org.cmoc.modules.srm.entity.StkIoBillEntry;
import org.cmoc.modules.srm.mapper.StkIoBillEntryMapper;
import org.cmoc.modules.srm.service.IStkIoBillEntryService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 入库单明细
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Service
public class StkIoBillEntryServiceImpl extends ServiceImpl<StkIoBillEntryMapper, StkIoBillEntry> implements IStkIoBillEntryService {
	
	@Autowired
	private StkIoBillEntryMapper stkIoBillEntryMapper;
	
	@Override
	public List<StkIoBillEntry> selectByMainId(String mainId) {
		return stkIoBillEntryMapper.selectByMainId(mainId);
	}

	@Override
	public List<StkIoBillEntry> queryDetailListByMainId(String id) {
		return baseMapper.queryDetailListByMainId(id);
	}
}
