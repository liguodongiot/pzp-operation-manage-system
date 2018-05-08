package com.pzp.manage.test;

import com.pzp.manage.util.IkTokenUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/5/8 16:42 星期二
 */
public class IkTokenTest {


    /**
     * 使用 IKAnalyzer 可以很轻松的实现敏感词过滤功能。
     * 缺点：使用 IKAnalyzer 进行分词，有时候分词结果并不是很理想。如：发呆着，
     * 分词结果是 ["发","呆着"]，而我们的敏感词是发呆，这种情况就会造成敏感词过滤不完整。
     * @throws IOException
     */
    @Test
    public void test() throws IOException {
        Set<String> sensitiveWordSet = new HashSet<>();
        sensitiveWordSet.add("太多");
        sensitiveWordSet.add("爱恋");
        sensitiveWordSet.add("静静");
        sensitiveWordSet.add("哈哈");
        sensitiveWordSet.add("啦啦");
        sensitiveWordSet.add("感动");
        sensitiveWordSet.add("发呆");
        //初始化敏感词库
        IkTokenUtil.init(sensitiveWordSet);

        /**
         * 需要进行处理的目标字符串
         */
        System.out.println("敏感词的数量：" + IkTokenUtil.sensitiveWordMap.size());
        String string = "太多的伤感情怀也许只局限于饲养基地 荧幕中的情节。"
                + "然后 我们的扮演的角色就是跟随着主人公的喜红客联盟 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
                + "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个贱人一杯红酒一部电影在夜 深人静的晚上，关上电话静静的发呆着。";
        System.out.println("待检测语句字数：" + string.length());

        /**
         * 是否含有关键字
         */
        try {
            boolean result = IkTokenUtil.contains(string);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 获取语句中的敏感词
         */
        Set<String> set = IkTokenUtil.getSensitiveWord(string);
        System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);

        /**
         * 替换语句中的敏感词
         */
        String filterStr = IkTokenUtil.replaceSensitiveWord(string, '*');
        System.out.println(filterStr);

        String filterStr2 = IkTokenUtil.replaceSensitiveWord(string, "[*敏感词*]");
        System.out.println(filterStr2);
    }


}
