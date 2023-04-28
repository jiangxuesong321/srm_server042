package org.jeecg.modules.srm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.conn.jco.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.modules.message.handle.impl.EmailSendMsgHandle;
import org.jeecg.modules.srm.entity.*;
import org.jeecg.modules.srm.mapper.*;
import org.jeecg.modules.srm.service.*;
import org.jeecg.modules.srm.utils.SAPConnUtil;
import org.jeecg.modules.srm.utils.SapConn;
import org.jeecg.modules.srm.vo.BiddingTemplatePage;
import org.jeecg.modules.system.entity.PurchaseOrderMain;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.mapper.PurchaseOrderMainMapper;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 招标主表
 * @Author: jeecg-boot
 * @Date:   2022-06-18
 * @Version: V1.0
 */
@Service
public class BiddingMainServiceImpl extends ServiceImpl<BiddingMainMapper, BiddingMain> implements IBiddingMainService {

	@Autowired
	private BiddingMainMapper biddingMainMapper;
	@Autowired
	private BiddingRecordMapper biddingRecordMapper;
	@Autowired
	private IBiddingRecordService iBiddingRecordService;
	@Autowired
	private BiddingSupplierMapper biddingSupplierMapper;
	@Autowired
	private IBiddingSupplierService iBiddingSupplierService;
	@Autowired
	private BiddingProfessionalsMapper biddingProfessionalsMapper;
	@Autowired
	private IBiddingProfessionalsService iBiddingProfessionalsService;
	@Autowired
	private IPurchaseRequestMainService iPurchaseRequestMainService;
	@Autowired
	private IPurchaseRequestDetailService iPurchaseRequestDetailService;
	@Autowired
	private IBiddingRecordToProfessionalsService iBiddingRecordToProfessionalsService;
	@Autowired
	private ISysBaseAPI iSysBaseAPI;
	@Autowired
	private ISupplierAccountService iSupplierAccountService;
	@Autowired
	private IBiddingRecordSupplierService iBiddingRecordSupplierService;
	@Autowired
	private IBasSupplierService iBasSupplierService;
	@Autowired
	private ISysUserService iSysUserService;
	@Autowired
	private IProjBaseService iProjBaseService;
	@Autowired
	private ISysDepartService iSysDepartService;
	@Autowired
	private IBiddingQuoteRecordService iBiddingQuoteRecordService;
	@Autowired
	private IBasSupplierContactService iBasSupplierContactService;

	@Autowired
	private PurchaseOrderMainMapper purchaseOrderMainMapper;

	@Autowired
	private BasSupplierMapper basSupplierMapper;

	@Autowired
	private IBasMaterialService basMaterialService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(BiddingMain biddingMain, List<BiddingRecord> recordList,List<BiddingSupplier> suppList,
						 List<BiddingProfessionals> templateList,List<BiddingProfessionals> templateList1) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();
		String reqId = biddingMain.getId();
		String id = String.valueOf(IdWorker.getId());

		JSONObject formData = new JSONObject();
		formData.put("prefix", "ZB");
		String code = (String) FillRuleUtil.executeRule("bidding_code", formData);

		biddingMain.setId(id);
		biddingMain.setRequestId(reqId);
		biddingMain.setBiddingNo(code);
		biddingMain.setDelFlag(CommonConstant.NO_READ_FLAG);
		biddingMain.setUpdateTime(nowTime);
		biddingMain.setUpdateUser(username);
		biddingMain.setCreateTime(nowTime);
		biddingMain.setCreateUser(username);
		biddingMainMapper.insert(biddingMain);

		PurchaseRequestMain main = iPurchaseRequestMainService.getById(reqId);
	//	ProjBase projBase = iProjBaseService.getById(main.getProjectId());
//		SysUser user = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,main.getBuyerId()));
//		SysDepart depart = iSysDepartService.getById(projBase.getSubject());

		List<BiddingSupplier> allList = new ArrayList<>();
		//需求明细ID
		List<String> recordIds = new ArrayList<>();
		//需求明细对应招标数量
		Map<String,BigDecimal> map = new HashMap<>();
		//生成供应商与明细关系表
		List<BiddingRecordSupplier> rsList = new ArrayList<>();
		Set<String> suppIds = new HashSet<>();

		if(recordList!=null && recordList.size()>0) {
			for(BiddingRecord entity:recordList) {
				String recordId = String.valueOf(IdWorker.getId());
				String toRecordId = entity.getId();
				//外键设置
				entity.setId(recordId);
				entity.setDelFlag(CommonConstant.NO_READ_FLAG);
				entity.setUpdateTime(nowTime);
				entity.setUpdateUser(username);
				entity.setCreateTime(nowTime);
				entity.setCreateUser(username);
				entity.setToRecordId(toRecordId);
				entity.setBiddingId(biddingMain.getId());
				recordIds.add(toRecordId);
				map.put(toRecordId,entity.getQty());
			}
			iBiddingRecordService.saveBatch(recordList);

		}
		if(suppList!=null && suppList.size()>0) {
			for(BiddingSupplier entity1 : suppList) {
				BiddingSupplier supp = new BiddingSupplier();
				BeanUtils.copyProperties(entity1,supp);
				//外键设置
				supp.setStatus("0");
				supp.setDelFlag(CommonConstant.NO_READ_FLAG);
				supp.setBiddingId(biddingMain.getId());
				supp.setSupplierId(entity1.getId());
				supp.setId(String.valueOf(IdWorker.getId()));
				allList.add(supp);

				suppIds.add(entity1.getId());
			}
		}
		if(allList != null && allList.size() > 0){
			iBiddingSupplierService.saveBatch(allList);
		}
		for(BiddingRecord br : recordList){
			for(BiddingSupplier bs : allList){
				BiddingRecordSupplier rs = new BiddingRecordSupplier();
				rs.setId(String.valueOf(IdWorker.getId()));
				rs.setBiddingId(br.getBiddingId());
				rs.setRecordId(br.getId());
				rs.setBsId(bs.getId());
				rs.setSupplierId(bs.getSupplierId());
				rsList.add(rs);
			}
		}

		iBiddingRecordSupplierService.saveOrUpdateBatch(rsList);

		//一个专家一条评分记录
		List<BiddingProfessionals> pfsList = new ArrayList<>();
		if(templateList!=null && templateList.size()>0) {
			for(BiddingProfessionals bps:templateList) {

				String[] professionalIds = bps.getProfessionalId().split(",");
				String[] professionalNames = bps.getProfessionalName().split(",");
				int i = 0;
				for(String pid : professionalIds){
					BiddingProfessionals entity = new BiddingProfessionals();
					BeanUtils.copyProperties(bps,entity);

					//外键设置
					entity.setBiddingEvaluateType("0");
					entity.setDelFlag(CommonConstant.NO_READ_FLAG);
					entity.setUpdateTime(nowTime);
					entity.setUpdateUser(username);
					entity.setCreateTime(nowTime);
					entity.setCreateUser(username);
					entity.setBiddingId(biddingMain.getId());
					entity.setId(String.valueOf(IdWorker.getId()));
					entity.setProfessionalId(pid);
					String name = professionalNames[i];
					entity.setProfessionalName(name);
					i++;
					pfsList.add(entity);
				}
			}
//			iBiddingProfessionalsService.saveBatch(templateList);
		}
		if(templateList1!=null && templateList1.size()>0) {
			for(BiddingProfessionals bps:templateList1) {
				String[] professionalIds = bps.getProfessionalId().split(",");
				String[] professionalNames = bps.getProfessionalName().split(",");
				int i = 0;
				for(String pid : professionalIds){
					BiddingProfessionals entity = new BiddingProfessionals();
					BeanUtils.copyProperties(bps,entity);
					//外键设置
					entity.setBiddingEvaluateType("1");
					entity.setDelFlag(CommonConstant.NO_READ_FLAG);
					entity.setUpdateTime(nowTime);
					entity.setUpdateUser(username);
					entity.setCreateTime(nowTime);
					entity.setCreateUser(username);
					entity.setBiddingId(biddingMain.getId());
					entity.setId(String.valueOf(IdWorker.getId()));
					entity.setProfessionalId(pid);
					String name = professionalNames[i];
					entity.setProfessionalName(name);
					i++;
					pfsList.add(entity);
				}
			}
		}
		if(pfsList != null && pfsList.size() > 0){
			iBiddingProfessionalsService.saveBatch(pfsList);
		}

		List<PurchaseRequestDetail> detailList = iPurchaseRequestDetailService.listByIds(recordIds);
		for(PurchaseRequestDetail prd : detailList){
			BigDecimal qty = map.get(prd.getId());
			prd.setPurcQty(prd.getPurcQty().add(qty));
		}
		iPurchaseRequestDetailService.updateBatchById(detailList);

		//更新采购申请单状态
		main.setDealType("1");
		iPurchaseRequestMainService.updateById(main);

		//发送邮件
		List<BasSupplier> supList = iBasSupplierService.listByIds(suppIds);
		List<BasSupplierContact> contactList = iBasSupplierContactService.list(Wrappers.<BasSupplierContact>query().lambda().
				in(BasSupplierContact :: getSupplierId,suppIds).
				eq(BasSupplierContact :: getDelFlag,CommonConstant.DEL_FLAG_0).
				eq(BasSupplierContact :: getIsDefault,CommonConstant.ACT_SYNC_1));
		Map<String,BasSupplierContact> contactMap = contactList.stream().collect(Collectors.toMap(BasSupplierContact::getSupplierId, sp->sp));

		List<SupplierAccount> accountList = iSupplierAccountService.list(Wrappers.<SupplierAccount>query().lambda().in(SupplierAccount :: getSupplierId,suppIds));
		Map<String,SupplierAccount> actMap = accountList.stream().collect(Collectors.toMap(SupplierAccount::getSupplierId, sp->sp));

		//实施地点
		SysDepart depart1 = iSysDepartService.getById(biddingMain.getOpenBiddingAddress());
		if(depart1 == null){
			depart1 = new SysDepart();
		}
//		for(BasSupplier supp : supList){
//			BasSupplierContact contact = contactMap.get(supp.getId());
//			List<String> emails = new ArrayList<>();
//			String context = "["+supp.getName()+"]:" +
//					"<br>&nbsp;&nbsp;&nbsp;&nbsp;你好！" +
//					"<br>&nbsp;&nbsp;&nbsp;&nbsp;我司拟对如下设备进行邀标,具体如下：";
//			for(BiddingRecord entity:recordList) {
//				String text = "<br>&nbsp;&nbsp;&nbsp;&nbsp;项目标的：["+entity.getProdName()+"]；" +
//						"<br>&nbsp;&nbsp;&nbsp;&nbsp;标的数量：["+entity.getQty().stripTrailingZeros().toPlainString()+"]；" +
//						"<br>&nbsp;&nbsp;&nbsp;&nbsp;标的交期：["+sdf.format(entity.getLeadTime())+"]；" +
//						"<br>&nbsp;&nbsp;&nbsp;&nbsp;实施地点：["+depart1.getDepartName()+"];";
//				context = context + text;
//			}
//			context = context + "<br>&nbsp;&nbsp;&nbsp;&nbsp;相关标的需求请联系["+user.getRealname()+"]，电话["+user.getPhone()+"];" +
//					"<br>&nbsp;&nbsp;&nbsp;&nbsp;请贵司务必于开标日["+sdf.format(biddingMain.getBiddingDeadline())+"]前完成规格确认及报价提交，谢谢！" +
//					"<br><span style='margin-left:300px'>["+depart.getDepartName()+"]</span>";
//			if(contact != null && StringUtils.isNotEmpty(contact.getContacterEmail())){
//				emails.add(contact.getContacterEmail());
//				EmailSendMsgHandle emailHandle = new EmailSendMsgHandle();
//				emailHandle.sendTemplateMail("招标通知书",context,emails,null,"0");
//			}else{
//				log.error("招标邮件发送,该供应商没有设置邮箱地址");
//			}
//
//			String accountId = actMap.get(supp.getId()).getId();
//			this.sendNotice(context,accountId,depart.getDepartName());
//		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(BiddingMain biddingMain,List<BiddingRecord> recordList,List<BiddingSupplier> suppList,
						   List<BiddingProfessionals> templateList,List<BiddingProfessionals> templateList1) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();

		biddingMain.setUpdateTime(nowTime);
		biddingMain.setUpdateUser(username);
		this.updateById(biddingMain);
		
		//1.先删除子表数据
		UpdateWrapper<BiddingSupplier> suppUpdate = new UpdateWrapper<>();
		suppUpdate.eq("bidding_id",biddingMain.getId());
		suppUpdate.set("del_flag","1");
		iBiddingSupplierService.update(suppUpdate);

		UpdateWrapper<BiddingProfessionals> prsUpdate = new UpdateWrapper<>();
		prsUpdate.eq("bidding_id",biddingMain.getId());
		prsUpdate.set("del_flag","1");
		iBiddingProfessionalsService.update(prsUpdate);

		iBiddingRecordSupplierService.remove(Wrappers.<BiddingRecordSupplier>query().lambda().eq(BiddingRecordSupplier :: getBiddingId,biddingMain.getId()));

		List<BiddingSupplier> allList = new ArrayList<>();
		//生成供应商与明细关系表
		List<BiddingRecordSupplier> rsList = new ArrayList<>();
		if(recordList!=null && recordList.size()>0) {
			iBiddingRecordService.saveOrUpdateBatch(recordList);
		}

		if(suppList!=null && suppList.size()>0) {
			for(BiddingSupplier entity1 : suppList) {
				BiddingSupplier supp = new BiddingSupplier();
				BeanUtils.copyProperties(entity1,supp);

				//外键设置
				supp.setStatus(entity1.getStatus());
				if("6".equals(entity1.getStatus())){
					supp.setStatus("0");
				}
				supp.setDelFlag(CommonConstant.NO_READ_FLAG);
				supp.setBiddingId(biddingMain.getId());
				supp.setSupplierId(entity1.getId());
				supp.setId(String.valueOf(IdWorker.getId()));
				allList.add(supp);
			}
		}
		if(allList != null && allList.size() > 0){
			iBiddingSupplierService.saveBatch(allList);
		}

		for(BiddingRecord br : recordList){
			for(BiddingSupplier bs : allList){
				BiddingRecordSupplier rs = new BiddingRecordSupplier();
				rs.setId(String.valueOf(IdWorker.getId()));
				rs.setBiddingId(br.getBiddingId());
				rs.setRecordId(br.getId());
				rs.setBsId(bs.getId());
				rs.setSupplierId(bs.getSupplierId());
				rsList.add(rs);
			}
		}
		iBiddingRecordSupplierService.saveOrUpdateBatch(rsList);

		//一个专家一条评分记录
		List<BiddingProfessionals> pfsList = new ArrayList<>();
		if(templateList!=null && templateList.size()>0) {
			for(BiddingProfessionals bps:templateList) {

				String[] professionalIds = bps.getProfessionalId().split(",");
				String[] professionalNames = bps.getProfessionalName().split(",");
				int i = 0;
				for(String pid : professionalIds){
					BiddingProfessionals entity = new BiddingProfessionals();
					BeanUtils.copyProperties(bps,entity);
					//外键设置
					entity.setBiddingEvaluateType("0");
					entity.setDelFlag(CommonConstant.NO_READ_FLAG);
					entity.setUpdateTime(nowTime);
					entity.setUpdateUser(username);
					entity.setCreateTime(nowTime);
					entity.setCreateUser(username);
					entity.setBiddingId(biddingMain.getId());
					entity.setId(String.valueOf(IdWorker.getId()));
					entity.setProfessionalId(pid);
					String name = professionalNames[i];
					entity.setProfessionalName(name);
					i++;
					pfsList.add(entity);
				}
			}
		}
		if(templateList1!=null && templateList1.size()>0) {
			for(BiddingProfessionals bps:templateList1) {
				String[] professionalIds = bps.getProfessionalId().split(",");
				String[] professionalNames = bps.getProfessionalName().split(",");
				int i = 0;
				for(String pid : professionalIds){
					BiddingProfessionals entity = new BiddingProfessionals();
					BeanUtils.copyProperties(bps,entity);
					//外键设置
					entity.setBiddingEvaluateType("1");
					entity.setDelFlag(CommonConstant.NO_READ_FLAG);
					entity.setUpdateTime(nowTime);
					entity.setUpdateUser(username);
					entity.setCreateTime(nowTime);
					entity.setCreateUser(username);
					entity.setBiddingId(biddingMain.getId());
					entity.setId(String.valueOf(IdWorker.getId()));
					entity.setProfessionalId(pid);
					String name = professionalNames[i];
					entity.setProfessionalName(name);
					i++;
					pfsList.add(entity);
				}
			}
		}
		if(pfsList != null && pfsList.size() > 0){
			iBiddingProfessionalsService.saveBatch(pfsList);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		biddingRecordMapper.deleteByMainId(id);
		biddingSupplierMapper.deleteByMainId(id);
		biddingProfessionalsMapper.deleteByMainId(id);
		biddingMainMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			biddingRecordMapper.deleteByMainId(id.toString());
			biddingSupplierMapper.deleteByMainId(id.toString());
			biddingProfessionalsMapper.deleteByMainId(id.toString());
			biddingMainMapper.deleteById(id);
		}
	}

	/**
	 * 招标列表
	 * @param page
	 * @param biddingMain
	 * @return
	 */
	@Override
	public IPage<BiddingMain> queryPageList(Page<BiddingMain> page, BiddingMain biddingMain) {
		return baseMapper.queryPageList(page,biddingMain);
	}

	/**
	 * 招标列表
	 * @param page
	 * @param biddingMain
	 * @return
	 */
	@Override
	public IPage<BiddingMain> pageList(Page<BiddingMain> page, BiddingMain biddingMain) {
		return baseMapper.pageList(page,biddingMain);
	}

	/**
	 * 获取专家信息
	 * @param id
	 * @return
	 */
	@Override
	public List<SysUser> fetchProFessionals(String id) {
		return baseMapper.fetchProFessionals(id);
	}

	/**
	 * 查询评标人员
	 * @param id
	 * @return
	 */
	@Override
	public List<BiddingProfessionals> fetchHasProfessionals(String id) {
		return baseMapper.fetchHasProfessionals(id);
	}

	/**
	 * 退回评标人员
	 * @param biddingProfessionals
	 */
	@Override
	public void toProfessionals(BiddingProfessionals biddingProfessionals) {
		//删除之前的评标记录
		List<BiddingRecordToProfessionals> existList = baseMapper.fetchRecordToProfessionals(biddingProfessionals);
		if(existList != null && existList.size() > 0){
			List<String> ids = new ArrayList<>();
			for(BiddingRecordToProfessionals exist : existList){
				exist.setIsSubmit("0");

				ids.add(exist.getBpsId());
			}
			iBiddingRecordToProfessionalsService.updateBatchById(existList);

			UpdateWrapper<BiddingProfessionals> updateWrapper = new UpdateWrapper<>();
			updateWrapper.set("status","0");
			updateWrapper.in("id",ids);
			iBiddingProfessionalsService.update(updateWrapper);
		}
		//更新招标状态
		BiddingMain bm = this.getById(biddingProfessionals.getBiddingId());
		bm.setBiddingStatus("2");
		this.updateById(bm);
	}

	/**
	 * 招标供应商
	 * @param id
	 * @return
	 */
	@Override
	public List<BasSupplier> fetchSuppList(String id) {
		return baseMapper.fetchSuppList(id);
	}

	/**
	 * 参与评标人员
	 * @param id
	 * @return
	 */
	@Override
	public List<String> fetchProFessionalsEmail(String id) {
		return baseMapper.fetchProFessionalsEmail(id);
	}

	/**
	 * 评标管理
	 * @param page
	 * @param biddingMain
	 * @return
	 */
	@Override
	public IPage<BiddingMain> evaluateList(Page<BiddingMain> page, BiddingMain biddingMain) {
		return baseMapper.evaluateList(page,biddingMain);
	}

	/**
	 * 评标明细
	 * @param biddingMain
	 * @return
	 */
	@Override
	public List<BiddingRecord> fetchRecordList(BiddingMain biddingMain) {
		List<BiddingRecord> pageList = baseMapper.fetchRecordList(biddingMain);
		return pageList;
	}

	/**
	 * 提交评标
	 * @param page
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void submitTemplate(BiddingTemplatePage page) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();

		List<BiddingSupplier> detaiList = page.getDetaiList();
		List<String> ids = page.getIds();
		//查询对应的模板

		String biddingId = null;
		List<BiddingRecordToProfessionals> recordList = new ArrayList<>();
		for(BiddingSupplier bs : detaiList){
			biddingId = bs.getBiddingId();
			int i = 0;
			for(String id : ids){
				BiddingRecordToProfessionals record = new BiddingRecordToProfessionals();
				//
				if("true".equals(bs.getIsRecommend()) || "1".equals(bs.getIsRecommend())){
					record.setIsRecommend("1");
				}else{
					record.setIsRecommend("0");
				}
				record.setId(String.valueOf(IdWorker.getId()));
				record.setBsId(bs.getId());
				record.setBpsId(id);
				List<String> itemScores = bs.getItemScores();
				record.setItemScore(new BigDecimal(itemScores.get(i)));
				List<String> itemTexts = bs.getItemTexts();
				record.setItemText(itemTexts.get(i));
				record.setItemTotal(bs.getItemTotal());
				record.setCreateUser(username);
				record.setCreateTime(nowTime);
				record.setUpdateTime(nowTime);
				record.setUpdateUser(username);
				record.setBiddingId(bs.getBiddingId());
				record.setIsSubmit("1");
				record.setComment(page.getComment());
				recordList.add(record);
				i++;
			}
		}
		//删除评标记录
		iBiddingRecordToProfessionalsService.remove(Wrappers.<BiddingRecordToProfessionals>query().lambda().
				in(BiddingRecordToProfessionals :: getBpsId,ids).
				eq(BiddingRecordToProfessionals :: getCreateUser,page.getProfessionalId()));

		iBiddingRecordToProfessionalsService.saveBatch(recordList);

		//该招标记录是否都已评标过
		BiddingMain bMain = this.getById(biddingId);

		//招标供应商
		List<BiddingSupplier> suppList = iBiddingSupplierService.list(Wrappers.<BiddingSupplier>query().lambda().
				eq(BiddingSupplier :: getDelFlag,CommonConstant.DEL_FLAG_0).
				eq(BiddingSupplier :: getBiddingId,biddingId));
		//评标模板
		List<BiddingProfessionals> psList = iBiddingProfessionalsService.list(Wrappers.<BiddingProfessionals>query().lambda().
				eq(BiddingProfessionals :: getDelFlag,CommonConstant.DEL_FLAG_0).
				eq(BiddingProfessionals :: getBiddingId,biddingId));
		//模板权重
		Map<String,BiddingProfessionals> map = psList.stream().collect(Collectors.toMap(BiddingProfessionals :: getId , pt -> pt));
		//评标记录
		List<BiddingRecordToProfessionals> bpList = iBiddingRecordToProfessionalsService.list(Wrappers.<BiddingRecordToProfessionals>query().lambda().
				eq(BiddingRecordToProfessionals :: getDelFlag,CommonConstant.DEL_FLAG_0).
				eq(BiddingRecordToProfessionals :: getBiddingId,biddingId).
				eq(BiddingRecordToProfessionals :: getIsSubmit,CommonConstant.ACT_SYNC_1));
		//如果 招标供应商数量*评标模板数量 = 评标记录,则所有专家都已评标完
		if(bpList.size() == suppList.size() * psList.size()){
			bMain.setBiddingStatus("8");
		}else{
			bMain.setBiddingStatus("2");
		}
		this.updateById(bMain);
		//更新专家评标状态
		List<BiddingProfessionals> ownerList = new ArrayList<>();
		//技术专家
		Set<String> jsList = new HashSet<>();
		Set<String> swList = new HashSet<>();

		for(BiddingProfessionals bp : psList){
			if("0".equals(bp.getBiddingEvaluateType())){
				jsList.add(bp.getProfessionalId());
			}
			if("1".equals(bp.getBiddingEvaluateType())){
				swList.add(bp.getProfessionalId());
			}

			if(bp.getProfessionalId().equals(page.getProfessionalId())){
				bp.setStatus("1");
				ownerList.add(bp);
			}
		}
		iBiddingProfessionalsService.updateBatchById(ownerList);

		//计算每个供应商的加权分
		for(BiddingSupplier bs : detaiList){
			bs.setIsRecommend(null);
			BigDecimal jsScore = BigDecimal.ZERO;
			BigDecimal swScore = BigDecimal.ZERO;

			for(BiddingRecordToProfessionals bps : bpList){
				String type = null;
				for(BiddingProfessionals bp : psList){
					if(bps.getBpsId().equals(bp.getId())){
						type = bp.getBiddingEvaluateType();
						break;
					}
				}

				if(bps.getBsId().equals(bs.getId()) && "0".equals(type)){
					BigDecimal itemScore = bps.getItemScore() == null ? BigDecimal.ZERO : bps.getItemScore();
					jsScore = jsScore.add(itemScore);
				}
				if(bps.getBsId().equals(bs.getId()) && "1".equals(type)){
					BigDecimal itemScore = bps.getItemScore() == null ? BigDecimal.ZERO : bps.getItemScore();
					swScore = swScore.add(itemScore);
				}
			}
			//除评标人
			BigDecimal jsAvg = jsScore.divide(new BigDecimal(jsList.size()),2,BigDecimal.ROUND_HALF_UP);
			BigDecimal swAvg = swScore.divide(new BigDecimal(swList.size()),2,BigDecimal.ROUND_HALF_UP);
			bs.setItemTotal(jsAvg.add(swAvg));
		}
		iBiddingSupplierService.updateBatchById(detaiList);
	}

	/**
	 * 保存评分
	 * @param page
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void draftTemplate(BiddingTemplatePage page) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();

		List<BiddingSupplier> detaiList = page.getDetaiList();
		List<String> ids = page.getIds();
		List<BiddingRecordToProfessionals> recordList = new ArrayList<>();
		for(BiddingSupplier bs : detaiList){
			int i = 0;
			for(String id : ids){
				BiddingRecordToProfessionals record = new BiddingRecordToProfessionals();
				//
				if("true".equals(bs.getIsRecommend()) || "1".equals(bs.getIsRecommend())){
					record.setIsRecommend("1");
				}else{
					record.setIsRecommend("0");
				}
				record.setId(String.valueOf(IdWorker.getId()));
				record.setBsId(bs.getId());
				record.setBpsId(id);
				List<String> itemScores = bs.getItemScores();
				if(itemScores != null && itemScores.size() > 0){
					record.setItemScore(StringUtils.isEmpty(itemScores.get(i)) ? null : new BigDecimal(itemScores.get(i)));
				}else{
					record.setItemScore(null);
				}
				List<String> itemTexts = bs.getItemTexts();
				if(itemTexts != null && itemTexts.size() > 0){
					record.setItemText(itemTexts.get(i));
				}else{
					record.setItemText(null);
				}
				record.setItemTotal(bs.getItemTotal());
				record.setCreateUser(username);
				record.setCreateTime(nowTime);
				record.setUpdateTime(nowTime);
				record.setUpdateUser(username);
				record.setBiddingId(bs.getBiddingId());
				record.setIsSubmit("0");
				record.setComment(page.getComment());
				recordList.add(record);
				i++;
			}
		}
		//删除评标记录
		iBiddingRecordToProfessionalsService.remove(Wrappers.<BiddingRecordToProfessionals>query().lambda().
				in(BiddingRecordToProfessionals :: getBpsId,ids).
				eq(BiddingRecordToProfessionals :: getCreateUser,username));

		iBiddingRecordToProfessionalsService.saveBatch(recordList);
	}

	/**
	 * 招标列表
	 * @param biddingMain
	 * @return
	 */
	@Override
	public List<BiddingRecord> fetchRecordTwoList(BiddingMain biddingMain) {
		List<BiddingRecord> pageList = baseMapper.fetchRecordTwoList(biddingMain);
		return pageList;
	}

	/**
	 * 定标
	 * @param page
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void fixBidding(BiddingSupplier page) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();

		BiddingMain main = this.getById(page.getBiddingId());
		main.setUpdateTime(nowTime);
		main.setUpdateUser(username);
		main.setBiddingStatus("3");
		this.updateById(main);

		List<BiddingRecordSupplier> suppList = page.getRecommendList();
		List<String> ids = new ArrayList<>();

		//标的 - 供应商
		Set<String> suppIds = new HashSet<>();
		Set<String> recordIds = new HashSet<>();
		//更新报价规格、品牌
		List<BiddingQuoteRecord> quoteList = new ArrayList<>();
		for(BiddingRecordSupplier supp : suppList){
			supp.setIsRecommend("1");
			ids.add(supp.getBsId());

			suppIds.add(supp.getSupplierId());
			recordIds.add(supp.getRecordId());

			BiddingQuoteRecord quote= new BiddingQuoteRecord();
			quote.setId(supp.getQuoteRecordId());
			quote.setSpeType(supp.getSupSpeType());
			quote.setBrandName(supp.getSupBrandName());
			quoteList.add(quote);
		}
		if(quoteList != null && quoteList.size() > 0){
			iBiddingQuoteRecordService.saveOrUpdateBatch(quoteList);
		}
		iBiddingRecordSupplierService.updateBatchById(suppList);

		PurchaseRequestMain pmain = iPurchaseRequestMainService.getById(main.getRequestId());
		ProjBase projBase = iProjBaseService.getById(pmain.getProjectId());
//		SysUser user = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,pmain.getBuyerId()));
//		SysDepart depart = iSysDepartService.getById(projBase.getSubject());
//
//		List<BiddingRecord> recordList = iBiddingRecordService.listByIds(recordIds);
		List<BasSupplier> supList = iBasSupplierService.listByIds(suppIds);
		List<BasSupplierContact> contactList = iBasSupplierContactService.list(Wrappers.<BasSupplierContact>query().lambda().
				in(BasSupplierContact :: getSupplierId,suppIds).
				eq(BasSupplierContact :: getDelFlag,CommonConstant.DEL_FLAG_0).
				eq(BasSupplierContact :: getIsDefault,CommonConstant.ACT_SYNC_1));
		List<SupplierAccount> accountList = iSupplierAccountService.list(Wrappers.<SupplierAccount>query().lambda().in(SupplierAccount :: getSupplierId,suppIds));
		Map<String,SupplierAccount> actMap = accountList.stream().collect(Collectors.toMap(SupplierAccount :: getSupplierId,sp->sp));
		Map<String,BasSupplier> supMap = supList.stream().collect(Collectors.toMap(BasSupplier :: getId,sp->sp));
		Map<String,BasSupplierContact> contactMap = contactList.stream().collect(Collectors.toMap(BasSupplierContact::getSupplierId, sp->sp));

		//实施地点
		SysDepart depart1 = iSysDepartService.getById(main.getOpenBiddingAddress());
		if(depart1 == null){
			depart1 = new SysDepart();
		}

//		for(BiddingRecord br : recordList){
//			for(BiddingRecordSupplier sp : suppList){
//				if(br.getId().equals(sp.getRecordId())){
//					BasSupplier supp = supMap.get(sp.getSupplierId());
//					BasSupplierContact contact = contactMap.get(sp.getSupplierId());
//					List<String> emails = new ArrayList<>();
//					String context = "["+supp.getName()+"]:" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;你好！" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;恭喜贵司中标如下项目：" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;项目标的：["+br.getProdName()+"]；" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;中标数量：["+sp.getBiddingQty().stripTrailingZeros().toPlainString()+"]；" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;标的交期：["+sdf.format(br.getLeadTime())+"]；" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;实施地点：["+depart1.getDepartName()+"];"+
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;相关合同及文件确认请联系["+user.getRealname()+"]，电话["+user.getPhone()+"];" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;请贵司务必于1个月内尽快完成合同文本及最终成交价格的确认，否则将视为自动弃权，谢谢！" +
//							"<br><span style='margin-left:450px'>["+depart.getDepartName()+"]</span>";
//					if(contact != null && StringUtils.isNotEmpty(contact.getContacterEmail())){
//						emails.add(contact.getContacterEmail());
//						EmailSendMsgHandle emailHandle=new EmailSendMsgHandle();
//
//						emailHandle.sendTemplateMail("中标通知书",context,emails,null,"0");
//					}else{
//						log.error("招标中标通知书,该供应商没有设置邮箱地址");
//					}
//					SupplierAccount account = actMap.get(sp.getSupplierId());
//					if(account != null){
//						this.sendNotice(context,account.getId(),depart.getDepartName());
//					}
//				}
//			}
//		}
		//SRM 调用sap 创建po单
		this.sendBillNoToSap(page);
		//更新结束状态
		UpdateWrapper<BiddingSupplier> updateWrapper = new UpdateWrapper<>();
		updateWrapper.set("status","4");
		updateWrapper.eq("bidding_id",page.getBiddingId());
		iBiddingSupplierService.update(updateWrapper);

		//更新中标状态
		updateWrapper = new UpdateWrapper<>();
		updateWrapper.set("is_recommend","1");
		updateWrapper.in("id",ids);
		iBiddingSupplierService.update(updateWrapper);

	}

	public String sendBillNoToSap(BiddingSupplier page) {

		try {
			PurchaseOrderMain purchaseOrderMain = new PurchaseOrderMain();
			//合同编号
			JSONObject formData = new JSONObject();
			formData.put("prefix", "PO");
			String code = (String) FillRuleUtil.executeRule("po_code", formData);
			purchaseOrderMain.setOrderNumber(code);
			purchaseOrderMain.setSupplierId(page.getSupplierId());
			//合同编号
//			purchaseOrderMain.setContactId();

			//招标编码
			BiddingMain biddingMain = baseMapper.selectById(page.getBiddingId());
			purchaseOrderMain.setBiddingNo(biddingMain.getBiddingNo());
			purchaseOrderMainMapper.insert(purchaseOrderMain);
			String JCO_HOST = "192.168.1.20";
			String JCO_SYNSNR = "00";
			String JCO_CLIENT = "200";
			String JCO_USER = "DLW_PDA";
			String JCO_PASSWD = "Delaware.001";
			String JCO_LANG = "ZH";
			String JCO_POOL_CAPACITY = "30";
			String JCO_PEAK_LIMIT = "100";
			String JCO_SAPROUTER = "/H/112.103.135.101/S/3299/W/Dch2017";

			SapConn con = new SapConn(JCO_HOST, JCO_SYNSNR, JCO_CLIENT, JCO_USER, JCO_PASSWD, JCO_LANG, JCO_POOL_CAPACITY, JCO_PEAK_LIMIT, JCO_SAPROUTER);
			JCoDestination jCoDestination = SAPConnUtil.connect(con);

			// 获取调用 RFC 函数对象
			JCoFunction func = jCoDestination.getRepository().getFunction("ZMM_OA_PO_CRT");
			// 配置传入参数抬头信息
			JCoParameterList importParameterList = func.getImportParameterList();
			JCoStructure sc = importParameterList.getStructure("IS_HEAD");
			sc.setValue("ZCONTRACT", purchaseOrderMain.getOrderNumber());

			//行项目
			JCoParameterList tableList =   func.getTableParameterList();
			JCoTable item_table =  tableList.getTable("IT_ITEM");
			for (int i = 0; i < page.getRecommendList().size(); i++) {
				item_table.appendRow();
				LambdaQueryWrapper<BasSupplier> query = new LambdaQueryWrapper<>();
				query.eq(BasSupplier::getId, page.getRecommendList().get(i).getSupplierId());
				BasSupplier baseSupplier = basSupplierMapper.selectOne(query);
//				item_table.setValue("LIFNR", "0000100561");
				item_table.setValue("LIFNR", baseSupplier.getCode());
				item_table.setValue("MENGE", page.getRecommendList().get(i).getBiddingQty());

				//获取物料的code
				LambdaQueryWrapper<BiddingRecord> query1 = new LambdaQueryWrapper<>();
				query1.eq(BiddingRecord::getId, page.getRecommendList().get(i).getRecordId());
				BiddingRecord BiddingRecord =   biddingRecordMapper.selectOne(query1);
//				BasMaterial BasMaterial  = basMaterialService.getById(BiddingRecord.getProdId());
				// 物料号
//				System.out.println("222"+BiddingRecord.getSpeType());
				item_table.setValue("MATNR", BiddingRecord.getProdCode());
				System.out.println("LIFNR"+baseSupplier.getCode());
				System.out.println("MATNR"+ BiddingRecord.getProdCode());
				System.out.println("MENGE"+ page.getRecommendList().get(i).getBiddingQty());
				System.out.println("ZCONTRACT"+ purchaseOrderMain.getOrderNumber());

			}
			// 调用并获取返回值
			func.execute(jCoDestination);
			// 获取 内表 - ET_MARA
			JCoTable maraTable = func.getTableParameterList().getTable("ET_OUTPUT");
			String po_sap = "";
			for (int i = 0; i < maraTable.getNumRows(); i++) {
				po_sap = maraTable.getString("EBELN");
				break;
			}
			if (StringUtils.isNotEmpty(po_sap)){
				purchaseOrderMain.setSapPo(po_sap);
				purchaseOrderMainMapper.updateById(purchaseOrderMain);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 中标供应商信息
	 * @param page
	 * @return
	 */
	@Override
	public BasSupplier getSuppInfo(BiddingMain page) {
		return baseMapper.getSuppInfo(page);
	}

	/**
	 * 报价信息
	 * @param param
	 * @return
	 */
	@Override
	public List<BiddingMain> fetchQuote(BiddingMain param) {
		List<BiddingMain> pageList = baseMapper.fetchQuote(param);
		return pageList;
	}

	/**
	 * 发布公告
	 */
	@Override
	public void sendNotice(String msg,String accountId,String subject) {
		MessageDTO dto = new MessageDTO();
		dto.setFromUser(subject);
		dto.setToUser(accountId);
		dto.setTitle("中标公告");
		dto.setContent(msg);
		dto.setCategory("2");
		iSysBaseAPI.sendSysAnnouncement(dto);
	}


}
