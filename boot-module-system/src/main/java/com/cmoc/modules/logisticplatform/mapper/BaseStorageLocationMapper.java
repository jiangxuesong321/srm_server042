package com.cmoc.modules.logisticplatform.mapper;

import com.cmoc.modules.logisticplatform.entity.StorageLocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaseStorageLocationMapper {

    List<StorageLocation> getList(@Param("query") StorageLocation storageLocation,
                                 @Param("pageNo") Integer pageNo,
                                 @Param("pageSize") Integer pageSize);

    Integer getCount();
}
