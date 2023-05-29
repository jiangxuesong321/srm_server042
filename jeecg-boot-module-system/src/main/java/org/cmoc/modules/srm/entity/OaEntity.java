package org.cmoc.modules.srm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 合同基本信息表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Data
public class OaEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String workcode;

    private String lastname;

    private String sex;

    private String jobtitlename;

    private String gwzt;

    private String email;

    private String firstid;

    private String firstdep;

    private String secendid;

    private String secenddep;

    private String thirthid;

    private String thirthdep;

    private String thirdid;

    private String thirddep;

    private String fourid;

    private String fourdep;

    private String fiveid;

    private String fivedep;

    private String sixid;

    private String sixdep;

}
