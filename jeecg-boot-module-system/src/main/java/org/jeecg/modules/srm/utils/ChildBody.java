package org.jeecg.modules.srm.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author ：Kevin.Wang
 * @date ：Created in 2022年09月15日 15:00
 * @description：
 * @modified By：
 * @version:
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Childs1")
@Data
public class ChildBody {
    /**合同编号**/
    @XmlElement(name="htbh")
    private String htbh;
    /**word附件**/
    @XmlElement(name="ht1")
    private List<Map<String,String>> ht1;
    /**其他附件**/
    @XmlElement(name="fj1")
    private List<Map<String,String>> fj1;
    /**盖章分数**/
    @XmlElement(name="gzfs")
    private String gzfs;

}
