package org.cmoc.modules.srm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.cmoc.modules.srm.entity.ClickMenuHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: click_menu_history
 * @Author: jeecg-boot
 * @Date:   2022-10-21
 * @Version: V1.0
 */
public interface ClickMenuHistoryMapper extends BaseMapper<ClickMenuHistory> {
    /**
     * 查询
     * @param clickMenuHistory
     * @return
     */
    List<ClickMenuHistory> queryList(@Param("query") ClickMenuHistory clickMenuHistory);
}
