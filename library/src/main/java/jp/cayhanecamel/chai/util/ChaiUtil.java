package jp.cayhanecamel.chai.util;

import android.content.Context;

import jp.cayhanecamel.chai.base.DbInfo;
import jp.cayhanecamel.chai.base.ProductInfos;
import jp.cayhanecamel.chai.data.provider.AppHistory;
import jp.cayhanecamel.chai.db.ChaiOpenHelper;
import jp.cayhanecamel.chai.feature.sqlite.TableInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ChaiUtil {

    private static Context sContext;

    public static final void setup(Context context, boolean isDebug) {
        setup(context, "", -1, isDebug);
    }

    public static final void setup(Context context, String dbName,
                                   int dbVersion, boolean isDebug) {
        List<DbInfo> dbInfos = new ArrayList<>();
        dbInfos.add(new DbInfo(dbName, dbVersion));
        setup(context, dbInfos, isDebug, context.getPackageName());
    }

    public static final void setup(Context context, List<DbInfo> dbInfos, boolean isDebug, String packageName) {
        sContext = context;
        ProductInfos.get().dbInfos = dbInfos;
        ProductInfos.get().isDebug = isDebug;
        ChaiOpenHelper.get();
        AppHistory.setPackageName(packageName);
    }


    public static final void setup(Context context, List<DbInfo> dbInfos, boolean isDebug) {
        setup(context, dbInfos, isDebug, context.getPackageName());
    }

    public static final void setAppInfos(LinkedHashMap<String, String> infos) {
        ProductInfos.get().infos = infos;
    }

    public static final void setDebugEmailAddress(String debugMailAddress) {
        ProductInfos.get().debugMailAddress = debugMailAddress;
    }


    public static final Context getApplicationContext() {
        return sContext;
    }

    public static final void addTableInfo(TableInfo tableInfo) {
        ProductInfos.get().tableInfos.add(tableInfo);
    }

}
