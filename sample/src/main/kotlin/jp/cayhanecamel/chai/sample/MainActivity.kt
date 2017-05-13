package jp.cayhanecamel.chai.sample

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.util.AtomicFile
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import jp.cayhanecamel.chai.feature.app_history.AppHistoryUtil
import jp.cayhanecamel.chai.feature.app_history.AppInfo
import jp.cayhanecamel.chai.feature.main.ChaiMainActivity
import jp.cayhanecamel.chai.util.ChaiLog
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {

    private var mySQLiteOpenHelper: MySQLiteOpenHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.jp_cayhanecamel_chai_app_name)

        findViewById(R.id.boot).setOnClickListener { this@MainActivity.startActivity(Intent(applicationContext, ChaiMainActivity::class.java)) }

        findViewById(R.id.add_record).setOnClickListener {
            mySQLiteOpenHelper = MySQLiteOpenHelper(applicationContext)
            val db = mySQLiteOpenHelper!!.writableDatabase

            val now = Calendar.getInstance().time.time
            val values = ContentValues()
            values.put("name", "hoge")
            values.put("age", "25")
            values.put("created_at", now.toString())
            values.put("updated_at", now.toString())
            db.insert("USER", null, values)
            Toast.makeText(applicationContext, getString(R.string.added_record), Toast.LENGTH_SHORT).show()
        }

        findViewById(R.id.add_app_history).setOnClickListener {
            ChaiLog.i("MainActivity#add_log clicked")
            Toast.makeText(applicationContext, getString(R.string.added_app_history), Toast.LENGTH_SHORT).show()
        }

        findViewById(R.id.add_app_history_api_dummy).setOnClickListener {
            // Chai用
            val req = AppInfo()
            req.server = "sever url"
            req.apiName = "api name"
            req.type = AppInfo.Type.WEB_API_REQUEST


            try {
                val jsonObject = JSONObject()
                jsonObject.put("id", 1)
                jsonObject.put("name", "hoge")
                jsonObject.put("address", "fuga")
                jsonObject.put("zipcode", "moga")
                req.json = jsonObject.toString()
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val requestSeq = AppHistoryUtil.add(req)

            val res = AppInfo()
            req.server = "sever url"
            req.apiName = "api name"
            res.type = AppInfo.Type.WEB_API_RESPONSE
            try {
                val jsonObject = JSONObject()
                jsonObject.put("result", 0)
                res.json = jsonObject.toString()
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            res.seq = requestSeq
            AppHistoryUtil.add(res)

            Toast.makeText(applicationContext, getString(R.string.added_app_history_api_dummy), Toast.LENGTH_SHORT).show()
        }

        findViewById(R.id.add_app_history_gcm_dummy).setOnClickListener {
            val info = AppInfo()
            info.type = AppInfo.Type.GCM
            info.content = "GCM Text"
            AppHistoryUtil.add(info)


            Toast.makeText(applicationContext, getString(R.string.added_app_history), Toast.LENGTH_SHORT).show()
        }


        findViewById(R.id.add_shared_prefs_dummy).setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
                    .putBoolean("data.boolean", true)
                    .putFloat("data.float", 33.4f)
                    .putInt("data.int", 42)
                    .putLong("data.long", System.nanoTime())
                    .putString("data.string", "SharedPreferences Default!")
                    .putStringSet("data.stringSet", HashSet(Arrays.asList(*arrayOf("1st", "2nd", "3rd"))))
                    .commit()

            Toast.makeText(applicationContext, getString(R.string.added_shared_prefs_dummy), Toast.LENGTH_SHORT).show()
        }

        findViewById(R.id.add_shared_prefs_named_dummy).setOnClickListener {
            getSharedPreferences("named", Context.MODE_PRIVATE).edit()
                    .putBoolean("data.boolean", false)
                    .putFloat("data.float", 129.3f)
                    .putInt("data.int", 2112)
                    .putLong("data.long", System.nanoTime())
                    .putString("data.string", "SharedPreferences Named!")
                    .putStringSet("data.stringSet", HashSet(Arrays.asList(*arrayOf("alpha", "beta", "cupcake"))))
                    .commit()

            Toast.makeText(applicationContext, getString(R.string.added_shared_prefs_named_dummy), Toast.LENGTH_SHORT).show()
        }

        findViewById(R.id.add_file_explorer_internal).setOnClickListener {
            // Files
            var dir = filesDir
            writeText(File(dir, "dummy.txt"), "This is created by Chai for Files Directory")
            writePng(File(dir, "dummy.png"), BitmapFactory.decodeResource(resources, R.drawable.jp_cayhanecamel_chai_ic_launcher))

            // Cache
            dir = cacheDir
            writeText(File(dir, "dummy.txt"), "This is created by Chai for Cache Directory")
            writePng(File(dir, "dummy.png"), BitmapFactory.decodeResource(resources, R.drawable.jp_cayhanecamel_chai_ic_launcher))

            Toast.makeText(applicationContext, getString(R.string.added_files_dummy_internal), Toast.LENGTH_SHORT).show()
        }

        findViewById(R.id.add_file_explorer_external).setOnClickListener {
            // Files
            var dir: File = getExternalFilesDir(null)
            writeText(File(dir, "dummy.txt"), "This is created by Chai for External Files Directory")
            writePng(File(dir, "dummy.png"), BitmapFactory.decodeResource(resources, R.drawable.jp_cayhanecamel_chai_ic_launcher))

            // Cache
            dir = externalCacheDir
            writeText(File(dir, "dummy.txt"), "This is created by Chai for External Cache Directory")
            writePng(File(dir, "dummy.png"), BitmapFactory.decodeResource(resources, R.drawable.jp_cayhanecamel_chai_ic_launcher))

            Toast.makeText(applicationContext, getString(R.string.added_files_dummy_external), Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_boot) {
            startActivity(Intent(applicationContext, ChaiMainActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    internal fun writeText(file: File, text: String) {
        writeBytes(file, text.toByteArray())
    }

    internal fun writePng(file: File, bitmap: Bitmap) {
        val atomicFile = AtomicFile(file)
        var output: FileOutputStream? = null
        try {
            output = atomicFile.startWrite()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, output)
            atomicFile.finishWrite(output)
        } catch (e: Exception) {
            if (output != null) {
                atomicFile.finishWrite(output)
            }
            atomicFile.delete()
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

    }

    internal fun writeBytes(file: File, bytes: ByteArray) {
        val atomicFile = AtomicFile(file)
        var output: FileOutputStream? = null
        try {
            output = atomicFile.startWrite()
            output!!.write(bytes)
            atomicFile.finishWrite(output)
        } catch (e: IOException) {
            if (output != null) {
                atomicFile.finishWrite(output)
            }
            atomicFile.delete()
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }

    }
}
