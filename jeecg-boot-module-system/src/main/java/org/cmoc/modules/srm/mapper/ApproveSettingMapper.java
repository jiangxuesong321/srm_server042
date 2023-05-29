package org.cmoc.modules.srm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.cmoc.modules.srm.entity.ApproveSetting;
import org.cmoc.modules.srm.entity.AttachmentFileRecord;

/**
 * @Description: 附件记录表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
public interface ApproveSettingMapper extends BaseMapper<ApproveSetting> {
    /**
     * 审核人配置列表
     * @param page
     * @param approveSetting
     * @return
     */
    IPage<ApproveSetting> queryPageList(Page<ApproveSetting> page, @Param("query") ApproveSetting approveSetting);
}
