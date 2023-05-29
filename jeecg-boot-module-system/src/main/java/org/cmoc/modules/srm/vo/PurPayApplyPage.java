package org.cmoc.modules.srm.vo;

import java.util.List;
import org.cmoc.modules.srm.entity.PurPayApply;
import org.cmoc.modules.srm.entity.PurPayApplyDetail;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.cmoc.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 付款申请
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Data
@ApiModel(value="pur_pay_applyPage对象", description="付款申请")
public class PurPayApplyPage {

	/**采购付款申请ID*/
	@ApiModelProperty(value = "采购付款申请ID")
    private String id;
	/**付款申请单号*/
	@Excel(name = "付款申请单号", width = 15)
	@ApiModelProperty(value = "付款申请单号")
    private String applyCode;
	/**付款金额*/
	@Excel(name = "付款金额", width = 15)
	@ApiModelProperty(value = "付款金额")
    private java.math.BigDecimal payAmount;
	/**税率*/
	@Excel(name = "税率", width = 15)
	@ApiModelProperty(value = "税率")
    private java.math.BigDecimal taxRate;
	/**付款方式*/
	@Excel(name = "付款方式", width = 15)
	@ApiModelProperty(value = "付款方式")
    private String payMethod;
	/**币种*/
	@Excel(name = "币种", width = 15)
	@ApiModelProperty(value = "币种")
    private String currency;
	/**申请理由*/
	@Excel(name = "申请理由", width = 15)
	@ApiModelProperty(value = "申请理由")
    private String applyReason;
	/**申请状态：00：待审核10：已驳回20:已受理,30:部分付款,40:付款完成*/
	@Excel(name = "申请状态：00：待审核10：已驳回20:已受理,30:部分付款,40:付款完成", width = 15)
	@ApiModelProperty(value = "申请状态：00：待审核10：已驳回20:已受理,30:部分付款,40:付款完成")
    private String applyStatus;
	/**申请时间*/
	@Excel(name = "申请时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "申请时间")
    private Date applyTime;
	/**收款开户行*/
	@Excel(name = "收款开户行", width = 15)
	@ApiModelProperty(value = "收款开户行")
    private String receivingBank;
	/**收款账号*/
	@Excel(name = "收款账号", width = 15)
	@ApiModelProperty(value = "收款账号")
    private String receivingNumber;
	/**应付款日期*/
	@Excel(name = "应付款日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "应付款日期")
    private Date dueDate;
	/**供应商id*/
	@Excel(name = "供应商id", width = 15)
	@ApiModelProperty(value = "供应商id")
    private String suppId;
	/**驳回理由*/
	@Excel(name = "驳回理由", width = 15)
	@ApiModelProperty(value = "驳回理由")
    private String returnReason;
	/**是否需要发票*/
	@Excel(name = "是否需要发票", width = 15)
	@ApiModelProperty(value = "是否需要发票")
    private String isNeedInvoice;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
    private String comment;
	/**排序*/
	@Excel(name = "排序", width = 15)
	@ApiModelProperty(value = "排序")
    private Integer sort;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
	@ApiModelProperty(value = "租户ID")
    private String tenantId;
	/**删除标志位*/
	@Excel(name = "删除标志位", width = 15)
	@ApiModelProperty(value = "删除标志位")
    private String delFlag;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	@ApiModelProperty(value = "创建人")
    private String createUser;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	@ApiModelProperty(value = "修改人")
    private String updateUser;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "修改时间")
    private Date updateTime;
	/**付款状态【0：未付款；1：部分付款；2：已完成付款；3已关闭】*/
	@Excel(name = "付款状态【0：未付款；1：部分付款；2：已完成付款；3已关闭】", width = 15)
	@ApiModelProperty(value = "付款状态【0：未付款；1：部分付款；2：已完成付款；3已关闭】")
    private String payStatus;
	/**审核人*/
	@Excel(name = "审核人", width = 15)
	@ApiModelProperty(value = "审核人")
    private String auditor;
	/**审核日期*/
	@Excel(name = "审核日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "审核日期")
    private Date auditorDate;
	/**发票金额*/
	@Excel(name = "发票金额", width = 15)
	@ApiModelProperty(value = "发票金额")
    private java.math.BigDecimal invoiceAmount;
	/**发票号*/
	@Excel(name = "发票号", width = 15)
	@ApiModelProperty(value = "发票号")
    private String invoiceNumber;
	/**发票时间*/
	@Excel(name = "发票时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "发票时间")
    private Date invoiceDate;
	/**发票代码*/
	@Excel(name = "发票代码", width = 15)
	@ApiModelProperty(value = "发票代码")
    private String invoiceCode;
	/**审批人ID*/
	@Excel(name = "审批人ID", width = 15)
	@ApiModelProperty(value = "审批人ID")
    private String approvalUserId;
	/**审批人*/
	@Excel(name = "审批人", width = 15)
	@ApiModelProperty(value = "审批人")
    private String approvalUserName;
	/**审批时间*/
	@Excel(name = "审批时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "审批时间")
    private Date approvalTime;
	/**付款类型：00：预付款 10：月结款*/
	@Excel(name = "付款类型：00：预付款 10：月结款", width = 15)
	@ApiModelProperty(value = "付款类型：00：预付款 10：月结款")
    private String payType;
	/**贸易商自己的付款方式*/
	@Excel(name = "贸易商自己的付款方式", width = 15)
	@ApiModelProperty(value = "贸易商自己的付款方式")
    private String traderPayment;
	/**付款追加类型*/
	@Excel(name = "付款追加类型", width = 15)
	@ApiModelProperty(value = "付款追加类型")
    private String addToType;
	/**流程实例ID*/
	@Excel(name = "流程实例ID", width = 15)
	@ApiModelProperty(value = "流程实例ID")
    private String processInstanceId;
	/**审批状态(0:待审批,1:审批通过)*/
	@Excel(name = "审批状态(0:待审批,1:审批通过)", width = 15)
	@ApiModelProperty(value = "审批状态(0:待审批,1:审批通过)")
    private String checkStatus;
	/**受理审批*/
	@Excel(name = "受理审批", width = 15)
	@ApiModelProperty(value = "受理审批")
    private String acceptComment;
	/**是否整单(1：整单支付 2：部分支付)*/
	@Excel(name = "是否整单(1：整单支付 2：部分支付)", width = 15)
	@ApiModelProperty(value = "是否整单(1：整单支付 2：部分支付)")
    private String isWholeSheet;
	/**付款比例*/
	@Excel(name = "付款比例", width = 15)
	@ApiModelProperty(value = "付款比例")
    private java.math.BigDecimal payRate;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
	@ApiModelProperty(value = "供应商名称")
    private String suppName;
	/**付款申请金额*/
	@Excel(name = "付款申请金额", width = 15)
	@ApiModelProperty(value = "付款申请金额")
    private java.math.BigDecimal applyPayAmount;
	/**付款申请来源（00：非月结，10月结）*/
	@Excel(name = "付款申请来源（00：非月结，10月结）", width = 15)
	@ApiModelProperty(value = "付款申请来源（00：非月结，10月结）")
    private String applySource;
	/**申请附件地址*/
	@Excel(name = "申请附件地址", width = 15)
	@ApiModelProperty(value = "申请附件地址")
    private String attachment;
	/**实际付款金额*/
	@Excel(name = "实际付款金额", width = 15)
	@ApiModelProperty(value = "实际付款金额")
    private java.math.BigDecimal actPayAmount;

	@ExcelCollection(name="付款申请明细")
	@ApiModelProperty(value = "付款申请明细")
	private List<PurPayApplyDetail> purPayApplyDetailList;

}
