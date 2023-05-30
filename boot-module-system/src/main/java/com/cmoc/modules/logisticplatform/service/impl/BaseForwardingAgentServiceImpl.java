package com.cmoc.modules.logisticplatform.service.impl;

import com.cmoc.modules.logisticplatform.entity.ForwardingAgent;
import com.cmoc.modules.logisticplatform.mapper.BaseForwardingAgentMapper;
import com.cmoc.modules.logisticplatform.service.BaseForwardingAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseForwardingAgentServiceImpl implements BaseForwardingAgentService {

    @Autowired
    private BaseForwardingAgentMapper baseForwardingAgentMapper;

    @Override
    public List<ForwardingAgent> getList(ForwardingAgent forwardingAgent, Integer pageNo, Integer pageSize) {
        List<ForwardingAgent> freightForwardingList = baseForwardingAgentMapper.getList(forwardingAgent, pageNo, pageSize);

        return freightForwardingList;
    }

    @Override
    public Integer getCount() {
        return baseForwardingAgentMapper.getCount();
    }
}
