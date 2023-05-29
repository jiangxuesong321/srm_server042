package com.cmoc.config.init;

import com.cmoc.modules.system.service.ISysGatewayRouteService;
import lombok.extern.slf4j.Slf4j;
import com.cmoc.common.constant.CacheConstant;
import com.cmoc.config.JeecgCloudCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @desc: 启动程序，初始化路由配置
 * @author: flyme
 */
@Slf4j
@Component
@Conditional(JeecgCloudCondition.class)
public class SystemInitListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {


    @Autowired
    private ISysGatewayRouteService sysGatewayRouteService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        log.info(" 服务已启动，初始化路由配置 ###################");
        if (applicationReadyEvent.getApplicationContext().getDisplayName().indexOf("AnnotationConfigServletWebServerApplicationContext") > -1) {
            sysGatewayRouteService.addRoute2Redis(CacheConstant.GATEWAY_ROUTES);
        }

    }

    @Override
    public int getOrder() {
        return 1;
    }
}