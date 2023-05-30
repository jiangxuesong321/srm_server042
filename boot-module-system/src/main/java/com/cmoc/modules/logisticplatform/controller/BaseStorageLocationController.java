package com.cmoc.modules.logisticplatform.controller;


import com.cmoc.common.api.vo.Result;
import com.cmoc.modules.logisticplatform.entity.FreightForwarding;
import com.cmoc.modules.logisticplatform.entity.StorageLocation;
import com.cmoc.modules.logisticplatform.service.BaseFreightForwardingService;
import com.cmoc.modules.logisticplatform.service.BaseStorageLocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: storage_location 库存地点
 * @Author: Atom Jiang
 * @Date: 2023-05-30
 * @Version: V1.0
 */
@Api(tags = "port")
@RestController
@RequestMapping("/base/storage_location")
@Slf4j
public class BaseStorageLocationController {

    @Autowired
    private BaseStorageLocationService baseStorageLocationService;

    /**
     * 分页列表查询
     *
     * @param storageLocation
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "bas_storage_location-分页列表查询", notes = "bas_storage_location-分页列表查询")
    @GetMapping(value = "/list")
    public Result<Map> queryPageList(StorageLocation storageLocation,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        List<StorageLocation> storageLocationList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        try {
            storageLocationList = baseStorageLocationService.getList(storageLocation, pageNo, pageSize);
            Integer total = baseStorageLocationService.getCount();
            map.put("total",total);
            map.put("result",storageLocationList);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.error("查询失败", map);
        }
        return Result.OK(map);
    }

}
