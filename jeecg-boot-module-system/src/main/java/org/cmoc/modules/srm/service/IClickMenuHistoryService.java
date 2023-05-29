package org.cmoc.modules.srm.service;

import org.cmoc.modules.srm.entity.ClickMenuHistory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: click_menu_history
 * @Author: jeecg-boot
 * @Date:   2022-10-21
 * @Version: V1.0
 */
public interface IClickMenuHistoryService extends IService<ClickMenuHistory> {
    /**
     * 查询
     * @param clickMenuHistory
     * @return
     */
    List<ClickMenuHistory> queryList(ClickMenuHistory clickMenuHistory);
}
