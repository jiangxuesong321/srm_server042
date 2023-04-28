package org.jeecg.modules.srm.service;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.srm.entity.BasSupplierContact;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 供应商联系人
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
public interface IBasSupplierContactService extends IService<BasSupplierContact> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<BasSupplierContact>
	 */
	public List<BasSupplierContact> selectByMainId(String mainId);

	boolean deleteByMainId(String mainId);
}
