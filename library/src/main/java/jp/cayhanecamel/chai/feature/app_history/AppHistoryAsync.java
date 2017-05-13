package jp.cayhanecamel.chai.feature.app_history;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import jp.cayhanecamel.chai.data.ChaiConfig;

public class AppHistoryAsync {
    private static AppHistoryAsync sInstance;

    AppHistoryAsync() {
    }

    public static synchronized AppHistoryAsync getInstance() {
        if (sInstance == null) {
            sInstance = new AppHistoryAsync();
        }
        return sInstance;
    }

    final ExecutorService executorService = Executors.newSingleThreadExecutor();
    final LinkedBlockingQueue<QueuedAppInfo> queue = new LinkedBlockingQueue<>();
    final AtomicInteger threadCount = new AtomicInteger();

    /**
     * App履歴を非同期で追加する。
     * <p>
     * 連番は{@link AppHistoryUtil#add(AppInfo)}とほぼ同様<br>
     * 生成時刻は生成時に記録
     * </p>
     *
     * @param appInfo 追加情報
     * @return このインスタンス
     */
    public AppHistoryAsync add(AppInfo appInfo) {
        switch (appInfo.type) {
            case WEB_API_REQUEST:
            case WEB_VIEW_REQUEST:
                add(appInfo, SimpleSequenceNumberGenerator.INSTANCE);
                break;
            default:
                add(appInfo, null);
        }
        return this;
    }

    /**
     * App履歴を非同期で追加する。
     * <p>
     * 生成時刻は生成時に記録
     * </p>
     *
     * @param appInfo                 追加情報
     * @param sequenceNumberGenerator 連番生成
     * @return このインスタンス
     */
    public AppHistoryAsync add(AppInfo appInfo, SequenceNumberGenerator sequenceNumberGenerator) {
        if ((ChaiConfig.getAppHistoryAddingMode() & ChaiConfig.CHAMPACA_APP_HISTORY_ADDING_MODE_ALLOW_ASYNC) == 0) {
            throw new IllegalStateException("AppHistoryAsync Disallowed");
        }

        appInfo.createdAt = System.currentTimeMillis();
        queue.add(new QueuedAppInfo(appInfo, sequenceNumberGenerator));
        return this;
    }

    /**
     * App履歴の追加を開始する。
     */
    public void start() {
        // Do parallel if needed
        if (executorService.isShutdown()) return;
        if (!threadCount.compareAndSet(0, 1)) return;

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        QueuedAppInfo info = queue.poll(1000, TimeUnit.MILLISECONDS);
                        if (info == null) return;
                        info.emit();
                    }
                } catch (InterruptedException e) {
                    // Nothing to do.
                } finally {
                    threadCount.set(0);
                }
            }
        });
    }

    /**
     * App履歴の追加を完全に停止する。
     */
    public void shutdown() {
        executorService.shutdown();
        queue.clear();
    }

    private static class QueuedAppInfo {
        private final AppInfo appInfo;
        private final SequenceNumberGenerator sequenceNumberGenerator;

        public QueuedAppInfo(AppInfo appInfo, SequenceNumberGenerator sequenceNumberGenerator) {
            this.appInfo = appInfo;
            this.sequenceNumberGenerator = sequenceNumberGenerator;
        }

        public void emit() {
            if (sequenceNumberGenerator != null) {
                appInfo.seq = sequenceNumberGenerator.next();
            }

            addToAppHistory(appInfo);
        }

        public static synchronized void addToAppHistory(AppInfo appInfo) {
            AppHistoryUtil.addInternally(appInfo, appInfo.createdAt);
        }
    }

    public interface SequenceNumberGenerator {
        long next();
    }

    private static final class SimpleSequenceNumberGenerator implements SequenceNumberGenerator {
        public static final SimpleSequenceNumberGenerator INSTANCE = new SimpleSequenceNumberGenerator();
        private long seq;

        public SimpleSequenceNumberGenerator() {
            this(ChaiConfig.getSeverApiRequestSequence());
        }

        public SimpleSequenceNumberGenerator(long seq) {
            this.seq = seq;
        }

        @Override
        public synchronized long next() {
            long seq = this.seq;
            ChaiConfig.setSeverApiRequestSequence(seq);
            this.seq++;
            return seq;
        }
    }
}
