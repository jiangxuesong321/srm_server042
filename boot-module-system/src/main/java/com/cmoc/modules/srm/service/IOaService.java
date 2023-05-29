package com.cmoc.modules.srm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmoc.modules.srm.entity.OaEntity;

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
