package org.jeecg.modules.srm.utils;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ：Kevin.Wang
 * @date ：Created in 2022年09月15日 15:00
 * @description：
 * @modified By：
 * @version:
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Childs2")
@Data
public class ChildBody2 {
    /**对方单位**/
    @XmlElement(name="dfdw")
    private String dfdw;
    /**所在地**/
    @XmlElement(name="szd")
    private String szd;
    /**对方单位联系方式**/
    @XmlElement(name="dfdwlxfs")
    private String dfdwlxfs;

}
