package com.cmoc.modules.quartz.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.cmoc.common.constant.CommonConstant;
import com.cmoc.modules.srm.entity.ProjBase;
import com.cmoc.modules.srm.entity.ProjectBomRelation;
import com.cmoc.modules.srm.service.IProjBaseService;
import com.cmoc.modules.srm.service.IProjectBomRelationService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 设备清单生成pdf
 * 
 * @Author Scott
 */
@Slf4j
public class CreateEqpPDF implements Job {
	@Autowired
	private IProjBaseService iProjBaseService;
	@Autowired
	private IProjectBomRelationService iProjectBomRelationService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Value("${jeecg.path.upload}")
	private String upLoadPath;

	@SneakyThrows
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		List<ProjBase> mainList = iProjBaseService.list(Wrappers.<ProjBase>query().lambda().
				eq(ProjBase :: getDelFlag, CommonConstant.DEL_FLAG_0));

		for(ProjBase pb : mainList){

			String filePath = upLoadPath + File.separator + "project" + File.separator + pb.getProjCode();
			if(!new File(filePath).mkdirs()){
				new File(filePath).mkdirs();
			}

			String savePath = filePath + File.separator +"[设备购置预算清单]-"+ pb.getProjName() + ".pdf";
			String oaPath = "project" + File.separator + pb.getProjCode() + File.separator +"[设备购置预算清单]-"+ pb.getProjName() + ".pdf";
			//保存文件位置
			pb.setOaAttachment(oaPath);
			try{
				createPDF(pb,savePath);
				iProjBaseService.updateBatchById(mainList);
			}catch (Exception e){
				log.error(e.getMessage());
			}

		}


	}

	public void createPDF(ProjBase prm,String savePath) throws Exception{
		List<ProjectBomRelation> eqpList = iProjectBomRelationService.fetchEqpList(prm);

		if(eqpList == null || eqpList.size() == 0){
			return;
		}

		Document document = new Document(PageSize.A4);
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

		Paragraph paragraph = new Paragraph("项目设备详情", oneFont);
		paragraph.setLeading(30);
		paragraph.setAlignment(Element.ALIGN_CENTER);//设置对齐方式，这个是居中对齐
		document.add(paragraph);

		//第四步，写入内容
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100);
		// 设置表格的宽度
		table.setTotalWidth(500);
		// 也可以每列分别设置宽度
		table.setTotalWidth(new float[] { 100,100,100,100});
		// 锁住宽度
		table.setLockedWidth(true);
		// 设置表格上面空白宽度
		table.setSpacingBefore(10f);
		// 设置表格下面空白宽度
		table.setSpacingAfter(10f);

		BigDecimal budgetAmount = BigDecimal.ZERO;
		BigDecimal projAmount = BigDecimal.ZERO;
		for(ProjectBomRelation pbr : eqpList){
			budgetAmount = budgetAmount.add(pbr.getBudgetAmount());
			projAmount = projAmount.add(pbr.getProjQty().multiply(pbr.getProjPrice()));
		}

		// 构建每个单元格
		PdfPCell cell1 = new PdfPCell(new Paragraph("立项总计",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph(projAmount.stripTrailingZeros().toPlainString(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		// 构建每个单元格
		cell1 = new PdfPCell(new Paragraph("执行总计",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph(budgetAmount.stripTrailingZeros().toPlainString(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);



		// 构建每个单元格
		cell1 = new PdfPCell(new Paragraph("超立项金额",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		BigDecimal overAmount = budgetAmount.subtract(projAmount);
		if(overAmount.compareTo(BigDecimal.ZERO) == -1){
			overAmount = BigDecimal.ZERO;
		}
		cell1 = new PdfPCell(new Paragraph(overAmount.stripTrailingZeros().toPlainString(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		// 构建每个单元格
		BigDecimal overRate = overAmount.divide(projAmount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
		cell1 = new PdfPCell(new Paragraph("超立项比例",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph(overRate.stripTrailingZeros().toPlainString() + "%",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		document.add(table);


		table = new PdfPTable(12);
		table.setWidthPercentage(100);
		// 设置表格的宽度
		table.setTotalWidth(800);
		// 也可以每列分别设置宽度
		table.setTotalWidth(new float[] { 80,80,40,80,40,40,80,40,80,80,80,80});
		// 锁住宽度
		table.setLockedWidth(true);
		// 设置表格上面空白宽度
		table.setSpacingBefore(10f);
		// 设置表格下面空白宽度
		table.setSpacingAfter(10f);

		// 构建每个单元格
		cell1 = new PdfPCell(new Paragraph("设备编码",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("设备名称",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("产能(万片/月)",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("规格型号",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("单位",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("立项数量",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("立项单价",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("执行数量",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("执行单价",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("立项总额",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("执行总额",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("资金分类",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		for(ProjectBomRelation pbr : eqpList){
			cell1 = new PdfPCell(new Paragraph(pbr.getCode(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(pbr.getName(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(pbr.getCapacity(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(pbr.getSpeType(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(pbr.getUnitName(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(pbr.getProjQty().stripTrailingZeros().toPlainString(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(pbr.getProjPrice().stripTrailingZeros().toPlainString(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(pbr.getQty().stripTrailingZeros().toPlainString(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(pbr.getBudgetPrice().stripTrailingZeros().toPlainString(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			projAmount = pbr.getProjPrice().multiply(pbr.getProjQty());
			cell1 = new PdfPCell(new Paragraph(projAmount.stripTrailingZeros().toPlainString(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(pbr.getBudgetAmount().stripTrailingZeros().toPlainString(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(pbr.getCategoryName(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);
		}
		document.add(table);

		//第五步，关闭文档
		document.close();


	}
}
