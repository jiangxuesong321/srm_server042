//package com.cmoc.modules.logisticplatform.service.impl;
//
//import com.cmoc.modules.logisticplatform.entity.FreightStation;
//import com.cmoc.modules.logisticplatform.mapper.BaseFreightStationMapper;
//import com.cmoc.modules.logisticplatform.service.BaseFreightForwardingService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class BaseFreightForwardingServiceImpl implements BaseFreightForwardingService {
//
//    @Autowired
//    private BaseFreightStationMapper baseFreightStationMapper;
//
//    @Override
//    public List<FreightStation> getList(FreightStation freightStation, Integer pageNo, Integer pageSize) {
//        List<FreightStation> freightStationList = baseFreightStationMapper.getList(freightStation, pageNo, pageSize);
//
//        return freightStationList;
//    }
//
//    @Override
//    public Integer getCount() {
//        return baseFreightStationMapper.getCount();
//    }
//}
