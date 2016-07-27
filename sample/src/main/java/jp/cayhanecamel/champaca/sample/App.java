package jp.cayhanecamel.champaca.sample;

import android.app.Application;


import java.util.LinkedHashMap;

import jp.cayhanecamel.feature.sqlite.TableInfo;
import jp.cayhanecamel.util.ChampacaUtil;

public class App extends Application {


    @Override
    public void onCreate() {

        ChampacaUtil.setup(this, Const.DB_NAME, MySQLiteOpenHelper.DB_VERSION, BuildConfig.DEBUG);

        // custom display for table Column
        TableInfo tableInfo1 = new TableInfo();
        tableInfo1.tableName = "USER";
        tableInfo1.columName = "created_at";
        tableInfo1.convert = TableInfo.ConvertType.DATE;
//        tableInfo1.multiply = 1000; // for milisec

        TableInfo tableInfo2 = new TableInfo();
        tableInfo2.tableName = "USER";
        tableInfo2.columName = "updated_at";
        tableInfo2.convert = TableInfo.ConvertType.DATE;

        ChampacaUtil.addTableInfo(tableInfo1);
        ChampacaUtil.addTableInfo(tableInfo2);

        // Call onCreate
        MySQLiteOpenHelper openHelper = new MySQLiteOpenHelper(getApplicationContext());
        openHelper.getReadableDatabase();

        // Original data
        LinkedHashMap<String, String> infos = new LinkedHashMap<>();
        infos.put("session_id", "123");
        infos.put("api server", "hoge/fuga");
        ChampacaUtil.setAppInfos(infos);

        // data export setting
        ChampacaUtil.setDebugEmailAddress("hoge@fuga.fuga");
    }


}
