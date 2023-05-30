package com.cmoc.modules.logisticplatform.mapper;

import com.cmoc.modules.logisticplatform.entity.ForwardingAgent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaseForwardingAgentMapper {

    List<ForwardingAgent> getList(@Param("query") ForwardingAgent forwardingAgent,
                                  @Param("pageNo") Integer pageNo,
                                  @Param("pageSize") Integer pageSize);

    Integer getCount();
}
