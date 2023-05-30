package com.cmoc.modules.logisticplatform.service;

import com.cmoc.modules.logisticplatform.entity.ForwardingAgent;

import java.util.List;

public interface BaseForwardingAgentService {

    List<ForwardingAgent> getList(ForwardingAgent forwardingAgent, Integer pageNo, Integer pageSize);

    Integer getCount();
}
