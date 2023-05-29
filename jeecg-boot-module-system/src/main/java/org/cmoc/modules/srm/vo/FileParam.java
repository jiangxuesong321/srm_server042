package org.cmoc.modules.srm.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.cmoc.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;
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
