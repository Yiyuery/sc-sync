package cn.com.showclear.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置加载、管理类
 * 首先需要调用 initialize(appConfig) 方法。
 * appConfig 放在classes目录中，配置如下：
 * <pre>
 * # 应用名称
 * app_name=dispatch-web
 * # 配置文件编码
 * config_encoding=UTF-8
 * # 需加载的默认配置文件（可选）（在classes目录中）
 * default=default.properties
 * # 需加载的配置文件（Linux在 /etc/scooper/{app_name}/ 目录下,Windows在 {UserHome}/scooper/{app_name}/ 目录下）
 * configs=config.properties,db.properties
 * </pre>
 */
public class PropertyConfig {

    /** key of app_name */
    public static final String APP_NAME = "app_name";
    /** key of config file encoding */
    public static final String CONF_ENC = "config_encoding";
    /** key of default config file name */
    public static final String DEF_CONF = "default";
    /** key of config files */
    public static final String CONF_FILES = "configs";

    // Linux 下的配置文件根目录
    private static final String CONF_ROOT_LINUX = "/icooper/config";
    // Windows 下的配置文件根目录
    private static final String CONF_ROOT_WINDOWS = System.getProperty("user.home") + "/scooper";

    // 单例
    private static class PropertyConfigInstance {
        private static final PropertyConfig _instance = new PropertyConfig();
    }

    public static PropertyConfig instance() {
        return PropertyConfigInstance._instance;
    }

    protected Map<String, String> config = new HashMap<String, String>();

    /**
     * 初始化并加载配置文件
     * @param appConfig 应用配置文件名
     */
    public void initialize(String appConfig) {

        // 加载应用配置
        loadFromResource(appConfig, "UTF-8");
        // 加载默认配置
        String defaultConfig = get(DEF_CONF);
        if (defaultConfig != null && !defaultConfig.isEmpty()) {
            loadFromResource(defaultConfig);
        }
        // 加载用户配置
        String[] configs = get(CONF_FILES, "").split(",");
        for (int i = 0; i < configs.length; i++) {
            if (configs[i] == null || configs[i].trim().isEmpty()) {
                continue;
            }
            loadFromUserDirectory(configs[i].trim());
        }
    }

    /**
     * 判断当前操作系统是否为 Windows
     * @return
     */
    public boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * 获取用户配置文件存储目录
     * @return
     */
    public File getUserConfigDirectory() {
        if (isWindows()) {
            return new File(CONF_ROOT_WINDOWS, get(APP_NAME));
        }
        return new File(CONF_ROOT_LINUX, get(APP_NAME));
    }

    /**
     * 获取配置文件编码
     * @return
     */
    public String getConfigEncoding() {
        return get(CONF_ENC, "UTF-8");
    }

    /**
     * 已加载的配置文件中，是否包含该配置项
     * @param key
     * @return
     */
    public boolean has(String key) {
        return config.containsKey(key);
    }

    public void put(String key, String value) {
        config.put(key, value);
    }

    public String get(String key) {
        return has(key) ? config.get(key) : null;
    }

    public String get(String key, String defaultValue) {
        String val = get(key);
        return val == null ? defaultValue : val;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        String val = get(key);
        try {
            if (val != null) {
                return Integer.parseInt(val);
            }
        } catch (Exception e) {
            // Ignore.
        }
        return defaultValue;
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        String val = get(key);
        try {
            if (val != null) {
                return Long.parseLong(val);
            }
        } catch (Exception e) {
            // Ignore.
        }
        return defaultValue;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String val = get(key);
        try {
            if (val != null) {
                val = val.toLowerCase();
                return "true".equals(val) || "yes".equals(val);
            }
        } catch (Exception e) {
            // Ignore.
        }
        return defaultValue;
    }

    public Map<String, String> getByPrefix(String prefix) {
        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry<String, String> ent : config.entrySet()) {
            if (ent.getKey() == null) continue;
            if (ent.getKey().startsWith(prefix)) {
                map.put(ent.getKey(), ent.getValue());
            }
        }
        return map;
    }

    /**
     * load properties from classes
     * @param name
     */
    public void loadFromResource(String name) {
        loadFromResource(name, getConfigEncoding());
    }

    /**
     * load properties from classes
     * @param name
     * @param encoding
     */
    public void loadFromResource(String name, String encoding) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("file name can't be null or empty");
        }
        if (name.contains("..")) {
            throw new IllegalArgumentException("file name can't contains \"..\"");
        }
        InputStream inputStream = null;
        java.util.Properties props = new java.util.Properties();
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            props.load(new InputStreamReader(inputStream, encoding));
        } catch (IOException e) {
            throw new IllegalArgumentException("load '" + name + "' from resource failed : " + e.getMessage());
        } finally {
            if (inputStream != null) {
                try { inputStream.close(); } catch (IOException ex) { }
            }
        }
        // copy properties
        for (Map.Entry<Object, Object> ent : props.entrySet()) {
            config.put(ent.getKey().toString(), ent.getValue() == null ? "" : ent.getValue().toString());
        }
    }

    /**
     * load properties from user directory
     * @param name
     */
    public void loadFromUserDirectory(String name) {
        loadFromUserDirectory(name, getConfigEncoding());
    }

    /**
     * load properties from user directory
     * @param name
     * @param encoding
     */
    public void loadFromUserDirectory(String name, String encoding) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("file name can't be null or empty");
        }
        if (name.contains("..")) {
            throw new IllegalArgumentException("file name can't contains \"..\"");
        }
        //
        InputStream inputStream = null;
        java.util.Properties props = new java.util.Properties();
        try {
            File file = new File(getUserConfigDirectory(), name);
            if (!file.exists()) {
                throw new IllegalArgumentException("user config not exists : " + file.toString());
            }
            inputStream = new FileInputStream(file);
            props.load(new InputStreamReader(inputStream, encoding));
        } catch (IOException e) {
            throw new IllegalArgumentException("load '" + name + "' from user directory failed : " + e.getMessage());
        } finally {
            if (inputStream != null) {
                try { inputStream.close(); } catch (IOException ex) { }
            }
        }
        // copy properties
        for (Map.Entry<Object, Object> ent : props.entrySet()) {
            config.put(ent.getKey().toString(), ent.getValue() == null ? "" : ent.getValue().toString());
        }
    }

    /**
     * 测试配置文件解析
     * @return
     */
    public Map<String, String> getConfig() {
        return config;
    }
}
