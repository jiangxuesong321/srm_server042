package com.cmoc.modules.logisticplatform.service;

import com.cmoc.modules.logisticplatform.entity.Port;

import java.util.List;

public interface BasePortService {

    List<Port> getList(Port port, Integer pageNo, Integer pageSize);

    Integer getCount();
}
