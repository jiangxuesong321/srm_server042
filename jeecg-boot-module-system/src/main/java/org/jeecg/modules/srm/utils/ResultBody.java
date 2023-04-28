package org.jeecg.modules.srm.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author ：Kevin.Wang
 * @date ：Created in 2022年09月15日 9:47
 * @description：
 * @modified By：
 * @version:
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ResultBody {

    /** 成功状态：0 为成功，非0为不成功 */
    private Long retCode;
    /** 返回的 消息内容 */
    private String retMessage;

    private String returnMsgBodyXmlString;
}
