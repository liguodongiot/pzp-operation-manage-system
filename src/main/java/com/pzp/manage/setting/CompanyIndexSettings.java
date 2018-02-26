package com.pzp.manage.setting;

import com.pzp.manage.controller.UserInfoEsController;
import com.pzp.manage.es.EsContext;
import com.pzp.manage.es.EsParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.setting</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/26 10:38 星期一
 */

@Component
@ConfigurationProperties(prefix = "es.company")
public class CompanyIndexSettings extends BaseIndexSettings{

}