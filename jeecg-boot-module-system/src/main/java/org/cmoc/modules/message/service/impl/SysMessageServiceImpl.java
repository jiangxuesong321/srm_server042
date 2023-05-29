package org.cmoc.modules.message.service.impl;

import org.cmoc.common.system.base.service.impl.JeecgServiceImpl;
import org.cmoc.modules.message.entity.SysMessage;
import org.cmoc.modules.message.mapper.SysMessageMapper;
import org.cmoc.modules.message.service.ISysMessageService;
import org.springframework.stereotype.Service;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:  2019-04-09
 * @Version: V1.0
 */
@Service
public class SysMessageServiceImpl extends JeecgServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {

}
