package com.pzp.manage.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfilesResolver;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: 指定读取配置文件</p>
 * <p>Description:
 * https://www.jianshu.com/p/83cd7f9bddba
 * </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/2 14:14 星期五
 */
public class ProfilesResolver implements ActiveProfilesResolver {

    private final static Logger LOGGER  = LoggerFactory.getLogger(ProfilesResolver.class);

    @Override
    public String[] resolve(Class<?> aClass) {
        String activeProfiles = System.getProperty("spring.profiles.active");
        LOGGER.info("activeProfiles: {} ...",activeProfiles);
        return new String[] {activeProfiles != null ? activeProfiles : "home"};
    }
}

