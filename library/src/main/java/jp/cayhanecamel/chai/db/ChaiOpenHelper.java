package jp.cayhanecamel.chai.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import jp.cayhanecamel.chai.base.ProductInfos;
import jp.cayhanecamel.chai.util.ChaiUtil;

/**
 * <pre>
 * MyOpenHelperクラス
 * </pre>
 */
public class ChaiOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "champaca.sqlite";

    public static final int DB_VERSION = 1;

    private static final String CREATE_APP_HISTORY_TABLE =
            "CREATE TABLE app_history("
                    + "_id     INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "seq     INTEGER,"
                    + "type    TEXT NOT NULL,"
                    + "name    TEXT NOT NULL,"
                    + "content TEXT NOT NULL,"
                    + "created_at  INTEGER NOT NULL" + ");";

    private static final String CREATE_MOCK_USER =
            "CREATE TABLE user("
                    + "_id          INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "first_name   TEXT NOT NULL,"
                    + "last_name    TEXT NOT NULL,"
                    + "tel TEXT NOT NULL,"
                    + "address TEXT NOT NULL,"
                    + "mail TEXT NOT NULL,"
                    + "create_at  INTEGER NOT NULL,"
                    + "updated_at  INTEGER NOT NULL,"
                    + "deleted_at  INTEGER NOT NULL" + ");";


    private static final ChaiOpenHelper sInstance = new ChaiOpenHelper(
            ChaiUtil.getApplicationContext(), DB_NAME, null, DB_VERSION);

    public ChaiOpenHelper(Context context, String name, CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public static ChaiOpenHelper get() {
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // デバッグモードの場合のみ、デバッグ用のテーブルを用意する。
        if (ProductInfos.get().isDebug) {
            createDebugTable(db);
        }
    }

    private void createDebugTable(SQLiteDatabase db) {
        db.execSQL(CREATE_APP_HISTORY_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
