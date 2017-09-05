package cn.com.showclear.scsync.config;

import cn.com.showclear.common.AppConstants;
import cn.com.showclear.utils.PropertyConfig;
import com.jfinal.config.JFinalConfig;
import com.jfinal.kit.Prop;

/**
 *  让 JFinal 使用 cn.com.showclear.utils.PropertyConfig 的配置信息
 * @author YF-XIACHAOYANG
 * @date 2017/9/4 14:39
 * @copy jiangwj
 */
public abstract class JFPropConfig extends JFinalConfig {

    protected void initConfig(String appConfig) {
        PropertyConfig.instance().initialize(appConfig);
        prop = new ConfProp(appConfig);
        /**
         * 对外开放配置参数：全局读取
         */
        AppConstants.SC_CONFIG =  PropertyConfig.instance().getConfig();
    }

    /**
     * 代理Prop类
     */
    private static class ConfProp extends Prop {
        public ConfProp(String appConfig) {
            super(appConfig);
        }
        @Override
        public String get(String key) {
            return PropertyConfig.instance().get(key);
        }
        @Override
        public String get(String key, String defaultValue) {
            return PropertyConfig.instance().get(key, defaultValue);
        }
        @Override
        public Integer getInt(String key, Integer defaultValue) {
            return PropertyConfig.instance().getInt(key, defaultValue == null ? 0 : defaultValue.intValue());
        }
        @Override
        public Long getLong(String key, Long defaultValue) {
            return PropertyConfig.instance().getLong(key, defaultValue == null ? 0 : defaultValue.longValue());
        }
        @Override
        public Boolean getBoolean(String key, Boolean defaultValue) {
            return PropertyConfig.instance().getBoolean(key, defaultValue == null ? false : defaultValue.booleanValue());
        }
        @Override
        public boolean containsKey(String key) {
            return PropertyConfig.instance().has(key);
        }
    }
}
