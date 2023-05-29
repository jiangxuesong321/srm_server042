package com.cmoc.modules.srm.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description: 合同基本信息表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Data
public class FileParam implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String column;

    private String table;

    private List<Map<String,String>> urlList;

    private String url;
}
