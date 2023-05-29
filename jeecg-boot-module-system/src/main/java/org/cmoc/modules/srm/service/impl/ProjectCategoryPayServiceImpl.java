package org.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.shiro.SecurityUtils;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.common.system.vo.LoginUser;
import org.cmoc.modules.srm.entity.ProjectCategory;
import org.cmoc.modules.srm.entity.ProjectCategoryPay;
import org.cmoc.modules.srm.mapper.ProjectCategoryPayMapper;
import org.cmoc.modules.srm.service.IProjectCategoryPayService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Description: project_category_pay
 * @Author: jeecg-boot
 * @Date:   2022-12-05
 * @Version: V1.0
 */
@Service
public class ProjectCategoryPayServiceImpl extends ServiceImpl<ProjectCategoryPayMapper, ProjectCategoryPay> implements IProjectCategoryPayService {
    /**
     * 获取特殊分类已付
     * @param id
     * @return
     */
    @Override
    public Map<String, BigDecimal> fetchHasPayByCategoryId(String id,String type) {
        return baseMapper.fetchHasPayByCategoryId(id,type);
    }

    /**
     * 提交分类支出
     * @param category
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitCategoryPay(ProjectCategory category) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        Date nowTime = new Date();
        //判断该分类是否已存在记录,如果存在则金额替换
        ProjectCategoryPay pay = this.getOne(Wrappers.<ProjectCategoryPay>query().lambda().
                eq(ProjectCategoryPay :: getCategoryId,category.getId()).
                eq(ProjectCategoryPay :: getDelFlag,CommonConstant.DEL_FLAG_0).
                eq(ProjectCategoryPay :: getType,category.getType()));
        BigDecimal payAmount = category.getPayAmount();
        if(pay == null){
            pay = new ProjectCategoryPay();
            pay.setId(String.valueOf(IdWorker.getId()));
            pay.setPayAmount(payAmount);
            pay.setPayAmountLocal(payAmount);
            pay.setPayAmountTax(payAmount);
            pay.setPayAmountLocalTax(payAmount);
            pay.setTaxRate(BigDecimal.ZERO);
            pay.setDelFlag(CommonConstant.NO_READ_FLAG);
            pay.setCreateUser(username);
            pay.setCreateTime(nowTime);
            pay.setUpdateTime(nowTime);
            pay.setUpdateUser(username);
            pay.setCategoryId(category.getId());
            pay.setProjectId(category.getProjectId());
            pay.setType(category.getType());
        }else{
            pay.setPayAmount(payAmount);
            pay.setPayAmountLocal(payAmount);
            pay.setPayAmountTax(payAmount);
            pay.setPayAmountLocalTax(payAmount);

            pay.setUpdateTime(nowTime);
            pay.setUpdateUser(username);
        }


        this.saveOrUpdate(pay);
    }
}
