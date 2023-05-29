package org.cmoc.modules.message.handle;

import java.util.List;

/**
 * @Description: 发送信息接口
 * @author: jeecg-boot
 */
public interface ISendMsgHandle {

    /**
     * 发送信息
     * @param es_receiver 发送人
     * @param es_title 标题
     * @param es_content 内容
     */
	void SendMsg(String es_receiver, String es_title, String es_content);

    void sendTemplateMail(String templateName, String mailInfo, List<String> toWho, List<String> ccPeoples,String cc);

}
