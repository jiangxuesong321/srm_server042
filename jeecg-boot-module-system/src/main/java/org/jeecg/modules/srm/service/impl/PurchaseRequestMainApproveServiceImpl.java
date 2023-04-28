package org.jeecg.modules.srm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.modules.srm.entity.PurchaseRequestDetailApprove;
import org.jeecg.modules.srm.entity.PurchaseRequestDetailApprove;
import org.jeecg.modules.srm.entity.PurchaseRequestMainApprove;
import org.jeecg.modules.srm.entity.PurchaseRequestMainApprove;
import org.jeecg.modules.srm.mapper.PurchaseRequestMainApproveMapper;
import org.jeecg.modules.srm.service.IPurchaseRequestDetailApproveService;
import org.jeecg.modules.srm.service.IPurchaseRequestDetailService;
import org.jeecg.modules.srm.service.IPurchaseRequestMainApproveService;
import org.jeecg.modules.srm.service.IPurchaseRequestMainApproveService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Description: purchase_request_main
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Service
public class PurchaseRequestMainApproveServiceImpl extends ServiceImpl<PurchaseRequestMainApproveMapper, PurchaseRequestMainApprove> implements IPurchaseRequestMainApproveService {



}
