package top.andnux.libbase.manager;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {

    private static final ThreadManager ourInstance = new ThreadManager();

    public static ThreadManager getInstance() {
        return ourInstance;
    }

    private ThreadPoolExecutor threadPoolExecutor;
    private LinkedBlockingQueue<WeakReference<Future<?>>> service = new LinkedBlockingQueue<>();

    private ThreadManager() {
        //
        RejectedExecutionHandler handler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                try {
                    service.put(new WeakReference<>(new FutureTask<Object>(runnable, null)));//
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        threadPoolExecutor = new ThreadPoolExecutor(4, 10,
                10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(4), handler);
        threadPoolExecutor.execute(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                FutureTask futureTask = null;
                try {
                    futureTask = (FutureTask) service.take().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (futureTask != null) {
                    threadPoolExecutor.execute(futureTask);
                }
            }
        }
    };

    public <T> void execute(final Runnable runnable, long delayed) {
        if (runnable != null) {
            execute(new FutureTask<Runnable>(runnable, null), delayed);
        }
    }

    public <T> void execute(final Runnable runnable) {
        execute(runnable, 0);
    }

    public <T> void execute(final FutureTask<T> futureTask) {
        execute(futureTask, 0);
    }

    public <T> void execute(final FutureTask<T> futureTask, long delayed) {
        if (futureTask != null) {
            try {

                if (delayed != 0) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            try {
                                service.put(new WeakReference<>(futureTask));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }, delayed);
                } else {
                    service.put(new WeakReference<>(futureTask));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
