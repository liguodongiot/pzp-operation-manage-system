package com.pzp.manage.test;

import org.ansj.library.DicLibrary;
import org.ansj.splitWord.analysis.*;
import org.ansj.util.MyStaticValue;
import org.junit.Test;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/5/8 14:10 星期二
 */
public class AnsjTest {

    @Test
    public void testAnsj() {

        String str = "洁面仪配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹" ;

        System.out.println(BaseAnalysis.parse(str));
        System.out.println(ToAnalysis.parse(str));
        System.out.println(DicAnalysis.parse(str));
        System.out.println(IndexAnalysis.parse(str));
        System.out.println(NlpAnalysis.parse(str));
    }


    @Test
    public void testDefault(){
        MyStaticValue.ENV.put(DicLibrary.DEFAULT,"library/ext.dic");
        String str = "李国冬是哪里人";
        System.out.println(DicAnalysis.parse(str));
    }




}
