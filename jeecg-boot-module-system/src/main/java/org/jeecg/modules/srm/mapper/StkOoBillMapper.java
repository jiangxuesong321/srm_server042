package org.jeecg.modules.srm.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.srm.entity.StkOoBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 出库单
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
public interface StkOoBillMapper extends BaseMapper<StkOoBill> {
    /**
     * 分页
     * @param page
     * @param stkOoBill
     * @return
     */
    IPage<StkOoBill> queryPageList(Page<StkOoBill> page, @Param("query") StkOoBill stkOoBill);
}
