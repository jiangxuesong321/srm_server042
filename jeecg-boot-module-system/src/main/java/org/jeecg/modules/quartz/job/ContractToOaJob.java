package org.jeecg.modules.quartz.job;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.message.handle.impl.EmailSendMsgHandle;
import org.jeecg.modules.srm.entity.*;
import org.jeecg.modules.srm.mapper.InquiryListMapper;
import org.jeecg.modules.srm.service.*;
import org.jeecg.modules.srm.utils.ChildBody;
import org.jeecg.modules.srm.utils.ChildBody2;
import org.jeecg.modules.srm.utils.MsgBody;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 合同推送OA
 * 
 * @Author Scott
 */
@Slf4j
public class ContractToOaJob implements Job {
	@Value("${jeecg.path.upload}")
	private String upLoadPath;

	@Value("${oa.url}")
	private String endpoint;

	@Autowired
	private IContractBaseService iContractBaseService;
	@Autowired
	private ISysDictService iSysDictService;
	@Autowired
	private IApproveSettingService iApproveSettingService;
	@Autowired
	private IBasContractTemplateService iBasContractTemplateService;
	@Autowired
	private IContractTermsService iContractTermsService;
	@Autowired
	private IBiddingMainService iBiddingMainService;
	@Autowired
	private IInquiryListService inquiryListService;
	@Autowired
	private IPurchaseRequestMainService iPurchaseRequestMainService;
	@Autowired
	private IProjBaseService iProjBaseService;
	@Autowired
	private IContractObjectService iContractObjectService;
	@Autowired
	private IBasSupplierService iBasSupplierService;
	@Autowired
	private IBasSupplierQualificationService iBasSupplierQualificationService;
	/**
	 * 若参数变量名修改 QuartzJobController中也需对应修改
	 */
	private String parameter;


	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

    /**
     * 项目立项人员、参与评标的人员、采购人 发送邮件
	 * @param jobExecutionContext
     * @throws JobExecutionException
	 */
	@SneakyThrows
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info(" 开始推送OA接口");
		List<ContractBase> baseList = iContractBaseService.list(Wrappers.<ContractBase>query().lambda().
				eq(ContractBase :: getDelFlag,CommonConstant.DEL_FLAG_0).
				and(i -> i.eq(ContractBase::getContractStatus, "3").or().eq(ContractBase::getContractStatus, "9")));

		//获取OA参数
		Map<String,String> oaParam = new HashMap<>();
		List<DictModel> ls = iSysDictService.getDictItems("oa_param");
		oaParam = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));


		String suffix = oaParam.get("suffix");
		for(ContractBase old : baseList){
			old.setProcessCreateTime(new Date());

			//发起人工号，配置项
			List<ApproveSetting> asList = iApproveSettingService.list(Wrappers.<ApproveSetting>query().lambda().
					eq(ApproveSetting :: getDelFlag,CommonConstant.DEL_FLAG_0).
					eq(ApproveSetting :: getType,"oaContract").
					eq(ApproveSetting :: getCompany,old.getContractFirstPartyId()));
			if(asList == null || asList.size() == 0){
				log.error("合同:" + old.getContractNumber() + "没有配置发起人");
				continue;
			}
			String creatId = asList.get(0).getUsername();
			MsgBody msgBody = new MsgBody();
			//区分合同类型
			msgBody.setHtmc(old.getContractName());
			msgBody.setCreatId(creatId);
			msgBody.setSource("srm");
			//固定资产采购合同
			if("0".equals(old.getOaType())){
				String billid0 = oaParam.get("billid0");
				String workflowid0 = oaParam.get("workflowid0");

				msgBody.setBillid(billid0);
				msgBody.setWorkflowid(workflowid0);
			}
			//施工合同
			else if("1".equals(old.getOaType())){
				String billid1 = oaParam.get("billid1");
				String workflowid1 = oaParam.get("workflowid1");

				msgBody.setBillid(billid1);
				msgBody.setWorkflowid(workflowid1);
			}
			//服务合同
			else if("2".equals(old.getOaType())){
				String billid2 = oaParam.get("billid2");
				String workflowid2 = oaParam.get("workflowid2");

				msgBody.setBillid(billid2);
				msgBody.setWorkflowid(workflowid2);
			}
			//合同主体(中环领先：59；内蒙领先：58；天津领先：74)
			if("1".equals(old.getContractFirstPartyId())){
				String zhlx = oaParam.get("zhlx");

				msgBody.setHt(zhlx);
				msgBody.setWfdw(zhlx);
			}else if("3".equals(old.getContractFirstPartyId())){
				String nmlx = oaParam.get("nmlx");

				msgBody.setHt(nmlx);
				msgBody.setWfdw(nmlx);
			}else if("2".equals(old.getContractFirstPartyId())){
				String tjlx = oaParam.get("tjlx");

				msgBody.setHt(tjlx);
				msgBody.setWfdw(tjlx);
			}else if("4".equals(old.getContractFirstPartyId())){
				String tjlx = oaParam.get("xzlx");

				msgBody.setHt(tjlx);
				msgBody.setWfdw(tjlx);
			}
			msgBody.setHtje(old.getContractAmountTax().divide(new BigDecimal(10000)));
			msgBody.setHtfs("1");
			//合同模板 中 的背景描述
			if(StringUtils.isNotEmpty(old.getTemplateId())){
				BasContractTemplate template = iBasContractTemplateService.getById(old.getTemplateId());
				if(template != null){
					msgBody.setBjms(template.getContractDescription());
					msgBody.setHtnr(template.getContractContent());
				}
			}
			List<ContractTerms> termsList = iContractTermsService.list(Wrappers.<ContractTerms>query().lambda().
					eq(ContractTerms :: getContractId,old.getId()).
					eq(ContractTerms :: getDelFlag,CommonConstant.DEL_FLAG_0));
			if(termsList != null && termsList.size() > 0){
				List<String> names = new ArrayList<>();
				for(ContractTerms ct : termsList){
					names.add(ct.getTermsContent());
				}
				msgBody.setJstk(String.join(",",names));
			}

			//需求文件地址(自动生成)
			BiddingMain bm = iBiddingMainService.getById(old.getRequestId());
			InquiryList il = inquiryListService.getById(old.getRequestId());
			String requestId = null;
			String code = null;
			String reqType = null;
			String file1 = null;
			if(bm != null){
				requestId = bm.getRequestId();
				code = bm.getBiddingNo();
				file1 = bm.getOaAttachment();
				reqType = "bidding";
			}else if(il != null){
				requestId = il.getRequestId();
				code = il.getInquiryCode();
				file1 = il.getOaAttachment();
				reqType = "inquiry";
			}

			PurchaseRequestMain main = iPurchaseRequestMainService.getById(requestId);
			ProjBase projBase = iProjBaseService.getById(old.getProjectId());

			List<Map<String,String>> mapList = new ArrayList<>();
			if(main != null && StringUtils.isNotEmpty(main.getOaAttachment())){
				String file = main.getOaAttachment();

				String[] str = file.split("/");
				String name = str[str.length - 1];

				Map<String,String> map = new HashMap<>();
				map.put("name",name);
				map.put("url",suffix + enCodeUrl(file));
				mapList.add(map);
			}else{
//				throw new Exception("没有生成对应得采购需求文件,请联系管理员");
				log.error("合同:" + old.getContractNumber() + ",没有生成对应得采购需求文件,请联系管理员");
				continue;
			}

			if(projBase != null && StringUtils.isNotEmpty(projBase.getOaAttachment())){
				String eqpUrl = projBase.getOaAttachment();

				String[] str = eqpUrl.split("/");
				String name = str[str.length - 1];

				Map<String,String> map = new HashMap<>();
				map.put("name",name);
				map.put("url",suffix + enCodeUrl(eqpUrl));
				mapList.add(map);
			}else{
//				throw new Exception("没有生成对应得项目购置设备清单文件,请联系管理员");
				log.error("合同:" + old.getContractNumber() + ",没有生成对应得项目购置设备清单文件,请联系管理员");
				continue;
			}


			if(il != null && "1".equals(il.getIsOne())){
				String[] strList = il.getOtherAttachment().split(",");
				for(String url : strList){
					String[] str = url.split("/");
					String name = str[str.length - 1];

					Map<String,String> map1 = new HashMap<>();
					map1.put("name",name);
					map1.put("url",suffix + enCodeUrl(url));
					mapList.add(map1);
				}
			}
			msgBody.setXqsq(mapList);


			//过会文件
			if(StringUtils.isNotEmpty(projBase.getInAttachment())){
				List<Map<String,String>> mapList1 = new ArrayList<>();
				String[] strList = projBase.getInAttachment().split(",");
				for(String url : strList){
					String[] str = url.split("/");
					String name = str[str.length - 1];

					Map<String,String> map1 = new HashMap<>();
					map1.put("name",name);
					map1.put("url",suffix + enCodeUrl(url));
					mapList1.add(map1);
				}
				msgBody.setGhwj(mapList1);
			}

			//其他支撑(营业执照、资质证书)
			BasSupplier bs = iBasSupplierService.getById(old.getContractSecondPartyId());
			List<BasSupplierQualification> qualList = iBasSupplierQualificationService.list(Wrappers.<BasSupplierQualification>query().lambda().
					eq(BasSupplierQualification :: getSupplierId,bs.getId()).
					eq(BasSupplierQualification :: getDelFlag,CommonConstant.DEL_FLAG_0));
			List<Map<String,String>> mapList2 = new ArrayList<>();
			if(StringUtils.isNotEmpty(bs.getSupplierAttachment())){
				String[] strList = bs.getSupplierAttachment().split(",");
				for(String url : strList){
					String[] str = url.split("/");
					String name = str[str.length - 1];
					Map<String,String> map2 = new HashMap<>();
					map2.put("name",name);
					map2.put("url",suffix + enCodeUrl(url));
					mapList2.add(map2);
				}
			}
			if(qualList != null && qualList.size() > 0){
				for(BasSupplierQualification qa : qualList){
					String[] strList = qa.getQualUrl().split(",");
					for(String url : strList){
						String[] str = url.split("/");
						String name = str[str.length - 1];
						Map<String,String> map2 = new HashMap<>();
						map2.put("name",name);
						map2.put("url",suffix + enCodeUrl(url));
						mapList2.add(map2);
					}
				}
			}
			if(mapList2 != null && mapList2.size() > 0){
				msgBody.setQtzc(mapList2);
			}

			//招标比价
			if(StringUtils.isNotEmpty(file1)){
				List<Map<String,String>> mapList3 = new ArrayList<>();

				//生成对应文件
				List<ContractObject> objList = iContractObjectService.list(Wrappers.<ContractObject>query().lambda().
						eq(ContractObject :: getContractId,old.getId()).
						eq(ContractObject :: getDelFlag,CommonConstant.DEL_FLAG_0));
				String name = "";
				//询比价
				if("0".equals(old.getSource())){
					name = "[询价明细]";
				}
				//招投标
				else{
					name = "[招标评分及议价明细]";
				}
				String currency = "";
				if("RMB".equals(old.getContractCurrency())){
					currency = "元";
				}else if("EUR".equals(old.getContractCurrency())){
					currency = "欧元";
				}else if("USD".equals(old.getContractCurrency())){
					currency = "美元";
				}else if("JPY".equals(old.getContractCurrency())){
					currency = "日元";
				}

				List<String> obj = new ArrayList<>();
				for(ContractObject co : objList){
					String eq = co.getQty().stripTrailingZeros().toPlainString() + "台" + co.getProdName();
					obj.add(eq);
				}
				name = name + "-" + String.join("-",obj) + "-" + old.getContractAmountTax().stripTrailingZeros().toPlainString()+ currency + "-" + old.getContractSecondParty() + ".pdf";

				File source = new File(upLoadPath + File.separator +file1);
				if(!source.exists()){
//					throw new Exception("没有生成对应的招投标或询价文件");
					log.error("合同:" + old.getContractNumber() + ",没有生成对应的招投标或询价文件,请联系管理员");
					continue;
				}
				String newFile = upLoadPath + File.separator + reqType  + File.separator + code + File.separator + name;
				String url = suffix  + reqType + File.separator + code + File.separator + name;
				File dest = new File(newFile);
				//判断服务器是否已存在文件,
				if(!dest.exists()){
					Files.copy(source.toPath(), dest.toPath());
				}

				Map<String,String> map3 = new HashMap<>();
				map3.put("name",name);
				map3.put("url",enCodeUrl(url));
				mapList3.add(map3);
				msgBody.setBjbj(mapList3);
			}

			//立项文件
			if(StringUtils.isNotEmpty(projBase.getOutAttachment())){
				List<Map<String,String>> mapList4 = new ArrayList<>();

				String[] strList = projBase.getOutAttachment().split(",");
				for(String url : strList){
					String[] str = url.split("/");
					String name = str[str.length - 1];

					Map<String,String> map4 = new HashMap<>();
					map4.put("name",name);
					map4.put("url",suffix + enCodeUrl(url));
					mapList4.add(map4);

				}

				msgBody.setLxwj(mapList4);
			}


			List<ChildBody> child1s = new ArrayList<>();
			ChildBody child1 = new ChildBody();
			child1.setHtbh(old.getContractNumber());
			if(StringUtils.isNotEmpty(old.getWordAttachment())){
				List<Map<String,String>> mapList5 = new ArrayList<>();

				String[] strList = old.getWordAttachment().split(",");
				for(String url : strList){

					String[] str = url.split("/");
					String name = str[str.length - 1];

					Map<String,String> map5 = new HashMap<>();
					map5.put("name",name);
					map5.put("url",suffix + enCodeUrl(url));
					mapList5.add(map5);

				}

				child1.setHt1(mapList5);
			}

			if(StringUtils.isNotEmpty(old.getSpecificationAttachment())){
				List<Map<String,String>> mapList6 = new ArrayList<>();
				String[] strList = old.getSpecificationAttachment().split(",");
				for(String url : strList){

					String[] str = url.split("/");
					String name = str[str.length - 1];

					Map<String,String> map6 = new HashMap<>();
					map6.put("name",name);
					map6.put("url",suffix + enCodeUrl(url));
					mapList6.add(map6);
				}

				child1.setFj1(mapList6);
			}
			child1.setGzfs("4");
			child1s.add(child1);
			msgBody.setChilds1(child1s);

			List<ChildBody2> child2s = new ArrayList<>();
			ChildBody2 child2 = new ChildBody2();
			child2.setDfdw(old.getContractSecondParty());
			child2.setSzd(old.getContractSecondAddress());
			child2.setDfdwlxfs(old.getContractSecondTelphone());
			child2s.add(child2);
			msgBody.setChilds2(child2s);

			String userStr = JSONObject.toJSONString(msgBody);
			log.info("OA传参数据===========:" + userStr);

			// 创建动态客户端
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			Client client = dcf.createClient(endpoint);
			HTTPConduit conduit = (HTTPConduit) client.getConduit();
			HTTPClientPolicy policy = new HTTPClientPolicy();
			long timeout = 10 * 60 * 1000;//
			policy.setConnectionTimeout(timeout);
			policy.setReceiveTimeout(timeout);
			conduit.setClient(policy);

			Object[] objects = new Object[0];
			try {
				objects = client.invoke("createxmsrm", userStr);
				System.out.println("返回数据:" + objects[0].toString());
				net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(objects[0]);
				String status = jsonObject.getString("status");
				String requestid = jsonObject.getString("requestid");

				if("success".equals(status)){
					old.setContractStatus("8");
					old.setProcessId(requestid);
				}else{
					old.setContractStatus("9");
				}

			} catch (java.lang.Exception e) {
				old.setContractStatus("9");
				log.error("合同:" + old.getContractNumber() + ",发起OA失败");
				log.error(e.getMessage());
			}
			iContractBaseService.updateById(old);
		}
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
}
