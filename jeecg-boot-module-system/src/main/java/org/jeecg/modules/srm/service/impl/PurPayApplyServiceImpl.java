package org.jeecg.modules.srm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.modules.message.handle.impl.EmailSendMsgHandle;
import org.jeecg.modules.srm.entity.*;
import org.jeecg.modules.srm.mapper.PurPayApplyDetailMapper;
import org.jeecg.modules.srm.mapper.PurPayApplyMapper;
import org.jeecg.modules.srm.service.*;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 付款申请
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Service
public class PurPayApplyServiceImpl extends ServiceImpl<PurPayApplyMapper, PurPayApply> implements IPurPayApplyService {

	@Autowired
	private PurPayApplyMapper purPayApplyMapper;
	@Autowired
	private PurPayApplyDetailMapper purPayApplyDetailMapper;
	@Autowired
	private IApproveRecordService iApproveRecordService;
	@Autowired
	private IStkIoBillService iStkIoBillService;
	@Autowired
	private IStkIoBillEntryService iStkIoBillEntryService;
	@Autowired
	private IContractObjectQtyService iContractObjectQtyService;
	@Autowired
	private IBasRateMainService iBasRateMainService;
	@Autowired
	private IContractBaseService iContractBaseService;
	@Autowired
	private IProjectExchangeRateService iProjectExchangeRateService;
	@Autowired
	private IBasSupplierContactService iBasSupplierContactService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PurPayApply purPayApply, List<PurPayApplyDetail> purPayApplyDetailList) {
		purPayApplyMapper.insert(purPayApply);
		if(purPayApplyDetailList!=null && purPayApplyDetailList.size()>0) {
			for(PurPayApplyDetail entity:purPayApplyDetailList) {
				//外键设置
				entity.setApplyId(purPayApply.getId());
				purPayApplyDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PurPayApply purPayApply,List<PurPayApplyDetail> purPayApplyDetailList) {
		purPayApplyMapper.updateById(purPayApply);
		
		//1.先删除子表数据
		purPayApplyDetailMapper.deleteByMainId(purPayApply.getId());
		
		//2.子表数据重新插入
		if(purPayApplyDetailList!=null && purPayApplyDetailList.size()>0) {
			for(PurPayApplyDetail entity:purPayApplyDetailList) {
				//外键设置
				entity.setApplyId(purPayApply.getId());
				purPayApplyDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		purPayApplyDetailMapper.deleteByMainId(id);
		purPayApplyMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			purPayApplyDetailMapper.deleteByMainId(id.toString());
			purPayApplyMapper.deleteById(id);
		}
	}

	/**
	 * 驳回
	 * @param purPayApply
	 */
	@Override
	public void toApprove(PurPayApply purPayApply) throws Exception {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();

		PurPayApply exist = this.getById(purPayApply.getId());
		exist.setSort(exist.getSort() == null ? 1 : exist.getSort() + 1);
		exist.setUpdateTime(nowTime);
		exist.setUpdateUser(username);
		exist.setApplyStatus(purPayApply.getApplyStatus());
		exist.setAttachment(purPayApply.getAttachment());
		exist.setForwarderAttachment(purPayApply.getForwarderAttachment());
		exist.setSuppAttachment(purPayApply.getSuppAttachment());

		exist.setIsContract(purPayApply.getIsContract());
		exist.setIsSend(purPayApply.getIsSend());
		exist.setIsReceive(purPayApply.getIsReceive());
		exist.setIsCheck(purPayApply.getIsCheck());
		exist.setIsProgress(purPayApply.getIsProgress());
		exist.setIsQa(purPayApply.getIsQa());
		exist.setIsSettle(purPayApply.getIsSettle());
		exist.setIsQaDue(purPayApply.getIsQaDue());
		exist.setIsInvoice(purPayApply.getIsInvoice());

		//只有设备类型的才生成入库申请
		ContractBase cb = iContractBaseService.getById(exist.getContractId());

		//审批通过 && 信用证、到货款、发货款 需要生成入库申请
		if("30".equals(exist.getApplyStatus())
				&& ("10".equals(exist.getPayType()) || "20".equals(exist.getPayType()) || "60".equals(exist.getPayType()))
				&& "0".equals(cb.getContractType())){
//			BigDecimal exchangeRate = BigDecimal.ZERO;
//			if("RMB".equals(exist.getCurrency())){
//				exchangeRate = BigDecimal.ONE;
//			}else{
//				//判断项目是否存在汇率,如果不存在则取全局汇率
//				ProjectExchangeRate rate = iProjectExchangeRateService.getOne(Wrappers.<ProjectExchangeRate>query().lambda().
//						eq(ProjectExchangeRate :: getCurrencyB,exist.getCurrency()).
//						eq(ProjectExchangeRate :: getDelFlag,CommonConstant.DEL_FLAG_0).
//						eq(ProjectExchangeRate :: getProjectId,exist.getProjectId()));
//				if(rate != null){
//					exchangeRate = rate.getValueB();
//				}else{
//					String month = DateUtils.toPrevMonth().substring(0,7);
//					List<BasRateMain> mainList = iBasRateMainService.list(Wrappers.<BasRateMain>query().lambda().
//							eq(BasRateMain :: getMonth,month).
//							eq(BasRateMain :: getCurrencyB,exist.getCurrency()));
//					if(mainList == null || mainList.size() == 0){
//						throw new Exception("请检查上月汇率是否存在");
//					}
//					BasRateMain main = mainList.get(0);
//					exchangeRate = main.getValueB();
//				}
//			}
//
//			//申请明细
//			List<String> ids = new ArrayList<>();
//			List<ContractObjectQty> detailList = baseMapper.getDetailList(purPayApply.getId());
//			if(detailList != null && detailList.size() > 0){
//				//生成入库记录
//				JSONObject formData = new JSONObject();
//				formData.put("prefix", "GE");
//				String code = (String) FillRuleUtil.executeRule("IO_CODE", formData);
//
//				StkIoBill stkIoBill = new StkIoBill();
//				stkIoBill.setId(String.valueOf(IdWorker.getId()));
//				stkIoBill.setTaxRate(exist.getTaxRate());
//				stkIoBill.setStockIoType("0");
//				stkIoBill.setBillNo(code);
//				stkIoBill.setBillDate(nowTime);
//				stkIoBill.setCreateTime(nowTime);
//				stkIoBill.setCreateBy(username);
//				stkIoBill.setUpdateBy(username);
//				stkIoBill.setUpdateTime(nowTime);
//				stkIoBill.setDelFlag(CommonConstant.NO_READ_FLAG);
//				stkIoBill.setStatus("0");
//				stkIoBill.setSuppId(exist.getSuppId());
//				stkIoBill.setSuppName(exist.getSuppName());
//				stkIoBill.setContractId(exist.getContractId());
//				stkIoBill.setContractName(exist.getContractName());
//				stkIoBill.setProjectId(exist.getProjectId());
//				stkIoBill.setProjectName(exist.getProjectName());
//				stkIoBill.setContractNumber(detailList.get(0).getContractNumber());
//				stkIoBill.setExchangeRate(exchangeRate);
//				stkIoBill.setApproverId("prod_line");
//				stkIoBill.setActualArrivalDate(nowTime);
//				//计算设备结算金额
//				BigDecimal contractAmount = BigDecimal.ZERO;
//				BigDecimal contractAmountTax = BigDecimal.ZERO;
//				BigDecimal contractAmountLocal = BigDecimal.ZERO;
//				BigDecimal contractAmountTaxLocal = BigDecimal.ZERO;
//
//
//				List<StkIoBillEntry> recordList = new ArrayList<>();
//				for(ContractObjectQty co : detailList){
//					ids.add(co.getId());
//
//					StkIoBillEntry entry = new StkIoBillEntry();
//					entry.setId(String.valueOf(IdWorker.getId()));
//					entry.setMid(stkIoBill.getId());
//					entry.setUnitId(co.getUnitId());
//					entry.setQty(co.getQty());
//					entry.setSupplierId(stkIoBill.getSuppId());
//					entry.setDelFlag(CommonConstant.NO_READ_FLAG);
//					entry.setCreateTime(nowTime);
//					entry.setCreateBy(username);
//					entry.setUpdateBy(username);
//					entry.setUpdateTime(nowTime);
//					entry.setProdCode(co.getProdCode());
//					entry.setProdName(co.getProdName());
//					entry.setOrderId(stkIoBill.getContractId());
//					entry.setSourceEntryId(co.getApplyDetailId());
//					entry.setOrderDetailId(co.getId());
//					entry.setProjectId(stkIoBill.getProjectId());
//					entry.setStockQty(co.getQty());
//					entry.setDeptId(co.getOrgId());
//
//					entry.setContractPrice(co.getContractPrice());
//					entry.setContractPriceTax(co.getContractPriceTax());
//					entry.setContractAmount(co.getContractAmount());
//					entry.setContractAmountTax(co.getContractAmountTax());
//					//计算本币价格
//					BigDecimal priceTaxLocal = co.getContractPriceTax().divide(exchangeRate,4,BigDecimal.ROUND_HALF_UP);
//					entry.setContractPriceTaxLocal(priceTaxLocal);
//					BigDecimal amountTaxLocal = priceTaxLocal.setScale(2,BigDecimal.ROUND_HALF_UP);
//					entry.setContractAmountTaxLocal(amountTaxLocal);
//					BigDecimal taxRate = exist.getTaxRate().divide(new BigDecimal(100)).add(new BigDecimal(1));
//					BigDecimal amountLocal = amountTaxLocal.divide(taxRate,2,BigDecimal.ROUND_HALF_UP);
//					entry.setContractAmountLocal(amountLocal);
//					BigDecimal priceLocal = amountLocal;
//					entry.setContractPriceLocal(priceLocal);
//
//
//					contractAmount = contractAmount.add(co.getContractAmount());
//					contractAmountTax = contractAmountTax.add(co.getContractAmountTax());
//					contractAmountLocal = contractAmountLocal.add(amountLocal);
//					contractAmountTaxLocal = contractAmountTaxLocal.add(amountTaxLocal);
//					recordList.add(entry);
//				}
//				stkIoBill.setContractAmount(contractAmount);
//				stkIoBill.setContractAmountTax(contractAmountTax);
//				stkIoBill.setContractAmountLocal(contractAmountLocal);
//				stkIoBill.setContractAmountTaxLocal(contractAmountTaxLocal);
//
//				stkIoBill.setTotalAmount(contractAmountTaxLocal);
//
//
//				iStkIoBillService.save(stkIoBill);
//				iStkIoBillEntryService.saveBatch(recordList);
//				//更新发货数量
//				UpdateWrapper<ContractObjectQty> updateWrapper = new UpdateWrapper<>();
//				updateWrapper.set("to_send_qty",1);
//				updateWrapper.in("id",ids);
//				iContractObjectQtyService.update(updateWrapper);
//			}
		}
		//驳回发邮件
		else if("20".equals(exist.getApplyStatus())){
			//发送邮件
			List<BasSupplierContact> contactList = iBasSupplierContactService.list(Wrappers.<BasSupplierContact>query().lambda().
					eq(BasSupplierContact :: getSupplierId,cb.getContractSecondPartyId()).
					eq(BasSupplierContact :: getDelFlag,CommonConstant.DEL_FLAG_0).
					eq(BasSupplierContact :: getIsDefault,CommonConstant.ACT_SYNC_1));
//			if(contactList != null && contactList.size() > 0){
//				List<String> emails = new ArrayList<>();
//				emails.add(contactList.get(0).getContacterEmail());
//				EmailSendMsgHandle emailHandle=new EmailSendMsgHandle();
//				String context = "[" + cb.getContractSecondParty() + "]:" +
//						"<br>&nbsp;&nbsp;&nbsp;&nbsp;你好！" +
//						"<br>&nbsp;&nbsp;&nbsp;&nbsp;付款申请单[" + exist.getApplyCode() + "] 存在问题,现已驳回,请重新修改提交" +
//						"<br>"+
//						"<br><span style='margin-left:310px'>["+cb.getContractFirstParty()+"]</span>";
//				emailHandle.sendTemplateMail("付款申请驳回",context,emails,null,"0");
//			}
		}
		//审批原因不为空
		if(StringUtils.isNotEmpty(purPayApply.getApproveComment())){
			ApproveRecord approve = new ApproveRecord();
			approve.setSort(exist.getSort());
			approve.setApprover(purPayApply.getApprover());
			approve.setApproveComment(purPayApply.getApproveComment());
			approve.setCreateTime(nowTime);
			approve.setUpdateTime(nowTime);
			approve.setUpdateUser(username);
			approve.setCreateUser(username);
			approve.setBusinessId(purPayApply.getId());
			approve.setDelFlag(CommonConstant.NO_READ_FLAG);
			approve.setType("zh_apply");
			iApproveRecordService.save(approve);
		}

		this.updateById(exist);
	}

	/**
	 * 付款申请
	 * @param page
	 * @param purPayApply
	 * @return
	 */
	@Override
	public IPage<PurPayApply> queryPageList(Page<PurPayApply> page, PurPayApply purPayApply) {
		return baseMapper.queryPageList(page,purPayApply);
	}

	/**
	 * 币种汇总
	 * @param purPayApply
	 * @return
	 */
	@Override
	public List<PurPayApply> getTotalAmountByCurrency(PurPayApply purPayApply) {
		return baseMapper.getTotalAmountByCurrency(purPayApply);
	}

	/**
	 * 获取支付明细
	 * @param ids
	 * @return
	 */
	@Override
	public List<PurPayApplyDetail> fetchPayDetailList(List<String> ids) {
		return baseMapper.fetchPayDetailList(ids);
	}

	/**
	 * 应付管理导出
	 * @param purPayApply
	 * @return
	 */
	@Override
	public List<PurPayApply> queryList(PurPayApply purPayApply) {
		return baseMapper.queryList(purPayApply);
	}

	/**
	 * 累计请款金额
	 * @param contractId
	 * @return
	 */
	@Override
	public List<PurPayApply> fetchHasPayDetailList(String contractId) {
		return baseMapper.fetchHasPayDetailList(contractId);
	}

}
