package org.cmoc.modules.srm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.common.system.vo.LoginUser;
import org.cmoc.common.util.FillRuleUtil;
import org.cmoc.modules.srm.entity.PurchaseRequestDetailApprove;
import org.cmoc.modules.srm.entity.PurchaseRequestDetailApprove;
import org.cmoc.modules.srm.entity.PurchaseRequestMainApprove;
import org.cmoc.modules.srm.entity.PurchaseRequestMainApprove;
import org.cmoc.modules.srm.mapper.PurchaseRequestMainApproveMapper;
import org.cmoc.modules.srm.service.IPurchaseRequestDetailApproveService;
import org.cmoc.modules.srm.service.IPurchaseRequestDetailService;
import org.cmoc.modules.srm.service.IPurchaseRequestMainApproveService;
import org.cmoc.modules.srm.service.IPurchaseRequestMainApproveService;
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
