package org.cmoc.modules.srm.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class ExportUtil {
    /**
     * 给 Excel 添加水印
     *
     * @param workbook      XSSFWorkbook
     * @param waterMarkText 水印文字内容
     */
    public static void insertWaterMarkTextToXlsx(XSSFWorkbook workbook, String waterMarkText) throws IOException {
        BufferedImage image = createWatermarkImage(waterMarkText);
        ByteArrayOutputStream imageOs = new ByteArrayOutputStream();
        ImageIO.write(image, "png", imageOs);
        int pictureIdx = workbook.addPicture(imageOs.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG);
        XSSFPictureData pictureData = workbook.getAllPictures().get(pictureIdx);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
            XSSFSheet sheet = workbook.getSheetAt(i);
            PackagePartName ppn = pictureData.getPackagePart().getPartName();
            String relType = XSSFRelation.IMAGES.getRelation();
            PackageRelationship pr = sheet.getPackagePart().addRelationship(ppn, TargetMode.INTERNAL, relType, null);
            sheet.getCTWorksheet().addNewPicture().setId(pr.getId());
        }
    }


    /**
     * 创建水印图片
     *
     * @param waterMark 水印文字
     */
    public static BufferedImage createWatermarkImage(String waterMark) {
        String[] textArray = waterMark.split("\n");
        Font font = new Font("Microsoft YaHei", Font.PLAIN, 28);
        int width = 500;
        int height = 400;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 背景透明 开始
        Graphics2D g = image.createGraphics();
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g.dispose();
        // 背景透明 结束
        g = image.createGraphics();
        g.setColor(new Color(Color.lightGray.getRGB()));// 设定画笔颜色
        g.setFont(font);// 设置画笔字体
        //   g.shear(0.1, -0.26);// 设定倾斜度

//        设置字体平滑
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //文字从中心开始输入，算出文字宽度，左移动一半的宽度，即居中
        FontMetrics fontMetrics = g.getFontMetrics(font);

        // 水印位置
        int x = width / 2;
        int y = height / 2;
        // 设置水印旋转
        g.rotate(Math.toRadians(-40), x, y);
        for (String s : textArray) {
            // 文字宽度
            int textWidth = fontMetrics.stringWidth(s);
            g.drawString(s, x - (textWidth / 2), y);// 画出字符串
            y = y + font.getSize();
        }

        g.dispose();// 释放画笔
        return image;
    }

}
