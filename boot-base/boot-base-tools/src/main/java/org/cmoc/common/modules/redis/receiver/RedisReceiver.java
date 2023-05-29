package org.cmoc.common.modules.redis.receiver;


import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import org.cmoc.common.base.BaseMap;
import org.cmoc.common.constant.GlobalConstants;
import org.cmoc.common.modules.redis.listener.JeecgRedisListener;
import org.cmoc.common.util.SpringContextHolder;
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
