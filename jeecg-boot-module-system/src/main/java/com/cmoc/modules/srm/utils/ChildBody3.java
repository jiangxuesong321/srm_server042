package com.cmoc.modules.srm.utils;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * @author ：Kevin.Wang
 * @date ：Created in 2022年09月15日 15:00
 * @description：
 * @modified By：
 * @version:
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Childs3")
@Data
public class ChildBody3 {
    /**本期请款金额**/
    @XmlElement(name="bqqkje")
    private BigDecimal bqqkje;
    /**本期请款占比**/
    @XmlElement(name="bqqk")
    private BigDecimal bqqk;
    /**累计请款金额**/
    @XmlElement(name="ljqkje")
    private BigDecimal ljqkje;
    /**累计请款占比%**/
    @XmlElement(name="ljqk")
    private BigDecimal ljqk;
    /**累计已提供发票金额**/
    @XmlElement(name="ljytgfpje")
    private BigDecimal ljytgfpje;
    /**款项说明**/
    @XmlElement(name="kxsm")
    private String kxsm;

}
