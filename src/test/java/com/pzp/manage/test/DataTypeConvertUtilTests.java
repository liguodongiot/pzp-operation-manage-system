package com.pzp.manage.test;

import com.pzp.manage.util.DataTypeConvertUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/28 16:38 星期三
 */
public class DataTypeConvertUtilTests {

    @Test
    public void test(){

        Map<String,Object> map = new HashMap<>();
        map.put("keyInt",2);
        map.put("keyFloat",33.4f);
        map.put("keyDoule",23.445D);
        map.put("keyString","dongdong");
        map.put("keyBoolean",true);


        System.out.println(DataTypeConvertUtil.mapValue2Int(map,"keyInt"));
        System.out.println(DataTypeConvertUtil.mapValue2Int(map,"keyInt2"));
        System.out.println(DataTypeConvertUtil.mapValue2Float(map,"keyFloat"));
        System.out.println(DataTypeConvertUtil.mapValue2Float(map,"keyFloat2"));
        System.out.println(DataTypeConvertUtil.mapValue2Double(map,"keyDoule"));
        System.out.println(DataTypeConvertUtil.mapValue2Double(map,"keyDoule2"));
        System.out.println(DataTypeConvertUtil.mapValue2Str(map,"keyString"));
        System.out.println(DataTypeConvertUtil.mapValue2Str(map,"keyString2"));
        System.out.println(DataTypeConvertUtil.mapValue2Boolean(map,"keyBoolean"));
        System.out.println(DataTypeConvertUtil.mapValue2Boolean(map,"keyBoolean2"));


        System.out.println(DataTypeConvertUtil.boolean2Str(true));
        System.out.println(DataTypeConvertUtil.int2Str(222));
        System.out.println(DataTypeConvertUtil.float2Str(332.43F));
        System.out.println(DataTypeConvertUtil.double2Str(324.22D));

    }


    @Test
    public void testUUID(){
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        System.out.println(uuid.replace("-", "").toLowerCase());
    }
}
