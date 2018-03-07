package com.pzp.manage.bean.es;

import lombok.Data;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.bean.es</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/3/7 11:42 星期三
 */
@Data
public class EsIndexFail {

/**********************************************

 {
 "error" : {
 "root_cause" : [
 {
 "type" : "index_already_exists_exception",
 "reason" : "index [gb/lk326oGyT-mittYoouml5g] already exists",
 "index_uuid" : "lk326oGyT-mittYoouml5g",
 "index" : "gb"
 }
 ],
 "type" : "index_already_exists_exception",
 "reason" : "index [gb/lk326oGyT-mittYoouml5g] already exists",
 "index_uuid" : "lk326oGyT-mittYoouml5g",
 "index" : "gb"
 },
 "status" : 400
 }


 "type" : "index_not_found_exception",
 "reason" : "no such index",
 "resource.type" : "index_or_alias",
 "resource.id" : "gb",
 "index_uuid" : "_na_",
 "index" : "gb"

 **********************************************/

    private EsErrorInfo error;

    private Integer status;
}
