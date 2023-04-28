package org.jeecg.modules.srm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.modules.srm.entity.PurchaseRequestDetailApprove;
import org.jeecg.modules.srm.entity.PurchaseRequestMain;
import org.jeecg.modules.srm.entity.PurchaseRequestDetail;
import org.jeecg.modules.srm.entity.PurchaseRequestMainApprove;
import org.jeecg.modules.srm.mapper.PurchaseRequestDetailApproveMapper;
import org.jeecg.modules.srm.mapper.PurchaseRequestDetailMapper;
import org.jeecg.modules.srm.mapper.PurchaseRequestMainMapper;
import org.jeecg.modules.srm.service.IPurchaseRequestDetailApproveService;
import org.jeecg.modules.srm.service.IPurchaseRequestDetailService;
import org.jeecg.modules.srm.service.IPurchaseRequestMainApproveService;
import org.jeecg.modules.srm.service.IPurchaseRequestMainService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: purchase_request_main
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Service
public class PurchaseRequestMainServiceImpl extends ServiceImpl<PurchaseRequestMainMapper, PurchaseRequestMain> implements IPurchaseRequestMainService {

	@Autowired
	private IPurchaseRequestDetailService iPurchaseRequestDetailService;
	@Autowired
	private IPurchaseRequestDetailApproveService iPurchaseRequestDetailApproveService;
	@Autowired
	private IPurchaseRequestMainApproveService iPurchaseRequestMainApproveService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PurchaseRequestMain purchaseRequestMain, List<PurchaseRequestDetail> purchaseRequestDetailList) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();

		JSONObject formData = new JSONObject();
		formData.put("prefix", "PR");
		String code = (String) FillRuleUtil.executeRule("pur_code", formData);
		purchaseRequestMain.setReqCode(code);
		purchaseRequestMain.setReqDate(nowTime);
		purchaseRequestMain.setId(String.valueOf(IdWorker.getId()));
		BigDecimal orderTotalAmount = BigDecimal.ZERO;
		BigDecimal orderTotalAmountTax = BigDecimal.ZERO;
		if(purchaseRequestDetailList!=null && purchaseRequestDetailList.size()>0) {
			int i = 1;
			for(PurchaseRequestDetail entity:purchaseRequestDetailList) {
				//外键设置
				entity.setReqId(purchaseRequestMain.getId());
				entity.setDelFlag(CommonConstant.NO_READ_FLAG);
				entity.setCreateTime(nowTime);
				entity.setCreateUser(username);
				entity.setUpdateTime(nowTime);
				entity.setUpdateUser(username);
				if(StringUtils.isEmpty(entity.getProdCode())){

					String prodCode = RandomStringUtils.random(8, "ABCDEFGHIJKLNMOPQRSTUVWXYZ1234567890");
					entity.setProdCode(prodCode);
					entity.setProdId(prodCode);
					i++;
				}
				entity.setId(UUIDGenerator.generate());
				if(purchaseRequestMain.getReqCategory().equals("0")) {
					orderTotalAmount = orderTotalAmount.add(entity.getOrderAmount());
					orderTotalAmountTax = orderTotalAmountTax.add(entity.getOrderAmountTax());
				}
			}
			iPurchaseRequestDetailService.saveBatch(purchaseRequestDetailList);
		}
		purchaseRequestMain.setReqStatus("1");
		purchaseRequestMain.setOrderTotalAmount(orderTotalAmount);
		purchaseRequestMain.setOrderTotalAmountTax(orderTotalAmountTax);
		purchaseRequestMain.setDelFlag(CommonConstant.NO_READ_FLAG);
		purchaseRequestMain.setCreateTime(nowTime);
		purchaseRequestMain.setCreateUser(username);
		purchaseRequestMain.setUpdateTime(nowTime);
		purchaseRequestMain.setUpdateUser(username);
		this.save(purchaseRequestMain);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PurchaseRequestMain purchaseRequestMain,List<PurchaseRequestDetail> purchaseRequestDetailList) {
		//1.先删除子表数据
		UpdateWrapper<PurchaseRequestDetail> updateWrapper = new UpdateWrapper<>();
		updateWrapper.set("del_flag", CommonConstant.ACT_SYNC_1);
		updateWrapper.eq("req_id",purchaseRequestMain.getId());
		iPurchaseRequestDetailService.update(updateWrapper);

		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();

		//2.子表数据重新插入
		BigDecimal orderTotalAmount = BigDecimal.ZERO;
		BigDecimal orderTotalAmountTax = BigDecimal.ZERO;

		if(purchaseRequestDetailList!=null && purchaseRequestDetailList.size()>0) {
			for(PurchaseRequestDetail entity:purchaseRequestDetailList) {
				//外键设置
				entity.setReqId(purchaseRequestMain.getId());
				entity.setCreateTime(nowTime);
				entity.setCreateUser(username);
				entity.setUpdateTime(nowTime);
				entity.setUpdateUser(username);
				entity.setDelFlag(CommonConstant.NO_READ_FLAG);
				if(purchaseRequestMain.getReqCategory().equals("0")) {
					orderTotalAmount = orderTotalAmount.add(entity.getOrderAmount());
					orderTotalAmountTax = orderTotalAmountTax.add(entity.getOrderAmountTax());
				}
			}
			iPurchaseRequestDetailService.saveOrUpdateBatch(purchaseRequestDetailList);
		}
		purchaseRequestMain.setUpdateTime(nowTime);
		purchaseRequestMain.setUpdateUser(username);
		purchaseRequestMain.setReqStatus("1");
		purchaseRequestMain.setOrderTotalAmount(orderTotalAmount);
		purchaseRequestMain.setOrderTotalAmountTax(orderTotalAmountTax);
		purchaseRequestMain.setBuyerId("");
		purchaseRequestMain.setApproveComment("");
		this.updateById(purchaseRequestMain);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {

	}

	/**
	 * 草稿
	 * @param purchaseRequestMain
	 * @param purchaseRequestDetailList
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void draft(PurchaseRequestMain purchaseRequestMain, List<PurchaseRequestDetail> purchaseRequestDetailList) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();

		//1.先删除子表数据
		if(StringUtils.isNotEmpty(purchaseRequestMain.getId())){
			UpdateWrapper<PurchaseRequestDetail> updateWrapper = new UpdateWrapper<>();
			updateWrapper.set("del_flag", CommonConstant.ACT_SYNC_1);
			updateWrapper.eq("req_id",purchaseRequestMain.getId());
			iPurchaseRequestDetailService.update(updateWrapper);
		}else{
			JSONObject formData = new JSONObject();
			formData.put("prefix", "PR");
			String code = (String) FillRuleUtil.executeRule("pur_code", formData);
			purchaseRequestMain.setReqCode(code);
			purchaseRequestMain.setId(String.valueOf(IdWorker.getId()));
			purchaseRequestMain.setReqDate(nowTime);
		}

		//2.子表数据重新插入
		BigDecimal orderTotalAmount = BigDecimal.ZERO;
		BigDecimal orderTotalAmountTax = BigDecimal.ZERO;

		if(purchaseRequestDetailList!=null && purchaseRequestDetailList.size()>0) {
			for(PurchaseRequestDetail entity:purchaseRequestDetailList) {
				//外键设置
				entity.setReqId(purchaseRequestMain.getId());
				entity.setDelFlag(CommonConstant.NO_READ_FLAG);
				entity.setCreateTime(nowTime);
				entity.setCreateUser(username);
				entity.setUpdateTime(nowTime);
				entity.setUpdateUser(username);
				if(purchaseRequestMain.getReqCategory().equals("0")) {
					orderTotalAmount = orderTotalAmount.add(entity.getOrderAmount());
					orderTotalAmountTax = orderTotalAmountTax.add(entity.getOrderAmountTax());
				}
			}
			iPurchaseRequestDetailService.saveOrUpdateBatch(purchaseRequestDetailList);
		}
		purchaseRequestMain.setDelFlag(CommonConstant.NO_READ_FLAG);
		purchaseRequestMain.setCreateTime(nowTime);
		purchaseRequestMain.setCreateUser(username);
		purchaseRequestMain.setUpdateTime(nowTime);
		purchaseRequestMain.setUpdateUser(username);
		purchaseRequestMain.setReqStatus("0");
		purchaseRequestMain.setOrderTotalAmount(orderTotalAmount);
		purchaseRequestMain.setOrderTotalAmountTax(orderTotalAmountTax);
		this.saveOrUpdate(purchaseRequestMain);
	}

	/**
	 * 驳回
	 * @param purchaseRequestMain
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void toReject(PurchaseRequestMain purchaseRequestMain) {
		Date nowTime = new Date();
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();

		String id = purchaseRequestMain.getId();
		PurchaseRequestMain oldMain = this.getById(id);
		oldMain.setUpdateTime(nowTime);
		oldMain.setUpdateUser(username);
		oldMain.setReqStatus("3");
		oldMain.setApproveComment(purchaseRequestMain.getApproveComment());
		oldMain.setApproveTime(nowTime);
		this.updateById(oldMain);

		String nId = String.valueOf(IdWorker.getId());
		PurchaseRequestMainApprove apMain = new PurchaseRequestMainApprove();
		BeanUtils.copyProperties(oldMain,apMain);
		apMain.setId(nId);
		iPurchaseRequestMainApproveService.save(apMain);

		List<PurchaseRequestDetail> oldList = iPurchaseRequestDetailService.list(Wrappers.<PurchaseRequestDetail>query().lambda().
				eq(PurchaseRequestDetail :: getReqId,id).
				eq(PurchaseRequestDetail :: getDelFlag,CommonConstant.DEL_FLAG_0));
		List<PurchaseRequestDetailApprove> apList = new ArrayList<>();
		for(PurchaseRequestDetail pd : oldList){
			PurchaseRequestDetailApprove ap = new PurchaseRequestDetailApprove();
			BeanUtils.copyProperties(pd,ap);
			ap.setUpdateTime(nowTime);
			ap.setUpdateUser(username);
			ap.setReqId(nId);
			ap.setId(String.valueOf(IdWorker.getId()));
			apList.add(ap);
		}
		iPurchaseRequestDetailApproveService.saveBatch(apList);
	}

	/**
	 * 同意
	 * @param purchaseRequestMain
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void toAgree(PurchaseRequestMain purchaseRequestMain) {
		Date nowTime = new Date();
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();

		String id = purchaseRequestMain.getId();
		PurchaseRequestMain oldMain = this.getById(id);
		oldMain.setUpdateTime(nowTime);
		oldMain.setUpdateUser(username);
		oldMain.setReqStatus("2");
		oldMain.setApproveComment(purchaseRequestMain.getApproveComment());
		oldMain.setBuyerId(purchaseRequestMain.getBuyerId());
		oldMain.setApproveTime(nowTime);
		this.updateById(oldMain);

		List<PurchaseRequestDetail> detailList = purchaseRequestMain.getDetailList();
		iPurchaseRequestDetailService.updateBatchById(detailList);

//		String nId = String.valueOf(IdWorker.getId());
//		PurchaseRequestMainApprove apMain = new PurchaseRequestMainApprove();
//		BeanUtils.copyProperties(oldMain,apMain);
//		apMain.setId(nId);
//		iPurchaseRequestMainApproveService.save(apMain);
//
//		List<PurchaseRequestDetail> oldList = iPurchaseRequestDetailService.list(Wrappers.<PurchaseRequestDetail>query().lambda().
//				eq(PurchaseRequestDetail :: getReqId,id).
//				eq(PurchaseRequestDetail :: getDelFlag,CommonConstant.DEL_FLAG_0));
//		List<PurchaseRequestDetailApprove> apList = new ArrayList<>();
//		for(PurchaseRequestDetail pd : oldList){
//			PurchaseRequestDetailApprove ap = new PurchaseRequestDetailApprove();
//			BeanUtils.copyProperties(pd,ap);
//			ap.setUpdateTime(nowTime);
//			ap.setUpdateUser(username);
//			ap.setReqId(nId);
//			ap.setId(String.valueOf(IdWorker.getId()));
//			apList.add(ap);
//		}
//		iPurchaseRequestDetailApproveService.saveBatch(apList);
	}

	/**
	 * 配套tab页
	 * @param page
	 * @param pmain
	 * @return
	 */
	@Override
	public IPage<PurchaseRequestMain> fetchChildList(Page<PurchaseRequestMain> page, PurchaseRequestMain pmain) {
		return baseMapper.fetchChildList(page,pmain);
	}

	/**
	 * 以项目统计需求金额
	 * @param purchaseRequestMain
	 * @return
	 */
	@Override
	public PurchaseRequestMain fetchRequestByProjId(PurchaseRequestMain purchaseRequestMain) {
		return baseMapper.fetchRequestByProjId(purchaseRequestMain);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param purchaseRequestMain
	 * @return
	 */
	@Override
	public IPage<PurchaseRequestMain> pageList(Page<PurchaseRequestMain> page, PurchaseRequestMain purchaseRequestMain) {
		return baseMapper.pageList(page,purchaseRequestMain);
	}

	/**
	 * 获取分类最大金额
	 * @param purchaseRequestMain
	 * @return
	 */
	@Override
	public PurchaseRequestMain fetchMaxAmount(PurchaseRequestMain purchaseRequestMain) {
		return baseMapper.fetchMaxAmount(purchaseRequestMain);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param purchaseRequestMain
	 * @return
	 */
	@Override
	public IPage<PurchaseRequestMain> queryPageList(Page<PurchaseRequestMain> page, PurchaseRequestMain purchaseRequestMain) {
		return baseMapper.queryPageList(page,purchaseRequestMain);
	}

	@Override
	public IPage<PurchaseRequestMain> queryPageGoodsList(Page<PurchaseRequestMain> page, PurchaseRequestMain purchaseRequestMain) {
		return baseMapper.queryPageGoodsList(page,purchaseRequestMain);
	}

	/**
	 * 报价附件
	 * @param purchaseRequestMain
	 * @return
	 */
	@Override
	public List<Map<String, String>> fetchQuoteFile(PurchaseRequestMain purchaseRequestMain) {
		return baseMapper.fetchQuoteFile(purchaseRequestMain);
	}


}
