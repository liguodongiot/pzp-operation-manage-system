package com.pzp.manage.test;

import org.junit.Test;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/5/8 17:41 星期二
 */
public class IkTest {

    @Test
    public void test() throws IOException {
        //String s = "JAVA是一个好语言，从到2015年12月1日它已经有20年的历史了";
        String s = "李国冬哪里人";
        queryWords(s);
    }

    public static void queryWords(String query) throws IOException {
        Configuration cfg = DefaultConfig.getInstance();
        System.out.println(cfg.getMainDictionary()); // 系统默认词库
        System.out.println(cfg.getQuantifierDicionary());

        StringReader input = new StringReader(query.trim());
        IKSegmenter ikSeg = new IKSegmenter(input, true);   // true 用智能分词 ，false细粒度
        for (Lexeme lexeme = ikSeg.next(); lexeme != null; lexeme = ikSeg.next()) {
            System.out.print(lexeme.getLexemeText()+"|");
        }

    }


}
