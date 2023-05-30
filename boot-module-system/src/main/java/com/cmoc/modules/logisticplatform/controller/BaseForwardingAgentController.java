package com.cmoc.modules.logisticplatform.controller;


import com.cmoc.common.api.vo.Result;
import com.cmoc.modules.logisticplatform.entity.ForwardingAgent;
import com.cmoc.modules.logisticplatform.service.BaseForwardingAgentService;
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
 * @Description: forwarding_agent 承运商
 * @Author: Atom Jiang
 * @Date: 2023-05-30
 * @Version: V1.0
 */
@Api(tags = "forwarding_agent")
@RestController
@RequestMapping("/base/forwarding_agent")
@Slf4j
public class BaseForwardingAgentController {

    @Autowired
    private BaseForwardingAgentService baseForwardingAgentService;

    /**
     * 分页列表查询
     *
     * @param forwardingAgent
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "freight_station-分页列表查询", notes = "freight_station-分页列表查询")
    @GetMapping(value = "/list")
    public Result<Map> queryPageList(ForwardingAgent forwardingAgent,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        List<ForwardingAgent> forwardingAgentList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        try {
            forwardingAgentList = baseForwardingAgentService.getList(forwardingAgent, pageNo, pageSize);
            Integer total = baseForwardingAgentService.getCount();
            map.put("total", total);
            map.put("result", forwardingAgentList);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.error("查询失败", map);
        }
        return Result.OK(map);
    }
}
