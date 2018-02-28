package com.pzp.manage.util;

import java.util.Map;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.util</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/28 16:21 星期三
 */
public class DataTypeConvertUtil {

    private DataTypeConvertUtil(){
    }

    public static Integer str2Int(String value){
        return Integer.parseInt(value);
    }

    public static String int2Str(Integer value){
        return Integer.toString(value);
    }

    public static Integer obj2Int(Object value){
        return str2Int(value.toString());
    }

    public static Integer mapValue2Int(Map<String,Object> map,String key){
        return map.get(key)==null ? null : obj2Int(map.get(key));
    }

    public static Float str2Float(String value){
        return Float.parseFloat(value);
    }

    public static String float2Str(Float value){
        return Float.toString(value);
    }

    public static Float obj2Float(Object value){
        return str2Float(value.toString());
    }

    public static Float mapValue2Float(Map<String,Object> map,String key){
        return map.get(key)==null ? null : obj2Float(map.get(key));
    }


    public static Double str2Double(String value){
        return Double.parseDouble(value);
    }

    public static String double2Str(Double value){
        return Double.toString(value);
    }

    public static Double obj2Double(Object value){
        return str2Double(value.toString());
    }

    public static Double mapValue2Double(Map<String,Object> map,String key){
        return map.get(key)==null ? null : obj2Double(map.get(key));
    }

    public static String obj2Str(Object value){
        return value.toString();
    }

    public static String mapValue2Str(Map<String,Object> map,String key){
        return map.get(key)==null ? null : obj2Str(map.get(key));
    }

    public static Boolean str2Boolean(String value){
        return Boolean.parseBoolean(value);
    }

    public static String boolean2Str(Boolean value){
        return Boolean.toString(value);
    }

    public static Boolean obj2Boolean(Object value){
        return str2Boolean(value.toString());
    }

    public static Boolean mapValue2Boolean(Map<String,Object> map,String key){
        return map.get(key)==null ? null : obj2Boolean(map.get(key));
    }

}
