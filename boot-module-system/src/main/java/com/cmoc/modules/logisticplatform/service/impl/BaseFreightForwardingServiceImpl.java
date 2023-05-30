package com.cmoc.modules.logisticplatform.service.impl;

import com.cmoc.modules.logisticplatform.entity.FreightForwarding;
import com.cmoc.modules.logisticplatform.mapper.BaseFreightForwardingMapper;
import com.cmoc.modules.logisticplatform.service.BaseFreightForwardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseFreightForwardingServiceImpl implements BaseFreightForwardingService {

    @Autowired
    private BaseFreightForwardingMapper baseFreightForwardingMapper;

    @Override
    public List<FreightForwarding> getList(FreightForwarding freightForwarding, Integer pageNo, Integer pageSize) {
        List<FreightForwarding> freightForwardingList = baseFreightForwardingMapper.getList(freightForwarding, pageNo, pageSize);
        return freightForwardingList;
    }

    @Override
    public Integer getCount() {
        return baseFreightForwardingMapper.getCount();
    }
}
