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
import org.jeecg.modules.srm.service.IProjBaseService;
import org.jeecg.modules.srm.service.IProjectCategoryService;
import org.jeecg.modules.srm.service.IPurchaseRequestDetailService;
import org.jeecg.modules.srm.service.IPurchaseRequestMainService;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 采购需求生成pdf
 * 
 * @Author Scott
 */
@Slf4j
public class CreateReqPDF implements Job {
	@Autowired
	private IPurchaseRequestMainService iPurchaseRequestMainService;
	@Autowired
	private IProjBaseService iProjBaseService;
	@Autowired
	private ISysUserService iSysUserService;
	@Autowired
	private ISysDepartService iSysDepartService;
	@Autowired
	private IProjectCategoryService iProjectCategoryService;
	@Autowired
	private IPurchaseRequestDetailService iPurchaseRequestDetailService;
	@Autowired
	private ISysDictService iSysDictService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Value("${jeecg.path.upload}")
	private String upLoadPath;

	@SneakyThrows
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		List<PurchaseRequestMain> mainList = iPurchaseRequestMainService.list(Wrappers.<PurchaseRequestMain>query().lambda().
				eq(PurchaseRequestMain :: getReqStatus,"2").
				eq(PurchaseRequestMain :: getDelFlag, CommonConstant.DEL_FLAG_0).
				eq(PurchaseRequestMain :: getIsPdf,CommonConstant.DEL_FLAG_0));

		List<SysDepart> departs = iSysDepartService.list(Wrappers.<SysDepart>query().lambda().eq(SysDepart :: getDelFlag,CommonConstant.DEL_FLAG_0));
		Map<String,SysDepart> depart = departs.stream().collect(Collectors.toMap(SysDepart:: getId, sp->sp));

		Map<String,String> model = new HashMap<>();
		try {
			List<DictModel> ls = iSysDictService.getDictItems("model");
			model = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));
		} catch (Exception ex) {
			log.error("查询数据字典:产品类别出错" + ex.getMessage());
		}

		Map<String,String> unit = new HashMap<>();
		try {
			List<DictModel> ls = iSysDictService.getDictItems("unit");
			unit = ls.stream().collect((Collectors.toMap(DictModel::getValue, DictModel::getText)));
		} catch (Exception ex) {
			log.error("查询数据字典:产品类别出错" + ex.getMessage());
		}
		for(PurchaseRequestMain prm : mainList){
			String filePath = upLoadPath + File.separator + "purchase" + File.separator + prm.getReqCode();
			if(!new File(filePath).mkdirs()){
				new File(filePath).mkdirs();
			}

			PurchaseRequestDetail detailList = iPurchaseRequestDetailService.countInfo(prm.getId());
			if(detailList != null){
				String newFileName = "[采购申请]-"+detailList.getProdName()+"-"+detailList.getOrderAmountTax().stripTrailingZeros().toPlainString()+"元"+".pdf";

				String savePath = filePath + File.separator + newFileName;
				String oaPath = "purchase" + File.separator + prm.getReqCode() + File.separator + newFileName;
				prm.setOaAttachment(oaPath);
				prm.setIsPdf("1");

				try{
					createPDF(prm,savePath,model,unit,depart);
					iPurchaseRequestMainService.updateBatchById(mainList);
				}catch (Exception e){
					log.error(e.getMessage());
				}
			}
		}


	}

	public void createPDF(PurchaseRequestMain prm,String savePath,Map<String,String> model,Map<String,String> unit,Map<String,SysDepart> depart) throws Exception{
		Document document = new Document(PageSize.A4);
		document.setMargins(70, 70, 1, 10);
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
		Paragraph paragraph = new Paragraph("采购需求", oneFont);
		paragraph.setLeading(30);
		paragraph.setAlignment(Element.ALIGN_CENTER);//设置对齐方式，这个是居中对齐
		document.add(paragraph);


		paragraph = new Paragraph("基本信息", twoFont);
		paragraph.setLeading(30);
		paragraph.setFont(new Font());
		paragraph.setAlignment(Element.ALIGN_LEFT);//设置对齐方式，这个是居中对齐
		document.add(paragraph);

		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		// 设置表格的宽度
		table.setTotalWidth(500);
		// 也可以每列分别设置宽度
		table.setTotalWidth(new float[] { 80,100,80,100,80,100 });
		// 锁住宽度
		table.setLockedWidth(true);
		// 设置表格上面空白宽度
		table.setSpacingBefore(10f);
		// 设置表格下面空白宽度
		table.setSpacingAfter(10f);

		// 构建每个单元格
		PdfPCell cell1 = new PdfPCell(new Paragraph("项目编号",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		ProjBase projBase = iProjBaseService.getById(prm.getProjectId());
		cell1 = new PdfPCell(new Paragraph(projBase.getProjCode(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("项目名称",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph(projBase.getProjName(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("立项时间",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		String date = "";
		if(projBase.getProjInitiationDate() != null){
			date = sdf.format(projBase.getProjInitiationDate());
		}
		cell1 = new PdfPCell(new Paragraph(date,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("立项人",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		String realname = "";
		if(StringUtils.isNotEmpty(projBase.getApplyUserId())){
			SysUser sysUser = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,projBase.getApplyUserId()));
			if(sysUser != null){
				realname = sysUser.getRealname();
			}
		}

		cell1 = new PdfPCell(new Paragraph(realname,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("立项部门",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);


		String deptName = "";
		if(StringUtils.isNotEmpty(projBase.getApplyOrgId())){
			if(depart.get(projBase.getApplyOrgId()) != null){
				SysDepart sd = depart.get(projBase.getApplyOrgId());
				deptName = sd.getDepartName();
			}
		}

		cell1 = new PdfPCell(new Paragraph(deptName,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		PurchaseRequestMain param = new PurchaseRequestMain();
		param.setProjectId(projBase.getId());
		PurchaseRequestMain main = iPurchaseRequestMainService.fetchRequestByProjId(param);
		BigDecimal usedAmount = BigDecimal.ZERO;
		BigDecimal remainAmount = BigDecimal.ZERO;
		if(main != null){
			usedAmount = main.getOrderTotalAmountTax();
			remainAmount = projBase.getBudgetAmount().subtract(projBase.getUsedAmount());
		}

		cell1 = new PdfPCell(new Paragraph("预算金额(元)",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph(projBase.getBudgetAmount() + "",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("使用金额(元)",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph(usedAmount + "",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("剩余金额(元)",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph(remainAmount + "",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("采购标题",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph(prm.getReqTitle(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("费用分类",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		ProjectCategory cate = iProjectCategoryService.getById(prm.getCategoryId());
		cell1 = new PdfPCell(new Paragraph(cate.getName(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("申请人",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		realname = "";
		if(StringUtils.isNotEmpty(prm.getApplyUserId())){
			SysUser sysUser = iSysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser :: getUsername,prm.getApplyUserId()));
			realname = sysUser.getRealname();
		}

		cell1 = new PdfPCell(new Paragraph(realname,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("申请人部门",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		deptName = "";
		if(StringUtils.isNotEmpty(prm.getReqOrgId())){
			if(depart.get(prm.getReqOrgId()) != null){
				SysDepart sd = depart.get(prm.getReqOrgId());
				deptName = sd.getDepartName();
			}
		}
		cell1 = new PdfPCell(new Paragraph(deptName,threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("备注",threeFont));
		// 设置背景颜色
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);


		cell1 = new PdfPCell(new Paragraph(prm.getComment(),threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		cell1.setColspan(5);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);



		document.add(table);

		paragraph = new Paragraph("采购明细项", twoFont);
		paragraph.setLeading(30);
		paragraph.setAlignment(Element.ALIGN_LEFT);//设置对齐方式，这个是居中对齐
		document.add(paragraph);


		table = new PdfPTable(10);
		table.setWidthPercentage(100);
		// 设置表格的宽度
		table.setTotalWidth(500);
		// 也可以每列分别设置宽度
		table.setTotalWidth(new float[] { 30,70,70,70,50,50,50,50,50,50});
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

		cell1 = new PdfPCell(new Paragraph("设备标识",threeFont));
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

		cell1 = new PdfPCell(new Paragraph("规格型号",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("设备产能",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("设备类型",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("计量单位",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("采购数量",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("交期",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);

		cell1 = new PdfPCell(new Paragraph("使用部门",threeFont));
		// 设置距左边的距离
		cell1.setPaddingLeft(10);
		// 设置高度
		cell1.setFixedHeight(20);
		table.addCell(cell1);


		List<PurchaseRequestDetail> detailList = iPurchaseRequestDetailService.selectByMainId(prm.getId());
		int index = 1;
		for(PurchaseRequestDetail prd : detailList){
			cell1 = new PdfPCell(new Paragraph(index + "",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(prd.getProdCode(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(prd.getProdName(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(prd.getSpeType(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(prd.getCapacity(),threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			String name = model.get(prd.getModel());
			cell1 = new PdfPCell(new Paragraph(name,threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			name = unit.get(prd.getUnitId());
			cell1 = new PdfPCell(new Paragraph(name,threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			cell1 = new PdfPCell(new Paragraph(prd.getQty() + "",threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			String leadTime = "";
			if (StringUtils.isNotEmpty(prd.getLeadTime())) {
				leadTime = prd.getLeadTime().substring(0,10);
			}
			cell1 = new PdfPCell(new Paragraph( leadTime,threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			deptName = "";
			if(StringUtils.isNotEmpty(prd.getOrgId())){
				if(depart.get(prd.getOrgId()) != null){
					SysDepart sd = depart.get(prd.getOrgId());
					deptName = sd.getDepartName();
				}
			}
			cell1 = new PdfPCell(new Paragraph(deptName,threeFont));
			// 设置距左边的距离
			cell1.setPaddingLeft(10);
			// 设置高度
			cell1.setFixedHeight(20);
			table.addCell(cell1);

			index++;
		}
		document.add(table);



		//第五步，关闭文档
		document.close();


	}
}
