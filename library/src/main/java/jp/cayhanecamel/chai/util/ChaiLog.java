package jp.cayhanecamel.chai.util;

import android.util.Log;

import jp.cayhanecamel.chai.feature.app_history.AppHistoryAsync;
import jp.cayhanecamel.chai.feature.app_history.AppInfo;
import jp.cayhanecamel.chai.feature.app_history.AppInfo.Type;

public class ChaiLog {

    public static void v(String msg) {
        addAppHistory(msg, Type.LOG_V);
    }

    public static void d(String msg) {
        addAppHistory(msg, Type.LOG_D);
    }

    public static void i(String msg) {
        addAppHistory(msg, Type.LOG_I);
    }

    public static void w(String msg) {
        addAppHistory(msg, Type.LOG_W);
    }

    public static void e(String msg) {
        addAppHistory(msg, Type.LOG_E);
    }

    /**
     * Throwable print
     */
    public static void e(Throwable e) {
        StringBuilder stb = new StringBuilder();
        for (StackTraceElement s : e.getStackTrace()) {
            stb.append(s.toString());
            stb.append(System.getProperty("file.separator"));
        }
        e(stb.toString());
    }

    public static void println(int priority, String msg) {
        if (priority == Log.VERBOSE) {
            v(msg);

        } else if (priority == Log.DEBUG) {
            d(msg);

        } else if (priority == Log.INFO) {
            i(msg);

        } else if (priority == Log.WARN) {
            w(msg);

        } else if (priority == Log.ERROR) {
            e(msg);

        }
    }

    private static void addAppHistory(final String msg, final Type type) {
        AppInfo info = new AppInfo();
        info.type = type;
        info.content = msg;
        AppHistoryAsync.getInstance().add(info).start();
    }

}
