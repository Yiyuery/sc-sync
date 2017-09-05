package cn.com.showclear.scsync.impl;

import cn.com.showclear.scsync.impl.mloc.MlocSyncTask;
import cn.com.showclear.scsync.service.mloc.MlocManager;
import cn.com.showclear.utils.PropertyConfig;
import cn.com.showclear.utils.TaskEngine;
import org.apache.log4j.Logger;

/**
 * 人员定位定时同步
 * @author YF-XIACHAOYANG
 * @date 2017/9/4 13:21
 */
public class MlocManagerImpl implements MlocManager {

    private static final Logger log = Logger.getLogger(MlocManagerImpl.class);
    /**
     * 人员定位同步任务
     */
    private MlocSyncTask mlocSyncTask;

    /**
     * 启动
     */
    @Override
    public void startup() {
        long delaySec = PropertyConfig.instance().getInt("timer.access.delay", 60);
        long periodSec = PropertyConfig.instance().getInt("timer.access.period", 10);
        if (mlocSyncTask == null) {
            mlocSyncTask = new MlocSyncTask();
            TaskEngine.INSTANCE.schedule(mlocSyncTask, delaySec * 1000, periodSec * 1000);
        }
    }

    /**
     * 关闭
     */
    @Override
    public void shutdown() {
        if (mlocSyncTask != null) {
            TaskEngine.INSTANCE.cancelScheduledTask(mlocSyncTask);
            mlocSyncTask = null;
        }
    }



}
