package org.cmoc.modules.srm.service;

import org.cmoc.modules.srm.entity.PurPayApplyDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 付款申请明细
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface IPurPayApplyDetailService extends IService<PurPayApplyDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PurPayApplyDetail>
	 */
	public List<PurPayApplyDetail> selectByMainId(String mainId);
}
