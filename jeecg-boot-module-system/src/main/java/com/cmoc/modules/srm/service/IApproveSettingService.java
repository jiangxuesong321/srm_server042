package com.cmoc.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmoc.modules.srm.entity.ApproveSetting;

/**
 * @Description: 附件记录表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface IApproveSettingService extends IService<ApproveSetting> {
    /**
     * 保存配置项
     * @param approveSetting
     */
    void saveEntity(ApproveSetting approveSetting);

    /**
     * 审核人列表
     * @param page
     * @param approveSetting
     * @return
     */
    IPage<ApproveSetting> queryPageList(Page<ApproveSetting> page, ApproveSetting approveSetting);
}
