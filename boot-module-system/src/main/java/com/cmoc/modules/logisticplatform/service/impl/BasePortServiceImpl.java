package com.cmoc.modules.logisticplatform.service.impl;

import com.cmoc.modules.logisticplatform.entity.Port;
import com.cmoc.modules.logisticplatform.mapper.BasePortMapper;
import com.cmoc.modules.logisticplatform.service.BasePortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasePortServiceImpl implements BasePortService {

    @Autowired
    private BasePortMapper basePortMapper;

    @Override
    public List<Port> getList(Port port, Integer pageNo, Integer pageSize) {
        List<Port> portList = basePortMapper.getList(port, pageNo, pageSize);

        return portList;
    }

    @Override
    public Integer getCount() {
        return basePortMapper.getCount();
    }
}
