package com.pzp.manage.test;

import com.alibaba.fastjson.JSONObject;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.dic.LearnTool;
import org.ansj.domain.Nature;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.DicLibrary;
import org.ansj.recognition.impl.NatureRecognition;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.*;
import org.ansj.util.MyStaticValue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

        Result parse = DicAnalysis.parse(str);
        List<Term> terms = parse.getTerms();
        List<String> termList = new ArrayList<>();
        for (Term term : terms) {
            termList.add(term.getName());
        }
        String join = String.join(" ", termList);
        System.out.println(join);

    }

    /**
     * 停止词测试
     */
    @Test
    public void testStopword(){
        String str = "我洁面仪配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹" ;
        Result parse = ToAnalysis.parse(str);
        System.out.println(JSONObject.toJSONString(parse.getTerms()));

        StopRecognition filter = new StopRecognition();
        filter.insertStopNatures("uj"); //过滤词性
        filter.insertStopNatures("ul");
        filter.insertStopNatures("null");
        filter.insertStopWords("我"); //过滤单词
        filter.insertStopRegexes("小.*?"); //支持正则表达式

        Result modifResult = ToAnalysis.parse(str).recognition(filter); //过滤分词结果
        System.out.println(JSONObject.toJSONString(modifResult.getTerms()));
    }


    /**
     * 词性标注
     */
    @Test
    public void testNatureTag(){
        Result parse = ToAnalysis.parse("Ansj中文分词是一个真正的ict的实现.并且加入了自己的一些数据结构和算法的分词.实现了高效率和高准确率的完美结合!");
        List<Term> terms = parse.getTerms();
        System.out.println(terms);
        new NatureRecognition().recognition(parse); //词性标注
        System.out.println(terms);

        System.out.println(JSONObject.toJSONString(NatureRecognition.guessNature("李国冬")));


        String[] strs = {"对", "非", "ansj", "的", "分词", "结果", "进行", "词性", "标注"} ;
        List<String> lists = Arrays.asList(strs) ;
        List<Term> recognition = new NatureRecognition().recognition(lists, 0) ;
        System.out.println(recognition);

    }


    /**
     * 关键词抽取
     */
    @Test
    public void testKeyWordCompuer(){
        // 返回关键词个数
        KeyWordComputer kwc = new KeyWordComputer(5);
        String title = "维基解密否认斯诺登接受委内瑞拉庇护";
        String content = "有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。　　俄罗斯国会国际事务委员会主席普什科夫，在个人推特率先披露斯诺登已接受委内瑞拉的庇护建议，令外界以为斯诺登的动向终于有新进展。　　不过推文在几分钟内旋即被删除，普什科夫澄清他是看到俄罗斯国营电视台的新闻才这样说，而电视台已经作出否认，称普什科夫是误解了新闻内容。　　委内瑞拉驻莫斯科大使馆、俄罗斯总统府发言人、以及外交部都拒绝发表评论。而维基解密就否认斯诺登已正式接受委内瑞拉的庇护，说会在适当时间公布有关决定。　　斯诺登相信目前还在莫斯科谢列梅捷沃机场，已滞留两个多星期。他早前向约20个国家提交庇护申请，委内瑞拉、尼加拉瓜和玻利维亚，先后表示答应，不过斯诺登还没作出决定。　　而另一场外交风波，玻利维亚总统莫拉莱斯的专机上星期被欧洲多国以怀疑斯诺登在机上为由拒绝过境事件，涉事国家之一的西班牙突然转口风，外长马加略]号表示愿意就任何误解致歉，但强调当时当局没有关闭领空或不许专机降落。";
        Collection<Keyword> result = kwc.computeArticleTfidf(title, content);
        System.out.println(result);
    }


    /**
     * 新词发现
     */
    @Test
    public void testLearnTool(){
        //构建一个新词学习的工具类。这个对象。保存了所有分词中出现的新词。出现次数越多。相对权重越大。
        LearnTool learnTool = new LearnTool() ;

        //进行词语分词。也就是nlp方式分词，这里可以分多篇文章
        NlpAnalysis nlpAnalysis = new NlpAnalysis().setLearnTool(learnTool);
        nlpAnalysis.parse("说过，社交软件也是打着沟通的平台，让无数寂寞男女有了肉体与精神的寄托。");
        nlpAnalysis.parse("其实可以打着这个需求点去运作的互联网公司不应只是社交类软件与可穿戴设备，还有携程网，去哪儿网等等，订房订酒店多好的寓意");
        nlpAnalysis.parse("张艺谋的卡宴，马明哲的戏");
        nlpAnalysis.parse("冬李动力东西冬李第冬李");

        //取得学习到的topn新词,返回前10个。这里如果设置为0则返回全部
        System.out.println(learnTool.getTopTree(10));

        //只取得词性为Nature.NR的新词
        System.out.println(learnTool.getTopTree(10, Nature.NR));
    }



}
