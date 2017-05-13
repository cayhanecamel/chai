package jp.cayhanecamel.chai.sample

import android.app.Application
import jp.cayhanecamel.chai.feature.sqlite.TableInfo
import jp.cayhanecamel.chai.util.ChaiUtil
import java.util.*

class App : Application() {


    override fun onCreate() {

        ChaiUtil.setup(this, Const.DB_NAME, MySQLiteOpenHelper.DB_VERSION, BuildConfig.DEBUG)

        // custom display for table Column
        val tableInfo1 = TableInfo()
        tableInfo1.tableName = "USER"
        tableInfo1.columName = "created_at"
        tableInfo1.convert = TableInfo.ConvertType.DATE
        //        tableInfo1.multiply = 1000; // for milisec

        val tableInfo2 = TableInfo()
        tableInfo2.tableName = "USER"
        tableInfo2.columName = "updated_at"
        tableInfo2.convert = TableInfo.ConvertType.DATE

        ChaiUtil.addTableInfo(tableInfo1)
        ChaiUtil.addTableInfo(tableInfo2)

        // Call onCreate
        val openHelper = MySQLiteOpenHelper(applicationContext)
        openHelper.readableDatabase

        // Original data
        val infos = LinkedHashMap<String, String>()
        infos.put("session_id", "123")
        infos.put("api server", "hoge/fuga")
        ChaiUtil.setAppInfos(infos)

        // data export setting
        ChaiUtil.setDebugEmailAddress("hoge@fuga.fuga")
    }


}
