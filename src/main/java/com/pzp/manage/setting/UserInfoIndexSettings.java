package com.pzp.manage.setting;

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
 * @date 2018/2/9 11:05 星期五
 */
@Component
@ConfigurationProperties(prefix = "es.userInfo")
public class UserInfoIndexSettings extends BaseIndexSettings{

}
