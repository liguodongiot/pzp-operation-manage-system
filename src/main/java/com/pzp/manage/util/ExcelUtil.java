package com.pzp.manage.util;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.util</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/5/11 17:39 星期五
 */

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelUtil {

    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName,String[] title,String[][] values, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

        // 第一行
        HSSFRow row0 = sheet.createRow(0);
        HSSFCell cell0 = row0.createCell(0);
        cell0.setCellStyle(style);

        //设置单元格内容
        cell0.setCellValue("学员考试成绩一览表");
        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));

        // 第二行
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(1);

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 2);
            for(int j=0;j<values[i].length;j++){
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }


    public static HSSFWorkbook getHSSFWorkbook(String sheetName,String[] title, HSSFWorkbook wb){

        // 创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第一行
        HSSFRow row1 = sheet.createRow(0);
        HSSFCell cell1 = row1.createCell(0);
        cell1.setCellStyle(style);
        // 设置单元格内容
        cell1.setCellValue("影像号");

        HSSFCell cell2 = row1.createCell(1);
        cell2.setCellStyle(style);
        cell2.setCellValue("姓名");

        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));
        sheet.addMergedRegion(new CellRangeAddress(0,1,1,1));

        HSSFCell cell3 = row1.createCell(2);
        cell3.setCellStyle(style);
        cell3.setCellValue("动脉1");
        sheet.addMergedRegion(new CellRangeAddress(0,0,2,3));

        HSSFCell cell5 = row1.createCell(4);
        cell5.setCellStyle(style);
        cell5.setCellValue("动脉2");
        sheet.addMergedRegion(new CellRangeAddress(0,0,4,5));
        HSSFCell cell7 = row1.createCell(6);
        cell7.setCellStyle(style);
        cell7.setCellValue("动脉3");
        sheet.addMergedRegion(new CellRangeAddress(0,0,6,8));


        HSSFRow row2 = sheet.createRow(1);

        HSSFCell cell23 = row2.createCell(2);
        cell23.setCellStyle(style);
        cell23.setCellValue("20180101");

        HSSFCell cell24 = row2.createCell(3);
        cell24.setCellStyle(style);
        cell24.setCellValue("20180102");


        HSSFCell cell25 = row2.createCell(4);
        cell25.setCellStyle(style);
        cell25.setCellValue("20180101");

        HSSFCell cell26 = row2.createCell(5);
        cell26.setCellStyle(style);
        cell26.setCellValue("20180102");

        HSSFCell cell27 = row2.createCell(6);
        cell27.setCellStyle(style);
        cell27.setCellValue("20180101");

        HSSFCell cell28 = row2.createCell(7);
        cell28.setCellStyle(style);
        cell28.setCellValue("20180102");

        HSSFCell cell29 = row2.createCell(8);
        cell29.setCellStyle(style);
        cell29.setCellValue("20180103");

        HSSFRow row3 = sheet.createRow(2);

        HSSFCell cell31 = row3.createCell(0);
        cell31.setCellStyle(style);
        // 设置单元格内容
        cell31.setCellValue("0012");

        HSSFCell cell32 = row3.createCell(1);
        cell32.setCellStyle(style);
        cell32.setCellValue("小马");

        HSSFCell cell33 = row3.createCell(2);
        cell33.setCellStyle(style);
        cell33.setCellValue("有");

        HSSFCell cell34 = row3.createCell(3);
        cell34.setCellStyle(style);
        cell34.setCellValue("无");

        HSSFCell cell35 = row3.createCell(4);
        cell35.setCellStyle(style);
        cell35.setCellValue("有");

        HSSFCell cell36 = row3.createCell(5);
        cell36.setCellStyle(style);
        cell36.setCellValue("有");


        HSSFCell cell37 = row3.createCell(6);
        cell37.setCellStyle(style);
        cell37.setCellValue("有");

        HSSFCell cell38 = row3.createCell(7);
        cell38.setCellStyle(style);
        cell38.setCellValue("有");

        HSSFCell cell39 = row3.createCell(8);
        cell39.setCellStyle(style);
        cell39.setCellValue("无");

        return wb;
    }


}
