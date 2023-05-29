package org.cmoc.modules.srm.service;

import org.cmoc.modules.srm.entity.StkOoBillDelivery;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 出库明细
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
public interface IStkOoBillDeliveryService extends IService<StkOoBillDelivery> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<StkOoBillDelivery>
	 */
	public List<StkOoBillDelivery> selectByMainId(String mainId);
}
