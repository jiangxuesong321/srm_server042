package com.cmoc.modules.logisticplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmoc.modules.logisticplatform.entity.FreightStation;
import com.cmoc.modules.srm.entity.AttachmentFileRecord;

import java.util.List;

public interface BaseFreightStationService {

    List<FreightStation> getList(FreightStation freightStation,Integer pageNo,Integer pageSize);

    Integer getCount();
}
