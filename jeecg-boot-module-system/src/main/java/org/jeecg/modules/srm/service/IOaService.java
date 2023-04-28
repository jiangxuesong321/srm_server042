package org.jeecg.modules.srm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.srm.entity.ApproveRecord;
import org.jeecg.modules.srm.entity.OaEntity;

import java.util.List;

/**
 * @Description: oa
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
public interface IOaService extends IService<OaEntity> {
    /**同步人员**/
    List<OaEntity> queryUserList() throws Exception;
    /**同步部门**/
    List<OaEntity> queryDeptList() throws Exception;
}
