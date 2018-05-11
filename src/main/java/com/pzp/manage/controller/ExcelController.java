package com.pzp.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.pzp.manage.bean.Student;
import com.pzp.manage.util.ExcelUtil;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.controller</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/5/11 17:31 星期五
 */
@Controller
@RequestMapping("/excel")
public class ExcelController {

    /**
     * http://localhost:8888/excel/index
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("excel");
    }


    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public String export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取数据
        List<Student> list = new ArrayList<>();
        list.add(new Student("dd","F","22","北京大学","大二"));
        list.add(new Student("ss","F","22","北京大学","大二"));
        list.add(new Student("cc","F","22","北京dd大学","大二"));
        //excel标题
        String[] title = {"名称","性别","年龄","学校","班级"};

        //excel文件名
        String fileName = "学生信息表"+System.currentTimeMillis()+".xls";

        //sheet名
        String sheetName = "学生信息表";
        String[][] content = new String[3][];
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            Student student = list.get(i);
            content[i][0] = student.getName();
            content[i][1] = student.getSex();
            content[i][2] = student.getAge();
            content[i][3] = student.getSchool();
            content[i][4] = student.getClassName();
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            System.out.println("error e");
        } finally {
            wb.close();
        }
        return JSONObject.toJSONString("");
    }

    // 发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                System.out.println("error e");
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            System.out.println("error ex");
        }
    }

}
