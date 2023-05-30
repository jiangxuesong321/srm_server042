package com.cmoc.modules.logisticplatform.mapper;

import com.cmoc.modules.logisticplatform.entity.FreightForwarding;
import com.cmoc.modules.logisticplatform.entity.FreightStation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaseFreightForwardingMapper {

    List<FreightForwarding> getList(@Param("query") FreightForwarding freightForwarding,
                                 @Param("pageNo") Integer pageNo,
                                 @Param("pageSize") Integer pageSize);

    Integer getCount();
}
