package com.cmoc.modules.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import com.cmoc.common.base.BaseMap;
import com.cmoc.common.constant.CacheConstant;
import com.cmoc.common.constant.GlobalConstants;
import com.cmoc.common.util.oConvertUtils;
import com.cmoc.modules.system.entity.SysGatewayRoute;
import com.cmoc.modules.system.mapper.SysGatewayRouteMapper;
import com.cmoc.modules.system.service.ISysGatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: gateway路由管理
 * @Author: jeecg-boot
 * @Date: 2020-05-26
 * @Version: V1.0
 */
@Service
@Slf4j
public class SysGatewayRouteServiceImpl extends ServiceImpl<SysGatewayRouteMapper, SysGatewayRoute> implements ISysGatewayRouteService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public void addRoute2Redis(String key) {
        List<SysGatewayRoute> ls = this.list(new LambdaQueryWrapper<SysGatewayRoute>());
        redisTemplate.opsForValue().set(key, JSON.toJSONString(ls));
    }

    @Override
    public void deleteById(String id) {
        this.removeById(id);
        this.resreshRouter(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAll(JSONObject json) {
        log.info("--gateway 路由配置修改--");
        try {
            json = json.getJSONObject("router");
            String id = json.getString("id");
            //update-begin-author:taoyan date:20211025 for: oracle路由网关新增小bug /issues/I4EV2J
            SysGatewayRoute route;
            if(oConvertUtils.isEmpty(id)){
                route = new SysGatewayRoute();
            }else{
                route = getById(id);
            }
            //update-end-author:taoyan date:20211025 for: oracle路由网关新增小bug /issues/I4EV2J
            if (ObjectUtil.isEmpty(route)) {
                route = new SysGatewayRoute();
            }
            route.setRouterId(json.getString("routerId"));
            route.setName(json.getString("name"));
            route.setPredicates(json.getString("predicates"));
            String filters = json.getString("filters");
            if (ObjectUtil.isEmpty(filters)) {
                filters = "[]";
            }
            route.setFilters(filters);
            route.setUri(json.getString("uri"));
            if (json.get("status") == null) {
                route.setStatus(1);
            } else {
                route.setStatus(json.getInteger("status"));
            }
            this.saveOrUpdate(route);
            resreshRouter(null);
        } catch (Exception e) {
            log.error("路由配置解析失败", e);
            resreshRouter(null);
            e.printStackTrace();
        }
    }

    /**
     * 更新redis路由缓存
     */
    private void resreshRouter(String delRouterId) {
        //更新redis路由缓存
        addRoute2Redis(CacheConstant.GATEWAY_ROUTES);
        BaseMap params = new BaseMap();
        params.put(GlobalConstants.HANDLER_NAME, GlobalConstants.LODER_ROUDER_HANDLER);
        params.put("delRouterId", delRouterId);
        //刷新网关
        redisTemplate.convertAndSend(GlobalConstants.REDIS_TOPIC_NAME, params);
    }

    @Override
    public void clearRedis() {
        redisTemplate.opsForValue().set(CacheConstant.GATEWAY_ROUTES, null);
    }


}
