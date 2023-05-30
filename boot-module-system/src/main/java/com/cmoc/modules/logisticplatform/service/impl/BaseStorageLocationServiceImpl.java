package com.cmoc.modules.logisticplatform.service.impl;

import com.cmoc.modules.logisticplatform.entity.StorageLocation;
import com.cmoc.modules.logisticplatform.mapper.BaseStorageLocationMapper;
import com.cmoc.modules.logisticplatform.service.BaseStorageLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseStorageLocationServiceImpl implements BaseStorageLocationService {

    @Autowired
    private BaseStorageLocationMapper baseStorageLocationMapper;

    @Override
    public List<StorageLocation> getList(StorageLocation storageLocation, Integer pageNo, Integer pageSize) {
        List<StorageLocation> storageLocationList = baseStorageLocationMapper.getList(storageLocation, pageNo, pageSize);

        return storageLocationList;
    }

    @Override
    public Integer getCount() {
        return baseStorageLocationMapper.getCount();
    }
}
