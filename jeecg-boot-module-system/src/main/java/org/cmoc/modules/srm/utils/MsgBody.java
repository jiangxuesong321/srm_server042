package org.cmoc.modules.srm.utils;

import com.alibaba.fastjson.annotation.JSONField;
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
 * @date ：Created in 2022年09月10日 10:47
 * @description：
 * @modified By：
 * @version:
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="msgBody")
@Data
public class MsgBody {
    /****/
    private String billid;
    /**发起人工号**/
    private String creatId;
    private String workflowid;
    /**合同单位**/
    private String ht;
    private String wfdw;
    /**合同分数**/
    private String htfs;
    /**合同金额(万元)**/
    private BigDecimal htje;
    /**背景描述**/
    private String bjms;
    /**合同内容**/
    private String htnr;
    /**结算条款**/
    private String jstk;
    /**需求申请**/
    private List<Map<String,String>> xqsq;
    /**过会文件**/
    private List<Map<String,String>> ghwj;
    /**其他支撑**/
    private List<Map<String,String>> qtzc;
    /**招标比价**/
    private List<Map<String,String>> bjbj;
    /**项目外部文件**/
    private List<Map<String,String>> lxwj;

    @JSONField(name="Childs1")
    @XmlElement(name = "Childs1")
    private List<ChildBody> Childs1;
    @JSONField(name="Childs2")
    @XmlElement(name = "Childs2")
    private List<ChildBody2> Childs2;
    @JSONField(name="Childs3")
    @XmlElement(name = "Childs3")
    private List<ChildBody3> Childs3;
    /**合同名称**/
    private String htmc;

    private String sqgs;

    private String xmmc;

    private String hth;

    private List<Map<String,String>> scfj;

    private String cjdw;

    private String khx;

    private String zh;

    private String zffs;

    private String qklx;

    private String jsfs;

    private BigDecimal rmbje;

    private BigDecimal bcqkjehjxx1;

    private String sflrzjjh;

    private String bb;

    private String htgc;

    /**来源**/
    private String source;


}
