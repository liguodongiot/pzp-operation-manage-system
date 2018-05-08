package com.pzp.manage.util;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.util</p>
 * <p>Title: 敏感词处理工具 - IKAnalyzer中文分词工具 - 借助分词进行敏感词过滤</p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/5/8 16:40 星期二
 */
public class IkTokenUtil {

    /**
     * 敏感词集合
     */
    public static HashMap sensitiveWordMap;

    /**
     * 初始化敏感词库
     *
     * @param sensitiveWordSet 敏感词库
     */
    public static synchronized void init(Set<String> sensitiveWordSet) {
        //初始化敏感词容器，减少扩容操作
        sensitiveWordMap = new HashMap(sensitiveWordSet.size());
        for (String sensitiveWord : sensitiveWordSet) {
            sensitiveWordMap.put(sensitiveWord, sensitiveWord);
        }
    }

    /**
     * 判断文字是否包含敏感字符
     *
     * @param txt 文字
     * @return 若包含返回true，否则返回false
     */
    public static boolean contains(String txt) throws Exception {
        boolean flag = false;
        List<String> wordList = segment(txt);
        for (String word : wordList) {
            if (sensitiveWordMap.get(word) != null) {
                return true;
            }
        }
        return flag;
    }

    /**
     * 获取文字中的敏感词
     *
     * @param txt 文字
     * @return
     */
    public static Set<String> getSensitiveWord(String txt) throws IOException {
        Set<String> sensitiveWordList = new HashSet<>();

        List<String> wordList = segment(txt);
        for (String word : wordList) {
            if (sensitiveWordMap.get(word) != null) {
                sensitiveWordList.add(word);
            }
        }
        return sensitiveWordList;
    }

    /**
     * 替换敏感字字符
     *
     * @param txt         文本
     * @param replaceChar 替换的字符，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符：*， 替换结果：我爱***
     * @return
     */
    public static String replaceSensitiveWord(String txt, char replaceChar) throws IOException {
        String resultTxt = txt;
        //获取所有的敏感词
        Set<String> sensitiveWordList = getSensitiveWord(txt);
        String replaceString;
        for (String sensitiveWord : sensitiveWordList) {
            replaceString = getReplaceChars(replaceChar, sensitiveWord.length());
            resultTxt = resultTxt.replaceAll(sensitiveWord, replaceString);
        }
        return resultTxt;
    }

    /**
     * 替换敏感字字符
     *
     * @param txt        文本
     * @param replaceStr 替换的字符串，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符串：[屏蔽]，替换结果：我爱[屏蔽]
     * @return
     */
    public static String replaceSensitiveWord(String txt, String replaceStr) throws IOException {
        String resultTxt = txt;
        //获取所有的敏感词
        Set<String> sensitiveWordList = getSensitiveWord(txt);
        for (String sensitiveWord : sensitiveWordList) {
            resultTxt = resultTxt.replaceAll(sensitiveWord, replaceStr);
        }
        return resultTxt;
    }

    /**
     * 获取替换字符串
     *
     * @param replaceChar
     * @param length
     * @return
     */
    private static String getReplaceChars(char replaceChar, int length) {
        String resultReplace = String.valueOf(replaceChar);
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }

        return resultReplace;
    }

    /**
     * 对语句进行分词
     *
     * @param text 语句
     * @return 分词后的集合
     * @throws IOException
     */
    private static List segment(String text) throws IOException {
        List<String> list = new ArrayList<>();
        StringReader re = new StringReader(text);
        IKSegmenter ik = new IKSegmenter(re, true);
        Lexeme lex;
        while ((lex = ik.next()) != null) {
            list.add(lex.getLexemeText());
        }
        return list;
    }



}
