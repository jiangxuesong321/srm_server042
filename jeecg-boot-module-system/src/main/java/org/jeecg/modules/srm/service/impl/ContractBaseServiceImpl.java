package org.jeecg.modules.srm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.cas.util.HttpURLConnectionUtil;
import org.jeecg.modules.srm.entity.*;
import org.jeecg.modules.srm.mapper.ContractBaseMapper;
import org.jeecg.modules.srm.service.*;
import org.jeecg.modules.srm.utils.JeecgEntityExcel;
import org.jeecg.modules.srm.utils.JeecgExcelView;
import org.jeecg.modules.srm.vo.*;
import org.jeecg.modules.system.entity.PurchaseOrderMain;
import org.jeecg.modules.system.mapper.PurchaseOrderMainMapper;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecgframework.poi.excel.def.MapExcelConstants;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.jeecg.common.util.CommonUtils.getFileName;


/**
 * @Description: 合同基本信息表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Service
@Slf4j
public class ContractBaseServiceImpl extends ServiceImpl<ContractBaseMapper, ContractBase> implements IContractBaseService {

	@Autowired
	private IContractObjectService iContractObjectService;
	@Autowired
	private IApproveRecordService iApproveRecordService;
	@Autowired
	private IProjectExchangeRateService iProjectExchangeRateService;
	@Autowired
	private IContractTermsService iContractTermsService;
	@Autowired
	private IContractPayStepService iContractPayStepService;
	@Autowired
	private IContractObjectQtyService iContractObjectQtyService;
	@Autowired
	private IBiddingSupplierService iBiddingSupplierService;
	@Autowired
	private IInquirySupplierService inquirySupplierService;
	@Autowired
	private IBasRateMainService iBasRateMainService;
	@Autowired
	private IProjBaseService iProjBaseService;
	@Autowired
	private ISysDictService iSysDictService;
	@Autowired
	private IContractObjectChildService iContractObjectChildService;

	@Autowired
	private PurchaseOrderMainMapper purchaseOrderMainMapper;


	@Value("${jeecg.path.upload}")
	private String upLoadPath;

	@Value("${oa.url}")
	private String endpoint;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");

	/**
	 * 提交
	 * @param contractBase
	 * @param contractObjectList
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(ContractBase contractBase, List<ContractObject> contractObjectList,List<ContractTerms> contractTermsList,List<ContractPayStep> contractPayStepList) throws Exception {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();
		String id = String.valueOf(IdWorker.getId());
		if(StringUtils.isNotEmpty(contractBase.getId())){
			id = contractBase.getId();
		}else{
			JSONObject formData = new JSONObject();
			formData.put("prefix", "HT");
			String code = (String) FillRuleUtil.executeRule("contract_code", formData);
			contractBase.setCreateTime(nowTime);
			contractBase.setCreateUser(username);
			contractBase.setContractNumber(code);
		}
		contractBase.setId(id);
		contractBase.setUpdateTime(nowTime);
		contractBase.setUpdateUser(username);
		contractBase.setDelFlag(CommonConstant.NO_READ_FLAG);
		contractBase.setContractStatus(CommonConstant.STATUS_1);
		contractBase.setSort(contractBase.getSort() == null ?  1 : contractBase.getSort() + 1);
		contractBase.setMainId(null);
//		contractBase.setSource(CommonConstant.STATUS_1);

		//计算未税价格
		BigDecimal taxRate = contractBase.getContractTaxRate();
		if(taxRate.compareTo(new BigDecimal(100)) == 0){
			taxRate = BigDecimal.ZERO;
		}
		taxRate = taxRate.divide(new BigDecimal(100)).add(new BigDecimal(1));
		BigDecimal contractAmountTax = contractBase.getContractAmountTax();

		BigDecimal contractAmount = contractAmountTax.divide(taxRate,2,BigDecimal.ROUND_HALF_UP);
		contractBase.setContractAmount(contractAmount);

		//计算本币价格
		BigDecimal contractAmountLocal = contractBase.getContractAmountTaxLocal().divide(taxRate,2,BigDecimal.ROUND_HALF_UP);
		contractBase.setContractAmountLocal(contractAmountLocal);

		if("RMB".equals(contractBase.getContractCurrency())){
			contractBase.setContractExchangeRate(new BigDecimal(1));
		}else{
			//判断项目是否存在汇率,如果不存在则取全局汇率
			ProjectExchangeRate rate = iProjectExchangeRateService.getOne(Wrappers.<ProjectExchangeRate>query().lambda().
					eq(ProjectExchangeRate :: getCurrencyB,contractBase.getContractCurrency()).
					eq(ProjectExchangeRate :: getDelFlag,CommonConstant.DEL_FLAG_0).
					eq(ProjectExchangeRate :: getProjectId,contractBase.getProjectId()));
			if(rate != null){
				contractBase.setContractExchangeRate(rate.getValueB());
			}else{
				String month = DateUtils.toPrevMonth().substring(0,7);
				List<BasRateMain> mainList = iBasRateMainService.list(Wrappers.<BasRateMain>query().lambda().
						eq(BasRateMain :: getMonth,month).
						eq(BasRateMain :: getCurrencyB,contractBase.getContractCurrency()));
				if(mainList == null || mainList.size() == 0){
					throw new Exception("请检查上月汇率是否存在");
				}
				BasRateMain main = mainList.get(0);
				contractBase.setContractExchangeRate(main.getValueB());
			}
		}

		//计算本币价格
		BigDecimal exchangeRate = contractBase.getContractExchangeRate();
//		BigDecimal contractAmountLocal = contractBase.getContractAmount().divide(exchangeRate,2,BigDecimal.ROUND_HALF_UP);
//		contractBase.setContractAmountLocal(contractAmountLocal);
//
//		BigDecimal contractAmountTaxLocal = contractBase.getContractAmountTax().divide(exchangeRate,2,BigDecimal.ROUND_HALF_UP);
//		contractBase.setContractAmountTaxLocal(contractAmountTaxLocal);

		iContractObjectChildService.remove(Wrappers.<ContractObjectChild>query().lambda().eq(ContractObjectChild :: getContractId,contractBase.getId()));
		//删除明细
		UpdateWrapper<ContractObject> updateWrapper = new UpdateWrapper<>();
		updateWrapper.set("del_flag",CommonConstant.ACT_SYNC_1);
		updateWrapper.eq("contract_id",id);
		iContractObjectService.update(updateWrapper);

		//1.先删除子表数据
		UpdateWrapper<ContractPayStep> updateWrapper2 = new UpdateWrapper<>();
		updateWrapper2.set("del_flag","1");
		updateWrapper2.eq("contract_id",contractBase.getId());
		iContractPayStepService.update(updateWrapper2);

		UpdateWrapper<ContractTerms> updateWrapper1 = new UpdateWrapper<>();
		updateWrapper1.set("del_flag","1");
		updateWrapper1.eq("contract_id",contractBase.getId());
		iContractTermsService.update(updateWrapper1);

//		UpdateWrapper<ContractObjectQty> updateWrapper3 = new UpdateWrapper<>();
//		updateWrapper3.set("del_flag","1");
//		updateWrapper3.eq("contract_id",contractBase.getId());
//		iContractObjectQtyService.update(updateWrapper3);

		if(contractPayStepList!=null && contractPayStepList.size()>0) {
			for(ContractPayStep entity:contractPayStepList) {
				//外键设置
				entity.setId(String.valueOf(IdWorker.getId()));
				entity.setContractId(contractBase.getId());
				entity.setDelFlag(CommonConstant.NO_READ_FLAG);
				entity.setCreateTime(nowTime);
				entity.setCreateUser(username);
				entity.setUpdateUser(username);
				entity.setUpdateTime(nowTime);
			}
			iContractPayStepService.saveBatch(contractPayStepList);
		}
		if(contractTermsList!=null && contractTermsList.size()>0) {
			for(ContractTerms entity:contractTermsList) {
				//外键设置
				entity.setId(String.valueOf(IdWorker.getId()));
				entity.setContractId(contractBase.getId());
				entity.setDelFlag(CommonConstant.NO_READ_FLAG);
				entity.setCreateTime(nowTime);
				entity.setCreateUser(username);
				entity.setUpdateUser(username);
				entity.setUpdateTime(nowTime);
			}
			iContractTermsService.saveBatch(contractTermsList);
		}

		String toRecordId = null;
		List<ContractObjectChild> childList = new ArrayList<>();
		if(contractObjectList!=null && contractObjectList.size()>0) {
			for(ContractObject entity:contractObjectList) {
				//外键设置
				String recordId = entity.getId();
				//新增
				if(StringUtils.isEmpty(entity.getToRecordId())){
					entity.setToRecordId(entity.getId());
					recordId = String.valueOf(IdWorker.getId());
				}
				toRecordId = entity.getToRecordId();
				entity.setId(recordId);
				entity.setContractId(contractBase.getId());
				entity.setDelFlag(CommonConstant.NO_READ_FLAG);
				entity.setCreateTime(nowTime);
				entity.setCreateUser(username);
				entity.setUpdateTime(nowTime);
				entity.setUpdateUser(username);
				entity.setContractTaxRate(contractBase.getContractTaxRate());
				entity.setExchangeRate(exchangeRate);
				entity.setPlanDeliveryDate(entity.getLeadTime());
				entity.setProdSpecType(entity.getSpeType());
				entity.setContractPrice(entity.getPrice());
				entity.setContractPriceTax(entity.getPriceTax());
				entity.setContractAmount(entity.getAmount());
				entity.setContractAmountTax(entity.getAmountTax());

				BigDecimal addTax = BigDecimal.ONE;
				BigDecimal customsTax = BigDecimal.ONE;
				BigDecimal otherAmount = BigDecimal.ZERO;
				if(!"RMB".equals(contractBase.getContractCurrency())){
					addTax = entity.getAddTax();
					customsTax = entity.getCustomsTax();
					otherAmount = entity.getOtherAmount();
				}

				BigDecimal priceTaxLocal = entity.getContractPriceTax().divide(exchangeRate,4,BigDecimal.ROUND_HALF_UP);
				priceTaxLocal = priceTaxLocal.multiply(addTax).multiply(customsTax).add(otherAmount);
				priceTaxLocal = priceTaxLocal.setScale(4,BigDecimal.ROUND_HALF_UP);
				entity.setContractPriceTaxLocal(priceTaxLocal);

				BigDecimal amountTaxLocal = priceTaxLocal.multiply(entity.getQty()).setScale(2,BigDecimal.ROUND_HALF_UP);
				entity.setContractAmountTaxLocal(amountTaxLocal);


				BigDecimal amountLocal = amountTaxLocal.divide(taxRate,2,BigDecimal.ROUND_HALF_UP);
				entity.setContractAmountLocal(amountLocal);

				BigDecimal priceLocal = amountLocal.divide(entity.getQty(),4,BigDecimal.ROUND_HALF_UP);
				entity.setContractPriceLocal(priceLocal);

//				BigDecimal amountTaxLocal = amountLocal.multiply(taxRate).setScale(2,BigDecimal.ROUND_HALF_UP);
//				entity.setContractAmountTaxLocal(amountTaxLocal);

//				BigDecimal priceTaxLocal = amountTaxLocal.divide(entity.getQty(),4,BigDecimal.ROUND_HALF_UP);
//				entity.setContractPriceTaxLocal(priceTaxLocal);

				if(entity.getObjList() != null && entity.getObjList().size() > 0){
					for(ContractObjectChild coc : entity.getObjList()){
						coc.setId(String.valueOf(IdWorker.getId()));
						coc.setMainDetailId(recordId);
						coc.setContractPriceTax(coc.getPriceTax());
						coc.setContractAmountTax(coc.getAmountTax());
						coc.setProdCode(entity.getProdCode());
						coc.setProdId(entity.getProdId());
						BigDecimal cTaxRate = coc.getTaxRate();
						if(cTaxRate.compareTo(new BigDecimal(100)) == 0){
							cTaxRate = BigDecimal.ZERO;
						}
						cTaxRate = cTaxRate.divide(new BigDecimal(100)).add(new BigDecimal(1));
						BigDecimal cAmountTax = coc.getAmountTax();

						//未税金额
						BigDecimal cAmount = cAmountTax.divide(cTaxRate,2,BigDecimal.ROUND_HALF_UP);
						coc.setContractAmount(cAmount);
						BigDecimal cPrice = cAmount.divide(coc.getQty(),4,BigDecimal.ROUND_HALF_UP);
						coc.setContractPrice(cPrice);

						//本币金额
						BigDecimal cPriceLocal = cPrice.divide(exchangeRate,4,BigDecimal.ROUND_HALF_UP);
						//供应商报价总额 + 关税等其他杂费
						cPriceLocal = cPriceLocal.multiply(addTax).multiply(customsTax).add(otherAmount);
						cPriceLocal = cPriceLocal.setScale(4,BigDecimal.ROUND_HALF_UP);
						coc.setContractPriceLocal(cPriceLocal);

						BigDecimal cAmountLocal = cPriceLocal.multiply(coc.getQty()).setScale(2,BigDecimal.ROUND_HALF_UP);
						coc.setContractAmountLocal(cAmountLocal);

						BigDecimal cAmountTaxLocal = cAmountLocal.multiply(cTaxRate).setScale(2,BigDecimal.ROUND_HALF_UP);
						coc.setContractAmountTaxLocal(cAmountTaxLocal);

						BigDecimal cPriceTaxLocal = cAmountTaxLocal.divide(coc.getQty(),4,BigDecimal.ROUND_HALF_UP);
						coc.setContractPriceTaxLocal(cPriceTaxLocal);

						coc.setContractId(entity.getContractId());
						coc.setDelFlag(CommonConstant.NO_READ_FLAG);
						coc.setCreateTime(nowTime);
						coc.setCreateUser(username);
						coc.setUpdateTime(nowTime);
						coc.setUpdateUser(username);
						coc.setPlanDeliveryDate(entity.getPlanDeliveryDate());
						childList.add(coc);
					}
				}
			}
			if(childList != null && childList.size() > 0){
				iContractObjectChildService.saveBatch(childList);
			}
			iContractObjectService.saveOrUpdateBatch(contractObjectList);
//			iContractObjectQtyService.saveBatch(qtyList);


		}

		this.saveOrUpdate(contractBase);

		//将合同编号更新进去
		LambdaQueryWrapper<PurchaseOrderMain> query = new LambdaQueryWrapper<>();
		query.eq(PurchaseOrderMain::getBiddingNo, contractBase.getBiddingNo());
		PurchaseOrderMain purchaseOrderMain = purchaseOrderMainMapper.selectOne(query);
		if (purchaseOrderMain != null && purchaseOrderMain.getBiddingNo() != null) {
			purchaseOrderMain.setContactId(contractBase.getId());
			purchaseOrderMainMapper.updateById(purchaseOrderMain);
		}

		//更新招标状态,已生成合同
		if("1".equals(contractBase.getSource())){
			BiddingSupplier supp = iBiddingSupplierService.getById(contractBase.getBsId());
			supp.setIsContract(CommonConstant.STATUS_1);
			iBiddingSupplierService.updateById(supp);
		}else if("0".equals(contractBase.getSource())){
			InquirySupplier iss = inquirySupplierService.getById(contractBase.getBsId());
			iss.setIsContract(CommonConstant.STATUS_1);
			inquirySupplierService.updateById(iss);

		}
		//审批原因不为空
		if(StringUtils.isNotEmpty(contractBase.getApproveComment())){
			ApproveRecord approve = new ApproveRecord();
			approve.setSort(contractBase.getSort());
			approve.setApprover(contractBase.getApprover());
			approve.setApproveComment(contractBase.getApproveComment());
			approve.setCreateTime(nowTime);
			approve.setUpdateTime(nowTime);
			approve.setUpdateUser(username);
			approve.setCreateUser(username);
			approve.setBusinessId(contractBase.getId());
			approve.setDelFlag(CommonConstant.NO_READ_FLAG);
			iApproveRecordService.save(approve);
		}
	}

	/**
	 * 草稿
	 * @param contractBase
	 * @param contractObjectList
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void draft(ContractBase contractBase, List<ContractObject> contractObjectList,List<ContractTerms> contractTermsList,List<ContractPayStep> contractPayStepList) throws Exception {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();

		String id = String.valueOf(IdWorker.getId());
		if(StringUtils.isNotEmpty(contractBase.getId())){
			id = contractBase.getId();
		}else{
			JSONObject formData = new JSONObject();
			formData.put("prefix", "HT");
			String code = (String) FillRuleUtil.executeRule("contract_code", formData);
			contractBase.setCreateTime(nowTime);
			contractBase.setCreateUser(username);
			contractBase.setContractNumber(code);
		}
		contractBase.setId(id);
		contractBase.setCreateTime(nowTime);
		contractBase.setCreateUser(username);
		contractBase.setUpdateTime(nowTime);
		contractBase.setUpdateUser(username);
		contractBase.setDelFlag(CommonConstant.NO_READ_FLAG);
		contractBase.setContractStatus(CommonConstant.NO_READ_FLAG);
		contractBase.setMainId(null);

		//计算未税价格
		BigDecimal taxRate = contractBase.getContractTaxRate();
		if(taxRate.compareTo(new BigDecimal(100)) == 0){
			taxRate = BigDecimal.ZERO;
		}
		taxRate = taxRate.divide(new BigDecimal(100)).add(new BigDecimal(1));
		BigDecimal contractAmountTax = contractBase.getContractAmountTax();

		BigDecimal contractAmount = contractAmountTax.divide(taxRate,2,BigDecimal.ROUND_HALF_UP);
		contractBase.setContractAmount(contractAmount);

		//计算本币价格
		BigDecimal contractAmountLocal = contractBase.getContractAmountTaxLocal().divide(taxRate,2,BigDecimal.ROUND_HALF_UP);
		contractBase.setContractAmountLocal(contractAmountLocal);

//		contractBase.setSource(CommonConstant.STATUS_1);

//		List<ProjectExchangeRate> rateList = iProjectExchangeRateService.list(Wrappers.<ProjectExchangeRate>query().lambda().
//				eq(ProjectExchangeRate :: getDelFlag,CommonConstant.DEL_FLAG_0).
//				eq(ProjectExchangeRate :: getProjectId,contractBase.getProjectId()).
//				eq(ProjectExchangeRate :: getCurrencyB,contractBase.getContractCurrency()));
//		if(rateList == null || rateList.size() == 0){
//			throw new Exception("当前项目没有维护改币种的汇率");
//		}else{
//			ProjectExchangeRate rate = rateList.get(0);
//			contractBase.setContractExchangeRate(rate.getValueB());
//		}

		//获取前一个月汇率
		if("RMB".equals(contractBase.getContractCurrency())){
			contractBase.setContractExchangeRate(new BigDecimal(1));
		}else{
			//判断项目是否存在汇率,如果不存在则取全局汇率
			ProjectExchangeRate rate = iProjectExchangeRateService.getOne(Wrappers.<ProjectExchangeRate>query().lambda().
					eq(ProjectExchangeRate :: getCurrencyB,contractBase.getContractCurrency()).
					eq(ProjectExchangeRate :: getDelFlag,CommonConstant.DEL_FLAG_0).
					eq(ProjectExchangeRate :: getProjectId,contractBase.getProjectId()));
			if(rate != null){
				contractBase.setContractExchangeRate(rate.getValueB());
			}else{
				String month = DateUtils.toPrevMonth().substring(0,7);
				List<BasRateMain> mainList = iBasRateMainService.list(Wrappers.<BasRateMain>query().lambda().
						eq(BasRateMain :: getMonth,month).
						eq(BasRateMain :: getCurrencyB,contractBase.getContractCurrency()));
				if(mainList == null || mainList.size() == 0){
					throw new Exception("请检查上月汇率是否存在");
				}
				BasRateMain main = mainList.get(0);
				contractBase.setContractExchangeRate(main.getValueB());
			}
		}

		//计算本币价格
		BigDecimal exchangeRate = contractBase.getContractExchangeRate();

		//删除明细
		UpdateWrapper<ContractObject> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("contract_id",id);
		iContractObjectService.remove(updateWrapper);

		//1.先删除子表数据
		UpdateWrapper<ContractPayStep> updateWrapper2 = new UpdateWrapper<>();
		updateWrapper2.set("del_flag","1");
		updateWrapper2.eq("contract_id",contractBase.getId());
		iContractPayStepService.update(updateWrapper2);

		UpdateWrapper<ContractTerms> updateWrapper1 = new UpdateWrapper<>();
		updateWrapper1.set("del_flag","1");
		updateWrapper1.eq("contract_id",contractBase.getId());
		iContractTermsService.update(updateWrapper1);

		if(contractPayStepList!=null && contractPayStepList.size()>0) {
			for(ContractPayStep entity:contractPayStepList) {
				//外键设置
				entity.setId(String.valueOf(IdWorker.getId()));
				entity.setContractId(contractBase.getId());
				entity.setDelFlag(CommonConstant.NO_READ_FLAG);
				entity.setCreateTime(nowTime);
				entity.setCreateUser(username);
				entity.setUpdateUser(username);
				entity.setUpdateTime(nowTime);
			}
			iContractPayStepService.saveBatch(contractPayStepList);
		}
		if(contractTermsList!=null && contractTermsList.size()>0) {
			for(ContractTerms entity:contractTermsList) {
				//外键设置
				entity.setId(String.valueOf(IdWorker.getId()));
				entity.setContractId(contractBase.getId());
				entity.setDelFlag(CommonConstant.NO_READ_FLAG);
				entity.setCreateTime(nowTime);
				entity.setCreateUser(username);
				entity.setUpdateUser(username);
				entity.setUpdateTime(nowTime);
			}
			iContractTermsService.saveBatch(contractTermsList);
		}

		iContractObjectChildService.remove(Wrappers.<ContractObjectChild>query().lambda().eq(ContractObjectChild :: getContractId,contractBase.getId()));

		List<ContractObjectChild> childList = new ArrayList<>();
		if(contractObjectList!=null && contractObjectList.size()>0) {
			for(ContractObject entity:contractObjectList) {

				//外键设置
				String recordId = String.valueOf(IdWorker.getId());
				if(StringUtils.isEmpty(entity.getToRecordId())){
					entity.setToRecordId(entity.getId());
				}

				entity.setId(recordId);
				entity.setContractId(contractBase.getId());
				entity.setDelFlag(CommonConstant.NO_READ_FLAG);
				entity.setCreateTime(nowTime);
				entity.setCreateUser(username);
				entity.setUpdateTime(nowTime);
				entity.setUpdateUser(username);
				entity.setContractTaxRate(contractBase.getContractTaxRate());
				entity.setPlanDeliveryDate(entity.getLeadTime());
				entity.setProdSpecType(entity.getSpeType());
				entity.setContractPrice(entity.getPrice());
				entity.setContractPriceTax(entity.getPriceTax());
				entity.setContractAmount(entity.getAmount());
				entity.setContractAmountTax(entity.getAmountTax());
				entity.setBrandName(entity.getProdBrand());
				entity.setProdSpecType(entity.getSpeType());

				BigDecimal addTax = BigDecimal.ONE;
				BigDecimal customsTax = BigDecimal.ONE;
				BigDecimal otherAmount = BigDecimal.ZERO;
				if(!"RMB".equals(contractBase.getContractCurrency())){
					addTax = entity.getAddTax();
					customsTax = entity.getCustomsTax();
					otherAmount = entity.getOtherAmount();
				}

				BigDecimal priceLocal = entity.getContractPrice().divide(exchangeRate,4,BigDecimal.ROUND_HALF_UP);
				//供应商报价总额 + 关税等其他杂费
				priceLocal = priceLocal.multiply(addTax).multiply(customsTax).add(otherAmount);
				priceLocal = priceLocal.setScale(4,BigDecimal.ROUND_HALF_UP);
				entity.setContractPriceLocal(priceLocal);

				BigDecimal amountLocal = priceLocal.multiply(entity.getQty()).setScale(2,BigDecimal.ROUND_HALF_UP);
				entity.setContractAmountLocal(amountLocal);

				BigDecimal amountTaxLocal = amountLocal.multiply(taxRate).setScale(2,BigDecimal.ROUND_HALF_UP);
				entity.setContractAmountTaxLocal(amountTaxLocal);

				BigDecimal priceTaxLocal = amountTaxLocal.divide(entity.getQty(),4,BigDecimal.ROUND_HALF_UP);
				entity.setContractPriceTaxLocal(priceTaxLocal);


				if(entity.getObjList() != null && entity.getObjList().size() > 0){
					for(ContractObjectChild coc : entity.getObjList()){
						coc.setId(String.valueOf(IdWorker.getId()));
						coc.setMainDetailId(recordId);
						coc.setContractPriceTax(coc.getPriceTax());
						coc.setContractAmountTax(coc.getAmountTax());
						coc.setProdCode(entity.getProdCode());
						coc.setProdId(coc.getProdId());

						BigDecimal cTaxRate = coc.getTaxRate();
						if(cTaxRate.compareTo(new BigDecimal(100)) == 0){
							cTaxRate = BigDecimal.ZERO;
						}
						cTaxRate = cTaxRate.divide(new BigDecimal(100)).add(new BigDecimal(1));
						BigDecimal cAmountTax = coc.getAmountTax();

						//未税金额
						BigDecimal cAmount = cAmountTax.divide(cTaxRate,2,BigDecimal.ROUND_HALF_UP);
						coc.setContractAmount(cAmount);
						BigDecimal cPrice = cAmount.divide(coc.getQty());
						coc.setContractPrice(cPrice);

						//本币金额
						BigDecimal cPriceLocal = cPrice.divide(exchangeRate,4,BigDecimal.ROUND_HALF_UP);
						//供应商报价总额 + 关税等其他杂费
						cPriceLocal = cPriceLocal.multiply(addTax).multiply(customsTax).add(otherAmount);
						cPriceLocal = cPriceLocal.setScale(4,BigDecimal.ROUND_HALF_UP);
						coc.setContractPriceLocal(cPriceLocal);

						BigDecimal cAmountLocal = cPriceLocal.multiply(coc.getQty()).setScale(2,BigDecimal.ROUND_HALF_UP);
						coc.setContractAmountLocal(cAmountLocal);

						BigDecimal cAmountTaxLocal = cAmountLocal.multiply(cTaxRate).setScale(2,BigDecimal.ROUND_HALF_UP);
						coc.setContractAmountTaxLocal(cAmountTaxLocal);

						BigDecimal cPriceTaxLocal = cAmountTaxLocal.divide(coc.getQty(),4,BigDecimal.ROUND_HALF_UP);
						coc.setContractPriceTaxLocal(cPriceTaxLocal);

						coc.setContractId(entity.getContractId());
						coc.setDelFlag(CommonConstant.NO_READ_FLAG);
						coc.setCreateTime(nowTime);
						coc.setCreateUser(username);
						coc.setUpdateTime(nowTime);
						coc.setUpdateUser(username);
						coc.setPlanDeliveryDate(entity.getPlanDeliveryDate());
						childList.add(coc);
					}
				}
			}
			if(childList != null && childList.size() > 0){
				iContractObjectChildService.saveBatch(childList);
			}
			iContractObjectService.saveOrUpdateBatch(contractObjectList);
		}
		this.saveOrUpdate(contractBase);


		//更新招标状态,已生成合同
		if("1".equals(contractBase.getSource())){
			BiddingSupplier supp = iBiddingSupplierService.getById(contractBase.getBsId());
			supp.setIsContract(CommonConstant.STATUS_1);
			iBiddingSupplierService.updateById(supp);
		}else if("0".equals(contractBase.getSource())){
			InquirySupplier iss = inquirySupplierService.getById(contractBase.getBsId());
			iss.setIsContract(CommonConstant.STATUS_1);
			inquirySupplierService.updateById(iss);

		}
	}

	/**
	 * 合同列表
	 * @param page
	 * @param contractBase
	 * @return
	 */
	@Override
	public IPage<ContractBase> queryPageList(Page<ContractBase> page, ContractBase contractBase) {
		return baseMapper.queryPageList(page,contractBase);
	}

	/**
	 * 以项目为单位统计合同总额
	 * @param contractBase
	 * @return
	 */
	@Override
	public ContractBase fetchContractByProjId(ContractBase contractBase) {
		String startMonth = sdf1.format(new Date());
		if("benyue".equals(contractBase.getSource())){
			contractBase.setStartMonth(startMonth);
		}else {

		}
		return baseMapper.fetchContractByProjId(contractBase);
	}

	/**
	 * 合同清单
	 * @param page
	 * @param contractBase
	 * @return
	 */
	@Override
	public IPage<ContractBase> fetchContractListByProjId(Page<ContractBase> page, ContractBase contractBase) {
		return baseMapper.fetchContractListByProjId(page,contractBase);
	}

	/**
	 * 供应商合同信息
	 * @param page
	 * @param contractBase
	 * @return
	 */
	@Override
	public IPage<ContractBase> fetchContractBySupp(Page<ContractBase> page, ContractBase contractBase) {
		return baseMapper.fetchContractBySupp(page,contractBase);
	}

	/**
	 * 项目合同进度报表
	 * @param page
	 * @param contractBase
	 * @return
	 */
	@Override
	public IPage<ContractProgress> fetchContractProgressPageList(Page<ContractProgress> page, ContractProgress contractBase) {
		return baseMapper.fetchContractProgressPageList(page,contractBase);
	}

	/**
	 * 合同发票管理统计表
	 * @param param
	 * @return
	 */
	@Override
	public Map<String, BigDecimal> fetchAmountTotal(Map<String, String> param) {
		String year = param.get("year");
		if(StringUtils.isNotEmpty(year) && year.length() > 4){
			year = year.substring(1,5);
			param.put("year",year);
		}
		//合同金额
		ContractObject contractObject = baseMapper.fetchContractAmount(param);
		BigDecimal contractAmountTax = BigDecimal.ZERO;
		if(contractObject != null){
			contractAmountTax = contractObject.getContractAmountTax();
		}
		//开票金额
		PurchasePayInovice payInovice = baseMapper.fetchPayInvoiceAmount(param);
		BigDecimal invoiceAmountTax = BigDecimal.ZERO;
		if(payInovice != null){
			invoiceAmountTax = payInovice.getInvoiceAmountTax();
		}
		//已收发票
		PurchasePayInovice hasPayInovice = baseMapper.fetchHasPayInvoiceAmount(param);
		BigDecimal hasAmountTax = BigDecimal.ZERO;
		if(hasPayInovice != null){
			hasAmountTax = hasPayInovice.getInvoiceAmountTax();
		}
		//待收发票
		BigDecimal remainAmountTax = invoiceAmountTax.subtract(hasAmountTax);
		//普票
//		PurchasePayInovice normalInovice = baseMapper.fetchPayNormalInvoiceAmount(param);
//		BigDecimal normalAmountTax = BigDecimal.ZERO;
//		if(normalInovice != null){
//			normalAmountTax = normalInovice.getInvoiceAmountTax();
//		}
//		//专票
//		PurchasePayInovice specialInovice = baseMapper.fetchPaySpecialInvoiceAmount(param);
//		BigDecimal specialAmountTax = BigDecimal.ZERO;
//		if(specialInovice != null){
//			specialAmountTax = specialInovice.getInvoiceAmountTax();
//		}
		Map<String,BigDecimal> map = new HashMap<>();
		map.put("contractAmountTax",contractAmountTax);
		map.put("invoiceAmountTax",invoiceAmountTax);
		map.put("hasAmountTax",hasAmountTax);
		map.put("remainAmountTax",remainAmountTax);
//		map.put("normalAmountTax",normalAmountTax);
//		map.put("specialAmountTax",specialAmountTax);
		return map;
	}

	/**
	 * 合同发票明细列表
	 * @param page
	 * @param param
	 * @return
	 */
	@Override
	public IPage<ContractToInvoice> fetchContractToInvoice(Page<ContractToInvoice> page, ContractToInvoice param) {
		String year = param.getYear();
		if(StringUtils.isNotEmpty(year) && year.length() > 4){
			year = year.substring(1,5);
			param.setYear(year);
		}
		return baseMapper.fetchContractToInvoice(page,param);
	}

	/**
	 * 合同发票明细列表按月汇总
	 * @param param
	 * @return
	 */
	@Override
	public List<ContractToInvoice> fetchContractToInvoiceByMonth(ContractToInvoice param) {
		String year = param.getYear();
		if(StringUtils.isNotEmpty(year) && year.length() > 4){
			year = year.substring(1,5);
			param.setYear(year);
		}
		return baseMapper.fetchContractToInvoiceByMonth(param);
	}

	/**
	 * 合同管理及支付明细报表
	 * @param param
	 * @return
	 */
	@Override
	public Map<String, Object> fetchContractAndPay(AmountPlanToYear param) {
		String year = param.getYear();
		if(StringUtils.isNotEmpty(year) && year.length() > 4){
			year = year.substring(1,5);
			param.setYear(year);
		}
		Map<String, String> pam = new HashMap<>();
//		pam.put("year",year);
		pam.put("projId",param.getProjId());
		pam.put("contractNumber",param.getContractNumber());
		pam.put("suppName",param.getSuppName());
		pam.put("startMonth",param.getStartMonth());
		pam.put("endMonth",param.getEndMonth());

		Map<String,Object> rtMap = new HashMap<>();
		//合同金额及数量
		ContractObject contractObject = baseMapper.fetchContractAmount(pam);
		BigDecimal contractAmountTax = BigDecimal.ZERO;
		Integer contractNum = 0;
		if(contractObject != null){
			contractAmountTax = contractObject.getContractAmountTax();
			contractNum = contractObject.getContractNum();
		}
		//开票金额
		PurchasePayInovice payInovice = baseMapper.fetchPayInvoiceAmount(pam);
		BigDecimal invoiceAmountTax = BigDecimal.ZERO;
		BigDecimal invoiceProgress = BigDecimal.ZERO;
		if(payInovice != null){
			invoiceAmountTax = payInovice.getInvoiceAmountTax();
		}

		//合同签订金额

		BigDecimal signAmountTax = BigDecimal.ZERO;
		if(contractObject != null){
			signAmountTax = contractObject.getContractAmountTax();
		}
		if(signAmountTax == null){
			signAmountTax = BigDecimal.ZERO;
		}
		if(signAmountTax != null && signAmountTax.compareTo(BigDecimal.ZERO) == 1){
			invoiceProgress = invoiceAmountTax.divide(signAmountTax,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
		}
		//付款金额
		PurPayPlan pay = baseMapper.fetchPayAmount(pam);
		BigDecimal payAmount = BigDecimal.ZERO;
		BigDecimal payProgress = BigDecimal.ZERO;
		if(pay != null){
			payAmount = pay.getPayAmountCope();
		}
		if(signAmountTax != null && signAmountTax.compareTo(BigDecimal.ZERO) == 1){
			payProgress = payAmount.divide(signAmountTax,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
		}

		//剩余款项
		BigDecimal remainPayAmount = signAmountTax.subtract(payAmount);

		rtMap.put("contractAmountTax",contractAmountTax);
		rtMap.put("contractNum",contractNum);
		rtMap.put("invoiceAmountTax",invoiceAmountTax);
		rtMap.put("invoiceProgress",invoiceProgress);
		rtMap.put("payAmount",payAmount);
		rtMap.put("payProgress",payProgress);
		rtMap.put("remainPayAmount",remainPayAmount);

		return rtMap;
	}

	/**
	 * 合同管理及支付明细报表
	 * @param page
	 * @param contractBase
	 * @return
	 */
	@Override
	public IPage<ContractAndPay> fetchContractAndPayPageList(Page<ContractAndPay> page, ContractAndPay contractBase) {
		String year = contractBase.getYear();
		if(StringUtils.isNotEmpty(year) && year.length() > 4){
			year = year.substring(1,5);
			contractBase.setYear(year);
		}
//		if(StringUtils.isNotEmpty(contractBase.getStartTime())){
//			contractBase.setStartTime(contractBase.getStartTime() + " 00:00:00");
//		}
//		if(StringUtils.isNotEmpty(contractBase.getEndTime())){
//			contractBase.setStartTime(contractBase.getEndTime() + " 23:59:59");
//		}

		IPage iPage = baseMapper.fetchContractAndPayPageList(page,contractBase);
		List<ContractAndPay> pageList = iPage.getRecords();
		//付款明细
		contractBase.setStatus("2");
		List<ContractAndPay> payList = baseMapper.fetchPayAmountList(contractBase);
		//付款中
		contractBase.setStatus("0,1");
		List<ContractAndPay> payingList = baseMapper.fetchPayAmountList(contractBase);

		for(ContractAndPay pg : pageList){
			pg.setPayAmount(BigDecimal.ZERO);
			pg.setPayProgress(BigDecimal.ZERO);
			BigDecimal invoiceAmount = pg.getInvoiceAmount();
			BigDecimal contractAmountTax = pg.getContractAmountTax();
			BigDecimal invoiceProgress = BigDecimal.ZERO;
			if(contractAmountTax != null && contractAmountTax.compareTo(BigDecimal.ZERO) == 1){
				invoiceProgress = invoiceAmount.divide(contractAmountTax,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			}
			pg.setInvoiceProgress(invoiceProgress);

			List<Map<String,Object>> childList = new ArrayList<>();
			BigDecimal payAmount = BigDecimal.ZERO;
			BigDecimal payProgress = BigDecimal.ZERO;

			BigDecimal payingAmount = BigDecimal.ZERO;
			BigDecimal payingProgress = BigDecimal.ZERO;

			int i = 1;
			for(ContractAndPay pay : payList){
				if(pg.getRecordId().equals(pay.getRecordId())){
					payAmount = payAmount.add(pay.getPayAmount());

					Map<String,Object> map = new HashMap<>();
					map.put("amount",pay.getPayAmount());
					String date = "";
					if(StringUtils.isNotEmpty(pay.getProcessCreateTime())){
						date = pay.getProcessCreateTime().substring(0,10);
					}
					map.put("createTime",date);
					childList.add(map);
					if(i == 1){
						pg.setPay1(pay.getPayAmount());
					}else if(i == 2){
						pg.setPay2(pay.getPayAmount());
					}else if(i == 3){
						pg.setPay3(pay.getPayAmount());
					}else if(i == 4){
						pg.setPay4(pay.getPayAmount());
					}else if(i == 5){
						pg.setPay5(pay.getPayAmount());
					}
					i++;

				}
			}

			for(ContractAndPay pay : payingList){
				if(pg.getRecordId().equals(pay.getRecordId())){
					payingAmount = payingAmount.add(pay.getPayAmount());
				}
			}

			pg.setPayAmount(payAmount.setScale(2,BigDecimal.ROUND_HALF_UP));
			BigDecimal remainPayAmount = pg.getContractAmountTax().subtract(payAmount);
			if(remainPayAmount.compareTo(BigDecimal.ZERO) == -1){
				remainPayAmount = BigDecimal.ZERO;
			}
			pg.setRemainPayAmount(remainPayAmount);
			payProgress = BigDecimal.ZERO;
			if(contractAmountTax != null && contractAmountTax.compareTo(BigDecimal.ZERO) == 1){
				payProgress = payAmount.divide(contractAmountTax,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			}
			pg.setPayProgress(payProgress);

			pg.setPayingAmount(payingAmount.setScale(2,BigDecimal.ROUND_HALF_UP));
			payingProgress = BigDecimal.ZERO;
			if(contractAmountTax != null && contractAmountTax.compareTo(BigDecimal.ZERO) == 1){
				payingProgress = payingAmount.divide(contractAmountTax,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			}
			pg.setPayingProgress(payingProgress);


			pg.setPayList(childList);

		}
		return iPage;
	}

	/**
	 * 编辑
	 * @param contractBase
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateContract(ContractBase contractBase) throws Exception {
		//获取OA参数
//		Map<String,String> oaParam = new HashMap<>();
//		List<DictModel> ls = iSysDictService.getDictItems("oa_param");
//		oaParam = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));


//		String suffix = oaParam.get("suffix");

		if("8".equals(contractBase.getContractStatus())){
//			contractBase.setProcessCreateTime(new Date());
//
//			ContractBase old = this.getById(contractBase.getId());
//
//			//发起人工号，配置项
//			List<ApproveSetting> asList = iApproveSettingService.list(Wrappers.<ApproveSetting>query().lambda().
//					eq(ApproveSetting :: getDelFlag,CommonConstant.DEL_FLAG_0).
//					eq(ApproveSetting :: getType,"oaContract").
//					eq(ApproveSetting :: getCompany,old.getContractFirstPartyId()));
//			if(asList == null || asList.size() == 0){
//				throw new Exception("当前主体没有配置OA发起人");
//			}
//			String creatId = asList.get(0).getUsername();
//
//
//			MsgBody msgBody = new MsgBody();
//			//区分合同类型
//			msgBody.setHtmc(old.getContractName());
//			msgBody.setCreatId(creatId);
//			//固定资产采购合同
//			if("0".equals(contractBase.getOaType())){
//				String billid0 = oaParam.get("billid0");
//				String workflowid0 = oaParam.get("workflowid0");
//
//				msgBody.setBillid(billid0);
//				msgBody.setWorkflowid(workflowid0);
//			}
//			//施工合同
//			else if("1".equals(contractBase.getOaType())){
//				String billid1 = oaParam.get("billid1");
//				String workflowid1 = oaParam.get("workflowid1");
//
//				msgBody.setBillid(billid1);
//				msgBody.setWorkflowid(workflowid1);
//			}
//			//服务合同
//			else if("2".equals(contractBase.getOaType())){
//				String billid2 = oaParam.get("billid2");
//				String workflowid2 = oaParam.get("workflowid2");
//
//				msgBody.setBillid(billid2);
//				msgBody.setWorkflowid(workflowid2);
//			}
//			//合同主体(中环领先：59；内蒙领先：58；天津领先：74)
//			if("1".equals(contractBase.getContractFirstPartyId())){
//				String zhlx = oaParam.get("zhlx");
//
//				msgBody.setHt(zhlx);
//				msgBody.setWfdw(zhlx);
//			}else if("3".equals(contractBase.getContractFirstPartyId())){
//				String nmlx = oaParam.get("nmlx");
//
//				msgBody.setHt(nmlx);
//				msgBody.setWfdw(nmlx);
//			}else if("2".equals(contractBase.getContractFirstPartyId())){
//				String tjlx = oaParam.get("tjlx");
//
//				msgBody.setHt(tjlx);
//				msgBody.setWfdw(tjlx);
//			}else if("4".equals(contractBase.getContractFirstPartyId())){
//				String tjlx = oaParam.get("xzlx");
//
//				msgBody.setHt(tjlx);
//				msgBody.setWfdw(tjlx);
//			}
//			msgBody.setHtje(contractBase.getContractAmountTax().divide(new BigDecimal(10000)));
//			msgBody.setHtfs("1");
//			//合同模板 中 的背景描述
//			if(StringUtils.isNotEmpty(old.getTemplateId())){
//				BasContractTemplate template = iBasContractTemplateService.getById(old.getTemplateId());
//				if(template != null){
//					msgBody.setBjms(template.getContractDescription());
//					msgBody.setHtnr(template.getContractContent());
//				}
//			}
//			List<ContractTerms> termsList = iContractTermsService.list(Wrappers.<ContractTerms>query().lambda().
//					eq(ContractTerms :: getContractId,old.getId()).
//					eq(ContractTerms :: getDelFlag,CommonConstant.DEL_FLAG_0));
//			if(termsList != null && termsList.size() > 0){
//				List<String> names = new ArrayList<>();
//				for(ContractTerms ct : termsList){
//					names.add(ct.getTermsContent());
//				}
//				msgBody.setJstk(String.join(",",names));
//			}
//
//			//需求文件地址(自动生成)
//			BiddingMain bm = iBiddingMainService.getById(old.getRequestId());
//			InquiryList il = inquiryListMapper.selectById(old.getRequestId());
//			String requestId = null;
//			String code = null;
//			String reqType = null;
//			String file1 = null;
//			if(bm != null){
//				requestId = bm.getRequestId();
//				code = bm.getBiddingNo();
//				file1 = bm.getOaAttachment();
//				reqType = "bidding";
//			}else if(il != null){
//				requestId = il.getRequestId();
//				code = il.getInquiryCode();
//				file1 = il.getOaAttachment();
//				reqType = "inquiry";
//			}
//
//			PurchaseRequestMain main = iPurchaseRequestMainService.getById(requestId);
//			ProjBase projBase = iProjBaseService.getById(old.getProjectId());
//
//			List<Map<String,String>> mapList = new ArrayList<>();
//			if(main != null && StringUtils.isNotEmpty(main.getOaAttachment())){
//				String file = main.getOaAttachment();
//
//				String[] str = file.split("/");
//				String name = str[str.length - 1];
//
//				Map<String,String> map = new HashMap<>();
//				map.put("name",name);
//				map.put("url",suffix + enCodeUrl(file));
//				mapList.add(map);
//			}else{
//				throw new Exception("没有生成对应得采购需求文件,请联系管理员");
//			}
//
//			if(projBase != null && StringUtils.isNotEmpty(projBase.getOaAttachment())){
//				String eqpUrl = projBase.getOaAttachment();
//
//				String[] str = eqpUrl.split("/");
//				String name = str[str.length - 1];
//
//				Map<String,String> map = new HashMap<>();
//				map.put("name",name);
//				map.put("url",suffix + enCodeUrl(eqpUrl));
//				mapList.add(map);
//			}else{
//				throw new Exception("没有生成对应得项目购置设备清单文件,请联系管理员");
//			}
//
//
//			//判断是否单一供应商
//			InquiryList iList = inquiryListMapper.selectById(old.getRequestId());
//			if(iList != null && "1".equals(iList.getIsOne())){
//				String[] strList = iList.getOtherAttachment().split(",");
//				for(String url : strList){
//					String[] str = url.split("/");
//					String name = str[str.length - 1];
//
//					Map<String,String> map1 = new HashMap<>();
//					map1.put("name",name);
//					map1.put("url",suffix + enCodeUrl(url));
//					mapList.add(map1);
//				}
//			}
//			msgBody.setXqsq(mapList);
//
//
//			//过会文件
//			if(StringUtils.isNotEmpty(projBase.getInAttachment())){
//				List<Map<String,String>> mapList1 = new ArrayList<>();
//				String[] strList = projBase.getInAttachment().split(",");
//				for(String url : strList){
//					String[] str = url.split("/");
//					String name = str[str.length - 1];
//
//					Map<String,String> map1 = new HashMap<>();
//					map1.put("name",name);
//					map1.put("url",suffix + enCodeUrl(url));
//					mapList1.add(map1);
//				}
//				msgBody.setGhwj(mapList1);
//			}
//
//			//其他支撑(营业执照、资质证书)
//			BasSupplier bs = iBasSupplierService.getById(old.getContractSecondPartyId());
//			List<BasSupplierQualification> qualList = iBasSupplierQualificationService.list(Wrappers.<BasSupplierQualification>query().lambda().
//					eq(BasSupplierQualification :: getSupplierId,bs.getId()).
//					eq(BasSupplierQualification :: getDelFlag,CommonConstant.DEL_FLAG_0));
//			List<Map<String,String>> mapList2 = new ArrayList<>();
//			if(StringUtils.isNotEmpty(bs.getSupplierAttachment())){
//				String[] strList = bs.getSupplierAttachment().split(",");
//				for(String url : strList){
//					String[] str = url.split("/");
//					String name = str[str.length - 1];
//					Map<String,String> map2 = new HashMap<>();
//					map2.put("name",name);
//					map2.put("url",suffix + enCodeUrl(url));
//					mapList2.add(map2);
//				}
//			}
//			if(qualList != null && qualList.size() > 0){
//				for(BasSupplierQualification qa : qualList){
//					String[] strList = qa.getQualUrl().split(",");
//					for(String url : strList){
//						String[] str = url.split("/");
//						String name = str[str.length - 1];
//						Map<String,String> map2 = new HashMap<>();
//						map2.put("name",name);
//						map2.put("url",suffix + enCodeUrl(url));
//						mapList2.add(map2);
//					}
//				}
//			}
//			if(mapList2 != null && mapList2.size() > 0){
//				msgBody.setQtzc(mapList2);
//			}
//
//			//招标比价
//			if(StringUtils.isNotEmpty(file1)){
//				List<Map<String,String>> mapList3 = new ArrayList<>();
//
//				//生成对应文件
//				List<ContractObject> objList = iContractObjectService.list(Wrappers.<ContractObject>query().lambda().
//						eq(ContractObject :: getContractId,old.getId()).
//						eq(ContractObject :: getDelFlag,CommonConstant.DEL_FLAG_0));
//				String name = "";
//				//询比价
//				if("0".equals(old.getSource())){
//					name = "[询价明细]";
//				}
//				//招投标
//				else{
//					name = "[招标评分及议价明细]";
//				}
//				String currency = "";
//				if("RMB".equals(old.getContractCurrency())){
//					currency = "元";
//				}else if("EUR".equals(old.getContractCurrency())){
//					currency = "欧元";
//				}else if("USD".equals(old.getContractCurrency())){
//					currency = "美元";
//				}else if("JPY".equals(old.getContractCurrency())){
//					currency = "日元";
//				}
//
//				List<String> obj = new ArrayList<>();
//				for(ContractObject co : objList){
//					String eq = co.getQty().stripTrailingZeros() + "台" + co.getProdName();
//					obj.add(eq);
//				}
//				name = name + "-" + String.join(";",obj) + "-" + old.getContractAmountTax().stripTrailingZeros()+ currency + "-" + old.getContractSecondParty() + ".pdf";
//
//				File source = new File(upLoadPath + File.separator +file1);
//				if(!source.exists()){
//					throw new Exception("没有生成对应的招投标或询价文件");
//				}
//				String newFile = upLoadPath + File.separator + reqType  + File.separator + code + File.separator + name;
//				String url = suffix  + reqType + File.separator + code + File.separator + name;
//				File dest = new File(newFile);
//				//判断服务器是否已存在文件,
//				if(!dest.exists()){
//					Files.copy(source.toPath(), dest.toPath());
//				}
//
//				Map<String,String> map3 = new HashMap<>();
//				map3.put("name",name);
//				map3.put("url",enCodeUrl(url));
//				mapList3.add(map3);
//				msgBody.setBjbj(mapList3);
//			}
//
//			//立项文件
//			if(StringUtils.isNotEmpty(projBase.getOutAttachment())){
//				List<Map<String,String>> mapList4 = new ArrayList<>();
//
//				String[] strList = projBase.getOutAttachment().split(",");
//				for(String url : strList){
//					String[] str = url.split("/");
//					String name = str[str.length - 1];
//
//					Map<String,String> map4 = new HashMap<>();
//					map4.put("name",name);
//					map4.put("url",suffix + enCodeUrl(url));
//					mapList4.add(map4);
//
//				}
//
//				msgBody.setLxwj(mapList4);
//			}
//
//
//			List<ChildBody> child1s = new ArrayList<>();
//			ChildBody child1 = new ChildBody();
//			child1.setHtbh(contractBase.getContractNumber());
//			if(StringUtils.isNotEmpty(old.getWordAttachment())){
//				List<Map<String,String>> mapList5 = new ArrayList<>();
//
//				String[] strList = old.getWordAttachment().split(",");
//				for(String url : strList){
//
//					String[] str = url.split("/");
//					String name = str[str.length - 1];
//
//					Map<String,String> map5 = new HashMap<>();
//					map5.put("name",name);
//					map5.put("url",suffix + enCodeUrl(url));
//					mapList5.add(map5);
//
//				}
//
//				child1.setHt1(mapList5);
//			}
//
//			if(StringUtils.isNotEmpty(old.getOtherAttachment())){
//				List<Map<String,String>> mapList6 = new ArrayList<>();
//				String[] strList = old.getOtherAttachment().split(",");
//				for(String url : strList){
//
//					String[] str = url.split("/");
//					String name = str[str.length - 1];
//
//					Map<String,String> map6 = new HashMap<>();
//					map6.put("name",name);
//					map6.put("url",suffix + enCodeUrl(url));
//					mapList6.add(map6);
//				}
//
//				child1.setFj1(mapList6);
//			}
//			child1.setGzfs("4");
//			child1s.add(child1);
//			msgBody.setChilds1(child1s);
//
//			List<ChildBody2> child2s = new ArrayList<>();
//			ChildBody2 child2 = new ChildBody2();
//			child2.setDfdw(contractBase.getContractSecondParty());
//			child2.setSzd(contractBase.getContractSecondAddress());
//			child2.setDfdwlxfs(contractBase.getContractSecondTelphone());
//			child2s.add(child2);
//			msgBody.setChilds2(child2s);
//
//			String userStr = JSONObject.toJSONString(msgBody);
//			log.info("OA传参数据===========:" + userStr);
//
//			// 创建动态客户端
//			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
//			Client client = dcf.createClient(endpoint);
//
//
//			Object[] objects = new Object[0];
//			try {
//				objects = client.invoke("createxmsrm", userStr);
//				System.out.println("返回数据:" + objects[0].toString());
//				net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(objects[0]);
//				String status = jsonObject.getString("status");
//				String requestid = jsonObject.getString("requestid");
//
//				if("success".equals(status)){
//					contractBase.setContractStatus("8");
//					contractBase.setProcessId(requestid);
//				}else{
//					contractBase.setContractStatus("9");
//				}
//
//			} catch (java.lang.Exception e) {
//				log.error(e.getMessage());
//				throw new Exception("调用OA接口出错");
//			}

		}
		this.updateById(contractBase);
	}


	public static String enCodeUrl(String url) throws UnsupportedEncodingException {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]+");
		Matcher m = p.matcher(url);
		StringBuffer b = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(b, URLEncoder.encode(m.group(0), "utf-8"));
		}
		m.appendTail(b);
		return b.toString();
	}


	/**
	 * 项目合同进度报表导出
	 * @param request
	 * @param contractBase
	 * @param clazz
	 * @param title
	 * @return
	 */
	@Override
	public ModelAndView exportContractProgressPageList(HttpServletRequest request, ContractProgress contractBase, Class<ContractProgress> clazz, String title) {
		List<ContractProgress> exportList = baseMapper.exportContractProgressPageList(contractBase);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		// Step.3 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcel());
		//此处设置的filename无效 ,前端会重更新设置一下
		mv.addObject(NormalExcelConstants.FILE_NAME, title);
		mv.addObject(NormalExcelConstants.CLASS, clazz);
		//update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
		ExportParams exportParams=new ExportParams(title, "导出人:" + sysUser.getRealname(), title);
		exportParams.setImageBasePath(upLoadPath);
		//update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
		mv.addObject(NormalExcelConstants.PARAMS,exportParams);
		mv.addObject(NormalExcelConstants.EXPORT_FIELDS, request.getParameter("field"));
		mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		return mv;
	}

	/**
	 * 合同发票明细列表导出
	 * @param request
	 * @param contractBase
	 * @param clazz
	 * @param title
	 * @return
	 */
	@Override
	public ModelAndView exportContractToInvoice(HttpServletRequest request, ContractToInvoice contractBase, Class<ContractToInvoice> clazz, String title) {
		String year = contractBase.getYear();
		if(StringUtils.isNotEmpty(year) && year.length() > 4){
			year = year.substring(1,5);
			contractBase.setYear(year);
		}

		List<ContractToInvoice> exportList = baseMapper.exportContractToInvoice(contractBase);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		// Step.3 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcel());
		//此处设置的filename无效 ,前端会重更新设置一下
		mv.addObject(NormalExcelConstants.FILE_NAME, title);
		mv.addObject(NormalExcelConstants.CLASS, clazz);
		//update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
		ExportParams exportParams=new ExportParams(title, "导出人:" + sysUser.getRealname(), title);
		exportParams.setImageBasePath(upLoadPath);
		//update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
		mv.addObject(NormalExcelConstants.PARAMS,exportParams);
		mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		mv.addObject(NormalExcelConstants.EXPORT_FIELDS, request.getParameter("field"));
		return mv;
	}

	/**
	 * 合同管理及支付明细报表导出
	 * @param request
	 * @param contractBase
	 * @param clazz
	 * @param title
	 * @return
	 */
	@Override
	public ModelAndView exportContractAndPayPageList(HttpServletRequest request, ContractAndPay contractBase, Class<ContractAndPay> clazz, String title) {
		String year = contractBase.getYear();
		if(StringUtils.isNotEmpty(year) && year.length() > 4){
			year = year.substring(1,5);
			contractBase.setYear(year);
		}

		List<ContractAndPay> exportList = baseMapper.exportContractAndPayPageList(contractBase);
		//付款明细
		contractBase.setStatus("2");
		List<ContractAndPay> payList = baseMapper.fetchPayAmountList(contractBase);

		//付款中
		contractBase.setStatus("0,1");
		List<ContractAndPay> payingList = baseMapper.fetchPayAmountList(contractBase);

		List<Map<String, Object>> expList = new ArrayList<>();
		int maxSize = 0;

		//币种字典
		Map<String,String> model = new HashMap<>();
		try {
			List<DictModel> ls = iSysDictService.getDictItems("model");
			model = ls.stream().collect((Collectors.toMap(DictModel::getValue,DictModel::getText)));
		} catch (Exception ex) {
			log.error("查询数据字典:币种出错" + ex.getMessage());
		}

		for(ContractAndPay pg : exportList){
			Map<String,Object> map = new HashMap<>();

			pg.setPayAmount(BigDecimal.ZERO);
			pg.setPayProgress(BigDecimal.ZERO);
			BigDecimal invoiceAmount = pg.getInvoiceAmount();
			BigDecimal contractAmountTax = pg.getContractAmountTax();
			BigDecimal invoiceProgress = BigDecimal.ZERO;
			if(contractAmountTax != null && contractAmountTax.compareTo(BigDecimal.ZERO) == 1){
				invoiceProgress = invoiceAmount.divide(contractAmountTax,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			}

			pg.setInvoiceProgress(invoiceProgress);

			BigDecimal payAmount = BigDecimal.ZERO;
			BigDecimal payProgress = BigDecimal.ZERO;

			BigDecimal payingAmount = BigDecimal.ZERO;
			BigDecimal payingProgress = BigDecimal.ZERO;

			int i = 0;
			List<String> dateList = new ArrayList<>();
			for(ContractAndPay pay : payList){
				if(pg.getRecordId().equals(pay.getRecordId())){
					payAmount = payAmount.add(pay.getPayAmount());

					map.put("pay" + (i + 1),pay.getPayAmount());
					String date = pay.getProcessCreateTime();
					if(StringUtils.isNotEmpty(date)){
						date = date.substring(0,10);
					}
					dateList.add(date);
					i++;
				}
			}
			if(i > maxSize){
				maxSize = i;
			}

			for(ContractAndPay pay : payingList){
				if(pg.getRecordId().equals(pay.getRecordId())){
					payingAmount = payingAmount.add(pay.getPayAmount());
				}
			}

			pg.setPayAmount(payAmount.setScale(2,BigDecimal.ROUND_HALF_UP));
			BigDecimal remainPayAmount = pg.getContractAmountTax().subtract(payAmount);
			if(remainPayAmount.compareTo(BigDecimal.ZERO) == -1){
				remainPayAmount = BigDecimal.ZERO;
			}
			pg.setRemainPayAmount(remainPayAmount);
			payProgress = BigDecimal.ZERO;
			if(contractAmountTax != null && contractAmountTax.compareTo(BigDecimal.ZERO) == 1){
				payProgress = payAmount.divide(contractAmountTax,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			}
			pg.setPayProgress(payProgress);

			pg.setPayingAmount(payingAmount.setScale(2,BigDecimal.ROUND_HALF_UP));
			payingProgress = BigDecimal.ZERO;
			if(contractAmountTax != null && contractAmountTax.compareTo(BigDecimal.ZERO) == 1){
				payingProgress = payingAmount.divide(contractAmountTax,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			}
			pg.setPayingProgress(payingProgress);



			map.put("projName",pg.getProjName());
			String text = "";
			if(StringUtils.isNotEmpty(pg.getModel())){
				text = model.get(pg.getModel());
			}
			map.put("model",text);
			String signDate = "";
			if(pg.getContractSignDate() != null){
				signDate = sdf.format(pg.getContractSignDate());
			}
			map.put("contractSignDate",signDate);
			map.put("contractNumber",pg.getContractNumber());
			map.put("contractName",pg.getContractName());
			map.put("suppName",pg.getSuppName());
			map.put("contractAmountTax",pg.getContractAmountTax());
			map.put("payAmount",pg.getPayAmount());
			map.put("payProgress",pg.getPayProgress());
			map.put("payingAmount",pg.getPayingAmount());
			map.put("payingProgress",pg.getPayingProgress());
			map.put("invoiceAmount",pg.getInvoiceAmount());
			map.put("invoiceProgress",pg.getInvoiceProgress());
			map.put("remainPayAmount",pg.getRemainPayAmount());


			expList.add(map);

		}


		ModelAndView mv = new ModelAndView(new JeecgExcelView());
		// 导出文件名称
		mv.addObject(MapExcelConstants.FILE_NAME, title);
		// 设置数据
		mv.addObject(MapExcelConstants.MAP_LIST, expList);
		// 设置 ExportParams
		mv.addObject(MapExcelConstants.PARAMS, new ExportParams(title, "sheet1"));

		// 设置表头样式
		List<ExcelExportEntity> filedsList = new ArrayList<>();
		String column = request.getParameter("field");
		if(StringUtils.isNotEmpty(column)){

			if(column.contains("projName")){
				filedsList.add(new ExcelExportEntity("项目名称", "projName",35));
			}
			if(column.contains("model")){
				filedsList.add(new ExcelExportEntity("项目子类", "model",20));
			}
			if(column.contains("contractSignDate")){
				filedsList.add(new ExcelExportEntity("签订日期", "contractSignDate",20));
			}
			if(column.contains("contractNumber")){
				filedsList.add(new ExcelExportEntity("合同编码", "contractNumber",20));
			}
			if(column.contains("contractName")){
				filedsList.add(new ExcelExportEntity("合同名称", "contractName",20));
			}
			if(column.contains("suppName")){
				filedsList.add(new ExcelExportEntity("设备供应商", "suppName",20));
			}
			if(column.contains("contractAmountTax")){
				filedsList.add(new ExcelExportEntity("合同金额", "contractAmountTax",20));
			}
			if(column.contains("payAmount")){
				filedsList.add(new ExcelExportEntity("已付款金额", "payAmount",20));
			}
			if(column.contains("payProgress")){
				filedsList.add(new ExcelExportEntity("付款进度(%)", "payProgress",20));
			}
			if(column.contains("payingAmount")){
				filedsList.add(new ExcelExportEntity("请款金额", "payingAmount",20));
			}
			if(column.contains("payingProgress")){
				filedsList.add(new ExcelExportEntity("请款进度(%)", "payingProgress",20));
			}
			if(column.contains("invoiceAmount")){
				filedsList.add(new ExcelExportEntity("开票金额", "invoiceAmount",20));
			}
			if(column.contains("invoiceProgress")){
				filedsList.add(new ExcelExportEntity("开票进度(%)", "invoiceProgress",20));
			}
			if(maxSize > 0){
				for(int i = 0; i < maxSize; i++){

					filedsList.add(new ExcelExportEntity("付款"+ (i+1), "pay" + (i+1),20));
				}
			}
			if(column.contains("remainPayAmount")){
				filedsList.add(new ExcelExportEntity("剩余款项", "remainPayAmount",20));
			}

		}
//		else{
//			filedsList.add(new ExcelExportEntity("项目名称", "projName",35));
//			filedsList.add(new ExcelExportEntity("项目子类", "model",20));
//			filedsList.add(new ExcelExportEntity("签订日期", "contractSignDate",20));
//
//			filedsList.add(new ExcelExportEntity("合同编码", "contractNumber",20));
//			filedsList.add(new ExcelExportEntity("合同名称", "contractName",20));
//			filedsList.add(new ExcelExportEntity("设备供应商", "suppName",20));
//			filedsList.add(new ExcelExportEntity("合同金额", "contractAmountTax",20));
//			filedsList.add(new ExcelExportEntity("已付款金额", "payAmount",20));
//			filedsList.add(new ExcelExportEntity("付款进度(%)", "payProgress",20));
//			filedsList.add(new ExcelExportEntity("请款金额", "payingAmount",20));
//			filedsList.add(new ExcelExportEntity("请款进度(%)", "payingProgress",20));
//			filedsList.add(new ExcelExportEntity("开票金额", "invoiceAmount",20));
//			filedsList.add(new ExcelExportEntity("开票进度(%)", "invoiceProgress",20));
//
//			if(maxSize > 0){
//				for(int i = 0; i < maxSize; i++){
//
//					filedsList.add(new ExcelExportEntity("付款"+ (i+1), "pay" + (i+1),20));
//				}
//			}
//
//			filedsList.add(new ExcelExportEntity("剩余款项", "remainPayAmount",20));
//		}



		mv.addObject(MapExcelConstants.ENTITY_LIST, filedsList);

		return mv;
	}

	/**
	 * 查询汇率
	 * @param projectId
	 * @return
	 */
	@Override
	public BigDecimal getExchangeRate(String projectId,String currency) throws Exception {
		BigDecimal exchangeRate = BigDecimal.ONE;
		//获取前一个月汇率
		if("RMB".equals(currency)){
			exchangeRate = new BigDecimal(1);
		}else{
			//判断项目是否存在汇率,如果不存在则取全局汇率
			ProjectExchangeRate rate = iProjectExchangeRateService.getOne(Wrappers.<ProjectExchangeRate>query().lambda().
					eq(ProjectExchangeRate :: getCurrencyB,currency).
					eq(ProjectExchangeRate :: getDelFlag,CommonConstant.DEL_FLAG_0).
					eq(ProjectExchangeRate :: getProjectId,projectId));
			if(rate != null){
				exchangeRate = rate.getValueB();
			}else{
				String month = DateUtils.toPrevMonth().substring(0,7);
				List<BasRateMain> mainList = iBasRateMainService.list(Wrappers.<BasRateMain>query().lambda().
						eq(BasRateMain :: getMonth,month).
						eq(BasRateMain :: getCurrencyB,currency));
				if(mainList == null || mainList.size() == 0){
					throw new Exception("请检查上月汇率是否存在");
				}
				BasRateMain main = mainList.get(0);
				exchangeRate = main.getValueB();
			}
		}
		return exchangeRate;
	}

	/**
	 * 生成子合同
	 * @param contractBase
	 * @param contractObjectList
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMainChild(ContractBase contractBase, List<ContractObject> contractObjectList) {
		//获取主合同
		String id = contractBase.getId();
		ContractBase mc = this.getById(id);
		//子合同
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();
		String cid = String.valueOf(IdWorker.getId());
		BigDecimal exchangeRate = mc.getContractExchangeRate();

		//获取子合同的数量
		List<ContractBase> childContractList = this.list(Wrappers.<ContractBase>query().lambda().
				eq(ContractBase :: getMainId,id).
				eq(ContractBase :: getDelFlag,CommonConstant.DEL_FLAG_0));
		int i = 1;
		if(childContractList != null && childContractList.size() > 0){
			i = childContractList.size() + 1;
		}
		ContractBase cc = new ContractBase();
		BeanUtils.copyProperties(mc,cc);
		cc.setContractName(cc.getContractName() + "(子合同)");
		cc.setId(cid);
		cc.setMainId(id);
		cc.setCreateTime(nowTime);
		cc.setCreateUser(username);
		cc.setContractNumber(mc.getContractNumber() + "/" + i);
		cc.setUpdateTime(nowTime);
		cc.setUpdateUser(username);
		cc.setDelFlag(CommonConstant.NO_READ_FLAG);
		cc.setContractStatus(CommonConstant.STATUS_1);
		cc.setSort(cc.getSort() == null ?  1 : cc.getSort() + 1);
		cc.setContractAmountTax(contractBase.getContractAmountTax());
		cc.setContractAmountTaxLocal(contractBase.getContractAmountTaxLocal());
		//计算未税价格
		BigDecimal taxRate = mc.getContractTaxRate();
		if(taxRate.compareTo(new BigDecimal(100)) == 0){
			taxRate = BigDecimal.ZERO;
		}
		taxRate = taxRate.divide(new BigDecimal(100)).add(new BigDecimal(1));
		BigDecimal contractAmountTax = contractBase.getContractAmountTax();

		BigDecimal contractAmount = contractAmountTax.divide(taxRate,2,BigDecimal.ROUND_HALF_UP);
		cc.setContractAmount(contractAmount);

		//计算本币价格
		BigDecimal contractAmountLocal = contractBase.getContractAmountTaxLocal().divide(taxRate,2,BigDecimal.ROUND_HALF_UP);
		cc.setContractAmountLocal(contractAmountLocal);
		cc.setContractStatus("4");
		this.save(cc);

		//获取拆分明细
		List<ContractObjectQty> oldList = iContractObjectQtyService.list(Wrappers.<ContractObjectQty>query().lambda().
				eq(ContractObjectQty :: getContractId,mc.getId()).
				eq(ContractObjectQty :: getDelFlag,CommonConstant.DEL_FLAG_0));

		List<ContractTerms> termsList = iContractTermsService.list(Wrappers.<ContractTerms>query().lambda().
				eq(ContractTerms :: getContractId,id).
				eq(ContractTerms :: getDelFlag,CommonConstant.DEL_FLAG_0));
		List<ContractPayStep> stepList = iContractPayStepService.list(Wrappers.<ContractPayStep>query().lambda().
				eq(ContractPayStep :: getContractId,id).
				eq(ContractPayStep :: getDelFlag,CommonConstant.DEL_FLAG_0));

		for(ContractTerms t : termsList){
			t.setId(String.valueOf(IdWorker.getId()));
			t.setCreateTime(nowTime);
			t.setCreateUser(username);
			t.setUpdateTime(nowTime);
			t.setUpdateUser(username);
			t.setDelFlag(CommonConstant.NO_READ_FLAG);
			t.setContractId(cid);
		}
		iContractTermsService.saveBatch(termsList);

		for(ContractPayStep t : stepList){
			t.setId(String.valueOf(IdWorker.getId()));
			t.setCreateTime(nowTime);
			t.setCreateUser(username);
			t.setUpdateTime(nowTime);
			t.setUpdateUser(username);
			t.setDelFlag(CommonConstant.NO_READ_FLAG);
			t.setContractId(cid);
		}
		iContractPayStepService.saveBatch(stepList);


		//生成明细
		List<ContractObjectQty> qtyList = new ArrayList<>();
		if(contractObjectList!=null && contractObjectList.size()>0) {
			for(ContractObject entity:contractObjectList) {
				//主合同明细ID
				String recordId = entity.getId();

				String toRecordId = String.valueOf(IdWorker.getId());
				entity.setMainDetailId(recordId);
				entity.setId(toRecordId);
				entity.setContractId(cid);
				entity.setDelFlag(CommonConstant.NO_READ_FLAG);
				entity.setCreateTime(nowTime);
				entity.setCreateUser(username);
				entity.setUpdateTime(nowTime);
				entity.setUpdateUser(username);
				entity.setContractTaxRate(mc.getContractTaxRate());
				entity.setExchangeRate(exchangeRate);
				entity.setPlanDeliveryDate(entity.getLeadTime());
				entity.setProdSpecType(entity.getSpeType());

				entity.setContractPriceTax(entity.getPriceTax());
				entity.setContractAmountTax(entity.getAmountTax());
				//计算未税
				BigDecimal amount = entity.getAmountTax().divide(taxRate,2,BigDecimal.ROUND_HALF_UP);
				entity.setContractAmount(amount);
				BigDecimal price = amount.divide(entity.getQty(),4,BigDecimal.ROUND_HALF_UP);
				entity.setContractPrice(price);


				BigDecimal addTax = BigDecimal.ONE;
				BigDecimal customsTax = BigDecimal.ONE;
				BigDecimal otherAmount = BigDecimal.ZERO;
				if(!"RMB".equals(mc.getContractCurrency())){
					addTax = entity.getAddTax();
					customsTax = entity.getCustomsTax();
					otherAmount = entity.getOtherAmount();
				}

				BigDecimal priceTaxLocal = entity.getContractPriceTax().divide(exchangeRate,4,BigDecimal.ROUND_HALF_UP);
				priceTaxLocal = priceTaxLocal.multiply(addTax).multiply(customsTax).add(otherAmount);
				priceTaxLocal = priceTaxLocal.setScale(4,BigDecimal.ROUND_HALF_UP);
				entity.setContractPriceTaxLocal(priceTaxLocal);

				BigDecimal amountTaxLocal = priceTaxLocal.multiply(entity.getQty()).setScale(2,BigDecimal.ROUND_HALF_UP);
				entity.setContractAmountTaxLocal(amountTaxLocal);

				BigDecimal amountLocal = amountTaxLocal.divide(taxRate,2,BigDecimal.ROUND_HALF_UP);
				entity.setContractAmountLocal(amountLocal);

				BigDecimal priceLocal = amountLocal.divide(entity.getQty(),4,BigDecimal.ROUND_HALF_UP);
				entity.setContractPriceLocal(priceLocal);

				//根据序号拆分
				String ids = entity.getIds();
				for(ContractObjectQty coq : oldList){
					if(ids.contains(coq.getId())){
						ContractObjectQty ccoq = new ContractObjectQty();
						BeanUtils.copyProperties(coq,ccoq);
						ccoq.setId(String.valueOf(IdWorker.getId()));
						ccoq.setContractId(cid);
						ccoq.setRecordId(toRecordId);
						ccoq.setDelFlag(CommonConstant.NO_READ_FLAG);
						ccoq.setCreateTime(nowTime);
						ccoq.setCreateUser(username);
						ccoq.setUpdateTime(nowTime);
						ccoq.setUpdateUser(username);
						ccoq.setMainDetailId(coq.getId());
						ccoq.setProdSpecType(entity.getProdSpecType());
						ccoq.setProdBrand(entity.getProdBrand());
						//含税原币
						ccoq.setContractPriceTax(entity.getPriceTax());
						ccoq.setContractAmountTax(entity.getPriceTax());
						//未税原币
						amount = ccoq.getContractAmountTax().divide(taxRate,2,BigDecimal.ROUND_HALF_UP);
						ccoq.setContractAmount(amount);
						ccoq.setContractPrice(amount);
						//含税本币
						ccoq.setContractPriceTaxLocal(priceTaxLocal);
						ccoq.setContractAmountTaxLocal(priceTaxLocal);
						//未税本币
						amountLocal = ccoq.getContractAmountTaxLocal().divide(taxRate,2,BigDecimal.ROUND_HALF_UP);
						ccoq.setContractAmountLocal(amountLocal);
						ccoq.setContractPriceLocal(amountLocal);

						qtyList.add(ccoq);
					}
				}

			}
			iContractObjectService.saveOrUpdateBatch(contractObjectList);
			iContractObjectQtyService.saveOrUpdateBatch(qtyList);

		}
	}

	/**
	 * 审批进度
	 * @param processVo
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result processSpeed(ProcessVo processVo) {
		//如果非SRM系统单据，则直接放行
		Result result = new Result();

		ContractBase cb = this.getOne(Wrappers.<ContractBase>query().lambda().eq(ContractBase :: getProcessId,processVo.getProcessId()));
		if("srm".equals(processVo.getSource())){
			if(cb == null){
				result.setCode(500);
				result.setMessage("合同在系统中不存在");
				result.setResult(null);
				result.setSuccess(false);
				return result;
			}
			//审批记录
			ApproveRecord record = new ApproveRecord();
			record.setId(processVo.getNodeId());
			record.setApprover(processVo.getNodeUser());
			record.setApproveComment(processVo.getComment());
			record.setDelFlag(CommonConstant.NO_READ_FLAG);
			record.setCreateUser(processVo.getNodeUser());
			record.setUpdateUser(processVo.getNodeUser());
			record.setCreateTime(new Date());
			record.setUpdateTime(new Date());
			record.setBusinessId(cb.getId());
			record.setType("OA");
			record.setName(processVo.getNodeName());
			record.setStatus("0");
			//OA审批通过
			cb.setProcessNode(processVo.getNodeName());
			if("1".equals(processVo.getStatus())){
				cb.setContractStatus("4");
				cb.setContractSignDate(new Date());
				cb.setContractValidDate(new Date());
				cb.setProcessNode("审核通过");

				if("1".equals(cb.getIsSag())){
					String contractNumber = cb.getMainNumber();
					String prefix = contractNumber + "-R";
					JSONObject formData = new JSONObject();
					formData.put("prefix", prefix);
					String code = (String) FillRuleUtil.executeRule("contract_sag", formData);
					cb.setContractNumber(code);
				}else{
					String prefix = null;
					ProjBase projBase = iProjBaseService.getById(cb.getProjectId());
					//生成 新的合同编号
					if("1".equals(cb.getContractFirstPartyId())){
						prefix = "YXLX";
					}else if("2".equals(cb.getContractFirstPartyId())){
						prefix = "TJLX";
					}else if("3".equals(cb.getContractFirstPartyId())){
						prefix = "NMLX";
					}else if("4".equals(cb.getContractFirstPartyId())){
						prefix = "XZLX";
					}else if("6".equals(cb.getContractFirstPartyId())){
						prefix = "ZHLX";
					}
					Calendar cal = Calendar.getInstance();
					int year = cal.get(Calendar.YEAR);
					String field7 = null;
					if("RMB".equals(cb.getContractCurrency())){
						field7 = "1";
					}else{
						field7 = "0";
					}
					prefix = prefix + "-XM" + "-" + projBase.getProjCode() + "-" + cb.getContractCategory() + "-" + year + "-";
					JSONObject formData = new JSONObject();
					formData.put("prefix", prefix);
					String code = (String) FillRuleUtil.executeRule("contract_number_new", formData);
					code = code + "-" + field7;
					cb.setContractNumber(code);
				}

			}
			//驳回
			else if ("2".equals(processVo.getStatus())){
				//如果驳回则更新合同状态
				cb.setContractStatus("5");
			}else{
				if(!"4".equals(cb.getContractStatus())){
					iApproveRecordService.saveOrUpdate(record);
				}
			}
			if(processVo.getAttachment() != null && processVo.getAttachment().size() > 0 ){

				List<FileEntity> attachments = processVo.getAttachment();
				List<String> oaAttachment = new ArrayList<>();
				for(FileEntity fileid : attachments){
					String ctxPath = upLoadPath + File.separator + fileid.getName();
					byteToFile(fileid.getValue(),ctxPath);

					oaAttachment.add(fileid.getName());
				}

				if(oaAttachment != null && oaAttachment.size() > 0){
					cb.setOaAttachment(String.join(",",oaAttachment));
				}
			}
			if(!"4".equals(cb.getContractStatus())){
				cb.setProcessCode(processVo.getNodeCode());
			}

			this.updateById(cb);

			result.setResult(cb.getContractNumber());
			result.setCode(200);
			result.setMessage("提交成功");
			result.setSuccess(true);
			return result;
		}else{
			result.setCode(200);
			result.setMessage("提交成功");
			result.setResult("");
			result.setSuccess(true);
			return result;
		}

	}

	/**
	 * 导出
	 * @param contractBase
	 * @return
	 */
	@Override
	public List<ContractBase> queryExportList(ContractBase contractBase) {
		return baseMapper.queryExportList(contractBase);
	}

	/**
	 * 合同总额
	 * @param contractBase
	 * @return
	 */
	@Override
	public List<Map<String, Object>> fetchContractByProjType(ContractBase contractBase) {
		return baseMapper.fetchContractByProjType(contractBase);
	}

	/**
	 * 合同分类统计金额
	 * @param contractBase
	 * @return
	 */
	@Override
	public List<Map<String, Object>> fetchContractAmountByType(ContractBase contractBase) {
		return baseMapper.fetchContractAmountByType(contractBase);
	}

	/**
	 * 合同数量
	 * @param contractBase
	 * @return
	 */
	@Override
	public Map<String, Object> fetchContractNum(ContractBase contractBase) {
		String startMonth = sdf1.format(new Date());
		if("benyue".equals(contractBase.getSource())){
			contractBase.setStartMonth(startMonth);
		}else if("twelve".equals(contractBase.getSource())){
			List<String> monthList = new ArrayList<>();
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
			for(int i=0; i<12; i++) {
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1); //逐次往前推1个月
				monthList.add(String.valueOf(cal.get(Calendar.YEAR))
						+ "-" + (cal.get(Calendar.MONTH) + 1 < 10 ? "0" +
						(cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)));
			}
			Collections.sort(monthList);
			contractBase.setStartMonth(monthList.get(0));
			contractBase.setEndMonth(monthList.get(monthList.size() - 1));
		}
		return baseMapper.fetchContractNum(contractBase);
	}

	/**
	 * 每个月项目数量
	 * @param contractBase
	 * @return
	 */
	@Override
	public Map<String, Object> fetchContractNumByMonth(ContractBase contractBase) {
		List<String> monthList = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
		for(int i=0; i<12; i++) {
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1); //逐次往前推1个月
			monthList.add(String.valueOf(cal.get(Calendar.YEAR))
					+ "-" + (cal.get(Calendar.MONTH) + 1 < 10 ? "0" +
					(cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)));
		}
		Collections.sort(monthList);
		contractBase.setStartMonth(monthList.get(0));
		contractBase.setEndMonth(monthList.get(monthList.size() - 1));
		List<ContractBase> contractList = baseMapper.fetchContractNumByMonth(contractBase);
		List<BigDecimal> dataList = new ArrayList<>();

		for(String st : monthList){
			Boolean flag = false;
			for(ContractBase cb : contractList){
				if(st.equals(cb.getStartMonth())){
					dataList.add(cb.getQty());
					flag = true;
					break;
				}
			}
			if(!flag){
				dataList.add(BigDecimal.ZERO);
			}
		}

		Map<String,Object> map = new HashMap<>();
		map.put("monthList",monthList);
		map.put("dataList",dataList);
		return map;
	}

	/**
	 * 合同台套数
	 * @param contractBase
	 * @return
	 */
	@Override
	public Map<String,Object> fetchQtyNum(ContractBase contractBase) {
		return baseMapper.fetchQtyNum(contractBase);
	}

	/**
	 * 合同金额
	 * @param contractBase
	 * @return
	 */
	@Override
	public Map<String, Object> fetchContractAmountTaxLocal(ContractBase contractBase) {
		return baseMapper.fetchContractAmountTaxLocal(contractBase);
	}

	/**
	 * 已付金额
	 * @param contractBase
	 * @return
	 */
	@Override
	public Map<String, Object> fetchPayAmountTaxLocal(ContractBase contractBase) {
		return baseMapper.fetchPayAmountTaxLocal(contractBase);
	}

	/**
	 * 已开票金额
	 * @param contractBase
	 * @return
	 */
	@Override
	public Map<String, Object> fetchInvoiceAmountTaxLocal(ContractBase contractBase) {
		return baseMapper.fetchInvoiceAmountTaxLocal(contractBase);
	}

	/**
	 * 子项
	 * @param contractBase
	 * @return
	 */
	@Override
	public List<ContractObject> fetchChildList(ContractBase contractBase) {
		return baseMapper.fetchChildList(contractBase);
	}

	/**
	 * 子项
	 * @param contractBase
	 * @return
	 */
	@Override
	public List<ContractObject> fetchChildQtyList(ContractBase contractBase) {
		return baseMapper.fetchChildQtyList(contractBase);
	}

	public static void byteToFile(byte[] contents, String filePath) {
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream output = null;
		try {
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(contents);
			bis = new BufferedInputStream(byteInputStream);
			File file = new File(filePath);
			// 获取文件的父路径字符串
			File path = file.getParentFile();
			if (!path.exists()) {
				System.out.println("文件夹不存在");
				boolean isCreated = path.mkdirs();
				if (!isCreated) {
					System.out.println("创建文件夹失败");
				}
			}
			fos = new FileOutputStream(file);
			// 实例化OutputString 对象
			output = new BufferedOutputStream(fos);
			byte[] buffer = new byte[1024];
			int length = bis.read(buffer);
			while (length != -1) {
				output.write(buffer, 0, length);
				length = bis.read(buffer);
			}
			output.flush();
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				bis.close();
				fos.close();
				output.close();
			} catch (IOException e0) {
				e0.getMessage();
			}
		}
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {

	}




}
