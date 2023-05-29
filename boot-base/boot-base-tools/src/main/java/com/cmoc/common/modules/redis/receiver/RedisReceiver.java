package com.cmoc.common.modules.redis.receiver;


import cn.hutool.core.util.ObjectUtil;
import com.cmoc.common.modules.redis.listener.JeecgRedisListener;
import lombok.Data;
import com.cmoc.common.base.BaseMap;
import com.cmoc.common.constant.GlobalConstants;
import com.cmoc.common.util.SpringContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author zyf
 */
@Component
@Data
public class RedisReceiver {


    /**
     * 接受消息并调用业务逻辑处理器
     *
     * @param params
     */
    public void onMessage(BaseMap params) {
        Object handlerName = params.get(GlobalConstants.HANDLER_NAME);
        JeecgRedisListener messageListener = SpringContextHolder.getHandler(handlerName.toString(), JeecgRedisListener.class);
        if (ObjectUtil.isNotEmpty(messageListener)) {
            messageListener.onMessage(params);
        }
    }

}
