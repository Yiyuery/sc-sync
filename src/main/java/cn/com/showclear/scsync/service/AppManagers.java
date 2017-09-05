package cn.com.showclear.scsync.service;

import cn.com.showclear.scsync.impl.MlocManagerImpl;
import cn.com.showclear.scsync.service.mloc.MlocManager;
import cn.com.showclear.utils.PropertyConfig;
import cn.com.showclear.utils.TaskEngine;

/**
 * 接口管理
 * @author YF-XIACHAOYANG
 * @date 2017/9/4 12:58
 */
public final class AppManagers implements IBaseManager{

    /**
     * 人员定位数据同步
     */
    private static MlocManager mloc ;

    /**
     * 私有构造方法，防止被实例化
     */
    private AppManagers(){}

    /**
     * 此处使用一个内部类来维护单例
     */
    private static final class App{
        private static AppManagers INSTANCE = new AppManagers();
    }

    /**
     * 获取实例
     * @return
     */
    public static  AppManagers getInstance() {
        return App.INSTANCE;
    }

    /**
     * 启动
     */
    @Override
    public void startup() {
        /**
         * 任务引擎初始化
         */
        taskEngineInit();
        /**
         * 人员定位数据同步模块管理
         */
        getMlocManager().startup();
    }

    /**
     * 关闭
     */
    @Override
    public void shutdown() {
        getMlocManager().shutdown();
    }

    /**
     * 任务引擎初始化
     */
    public void taskEngineInit(){
        /**
         * 定时任务管理器
         */
        int maxTaskThread = PropertyConfig.instance().getInt("thread.max.task", 10);
        TaskEngine.INSTANCE.startup(maxTaskThread);
    }

    /**
     * 获取 Mloc
     * @return
     */
    public static MlocManager getMlocManager() {
        if (mloc == null) {
            mloc = new MlocManagerImpl();
        }
        return mloc;
    }
}
