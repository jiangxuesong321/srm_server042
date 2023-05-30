package com.cmoc.modules.logisticplatform.service;

import com.cmoc.modules.logisticplatform.entity.FreightForwarding;

import java.util.List;

public interface BaseFreightForwardingService {
    List<FreightForwarding> getList(FreightForwarding freightForwarding, Integer pageNo, Integer pageSize);

    Integer getCount();
}
