//package com.cmoc.modules.logisticplatform.controller;
//
//
//import com.cmoc.common.api.vo.Result;
//import com.cmoc.modules.logisticplatform.entity.FreightForwarding;
//import com.cmoc.modules.logisticplatform.entity.FreightStation;
//import com.cmoc.modules.logisticplatform.service.BaseFreightForwardingService;
//import com.cmoc.modules.logisticplatform.service.BaseFreightStationService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @Description: freight_forwarding 货运代理
// * @Author: Atom Jiang
// * @Date: 2023-05-30
// * @Version: V1.0
// */
//@Api(tags = "freight_forwarding")
//@RestController
//@RequestMapping("/base/freight_forwarding")
//@Slf4j
//public class BaseFreightForwardingController {
//
//    @Autowired
//    private BaseFreightForwardingService baseFreightForwardingService;
//
//    /**
//     * 分页列表查询
//     *
//     * @param FreightForwarding
//     * @param pageNo
//     * @param pageSize
//     * @param req
//     * @return
//     */
//    @ApiOperation(value = "freight_forwarding-分页列表查询", notes = "freight_forwarding-分页列表查询")
//    @GetMapping(value = "/list")
//    public Result<Map> queryPageList(FreightForwarding freightForwarding,
//                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
//                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
//                                     HttpServletRequest req) {
//        List<FreightForwarding> freightForwardingList = new ArrayList<>();
//        Map<String,Object> map = new HashMap<>();
//        try {
//            freightForwardingList = baseFreightForwardingService.getList(freightForwarding, pageNo, pageSize);
//            Integer total = baseFreightForwardingService.getCount();
//            map.put("total",total);
//            map.put("result",freightForwardingList);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return Result.error("查询失败", map);
//        }
//        return Result.OK(map);
//    }
//}
