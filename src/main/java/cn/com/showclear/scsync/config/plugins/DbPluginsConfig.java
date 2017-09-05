package cn.com.showclear.scsync.config.plugins;

import java.util.Map;

import cn.com.showclear.utils.PropertyConfig;
import com.jfinal.kit.PathKit;
import org.apache.commons.lang3.StringUtils;
import com.jfinal.config.Plugins;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import org.apache.log4j.Logger;

/**
 * JFinal 程序配置类
 * 根据数据库配置初始化 JFinal 数据库连接池，以及相应的 Model 配置。
 * 数据库相关配置文件：
 * {class_path}/default.config
 * {user_config}/db.properties or config.properties
 */
public class DbPluginsConfig {

    private static final Logger log = Logger.getLogger(DbPluginsConfig.class);

    /**
     * 根据配置文件完成 JFinal 的数据库配置
     *
     * @param me
     */
    public static void initialize(Plugins me) {
        String dbNames = config().get("db.names");
        if (StringUtils.isEmpty(dbNames)) {
            return;
        }
        String[] dbNamesArr = dbNames.split(",");
        for (String dbName : dbNamesArr) {
            dbName = dbName.trim();
            if (StringUtils.isEmpty(dbName)) continue;
            //
            addDbPlugin(dbName, me);
        }
    }

    private static void addDbPlugin(String name, Plugins me) {
        //
        String driverClass = config().get("db." + name + ".driverClass");
        String jdbcUrl = config().get("db." + name + ".jdbcUrl");
        String username = config().get("db." + name + ".username");
        String password = config().get("db." + name + ".password");
        int initialPoolSize = config().getInt("db." + name + ".initialPoolSize", -1);
        int minPoolSize = config().getInt("db." + name + ".minPoolSize", -1);
        int maxPoolSize = config().getInt("db." + name + ".maxPoolSize", -1);
        int maxIdleTime = config().getInt("db." + name + ".maxIdleTime", -1);
        boolean showSql = config().getBoolean("db." + name + ".showSql", false);
        String sqlBasePath = config().get("db." + name + ".sqlBasePath", null);
        String sqlTemplate = config().get("db." + name + ".sqlTemplate", null);
        if (jdbcUrl != null) jdbcUrl = jdbcUrl.trim();
        if (username != null) username = username.trim();
        if (password != null) password = password.trim();
        // 数据库配置
        C3p0Plugin c3p0Plugin;
        if (StringUtils.isEmpty(driverClass)) {
            c3p0Plugin = new C3p0Plugin(jdbcUrl, username, password);
        } else {
            c3p0Plugin = new C3p0Plugin(jdbcUrl, username, password, driverClass.trim());
        }
        if (initialPoolSize > 0) {
            c3p0Plugin.setInitialPoolSize(initialPoolSize);
        }
        if (minPoolSize > 0) {
            c3p0Plugin.setMinPoolSize(minPoolSize);
        }
        if (maxPoolSize > 0) {
            c3p0Plugin.setMaxPoolSize(maxPoolSize);
        }
        if (maxIdleTime > 0) {
            c3p0Plugin.setMaxIdleTime(maxIdleTime);
        }
        me.add(c3p0Plugin);
        /**
         * Db.use(name)... 数据库切换
         */
        ActiveRecordPlugin arp = new ActiveRecordPlugin(name, c3p0Plugin);
        arp.setShowSql(showSql);
        /**
         * jFinal动态sql模板管理
         */
        if (sqlBasePath != null && sqlTemplate != null) {
            arp.setBaseSqlTemplatePath(PathKit.getRootClassPath() + sqlBasePath);
            for(String sql : sqlTemplate.split(",")) {
                arp.addSqlTemplate(sql);
            }
        }
        /**
         * 添加model表映射
         */
        final String prefix = "db." + name + ".model.";
        Map<String, String> modelsCfg = config().getByPrefix(prefix);
        if (modelsCfg.size() > 0) {
            for (Map.Entry<String, String> ent : modelsCfg.entrySet()) {
                String tableName = ent.getKey().substring(prefix.length());
                addMapping(arp, tableName, ent.getValue());
            }
        }
        me.add(arp);
    }

    /**
     * 添加表映射 / 其实可以使用 Record + Db 的方式和数据库进行交互
     *
     * @param arp
     * @param tableName
     * @param val
     */
    private static void addMapping(ActiveRecordPlugin arp, String tableName, String val) {
        if (StringUtils.isEmpty(tableName)) return;
        if (StringUtils.isEmpty(val)) return;
        //
        Class<? extends Model<?>> modelClass;
        // val : [id,]className
        String[] valArr = val.split(",");
        if (valArr.length > 1) {
            modelClass = classForName(valArr[1].trim());
            if (modelClass == null) return;
            arp.addMapping(tableName, valArr[0].trim(), modelClass);
        } else {
            modelClass = classForName(valArr[0].trim());
            if (modelClass == null) return;
            arp.addMapping(tableName, modelClass);
        }
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Model<?>> classForName(String className) {
        if (StringUtils.isEmpty(className)) {
            return null;
        }
        try {
            return (Class<? extends Model<?>>) Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            log.error("not found model class : " + className);
        }
        return null;
    }

    private static PropertyConfig config() {
        return PropertyConfig.instance();
    }
}

