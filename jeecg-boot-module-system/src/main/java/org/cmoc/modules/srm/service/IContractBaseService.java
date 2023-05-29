package org.cmoc.modules.srm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.cmoc.common.api.vo.Result;
import org.cmoc.modules.srm.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cmoc.modules.srm.vo.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 合同基本信息表
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
public interface IContractBaseService extends IService<ContractBase> {

	/**
	 * 添加一对多
	 *
	 * @param contractBase
	 * @param contractObjectList
	 */
	public void saveMain(ContractBase contractBase,List<ContractObject> contractObjectList,List<ContractTerms> contractTermsList,List<ContractPayStep> contractPayStepList) throws Exception;

	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	/**
	 * 草稿
	 * @param contractBase
	 * @param contractObjectList
	 */
	void draft(ContractBase contractBase, List<ContractObject> contractObjectList,List<ContractTerms> contractTermsList,List<ContractPayStep> contractPayStepList) throws Exception;

	/**
	 * 合同列表
	 * @param page
	 * @param contractBase
	 * @return
	 */
    IPage<ContractBase> queryPageList(Page<ContractBase> page, ContractBase contractBase);

	/**
	 * 以项目为单位统计合同总额
	 * @param contractBase
	 * @return
	 */
    ContractBase fetchContractByProjId(ContractBase contractBase);

	/**
	 * 合同清单
	 * @param page
	 * @param contractBase
	 * @return
	 */
	IPage<ContractBase> fetchContractListByProjId(Page<ContractBase> page, ContractBase contractBase);

	/**
	 * 供应商合同信息
	 * @param page
	 * @param contractBase
	 * @return
	 */
    IPage<ContractBase> fetchContractBySupp(Page<ContractBase> page, ContractBase contractBase);

	/**
	 * 项目合同进度报表
	 * @param page
	 * @param contractBase
	 * @return
	 */
    IPage<ContractProgress> fetchContractProgressPageList(Page<ContractProgress> page, ContractProgress contractBase);

	/**
	 * 合同发票管理统计表
	 * @param map
	 * @return
	 */
    Map<String, BigDecimal> fetchAmountTotal(Map<String, String> map);

	/**
	 * 合同发票明细列表
	 * @param page
	 * @param contractBase
	 * @return
	 */
	IPage<ContractToInvoice> fetchContractToInvoice(Page<ContractToInvoice> page, ContractToInvoice contractBase);

	/**
	 * 合同发票明细列表按月汇总
	 * @param contractBase
	 * @return
	 */
	List<ContractToInvoice> fetchContractToInvoiceByMonth(ContractToInvoice contractBase);

	/**
	 * 合同管理及支付明细报表
	 * @param param
	 * @return
	 */
	Map<String, Object> fetchContractAndPay(AmountPlanToYear param);

	/**
	 * 合同管理及支付明细报表
	 * @param page
	 * @param contractBase
	 * @return
	 */
	IPage<ContractAndPay> fetchContractAndPayPageList(Page<ContractAndPay> page, ContractAndPay contractBase);

	/**
	 * 编辑
	 * @param contractBase
	 */
    void updateContract(ContractBase contractBase) throws Exception;

	/**
	 * 项目合同进度报表导出
	 * @param request
	 * @param contractBase
	 * @param clazz
	 * @param title
	 * @return
	 */
    ModelAndView exportContractProgressPageList(HttpServletRequest request, ContractProgress contractBase, Class<ContractProgress> clazz, String title);

	/**
	 * 合同发票明细列表导出
	 * @param request
	 * @param contractBase
	 * @param clazz
	 * @param title
	 * @return
	 */
	ModelAndView exportContractToInvoice(HttpServletRequest request, ContractToInvoice contractBase, Class<ContractToInvoice> clazz, String title);

	/**
	 * 合同管理及支付明细报表导出
	 * @param request
	 * @param contractBase
	 * @param clazz
	 * @param title
	 * @return
	 */
	ModelAndView exportContractAndPayPageList(HttpServletRequest request, ContractAndPay contractBase, Class<ContractAndPay> clazz, String title);

	/**
	 * 查询汇率
	 * @param projectId
	 * @return
	 */
    BigDecimal getExchangeRate(String projectId,String currency) throws Exception;

	/**
	 * 生成子合同
	 * @param contractBase
	 * @param contractObjectList
	 */
	void saveMainChild(ContractBase contractBase, List<ContractObject> contractObjectList);

	/**
	 * 审批进度
	 * @param processVo
	 * @return
	 */
    Result processSpeed(ProcessVo processVo);

	/**
	 * 导出
	 * @param contractBase
	 * @return
	 */
    List<ContractBase> queryExportList(ContractBase contractBase);

	/**
	 * 合同总额
	 * @param contractBase
	 * @return
	 */
    List<Map<String, Object>> fetchContractByProjType(ContractBase contractBase);

	/**
	 * 合同分类统计金额
	 * @param contractBase
	 * @return
	 */
    List<Map<String, Object>> fetchContractAmountByType(ContractBase contractBase);

	/**
	 * 合同数量
	 * @param contractBase
	 * @return
	 */
	Map<String, Object> fetchContractNum(ContractBase contractBase);

	/**
	 * 每个月项目数量
	 * @param contractBase
	 * @return
	 */
    Map<String, Object> fetchContractNumByMonth(ContractBase contractBase);

	/**
	 * 合同台套数
	 * @param contractBase
	 * @return
	 */
	Map<String,Object> fetchQtyNum(ContractBase contractBase);

	/**
	 * 合同金额
	 * @param contractBase
	 * @return
	 */
	Map<String, Object> fetchContractAmountTaxLocal(ContractBase contractBase);

	/**
	 * 已付金额
	 * @param contractBase
	 * @return
	 */
	Map<String, Object> fetchPayAmountTaxLocal(ContractBase contractBase);

	/**
	 * 已开票金额
	 * @param contractBase
	 * @return
	 */
	Map<String, Object> fetchInvoiceAmountTaxLocal(@Param("query") ContractBase contractBase);

	/**
	 * 子项
	 * @param contractBase
	 * @return
	 */
    List<ContractObject> fetchChildList(ContractBase contractBase);

	/**
	 * 子项
	 * @param contractBase
	 * @return
	 */
	List<ContractObject> fetchChildQtyList(ContractBase contractBase);
}
