package org.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.common.system.vo.LoginUser;
import org.cmoc.modules.srm.entity.ApproveSetting;
import org.cmoc.modules.srm.entity.AttachmentFileRecord;
import org.cmoc.modules.srm.mapper.ApproveSettingMapper;
import org.cmoc.modules.srm.mapper.AttachmentFileRecordMapper;
import org.cmoc.modules.srm.service.IApproveSettingService;
import org.cmoc.modules.srm.service.IAttachmentFileRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 附件记录表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Service
public class ApproveSettingServiceImpl extends ServiceImpl<ApproveSettingMapper, ApproveSetting> implements IApproveSettingService {
    /**
     * 保存配置项
     * @param approveSetting
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(ApproveSetting approveSetting) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        Date nowTime = new Date();

        List<ApproveSetting> asList = new ArrayList<>();

        String[] typeList = approveSetting.getType().split(",");
        String payMethod = approveSetting.getPayMethod();
        String[] payMethodList = new String[]{};
        if(StringUtils.isNotEmpty(payMethod)){
            payMethodList = payMethod.split(",");
        }
        String company = approveSetting.getCompany();
        String[] companyList = new String[]{};
        if(StringUtils.isNotEmpty(company)){
            companyList = company.split(",");
        }

        for(String type : typeList){
            if("supp".equals(type)){
                ApproveSetting ass = new ApproveSetting();
                ass.setId(String.valueOf(IdWorker.getId()));
                ass.setDelFlag(CommonConstant.NO_READ_FLAG);
                ass.setCreateTime(nowTime);
                ass.setCreateBy(username);
                ass.setUpdateTime(nowTime);
                ass.setUpdateBy(username);
                ass.setUsername(approveSetting.getUsername());
                ass.setType(type);
                asList.add(ass);
            }else if("pay".equals(type)){
                for(String pm : payMethodList){
                    for(String co : companyList){
                        ApproveSetting ass = new ApproveSetting();
                        ass.setId(String.valueOf(IdWorker.getId()));
                        ass.setDelFlag(CommonConstant.NO_READ_FLAG);
                        ass.setCreateTime(nowTime);
                        ass.setCreateBy(username);
                        ass.setUpdateTime(nowTime);
                        ass.setUpdateBy(username);
                        ass.setUsername(approveSetting.getUsername());
                        ass.setType(type);
                        //供应商 不存在 主体、付款方式
                        if(!"supp".equals(type)){
                            ass.setCompany(co);
                            // 只有 付款申请 才有 付款方式
                            if("pay".equals(type)){
                                ass.setPayMethod(pm);
                            }
                        }

                        asList.add(ass);
                    }
                }
            }
            else {
                for (String co : companyList) {
                    ApproveSetting ass = new ApproveSetting();
                    ass.setId(String.valueOf(IdWorker.getId()));
                    ass.setDelFlag(CommonConstant.NO_READ_FLAG);
                    ass.setCreateTime(nowTime);
                    ass.setCreateBy(username);
                    ass.setUpdateTime(nowTime);
                    ass.setUpdateBy(username);
                    ass.setUsername(approveSetting.getUsername());
                    ass.setType(type);
                    //供应商 不存在 主体、付款方式
                    if (!"supp".equals(type)) {
                        ass.setCompany(co);
                    }
                    asList.add(ass);
                }
            }

        }
        this.saveBatch(asList);
    }

    /**
     * 审核人列表
     * @param page
     * @param approveSetting
     * @return
     */
    @Override
    public IPage<ApproveSetting> queryPageList(Page<ApproveSetting> page, ApproveSetting approveSetting) {
        return baseMapper.queryPageList(page,approveSetting);
    }
}
