package com.cmoc.modules.srm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmoc.common.constant.CommonConstant;
import com.cmoc.common.system.vo.LoginUser;
import com.cmoc.common.util.FillRuleUtil;
import com.cmoc.modules.srm.entity.*;
import com.cmoc.modules.srm.mapper.StkIoBillEntryMapper;
import com.cmoc.modules.srm.mapper.StkIoBillMapper;
import com.cmoc.modules.srm.service.*;
import org.apache.shiro.SecurityUtils;
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
 * @Description: 入库单
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Service
public class StkIoBillServiceImpl extends ServiceImpl<StkIoBillMapper, StkIoBill> implements IStkIoBillService {

	@Autowired
	private StkIoBillMapper stkIoBillMapper;
	@Autowired
	private StkIoBillEntryMapper stkIoBillEntryMapper;
	@Autowired
	private IStkIoBillEntryService iStkIoBillEntryService;
	@Autowired
	private IContractObjectQtyService iContractObjectQtyService;
	@Autowired
	private IStkOoBillService iStkOoBillService;
	@Autowired
	private IStkOoBillDeliveryService iStkOoBillDeliveryService;
	@Autowired
	private IApproveRecordService iApproveRecordService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(StkIoBill stkIoBill, List<StkIoBillEntry> stkIoBillEntryList) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		String deptId = loginUser.getDepartIds();
		Date nowTime = new Date();

		JSONObject formData = new JSONObject();
		formData.put("prefix", "GE");
		String code = (String) FillRuleUtil.executeRule("IO_CODE", formData);
		//入库记录
		stkIoBill.setId(String.valueOf(IdWorker.getId()));
		stkIoBill.setStockIoType("1");
		stkIoBill.setBillNo(code);
		stkIoBill.setCreateTime(nowTime);
		stkIoBill.setCreateBy(username);
		stkIoBill.setUpdateBy(username);
		stkIoBill.setUpdateTime(nowTime);
		stkIoBill.setDelFlag(CommonConstant.NO_READ_FLAG);
		stkIoBill.setStatus("1");
		this.save(stkIoBill);

		//生成出库记录
		formData = new JSONObject();
		formData.put("prefix", "DO");
		code = (String) FillRuleUtil.executeRule("OO_CODE", formData);

		StkOoBill bill = new StkOoBill();
		BeanUtils.copyProperties(stkIoBill,bill);
		bill.setBillNo(code);
		bill.setStockIoType("0");
		iStkOoBillService.save(bill);

		List<String> ids = new ArrayList<>();
		List<StkOoBillDelivery> ooList = new ArrayList<>();
		if(stkIoBillEntryList!=null && stkIoBillEntryList.size()>0) {
			//金额分摊到每一条明细
			BigDecimal totalAmount = stkIoBill.getTotalAmount().subtract(stkIoBill.getContractAmountTaxLocal());
			int i = 1;
			int size = stkIoBillEntryList.size();
			BigDecimal avgAmount = BigDecimal.ZERO;
			BigDecimal amount = BigDecimal.ZERO;
			if(totalAmount.compareTo(BigDecimal.ZERO) == 1){
				avgAmount = totalAmount.divide(new BigDecimal(size),2,BigDecimal.ROUND_HALF_UP);
			}

			for(StkIoBillEntry entity:stkIoBillEntryList) {
				ids.add(entity.getOrderDetailId());
				//外键设置
				entity.setMid(stkIoBill.getId());
				entity.setId(String.valueOf(IdWorker.getId()));
				entity.setCreateTime(nowTime);
				entity.setCreateBy(username);
				entity.setUpdateBy(username);
				entity.setUpdateTime(nowTime);
				entity.setDelFlag(CommonConstant.NO_READ_FLAG);
				entity.setQty(entity.getQty());
				entity.setStockQty(entity.getQty());

				entity.setWhId(stkIoBill.getWhId());
				entity.setSupplierId(stkIoBill.getSuppId());
				entity.setSuppName(stkIoBill.getSuppName());
				entity.setOrderId(stkIoBill.getContractId());
				entity.setProjectId(stkIoBill.getProjectId());
				entity.setDeptId(deptId);

				StkOoBillDelivery sd = new StkOoBillDelivery();
				BeanUtils.copyProperties(entity,sd);
				sd.setMid(bill.getId());
				sd.setCreateTime(nowTime);
				sd.setUpdateTime(nowTime);
				sd.setDelFlag(CommonConstant.NO_READ_FLAG);

				if(i == size){
					entity.setOtherAmount(totalAmount.subtract(amount).setScale(2,BigDecimal.ROUND_HALF_UP));
				}else{
					entity.setOtherAmount(avgAmount);
				}
				amount = amount.add(avgAmount);
				i++;

				ooList.add(sd);
			}
			iStkIoBillEntryService.saveBatch(stkIoBillEntryList);
			iStkOoBillDeliveryService.saveBatch(ooList);
		}
		//更新发货数量
		UpdateWrapper<ContractObjectQty> updateWrapper = new UpdateWrapper<>();
		updateWrapper.set("to_send_qty",1);
		updateWrapper.in("id",ids);
		iContractObjectQtyService.update(updateWrapper);
	}

	/**
	 * 编辑
	 * @param stkIoBill
	 * @param stkIoBillEntryList
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(StkIoBill stkIoBill,List<StkIoBillEntry> stkIoBillEntryList) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();

		stkIoBill.setUpdateBy(username);
		stkIoBill.setUpdateTime(nowTime);
		this.updateById(stkIoBill);

		BigDecimal exchangeRate = stkIoBill.getExchangeRate();

		//金额分摊到每一条明细
		BigDecimal totalAmount = stkIoBill.getTotalAmount().subtract(stkIoBill.getContractAmountTaxLocal());
//		if(totalAmount.compareTo(BigDecimal.ZERO) == 1){
		int size = stkIoBillEntryList.size();
		BigDecimal avgAmount = totalAmount.divide(new BigDecimal(size),2,BigDecimal.ROUND_HALF_UP);
		BigDecimal amount = BigDecimal.ZERO;
		int i = 1;
		for(StkIoBillEntry sie : stkIoBillEntryList){
			//金额分摊
			if(totalAmount.compareTo(BigDecimal.ZERO) == 1){
				if(i == size){
					sie.setOtherAmountLocal(totalAmount.subtract(amount).setScale(2,BigDecimal.ROUND_HALF_UP));
				}else{
					sie.setOtherAmountLocal(avgAmount);
				}
				BigDecimal otherAmount = sie.getOtherAmountLocal().multiply(exchangeRate).setScale(2,BigDecimal.ROUND_HALF_UP);
				sie.setOtherAmount(otherAmount);
				amount = amount.add(avgAmount);
				i++;
			}
			//根据汇率重新计算合同金额
			BigDecimal priceLocal = sie.getContractPrice().divide(exchangeRate,4,BigDecimal.ROUND_HALF_UP);
			sie.setContractPriceLocal(priceLocal);

			BigDecimal priceTaxLocal = sie.getContractPriceTax().divide(exchangeRate,4,BigDecimal.ROUND_HALF_UP);
			sie.setContractPriceTaxLocal(priceTaxLocal);

			BigDecimal amountLocal = sie.getContractAmount().divide(exchangeRate,2,BigDecimal.ROUND_HALF_UP);
			sie.setContractAmountLocal(amountLocal);

			BigDecimal amountTaxLocal = sie.getContractAmountTax().divide(exchangeRate,2,BigDecimal.ROUND_HALF_UP);
			sie.setContractAmountTaxLocal(amountTaxLocal);

		}
//		}
		iStkIoBillEntryService.updateBatchById(stkIoBillEntryList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		stkIoBillEntryMapper.deleteByMainId(id);
		stkIoBillMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			stkIoBillEntryMapper.deleteByMainId(id.toString());
			stkIoBillMapper.deleteById(id);
		}
	}

	/**
	 * 设备到厂数统计
	 * @param entry
	 * @return
	 */
	@Override
	public StkIoBillEntry fetchArrivalQty(StkIoBillEntry entry) {
		return baseMapper.fetchArrivalQty(entry);
	}

	/**
	 * 库存
	 * @param page
	 * @param stkIoBill
	 * @return
	 */
	@Override
	public IPage<StkIoBillEntry> queryDetailPageList(Page<StkIoBillEntry> page, StkIoBillEntry stkIoBill) {
		return baseMapper.queryDetailPageList(page,stkIoBill);
	}

	/**
	 * 分页
	 * @param page
	 * @param stkIoBill
	 * @return
	 */
	@Override
	public IPage<StkIoBill> queryPageList(Page<StkIoBill> page, StkIoBill stkIoBill) {
		return baseMapper.queryPageList(page,stkIoBill);
	}

    @Override
    public ContractObjectQty queryOtherDetailsById(StkIoBill sk) {
        return baseMapper.queryOtherDetailsById(sk);
    }

	/**
	 * 导出
	 * @param stkIoBill
	 * @return
	 */
	@Override
	public List<StkIoBill> exportXls(StkIoBill stkIoBill) {
		return baseMapper.exportXls(stkIoBill);
	}

	/**
	 *
	 * @param ids
	 * @return
	 */
	@Override
	public List<ContractObjectQty> queryOtherDetailsByIds(List<String> ids) {
		return baseMapper.queryOtherDetailsByIds(ids);
	}

	/**
	 * 发货审批
	 * @param stkIoBill
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handleSendPass(StkIoBill stkIoBill) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = sysUser.getUsername();
		Date nowTime = new Date();

		StkIoBill sib = this.getById(stkIoBill.getId());
		//判断是否最后一个人审批
		List<ApproveRecord> recordList = iApproveRecordService.list(Wrappers.<ApproveRecord>query().lambda().
				eq(ApproveRecord :: getBusinessId,sib.getSendProcessId()).
				eq(ApproveRecord :: getDelFlag,CommonConstant.DEL_FLAG_0).
				orderByDesc(ApproveRecord :: getCreateTime));

		//更新审批状态
		sib.setUpdateTime(nowTime);
		sib.setUpdateBy(username);
		//只有全部通过才算通过
		if(recordList == null || recordList.size() == 0){

		}else{
			sib.setSendStatus("2");
		}
		this.updateById(sib);

		//生成审批记录
		ApproveRecord ar = new ApproveRecord();
		ar.setId(String.valueOf(IdWorker.getId()));
		ar.setApprover(username);
		ar.setApproveComment(stkIoBill.getApproveComment());
		ar.setDelFlag(CommonConstant.NO_READ_FLAG);
		ar.setCreateUser(username);
		ar.setCreateTime(nowTime);
		ar.setUpdateTime(nowTime);
		ar.setUpdateUser(username);
		ar.setBusinessId(sib.getSendProcessId());
		ar.setType("send");
		ar.setStatus("通过");
		ar.setName(stkIoBill.getApproverId());
		ar.setCode(sib.getId());
		iApproveRecordService.save(ar);

	}

	/**
	 * 发货审批
	 * @param stkIoBill
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handleSendReject(StkIoBill stkIoBill) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = sysUser.getUsername();
		Date nowTime = new Date();

		StkIoBill sib = this.getById(stkIoBill.getId());

		//更新审批状态
		sib.setUpdateTime(nowTime);
		sib.setUpdateBy(username);
		sib.setSendStatus("1");
		this.updateById(sib);

		//生成审批记录
		ApproveRecord ar = new ApproveRecord();
		ar.setId(String.valueOf(IdWorker.getId()));
		ar.setApprover(username);
		ar.setApproveComment(stkIoBill.getApproveComment());
		ar.setDelFlag(CommonConstant.NO_READ_FLAG);
		ar.setCreateUser(username);
		ar.setCreateTime(nowTime);
		ar.setUpdateTime(nowTime);
		ar.setUpdateUser(username);
		ar.setBusinessId(sib.getSendProcessId());
		ar.setType("send");
		ar.setStatus("驳回");
		ar.setName(stkIoBill.getApproverId());
		ar.setCode(sib.getId());
		iApproveRecordService.save(ar);

	}

	/**
	 * 入库明细
	 * @param page
	 * @param stkIoBill
	 * @return
	 */
	@Override
	public IPage<StkIoBillEntry> fetchDetailPageList(Page<StkIoBill> page, StkIoBill stkIoBill) {
		return baseMapper.fetchDetailPageList(page,stkIoBill);
	}

}
