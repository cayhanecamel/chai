package jp.cayhanecamel.chai.sample

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class MySQLiteOpenHelper(c: Context) : SQLiteOpenHelper(c, Const.DB_NAME, null, MySQLiteOpenHelper.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {


        val DB_VERSION = 1

        internal val CREATE_TABLE = "create table USER ( " +
                "_id integer primary key autoincrement," +
                " name text," +
                " age integer," +
                " created_at integer, " +
                " updated_at integer );"
    }


}
