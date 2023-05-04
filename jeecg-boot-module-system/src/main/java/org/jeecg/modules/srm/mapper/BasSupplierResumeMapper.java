package org.jeecg.modules.srm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.srm.entity.BasSupplierResume;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: bas_supplier_resume
 * @Author: jeecg-boot
 * @Date:   2022-10-25
 * @Version: V1.0
 */
public interface BasSupplierResumeMapper extends BaseMapper<BasSupplierResume> {
    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<BasSupplierResume>
     */
    public List<BasSupplierResume> selectByMainId(@Param("mainId") String mainId);

    /**
     * 通过主表id删除子表数据
     *
     * @param mainId 主表id
     * @return boolean
     */
    public boolean deleteByMainId(@Param("mainId") String mainId);
}
