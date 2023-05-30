package com.cmoc.modules.logisticplatform.mapper;

import com.cmoc.modules.logisticplatform.entity.Port;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BasePortMapper {

    List<Port> getList(@Param("query") Port port,
                       @Param("pageNo") Integer pageNo,
                       @Param("pageSize") Integer pageSize);

    Integer getCount();
}
