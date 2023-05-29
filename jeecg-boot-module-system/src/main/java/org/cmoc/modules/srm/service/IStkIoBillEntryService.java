package org.cmoc.modules.srm.service;

import org.cmoc.modules.srm.entity.StkIoBillEntry;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 入库单明细
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
public interface IStkIoBillEntryService extends IService<StkIoBillEntry> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<StkIoBillEntry>
	 */
	public List<StkIoBillEntry> selectByMainId(String mainId);

	/**
	 * 通过主表id查询子表数据
	 * @param id
	 * @return
	 */
    List<StkIoBillEntry> queryDetailListByMainId(String id);
}
