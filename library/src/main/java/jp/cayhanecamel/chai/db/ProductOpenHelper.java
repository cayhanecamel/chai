package jp.cayhanecamel.champaca.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <pre>
 * ProductOpenHelperクラス
 * </pre>
 */
public class ProductOpenHelper extends SQLiteOpenHelper {

    public ProductOpenHelper(Context context, String name,
                             CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

//    public static boolean exists() {
//        return sInstance == null ? false : true;
//    }

//    public static ProductOpenHelper get(String dbName, int dbVersion) {
//        if (sInstance == null) {
//            sInstance = new ProductOpenHelper(
//                    ChampacaUtil.getApplicationContext(),
//                    dbName,
//                    null,
//                    dbVersion);
//        }
//        return sInstance;
//    }

    private static ProductOpenHelper sInstance = null;

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
