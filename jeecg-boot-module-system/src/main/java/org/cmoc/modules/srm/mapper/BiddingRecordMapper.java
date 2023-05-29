package org.cmoc.modules.srm.mapper;

import java.util.List;

import org.cmoc.modules.srm.entity.BiddingProfessionals;
import org.cmoc.modules.srm.entity.BiddingRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cmoc.modules.srm.entity.BiddingSupplier;

/**
 * @Description: 招标明细表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface BiddingRecordMapper extends BaseMapper<BiddingRecord> {

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
   * @return List<BiddingRecord>
   */
	public List<BiddingRecord> selectByMainId(@Param("mainId") String mainId);

	/**
	 * 招标明细
	 * @param id
	 * @return
	 */
    List<BiddingRecord> queryRecordList(@Param("id") String id);

	/**
	 * 招标供应商
	 * @param id
	 * @return
	 */
	List<BiddingSupplier> querySuppList(@Param("id") String id);

	/**
	 * 评标模板
	 * @param id
	 * @return
	 */
	List<BiddingProfessionals> queryTemplateList(@Param("id") String id);

	/**
	 * 招标明细
	 * @param id
	 * @return
	 */
    List<BiddingRecord> queryRecordListByMainId(@Param("id") String id);
}
