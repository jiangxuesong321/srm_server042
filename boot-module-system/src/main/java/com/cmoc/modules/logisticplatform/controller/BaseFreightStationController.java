package com.cmoc.modules.logisticplatform.controller;


import com.cmoc.common.api.vo.Result;
import com.cmoc.modules.logisticplatform.entity.FreightStation;
import com.cmoc.modules.logisticplatform.service.BaseFreightStationService;
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
 * @Description: freight_station 货运站
 * @Author: Atom Jiang
 * @Date: 2023-05-30
 * @Version: V1.0
 */
@Api(tags = "freight_station")
@RestController
@RequestMapping("/base/freight_station")
@Slf4j
public class BaseFreightStationController {

    @Autowired
    private BaseFreightStationService baseFreightStationService;

    /**
     * 分页列表查询
     *
     * @param freightStation
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "freight_station-分页列表查询", notes = "freight_station-分页列表查询")
    @GetMapping(value = "/list")
    public Result<Map> queryPageList(FreightStation freightStation,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest req) {
        List<FreightStation> freightStationList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        try {
            freightStationList = baseFreightStationService.getList(freightStation, pageNo, pageSize);
            Integer total = baseFreightStationService.getCount();
            map.put("total",total);
            map.put("result",freightStationList);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.error("查询失败", map);
        }
        return Result.OK(map);
    }
}
