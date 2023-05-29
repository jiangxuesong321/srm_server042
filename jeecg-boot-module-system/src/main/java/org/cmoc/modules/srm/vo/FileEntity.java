package org.cmoc.modules.srm.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: proj_base
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Data
public class FileEntity {
    private static final long serialVersionUID = 1L;

    private String name;

    private byte[] value;
}
