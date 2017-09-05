package cn.com.showclear.utils;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务引擎
 * @author YF-XIACHAOYANG
 * @date 2017/9/4 17:32
 */
public enum TaskEngine {

    INSTANCE;

    /**
     * 线程池的最大数
     */
    private int maxThread = 10;
    /**
     * 线程名称前缀
     */
    private String threadNamePre = "TaskHandler_";

    private Timer timer;

    private ExecutorService executor;

    private Map<TimerTask, TimerTaskWrapper> wrappedTasks;

    private TaskEngine() {
    }

    /**
     *  启动任务引擎
     */
    public void startup() {
        startup(maxThread, threadNamePre);
    }

    /**
     *  启动任务引擎
     * @param maxThread 线程池最大数，范围(0, 512]
     */
    public void startup(int maxThread) {
        startup(maxThread, threadNamePre);
    }

    /**
     *  启动任务引擎
     * @param maxThread 线程池最大数，范围(0, 512]
     * @param threadNamePre 线程名称前缀
     */
    public void startup(int maxThread, String threadNamePre) {
        //防止线程池过大或者非法
        if (maxThread <= 0 || maxThread > 512) {
            this.maxThread = 10;
        } else {
            this.maxThread = maxThread;
        }
        if (threadNamePre != null && !threadNamePre.isEmpty()) {
            this.threadNamePre = threadNamePre;
        }

        timer = new Timer("TaskEngine-timer", true);

        wrappedTasks = new ConcurrentHashMap<TimerTask, TimerTaskWrapper>();

        executor = Executors.newFixedThreadPool(this.maxThread, new ThreadFactory() {
            final AtomicInteger threadNumber = new AtomicInteger(1);
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, genThreadName(threadNumber.getAndIncrement()));
            }
        });
    }

    /**
     * 生成线程名称
     * @param flag
     * @return
     */
    private String genThreadName(int flag) {
        return threadNamePre + flag;
    }

    /**
     * 关闭任务引擎
     */
    public void shutdown() {
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 取消任务
     * @param task
     */
    public void cancelScheduledTask(TimerTask task) {
        TaskEngine.TimerTaskWrapper taskWrapper = wrappedTasks.remove(task);
        if (taskWrapper != null) {
            taskWrapper.cancel();
        }
    }

    public void schedule(TimerTask task, Date time) {
        timer.schedule(new TimerTaskWrapper(task), time);
    }

    public void schedule(TimerTask task, Date firstTime, long period) {
        TimerTaskWrapper taskWrapper = new TimerTaskWrapper(task);
        wrappedTasks.put(task, taskWrapper);
        timer.schedule(taskWrapper, firstTime, period);
    }

    public void schedule(TimerTask task, long delay) {
        timer.schedule(new TimerTaskWrapper(task), delay);
    }

    public void schedule(TimerTask task, long delay, long period) {
        TimerTaskWrapper taskWrapper = new TimerTaskWrapper(task);
        wrappedTasks.put(task, taskWrapper);
        timer.schedule(taskWrapper, delay, period);
    }

    public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
        TimerTaskWrapper taskWrapper = new TimerTaskWrapper(task);
        wrappedTasks.put(task, taskWrapper);
        timer.scheduleAtFixedRate(taskWrapper, firstTime, period);
    }

    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        TimerTaskWrapper taskWrapper = new TimerTaskWrapper(task);
        wrappedTasks.put(task, taskWrapper);
        timer.scheduleAtFixedRate(taskWrapper, delay, period);
    }

    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }

    private class TimerTaskWrapper extends TimerTask {
        private TimerTask task;

        public TimerTaskWrapper(TimerTask task) {
            this.task = task;
        }

        @Override
        public void run() {
            executor.submit(task);
        }
    }
}
