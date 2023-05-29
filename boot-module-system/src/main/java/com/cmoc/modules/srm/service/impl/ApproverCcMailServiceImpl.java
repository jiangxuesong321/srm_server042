package com.cmoc.modules.srm.service.impl;

import com.cmoc.modules.srm.entity.ApproverCcMail;
import com.cmoc.modules.srm.mapper.ApproverCcMailMapper;
import com.cmoc.modules.srm.service.IApproverCcMailService;
import com.cmoc.modules.system.entity.SysUser;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: approver_cc_mail
 * @Author: jeecg-boot
 * @Date:   2023-02-20
 * @Version: V1.0
 */
@Service
public class ApproverCcMailServiceImpl extends ServiceImpl<ApproverCcMailMapper, ApproverCcMail> implements IApproverCcMailService {

    @Override
    public List<SysUser> selectUserMails() {
        //查询配置信息
        String ids = baseMapper.getCcUsersId();

        return baseMapper.selectUserMails(ids);
    }
}
