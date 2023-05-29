package com.cmoc.modules.srm.vo;

import java.util.List;

import com.cmoc.modules.srm.entity.StkOoBillDelivery;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 出库单
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="stk_oo_billPage对象", description="出库单")
public class StkOoBillPage {

	/**ID*/
	@ApiModelProperty(value = "ID")
    private String id;
	/**单据编号*/
	@Excel(name = "单据编号", width = 15)
	@ApiModelProperty(value = "单据编号")
    private String billNo;
	/**出入库类型(00:领料出库,10:盘亏)*/
	@Excel(name = "出入库类型(00:领料出库,10:盘亏)", width = 15)
	@ApiModelProperty(value = "出入库类型(00:领料出库,10:盘亏)")
    private String stockIoType;
	/**单据日期*/
	@Excel(name = "单据日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "单据日期")
    private Date billDate;
	/**领用人ID*/
	@Excel(name = "领用人ID", width = 15)
	@ApiModelProperty(value = "领用人ID")
    private String clerkId;
	/**附件*/
	@Excel(name = "附件", width = 15)
	@ApiModelProperty(value = "附件")
    private String attachment;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private String remark;
	/**创建部门*/
	@ApiModelProperty(value = "创建部门")
    private String sysOrgCode;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改人*/
	@ApiModelProperty(value = "修改人")
    private String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "修改时间")
    private Date updateTime;
	/**仓库ID*/
	@Excel(name = "仓库ID", width = 15)
	@ApiModelProperty(value = "仓库ID")
    private String whId;
	/**设备安装点*/
	@Excel(name = "设备安装点", width = 15)
	@ApiModelProperty(value = "设备安装点")
    private String equAdd;

	@ExcelCollection(name="出库明细")
	@ApiModelProperty(value = "出库明细")
	private List<StkOoBillDelivery> stkOoBillDeliveryList;

}
