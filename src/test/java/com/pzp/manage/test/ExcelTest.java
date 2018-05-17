package com.pzp.manage.test;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/5/15 16:41 星期二
 */
public class ExcelTest {


    @Test
    public void testReadXlsx() throws IOException {
        //创建输入流
        InputStream inputStream = this.getClass().getResourceAsStream("/test.xlsx");
        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        //获取工作表
        XSSFSheet sheet = workbook.getSheetAt(0);

        //获取行,行号作为参数传递给getRow方法,第一行从0开始计算
        XSSFRow row = sheet.getRow(0);
        //获取单元格,row已经确定了行号,列号作为参数传递给getCell,第一列从0开始计算
        XSSFCell cell = row.getCell(1);
        //获取单元格的值,即C1的值(第一行,第二列)
        String cellValue = cell.getStringCellValue();
        System.out.println("第一行第二列的值是："+cellValue);
        workbook.close();
        inputStream.close();

    }


    @Test
    public void testReadXls() throws IOException {
        //创建输入流
        InputStream inputStream = this.getClass().getResourceAsStream("/test.xls");

        //通过构造函数传参
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        //获取工作表
        HSSFSheet sheet = workbook.getSheetAt(0);
        //获取行,行号作为参数传递给getRow方法,第一行从0开始计算
        HSSFRow row = sheet.getRow(0);
        //获取单元格,row已经确定了行号,列号作为参数传递给getCell,第一列从0开始计算
        HSSFCell cell = row.getCell(2);
        //获取单元格的值,即C1的值(第一行,第三列)
        String cellValue = cell.getStringCellValue();
        System.out.println("第一行第三列的值是："+cellValue);
        workbook.close();
        inputStream.close();

    }



    @Test
    public void testReadXlsAndXlsx() throws IOException {
        String name = "/test.xlsx";
        //创建输入流
        InputStream inputStream = this.getClass().getResourceAsStream(name);

        //通过构造函数传参
        Workbook workbook = name.endsWith("xls") ?new HSSFWorkbook(inputStream):new XSSFWorkbook(inputStream);
        //获取工作表
        Sheet sheet = workbook.getSheetAt(0);
        //获取行,行号作为参数传递给getRow方法,第一行从0开始计算
        Row row = sheet.getRow(1);
        //获取单元格,row已经确定了行号,列号作为参数传递给getCell,第一列从0开始计算
        Cell cell = row.getCell(1);
        //获取单元格的值,即C1的值(第一行,第三列)
        String cellValue = cell.getStringCellValue();
        System.out.println("第一行第三列的值是："+cellValue);
        workbook.close();
        inputStream.close();

    }

}
