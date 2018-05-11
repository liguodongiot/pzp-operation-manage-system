

常用组件：

HSSFWorkbook   --> excel的文档对象

HSSFSheet  -->  excel的表单

HSSFRow   -->  excel的行

HSSFCell  -->   excel的格子单元

HSSFFont -->  excel字体

样式：

HSSFCellStyle   --> cell样式






用poi要导出一个Excel表格的正确顺序应该是：

1、用HSSFWorkbook打开或者创建“Excel文件对象”

2、用HSSFWorkbook对象返回或者创建Sheet对象

3、用Sheet对象返回行对象，用行对象得到Cell对象

4、对Cell对象读写。

5、将生成的HSSFWorkbook放入HttpServletResponse中响应到前端页面