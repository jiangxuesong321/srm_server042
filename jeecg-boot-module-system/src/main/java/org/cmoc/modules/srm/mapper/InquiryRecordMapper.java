package org.cmoc.modules.srm.mapper;

import java.util.List;
import org.cmoc.modules.srm.entity.InquiryRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cmoc.modules.srm.entity.InquirySupplier;

/**
 * @Description: 询价单明细表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface InquiryRecordMapper extends BaseMapper<InquiryRecord> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<InquiryRecord>
   */
	public List<InquiryRecord> selectByMainId(@Param("mainId") String mainId);

	/**
	 * 查看询价
	 * @param id
	 * @return
	 */
    List<InquiryRecord> queryRecordList(@Param("id") String id);

	/**
	 * 询价供应商
	 * @param recordIds
	 * @return
	 */
	List<InquirySupplier> getRecordToSupp(@Param("ids") List<String> recordIds);

	/**
	 * 询价明细
	 * @param id
	 * @return
	 */
    List<InquiryRecord> getRecordById(@Param("id") String id);
}
