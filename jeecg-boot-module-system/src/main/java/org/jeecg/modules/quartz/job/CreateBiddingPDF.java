package org.jeecg.modules.quartz.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.srm.entity.*;
import org.jeecg.modules.srm.service.*;
import org.jeecg.modules.srm.vo.FixBiddingPage;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 招投标生成pdf
 * 
 * @Author Scott
 */
@Slf4j
public class CreateBiddingPDF implements Job {
	@Autowired
	private IBiddingMainService iBiddingMainService;
	@Autowired
	private ISysDictService iSysDictService;
	@Autowired
	private IBiddingSupplierService biddingSupplierService;
	@Autowired
	private IBiddingRecordService biddingRecordService;
	@Autowired
	private IBiddingBarginRecordService iBiddingBarginRecordService;
	@Autowired
	private ISysDepartService iSysDepartService;
	@Autowired
	private IBiddingProfessionalsService biddingProfessionalsService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Value("${jeecg.path.upload}")
	private String upLoadPath;

	@SneakyThrows
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {



		List<BiddingMain> mainList = iBiddingMainService.list(Wrappers.<BiddingMain>query().lambda().
				eq(BiddingMain :: getBiddingStatus,"3").
				eq(BiddingMain :: getDelFlag, CommonConstant.DEL_FLAG_0).
				and(i -> i.eq(BiddingMain::getIsPdf, CommonConstant.DEL_FLAG_0).or().like(BiddingMain::getOaAttachment, "purchase")));


		Map<String,String> currency = new HashMap<>();
		try {
			List<DictModel> ls = iSysDictService.getDictItems("currency_type");
			currency = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));
		} catch (Exception ex) {
			log.error("查询数据字典:币种出错" + ex.getMessage());
		}

		Map<String,String> biddingType = new HashMap<>();
		try {
			List<DictModel> ls = iSysDictService.getDictItems("bidding_type");
			biddingType = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));
		} catch (Exception ex) {
			log.error("查询数据字典:招标类型出错" + ex.getMessage());
		}

		Map<String,String> openType = new HashMap<>();
		try {
			List<DictModel> ls = iSysDictService.getDictItems("open_bidding_type");
			openType = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));
		} catch (Exception ex) {
			log.error("查询数据字典:招标类型出错" + ex.getMessage());
		}

		List<SysDepart> departList = iSysDepartService.list(Wrappers.<SysDepart>query().lambda().
				eq(SysDepart :: getDelFlag,CommonConstant.DEL_FLAG_0).
				eq(SysDepart :: getParentId,"-1"));
		Map<String,String> subject = new HashMap<>();
		try {
			subject = departList.stream().collect((Collectors.toMap(SysDepart::getId, SysDepart::getDepartName)));
		} catch (Exception ex) {
			log.error("查询数据字典:招标地址出错" + ex.getMessage());
		}

		for(BiddingMain bm : mainList){
			String filePath = upLoadPath + File.separator + "bidding" + File.separator + bm.getBiddingNo();
			if(!new File(filePath).mkdirs()){
				new File(filePath).mkdirs();
			}

			String savePath = filePath + File.separator + bm.getBiddingNo() + ".pdf";
			String oaPath = "bidding" + File.separator + bm.getBiddingNo() + File.separator + bm.getBiddingNo() + ".pdf";
			bm.setOaAttachment(oaPath);
			bm.setIsPdf("1");

			List<BiddingRecord> pageList = biddingSupplierService.fetchFixBiddingList(bm.getId());
			try{
				createPDF(bm,savePath,currency,pageList,biddingType,openType,subject);
				iBiddingMainService.updateBatchById(mainList);
			}catch (Exception e){
				log.error("生成pdf文件出错============"+e.getMessage());
			}
		}
	}

	public void createPDF(BiddingMain bm,String savePath,Map<String,String> currency,List<BiddingRecord> pageList,
						  Map<String,String> biddingType,Map<String,String> openType,Map<String,String> subject) throws Exception{
		Document document = new Document();
		Rectangle pageSize = new Rectangle(PageSize.A4.getHeight(), PageSize.A4.getWidth());
		pageSize.rotate();
		document.setPageSize(pageSize);
//		document.setMargins(70, 70, 1, 10);
		//第二步，创建Writer实例
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
		Paragraph paragraph = new Paragraph("招投标详情", oneFont);
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
		table.setTotalWidth(750);
		// 也可以每列分别设置宽度
		table.setTotalWidth(new float[] { 100,145,100,145,100,145 });
		// 锁住宽度
		table.setLockedWidth(true);
		// 设置表格上面空白宽度
		table.setSpacingBefore(10f);
		// 设置表格下面空白宽度
		table.setSpacingAfter(10f);

		// 构建每个单元格
		PdfPCell cell1 = new PdfPCell(new Paragraph("招标编码",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph(bm.getBiddingNo(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);


		cell1 = new PdfPCell(new Paragraph("招标名称",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph(bm.getBiddingName(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("招标类型",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		String biddingTypeName = "";
		if(StringUtils.isNotEmpty(bm.getBiddingType())){
			biddingTypeName = biddingType.get(bm.getBiddingType());
		}
		cell1 = new PdfPCell(new Paragraph(biddingTypeName,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);


		cell1 = new PdfPCell(new Paragraph("开标方式",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		String openName = "";
		if(StringUtils.isNotEmpty(bm.getOpenBiddingType())){
			openName = openType.get(bm.getOpenBiddingType());
		}
		cell1 = new PdfPCell(new Paragraph(openName,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("开标地址",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		String address = "";
		if(StringUtils.isNotEmpty(bm.getOpenBiddingAddress())){
			address = subject.get(bm.getOpenBiddingAddress());
		}
		cell1 = new PdfPCell(new Paragraph(address,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("投标截止时间",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		String biddingDeadline = "";
		if(bm.getBiddingDeadline() != null){
			biddingDeadline = sdf.format(bm.getBiddingDeadline());
		}
		cell1 = new PdfPCell(new Paragraph(biddingDeadline,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("开标时间",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		String openBidding = "";
		if(bm.getOpenBiddingDate() != null){
			openBidding = sdf.format(bm.getOpenBiddingDate());
		}
		cell1 = new PdfPCell(new Paragraph(openBidding,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);


		cell1 = new PdfPCell(new Paragraph("开标时间",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		String pbBidding = "";
		if(bm.getBiddingStartTime() != null && bm.getBiddingEndTime() != null){
			pbBidding = sdf.format(bm.getBiddingStartTime()) + "--" + sdf.format(bm.getBiddingEndTime());
		}
		cell1 = new PdfPCell(new Paragraph(pbBidding,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("是否显示排名",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		String isSort = "";
		if("1".equals(bm.getIsSort())){
			isSort = "是";
		}else{
			isSort = "否";
		}
		cell1 = new PdfPCell(new Paragraph(isSort,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("是否多次报价",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		String isQuotes = "";
		if("1".equals(bm.getIsQuotes())){
			isQuotes = "是";
		}else{
			isQuotes = "否";
		}
		cell1 = new PdfPCell(new Paragraph(isQuotes,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("投标须知",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);


		cell1 = new PdfPCell(new Paragraph(bm.getComment(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setColspan(5);
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		document.add(table);

		BiddingSupplier biddingSupplier = new BiddingSupplier();
		biddingSupplier.setBiddingId(bm.getId());
		List<BiddingProfessionals> biddingSupplierList = biddingProfessionalsService.fetchBiddingExpertList(biddingSupplier);

		Map<String,Map<String,Object>> supMap = new HashMap<>();
		for(BiddingProfessionals bp : biddingSupplierList){
			List<BiddingSupplier> suppList = bp.getSuppList();
			for(BiddingSupplier bs : suppList){
				if(supMap.containsKey(bs.getSupplierId())){
					Map<String,Object> cMap = supMap.get(bs.getSupplierId());
					BigDecimal score = (BigDecimal) cMap.get("score");
					score = score.add(bs.getItemTotal());
					cMap.put("score",score);
					supMap.put(bs.getSupplierId(),cMap);
				}else{
					Map<String,Object> cMap = new HashMap<>();
					cMap.put("name",bs.getName());
					cMap.put("score",bs.getItemTotal());
					supMap.put(bs.getSupplierId(),cMap);
				}
			}
		}


		//总分
		paragraph = new Paragraph("供应商总分汇总", twoFont);
		paragraph.setLeading(30);
		paragraph.setAlignment(Element.ALIGN_LEFT);//设置对齐方式，这个是居中对齐
		document.add(paragraph);

		table = new PdfPTable(4);
		table.setWidthPercentage(100);
		// 设置表格的宽度
		table.setTotalWidth(750);
		// 也可以每列分别设置宽度
		table.setTotalWidth(new float[] { 150,225,150,225});
		// 锁住宽度
		table.setLockedWidth(true);
		// 设置表格上面空白宽度
		table.setSpacingBefore(10f);
		// 设置表格下面空白宽度
		table.setSpacingAfter(10f);


		for (Map.Entry<String, Map<String,Object>> entry : supMap.entrySet()) {
			Map<String,Object> mp = entry.getValue();

			// 构建每个单元格
			cell1 = new PdfPCell(new Paragraph("供应商",threeFont));
			// 设置背景颜色
			cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(mp.get("name") + "",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			// 构建每个单元格
			cell1 = new PdfPCell(new Paragraph("总分",threeFont));
			// 设置背景颜色
			cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(mp.get("score") + "",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

		}
		document.add(table);

		//评分明细
		paragraph = new Paragraph("评标记录", twoFont);
		paragraph.setLeading(30);
		paragraph.setAlignment(Element.ALIGN_LEFT);//设置对齐方式，这个是居中对齐
		document.add(paragraph);

		for(BiddingProfessionals bp : biddingSupplierList) {

			//第四步，写入内容
			table = new PdfPTable(1);
			table.setWidthPercentage(100);
			// 设置表格的宽度
			table.setTotalWidth(300);
			// 也可以每列分别设置宽度
			table.setTotalWidth(new float[] { 300 });
			// 锁住宽度
			table.setLockedWidth(true);
			// 设置表格上面空白宽度
			table.setSpacingBefore(10f);
			// 设置表格下面空白宽度
			table.setSpacingAfter(10f);

			// 构建每个单元格
			cell1 = new PdfPCell(new Paragraph("评标人员:"+ bp.getProfessionalName(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(-220);
			// 设置高度
			cell1.setFixedHeight(20);
			cell1.disableBorderSide(15);
			table.addCell(cell1);

			document.add(table);

			List<BiddingSupplier> suppList = bp.getSuppList();

			//技术
			if("0".equals(bp.getBiddingEvaluateType())){

				String colums1 = "";
				BigDecimal totalScore = BigDecimal.ZERO;
				List<BiddingProfessionals> templateList = bp.getTemplateList();
				List<String> ids = new ArrayList<>();


				for(BiddingSupplier bs : suppList){

					//供应商
					table = new PdfPTable(1);
					table.setWidthPercentage(100);
					// 设置表格的宽度
					table.setTotalWidth(300);
					// 也可以每列分别设置宽度
					table.setTotalWidth(new float[] { 300 });
					// 锁住宽度
					table.setLockedWidth(true);
					// 设置表格上面空白宽度
					table.setSpacingBefore(0f);
					// 设置表格下面空白宽度
					table.setSpacingAfter(0f);

					// 构建每个单元格
					cell1 = new PdfPCell(new Paragraph("供应商:"+ bs.getName(),threeFont));
					// 设置距左边的距离
					cell1.setPaddingLeft(-180);
					// 设置高度
					cell1.setFixedHeight(20);
					cell1.disableBorderSide(15);
					table.addCell(cell1);

					document.add(table);

					//供应商
					table = new PdfPTable(2);
					table.setWidthPercentage(100);
					// 设置表格的宽度
					table.setTotalWidth(750);
					// 也可以每列分别设置宽度
					table.setTotalWidth(new float[] { 400,350 });
					// 锁住宽度
					table.setLockedWidth(true);
					// 设置表格上面空白宽度
					table.setSpacingBefore(0f);
					// 设置表格下面空白宽度
					table.setSpacingAfter(0f);

					int idx = 0;
					for (BiddingProfessionals tp : templateList) {
						colums1 = "评分项:" + tp.getItemName() + "\n评分标准:" + tp.getItemStandard() + "\n满分:" + tp.getItemScore();
						totalScore = totalScore.add(new BigDecimal(tp.getItemScore()));

						// 构建每个单元格
						cell1 = new PdfPCell(new Paragraph(colums1,threeFont));
						// 设置背景颜色
						cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
						cell1.setFixedHeight(40);
						// 设置距左边的距离
						cell1.setPaddingLeft(10);
						// 设置高度
						table.addCell(cell1);

						String cell = bs.getItemScores().get(idx) + "\n" + bs.getItemTexts().get(idx);
						cell1 = new PdfPCell(new Paragraph(cell,threeFont));
						cell1.setFixedHeight(40);
						// 设置距左边的距离
						cell1.setPaddingLeft(10);
						// 设置高度
						table.addCell(cell1);

						idx++;
					}
					// 构建每个单元格
					cell1 = new PdfPCell(new Paragraph("总分",threeFont));
					// 设置背景颜色
					cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					cell1.setFixedHeight(40);
					// 设置距左边的距离
					cell1.setPaddingLeft(10);
					// 设置高度
					table.addCell(cell1);

					cell1 = new PdfPCell(new Paragraph(bs.getItemTotal().stripTrailingZeros().toPlainString(),threeFont));
					cell1.setFixedHeight(40);
					// 设置距左边的距离
					cell1.setPaddingLeft(10);
					// 设置高度
					table.addCell(cell1);


					document.add(table);
				}

			}
			//商务
			else{
				String colums1 = "";
				BigDecimal totalScore = BigDecimal.ZERO;
				List<BiddingProfessionals> templateList = bp.getTemplateList();
				List<String> ids = new ArrayList<>();


				for(BiddingSupplier bs : suppList){

					//供应商
					table = new PdfPTable(1);
					table.setWidthPercentage(100);
					// 设置表格的宽度
					table.setTotalWidth(300);
					// 也可以每列分别设置宽度
					table.setTotalWidth(new float[] { 300 });
					// 锁住宽度
					table.setLockedWidth(true);
					// 设置表格上面空白宽度
					table.setSpacingBefore(0f);
					// 设置表格下面空白宽度
					table.setSpacingAfter(0f);

					// 构建每个单元格
					cell1 = new PdfPCell(new Paragraph("供应商:"+ bs.getName(),threeFont));
					// 设置距左边的距离
					cell1.setPaddingLeft(-180);
					// 设置高度
					cell1.setFixedHeight(20);
					cell1.disableBorderSide(15);
					table.addCell(cell1);

					document.add(table);

					//供应商
					table = new PdfPTable(2);
					table.setWidthPercentage(100);
					// 设置表格的宽度
					table.setTotalWidth(750);
					// 也可以每列分别设置宽度
					table.setTotalWidth(new float[] { 400,350 });
					// 锁住宽度
					table.setLockedWidth(true);
					// 设置表格上面空白宽度
					table.setSpacingBefore(0f);
					// 设置表格下面空白宽度
					table.setSpacingAfter(0f);

					int idx = 0;
					for (BiddingProfessionals tp : templateList) {
						colums1 = "评分项:" + tp.getItemName() + "\n评分标准:" + tp.getItemStandard() + "\n满分:" + tp.getItemScore();
						totalScore = totalScore.add(new BigDecimal(tp.getItemScore()));

						// 构建每个单元格
						cell1 = new PdfPCell(new Paragraph(colums1,threeFont));
						// 设置背景颜色
						cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
						cell1.setFixedHeight(40);
						// 设置距左边的距离
						cell1.setPaddingLeft(10);
						// 设置高度
						table.addCell(cell1);

						String cell = bs.getItemScores().get(idx) + "\n" + bs.getItemTexts().get(idx);
						cell1 = new PdfPCell(new Paragraph(cell,threeFont));
						cell1.setFixedHeight(40);
						// 设置距左边的距离
						cell1.setPaddingLeft(10);
						// 设置高度
						table.addCell(cell1);

						idx++;
					}
					// 构建每个单元格
					cell1 = new PdfPCell(new Paragraph("总分",threeFont));
					// 设置背景颜色
					cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					cell1.setFixedHeight(40);
					// 设置距左边的距离
					cell1.setPaddingLeft(10);
					// 设置高度
					table.addCell(cell1);

					cell1 = new PdfPCell(new Paragraph(bs.getItemTotal().stripTrailingZeros().toPlainString(),threeFont));
					cell1.setFixedHeight(40);
					// 设置距左边的距离
					cell1.setPaddingLeft(10);
					// 设置高度
					table.addCell(cell1);


					document.add(table);
				}
			}

		}





		paragraph = new Paragraph("报价详情", twoFont);
		paragraph.setLeading(30);
		paragraph.setAlignment(Element.ALIGN_LEFT);//设置对齐方式，这个是居中对齐
		document.add(paragraph);

		for(BiddingRecord record : pageList){
			//第四步，写入内容
			table = new PdfPTable(3);
			table.setWidthPercentage(100);
			// 设置表格的宽度
			table.setTotalWidth(300);
			// 也可以每列分别设置宽度
			table.setTotalWidth(new float[] { 100,100,100 });
			// 锁住宽度
			table.setLockedWidth(true);
			// 设置表格上面空白宽度
			table.setSpacingBefore(10f);
			// 设置表格下面空白宽度
			table.setSpacingAfter(10f);

			// 构建每个单元格
			cell1 = new PdfPCell(new Paragraph("标的:"+ record.getProdCode(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(-220);
			// 设置高度
			cell1.setFixedHeight(20);
			cell1.disableBorderSide(15);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph("标的名称:" + record.getProdName(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(-220);
			// 设置高度
			cell1.setFixedHeight(20);
			cell1.disableBorderSide(15);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph("标的数量:" + record.getQty(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(-220);
			// 设置高度
			cell1.setFixedHeight(20);
			cell1.disableBorderSide(15);
			table.addCell(cell1);
			document.add(table);

			table = new PdfPTable(14);
			table.setWidthPercentage(100);
			// 设置表格的宽度
			table.setTotalWidth(800);
			// 也可以每列分别设置宽度
			table.setTotalWidth(new float[] { 40,70,50,50,50,70,50,50,50,50,50,60,60,40 });
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

			cell1 = new PdfPCell(new Paragraph("供应商名称",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph("单价",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph("总额",threeFont));
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

			cell1 = new PdfPCell(new Paragraph("报价明细行备注",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph("评分",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph("联系人",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph("联系电话",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph("报价时间",threeFont));
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
			List<FixBiddingPage> childList = record.getChildList();
			int index = 1;
			for(FixBiddingPage fbp : childList){
				cell1 = new PdfPCell(new Paragraph(index + "",threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(fbp.getName(),threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(fbp.getPriceTax() == null ? "" : fbp.getPriceTax() + "",threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(fbp.getAmount() == null ? "" : fbp.getAmount() + "",threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				String currencyName = currency.get(fbp.getCurrency());
				cell1 = new PdfPCell(new Paragraph(currencyName == null ? "" : currencyName,threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(fbp.getComment(),threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(fbp.getItemTotal() == null ? "" : fbp.getItemTotal() + "",threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(fbp.getContacter() == null ?  "" : fbp.getContacter() + "",threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(fbp.getContacterTel() == null ? "" : fbp.getContacterTel() + "",threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				String offerTime = "";
				if (fbp.getOfferTime() != null){
					offerTime = sdf.format(fbp.getOfferTime());
				}
				cell1 = new PdfPCell(new Paragraph(offerTime,threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(fbp.getBiddingQty() == null ? "" :fbp.getBiddingQty() + "",threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(fbp.getSupBrandName() == null ? "" : fbp.getSupBrandName(),threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(fbp.getSupSpeType() == null ? "" : fbp.getSupSpeType(),threeFont));
				// 设置距左边的距离
				cell1.setPaddingLeft(10);
				// 设置高度
				cell1.setFixedHeight(20);
				table.addCell(cell1);

				String isRecommend = "";
				if(fbp.getIsRecommend() == 1 || "1".equals(fbp.getIsRecommend())){
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

				index++;
			}
			document.add(table);
		}

		List<BiddingBarginRecord> barginList = iBiddingBarginRecordService.fetchHistoryPrice(bm.getId());

		paragraph = new Paragraph("议价记录", twoFont);
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

		cell1 = new PdfPCell(new Paragraph("供应商名称",threeFont));
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
		for(BiddingBarginRecord bbr : barginList){
			cell1 = new PdfPCell(new Paragraph(idx + "",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(bbr.getSuppName(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(bbr.getProdCode(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(bbr.getProdName(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(bbr.getBgPriceTax() + "",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(bbr.getSuppBgPriceTax() + "",threeFont));
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
