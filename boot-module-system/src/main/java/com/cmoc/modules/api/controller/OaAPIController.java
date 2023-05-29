package com.cmoc.modules.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.cmoc.modules.srm.service.IContractBaseService;
import com.cmoc.modules.srm.service.IProjBaseService;
import com.cmoc.modules.srm.service.IPurPayPlanService;
import com.cmoc.modules.srm.vo.ProcessVo;
import com.cmoc.modules.srm.vo.ProjBaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.cmoc.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 服务化 system模块 对外接口请求类
 * @author: jeecg-boot
 */
@Slf4j
@RestController
@Api(tags="OA对接")
@RequestMapping("/srm/oa")
public class OaAPIController {
    @Autowired
    private IProjBaseService iProjBaseService;
    @Autowired
    private IContractBaseService iContractBaseService;
    @Autowired
    private IPurPayPlanService iPurPayPlanService;

    /**
     * 项目立项通知
     */
    @ApiOperation(value="项目立项通知", notes="项目立项通知")
    @PostMapping("/createProj")
    public Result createProj(@RequestBody ProjBaseVo projBaseVo){
        Result result = iProjBaseService.createProj(projBaseVo);
        return result;
    }

    /**
     * 项目变更通知
     */
    @ApiOperation(value="项目变更通知", notes="项目变更通知")
    @PostMapping("/updateProj")
    public Result updateProj(@RequestBody ProjBaseVo projBaseVo){
        Result result = iProjBaseService.updateProj(projBaseVo);
        return result;
    }

    /**
     * 项目变更通知
     */
    @ApiOperation(value="审批进度通知", notes="审批进度通知")
    @PostMapping("/processSpeed")
    public Result processSpeed(@RequestBody ProcessVo processVo){
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(processVo);
        log.info("OA传参",jsonObject);
        //合同
        if("0".equals(processVo.getType())){
            log.info("开始进行OA流程-合同");
            Result result = iContractBaseService.processSpeed(processVo);
            log.info("结束进行OA流程-合同");
            return result;
        }
        //付款
        else if("1".equals(processVo.getType())){
            log.info("开始进行OA流程-付款");
            Result result = iPurPayPlanService.processSpeed(processVo);
            log.info("结束进行OA流程-付款");
            return result;
        }
        return Result.error("500","提交失败：没有对应类型");
    }

}
