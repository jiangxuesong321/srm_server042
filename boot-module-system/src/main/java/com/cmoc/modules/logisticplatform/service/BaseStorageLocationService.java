package com.cmoc.modules.logisticplatform.service;

import com.cmoc.modules.logisticplatform.entity.StorageLocation;

import java.util.List;

public interface BaseStorageLocationService {

    List<StorageLocation> getList(StorageLocation storageLocation, Integer pageNo, Integer pageSize);

    Integer getCount();
}
