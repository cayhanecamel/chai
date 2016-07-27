package jp.cayhanecamel.champaca.sample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySQLiteOpenHelper extends SQLiteOpenHelper {


    public final static int DB_VERSION = 1;

    static final String CREATE_TABLE = "create table USER ( " +
            "_id integer primary key autoincrement," +
            " name text," +
            " age integer," +
            " created_at integer, " +
            " updated_at integer );";

    public MySQLiteOpenHelper(Context c) {
        super(c, Const.DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}
