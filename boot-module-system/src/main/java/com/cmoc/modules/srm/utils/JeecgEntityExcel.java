package com.cmoc.modules.srm.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import com.cmoc.common.system.vo.LoginUser;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.export.ExcelExportServer;
import org.jeecgframework.poi.excel.view.MiniAbstractExcelView;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller("JeecgEntityExcel")
@Slf4j
public class JeecgEntityExcel extends MiniAbstractExcelView {
    public JeecgEntityExcel() {
    }

    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String codedFileName = "临时文件";
        Workbook workbook = null;
        String[] exportFields = null;
        Object exportFieldStr = model.get("exportFields");
        if (exportFieldStr != null && exportFieldStr != "") {
            exportFields = exportFieldStr.toString().split(",");
        }

        if (model.containsKey("mapList")) {
            List<Map<String, Object>> list = (List)model.get("mapList");
            if (list.size() == 0) {
                throw new RuntimeException("MAP_LIST IS NULL");
            }
            ExportParams params = (ExportParams)((Map)list.get(0)).get("params");
            params.setType(ExcelType.XSSF);

            workbook = ExcelExportUtil.exportExcel(params, (Class)((Map)list.get(0)).get("entity"), (Collection)((Map)list.get(0)).get("data"), exportFields);

            for(int i = 1; i < list.size(); ++i) {
                (new ExcelExportServer()).createSheet(workbook, params, (Class)((Map)list.get(i)).get("entity"), (Collection)((Map)list.get(i)).get("data"), exportFields);
            }
        } else {
            ExportParams params = (ExportParams)model.get("params");
            params.setType(ExcelType.XSSF);

            workbook = ExcelExportUtil.exportExcel(params, (Class)model.get("entity"), (Collection)model.get("data"), exportFields);
        }

        if (model.containsKey("fileName")) {
            codedFileName = (String)model.get("fileName");
        }

        if (workbook instanceof HSSFWorkbook) {
            codedFileName = codedFileName + ".xls";
        } else {
            codedFileName = codedFileName + ".xlsx";
        }

        if (this.isIE(request)) {
            codedFileName = URLEncoder.encode(codedFileName, "UTF8");
        } else {
            codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
        }

        try {
            log.info("============水印名称:"+sysUser.getRealname());
            ExportUtil.insertWaterMarkTextToXlsx((XSSFWorkbook)workbook,sysUser.getRealname() + "(" + sysUser.getUsername() + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
    }
}
