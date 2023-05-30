package com.cmoc.modules.logisticplatform.mapper;

import com.cmoc.modules.logisticplatform.entity.FreightStation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaseFreightStationMapper {

    List<FreightStation> getList(@Param("query") FreightStation freightStation,
                                 @Param("pageNo") Integer pageNo,
                                 @Param("pageSize") Integer pageSize);

    Integer getCount();
}
