package com.cmoc.modules.srm.vo;

import com.cmoc.modules.srm.entity.BiddingSupplier;
import lombok.Data;

import java.util.List;

/**
 * @Description: 招标主表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Data
public class BiddingTemplatePage {
	/**模板ID**/
	List<String> ids;
	/****/
	private List<BiddingSupplier> detaiList;
	/**评标意见**/
	private String comment;
	/**专家**/
	private String professionalId;
}
