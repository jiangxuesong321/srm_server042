package com.cmoc.modules.srm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmoc.common.api.vo.Result;
import com.cmoc.common.constant.CommonConstant;
import com.cmoc.common.system.vo.DictModel;
import com.cmoc.common.system.vo.LoginUser;
import com.cmoc.common.util.DateUtils;
import com.cmoc.common.util.FillRuleUtil;
import com.cmoc.modules.srm.entity.*;
import com.cmoc.modules.srm.mapper.PurPayPlanDetailMapper;
import com.cmoc.modules.srm.mapper.PurPayPlanMapper;
import com.cmoc.modules.srm.service.*;
import com.cmoc.modules.srm.utils.ChildBody3;
import com.cmoc.modules.srm.utils.JeecgEntityExcel;
import com.cmoc.modules.srm.utils.MsgBody;
import com.cmoc.modules.srm.vo.*;
import com.cmoc.modules.system.service.ISysDictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.def.MapExcelConstants;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.jeecgframework.poi.excel.view.JeecgMapExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description: 付款计划
 * @Author: jeecg-boot
 * @Date:   2022-06-17
 * @Version: V1.0
 */
@Service
@Slf4j
public class PurPayPlanServiceImpl extends ServiceImpl<PurPayPlanMapper, PurPayPlan> implements IPurPayPlanService {

	@Autowired
	private PurPayPlanMapper purPayPlanMapper;
	@Autowired
	private PurPayPlanDetailMapper purPayPlanDetailMapper;
	@Autowired
	private IPurPayPlanDetailService iPurPayPlanDetailService;
	@Autowired
	private IPurPayApplyService iPurPayApplyService;
	@Autowired
	private IContractBaseService iContractBaseService;
	@Autowired
	private IProjBaseService iProjBaseService;
	@Autowired
	private ISysDictService iSysDictService;
	@Autowired
	private IApproveRecordService iApproveRecordService;
	@Autowired
	private IProjectBomChildService iProjectBomChildService;
	@Autowired
	private IApproveSettingService iApproveSettingService;
	@Autowired
	private IPurPayApplyDetailService iPurPayApplyDetailService;
	@Autowired
	private IPurchaseApplyInvoiceService iPurchaseApplyInvoiceService;

	@Value("${jeecg.path.upload}")
	private String upLoadPath;

	@Value("${oa.url}")
	private String endpoint;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PurPayPlan purPayPlan, List<PurPayPlanDetail> purPayPlanDetailList) throws Exception {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = loginUser.getUsername();
		Date nowTime = new Date();
		String id = String.valueOf(IdWorker.getId());
		if(StringUtils.isNotEmpty(purPayPlan.getId())){
			id = purPayPlan.getId();
		}else{
			JSONObject formData = new JSONObject();
			formData.put("prefix", "PB");
			String code = (String) FillRuleUtil.executeRule("pya_plan_code", formData);
			purPayPlan.setCode(code);
			purPayPlan.setCreateTime(nowTime);
			purPayPlan.setCreateUser(username);
		}
		purPayPlan.setId(id);
		purPayPlan.setUpdateTime(nowTime);
		purPayPlan.setUpdateUser(username);
		purPayPlan.setPayStatus(CommonConstant.STATUS_0);
		purPayPlan.setDelFlag(CommonConstant.NO_READ_FLAG);
		purPayPlan.setProcessCreateTime(nowTime);
		purPayPlan.setProcessStatus(CommonConstant.STATUS_0);


		UpdateWrapper<PurPayPlanDetail> child = new UpdateWrapper<>();
		child.set("del_flag", CommonConstant.ACT_SYNC_1);
		child.eq("pay_plan_id",id);
		iPurPayPlanDetailService.update(child);

		//更新应付记录状态
		List<String> ids = new ArrayList<>();
		//一个 合同 生成一个付款计划
		String contractId = null;
		String jsfs = null;
		List<String> jsfsList = new ArrayList<>();
		Set<String> zffsList = new HashSet<>();
		String zffs = null;

		Map<String,String> payType = new HashMap<>();
		List<DictModel> ls = iSysDictService.getDictItems("payType");
		payType = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));

		if(purPayPlanDetailList != null && purPayPlanDetailList.size() > 0) {
			for(PurPayPlanDetail entity : purPayPlanDetailList) {
				//外键设置
				if(StringUtils.isEmpty(entity.getPayApplyId())){
					entity.setPayApplyId(entity.getId());
				}
				entity.setId(String.valueOf(IdWorker.getId()));
				entity.setPayPlanId(purPayPlan.getId());
				entity.setDelFlag(CommonConstant.NO_READ_FLAG);

				contractId = entity.getContractId();
				ids.add(entity.getPayApplyId());

//				zffs = entity.getPayMethod();
				zffsList.add(entity.getPayMethod());
//				String payTypeName = payType.get(entity.getPayType());
//				jsfsList.add(entity.getPayRate() + "%" + payTypeName);

			}
			iPurPayPlanDetailService.saveBatch(purPayPlanDetailList);

			UpdateWrapper<PurPayApply> updateWrapper = new UpdateWrapper<>();
			updateWrapper.set("is_plan",CommonConstant.ACT_SYNC_1);
			updateWrapper.in("id",ids);
			iPurPayApplyService.update(updateWrapper);
		}
		if(zffsList != null && zffsList.size() > 0){
			zffs = String.join(",",zffsList);
		}
//		if(jsfsList != null && jsfsList.size() > 0){
//			jsfs = String.join(";",jsfsList);
//		}
		extracted(purPayPlan, ids, contractId,zffs,jsfs,payType);

		this.saveOrUpdate(purPayPlan);
	}

	private void extracted(PurPayPlan purPayPlan, List<String> ids, String contractId,String zffs,String jsfs,Map<String,String> payType) throws Exception {
		//OA类型 = 现金:0　支票:1　本票:2　汇票:3　电汇:4　信用证:5　其他:6
		//对公转账
		if("0".equals(zffs)){
			zffs = "6";
		}
		//现金
		else if("1".equals(zffs)){
			zffs = "0";
		}
		//电汇
		else if("2".equals(zffs)){
			zffs = "4";
		}
		//信用证
		else if("3".equals(zffs)){
			zffs = "5";
		}
		//电汇 + 信用证
		else if("4".equals(zffs)){
			zffs = "6";
		}
		//承兑
		else if("5".equals(zffs)){
			zffs = "3";
		}
		else{
			zffs = "6";
		}
		//OA
		Map<String,String> oaParam = new HashMap<>();
		List<DictModel> ls = iSysDictService.getDictItems("oa_param");
		oaParam = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));

//		String creatId = oaParam.get("creatId");
		String suffix = oaParam.get("suffix");

		MsgBody msgBody = new MsgBody();

//		msgBody.setBillid("2305");
//		msgBody.setWorkflowid("140529");

		String billid2 = oaParam.get("billid3");
		String workflowid2 = oaParam.get("workflowid3");

		msgBody.setBillid(billid2);
		msgBody.setWorkflowid(workflowid2);
		msgBody.setSource("srm");

		ContractBase cb = iContractBaseService.getById(contractId);

		//发起人工号，配置项
		List<ApproveSetting> asList = iApproveSettingService.list(Wrappers.<ApproveSetting>query().lambda().
				eq(ApproveSetting :: getDelFlag,CommonConstant.DEL_FLAG_0).
				eq(ApproveSetting :: getType,"oaPay").
				eq(ApproveSetting :: getCompany,cb.getContractFirstPartyId()));
		if(asList == null || asList.size() == 0){
			throw new Exception("当前主体没有配置OA发起人");
		}
		String creatId = asList.get(0).getUsername();
		msgBody.setCreatId(creatId);
		//宜兴
		if("1".equals(cb.getContractFirstPartyId())){
			msgBody.setSqgs("0");
			msgBody.setQklx("0");
		}
		//内蒙
		else if("3:".equals(cb.getContractFirstPartyId())){
			msgBody.setSqgs("1");
			msgBody.setQklx("3");
		}
		//天津
		else if("2".equals(cb.getContractFirstPartyId())){
			msgBody.setSqgs("2");
			msgBody.setQklx("2");
		}
		ProjBase projBase = iProjBaseService.getById(cb.getProjectId());
		msgBody.setXmmc(projBase.getProjName());
		msgBody.setHth(cb.getContractNumber());
		List<PurPayApply> applyList = iPurPayApplyService.listByIds(ids);
		//获取付款信息，默认取第一条
		String openBanl = applyList.get(0).getReceivingBank();
		String openNumber = applyList.get(0).getReceivingNumber();

		List<Map<String,String>> mapList = new ArrayList<>();
		for(PurPayApply ppa : applyList){
			String attachment = ppa.getSuppAttachment();
			if(StringUtils.isNotEmpty(attachment)){

				String[] paths = attachment.split(",");
				for(String path : paths){
					String[] str = path.split("/");
					String name = str[str.length - 1];

					Map<String,String> map = new HashMap<>();
					map.put("name",name);
					map.put("url",suffix + enCodeUrl(path));
					mapList.add(map);
				}
			}
		}
		if(StringUtils.isNotEmpty(cb.getOaAttachment())){
			String[] paths = cb.getOaAttachment().split(",");
			for(String path : paths){
				String[] str = path.split("/");
				String name = str[str.length - 1];

				Map<String,String> map = new HashMap<>();
				map.put("name",name);
				map.put("url",suffix + enCodeUrl(path));
				mapList.add(map);
			}
		}
		if(mapList != null && mapList.size() > 0){
			msgBody.setScfj(mapList);
		}
		msgBody.setCjdw(cb.getContractSecondParty());
		//获取对应付款申请得账号、开户行
		msgBody.setKhx(openBanl);
		msgBody.setZh(openNumber);
		//TODO
		msgBody.setZffs(zffs);

		msgBody.setHtje(cb.getContractAmountTax());
		msgBody.setRmbje(cb.getContractAmountTaxLocal());
		msgBody.setSflrzjjh("0");

		String unit = "";
		if("RMB".equals(cb.getContractCurrency())){
			msgBody.setBcqkjehjxx1(purPayPlan.getPayAmountCope());
			msgBody.setBb("CNY");
			unit = "元";
		}else if("USD".equals(cb.getContractCurrency())){
			msgBody.setBcqkjehjxx1(purPayPlan.getPayAmountUsd());
			msgBody.setBb("美元");
			unit = "美元";
		}else if("JPY".equals(cb.getContractCurrency())){
			msgBody.setBcqkjehjxx1(purPayPlan.getPayAmountJpy());
			msgBody.setBb("日元");
			unit = "日元";
		}else if("EUR".equals(cb.getContractCurrency())){
			msgBody.setBcqkjehjxx1(purPayPlan.getPayAmountEur());
			msgBody.setBb("欧元");
			unit = "欧元";
		}
		//合同工程（设备名称） = 2台 + 设备名称 - 总金额 - 厂商名称
		List<PurPayApplyDetail> applyDetailList = iPurPayApplyService.fetchPayDetailList(ids);
		List<String> names = new ArrayList<>();
		for(PurPayApplyDetail ppad : applyDetailList){
			String name = ppad.getQty().stripTrailingZeros().toPlainString() + "台" + ppad.getProdName();
			names.add(name);
		}
		String htgc = String.join(";",names) + "-" + msgBody.getBcqkjehjxx1() + unit + "-" + cb.getContractSecondParty();
		msgBody.setHtgc(htgc);

		//请款明细
		List<PurPayApplyDetail> detailList = iPurPayApplyDetailService.list(Wrappers.<PurPayApplyDetail>query().lambda().
				in(PurPayApplyDetail :: getApplyId,ids).
				eq(PurPayApplyDetail :: getDelFlag,CommonConstant.DEL_FLAG_0));

		//累计请款金额
		List<PurPayApply> ppaList = iPurPayApplyService.fetchHasPayDetailList(contractId);

		BigDecimal totalPayAmount = BigDecimal.ZERO;
		if(ppaList != null && ppaList.size() > 0){
			for(PurPayApply ppa : ppaList){
				totalPayAmount = totalPayAmount.add(ppa.getPayAmountOther());
			}
		}

		//累计开票金额(不包含本次)
		PurchaseApplyInvoice invoice = iPurchaseApplyInvoiceService.fetchDetailListByContractId(contractId,ids);
		BigDecimal invoiceAmount = BigDecimal.ZERO;
		if(invoice != null ){
			invoiceAmount = invoice.getInvoiceAmountTax();
		}
		//本次开票金额
		List<PurchaseApplyInvoice> paiList = iPurchaseApplyInvoiceService.fetchDetailListByApplyids(ids);
		//是否已计算过
		Map<String,String> exist = new HashMap<>();

		List<ChildBody3> Childs3 = new ArrayList<>();
		List<String> jsfsList = new ArrayList<>();
		for(PurPayApplyDetail ppad : detailList){
			ChildBody3 entity = new ChildBody3();
			entity.setBqqkje(ppad.getContractAmountTax());

			BigDecimal bqqk = entity.getBqqkje().divide(cb.getContractAmountTax(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			entity.setBqqk(bqqk);

			totalPayAmount = totalPayAmount.add(entity.getBqqkje());
			entity.setLjqkje(totalPayAmount);

			BigDecimal ljqk = totalPayAmount.divide(cb.getContractAmountTax(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			entity.setLjqk(ljqk);

			BigDecimal amount = BigDecimal.ZERO;
			for(PurchaseApplyInvoice pai : paiList){
				if(ppad.getApplyId().equals(pai.getApplyId())){
					amount = amount.add(pai.getInvoiceAmountTax());
					break;
				}
			}
			//判断是否已累加
			if(exist.containsKey(ppad.getApplyId())){

			}else{
				invoiceAmount = invoiceAmount.add(amount);
				exist.put(ppad.getApplyId(), ppad.getApplyId());
			}
			entity.setLjytgfpje(invoiceAmount);


			BigDecimal payRate = ppad.getPayRate();
			if(payRate.compareTo(new BigDecimal(1)) == -1){
				payRate = payRate.multiply(new BigDecimal(100));
			}

			String payTypeName = payType.get(ppad.getPayType());
			String kxsm = "#" + ppad.getNo() + "," +payRate + "%" + payTypeName;
			entity.setKxsm(kxsm);

			jsfsList.add(payRate + "%" + payTypeName);

			Childs3.add(entity);
		}
		msgBody.setChilds3(Childs3);
		if(jsfsList != null && jsfsList.size() > 0){
			msgBody.setJsfs(String.join(",",jsfsList));
		}

		String userStr = JSONObject.toJSONString(msgBody);
		log.info("OA传参数据===========:" + userStr);

		// 创建动态客户端
		log.info("开始创建客户端1=====" + System.currentTimeMillis());
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		log.info("开始创建客户端2=====" + System.currentTimeMillis());
		Client client = dcf.createClient(endpoint);
		log.info("开始创建客户端3=====" + System.currentTimeMillis());

		Object[] objects = new Object[0];
		try {
			objects = client.invoke("createxmsrm", userStr);
			System.out.println("返回数据:" + objects[0].toString());
			net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(objects[0]);
			String status = jsonObject.getString("status");
			String requestid = jsonObject.getString("requestid");

			if("success".equals(status)){
				purPayPlan.setPayStatus("0");
				purPayPlan.setProcessId(requestid);
			}else{
				purPayPlan.setPayStatus("9");
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception("调用OA接口出错");
		}
	}

	@Override
	public void toOa(PurPayPlan purPayPlan) throws Exception {
		List<PurPayPlanDetail> detailList = baseMapper.fetchDetailList(purPayPlan);
		List<String> ids = new ArrayList<>();
		String contractId = null;
		String jsfs = null;
		List<String> jsfsList = new ArrayList<>();
		String zffs = null;

		Map<String,String> payType = new HashMap<>();
		List<DictModel> ls = iSysDictService.getDictItems("payType");
		payType = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));

		for(PurPayPlanDetail ppd : detailList){
			ids.add(ppd.getPayApplyId());
			contractId  = ppd.getContractId();

			zffs = ppd.getPayMethod();

			String payTypeName = payType.get(ppd.getPayType());
			jsfsList.add(ppd.getPayRate() + "%" + payTypeName);
		}
		if(jsfsList != null && jsfsList.size() > 0){
			jsfs = String.join(";",jsfsList);
		}
		extracted(purPayPlan, ids, contractId,zffs,jsfs,payType);;
		purPayPlan.setProcessCreateTime(new Date());
		this.updateById(purPayPlan);
	}

	/**
	 * 审批进度
	 * @param processVo
	 * @return
	 */
	@Override
	public Result processSpeed(ProcessVo processVo) {
		Result result = new Result();
		if("srm".equals(processVo.getSource())){
			PurPayPlan ppp = this.getOne(Wrappers.<PurPayPlan>query().lambda().eq(PurPayPlan :: getProcessId,processVo.getProcessId()));
			if(ppp == null){
				result.setCode(500);
				result.setMessage("付款申请在系统中不存在");
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
			record.setName(processVo.getNodeName());
			record.setBusinessId(ppp.getId());
			record.setType("OA_PLAN");
			//OA审批通过
			ppp.setProcessNode(processVo.getNodeName());
			if("1".equals(processVo.getStatus())){
				ppp.setPayStatus("2");
				ppp.setPayTime(new Date());
				ppp.setProcessNode("审核通过");
			}
			//驳回
			else if ("2".equals(processVo.getStatus())){
				ppp.setPayStatus("1");
			}else{
				if(!"2".equals(ppp.getPayStatus())){
					iApproveRecordService.saveOrUpdate(record);
				}
//				iApproveRecordService.saveOrUpdate(record);
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
					ppp.setOaAttachment(String.join(",",oaAttachment));
				}
			}
			if(!"2".equals(ppp.getPayStatus())){
				ppp.setProcessCode(processVo.getNodeCode());
			}

			this.updateById(ppp);

			result.setResult(processVo.getProcessId());
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


	public String enCodeUrl(String url) throws UnsupportedEncodingException {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]+");
		Matcher m = p.matcher(url);
		StringBuffer b = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(b, URLEncoder.encode(m.group(0), "utf-8"));
		}
		m.appendTail(b);
		return b.toString();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PurPayPlan purPayPlan,List<PurPayPlanDetail> purPayPlanDetailList) {
		purPayPlanMapper.updateById(purPayPlan);
		
		//1.先删除子表数据
		purPayPlanDetailMapper.deleteByMainId(purPayPlan.getId());
		
		//2.子表数据重新插入
		if(purPayPlanDetailList!=null && purPayPlanDetailList.size()>0) {
			for(PurPayPlanDetail entity:purPayPlanDetailList) {
				//外键设置
				entity.setPayPlanId(purPayPlan.getId());
				purPayPlanDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		purPayPlanDetailMapper.deleteByMainId(id);
		purPayPlanMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			purPayPlanDetailMapper.deleteByMainId(id.toString());
			purPayPlanMapper.deleteById(id);
		}
	}

	/**
	 * 币种汇总
	 * @param purPayPlan
	 * @return
	 */
	@Override
	public PurPayPlan getTotalAmountByCurrency(PurPayPlan purPayPlan) {
		return baseMapper.getTotalAmountByCurrency(purPayPlan);
	}

	/**
	 * 以项目为单位统计已付款金额
	 * @param purPayPlan
	 * @return
	 */
	@Override
	public PurPayPlan fetchPayAmountByProjId(PurPayPlan purPayPlan) {
		String startMonth = sdf1.format(new Date());
		if("benyue".equals(purPayPlan.getSource())){
			purPayPlan.setStartMonth(startMonth);
		}else if("twelve".equals(purPayPlan.getSource())){
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
			purPayPlan.setStartMonth(monthList.get(0));
			purPayPlan.setEndMonth(monthList.get(monthList.size() - 1));
		}
		return baseMapper.fetchPayAmountByProjId(purPayPlan);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param purPayPlan
	 * @return
	 */
	@Override
	public IPage<PurPayPlan> queryPageList(Page<PurPayPlan> page, PurPayPlan purPayPlan) {
		return baseMapper.queryPageList(page,purPayPlan);
	}

	/**
	 * 首页 - 资金计划
	 * @param purPayPlan
	 * @return
	 */
	@Override
	public List<Map<String, Object>> fetchPlanAmount(PurPayPlan purPayPlan) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String deptId = loginUser.getDepartIds();

		//如果是部门职员进入则看该部门数据汇总
//		if("dept".equals(purPayPlan.getSource())){
//			purPayPlan.setDeptId(deptId);
//		}

		List<Map<String,Object>> pageList = new ArrayList<>();
		List<String> monthList = new ArrayList<>();
		//如果 不存在月份 选择,则默认只查最近7个月数据
		if(StringUtils.isNotEmpty(purPayPlan.getStartMonth()) && StringUtils.isNotEmpty(purPayPlan.getEndMonth())){
			String startStr = purPayPlan.getStartMonth();
			String endStr = purPayPlan.getEndMonth();
			monthList = getMonthBetweenDate(startStr, endStr);
			Collections.sort(monthList);
		}else{
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
			for(int i=0; i<6; i++) {
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1); //逐次往前推1个月
				monthList.add(String.valueOf(cal.get(Calendar.YEAR))
						+ "-" + (cal.get(Calendar.MONTH) + 1 < 10 ? "0" +
						(cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)));
			}
			Collections.sort(monthList);
			purPayPlan.setStartMonth(monthList.get(0));
			purPayPlan.setEndMonth(monthList.get(monthList.size() - 1));
		}
		List<PurPayPlan> planList = baseMapper.fetchPlanAmount(purPayPlan);
		for(String month : monthList){
			Map<String,Object> map = new HashMap<>();
			BigDecimal payAMount = BigDecimal.ZERO;
			for(PurPayPlan ppp : planList){
				if(month.equals(ppp.getPlanMonth())){
					payAMount = ppp.getPayAmountCope();
					break;
				}
			}
			map.put("x",month);
			map.put("y",payAMount);
			pageList.add(map);
		}

		return pageList;
	}

	/**
	 * 付款详情
	 * @param page
	 * @param purPayPlan
	 * @return
	 */
	@Override
	public IPage<PurPayPlan> fetchPageList(Page<PurPayPlan> page, PurPayPlan purPayPlan) {
		return baseMapper.fetchPageList(page,purPayPlan);
	}

	/**
	 * 付款流程节点报表
	 * @param page
	 * @param contractBase
	 * @return
	 */
	@Override
	public IPage<PayProgress> fetchPayProgressPageList(Page<PayProgress> page, PayProgress contractBase) {
		return baseMapper.fetchPayProgressPageList(page,contractBase);
	}

	/**
	 * 目标完成情况数据
	 * @param param
	 * @return
	 */
	@Override
	public List<AmountPlanToYear> fetchPlanAmountByYear(AmountPlanToYear param) {
		List<String> monthList = DateUtils.toDateList(param.getStartMonth(),param.getEndMonth());
		//统计当前年份每个月的付款计划数量
		List<Map<String,Object>> planList = baseMapper.fetchPlanByYear(param);
		//统计每个月完成的数量
		List<Map<String,Object>> completeList = baseMapper.fetchCompletePlanByYear(param);
		List<AmountPlanToYear> yearList = new ArrayList<>();
//		setFiled(yearList);

		//目标
		extracted(planList, yearList,monthList);
		//完成
		extracted1(completeList, yearList,monthList);

		AmountPlanToYear year1 = yearList.get(0);
		AmountPlanToYear year2 = yearList.get(1);
		//完成率
		extracted2(yearList, year1, year2);
		//差额
		extracted3(yearList, year1, year2);
		AmountPlanToYear year3 = yearList.get(3);
		//占比
		extracted4(yearList, year1, year3);

		return yearList;
	}



	/**
	 * 完成率&占比
	 * @param param
	 * @return
	 */
	@Override
	public Map<String,Object> fetchPlanAmountRate(AmountPlanToYear param) {
		List<String> monthList = DateUtils.toDateList(param.getStartMonth(),param.getEndMonth());
		//统计当前年份每个月的付款计划数量
		List<Map<String,Object>> planList = baseMapper.fetchPlanByYear(param);
		//统计每个月完成的数量
		List<Map<String,Object>> completeList = baseMapper.fetchCompletePlanByYear(param);

		List<AmountPlanToYear> yearList = new ArrayList<>();
		//目标
		extracted(planList, yearList,monthList);
		//完成
		extracted1(completeList, yearList,monthList);

		List<BigDecimal> rateList = new ArrayList<>();
		List<BigDecimal> propList = new ArrayList<>();
		for(int i = 0; i < monthList.size(); i++){

			AmountPlanToYear year1 = yearList.get(0);
			AmountPlanToYear year2 = yearList.get(1);

			if(i == 0){
				BigDecimal one = BigDecimal.ZERO;
				//完成率
				if(year1.getOne().compareTo(BigDecimal.ZERO) == 1){
					one = year2.getOne().divide(year1.getOne(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(one);
				//占比
				one = year2.getOne().subtract(year1.getOne());
				if(year1.getOne().compareTo(BigDecimal.ZERO) == 1){
					one = one.divide(year1.getOne(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					one = BigDecimal.ZERO;
				}
				propList.add(one);
			}else if(i == 1){
				BigDecimal two = BigDecimal.ZERO;
				//完成率
				if(year1.getTwo().compareTo(BigDecimal.ZERO) == 1){
					two = year2.getTwo().divide(year1.getTwo(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(two);
				//占比
				two = year2.getTwo().subtract(year1.getTwo());
				if(year1.getTwo().compareTo(BigDecimal.ZERO) == 1){
					two = two.divide(year1.getTwo(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					two = BigDecimal.ZERO;
				}
				propList.add(two);
			}else if(i == 2){
				BigDecimal three = BigDecimal.ZERO;
				//完成率
				if(year1.getThree().compareTo(BigDecimal.ZERO) == 1){
					three = year2.getThree().divide(year1.getThree(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(three);
				//占比
				three = year2.getThree().subtract(year1.getThree());
				if(year1.getThree().compareTo(BigDecimal.ZERO) == 1){
					three = three.divide(year1.getThree(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					three = BigDecimal.ZERO;
				}
				propList.add(three);
			}else if(i == 3){
				BigDecimal four = BigDecimal.ZERO;
				//完成率
				if(year1.getFour().compareTo(BigDecimal.ZERO) == 1){
					four = year2.getFour().divide(year1.getFour(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(four);
				//占比
				four = year2.getFour().subtract(year1.getFour());
				if(year1.getFour().compareTo(BigDecimal.ZERO) == 1){
					four = four.divide(year1.getFour(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					four = BigDecimal.ZERO;
				}
				propList.add(four);
			}else if(i == 4){
				BigDecimal five = BigDecimal.ZERO;
				//完成率
				if(year1.getFive().compareTo(BigDecimal.ZERO) == 1){
					five = year2.getFive().divide(year1.getFive(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(five);
				//占比
				five = year2.getFive().subtract(year1.getFive());
				if(year1.getFive().compareTo(BigDecimal.ZERO) == 1){
					five = five.divide(year1.getFive(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					five = BigDecimal.ZERO;
				}
				propList.add(five);
			}else if(i == 5){
				BigDecimal six = BigDecimal.ZERO;
				//完成率
				if(year1.getSix().compareTo(BigDecimal.ZERO) == 1){
					six = year2.getSix().divide(year1.getSix(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(six);
				//占比
				six = year2.getSix().subtract(year1.getSix());
				if(year1.getSix().compareTo(BigDecimal.ZERO) == 1){
					six = six.divide(year1.getSix(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					six = BigDecimal.ZERO;
				}
				propList.add(six);
			}else if(i == 6){
				BigDecimal seven = BigDecimal.ZERO;
				//完成率
				if(year1.getSeven().compareTo(BigDecimal.ZERO) == 1){
					seven = year2.getSeven().divide(year1.getSeven(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(seven);
				//占比
				seven = year2.getSeven().subtract(year1.getSeven());
				if(year1.getSeven().compareTo(BigDecimal.ZERO) == 1){
					seven = seven.divide(year1.getSeven(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					seven = BigDecimal.ZERO;
				}
				propList.add(seven);
			}else if(i == 7){
				BigDecimal eight = BigDecimal.ZERO;
				//完成率
				if(year1.getEight().compareTo(BigDecimal.ZERO) == 1){
					eight = year2.getEight().divide(year1.getEight(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(eight);
				//占比
				eight = year2.getEight().subtract(year1.getEight());
				if(year1.getEight().compareTo(BigDecimal.ZERO) == 1){
					eight = eight.divide(year1.getEight(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					eight = BigDecimal.ZERO;
				}
				propList.add(eight);
			}else if(i == 8){
				BigDecimal nine = BigDecimal.ZERO;
				//完成率
				if(year1.getNine().compareTo(BigDecimal.ZERO) == 1){
					nine = year2.getNine().divide(year1.getNine(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(nine);
				//占比
				nine = year2.getNine().subtract(year1.getNine());
				if(year1.getNine().compareTo(BigDecimal.ZERO) == 1){
					nine = nine.divide(year1.getNine(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					nine = BigDecimal.ZERO;
				}
				propList.add(nine);
			}else if(i == 9){
				BigDecimal ten = BigDecimal.ZERO;
				//完成率
				if(year1.getTen().compareTo(BigDecimal.ZERO) == 1){
					ten = year2.getTen().divide(year1.getTen(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(ten);
				//占比
				ten = year2.getTen().subtract(year1.getTen());
				if(year1.getTen().compareTo(BigDecimal.ZERO) == 1){
					ten = ten.divide(year1.getTen(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					ten = BigDecimal.ZERO;
				}
				propList.add(ten);
			}else if(i == 10){
				BigDecimal eleven = BigDecimal.ZERO;
				//完成率
				if(year1.getEleven().compareTo(BigDecimal.ZERO) == 1){
					eleven = year2.getEleven().divide(year1.getEleven(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(eleven);
				//占比
				eleven = year2.getEleven().subtract(year1.getEleven());
				if(year1.getEleven().compareTo(BigDecimal.ZERO) == 1){
					eleven = eleven.divide(year1.getEleven(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					eleven = BigDecimal.ZERO;
				}
				propList.add(eleven);
			}else if(i == 11){
				BigDecimal twelev = BigDecimal.ZERO;
				//完成率
				if(year1.getTwelve().compareTo(BigDecimal.ZERO) == 1){
					twelev = year2.getTwelve().divide(year1.getTwelve(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(twelev);
				//占比
				twelev = year2.getTwelve().subtract(year1.getTwelve());
				if(year1.getTwelve().compareTo(BigDecimal.ZERO) == 1){
					twelev = twelev.divide(year1.getTwelve(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					twelev = BigDecimal.ZERO;
				}
				propList.add(twelev);
			}

			else if(i == 12){
				BigDecimal ten3 = BigDecimal.ZERO;
				//完成率
				if(year1.getTen3().compareTo(BigDecimal.ZERO) == 1){
					ten3 = year2.getTen3().divide(year1.getTen3(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(ten3);
				//占比
				ten3 = year2.getTen3().subtract(year1.getTen3());
				if(year1.getTen3().compareTo(BigDecimal.ZERO) == 1){
					ten3 = ten3.divide(year1.getTen3(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					ten3 = BigDecimal.ZERO;
				}
				propList.add(ten3);
			}

			else if(i == 13){
				BigDecimal ten4 = BigDecimal.ZERO;
				//完成率
				if(year1.getTen4().compareTo(BigDecimal.ZERO) == 1){
					ten4 = year2.getTen4().divide(year1.getTen4(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(ten4);
				//占比
				ten4 = year2.getTen4().subtract(year1.getTen4());
				if(year1.getTen4().compareTo(BigDecimal.ZERO) == 1){
					ten4 = ten4.divide(year1.getTen4(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					ten4 = BigDecimal.ZERO;
				}
				propList.add(ten4);
			}

			else if(i == 14){
				BigDecimal ten5 = BigDecimal.ZERO;
				//完成率
				if(year1.getTen5().compareTo(BigDecimal.ZERO) == 1){
					ten5 = year2.getTen5().divide(year1.getTen5(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(ten5);
				//占比
				ten5 = year2.getTen5().subtract(year1.getTen5());
				if(year1.getTen5().compareTo(BigDecimal.ZERO) == 1){
					ten5 = ten5.divide(year1.getTen5(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					ten5 = BigDecimal.ZERO;
				}
				propList.add(ten5);
			}
			else if(i == 15){
				BigDecimal ten6 = BigDecimal.ZERO;
				//完成率
				if(year1.getTen6().compareTo(BigDecimal.ZERO) == 1){
					ten6 = year2.getTen6().divide(year1.getTen6(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(ten6);
				//占比
				ten6 = year2.getTen6().subtract(year1.getTen6());
				if(year1.getTen6().compareTo(BigDecimal.ZERO) == 1){
					ten6 = ten6.divide(year1.getTen6(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					ten6 = BigDecimal.ZERO;
				}
				propList.add(ten6);
			}
			else if(i == 16){
				BigDecimal ten7 = BigDecimal.ZERO;
				//完成率
				if(year1.getTen7().compareTo(BigDecimal.ZERO) == 1){
					ten7 = year2.getTen7().divide(year1.getTen7(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(ten7);
				//占比
				ten7 = year2.getTen7().subtract(year1.getTen7());
				if(year1.getTen7().compareTo(BigDecimal.ZERO) == 1){
					ten7 = ten7.divide(year1.getTen7(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					ten7 = BigDecimal.ZERO;
				}
				propList.add(ten7);
			}
			else if(i == 17){
				BigDecimal ten8 = BigDecimal.ZERO;
				//完成率
				if(year1.getTen8().compareTo(BigDecimal.ZERO) == 1){
					ten8 = year2.getTen8().divide(year1.getTen8(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(ten8);
				//占比
				ten8 = year2.getTen8().subtract(year1.getTen8());
				if(year1.getTen8().compareTo(BigDecimal.ZERO) == 1){
					ten8 = ten8.divide(year1.getTen8(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					ten8 = BigDecimal.ZERO;
				}
				propList.add(ten8);
			}
			else if(i == 18){
				BigDecimal ten9 = BigDecimal.ZERO;
				//完成率
				if(year1.getTen9().compareTo(BigDecimal.ZERO) == 1){
					ten9 = year2.getTen9().divide(year1.getTen9(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(ten9);
				//占比
				ten9 = year2.getTen9().subtract(year1.getTen9());
				if(year1.getTen9().compareTo(BigDecimal.ZERO) == 1){
					ten9 = ten9.divide(year1.getTen9(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					ten9 = BigDecimal.ZERO;
				}
				propList.add(ten9);
			}
			else if(i == 19){
				BigDecimal ten10 = BigDecimal.ZERO;
				//完成率
				if(year1.getTen10().compareTo(BigDecimal.ZERO) == 1){
					ten10 = year2.getTen10().divide(year1.getTen10(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(ten10);
				//占比
				ten10 = year2.getTen10().subtract(year1.getTen10());
				if(year1.getTen10().compareTo(BigDecimal.ZERO) == 1){
					ten10 = ten10.divide(year1.getTen10(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}else{
					ten10 = BigDecimal.ZERO;
				}
				propList.add(ten10);
			}
			else if(i == 20){
				BigDecimal ten11 = BigDecimal.ZERO;
				//完成率
				if(year1.getTen11().compareTo(BigDecimal.ZERO) == 1){
					ten11 = year2.getTen11().divide(year1.getTen11(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(110));
				}
				rateList.add(ten11);
				//占比
				ten11 = year2.getTen11().subtract(year1.getTen11());
				if(year1.getTen11().compareTo(BigDecimal.ZERO) == 1){
					ten11 = ten11.divide(year1.getTen11(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(110));
				}else{
					ten11 = BigDecimal.ZERO;
				}
				propList.add(ten11);
			}
			else if(i == 21){
				BigDecimal ten12 = BigDecimal.ZERO;
				//完成率
				if(year1.getTen12().compareTo(BigDecimal.ZERO) == 1){
					ten12 = year2.getTen12().divide(year1.getTen12(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(120));
				}
				rateList.add(ten12);
				//占比
				ten12 = year2.getTen12().subtract(year1.getTen12());
				if(year1.getTen12().compareTo(BigDecimal.ZERO) == 1){
					ten12 = ten12.divide(year1.getTen12(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(120));
				}else{
					ten12 = BigDecimal.ZERO;
				}
				propList.add(ten12);
			}
			else if(i == 22){
				BigDecimal ten13 = BigDecimal.ZERO;
				//完成率
				if(year1.getTen13().compareTo(BigDecimal.ZERO) == 1){
					ten13 = year2.getTen13().divide(year1.getTen13(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(130));
				}
				rateList.add(ten13);
				//占比
				ten13 = year2.getTen13().subtract(year1.getTen13());
				if(year1.getTen13().compareTo(BigDecimal.ZERO) == 1){
					ten13 = ten13.divide(year1.getTen13(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(130));
				}else{
					ten13 = BigDecimal.ZERO;
				}
				propList.add(ten13);
			}
			else if(i == 23){
				BigDecimal ten14 = BigDecimal.ZERO;
				//完成率
				if(year1.getTen14().compareTo(BigDecimal.ZERO) == 1){
					ten14 = year2.getTen14().divide(year1.getTen14(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(140));
				}
				rateList.add(ten14);
				//占比
				ten14 = year2.getTen14().subtract(year1.getTen14());
				if(year1.getTen14().compareTo(BigDecimal.ZERO) == 1){
					ten14 = ten14.divide(year1.getTen14(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(140));
				}else{
					ten14 = BigDecimal.ZERO;
				}
				propList.add(ten14);
			}
			
		}
		Map<String,Object> map = new HashMap<>();
		map.put("month",monthList);

		List<AmountPlanToYear> pageList = new ArrayList<>();
		AmountPlanToYear year1 = new AmountPlanToYear();
		year1.setName("完成率");
		year1.setType("line");
		year1.setStack("Total");
		year1.setData(rateList);
		pageList.add(year1);

//		AmountPlanToYear year2 = new AmountPlanToYear();
//		year2.setName("占比");
//		year2.setType("line");
//		year2.setStack("Total");
//		year2.setData(propList);
//		pageList.add(year2);


		map.put("data",pageList);

		return map;
	}

	/**
	 * 采购资金目标与完成分析
	 * @param param
	 * @return
	 */
	@Override
	public Map<String, Object> fetchPlanAmountProgress(AmountPlanToYear param) {
		if(param.getYear().length() > 4){
			param.setYear(param.getYear().substring(1,5));
		}
		//统计当前年份每个月的付款计划数量
		List<Map<String,Object>> planList = baseMapper.fetchPlanByYear(param);
		//统计每个月完成的数量
		List<Map<String,Object>> completeList = baseMapper.fetchCompletePlanByYear(param);

		List<AmountPlanToYear> yearList = new ArrayList<>();
		//目标
		extracted(planList, yearList,new ArrayList<>());
		//完成
		extracted1(completeList, yearList,new ArrayList<>());

		List<BigDecimal> rateList = new ArrayList<>();
		List<BigDecimal> allList = new ArrayList<>();
		List<BigDecimal> cpList = new ArrayList<>();
		List<String> monthList = new ArrayList<>();
		for(int i = 0; i < 12; i++){

			AmountPlanToYear year1 = yearList.get(0);
			AmountPlanToYear year2 = yearList.get(1);

			if(i == 0){
				monthList.add("一月");
				BigDecimal one = BigDecimal.ZERO;
				//完成率
				if(year1.getOne().compareTo(BigDecimal.ZERO) == 1){
					one = year2.getOne().divide(year1.getOne(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(one);
				allList.add(year1.getOne());
				cpList.add(year2.getOne());

			}else if(i == 1){
				monthList.add("二月");
				BigDecimal two = BigDecimal.ZERO;
				//完成率
				if(year1.getTwo().compareTo(BigDecimal.ZERO) == 1){
					two = year2.getTwo().divide(year1.getTwo(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(two);
				allList.add(year1.getTwo());
				cpList.add(year2.getTwo());

			}else if(i == 2){
				monthList.add("三月");
				BigDecimal three = BigDecimal.ZERO;
				//完成率
				if(year1.getThree().compareTo(BigDecimal.ZERO) == 1){
					three = year2.getThree().divide(year1.getThree(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(three);
				allList.add(year1.getThree());
				cpList.add(year2.getThree());

			}else if(i == 3){
				monthList.add("四月");
				BigDecimal four = BigDecimal.ZERO;
				//完成率
				if(year1.getFour().compareTo(BigDecimal.ZERO) == 1){
					four = year2.getFour().divide(year1.getFour(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(four);
				allList.add(year1.getFour());
				cpList.add(year2.getFour());

			}else if(i == 4){
				monthList.add("五月");
				BigDecimal five = BigDecimal.ZERO;
				//完成率
				if(year1.getFive().compareTo(BigDecimal.ZERO) == 1){
					five = year2.getFive().divide(year1.getFive(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(five);
				allList.add(year1.getFive());
				cpList.add(year2.getFive());

			}else if(i == 5){
				monthList.add("六月");
				BigDecimal six = BigDecimal.ZERO;
				//完成率
				if(year1.getSix().compareTo(BigDecimal.ZERO) == 1){
					six = year2.getSix().divide(year1.getSix(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(six);
				allList.add(year1.getSix());
				cpList.add(year2.getSix());

			}else if(i == 6){
				monthList.add("七月");
				BigDecimal seven = BigDecimal.ZERO;
				//完成率
				if(year1.getSeven().compareTo(BigDecimal.ZERO) == 1){
					seven = year2.getSeven().divide(year1.getSeven(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(seven);
				allList.add(year1.getSeven());
				cpList.add(year2.getSeven());

			}else if(i == 7){
				monthList.add("八月");
				BigDecimal eight = BigDecimal.ZERO;
				//完成率
				if(year1.getEight().compareTo(BigDecimal.ZERO) == 1){
					eight = year2.getEight().divide(year1.getEight(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(eight);
				allList.add(year1.getEight());
				cpList.add(year2.getEight());

			}else if(i == 8){
				monthList.add("九月");
				BigDecimal nine = BigDecimal.ZERO;
				//完成率
				if(year1.getNine().compareTo(BigDecimal.ZERO) == 1){
					nine = year2.getNine().divide(year1.getNine(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(nine);
				allList.add(year1.getNine());
				cpList.add(year2.getNine());

			}else if(i == 9){
				monthList.add("十月");
				BigDecimal ten = BigDecimal.ZERO;
				//完成率
				if(year1.getTen().compareTo(BigDecimal.ZERO) == 1){
					ten = year2.getTen().divide(year1.getTen(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(ten);
				allList.add(year1.getTen());
				cpList.add(year2.getTen());

			}else if(i == 10){
				monthList.add("十一月");
				BigDecimal eleven = BigDecimal.ZERO;
				//完成率
				if(year1.getEleven().compareTo(BigDecimal.ZERO) == 1){
					eleven = year2.getEleven().divide(year1.getEleven(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(eleven);
				allList.add(year1.getEleven());
				cpList.add(year2.getEleven());

			}else if(i == 11){
				monthList.add("十二月");
				BigDecimal twelev = BigDecimal.ZERO;
				//完成率
				if(year1.getTwelve().compareTo(BigDecimal.ZERO) == 1){
					twelev = year2.getTwelve().divide(year1.getTwelve(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
				}
				rateList.add(twelev);
				allList.add(year1.getTwelve());
				cpList.add(year2.getTwelve());

			}

		}
		Map<String,Object> map = new HashMap<>();
		map.put("month",monthList);

		List<AmountPlanToYear> pageList = new ArrayList<>();
		AmountPlanToYear year1 = new AmountPlanToYear();
		year1.setName("目标");
		year1.setType("bar");
		year1.setStack("one");
		year1.setData(allList);
		pageList.add(year1);

		AmountPlanToYear year2 = new AmountPlanToYear();
		year2.setName("完成");
		year2.setType("bar");
		year2.setStack("one");
		year2.setData(cpList);
		pageList.add(year2);

		AmountPlanToYear year3 = new AmountPlanToYear();
		year3.setName("完成率");
		year3.setType("bar");
		year3.setStack("one");
		year3.setData(rateList);
		pageList.add(year3);

		map.put("data",pageList);

		return map;
	}

	/**
	 * 一季度差异
	 * @param param
	 * @return
	 */
	@Override
	public Map<String, Object> fetchPlanAmountDiff(AmountPlanToYear param) {
		List<String> monthList = DateUtils.toDateList(param.getStartMonth(),param.getEndMonth());
		//统计当前年份每个月的付款计划数量
		List<Map<String,Object>> planList = baseMapper.fetchPlanByYear(param);
		//统计每个月完成的数量
		List<Map<String,Object>> completeList = baseMapper.fetchCompletePlanByYear(param);

		List<AmountPlanToYear> yearList = new ArrayList<>();
		//目标
		extracted(planList, yearList,monthList);
		//完成
		extracted1(completeList, yearList,monthList);

		List<BigDecimal> diffList = new ArrayList<>();
		List<BigDecimal> allList = new ArrayList<>();
		List<BigDecimal> cpList = new ArrayList<>();
		for(int i = 0; i < monthList.size(); i++){

			AmountPlanToYear year1 = yearList.get(0);
			AmountPlanToYear year2 = yearList.get(1);

			if(i == 0){

				//差额
				BigDecimal one = year2.getOne().subtract(year1.getOne());
				diffList.add(one.abs());
				allList.add(year1.getOne());
				cpList.add(year2.getOne());

			}else if(i == 1){
				//差额
				BigDecimal two = year2.getTwo().subtract(year1.getTwo());
				diffList.add(two.abs());
				allList.add(year1.getTwo());
				cpList.add(year2.getTwo());

			}else if(i == 2){
				//差额
				BigDecimal three = year2.getThree().subtract(year1.getThree());
				diffList.add(three.abs());
				allList.add(year1.getThree());
				cpList.add(year2.getThree());

			}else if(i == 3){
				//差额
				BigDecimal four = year2.getFour().subtract(year1.getFour());
				diffList.add(four.abs());
				allList.add(year1.getFour());
				cpList.add(year2.getFour());

			}else if(i == 4){
				//差额
				BigDecimal five = year2.getFive().subtract(year1.getFive());
				diffList.add(five.abs());
				allList.add(year1.getFive());
				cpList.add(year2.getFive());

			}else if(i == 5){
				//差额
				BigDecimal six = year2.getSix().subtract(year1.getSix());
				diffList.add(six.abs());
				allList.add(year1.getSix());
				cpList.add(year2.getSix());

			}else if(i == 6){
				//差额
				BigDecimal seven =  year2.getSeven().subtract(year1.getSeven());
				diffList.add(seven.abs());
				allList.add(year1.getSeven());
				cpList.add(year2.getSeven());

			}else if(i == 7){
				//差额
				BigDecimal eight =  year2.getEight().subtract(year1.getEight());
				diffList.add(eight.abs());
				allList.add(year1.getEight());
				cpList.add(year2.getEight());

			}else if(i == 8){
				//差额
				BigDecimal nine =  year2.getNine().subtract(year1.getNine());
				diffList.add(nine.abs());
				allList.add(year1.getNine());
				cpList.add(year2.getNine());

			}else if(i == 9){
				//差额
				BigDecimal ten =  year2.getTen().subtract(year1.getTen());
				diffList.add(ten.abs());
				allList.add(year1.getTen());
				cpList.add(year2.getTen());

			}else if(i == 10){
				//差额
				BigDecimal eleven =  year2.getEleven().subtract(year1.getEleven());
				diffList.add(eleven.abs());
				allList.add(year1.getEleven());
				cpList.add(year2.getEleven());

			}else if(i == 11){
				//差额
				BigDecimal twelve =  year2.getTwelve().subtract(year1.getTwelve());
				diffList.add(twelve.abs());
				allList.add(year1.getTwelve());
				cpList.add(year2.getTwelve());

			}

			else if(i == 12){
				//差额
				BigDecimal ten3 =  year2.getTen3().subtract(year1.getTen3());
				diffList.add(ten3.abs());
				allList.add(year1.getTen3());
				cpList.add(year2.getTen3());
			}
			else if(i == 13){
				//差额
				BigDecimal ten4 =  year2.getTen4().subtract(year1.getTen4());
				diffList.add(ten4.abs());
				allList.add(year1.getTen4());
				cpList.add(year2.getTen4());
			}
			else if(i == 14){
				//差额
				BigDecimal ten5 =  year2.getTen5().subtract(year1.getTen5());
				diffList.add(ten5.abs());
				allList.add(year1.getTen5());
				cpList.add(year2.getTen5());
			}
			else if(i == 15){
				//差额
				BigDecimal ten6 =  year2.getTen6().subtract(year1.getTen6());
				diffList.add(ten6.abs());
				allList.add(year1.getTen6());
				cpList.add(year2.getTen6());
			}
			else if(i == 16){
				//差额
				BigDecimal ten7 =  year2.getTen7().subtract(year1.getTen7());
				diffList.add(ten7.abs());
				allList.add(year1.getTen7());
				cpList.add(year2.getTen7());
			}
			else if(i == 17){
				//差额
				BigDecimal ten8 =  year2.getTen8().subtract(year1.getTen8());
				diffList.add(ten8.abs());
				allList.add(year1.getTen8());
				cpList.add(year2.getTen8());
			}
			else if(i == 18){
				//差额
				BigDecimal ten9 =  year2.getTen9().subtract(year1.getTen9());
				diffList.add(ten9.abs());
				allList.add(year1.getTen9());
				cpList.add(year2.getTen9());
			}
			else if(i == 19){
				//差额
				BigDecimal ten10 =  year2.getTen10().subtract(year1.getTen10());
				diffList.add(ten10.abs());
				allList.add(year1.getTen10());
				cpList.add(year2.getTen10());
			}
			else if(i == 20){
				//差额
				BigDecimal ten11 =  year2.getTen11().subtract(year1.getTen11());
				diffList.add(ten11.abs());
				allList.add(year1.getTen11());
				cpList.add(year2.getTen11());
			}
			else if(i == 21){
				//差额
				BigDecimal ten12 =  year2.getTen12().subtract(year1.getTen12());
				diffList.add(ten12.abs());
				allList.add(year1.getTen12());
				cpList.add(year2.getTen12());
			}
			else if(i == 22){
				//差额
				BigDecimal ten13 =  year2.getTen13().subtract(year1.getTen13());
				diffList.add(ten13.abs());
				allList.add(year1.getTen13());
				cpList.add(year2.getTen13());
			}
			else if(i == 23){
				//差额
				BigDecimal ten14 =  year2.getTen14().subtract(year1.getTen14());
				diffList.add(ten14.abs());
				allList.add(year1.getTen14());
				cpList.add(year2.getTen14());
			}
		}
		Map<String,Object> map = new HashMap<>();
		map.put("month",monthList);

		List<AmountPlanToYear> pageList = new ArrayList<>();
		AmountPlanToYear year1 = new AmountPlanToYear();
		year1.setName("目标");
		year1.setType("bar");
		year1.setStack("one");
		year1.setData(allList);
		pageList.add(year1);

		AmountPlanToYear year2 = new AmountPlanToYear();
		year2.setName("完成");
		year2.setType("bar");
		year2.setStack("two");
		year2.setData(cpList);
		pageList.add(year2);

		AmountPlanToYear year3 = new AmountPlanToYear();
		year3.setName("差额");
		year3.setType("bar");
		year3.setStack("three");
		year3.setData(diffList);
		pageList.add(year3);

		map.put("data",pageList);

		return map;
	}

	/**
	 * 年度项目费用明细表
	 * @param param
	 * @return
	 */
	@Override
	public List<AmountPlanToYear> fetchProjAmountByMonth(AmountPlanToYear param) {
		List<String> monthList = DateUtils.toDateList(param.getStartMonth(),param.getEndMonth());
		//计划付款金额
		List<AmountPlanToYear> planList = baseMapper.fetchPlanProjAmountByMonth(param);
		//付款金额
		List<AmountPlanToYear> payList = baseMapper.fetchProjAmountByMonth(param);

		//获取项目集合
		Set<String> projIds = new HashSet<>();
		for(AmountPlanToYear pp : planList){
			projIds.add(pp.getProjId());
		}
		for(AmountPlanToYear pp : payList){
			projIds.add(pp.getProjId());
		}
		List<AmountPlanToYear> pageList = new ArrayList<>();
		//计划合计
		AmountPlanToYear planTotal = new AmountPlanToYear();
		BigDecimal planAmount = BigDecimal.ZERO;
		planTotal.setTitle("计划金额总计");
		planTotal.setRowSpan(1);
		planTotal.setType("plan");
		planTotal.setProjId(null);
		AmountPlanToYear payTotal = new AmountPlanToYear();
		BigDecimal payAmount = BigDecimal.ZERO;
		payTotal.setTitle("已付金额总计");
		payTotal.setRowSpan(1);
		payTotal.setType("complete");
		payTotal.setProjId(null);

		for(String id : projIds){
			int i = 1;
			//计划
			AmountPlanToYear row = new AmountPlanToYear();
			//完成
			AmountPlanToYear row1 = new AmountPlanToYear();
			//完成率
			AmountPlanToYear row2 = new AmountPlanToYear();
			String title = "";
			BigDecimal total = BigDecimal.ZERO;
			BigDecimal ptotal = BigDecimal.ZERO;
			BigDecimal rtotal = BigDecimal.ZERO;
			for(String m : monthList){
				//计划
				for(AmountPlanToYear pp : planList){
					if(id.equals(pp.getProjId()) && m.equals(pp.getMonth())){
						title = pp.getTitle();
						if(i == 1){
							row.setOne(pp.getPayAmount());
							planTotal.setOne(planTotal.getOne().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 2){
							row.setTwo(pp.getPayAmount());
							planTotal.setTwo(planTotal.getTwo().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 3){
							row.setThree(pp.getPayAmount());
							planTotal.setThree(planTotal.getThree().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 4){
							row.setFour(pp.getPayAmount());
							planTotal.setFour(planTotal.getFour().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 5){
							row.setFive(pp.getPayAmount());
							planTotal.setFive(planTotal.getFive().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 6){
							row.setSix(pp.getPayAmount());
							planTotal.setSix(planTotal.getSix().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 7){
							row.setSeven(pp.getPayAmount());
							planTotal.setSeven(planTotal.getSeven().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 8){
							row.setEight(pp.getPayAmount());
							planTotal.setEight(planTotal.getEight().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 9){
							row.setNine(pp.getPayAmount());
							planTotal.setNine(planTotal.getNine().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 10){
							row.setTen(pp.getPayAmount());
							planTotal.setTen(planTotal.getTen().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 11){
							row.setEleven(pp.getPayAmount());
							planTotal.setEleven(planTotal.getEleven().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 12){
							row.setTwelve(pp.getPayAmount());
							planTotal.setTwelve(planTotal.getTwelve().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}

						else if(i == 13){
							row.setTen3(pp.getPayAmount());
							planTotal.setTen3(planTotal.getTen3().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 14){
							row.setTen4(pp.getPayAmount());
							planTotal.setTen4(planTotal.getTen4().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 15){
							row.setTen5(pp.getPayAmount());
							planTotal.setTen5(planTotal.getTen5().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 16){
							row.setTen6(pp.getPayAmount());
							planTotal.setTen6(planTotal.getTen6().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 17){
							row.setTen7(pp.getPayAmount());
							planTotal.setTen7(planTotal.getTen7().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 18){
							row.setTen8(pp.getPayAmount());
							planTotal.setTen8(planTotal.getTen8().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 19){
							row.setTen9(pp.getPayAmount());
							planTotal.setTen9(planTotal.getTen9().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 20){
							row.setTen10(pp.getPayAmount());
							planTotal.setTen10(planTotal.getTen10().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 21){
							row.setTen11(pp.getPayAmount());
							planTotal.setTen11(planTotal.getTen11().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 22){
							row.setTen12(pp.getPayAmount());
							planTotal.setTen12(planTotal.getTen12().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 23){
							row.setTen13(pp.getPayAmount());
							planTotal.setTen13(planTotal.getTen13().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 24){
							row.setTen14(pp.getPayAmount());
							planTotal.setTen14(planTotal.getTen14().add(pp.getPayAmount()));
							ptotal = ptotal.add(pp.getPayAmount());
						}


					}
				}
				//完成
				for(AmountPlanToYear pa : payList){
					if(id.equals(pa.getProjId()) && m.equals(pa.getMonth())){
						title = pa.getTitle();
						if(i == 1){
							row1.setOne(pa.getPayAmount());
							payTotal.setOne(payTotal.getOne().add(pa.getPayAmount()));
							total = total.add(pa.getPayAmount());
						}else if(i == 2){
							row1.setTwo(pa.getPayAmount());
							payTotal.setTwo(payTotal.getTwo().add(pa.getPayAmount()));
							total = total.add(pa.getPayAmount());
						}else if(i == 3){
							row1.setThree(pa.getPayAmount());
							payTotal.setThree(payTotal.getThree().add(pa.getPayAmount()));
							total = total.add(pa.getPayAmount());
						}else if(i == 4){
							row1.setFour(pa.getPayAmount());
							payTotal.setFour(payTotal.getFour().add(pa.getPayAmount()));
							total = total.add(pa.getPayAmount());
						}else if(i == 5){
							row1.setFive(pa.getPayAmount());
							payTotal.setFive(payTotal.getFive().add(pa.getPayAmount()));
							total = total.add(pa.getPayAmount());
						}else if(i == 6){
							row1.setSix(pa.getPayAmount());
							payTotal.setSix(payTotal.getSix().add(pa.getPayAmount()));
							total = total.add(pa.getPayAmount());
						}else if(i == 7){
							row1.setSeven(pa.getPayAmount());
							payTotal.setSeven(payTotal.getSeven().add(pa.getPayAmount()));
							total = total.add(pa.getPayAmount());
						}else if(i == 8){
							row1.setEight(pa.getPayAmount());
							payTotal.setEight(payTotal.getEight().add(pa.getPayAmount()));
							total = total.add(pa.getPayAmount());
						}else if(i == 9){
							row1.setNine(pa.getPayAmount());
							payTotal.setNine(payTotal.getNine().add(pa.getPayAmount()));
							total = total.add(pa.getPayAmount());
						}else if(i == 10){
							row1.setTen(pa.getPayAmount());
							payTotal.setTen(payTotal.getTen().add(pa.getPayAmount()));
							total = total.add(pa.getPayAmount());
						}else if(i == 11){
							row1.setEleven(pa.getPayAmount());
							payTotal.setEleven(payTotal.getEleven().add(pa.getPayAmount()));
							total = total.add(pa.getPayAmount());
						}else if(i == 12){
							row1.setTwelve(pa.getPayAmount());
							payTotal.setTwelve(payTotal.getTwelve().add(pa.getPayAmount()));
							total = total.add(pa.getPayAmount());
						}

						else if(i == 13){
							row1.setTen3(pa.getPayAmount());
							planTotal.setTen3(planTotal.getTen3().add(pa.getPayAmount()));
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 14){
							row1.setTen4(pa.getPayAmount());
							planTotal.setTen4(planTotal.getTen4().add(pa.getPayAmount()));
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 15){
							row1.setTen5(pa.getPayAmount());
							planTotal.setTen5(planTotal.getTen5().add(pa.getPayAmount()));
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 16){
							row1.setTen6(pa.getPayAmount());
							planTotal.setTen6(planTotal.getTen6().add(pa.getPayAmount()));
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 17){
							row1.setTen7(pa.getPayAmount());
							planTotal.setTen7(planTotal.getTen7().add(pa.getPayAmount()));
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 18){
							row1.setTen8(pa.getPayAmount());
							planTotal.setTen8(planTotal.getTen8().add(pa.getPayAmount()));
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 19){
							row1.setTen9(pa.getPayAmount());
							planTotal.setTen9(planTotal.getTen9().add(pa.getPayAmount()));
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 20){
							row1.setTen10(pa.getPayAmount());
							planTotal.setTen10(planTotal.getTen10().add(pa.getPayAmount()));
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 21){
							row1.setTen11(pa.getPayAmount());
							planTotal.setTen11(planTotal.getTen11().add(pa.getPayAmount()));
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 22){
							row1.setTen12(pa.getPayAmount());
							planTotal.setTen12(planTotal.getTen12().add(pa.getPayAmount()));
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 23){
							row1.setTen13(pa.getPayAmount());
							planTotal.setTen13(planTotal.getTen13().add(pa.getPayAmount()));
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 24){
							row1.setTen14(pa.getPayAmount());
							planTotal.setTen14(planTotal.getTen14().add(pa.getPayAmount()));
							ptotal = ptotal.add(pa.getPayAmount());
						}
					}
				}
				i++;
			}
			row.setRowSpan(3);
			row.setType("plan");
			row.setProjId(id);
			row.setTitle(title);
			row.setTotal(ptotal);
			pageList.add(row);

			planAmount = planAmount.add(ptotal);

			row1.setRowSpan(0);
			row1.setType("complete");
			row1.setProjId(id);
			row1.setTitle(title);
			row1.setTotal(total);
			pageList.add(row1);
			payAmount = payAmount.add(total);

			//完成率
			if(row.getOne() != null && row.getOne().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal one = row1.getOne().divide(row.getOne(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setOne(one);
			}
			if(row.getTwo() != null && row.getTwo().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal two = row1.getTwo().divide(row.getTwo(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTwo(two);
			}
			if(row.getThree() != null && row.getThree().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal three = row1.getThree().divide(row.getThree(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setThree(three);
			}
			if(row.getFour() != null && row.getFour().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal four = row1.getFour().divide(row.getFour(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setFour(four);
			}
			if(row.getFive() != null && row.getFive().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal five = row1.getFive().divide(row.getFive(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setFive(five);
			}
			if(row.getSix() != null && row.getSix().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal six = row1.getSix().divide(row.getSix(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setSix(six);
			}
			if(row.getSeven() != null && row.getSeven().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal seven = row1.getSeven().divide(row.getSeven(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setSeven(seven);
			}
			if(row.getEight() != null && row.getEight().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal eight = row1.getEight().divide(row.getEight(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setEight(eight);
			}
			if(row.getNine() != null && row.getNine().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal nine = row1.getNine().divide(row.getNine(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setNine(nine);
			}
			if(row.getTen() != null && row.getTen().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal ten = row1.getTen().divide(row.getTen(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTen(ten);
			}
			if(row.getEleven() != null && row.getEleven().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal eleven = row1.getEleven().divide(row.getEleven(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setEleven(eleven);
			}
			if(row.getTwelve() != null && row.getTwelve().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal twelve = row1.getTwelve().divide(row.getTwelve(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTwelve(twelve);
			}

			if(row.getTen3() != null && row.getTen3().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal ten3 = row1.getTen3().divide(row.getTen3(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTen3(ten3);
			}
			if(row.getTen4() != null && row.getTen4().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal ten4 = row1.getTen4().divide(row.getTen4(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTen4(ten4);
			}
			if(row.getTen5() != null && row.getTen5().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal ten5 = row1.getTen5().divide(row.getTen5(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTen5(ten5);
			}
			if(row.getTen6() != null && row.getTen6().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal ten6 = row1.getTen6().divide(row.getTen6(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTen6(ten6);
			}
			if(row.getTen7() != null && row.getTen7().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal ten7 = row1.getTen7().divide(row.getTen7(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTen7(ten7);
			}
			if(row.getTen8() != null && row.getTen8().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal ten8 = row1.getTen8().divide(row.getTen8(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTen8(ten8);
			}
			if(row.getTen9() != null && row.getTen9().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal ten9 = row1.getTen9().divide(row.getTen9(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTen9(ten9);
			}
			if(row.getTen10() != null && row.getTen10().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal ten10 = row1.getTen10().divide(row.getTen10(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTen10(ten10);
			}
			if(row.getTen11() != null && row.getTen11().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal ten11 = row1.getTen11().divide(row.getTen11(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTen11(ten11);
			}
			if(row.getTen12() != null && row.getTen12().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal ten12 = row1.getTen12().divide(row.getTen12(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTen12(ten12);
			}
			if(row.getTen13() != null && row.getTen13().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal ten13 = row1.getTen13().divide(row.getTen13(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTen13(ten13);
			}
			if(row.getTen14() != null && row.getTen14().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal ten14 = row1.getTen14().divide(row.getTen14(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTen14(ten14);
			}


			if(row.getTotal() != null && row.getTotal().compareTo(BigDecimal.ZERO) == 1){
				rtotal = row1.getTotal().divide(row.getTotal(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			}
			row2.setRowSpan(0);
			row2.setType("rate");
			row2.setProjId(id);
			row2.setTitle(title);
			row2.setTotal(rtotal);
			pageList.add(row2);

		}
		if(pageList != null && pageList.size() > 0){
			planTotal.setTotal(planAmount);
			pageList.add(planTotal);
			payTotal.setTotal(payAmount);
			pageList.add(payTotal);
		}

		return pageList;
	}
	/**
	 * 年度项目费用明细表 - 子项
	 * @param param
	 * @return
	 */
	@Override
	public List<AmountPlanToYear> fetchProjChildAmountByMonth(AmountPlanToYear param) {

		List<String> monthList = DateUtils.toDateList(param.getStartMonth(),param.getEndMonth());
		Map<String,String> model = new HashMap<>();
		try {
			List<DictModel> ls = iSysDictService.getDictItems("model");
			model = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));
		} catch (Exception ex) {
			log.error("查询数据字典:产品类别出错" + ex.getMessage());
		}

		//项目子项
		List<ProjectBomChild> childList = iProjectBomChildService.list(Wrappers.<ProjectBomChild>query().lambda().
				eq(ProjectBomChild :: getProjectId,param.getProjId()).eq(ProjectBomChild :: getDelFlag,CommonConstant.DEL_FLAG_0));
		//计划付款金额
		List<AmountPlanToYear> planList = baseMapper.fetchPlanProjChildAmountByMonth(param);
		//付款金额
		List<AmountPlanToYear> payList = baseMapper.fetchProjChildAmountByMonth(param);


		List<AmountPlanToYear> pageList = new ArrayList<>();


		for(ProjectBomChild pbc : childList){
			int i = 1;
			//计划
			AmountPlanToYear row = new AmountPlanToYear();
			//完成
			AmountPlanToYear row1 = new AmountPlanToYear();
			//完成率
			AmountPlanToYear row2 = new AmountPlanToYear();
			String title = "";
			BigDecimal total = BigDecimal.ZERO;
			BigDecimal ptotal = BigDecimal.ZERO;
			BigDecimal rtotal = BigDecimal.ZERO;

			for(String m : monthList){
				//计划
				for(AmountPlanToYear pp : planList){
					if(pbc.getModel().equals(pp.getTitle()) && m.equals(pp.getMonth())){
						title = pp.getTitle();
						if(i == 1){
							row.setOne(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 2){
							row.setTwo(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 3){
							row.setThree(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 4){
							row.setFour(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 5){
							row.setFive(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 6){
							row.setSix(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 7){
							row.setSeven(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 8){
							row.setEight(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 9){
							row.setNine(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 10){
							row.setTen(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 11){
							row.setEleven(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}else if(i == 12){
							row.setTwelve(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}

						else if(i == 13){
							row.setTen3(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 14){
							row.setTen4(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 15){
							row.setTen5(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 16){
							row.setTen6(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 17){
							row.setTen7(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 18){
							row.setTen8(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 19){
							row.setTen9(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 20){
							row.setTen10(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 21){
							row.setTen11(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 22){
							row.setTen12(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 23){
							row.setTen13(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}
						else if(i == 24){
							row.setTen14(pp.getPayAmount());
							ptotal = ptotal.add(pp.getPayAmount());
						}

					}
				}
				//完成
				for(AmountPlanToYear pa : payList){
					if(pbc.getModel().equals(pa.getTitle()) && m.equals(pa.getMonth())){
						title = pa.getTitle();
						if(i == 1){
							row1.setOne(pa.getPayAmount());
							total = total.add(pa.getPayAmount());
						}else if(i == 2){
							row1.setTwo(pa.getPayAmount());
							total = total.add(pa.getPayAmount());
						}else if(i == 3){
							row1.setThree(pa.getPayAmount());
							total = total.add(pa.getPayAmount());
						}else if(i == 4){
							row1.setFour(pa.getPayAmount());
							total = total.add(pa.getPayAmount());
						}else if(i == 5){
							row1.setFive(pa.getPayAmount());
							total = total.add(pa.getPayAmount());
						}else if(i == 6){
							row1.setSix(pa.getPayAmount());
							total = total.add(pa.getPayAmount());
						}else if(i == 7){
							row1.setSeven(pa.getPayAmount());
							total = total.add(pa.getPayAmount());
						}else if(i == 8){
							row1.setEight(pa.getPayAmount());
							total = total.add(pa.getPayAmount());
						}else if(i == 9){
							row1.setNine(pa.getPayAmount());
							total = total.add(pa.getPayAmount());
						}else if(i == 10){
							row1.setTen(pa.getPayAmount());
							total = total.add(pa.getPayAmount());
						}else if(i == 11){
							row1.setEleven(pa.getPayAmount());
							total = total.add(pa.getPayAmount());
						}else if(i == 12){
							row1.setTwelve(pa.getPayAmount());
							total = total.add(pa.getPayAmount());
						}

						else if(i == 13){
							row1.setTen3(pa.getPayAmount());
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 14){
							row1.setTen4(pa.getPayAmount());
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 15){
							row1.setTen5(pa.getPayAmount());
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 16){
							row1.setTen6(pa.getPayAmount());
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 17){
							row1.setTen7(pa.getPayAmount());
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 18){
							row1.setTen8(pa.getPayAmount());
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 19){
							row1.setTen9(pa.getPayAmount());
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 20){
							row1.setTen10(pa.getPayAmount());
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 21){
							row1.setTen11(pa.getPayAmount());
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 22){
							row1.setTen12(pa.getPayAmount());
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 23){
							row1.setTen13(pa.getPayAmount());
							ptotal = ptotal.add(pa.getPayAmount());
						}
						else if(i == 24){
							row1.setTen14(pa.getPayAmount());
							ptotal = ptotal.add(pa.getPayAmount());
						}
					}
				}
				i++;
			}

			String name = model.get(pbc.getModel());


			row.setRowSpan(3);
			row.setType("plan");
			row.setProjId(pbc.getModel());
			row.setTitle(name);
			row.setTotal(ptotal);
			pageList.add(row);

			row1.setRowSpan(0);
			row1.setType("complete");
			row1.setProjId(pbc.getModel());
			row1.setTitle(name);
			row1.setTotal(total);
			pageList.add(row1);

			//完成率
			if(row.getOne() != null && row.getOne().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal one = row1.getOne().divide(row.getOne(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setOne(one);
			}
			if(row.getTwo() != null && row.getTwo().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal two = row1.getTwo().divide(row.getTwo(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTwo(two);
			}
			if(row.getThree() != null && row.getThree().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal three = row1.getThree().divide(row.getThree(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setThree(three);
			}
			if(row.getFour() != null && row.getFour().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal four = row1.getFour().divide(row.getFour(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setFour(four);
			}
			if(row.getFive() != null && row.getFive().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal five = row1.getFive().divide(row.getFive(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setFive(five);
			}
			if(row.getSix() != null && row.getSix().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal six = row1.getSix().divide(row.getSix(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setSix(six);
			}
			if(row.getSeven() != null && row.getSeven().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal seven = row1.getSeven().divide(row.getSeven(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setSeven(seven);
			}
			if(row.getEight() != null && row.getEight().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal eight = row1.getEight().divide(row.getEight(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setEight(eight);
			}
			if(row.getNine() != null && row.getNine().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal nine = row1.getNine().divide(row.getNine(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setNine(nine);
			}
			if(row.getTen() != null && row.getTen().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal ten = row1.getTen().divide(row.getTen(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTen(ten);
			}
			if(row.getEleven() != null && row.getEleven().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal eleven = row1.getEleven().divide(row.getEleven(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setEleven(eleven);
			}
			if(row.getTwelve() != null && row.getTwelve().compareTo(BigDecimal.ZERO) == 1){
				BigDecimal twelve = row1.getTwelve().divide(row.getTwelve(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				row2.setTwelve(twelve);
			}
			if(row.getTotal() != null && row.getTotal().compareTo(BigDecimal.ZERO) == 1){
				rtotal = row1.getTotal().divide(row.getTotal(),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			}
			row2.setRowSpan(0);
			row2.setType("rate");
			row2.setProjId(pbc.getModel());
			row2.setTitle(name);
			row2.setTotal(rtotal);
			pageList.add(row2);

		}

		return pageList;
	}

	/**
	 * 资金计划 - 分组统计
	 * @param contractBase
	 * @return
	 */
	@Override
	public List<AmountPlan> fetchAmountByTypePlanPageList(AmountPlan contractBase) {
		List<AmountPlan> planList = new ArrayList<>();
		//币种汇总
		if("currency".equals(contractBase.getType())){
			planList = baseMapper.fetchAmountPlanByCurrency(contractBase);
		}else if("payMethod".equals(contractBase.getType())){
			planList = baseMapper.fetchAmountPlanByPayMethod(contractBase);
		}else if("area".equals(contractBase.getType())){
			planList = baseMapper.fetchAmountPlanByArea(contractBase);
		}
		return planList;
	}

	/**
	 * 付款计划
	 * @param purPayPlan
	 * @return
	 */
	@Override
	public List<PurPayPlan> queryList(PurPayPlan purPayPlan) {
		return baseMapper.queryList(purPayPlan);
	}

	/**
	 * 按照项目类型统计金额
	 * @param purPayPlan
	 * @return
	 */
	@Override
	public List<Map<String,Object>> fetchProjTypeAmountByProjId(PurPayPlan purPayPlan) {
		return baseMapper.fetchProjTypeAmountByProjId(purPayPlan);
	}

	/**
	 * 每个月付款金额
	 * @param contractBase
	 * @return
	 */
	@Override
	public Map<String, Object> fetchPayAmountByMonth(ContractBase contractBase) {
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
		List<PurPayPlan> payList = baseMapper.fetchPayAmountByMonth(contractBase);
		List<BigDecimal> dataList = new ArrayList<>();

		for(String st : monthList){
			Boolean flag = false;
			for(PurPayPlan cb : payList){
				if(st.equals(cb.getStartMonth())){
					dataList.add(cb.getPayAmount());
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
	 * 月份各项目分析图
	 * @param param
	 * @return
	 */
	@Override
	public Map<String, Object> fetchProjAmountBar(AmountPlanToYear param) {
		List<String> monthList = DateUtils.toDateList(param.getStartMonth(),param.getEndMonth());
		//计划付款金额
		List<AmountPlanToYear> planList = baseMapper.fetchPlanProjAmount(param);
		//完成付款金额
		List<AmountPlanToYear> payList = baseMapper.fetchProjAmountBar(param);

		List<BigDecimal> dataList = new ArrayList<>();
		List<BigDecimal> data1List = new ArrayList<>();
		for(String m : monthList){
			dataList.add(BigDecimal.ZERO);
			data1List.add(BigDecimal.ZERO);
		}
		int i = 1;
		for(String m : monthList){
			for(AmountPlanToYear pp : planList){
				if(m.equals(pp.getMonth())){
					data1List.set(i-1,pp.getPayAmount());
				}
			}
			for(AmountPlanToYear pa : payList){
				if(m.equals(pa.getMonth())){
					dataList.set(i-1,pa.getPayAmount());
				}
			}
			i++;
		}

//		dataList.add(entity.getOne());
//		dataList.add(entity.getTwo());
//		dataList.add(entity.getThree());
//		dataList.add(entity.getFour());
//		dataList.add(entity.getFive());
//		dataList.add(entity.getSix());
//		dataList.add(entity.getSeven());
//		dataList.add(entity.getEight());
//		dataList.add(entity.getNine());
//		dataList.add(entity.getTen());
//		dataList.add(entity.getEleven());
//		dataList.add(entity.getTwelve());


		Map<String,Object> map = new HashMap<>();
		map.put("monthList",monthList);
		map.put("dataList",dataList);
		map.put("data1List",data1List);
		return map;
	}

	/**
	 * 全年项目分析图
	 * @param param
	 * @return
	 */
	@Override
	public List<Map<String, Object>> fetchProjAmountRound(AmountPlanToYear param) {
		return baseMapper.fetchProjAmountRound(param);
	}

	/**
	 * 资金计划报表
	 * @param page
	 * @param contractBase
	 * @return
	 */
	@Override
	public IPage<AmountPlan> fetchAmountPlanPageList(Page<AmountPlan> page, AmountPlan contractBase) {
		Map<String,String> payType = new HashMap<>();
		try {
			List<DictModel> ls = iSysDictService.getDictItems("payType");
			payType = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));
		} catch (Exception ex) {
			log.error("查询数据字典:币种出错" + ex.getMessage());
		}
		Map<String,String> payMethod = new HashMap<>();
		try {
			List<DictModel> ls = iSysDictService.getDictItems("payMethod");
			payMethod = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));
		} catch (Exception ex) {
			log.error("查询数据字典:币种出错" + ex.getMessage());
		}
		IPage<AmountPlan> iPage = baseMapper.fetchAmountPlanPageList(page,contractBase);
		List<AmountPlan> apList = iPage.getRecords();
		for(AmountPlan ap : apList){
			if(StringUtils.isNotEmpty(ap.getPayMethod())){
				String[] method = ap.getPayMethod().split(";");
				List<String> names = new ArrayList<>();
				for(String str : method){
					String name = payMethod.get(str);
					names.add(name);
				}
				ap.setPayMethod(String.join(";",names));
			}
			if(StringUtils.isNotEmpty(ap.getPayType())){
				String[] method = ap.getPayType().split(";");
				List<String> names = new ArrayList<>();
				for(String str : method){
					String name = payType.get(str);
					names.add(name);
				}
				ap.setPayType(String.join(";",names));
			}

		}
		return iPage;
	}

	/**
	 * 付款流程节点报表导出
	 * @param request
	 * @param contractBase
	 * @param clazz
	 * @param title
	 * @return
	 */
	@Override
	public ModelAndView exportPayProgressPageList(HttpServletRequest request, PayProgress contractBase, Class<PayProgress> clazz, String title) {
		List<PayProgress> exportList = baseMapper.exportPayProgressPageList(contractBase);
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
		mv.addObject(NormalExcelConstants.EXPORT_FIELDS,request.getParameter("field"));
		mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		return mv;
	}

	/**
	 * 资金计划报表导出
	 * @param request
	 * @param contractBase
	 * @param clazz
	 * @param title
	 * @return
	 */
	@Override
	public ModelAndView exportAmountPlanPageList(HttpServletRequest request, AmountPlan contractBase, Class<AmountPlan> clazz, String title) {
//		List<AmountPlan> exportList = baseMapper.exportAmountPlanPageList(contractBase);
		Map<String,String> payType = new HashMap<>();
		try {
			List<DictModel> ls = iSysDictService.getDictItems("payType");
			payType = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));
		} catch (Exception ex) {
			log.error("查询数据字典:币种出错" + ex.getMessage());
		}
		Map<String,String> payMethod = new HashMap<>();
		try {
			List<DictModel> ls = iSysDictService.getDictItems("payMethod");
			payMethod = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));
		} catch (Exception ex) {
			log.error("查询数据字典:币种出错" + ex.getMessage());
		}

		List<AmountPlan> exportList = baseMapper.exportAmountPlanPageList(contractBase);
		for(AmountPlan ap : exportList){
			if(StringUtils.isNotEmpty(ap.getPayMethod())){
				String[] method = ap.getPayMethod().split(";");
				List<String> names = new ArrayList<>();
				for(String str : method){
					String name = payMethod.get(str);
					names.add(name);
				}
				ap.setPayMethod(String.join("\r\n",names));
			}
			if(StringUtils.isNotEmpty(ap.getPayType())){
				String[] method = ap.getPayType().split(";");
				List<String> names = new ArrayList<>();
				for(String str : method){
					String name = payType.get(str);
					names.add(name);
				}
				ap.setPayType(String.join("\r\n",names));
			}
			if(StringUtils.isNotEmpty(ap.getLeadTime())){
				String leadTime = ap.getLeadTime();
				leadTime = leadTime.replaceAll(";","\r\n");
				ap.setLeadTime(leadTime);
			}
			if(StringUtils.isNotEmpty(ap.getPayRate())){
				String payRate = ap.getPayRate();
				payRate = payRate.replaceAll(";","\r\n");
				ap.setPayRate(payRate);
			}
			if(StringUtils.isNotEmpty(ap.getSort())){
				String sort = ap.getSort();
				sort = sort.replaceAll(";","\r\n");
				ap.setSort(sort);
			}
			if(StringUtils.isNotEmpty(ap.getIsContract())){

				String isContract = ap.getIsContract();
				isContract = isContract.replaceAll("1","是");
				isContract = isContract.replaceAll("0","否");

				isContract = isContract.replaceAll(";","\r\n");
				ap.setIsContract(isContract);
			}
			if(StringUtils.isNotEmpty(ap.getIsCheck())){
				String isCheck = ap.getIsCheck();
				isCheck = isCheck.replaceAll("1","是");
				isCheck = isCheck.replaceAll("0","否");

				isCheck = isCheck.replaceAll(";","\r\n");
				ap.setIsCheck(isCheck);
			}
			if(StringUtils.isNotEmpty(ap.getIsSend())){
				String isSend = ap.getIsSend();
				isSend = isSend.replaceAll("1","是");
				isSend = isSend.replaceAll("0","否");

				isSend = isSend.replaceAll(";","\r\n");
				ap.setIsSend(isSend);
			}
			if(StringUtils.isNotEmpty(ap.getIsReceive())){
				String isReceive = ap.getIsReceive();
				isReceive = isReceive.replaceAll("1","是");
				isReceive = isReceive.replaceAll("0","否");

				isReceive = isReceive.replaceAll(";","\r\n");
				ap.setIsReceive(isReceive);
			}
			if(StringUtils.isNotEmpty(ap.getIsProgress())){
				String isProgress = ap.getIsProgress();
				isProgress = isProgress.replaceAll("1","是");
				isProgress = isProgress.replaceAll("0","否");

				isProgress = isProgress.replaceAll(";","\r\n");
				ap.setIsProgress(isProgress);
			}
			if(StringUtils.isNotEmpty(ap.getIsQa())){
				String isQa = ap.getIsQa();
				isQa = isQa.replaceAll("1","是");
				isQa = isQa.replaceAll("0","否");

				isQa = isQa.replaceAll(";","\r\n");
				ap.setIsQa(isQa);
			}
			if(StringUtils.isNotEmpty(ap.getIsSettle())){
				String isSettle = ap.getIsSettle();
				isSettle = isSettle.replaceAll("1","是");
				isSettle = isSettle.replaceAll("0","否");

				isSettle = isSettle.replaceAll(";","\r\n");
				ap.setIsSettle(isSettle);
			}
			if(StringUtils.isNotEmpty(ap.getIsQaDue())){
				String isQaDue = ap.getIsQaDue();
				isQaDue = isQaDue.replaceAll("1","是");
				isQaDue = isQaDue.replaceAll("0","否");

				isQaDue = isQaDue.replaceAll(";","\r\n");
				ap.setIsQaDue(isQaDue);
			}
			if(StringUtils.isNotEmpty(ap.getIsInvoice())){
				String isInvoice = ap.getIsInvoice();
				isInvoice = isInvoice.replaceAll("1","是");
				isInvoice = isInvoice.replaceAll("0","否");

				isInvoice = isInvoice.replaceAll(";","\r\n");
				ap.setIsInvoice(isInvoice);
			}
		}

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
		mv.addObject(NormalExcelConstants.EXPORT_FIELDS,request.getParameter("field"));
		mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		return mv;
	}

	/**
	 * 目标完成情况数据导出
	 * @param request
	 * @param param
	 * @param clazz
	 * @param title
	 * @return
	 */
	@Override
	public ModelAndView exportPlanAmountByYear(HttpServletRequest request, AmountPlanToYear param, Class<AmountPlanToYear> clazz, String title) {
		List<String> monthList = DateUtils.toDateList(param.getStartMonth(),param.getEndMonth());
		List<AmountPlanToYear> dataList = fetchPlanAmountByYear(param);

		List<Map<String, Object>> exportList = new ArrayList<>();

		for(AmountPlanToYear apy : dataList){
			Map<String,Object> map = new HashMap<>();
			map.put("title",apy.getTitle());
			map.put("one",apy.getOne().stripTrailingZeros().toPlainString());
			map.put("two",apy.getTwo().stripTrailingZeros().toPlainString());
			map.put("three",apy.getThree().stripTrailingZeros().toPlainString());
			map.put("four",apy.getFour().stripTrailingZeros().toPlainString());
			map.put("five",apy.getFive().stripTrailingZeros().toPlainString());
			map.put("six",apy.getSix().stripTrailingZeros().toPlainString());
			map.put("seven",apy.getSeven().stripTrailingZeros().toPlainString());
			map.put("eight",apy.getEight().stripTrailingZeros().toPlainString());
			map.put("nine",apy.getNine().stripTrailingZeros().toPlainString());
			map.put("ten",apy.getTen().stripTrailingZeros().toPlainString());
			map.put("eleven",apy.getEleven().stripTrailingZeros().toPlainString());
			map.put("twelve",apy.getTwelve().stripTrailingZeros().toPlainString());

			map.put("ten3",apy.getTen3().stripTrailingZeros().toPlainString());
			map.put("ten4",apy.getTen4().stripTrailingZeros().toPlainString());
			map.put("ten5",apy.getTen5().stripTrailingZeros().toPlainString());
			map.put("ten6",apy.getTen6().stripTrailingZeros().toPlainString());
			map.put("ten7",apy.getTen7().stripTrailingZeros().toPlainString());
			map.put("ten8",apy.getTen8().stripTrailingZeros().toPlainString());
			map.put("ten9",apy.getTen9().stripTrailingZeros().toPlainString());
			map.put("ten10",apy.getTen10().stripTrailingZeros().toPlainString());
			map.put("ten11",apy.getTen11().stripTrailingZeros().toPlainString());
			map.put("ten12",apy.getTen12().stripTrailingZeros().toPlainString());
			map.put("ten13",apy.getTen13().stripTrailingZeros().toPlainString());
			map.put("ten14",apy.getTen14().stripTrailingZeros().toPlainString());

			map.put("total",apy.getTotal().stripTrailingZeros().toPlainString());
			exportList.add(map);
		}


		ModelAndView mv = new ModelAndView(new JeecgMapExcelView());
		// 导出文件名称
		mv.addObject(MapExcelConstants.FILE_NAME, title);
		// 设置数据
		mv.addObject(MapExcelConstants.MAP_LIST, exportList);
		// 设置 ExportParams
		mv.addObject(MapExcelConstants.PARAMS, new ExportParams(title, "sheet1"));

		// 设置表头样式
		List<ExcelExportEntity> filedsList = new ArrayList<>();
		filedsList.add(new ExcelExportEntity("#", "title",20));
		String[] valueList = new String[]{"one","two","three","four","five","six","seven","eight","nine","ten","eleven","twelve","ten3","ten4","ten5","ten6","ten7","ten8","ten9","ten10","ten11","ten12","ten13","ten14","total"};
		int i = 0;
		for(String month : monthList){
			filedsList.add(new ExcelExportEntity(month, valueList[i],20));
			i++;
		}
		filedsList.add(new ExcelExportEntity("合计", "total",20));


		mv.addObject(MapExcelConstants.ENTITY_LIST, filedsList);

		return mv;
	}

	/**
	 * 年度项目费用明细表导出
	 * @param request
	 * @param param
	 * @param clazz
	 * @param title
	 * @return
	 */
	@Override
	public ModelAndView exportProjAmountByMonth(HttpServletRequest request, AmountPlanToYear param, Class<AmountPlanToYear> clazz, String title) {

		List<String> monthList = DateUtils.toDateList(param.getStartMonth(),param.getEndMonth());
		List<AmountPlanToYear> dataList = fetchProjAmountByMonth(param);

		List<Map<String, Object>> exportList = new ArrayList<>();



		for(AmountPlanToYear apy : dataList){
			Map<String,Object> map = new HashMap<>();
			map.put("title",apy.getTitle() + "-" + ("rate".equals(apy.getType()) ? "完成率" : "complete".equals(apy.getType()) ? "已付金额":"计划金额"));
			map.put("one",apy.getOne().stripTrailingZeros().toPlainString());
			map.put("two",apy.getTwo().stripTrailingZeros().toPlainString());
			map.put("three",apy.getThree().stripTrailingZeros().toPlainString());
			map.put("four",apy.getFour().stripTrailingZeros().toPlainString());
			map.put("five",apy.getFive().stripTrailingZeros().toPlainString());
			map.put("six",apy.getSix().stripTrailingZeros().toPlainString());
			map.put("seven",apy.getSeven().stripTrailingZeros().toPlainString());
			map.put("eight",apy.getEight().stripTrailingZeros().toPlainString());
			map.put("nine",apy.getNine().stripTrailingZeros().toPlainString());
			map.put("ten",apy.getTen().stripTrailingZeros().toPlainString());
			map.put("eleven",apy.getEleven().stripTrailingZeros().toPlainString());
			map.put("twelve",apy.getTwelve().stripTrailingZeros().toPlainString());
			map.put("total",apy.getTotal().stripTrailingZeros().toPlainString());


			map.put("ten3",apy.getTen3().stripTrailingZeros().toPlainString());
			map.put("ten4",apy.getTen4().stripTrailingZeros().toPlainString());
			map.put("ten5",apy.getTen5().stripTrailingZeros().toPlainString());
			map.put("ten6",apy.getTen6().stripTrailingZeros().toPlainString());
			map.put("ten7",apy.getTen7().stripTrailingZeros().toPlainString());
			map.put("ten8",apy.getTen8().stripTrailingZeros().toPlainString());
			map.put("ten9",apy.getTen9().stripTrailingZeros().toPlainString());
			map.put("ten10",apy.getTen10().stripTrailingZeros().toPlainString());
			map.put("ten11",apy.getTen11().stripTrailingZeros().toPlainString());
			map.put("ten12",apy.getTen12().stripTrailingZeros().toPlainString());
			map.put("ten13",apy.getTen13().stripTrailingZeros().toPlainString());
			map.put("ten14",apy.getTen14().stripTrailingZeros().toPlainString());
			exportList.add(map);


		}

		ModelAndView mv = new ModelAndView(new JeecgMapExcelView());
		// 导出文件名称
		mv.addObject(MapExcelConstants.FILE_NAME, title);
		// 设置数据
		mv.addObject(MapExcelConstants.MAP_LIST, exportList);
		// 设置 ExportParams
		mv.addObject(MapExcelConstants.PARAMS, new ExportParams(title, "sheet1"));

		// 设置表头样式
		List<ExcelExportEntity> filedsList = new ArrayList<>();
		filedsList.add(new ExcelExportEntity("内容", "title",35));
		String[] valueList = new String[]{"one","two","three","four","five","six","seven","eight","nine","ten","eleven","twelve","ten3","ten4","ten5","ten6","ten7","ten8","ten9","ten10","ten11","ten12","ten13","ten14","total"};
		int i = 0;
		for(String month : monthList){
			filedsList.add(new ExcelExportEntity(month, valueList[i],20));
			i++;
		}
		filedsList.add(new ExcelExportEntity("合计", "total",20));


		mv.addObject(MapExcelConstants.ENTITY_LIST, filedsList);

		return mv;


	}

	/**
	 * 资金计划报表金额分组汇总
	 * @param contractBase
	 * @return
	 */
	@Override
	public Map<String,BigDecimal> fetchGroupAmountPlanPageList(AmountPlan contractBase) {
		Map<String,BigDecimal> map = new HashMap<>();


		BigDecimal usdAmount = BigDecimal.ZERO;
		BigDecimal jpyAmount = BigDecimal.ZERO;
		BigDecimal eurAmount = BigDecimal.ZERO;
		BigDecimal rmbAmount = BigDecimal.ZERO;
		BigDecimal cdAmount = BigDecimal.ZERO;
		BigDecimal dhAmount = BigDecimal.ZERO;
		List<PurPayApply> planList = baseMapper.fetchGroupAmountPlanPageList(contractBase);
		if(planList != null && planList.size() > 0){
			for(PurPayApply ppa : planList){
				if("RMB".equals(ppa.getCurrency())){
					rmbAmount = rmbAmount.add(ppa.getPayAmount());
				}else if("USD".equals(ppa.getCurrency())){
					usdAmount = usdAmount.add(ppa.getPayAmount());
				}else if("JPY".equals(ppa.getCurrency())){
					jpyAmount = jpyAmount.add(ppa.getPayAmount());
				}else if("EUR".equals(ppa.getCurrency())){
					eurAmount = eurAmount.add(ppa.getPayAmount());
				}
				//电汇
				if("2".equals(ppa.getPayMethod())){
					dhAmount = dhAmount.add(ppa.getPayAmount());
				}
				//承兑
				if("5".equals(ppa.getPayMethod())){
					cdAmount = cdAmount.add(ppa.getPayAmount());
				}
			}
		}
		map.put("usdAmount",usdAmount);
		map.put("jpyAmount",jpyAmount);
		map.put("eurAmount",eurAmount);
		map.put("rmbAmount",rmbAmount);
		map.put("cdAmount",cdAmount);
		map.put("dhAmount",dhAmount);
		return map;
	}



	private void extracted4(List<AmountPlanToYear> yearList, AmountPlanToYear year1, AmountPlanToYear year3) {
		AmountPlanToYear year = new AmountPlanToYear();
		year.setTitle("占比(%)");
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal one = BigDecimal.ZERO;
		if(year1.getOne().compareTo(BigDecimal.ZERO) == 1){
			one = year3.getOne().divide(year1.getOne(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			one = BigDecimal.ZERO;
		}
		year.setOne(one.abs());


		BigDecimal two = BigDecimal.ZERO;
		if(year1.getTwo().compareTo(BigDecimal.ZERO) == 1){
			two = year3.getTwo().divide(year1.getTwo(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			two = BigDecimal.ZERO;
		}
		year.setTwo(two.abs());


		BigDecimal three = BigDecimal.ZERO;
		if(year1.getThree().compareTo(BigDecimal.ZERO) == 1){
			three = year3.getThree().divide(year1.getThree(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			three = BigDecimal.ZERO;
		}
		year.setThree(three.abs());


		BigDecimal four = BigDecimal.ZERO;
		if(year1.getFour().compareTo(BigDecimal.ZERO) == 1){
			four = year3.getFour().divide(year1.getFour(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			four = BigDecimal.ZERO;
		}
		year.setFour(four.abs());


		BigDecimal five = BigDecimal.ZERO;
		if(year1.getFive().compareTo(BigDecimal.ZERO) == 1){
			five = year3.getFive().divide(year1.getFive(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			five = BigDecimal.ZERO;
		}
		year.setFive(five.abs());


		BigDecimal six = BigDecimal.ZERO;
		if(year1.getSix().compareTo(BigDecimal.ZERO) == 1){
			six = year3.getSix().divide(year1.getSix(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			six = BigDecimal.ZERO;
		}
		year.setSix(six.abs());


		BigDecimal seven = BigDecimal.ZERO;
		if(year1.getSeven().compareTo(BigDecimal.ZERO) == 1){
			seven = year3.getSeven().divide(year1.getSeven(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			seven = BigDecimal.ZERO;
		}
		year.setSeven(seven.abs());


		BigDecimal eight = BigDecimal.ZERO;
		if(year1.getEight().compareTo(BigDecimal.ZERO) == 1){
			eight = year3.getEight().divide(year1.getEight(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			eight = BigDecimal.ZERO;
		}
		year.setEight(eight.abs());


		BigDecimal nine = BigDecimal.ZERO;
		if(year1.getNine().compareTo(BigDecimal.ZERO) == 1){
			nine = year3.getNine().divide(year1.getNine(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			nine = BigDecimal.ZERO;
		}
		year.setNine(nine.abs());


		BigDecimal ten = BigDecimal.ZERO;
		if(year1.getTen().compareTo(BigDecimal.ZERO) == 1){
			ten = year3.getTen().divide(year1.getTen(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			ten = BigDecimal.ZERO;
		}
		year.setTen(ten.abs());


		BigDecimal eleven = BigDecimal.ZERO;
		if(year1.getEleven().compareTo(BigDecimal.ZERO) == 1){
			eleven = year3.getEleven().divide(year1.getEleven(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			eleven = BigDecimal.ZERO;
		}
		year.setEleven(eleven.abs());


		BigDecimal twelve = BigDecimal.ZERO;
		if(year1.getTwelve().compareTo(BigDecimal.ZERO) == 1){
			twelve = year3.getTwelve().divide(year1.getTwelve(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			twelve = BigDecimal.ZERO;
		}
		year.setTwelve(twelve.abs());



		BigDecimal ten3 = BigDecimal.ZERO;
		if(year1.getTen3().compareTo(BigDecimal.ZERO) == 1){
			ten3 = year3.getTen3().divide(year1.getTen3(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			ten3 = BigDecimal.ZERO;
		}
		year.setTen3(ten3.abs());

		BigDecimal ten4 = BigDecimal.ZERO;
		if(year1.getTen4().compareTo(BigDecimal.ZERO) == 1){
			ten4 = year3.getTen4().divide(year1.getTen4(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			ten4 = BigDecimal.ZERO;
		}
		year.setTen4(ten4.abs());

		BigDecimal ten5 = BigDecimal.ZERO;
		if(year1.getTen5().compareTo(BigDecimal.ZERO) == 1){
			ten5 = year3.getTen5().divide(year1.getTen5(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			ten5 = BigDecimal.ZERO;
		}
		year.setTen5(ten5.abs());

		BigDecimal ten6 = BigDecimal.ZERO;
		if(year1.getTen6().compareTo(BigDecimal.ZERO) == 1){
			ten6 = year3.getTen6().divide(year1.getTen6(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			ten6 = BigDecimal.ZERO;
		}
		year.setTen6(ten6.abs());

		BigDecimal ten7 = BigDecimal.ZERO;
		if(year1.getTen7().compareTo(BigDecimal.ZERO) == 1){
			ten7 = year3.getTen7().divide(year1.getTen7(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			ten7 = BigDecimal.ZERO;
		}
		year.setTen7(ten7.abs());

		BigDecimal ten8 = BigDecimal.ZERO;
		if(year1.getTen8().compareTo(BigDecimal.ZERO) == 1){
			ten8 = year3.getTen8().divide(year1.getTen8(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			ten8 = BigDecimal.ZERO;
		}
		year.setTen8(ten8.abs());

		BigDecimal ten9 = BigDecimal.ZERO;
		if(year1.getTen9().compareTo(BigDecimal.ZERO) == 1){
			ten9 = year3.getTen9().divide(year1.getTen9(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			ten9 = BigDecimal.ZERO;
		}
		year.setTen9(ten9.abs());

		BigDecimal ten10 = BigDecimal.ZERO;
		if(year1.getTen10().compareTo(BigDecimal.ZERO) == 1){
			ten10 = year3.getTen10().divide(year1.getTen10(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			ten10 = BigDecimal.ZERO;
		}
		year.setTen10(ten10.abs());

		BigDecimal ten11 = BigDecimal.ZERO;
		if(year1.getTen11().compareTo(BigDecimal.ZERO) == 1){
			ten11 = year3.getTen11().divide(year1.getTen11(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			ten11 = BigDecimal.ZERO;
		}
		year.setTen11(ten11.abs());

		BigDecimal ten12 = BigDecimal.ZERO;
		if(year1.getTen12().compareTo(BigDecimal.ZERO) == 1){
			ten12 = year3.getTen12().divide(year1.getTen12(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			ten12 = BigDecimal.ZERO;
		}
		year.setTen12(ten12.abs());

		BigDecimal ten13 = BigDecimal.ZERO;
		if(year1.getTen13().compareTo(BigDecimal.ZERO) == 1){
			ten13 = year3.getTen13().divide(year1.getTen13(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			ten13 = BigDecimal.ZERO;
		}
		year.setTen13(ten13.abs());

		BigDecimal ten14 = BigDecimal.ZERO;
		if(year1.getTen14().compareTo(BigDecimal.ZERO) == 1){
			ten14 = year3.getTen14().divide(year1.getTen14(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			ten14 = BigDecimal.ZERO;
		}
		year.setTen14(ten14.abs());

		if(year1.getTotal().compareTo(BigDecimal.ZERO) == 1){
			total = year3.getTotal().divide(year1.getTotal(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}else{
			total = BigDecimal.ZERO;
		}
		year.setTotal(total.abs());
		yearList.add(year);
	}

	private void extracted3(List<AmountPlanToYear> yearList, AmountPlanToYear year1, AmountPlanToYear year2) {
		AmountPlanToYear year = new AmountPlanToYear();
		year.setTitle("差额(笔数)");
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal one = year2.getOne().subtract(year1.getOne());
		year.setOne(one.abs());
		total = total.add(one.abs());

		BigDecimal two = year2.getTwo().subtract(year1.getTwo());
		year.setTwo(two.abs());
		total = total.add(two.abs());

		BigDecimal three = year2.getThree().subtract(year1.getThree());
		year.setThree(three.abs());
		total = total.add(three.abs());

		BigDecimal four = year2.getFour().subtract(year1.getFour());
		year.setFour(four.abs());
		total = total.add(four.abs());

		BigDecimal five = year2.getFive().subtract(year1.getFive());
		year.setFive(five.abs());
		total = total.add(five.abs());

		BigDecimal six = year2.getSix().subtract(year1.getSix());
		year.setSix(six.abs());
		total = total.add(six.abs());

		BigDecimal seven = year2.getSeven().subtract(year1.getSeven());
		year.setSeven(seven.abs());
		total = total.add(seven.abs());

		BigDecimal eight = year2.getEight().subtract(year1.getEight());
		year.setEight(eight.abs());
		total = total.add(eight.abs());

		BigDecimal nine = year2.getNine().subtract(year1.getNine());
		year.setNine(nine.abs());
		total = total.add(nine.abs());

		BigDecimal ten = year2.getTen().subtract(year1.getTen());
		year.setTen(ten.abs());
		total = total.add(ten.abs());

		BigDecimal eleven = year2.getEleven().subtract(year1.getEleven());
		year.setEleven(eleven.abs());
		total = total.add(eleven.abs());

		BigDecimal twelve = year2.getTwelve().subtract(year1.getTwelve());
		year.setTwelve(twelve.abs());
		total = total.add(twelve.abs());

		BigDecimal ten3 = year2.getTen3().subtract(year1.getTen3());
		year.setTen3(ten3.abs());
		total = total.add(ten3.abs());

		BigDecimal ten4 = year2.getTen4().subtract(year1.getTen4());
		year.setTen4(ten4.abs());
		total = total.add(ten4.abs());

		BigDecimal ten5 = year2.getTen5().subtract(year1.getTen5());
		year.setTen5(ten5.abs());
		total = total.add(ten5.abs());

		BigDecimal ten6 = year2.getTen6().subtract(year1.getTen6());
		year.setTen6(ten6.abs());
		total = total.add(ten6.abs());

		BigDecimal ten7 = year2.getTen7().subtract(year1.getTen7());
		year.setTen7(ten7.abs());
		total = total.add(ten7.abs());

		BigDecimal ten8 = year2.getTen8().subtract(year1.getTen8());
		year.setTen8(ten8.abs());
		total = total.add(ten8.abs());

		BigDecimal ten9 = year2.getTen9().subtract(year1.getTen9());
		year.setTen9(ten9.abs());
		total = total.add(ten9.abs());

		BigDecimal ten10 = year2.getTen10().subtract(year1.getTen10());
		year.setTen10(ten10.abs());
		total = total.add(ten10.abs());

		BigDecimal ten11 = year2.getTen11().subtract(year1.getTen11());
		year.setTen11(ten11.abs());
		total = total.add(ten11.abs());

		BigDecimal ten12 = year2.getTen12().subtract(year1.getTen12());
		year.setTen12(ten12.abs());
		total = total.add(ten12.abs());

		BigDecimal ten13 = year2.getTen13().subtract(year1.getTen13());
		year.setTen13(ten13.abs());
		total = total.add(ten13.abs());

		BigDecimal ten14 = year2.getTen14().subtract(year1.getTen14());
		year.setTen14(ten14.abs());
		total = total.add(ten14.abs());

		year.setTotal(total.abs());
		yearList.add(year);
	}

	private void extracted2(List<AmountPlanToYear> yearList, AmountPlanToYear year1, AmountPlanToYear year2) {
		AmountPlanToYear year = new AmountPlanToYear();
		year.setTitle("完成率(%)");
		BigDecimal one = BigDecimal.ZERO;
		if(year1.getOne().compareTo(BigDecimal.ZERO) == 1){
			one = year2.getOne().divide(year1.getOne(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setOne(one);

		BigDecimal two = BigDecimal.ZERO;
		if(year1.getTwo().compareTo(BigDecimal.ZERO) == 1){
			two = year2.getTwo().divide(year1.getTwo(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setTwo(two);

		BigDecimal three = BigDecimal.ZERO;
		if(year1.getThree().compareTo(BigDecimal.ZERO) == 1){
			three = year2.getThree().divide(year1.getThree(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setThree(three);

		BigDecimal four = BigDecimal.ZERO;
		if(year1.getFour().compareTo(BigDecimal.ZERO) == 1){
			four = year2.getFour().divide(year1.getFour(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setFour(four);

		BigDecimal five = BigDecimal.ZERO;
		if(year1.getFive().compareTo(BigDecimal.ZERO) == 1){
			five = year2.getFive().divide(year1.getFive(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setFive(five);

		BigDecimal six = BigDecimal.ZERO;
		if(year1.getSix().compareTo(BigDecimal.ZERO) == 1){
			six = year2.getSix().divide(year1.getSix(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setSix(six);

		BigDecimal seven = BigDecimal.ZERO;
		if(year1.getSeven().compareTo(BigDecimal.ZERO) == 1){
			seven = year2.getSeven().divide(year1.getSeven(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setSeven(seven);

		BigDecimal eight = BigDecimal.ZERO;
		if(year1.getEight().compareTo(BigDecimal.ZERO) == 1){
			eight = year2.getEight().divide(year1.getEight(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setEight(eight);

		BigDecimal nine = BigDecimal.ZERO;
		if(year1.getNine().compareTo(BigDecimal.ZERO) == 1){
			nine = year2.getNine().divide(year1.getNine(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setNine(nine);

		BigDecimal ten = BigDecimal.ZERO;
		if(year1.getTen().compareTo(BigDecimal.ZERO) == 1){
			ten = year2.getTen().divide(year1.getTen(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setTen(ten);

		BigDecimal eleven = BigDecimal.ZERO;
		if(year1.getEleven().compareTo(BigDecimal.ZERO) == 1){
			eleven = year2.getEleven().divide(year1.getEleven(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setEleven(eleven);

		BigDecimal twelve = BigDecimal.ZERO;
		if(year1.getTwelve().compareTo(BigDecimal.ZERO) == 1){
			twelve = year2.getTwelve().divide(year1.getTwelve(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setTwelve(twelve);

		BigDecimal ten3 = BigDecimal.ZERO;
		if(year1.getTen3().compareTo(BigDecimal.ZERO) == 1){
			ten3 = year2.getTen3().divide(year1.getTen3(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setTen3(ten3);

		BigDecimal ten4 = BigDecimal.ZERO;
		if(year1.getTen4().compareTo(BigDecimal.ZERO) == 1){
			ten4 = year2.getTen4().divide(year1.getTen4(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setTen4(ten4);

		BigDecimal ten5 = BigDecimal.ZERO;
		if(year1.getTen5().compareTo(BigDecimal.ZERO) == 1){
			ten5 = year2.getTen5().divide(year1.getTen5(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setTen5(ten5);

		BigDecimal ten6 = BigDecimal.ZERO;
		if(year1.getTen6().compareTo(BigDecimal.ZERO) == 1){
			ten6 = year2.getTen6().divide(year1.getTen6(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setTen6(ten6);

		BigDecimal ten7 = BigDecimal.ZERO;
		if(year1.getTen7().compareTo(BigDecimal.ZERO) == 1){
			ten7 = year2.getTen7().divide(year1.getTen7(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setTen7(ten7);

		BigDecimal ten8 = BigDecimal.ZERO;
		if(year1.getTen8().compareTo(BigDecimal.ZERO) == 1){
			ten8 = year2.getTen8().divide(year1.getTen8(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setTen8(ten8);

		BigDecimal ten9 = BigDecimal.ZERO;
		if(year1.getTen9().compareTo(BigDecimal.ZERO) == 1){
			ten9 = year2.getTen9().divide(year1.getTen9(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setTen9(ten9);

		BigDecimal ten10 = BigDecimal.ZERO;
		if(year1.getTen10().compareTo(BigDecimal.ZERO) == 1){
			ten10 = year2.getTen10().divide(year1.getTen10(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setTen10(ten10);

		BigDecimal ten11 = BigDecimal.ZERO;
		if(year1.getTen11().compareTo(BigDecimal.ZERO) == 1){
			ten11 = year2.getTen11().divide(year1.getTen11(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(110));
		}
		year.setTen11(ten11);

		BigDecimal ten12 = BigDecimal.ZERO;
		if(year1.getTen12().compareTo(BigDecimal.ZERO) == 1){
			ten12 = year2.getTen12().divide(year1.getTen12(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(120));
		}
		year.setTen12(ten12);

		BigDecimal ten13 = BigDecimal.ZERO;
		if(year1.getTen13().compareTo(BigDecimal.ZERO) == 1){
			ten13 = year2.getTen13().divide(year1.getTen13(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(130));
		}
		year.setTen13(ten13);

		BigDecimal ten14 = BigDecimal.ZERO;
		if(year1.getTen14().compareTo(BigDecimal.ZERO) == 1){
			ten14 = year2.getTen14().divide(year1.getTen14(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(140));
		}
		year.setTen14(ten14);

		BigDecimal total = BigDecimal.ZERO;
		if(year1.getTotal().compareTo(BigDecimal.ZERO) == 1){
			total = year2.getTotal().divide(year1.getTotal(),BigDecimal.ROUND_HALF_UP,2).multiply(new BigDecimal(100));
		}
		year.setTotal(total);
		yearList.add(year);
	}


	private void extracted1(List<Map<String, Object>> completeList, List<AmountPlanToYear> yearList,List<String> monthList) {
		AmountPlanToYear year = new AmountPlanToYear();
		year.setTitle("完成(笔数)");
		year.setOne(BigDecimal.ZERO);
		year.setTwo(BigDecimal.ZERO);
		year.setThree(BigDecimal.ZERO);
		year.setFour(BigDecimal.ZERO);
		year.setFive(BigDecimal.ZERO);
		year.setSix(BigDecimal.ZERO);
		year.setSeven(BigDecimal.ZERO);
		year.setEight(BigDecimal.ZERO);
		year.setNine(BigDecimal.ZERO);
		year.setTen(BigDecimal.ZERO);
		year.setEleven(BigDecimal.ZERO);
		year.setTwelve(BigDecimal.ZERO);
		year.setTotal(BigDecimal.ZERO);

		year.setTen3(BigDecimal.ZERO);
		year.setTen4(BigDecimal.ZERO);
		year.setTen5(BigDecimal.ZERO);
		year.setTen6(BigDecimal.ZERO);
		year.setTen7(BigDecimal.ZERO);
		year.setTen8(BigDecimal.ZERO);
		year.setTen9(BigDecimal.ZERO);
		year.setTen10(BigDecimal.ZERO);
		year.setTen11(BigDecimal.ZERO);
		year.setTen12(BigDecimal.ZERO);
		year.setTen13(BigDecimal.ZERO);
		year.setTen14(BigDecimal.ZERO);
		BigDecimal total = BigDecimal.ZERO;
		for(int i = 0; i < monthList.size(); i++) {
			String m = monthList.get(i);
			for(Map<String,Object> mp : completeList){
				String month = (String) mp.get("month");
				BigDecimal num = new BigDecimal((Long) mp.get("num"));
				if(m.equals(month) && i == 0){
					year.setOne(num);
					total = total.add(num);
				}else if(m.equals(month) && i == 1){
					year.setTwo(num);
					total = total.add(num);
				}else if(m.equals(month) && i == 2){
					year.setThree(num);
					total = total.add(num);
				}else if(m.equals(month) && i == 3){
					year.setFour(num);
					total = total.add(num);
				}else if(m.equals(month) && i == 4){
					year.setFive(num);
					total = total.add(num);
				}else if(m.equals(month) && i == 5){
					year.setSix(num);
					total = total.add(num);
				}else if(m.equals(month) && i == 6){
					year.setSeven(num);
					total = total.add(num);
				}else if(m.equals(month) && i == 7){
					year.setEight(num);
					total = total.add(num);
				}else if(m.equals(month) && i == 8){
					year.setNine(num);
					total = total.add(num);
				}else if(m.equals(month) && i == 9){
					year.setTen(num);
					total = total.add(num);
				}else if(m.equals(month) && i == 10){
					year.setEleven(num);
					total = total.add(num);
				}else if(m.equals(month) && i == 11){
					year.setTwelve(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 12){
					year.setTen3(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 13){
					year.setTen4(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 14){
					year.setTen5(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 15){
					year.setTen6(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 16){
					year.setTen7(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 17){
					year.setTen8(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 18){
					year.setTen9(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 19){
					year.setTen10(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 20){
					year.setTen11(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 21){
					year.setTen12(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 22){
					year.setTen13(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 23){
					year.setTen14(num);
					total = total.add(num);
				}

			}
		}

		year.setTotal(total);
		yearList.add(year);
	}

	private void extracted(List<Map<String, Object>> planList, List<AmountPlanToYear> yearList,List<String> monthList) {
		AmountPlanToYear year = new AmountPlanToYear();
		year.setTitle("目标(笔数)");
		year.setOne(BigDecimal.ZERO);
		year.setTwo(BigDecimal.ZERO);
		year.setThree(BigDecimal.ZERO);
		year.setFour(BigDecimal.ZERO);
		year.setFive(BigDecimal.ZERO);
		year.setSix(BigDecimal.ZERO);
		year.setSeven(BigDecimal.ZERO);
		year.setEight(BigDecimal.ZERO);
		year.setNine(BigDecimal.ZERO);
		year.setTen(BigDecimal.ZERO);
		year.setEleven(BigDecimal.ZERO);
		year.setTwelve(BigDecimal.ZERO);

		year.setTen3(BigDecimal.ZERO);
		year.setTen4(BigDecimal.ZERO);
		year.setTen5(BigDecimal.ZERO);
		year.setTen6(BigDecimal.ZERO);
		year.setTen7(BigDecimal.ZERO);
		year.setTen8(BigDecimal.ZERO);
		year.setTen9(BigDecimal.ZERO);
		year.setTen10(BigDecimal.ZERO);
		year.setTen11(BigDecimal.ZERO);
		year.setTen12(BigDecimal.ZERO);
		year.setTen13(BigDecimal.ZERO);
		year.setTen14(BigDecimal.ZERO);


		year.setTotal(BigDecimal.ZERO);
		BigDecimal total = BigDecimal.ZERO;
		for(int i = 0; i < monthList.size(); i++){
			String m = monthList.get(i);
			for(Map<String,Object> mp : planList){
				String month = (String) mp.get("month");
				BigDecimal num = new BigDecimal((Long) mp.get("num"));
				if(m.equals(month) && i == 0){
					year.setOne(num);
					total = total.add(num);
				}else if(m.equals(month)  && i == 1){
					year.setTwo(num);
					total = total.add(num);
				}else if(m.equals(month)  && i == 2){
					year.setThree(num);
					total = total.add(num);
				}else if(m.equals(month)  && i == 3){
					year.setFour(num);
					total = total.add(num);
				}else if(m.equals(month)  && i == 4){
					year.setFive(num);
					total = total.add(num);
				}else if(m.equals(month)  && i == 5){
					year.setSix(num);
					total = total.add(num);
				}else if(m.equals(month)  && i == 6){
					year.setSeven(num);
					total = total.add(num);
				}else if(m.equals(month)  && i == 7){
					year.setEight(num);
					total = total.add(num);
				}else if(m.equals(month)  && i == 8){
					year.setNine(num);
					total = total.add(num);
				}else if(m.equals(month)  && i == 9){
					year.setTen(num);
					total = total.add(num);
				}else if(m.equals(month)  && i == 10){
					year.setEleven(num);
					total = total.add(num);
				}else if(m.equals(month)  && i == 11){
					year.setTwelve(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 12){
					year.setTen3(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 13){
					year.setTen4(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 14){
					year.setTen5(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 15){
					year.setTen6(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 16){
					year.setTen7(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 17){
					year.setTen8(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 18){
					year.setTen9(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 19){
					year.setTen10(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 20){
					year.setTen11(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 21){
					year.setTen12(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 22){
					year.setTen13(num);
					total = total.add(num);
				}
				else if(m.equals(month)  && i == 23){
					year.setTen14(num);
					total = total.add(num);
				}
			}
		}

		year.setTotal(total);
		yearList.add(year);
	}

	private void setFiled(List<AmountPlanToYear> yearList) {
		for(int i = 0; i < 5; i++){
			AmountPlanToYear year = new AmountPlanToYear();
			if(i == 0){
				year.setTitle("目标");
			}else if(i == 1){
				year.setTitle("完成");
			}else if(i == 2){
				year.setTitle("完成率");
			}else if(i == 3){
				year.setTitle("差额");
			}else if(i == 4){
				year.setTitle("占比");
			}
			year.setOne(BigDecimal.ZERO);
			year.setTwo(BigDecimal.ZERO);
			year.setThree(BigDecimal.ZERO);
			year.setFour(BigDecimal.ZERO);
			year.setFive(BigDecimal.ZERO);
			year.setSix(BigDecimal.ZERO);
			year.setSeven(BigDecimal.ZERO);
			year.setEight(BigDecimal.ZERO);
			year.setNine(BigDecimal.ZERO);
			year.setTen(BigDecimal.ZERO);
			year.setEleven(BigDecimal.ZERO);
			year.setTwelve(BigDecimal.ZERO);
			year.setTotal(BigDecimal.ZERO);
			yearList.add(year);
		}
	}

	/**
	 * 获取指定月份内的所有月份
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static List<String> getMonthBetweenDate(String startTime, String endTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		// 声明保存日期集合
		List<String> list = new ArrayList<String>();
		try {
			// 转化成日期类型
			Date startDate = sdf.parse(startTime);
			Date endDate = sdf.parse(endTime);

			//用Calendar 进行日期比较判断
			Calendar calendar = Calendar.getInstance();
			while (startDate.getTime()<=endDate.getTime()){
				// 把日期添加到集合
				list.add(sdf.format(startDate));
				// 设置日期
				calendar.setTime(startDate);
				//把日期增加一天
				calendar.add(Calendar.MONTH, 1);
				// 获取增加后的日期
				startDate=calendar.getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}



}
