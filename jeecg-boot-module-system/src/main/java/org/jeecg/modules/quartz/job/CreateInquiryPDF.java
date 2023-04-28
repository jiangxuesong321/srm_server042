package org.jeecg.modules.quartz.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.srm.entity.*;
import org.jeecg.modules.srm.service.*;
import org.jeecg.modules.srm.vo.FixBiddingPage;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 询报价生成pdf
 * 
 * @Author Scott
 */
@Slf4j
public class CreateInquiryPDF implements Job {
	@Autowired
	private IInquiryListService inquiryListService;
	@Autowired
	private ISysDictService iSysDictService;
	@Autowired
	private ISysUserService iSysUserService;
	@Autowired
	private IInquiryRecordService inquiryRecordService;
	@Autowired
	private IPurchaseRequestMainService iPurchaseRequestMainService;
	@Autowired
	private ISupQuoteService iSupQuoteService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Value("${jeecg.path.upload}")
	private String upLoadPath;

	@SneakyThrows
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {



		List<InquiryList> mainList = inquiryListService.list(Wrappers.<InquiryList>query().lambda().
				eq(InquiryList :: getInquiryStatus,"2").
				eq(InquiryList :: getDelFlag, CommonConstant.DEL_FLAG_0).
				eq(InquiryList :: getIsPdf,CommonConstant.DEL_FLAG_0));


		Map<String,String> currency = new HashMap<>();
		try {
			List<DictModel> ls = iSysDictService.getDictItems("currency_type");
			currency = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));
		} catch (Exception ex) {
			log.error("查询数据字典:币种出错" + ex.getMessage());
		}

		Map<String,String> method = new HashMap<>();
		try {
			List<DictModel> ls = iSysDictService.getDictItems("invitation_method");
			method = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));
		} catch (Exception ex) {
			log.error("查询数据字典:邀请方式出错" + ex.getMessage());
		}

		Map<String,String> trade = new HashMap<>();
		try {
			List<DictModel> ls = iSysDictService.getDictItems("trade_type");
			trade = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));
		} catch (Exception ex) {
			log.error("查询数据字典:贸易方式出错" + ex.getMessage());
		}


		for(InquiryList il : mainList){
			String filePath = upLoadPath + File.separator + "inquiry" + File.separator + il.getInquiryCode();
			if(!new File(filePath).mkdirs()){
				new File(filePath).mkdirs();
			}

			String savePath = filePath + File.separator + il.getInquiryCode() + ".pdf";
			String oaPath = "inquiry" + File.separator + il.getInquiryCode() + File.separator + il.getInquiryCode() + ".pdf";
			//保存文件位置
			il.setIsPdf("1");
			il.setOaAttachment(oaPath);

			try{
				createPDF(il,savePath,currency,method,trade);
				inquiryListService.updateBatchById(mainList);
			}catch (Exception e){
				log.error(e.getMessage());
			}
		}


	}

	public void createPDF(InquiryList il,String savePath,Map<String,String> currency,Map<String,String> method,Map<String,String> trade) throws Exception{
		Document document = new Document();
		Rectangle pageSize = new Rectangle(PageSize.A4.getHeight(), PageSize.A4.getWidth());
		pageSize.rotate();
		document.setPageSize(pageSize);
		PdfWriter.getInstance(document, new FileOutputStream(savePath));
		//创建中文字体
		BaseFont bfchinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		//文字大小
		Font oneFont = new Font(bfchinese, 12, Font.BOLD);
		Font twoFont = new Font(bfchinese, 10, Font.NORMAL);
		Font threeFont = new Font(bfchinese, 8, Font.NORMAL);
		//第三步，打开文档
		document.open();
		//第四步，写入内容
		Paragraph paragraph = new Paragraph("询比价详情", oneFont);
		paragraph.setLeading(30);
		paragraph.setAlignment(Element.ALIGN_CENTER);//设置对齐方式，这个是居中对齐
		document.add(paragraph);

		paragraph = new Paragraph("基本信息", twoFont);
		paragraph.setLeading(30);
		paragraph.setAlignment(Element.ALIGN_LEFT);//设置对齐方式，这个是居中对齐
		document.add(paragraph);


		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		// 设置表格的宽度
		table.setTotalWidth(800);
		// 也可以每列分别设置宽度
		table.setTotalWidth(new float[] { 100,160,100,160,100,160 });
		// 锁住宽度
		table.setLockedWidth(true);
		// 设置表格上面空白宽度
		table.setSpacingBefore(10f);
		// 设置表格下面空白宽度
		table.setSpacingAfter(10f);

		// 构建每个单元格
		PdfPCell cell1 = new PdfPCell(new Paragraph("询价单号",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph(il.getInquiryCode(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		// 构建每个单元格
		cell1 = new PdfPCell(new Paragraph("询报价名称",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph(il.getInquiryName(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		// 构建每个单元格
		cell1 = new PdfPCell(new Paragraph("邀请方式",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		String iMethod = "";
		if(method.get(il.getInvitationMethod()) != null){
			iMethod = method.get(il.getInvitationMethod());
		}
		cell1 = new PdfPCell(new Paragraph(iMethod,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		// 构建每个单元格
		cell1 = new PdfPCell(new Paragraph("采购员",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		SysUser sysUser = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,il.getInquirer()));
		cell1 = new PdfPCell(new Paragraph(sysUser.getRealname(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		// 构建每个单元格
		cell1 = new PdfPCell(new Paragraph("联系电话",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph(il.getInquirerTel(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		// 构建每个单元格
//		cell1 = new PdfPCell(new Paragraph("币种",threeFont));
//		// 设置背景颜色
//		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
//		// 设置距左边的距离
//		cell1.setPaddingLeft(10);
//		// 设置高度
//		cell1.setFixedHeight(20);
//		table.addCell(cell1);
//
//		String currencyName = "";
//		if(currency.get(il.getCurrency()) != null){
//			currencyName = currency.get(il.getCurrency());
//		}
//		cell1 = new PdfPCell(new Paragraph(currencyName,threeFont));
//		// 设置距左边的距离
//		cell1.setPaddingLeft(10);
//		// 设置高度
//		cell1.setFixedHeight(20);
//		table.addCell(cell1);

		// 构建每个单元格
		cell1 = new PdfPCell(new Paragraph("是否单一供应商",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		String isOne = "";
		if("1".equals(il.getIsOne())){
			isOne = "是";
		}else {
			isOne = "否";
		}
		cell1 = new PdfPCell(new Paragraph(isOne,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		// 构建每个单元格
		cell1 = new PdfPCell(new Paragraph("报价截止日期",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		String quotationDeadline = "";
		if(il.getQuotationDeadline() != null){
			quotationDeadline = sdf.format(il.getQuotationDeadline());
		}
		cell1 = new PdfPCell(new Paragraph(quotationDeadline,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

//		// 构建每个单元格
//		cell1 = new PdfPCell(new Paragraph("中标公告",threeFont));
//		// 设置背景颜色
//		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
//		// 设置距左边的距离
//		cell1.setPaddingLeft(10);
//		// 设置高度
//		cell1.setFixedHeight(20);
//		table.addCell(cell1);
//
//		String isNotice = "";
//		if("1".equals(il.getIsNotice())){
//			isNotice = "自动发布";
//		}else{
//			isNotice = "手动发布";
//		}
//		cell1 = new PdfPCell(new Paragraph(isNotice,threeFont));
//		// 设置距左边的距离
//		cell1.setPaddingLeft(10);
//		// 设置高度
//		cell1.setFixedHeight(20);
//		table.addCell(cell1);

		// 构建每个单元格
		cell1 = new PdfPCell(new Paragraph("采购说明",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph(il.getRemark(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		cell1.setColspan(5);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		// 构建每个单元格
		cell1 = new PdfPCell(new Paragraph("投标须知",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph(il.getComment(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		cell1.setColspan(5);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);
		document.add(table);


		paragraph = new Paragraph("报价信息", twoFont);
		paragraph.setLeading(30);
		paragraph.setAlignment(Element.ALIGN_LEFT);//设置对齐方式，这个是居中对齐
		document.add(paragraph);


		table = new PdfPTable(13);
		table.setWidthPercentage(100);
		// 设置表格的宽度
		table.setTotalWidth(800);
		// 也可以每列分别设置宽度
		table.setTotalWidth(new float[] { 40,120,50,100,50,50,50,50,50,50,60,60,50 });
		// 锁住宽度
		table.setLockedWidth(true);
		// 设置表格上面空白宽度
		table.setSpacingBefore(10f);
		// 设置表格下面空白宽度
		table.setSpacingAfter(10f);

		cell1 = new PdfPCell(new Paragraph("序号",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("设备信息",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("采购量(台)",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("供应商",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("币种",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("税率",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("首次报价(含税)",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("二次议价",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("贸易方式",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("中标数量",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("报价品牌",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("报价规格",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("是否中标",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		List<InquiryRecord> inquiryRecordList = inquiryRecordService.queryRecordList(il.getId());
		int index = 1;
		List<String> ids = new ArrayList<>();
		for(InquiryRecord ir : inquiryRecordList){
			ids.add(ir.getId());
			List<InquirySupplier> suppList = ir.getSuppList();
			int rowSpan = suppList.size();

			cell1 = new PdfPCell(new Paragraph(index + "",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置单元格的垂直对齐方式
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
			cell1.setUseAscender(true);
			// 设置高度
			cell1.setRowspan(rowSpan);
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			String eqp = "设备标识:" + ir.getProdCode() + "\n设备名称:" + ir.getProdName();
			PurchaseRequestMain prm = iPurchaseRequestMainService.getById(il.getRequestId());
			if("0".equals(prm.getReqType())){
				eqp = eqp + "\n设备分类:" + ir.getCategoryName() + "\n规格型号:" + ir.getSpeType();
			}
			cell1 = new PdfPCell(new Paragraph(eqp,threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			cell1.setVerticalAlignment(cell1.ALIGN_MIDDLE);//设置单元格的垂直对齐方式
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);//设置单元格的水平对齐方式
			cell1.setRowspan(rowSpan);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(ir.getQty() + "",threeFont));
			// 设置距左边的距离
			cell1.setRowspan(rowSpan);
			cell1.setVerticalAlignment(cell1.ALIGN_MIDDLE);//设置单元格的垂直对齐方式
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);


			for(InquirySupplier is : suppList){
				cell1 = new PdfPCell(new Paragraph(is.getName(),threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				String currencyName = "";
				if(currency.get(is.getCurrency()) != null){
					currencyName = currency.get(is.getCurrency());
				}
				cell1 = new PdfPCell(new Paragraph(currencyName,threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				String taxRate = is.getTaxRate() == null ? "" : is.getTaxRate().stripTrailingZeros().toString();
				if("100".equals(taxRate) || "100.00".equals(taxRate)){
					taxRate = "其他";
				}else{
					taxRate = taxRate + "%";
				}
				cell1 = new PdfPCell(new Paragraph(taxRate,threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(is.getOrderPriceTax() == null ? "" : is.getOrderPriceTax() + "",threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(is.getBgOrderPriceTax() == null ? "" :  is.getBgOrderPriceTax() + "",threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				String tradeName = "";
				if(trade.get(is.getTradeType()) != null){
					tradeName = trade.get(is.getTradeType());
				}
				cell1 = new PdfPCell(new Paragraph(tradeName,threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(is.getInquiryQty() == null ? "" : is.getInquiryQty() + "",threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(is.getBrandName() == null ? "" : is.getBrandName() + "",threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(is.getSpeType() == null ? "" : is.getSpeType() + "",threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				String isRecommend = "";
				if("1".equals(is.getIsRecommend()) || 1 == is.getIsRecommend()){
					isRecommend = "是";
				}else{
					isRecommend = "否";
				}
				cell1 = new PdfPCell(new Paragraph(isRecommend,threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);
			}

			index++;
		}
		document.add(table);
		//议价记录
		List<SupQuote> quoteList = iSupQuoteService.fetchPriceHistoryById(ids);
		paragraph = new Paragraph("议价信息", twoFont);
		paragraph.setLeading(30);
		paragraph.setAlignment(Element.ALIGN_LEFT);//设置对齐方式，这个是居中对齐
		document.add(paragraph);


		table = new PdfPTable(6);
		table.setWidthPercentage(100);
		// 设置表格的宽度
		table.setTotalWidth(800);
		// 也可以每列分别设置宽度
		table.setTotalWidth(new float[] { 100,150,150,150,100,100 });
		// 锁住宽度
		table.setLockedWidth(true);
		// 设置表格上面空白宽度
		table.setSpacingBefore(10f);
		// 设置表格下面空白宽度
		table.setSpacingAfter(10f);

		cell1 = new PdfPCell(new Paragraph("序号",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("供应商",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("设备编码",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("设备名称",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("议价价格",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("反馈价格",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		int idx = 1;
		for(SupQuote sq : quoteList){
			cell1 = new PdfPCell(new Paragraph(idx + "",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(sq.getSuppName(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(sq.getProdCode(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(sq.getProdName(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(sq.getOrderPriceTax() + "",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(sq.getSuppOrderPriceTax() + "",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			idx++;

		}
		document.add(table);
		//第五步，关闭文档
		document.close();
	}


}
