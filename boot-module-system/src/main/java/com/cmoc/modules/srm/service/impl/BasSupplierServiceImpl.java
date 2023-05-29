package com.cmoc.modules.srm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmoc.common.constant.CommonConstant;
import com.cmoc.common.system.vo.DictModelMany;
import com.cmoc.common.system.vo.LoginUser;
import com.cmoc.common.util.FillRuleUtil;
import com.cmoc.common.util.PasswordUtil;
import com.cmoc.common.util.oConvertUtils;
import com.cmoc.modules.base.service.BaseCommonService;
import com.cmoc.modules.srm.entity.*;
import com.cmoc.modules.srm.mapper.BasSupplierMapper;
import com.cmoc.modules.srm.service.*;
import com.cmoc.modules.srm.vo.BasSupplierPage;
import com.cmoc.modules.system.service.ISysDictService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 供应商基本信息
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Service
public class BasSupplierServiceImpl extends ServiceImpl<BasSupplierMapper, BasSupplier> implements IBasSupplierService {
	@Autowired
	private IBasSupplierContactService iBasSupplierContactService;
	@Autowired
	private IBasSupplierQualificationService iBasSupplierQualificationService;
	@Autowired
	private IBasSupplierBankService iBasSupplierBankService;
	@Autowired
	private ISupplierAccountService supplierAccountService;
	@Autowired
	private BaseCommonService baseCommonService;
	@Autowired
	private ISysDictService iSysDictService;
	@Autowired
	private IBasSupplierPayService iBasSupplierPayService;
	@Autowired
	private IBasSupplierFastService iBasSupplierFastService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(BasSupplier basSupplier, List<BasSupplierContact> basSupplierContactList,
						 List<BasSupplierQualification> basSupplierQualificationList,
						 List<BasSupplierBank> basSupplierBankList, List<BasSupplierFast> basSupplierFastList) {
		JSONObject formData = new JSONObject();
		formData.put("prefix", "S");
		String code = (String) FillRuleUtil.executeRule("supp_code", formData);

		basSupplier.setStatus(CommonConstant.STATUS_0);
		basSupplier.setCode(code);
		this.save(basSupplier);
		if(basSupplierContactList!=null && basSupplierContactList.size()>0) {
			for(BasSupplierContact entity:basSupplierContactList) {
				//外键设置
				entity.setSupplierId(basSupplier.getId());
			}
			iBasSupplierContactService.saveBatch(basSupplierContactList);
		}
		if(basSupplierQualificationList!=null && basSupplierQualificationList.size()>0) {
			for(BasSupplierQualification entity:basSupplierQualificationList) {
				//外键设置
				entity.setSupplierId(basSupplier.getId());
			}
			iBasSupplierQualificationService.saveBatch(basSupplierQualificationList);
		}
		if(basSupplierBankList!=null && basSupplierBankList.size()>0) {
			for(BasSupplierBank entity:basSupplierBankList) {
				//外键设置
				entity.setSupplierId(basSupplier.getId());
			}
			iBasSupplierBankService.saveBatch(basSupplierBankList);
		}
		if(basSupplierFastList!=null && basSupplierFastList.size()>0) {
			for(BasSupplierFast entity:basSupplierFastList) {
				//外键设置
				entity.setSuppId(basSupplier.getId());
			}
			iBasSupplierFastService.saveBatch(basSupplierFastList);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(BasSupplier basSupplier,List<BasSupplierContact> basSupplierContactList,
						   List<BasSupplierQualification> basSupplierQualificationList,
						   List<BasSupplierBank> basSupplierBankList, List<BasSupplierFast> basSupplierFastList) {
		this.updateById(basSupplier);
		
		//1.先删除子表数据
		iBasSupplierContactService.deleteByMainId(basSupplier.getId());
		iBasSupplierQualificationService.deleteByMainId(basSupplier.getId());
		iBasSupplierBankService.deleteByMainId(basSupplier.getId());
		iBasSupplierFastService.remove(Wrappers.<BasSupplierFast>query().lambda().eq(BasSupplierFast :: getSuppId,basSupplier.getId()));
		
		//2.子表数据重新插入
		if(basSupplierContactList!=null && basSupplierContactList.size()>0) {
			for(BasSupplierContact entity:basSupplierContactList) {
				//外键设置
				entity.setSupplierId(basSupplier.getId());
			}
			iBasSupplierContactService.saveBatch(basSupplierContactList);
		}
		if(basSupplierQualificationList!=null && basSupplierQualificationList.size()>0) {
			for(BasSupplierQualification entity:basSupplierQualificationList) {
				//外键设置
				entity.setSupplierId(basSupplier.getId());
			}
			iBasSupplierQualificationService.saveBatch(basSupplierQualificationList);
		}
		if(basSupplierBankList!=null && basSupplierBankList.size()>0) {
			for(BasSupplierBank entity:basSupplierBankList) {
				//外键设置
				entity.setSupplierId(basSupplier.getId());
			}
			iBasSupplierBankService.saveBatch(basSupplierBankList);
		}
		if(basSupplierFastList!=null && basSupplierFastList.size()>0) {
			for(BasSupplierFast entity:basSupplierFastList) {
				//外键设置
				entity.setSuppId(basSupplier.getId());
			}
			iBasSupplierFastService.saveBatch(basSupplierFastList);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		iBasSupplierContactService.deleteByMainId(id);
		iBasSupplierQualificationService.deleteByMainId(id);
		iBasSupplierBankService.deleteByMainId(id);
		this.removeById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			iBasSupplierContactService.deleteByMainId(id.toString());
			iBasSupplierQualificationService.deleteByMainId(id.toString());
			iBasSupplierBankService.deleteByMainId(id.toString());
			this.removeById(id);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean createSrmAccount(BasSupplierPage basSupplierPage) throws Exception {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(sysUser==null){
			return Boolean.FALSE;
		}
		BasSupplier supplier = this.getById(basSupplierPage.getId());
		supplier.setSysAccount(basSupplierPage.getSysAccount());
		supplier.setSysPwd(basSupplierPage.getSysPwd());
		supplier.setUpdateTime(new Date());
		supplier.setUpdateBy(sysUser.getUsername());
		supplier.setStatus("6");

		LambdaQueryWrapper<BasSupplier> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(BasSupplier::getId, basSupplierPage.getId());
		if(!this.update(supplier, queryWrapper)) {
			return Boolean.FALSE;
		} else {
			String username = basSupplierPage.getSysAccount();
			List<SupplierAccount> accountList = supplierAccountService.list(Wrappers.<SupplierAccount>query().lambda().
					eq(SupplierAccount :: getUsername,username));
			if(accountList != null && accountList.size() > 0){
				throw new Exception("账号名已存在");
			}

			SupplierAccount supplierAccount = new SupplierAccount();
			supplierAccount.setSupplierId(basSupplierPage.getId());
			supplierAccount.setUsername(basSupplierPage.getSysAccount());
			supplierAccount.setRealname(basSupplierPage.getName());
			supplierAccount.setDelFlag(CommonConstant.DEL_FLAG_0);
			supplierAccount.setSupplierCode(basSupplierPage.getCode());
			supplierAccount.setStatus(CommonConstant.DEL_FLAG_1);
			supplierAccount.setCreateBy(sysUser.getUsername());
			supplierAccount.setCreateTime(new Date());
			supplierAccount.setSex(CommonConstant.DEL_FLAG_0);
			String salt = oConvertUtils.randomGen(8);
			supplierAccount.setSalt(salt);
			String passwordEncode = PasswordUtil.encrypt(supplierAccount.getUsername(), basSupplierPage.getSysPwd(), salt);
			supplierAccount.setPassword(passwordEncode);
			if(supplierAccountService.save(supplierAccount)) {
				baseCommonService.addLog("添加供应商账号，username： " + supplierAccount.getUsername() ,CommonConstant.LOG_TYPE_2, 2);

				BasSupplierPay pay = new BasSupplierPay();
				pay.setSupplierId(basSupplierPage.getId());
				pay.setPayTime(basSupplierPage.getPayTime());
				pay.setStartTime(basSupplierPage.getStartTime());
				pay.setEndTime(basSupplierPage.getEndTime());
				pay.setIsEnabled(CommonConstant.ACT_SYNC_1);
				pay.setCreateBy(sysUser.getUsername());
				pay.setCreateTime(new Date());
				pay.setUpdateBy(sysUser.getUsername());
				pay.setUpdateTime(new Date());
				iBasSupplierPayService.save(pay);

				List<BasSupplierContact> contactList = iBasSupplierContactService.list(Wrappers.<BasSupplierContact>query().lambda().
						eq(BasSupplierContact :: getSupplierId,basSupplierPage.getId()).
						eq(BasSupplierContact :: getDelFlag,CommonConstant.DEL_FLAG_0).
						eq(BasSupplierContact :: getIsDefault,CommonConstant.ACT_SYNC_1));

				//发送邮件
//				if(contactList != null && contactList.size() > 0){
//					List<String> codeList = new ArrayList<>();
//					codeList.add("supp_url");
//					List<DictModelMany> dictList = iSysDictService.getDictItemsByCodeList(codeList);
//					DictModelMany dict = dictList.get(0);
//					List<String> emails = new ArrayList<>();
//					emails.add(contactList.get(0).getContacterEmail());
//					EmailSendMsgHandle emailHandle=new EmailSendMsgHandle();
//					String context = "[" + supplier.getName() + "]:" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;你好！" +
//							"<br>&nbsp;&nbsp;&nbsp;&nbsp;恭喜贵司已成为中环领先供应商系统的尊贵会员，会员将享受如下服务："+
//							"<br>1、享有参与中环领先招投标的资格；"+
//							"<br>2、享有确认合同及申请付款的服务；"+
//							"<br>3、享有合同进度查询及付款进度查询服务；中环领先将与各方会员携手共进、砥砺前行、共创辉煌，谢谢！；"+
//							"<br>4、账号密码如下:["+basSupplierPage.getSysAccount()+"/"+basSupplierPage.getSysPwd()+"],登录地址如下:["+dict.getText()+"]"+
//							"<br>"+
//							"<br><span style='margin-left:310px'>[中环领先半导体材料有限公司]</span>";
//					emailHandle.sendTemplateMail("开通账号",context,emails,null,"0");
//				}else{
//					log.error("供应商开通账号,该供应商没有设置邮箱地址");
//				}

			} else {
				return Boolean.FALSE;
			}

		}


		return Boolean.TRUE;
	}

	/**
	 * 分页
	 * @param page
	 * @param basSupplier
	 * @return
	 */
	@Override
	public IPage<BasSupplier> pageList(Page<BasSupplier> page, BasSupplier basSupplier) {
		IPage<BasSupplier> iPage = baseMapper.pageList(page,basSupplier);
		List<BasSupplier> pageList = iPage.getRecords();
		List<String> codeList = new ArrayList<>();
		codeList.add("supp_prop");
		codeList.add("supp_type");
		codeList.add("supp_grade");
		List<DictModelMany> dictList = iSysDictService.getDictItemsByCodeList(codeList);
		Map<String,String> map = dictList.stream().collect(Collectors.toMap(DictModelMany::getValue, DictModelMany::getText));
		if(pageList != null && pageList.size() > 0){
			for(BasSupplier bs : pageList){
				if(map != null && !map.isEmpty()){
					String suppProp = map.get(bs.getSupplierProp());
					if(StringUtils.isNotEmpty(suppProp)){
						bs.setSupplierPropDict(suppProp);
					}
					List<String> suppTypes = new ArrayList<>();
					if(StringUtils.isNotEmpty(bs.getSupplierType())){
						String[] types = bs.getSupplierType().split(",");
						for(String str : types){
							String value = map.get(str);
							if(StringUtils.isNotEmpty(value)){
								suppTypes.add(value);
							}
						}
						if(suppTypes != null && suppTypes.size() > 0){
							bs.setSupplierTypeDict(String.join(",",suppTypes));
						}
					}

				}
				if (bs.getSupplierGrade() != null){
					String suppGrade = map.get(bs.getSupplierGrade());
					if(StringUtils.isNotEmpty(suppGrade)){
						bs.setSupplierGradeDict(suppGrade);
					}
				}
			}
		}
		return iPage;
	}

	/**
	 * 重置密码
	 * @param basSupplierPage
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void resetSrmAccount(BasSupplierPage basSupplierPage) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String id = basSupplierPage.getId();
		BasSupplier supplier = this.getById(id);

		supplier.setSysPwd(basSupplierPage.getNewSysPwd());
		supplier.setUpdateTime(new Date());
		supplier.setUpdateBy(sysUser.getUsername());
		this.updateById(supplier);

		SupplierAccount account = supplierAccountService.getOne(Wrappers.<SupplierAccount>query().lambda().
				eq(SupplierAccount :: getSupplierId,id));
		String salt = account.getSalt();
		String passwordEncode = PasswordUtil.encrypt(account.getUsername(), basSupplierPage.getNewSysPwd(), salt);
		account.setPassword(passwordEncode);
		account.setUpdateTime(new Date());
		account.setUpdateBy(sysUser.getUsername());
		supplierAccountService.updateById(account);
	}

	/**
	 * 续费
	 * @param basSupplierPage
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void reNew(BasSupplierPage basSupplierPage) {
		UpdateWrapper<BasSupplierPay> updateWrapper = new UpdateWrapper<>();
		updateWrapper.set("is_enabled",CommonConstant.DEL_FLAG_0);
		updateWrapper.eq("supplier_id",basSupplierPage.getId());
		iBasSupplierPayService.update(updateWrapper);

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		BasSupplierPay pay = new BasSupplierPay();
		pay.setSupplierId(basSupplierPage.getId());
		pay.setPayTime(basSupplierPage.getPayTime());
		pay.setStartTime(basSupplierPage.getStartTime());
		pay.setEndTime(basSupplierPage.getEndTime());
		pay.setIsEnabled(CommonConstant.ACT_SYNC_1);
		pay.setCreateBy(sysUser.getUsername());
		pay.setCreateTime(new Date());
		pay.setUpdateBy(sysUser.getUsername());
		pay.setUpdateTime(new Date());
		iBasSupplierPayService.save(pay);

		BasSupplier supplier = this.getById(basSupplierPage.getId());
		supplier.setStatus("6");
		this.updateById(supplier);

		//账号解冻
		SupplierAccount account = supplierAccountService.getOne(Wrappers.<SupplierAccount>query().lambda().eq(SupplierAccount :: getSupplierId,supplier.getId()));
		account.setStatus(1);
		supplierAccountService.updateById(account);
	}

	/**
	 * 供应商属性统计
	 * @param basSupplier
	 * @return
	 */
	@Override
	public List<Map<String,Object>> fetchSuppCategory(BasSupplier basSupplier) {
		return baseMapper.fetchSuppCategory(basSupplier);
	}

	/**
	 * 供应商分类
	 * @param basSupplier
	 * @return
	 */
	@Override
	public List<Map<String, Object>> fetchSuppType(BasSupplier basSupplier) {
		return baseMapper.fetchSuppType(basSupplier);
	}

	/**
	 * 供应商合同
	 * @param basSupplier
	 * @return
	 */
	@Override
	public Map<String,Object> fetchSuppContract(BasSupplier basSupplier) {
		return baseMapper.fetchSuppContract(basSupplier);
	}

	/**
	 * 供应商合同
	 * @param basSupplier
	 * @return
	 */
	@Override
	public Map<String, Object> fetchSuppContracting(BasSupplier basSupplier) {
		return baseMapper.fetchSuppContracting(basSupplier);
	}

	/**
	 * 供应商状态
	 * @param basSupplier
	 * @return
	 */
	@Override
	public List<Map<String, Object>> fetchSuppStatus(BasSupplier basSupplier) {
		return baseMapper.fetchSuppStatus(basSupplier);
	}

	/**
	 * 活跃供应商
	 * @param basSupplier
	 * @return
	 */
	@Override
	public Map<String, Object> fetchSuppActive(BasSupplier basSupplier) {
		return baseMapper.fetchSuppActive(basSupplier);
	}

}
