package com.cmoc.modules.srm.service;

import com.cmoc.modules.system.entity.SysUser;
import com.cmoc.modules.srm.entity.ApproverCcMail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: approver_cc_mail
 * @Author: jeecg-boot
 * @Date:   2023-02-20
 * @Version: V1.0
 */
public interface IApproverCcMailService extends IService<ApproverCcMail> {
    /**
     *
     * @author Kevin.Wang
     * @date : 2023/2/20 16:23
     * @param
     * @return List<SysUser>
     **/
    List<SysUser> selectUserMails();
}
