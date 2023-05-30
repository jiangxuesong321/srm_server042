package com.cmoc.modules.logisticplatform.controller;


import com.cmoc.common.api.vo.Result;
import com.cmoc.modules.logisticplatform.entity.Port;
import com.cmoc.modules.logisticplatform.service.BasePortService;
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
 * @Description: port 港口
 * @Author: Atom Jiang
 * @Date: 2023-05-30
 * @Version: V1.0
 */
@Api(tags = "port")
@RestController
@RequestMapping("/base/port")
@Slf4j
public class BasePortController {

    @Autowired
    private BasePortService basePortService;

    /**
     * 分页列表查询
     *
     * @param port
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "port-分页列表查询", notes = "port-分页列表查询")
    @GetMapping(value = "/list")
    public Result<Map> queryPageList(Port port,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        List<Port> portList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        try {
            portList = basePortService.getList(port, pageNo, pageSize);
            Integer total = basePortService.getCount();
            map.put("total", total);
            map.put("result", portList);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.error("查询失败", map);
        }
        return Result.OK(map);
    }
}
