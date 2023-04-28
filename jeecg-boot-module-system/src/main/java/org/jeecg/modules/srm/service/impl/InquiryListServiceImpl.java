package org.jeecg.modules.srm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.modules.message.handle.impl.EmailSendMsgHandle;
import org.jeecg.modules.srm.entity.*;
import org.jeecg.modules.srm.mapper.InquiryRecordMapper;
import org.jeecg.modules.srm.mapper.InquirySupplierMapper;
import org.jeecg.modules.srm.mapper.InquiryListMapper;
import org.jeecg.modules.srm.service.*;
import org.jeecg.modules.srm.vo.InquiryListPage;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 询价单主表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Service
public class InquiryListServiceImpl extends ServiceImpl<InquiryListMapper, InquiryList> implements IInquiryListService {

	@Autowired
	private InquiryListMapper inquiryListMapper;
	@Autowired
	private InquiryRecordMapper inquiryRecordMapper;
	@Autowired
	private InquirySupplierMapper inquirySupplierMapper;
	@Autowired
	private IInquiryRecordService inquiryRecordService;
	@Autowired
	private IInquirySupplierService inquirySupplierService;
	@Autowired
	private IPurchaseRequestMainService iPurchaseRequestMainService;
	@Autowired
	private ISysBaseAPI iSysBaseAPI;
	@Autowired
	private ISupplierAccountService iSupplierAccountService;
	@Autowired
	private IPurchaseRequestDetailService iPurchaseRequestDetailService;
	@Autowired
	private IContractBaseService iContractBaseService;
	@Autowired
	private IBasSupplierService iBasSupplierService;
	@Autowired
	private IBasSupplierContactService iBasSupplierContactService;
	@Autowired
	private ISysUserService iSysUserService;
	@Autowired
	private IProjBaseService iProjBaseService;
	@Autowired
	private ISysDepartService iSysDepartService;
	@Autowired
	private ISupQuoteService iSupQuoteService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(InquiryList inquiryList, List<InquiryRecord> inquiryRecordList) {

		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();
		String reqId = inquiryList.getId();
		String id = String.valueOf(IdWorker.getId());

		JSONObject formData = new JSONObject();
		formData.put("prefix", "XJ");
		String code = (String) FillRuleUtil.executeRule("inquiry_code", formData);

		inquiryList.setId(id);
		inquiryList.setInquiryCode(code);
		inquiryList.setRequestId(reqId);
		inquiryList.setDelFlag(CommonConstant.NO_READ_FLAG);
		inquiryList.setUpdateTime(nowTime);
		inquiryList.setUpdateUser(username);
		inquiryList.setCreateTime(nowTime);
		inquiryList.setCreateUser(username);
		inquiryList.setInquiryDate(nowTime);
		inquiryListMapper.insert(inquiryList);

		//采购申请人
//		PurchaseRequestMain main = iPurchaseRequestMainService.getById(reqId);
//		ProjBase projBase = iProjBaseService.getById(main.getProjectId());
//		SysUser user = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,main.getBuyerId()));
//		SysDepart depart = iSysDepartService.getById(projBase.getSubject());

		//需求明细ID
		List<String> recordIds = new ArrayList<>();
		//需求明细对应招标数量
		Map<String, BigDecimal> map = new HashMap<>();

		List<InquirySupplier> suppList = new ArrayList<>();
		Set<String> suppIds = new HashSet<>();
		if(inquiryRecordList!=null && inquiryRecordList.size()>0) {
			for(InquiryRecord entity:inquiryRecordList) {
				//外键设置
				String toRecordId = entity.getId();
				entity.setDelFlag(CommonConstant.NO_READ_FLAG);
				entity.setUpdateTime(nowTime);
				entity.setUpdateUser(username);
				entity.setCreateTime(nowTime);
				entity.setCreateUser(username);
				entity.setToRecordId(entity.getId());
				entity.setId(String.valueOf(IdWorker.getId()));
				entity.setInquiryId(inquiryList.getId());

				for(String suppId : entity.getSuppIds()){
					InquirySupplier supp = new InquirySupplier();
					supp.setId(String.valueOf(IdWorker.getId()));
					supp.setInquiryId(id);
					supp.setRecordId(entity.getId());
					supp.setStatus(CommonConstant.STATUS_0);
					supp.setDelFlag(CommonConstant.NO_READ_FLAG);
					supp.setSupplierId(suppId);
					suppList.add(supp);

					suppIds.add(suppId);
				}

				recordIds.add(toRecordId);
				map.put(toRecordId,entity.getQty());
			}
			inquiryRecordService.saveBatch(inquiryRecordList);
			inquirySupplierService.saveBatch(suppList);

			//发送邮件
//			List<BasSupplier> supList = iBasSupplierService.listByIds(suppIds);
//			List<BasSupplierContact> contactList = iBasSupplierContactService.list(Wrappers.<BasSupplierContact>query().lambda().
//					in(BasSupplierContact :: getSupplierId,suppIds).
//					eq(BasSupplierContact :: getDelFlag,CommonConstant.DEL_FLAG_0).
//					eq(BasSupplierContact :: getIsDefault,CommonConstant.ACT_SYNC_1));
//			Map<String,BasSupplierContact> contactMap = contactList.stream().collect(Collectors.toMap(BasSupplierContact::getSupplierId, sp->sp));
//			Map<String,BasSupplier> supMap = supList.stream().collect(Collectors.toMap(BasSupplier::getId, sp->sp));
//
//			List<SupplierAccount> accountList = iSupplierAccountService.list(Wrappers.<SupplierAccount>query().lambda().in(SupplierAccount :: getSupplierId,suppIds));
//			Map<String,SupplierAccount> actMap = accountList.stream().collect(Collectors.toMap(SupplierAccount::getSupplierId, sp->sp));
//			for(InquiryRecord entity:inquiryRecordList) {
//				for(String suppId : entity.getSuppIds()){
//					BasSupplier sp = supMap.get(suppId);
//					BasSupplierContact contact = contactMap.get(suppId);
//
//					String context = "["+sp.getName()+"]:" +
//							"<br>    &nbsp;&nbsp;&nbsp;&nbsp;你好！" +
//							"<br>    &nbsp;&nbsp;&nbsp;&nbsp;我司拟对如下设备进行询价,具体如下：" +
//							"<br>    &nbsp;&nbsp;&nbsp;&nbsp;项目标的：["+entity.getProdName()+"];" +
//							"<br>    &nbsp;&nbsp;&nbsp;&nbsp;标的数量：["+entity.getQty().stripTrailingZeros().toPlainString()+"];" +
//							"<br>    &nbsp;&nbsp;&nbsp;&nbsp;标的交期：["+sdf.format(entity.getLeadTime())+"];" +
//							"<br>    &nbsp;&nbsp;&nbsp;&nbsp;相关标的需求请联系["+user.getRealname()+"],电话["+user.getPhone()+"];" +
//							"<br>    &nbsp;&nbsp;&nbsp;&nbsp;请贵司务必于["+sdf.format(inquiryList.getQuotationDeadline())+"]前完成规格确认及报价提交，谢谢！" +
//							"<br><span style='margin-left:300px'>["+depart.getDepartName()+"]</span>";
//
//					if(contact != null && StringUtils.isNotEmpty(contact.getContacterEmail())){
//						List<String> emails = new ArrayList<>();
//						emails.add(contact.getContacterEmail());
//
//						EmailSendMsgHandle emailHandle=new EmailSendMsgHandle();
//
//						emailHandle.sendTemplateMail("询价通知书",context,emails,null,"0");
//					}else{
//						log.error("询价通知书,该供应商没有设置邮箱地址");
//					}
//
//					String accountId = actMap.get(sp.getId()).getId();
//					this.sendNotice(context,accountId,depart.getDepartName());
//				}
//			}

		}

		List<PurchaseRequestDetail> detailList = iPurchaseRequestDetailService.listByIds(recordIds);
		for(PurchaseRequestDetail prd : detailList){
			BigDecimal qty = map.get(prd.getId());
			prd.setPurcQty(prd.getPurcQty().add(qty));
		}
		iPurchaseRequestDetailService.updateBatchById(detailList);

		//更新采购申请单状态
//		main.setDealType("2");
//		iPurchaseRequestMainService.updateById(main);


	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(InquiryList inquiryList,List<InquiryRecord> inquiryRecordList) {
		inquiryListMapper.updateById(inquiryList);
		
		//1.先删除子表数据
		inquiryRecordMapper.deleteByMainId(inquiryList.getId());
		inquirySupplierMapper.deleteByMainId(inquiryList.getId());
		
		//2.子表数据重新插入
		if(inquiryRecordList!=null && inquiryRecordList.size()>0) {
			for(InquiryRecord entity:inquiryRecordList) {
				//外键设置
				entity.setInquiryId(inquiryList.getId());
				inquiryRecordMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		inquiryRecordMapper.deleteByMainId(id);
		inquirySupplierMapper.deleteByMainId(id);
		inquiryListMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			inquiryRecordMapper.deleteByMainId(id.toString());
			inquirySupplierMapper.deleteByMainId(id.toString());
			inquiryListMapper.deleteById(id);
		}
	}

	/**
	 * 询比价列表
	 * @param page
	 * @param inquiryList
	 * @return
	 */
	@Override
	public IPage<InquiryList> queryPageList(Page<InquiryList> page, InquiryList inquiryList) {
		return baseMapper.queryPageList(page,inquiryList);
	}

	/**
	 * 再次发布需求
	 * @param inquiryList
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void handlePush(InquiryList inquiryList) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();

		String id = String.valueOf(IdWorker.getId());
		JSONObject formData = new JSONObject();
		formData.put("prefix", "XJ");
		String code = (String) FillRuleUtil.executeRule("inquiry_code", formData);

		InquiryList existMain = this.getById(inquiryList.getId());
		InquiryList newMain = new InquiryList();
		BeanUtils.copyProperties(existMain,newMain);
		newMain.setInquiryCode(code);
		newMain.setDelFlag(CommonConstant.NO_READ_FLAG);
		newMain.setInquiryStatus(CommonConstant.STATUS_0);
		newMain.setUpdateTime(nowTime);
		newMain.setUpdateUser(username);
		newMain.setCreateTime(nowTime);
		newMain.setCreateUser(username);
		newMain.setInquiryDate(nowTime);
		newMain.setId(id);
		newMain.setRoundNum(CommonConstant.ACT_SYNC_1);
		this.save(newMain);

		List<InquiryRecord> recordList = inquiryRecordService.list(Wrappers.<InquiryRecord>query().lambda().
				eq(InquiryRecord :: getInquiryId,existMain.getId()).
				eq(InquiryRecord :: getDelFlag,CommonConstant.DEL_FLAG_0));

		List<InquiryRecord> newRecordList = new ArrayList<>();
		Map<String,String> toRecord = new HashMap<>();
		for(InquiryRecord ir : recordList){
			InquiryRecord entity = new InquiryRecord();
			BeanUtils.copyProperties(ir,entity);
			entity.setDelFlag(CommonConstant.NO_READ_FLAG);
			entity.setUpdateTime(nowTime);
			entity.setUpdateUser(username);
			entity.setCreateTime(nowTime);
			entity.setCreateUser(username);
			entity.setId(String.valueOf(IdWorker.getId()));
			entity.setInquiryId(newMain.getId());
			entity.setInquiryStatus(CommonConstant.STATUS_0);
			newRecordList.add(entity);

			toRecord.put(ir.getId(),entity.getId());
		}
		inquiryRecordService.saveBatch(newRecordList);

		List<InquirySupplier> suppList = inquirySupplierService.list(Wrappers.<InquirySupplier>query().lambda().
				eq(InquirySupplier :: getInquiryId,existMain.getId()).
				eq(InquirySupplier :: getDelFlag,CommonConstant.DEL_FLAG_0));
		List<InquirySupplier> newSuppList = new ArrayList<>();
		for(InquirySupplier isp : suppList){
			InquirySupplier entity = new InquirySupplier();
			BeanUtils.copyProperties(isp,entity);
			entity.setDelFlag(CommonConstant.NO_READ_FLAG);
			entity.setId(String.valueOf(IdWorker.getId()));
			entity.setInquiryId(newMain.getId());
			entity.setStatus(CommonConstant.STATUS_0);
			String recordId = toRecord.get(isp.getRecordId());
			entity.setRecordId(recordId);
			newSuppList.add(entity);
		}
		inquirySupplierService.saveBatch(newSuppList);
	}

	/**
	 * 提交
	 * @param page
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void toRecommend(InquiryListPage page) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();
		//更新询价结束状态
		InquiryList inquiryList = this.getById(page.getId());
		inquiryList.setInquiryStatus(page.getInquiryStatus());
		inquiryList.setId(page.getId());
		inquiryList.setUpdateTime(nowTime);
		inquiryList.setUpdateUser(username);
		this.updateById(inquiryList);

		//更新中标供应商
		List<InquiryRecord> recordList = page.getRecordList();
		List<InquiryRecord> childList = new ArrayList<>();
		List<InquirySupplier> iSuppList = new ArrayList<>();
		List<SupQuote> quoteList = new ArrayList<>();
		for(InquiryRecord ir : recordList){

			InquiryRecord record = new InquiryRecord();
//			record.setStatus("2");
			record.setId(ir.getId());
			record.setUpdateTime(nowTime);
			record.setUpdateUser(username);
			childList.add(record);

			List<InquirySupplier> suppList = ir.getSuppList();
			for(InquirySupplier isp : suppList){
				InquirySupplier supp = new InquirySupplier();
				supp.setId(isp.getId());
//				supp.setStatus("4");
				supp.setIsRecommend(isp.getIsRecommend());
				supp.setInquiryQty(isp.getInquiryQty());
				iSuppList.add(supp);

				//更新品牌、型号
				if(StringUtils.isNotEmpty(isp.getQuoteId())){
					SupQuote quote = new SupQuote();
					quote.setId(isp.getQuoteId());
					quote.setBrandName(isp.getBrandName());
					quote.setSpeType(isp.getSpeType());
					quoteList.add(quote);
				}
			}
		}

		inquiryRecordService.updateBatchById(childList);
		inquirySupplierService.updateBatchById(iSuppList);
		if(quoteList != null && quoteList.size() > 0){
			iSupQuoteService.saveOrUpdateBatch(quoteList);
		}
	}

	/**
	 * 合同生成列表
	 * @param page
	 * @param inquiryList
	 * @return
	 */
	@Override
	public IPage<InquiryList> queryPageToDetailList(Page<InquiryList> page, InquiryList inquiryList) {
		return baseMapper.queryPageToDetailList(page,inquiryList);
	}

	/**
	 * 结束询价
	 * @param page
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void toRecommand(InquiryListPage page) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();
		//更新询价结束状态
		InquiryList inquiryList = this.getById(page.getId());
		inquiryList.setInquiryStatus(page.getInquiryStatus());
		inquiryList.setId(page.getId());
		inquiryList.setUpdateTime(nowTime);
		inquiryList.setUpdateUser(username);
		this.updateById(inquiryList);

		//更新中标供应商
		List<InquiryRecord> recordList = page.getRecordList();
		List<InquiryRecord> childList = new ArrayList<>();
		List<InquirySupplier> iSuppList = new ArrayList<>();

		//发送邮件
		List<SupQuote> quoteList = new ArrayList<>();
		Set<String> suppIds = new HashSet<>();
		for(InquiryRecord ir : recordList){

			InquiryRecord record = new InquiryRecord();
			record.setStatus("2");
			record.setId(ir.getId());
			record.setUpdateTime(nowTime);
			record.setUpdateUser(username);
			childList.add(record);

			List<InquirySupplier> suppList = ir.getSuppList();
			for(InquirySupplier isp : suppList){
				InquirySupplier supp = new InquirySupplier();
				supp.setId(isp.getId());
				supp.setStatus("4");
				supp.setIsRecommend(isp.getIsRecommend());
				supp.setInquiryQty(isp.getInquiryQty());
				iSuppList.add(supp);

				if("1".equals(supp.getIsRecommend()) || 1 == supp.getIsRecommend()){
					suppIds.add(isp.getSupplierId());
				}

				//更新品牌、型号
				if(StringUtils.isNotEmpty(isp.getQuoteId())){
					SupQuote quote = new SupQuote();
					quote.setId(isp.getQuoteId());
					quote.setBrandName(isp.getBrandName());
					quote.setSpeType(isp.getSpeType());
					quoteList.add(quote);
				}
			}
		}
		inquiryRecordService.updateBatchById(childList);
		inquirySupplierService.updateBatchById(iSuppList);

		if(quoteList != null && quoteList.size() > 0){
			iSupQuoteService.saveOrUpdateBatch(quoteList);
		}

//		PurchaseRequestMain main = iPurchaseRequestMainService.getById(inquiryList.getRequestId());
//		ProjBase projBase = iProjBaseService.getById(main.getProjectId());
//		SysUser user = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,main.getApplyUserId()));
//		SysDepart depart = iSysDepartService.getById(projBase.getSubject());
//		List<BasSupplier> supList = iBasSupplierService.listByIds(suppIds);
//		List<BasSupplierContact> contactList = iBasSupplierContactService.list(Wrappers.<BasSupplierContact>query().lambda().
//				in(BasSupplierContact :: getSupplierId,suppIds).
//				eq(BasSupplierContact :: getDelFlag,CommonConstant.DEL_FLAG_0).
//				eq(BasSupplierContact :: getIsDefault,CommonConstant.ACT_SYNC_1));
//		Map<String,BasSupplierContact> contactMap = contactList.stream().collect(Collectors.toMap(BasSupplierContact::getSupplierId, sp->sp));
//		List<SupplierAccount> accountList = iSupplierAccountService.list(Wrappers.<SupplierAccount>query().lambda().in(SupplierAccount :: getSupplierId,suppIds));
//		Map<String,BasSupplier> supMap = supList.stream().collect(Collectors.toMap(BasSupplier::getId, sp->sp));
//		Map<String,SupplierAccount> actMap = accountList.stream().collect(Collectors.toMap(SupplierAccount::getSupplierId, sp->sp));
//		for(InquiryRecord ir : recordList){
//			List<InquirySupplier> suppList = ir.getSuppList();
//			for(InquirySupplier isp : suppList){
//				if("1".equals(isp.getIsRecommend()) || 1 == isp.getIsRecommend()){
//					BasSupplier supp = supMap.get(isp.getSupplierId());
//					BasSupplierContact contact = contactMap.get(isp.getSupplierId());
//					if (contact != null && StringUtils.isNotEmpty(contact.getContacterEmail())) {
//						List<String> emails = new ArrayList<>();
//						emails.add(contact.getContacterEmail());
//						EmailSendMsgHandle emailHandle=new EmailSendMsgHandle();
//						String context = "["+supp.getName()+"]:" +
//								"<br>&nbsp;&nbsp;&nbsp;&nbsp;你好！" +
//								"<br>&nbsp;&nbsp;&nbsp;&nbsp;恭喜贵司中标如下项目：" +
//								"<br>&nbsp;&nbsp;&nbsp;&nbsp;项目标的：["+ir.getProdName()+"]；" +
//								"<br>&nbsp;&nbsp;&nbsp;&nbsp;中标数量：["+isp.getInquiryQty().stripTrailingZeros().toPlainString()+"]；" +
//								"<br>&nbsp;&nbsp;&nbsp;&nbsp;标的交期：["+sdf.format(ir.getLeadTime())+"]；" +
//								"<br>&nbsp;&nbsp;&nbsp;&nbsp;相关合同及文件确认请联系["+user.getRealname()+"]，电话["+user.getPhone()+"];" +
//								"<br>&nbsp;&nbsp;&nbsp;&nbsp;请贵司务必于1个月内尽快完成合同文本及最终成交价格的确认，否则将视为自动弃权，谢谢！" +
//								"<br><span style='margin-left:450px'>["+depart.getDepartName()+"]</span>";
//						emailHandle.sendTemplateMail("中标通知书",context,emails,null,"0");
//
//						//发布公告
//						String accountId = actMap.get(isp.getSupplierId()).getId();
//						this.sendNotice(context,accountId,depart.getDepartName());
//					}else{
//						log.error("开标通知书,该专家没有设置邮箱地址");
//					}
//
//				}
//			}
//		}

	}

	/**
	 * 发送公告
	 * @param content
	 */
	@Override
	public void sendNotice(String content,String accountId,String subject) {
		MessageDTO dto = new MessageDTO();
		dto.setFromUser(subject);
		dto.setToUser(accountId);
		dto.setTitle("中标公告");
		dto.setContent(content);
		dto.setCategory("2");
		iSysBaseAPI.sendSysAnnouncement(dto);
	}

	/**
	 * 编辑
	 * @param inquiryList
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void editEntity(InquiryList inquiryList) throws Exception {
		//判断是否可以作废(合同是否生成)
		if("4".equals(inquiryList.getInquiryStatus())){
			List<ContractBase> cbList = iContractBaseService.list(Wrappers.<ContractBase>query().lambda().
					eq(ContractBase :: getDelFlag,CommonConstant.DEL_FLAG_0).
					eq(ContractBase :: getRequestId,inquiryList.getId()));
			if(cbList != null && cbList.size() > 0){
				throw new Exception("已生成合同,不能作废");
			}

			//数量回滚
			List<InquiryRecord> recordList = inquiryRecordService.list(Wrappers.<InquiryRecord>query().lambda().
					eq(InquiryRecord :: getDelFlag,CommonConstant.DEL_FLAG_0).
					eq(InquiryRecord :: getInquiryId,inquiryList.getId()));
			List<String> ids = new ArrayList<>();
			Map<String,BigDecimal> map = new HashMap<>();
			for(InquiryRecord ir : recordList){
				ids.add(ir.getToRecordId());

				map.put(ir.getToRecordId(),ir.getQty());
			}
			//需求明细
			List<PurchaseRequestDetail> detailList = iPurchaseRequestDetailService.listByIds(ids);
			for(PurchaseRequestDetail pd : detailList){
				BigDecimal qty = map.get(pd.getId());
				pd.setPurcQty(pd.getPurcQty().subtract(qty));
				pd.setUpdateUser(inquiryList.getUpdateUser());
				pd.setUpdateTime(inquiryList.getUpdateTime());
			}
			iPurchaseRequestDetailService.updateBatchById(detailList);
		}

		this.updateById(inquiryList);
	}

	/**
	 * 询价供应商
	 * @param id
	 * @return
	 */
	@Override
	public List<BasSupplier> fetchSuppList(String id) {
		return baseMapper.fetchSuppList(id);
	}

}
