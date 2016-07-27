package jp.cayhanecamel.champaca.sample;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.util.AtomicFile;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;

import jp.cayhanecamel.feature.app_history.AppHistoryUtil;
import jp.cayhanecamel.feature.app_history.AppInfo;
import jp.cayhanecamel.feature.main.ChampacaMainActivity;
import jp.cayhanecamel.util.ChampacaLog;


public class MainActivity extends AppCompatActivity {

    private MySQLiteOpenHelper mySQLiteOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.jp_cayhanecamel_champaca_app_name);

        (findViewById(R.id.boot)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(getApplicationContext(), ChampacaMainActivity.class));
            }
        });

        (findViewById(R.id.add_record)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySQLiteOpenHelper = new MySQLiteOpenHelper(getApplicationContext());
                SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();

                long now = Calendar.getInstance().getTime().getTime();
                ContentValues values = new ContentValues();
                values.put("name", "hoge");
                values.put("age", "25");
                values.put("created_at", String.valueOf(now));
                values.put("updated_at", String.valueOf(now));
                db.insert("USER", null, values);
                Toast.makeText(getApplicationContext(), getString(R.string.added_record), Toast.LENGTH_SHORT).show();

            }
        });

        (findViewById(R.id.add_app_history)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChampacaLog.i("MainActivity#add_log clicked");
                Toast.makeText(getApplicationContext(), getString(R.string.added_app_history), Toast.LENGTH_SHORT).show();
            }
        });

        (findViewById(R.id.add_app_history_api_dummy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Champaca用
                AppInfo req = new AppInfo();
                req.server = "sever url";
                req.apiName = "api name";
                req.type = AppInfo.Type.WEB_API_REQUEST;


                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", 1);
                    jsonObject.put("name", "hoge");
                    jsonObject.put("address", "fuga");
                    jsonObject.put("zipcode", "moga");
                    req.json = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                long requestSeq = AppHistoryUtil.add(req);

                AppInfo res = new AppInfo();
                req.server = "sever url";
                req.apiName = "api name";
                res.type = AppInfo.Type.WEB_API_RESPONSE;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("result", 0);
                    res.json = jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                res.seq = requestSeq;
                AppHistoryUtil.add(res);

                Toast.makeText(getApplicationContext(), getString(R.string.added_app_history_api_dummy), Toast.LENGTH_SHORT).show();
            }
        });

        (findViewById(R.id.add_app_history_gcm_dummy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppInfo info = new AppInfo();
                info.type = AppInfo.Type.GCM;
                info.content = "GCM Text";
                AppHistoryUtil.add(info);


                Toast.makeText(getApplicationContext(), getString(R.string.added_app_history), Toast.LENGTH_SHORT).show();
            }
        });


        (findViewById(R.id.add_shared_prefs_dummy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean("data.boolean", true)
                        .putFloat("data.float", 33.4f)
                        .putInt("data.int", 42)
                        .putLong("data.long", System.nanoTime())
                        .putString("data.string", "SharedPreferences Default!")
                        .putStringSet("data.stringSet", new HashSet<String>(Arrays.asList(new String[]{"1st", "2nd", "3rd"})))
                        .commit();

                Toast.makeText(getApplicationContext(), getString(R.string.added_shared_prefs_dummy), Toast.LENGTH_SHORT).show();
            }
        });

        (findViewById(R.id.add_shared_prefs_named_dummy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSharedPreferences("named", MODE_PRIVATE).edit()
                        .putBoolean("data.boolean", false)
                        .putFloat("data.float", 129.3f)
                        .putInt("data.int", 2112)
                        .putLong("data.long", System.nanoTime())
                        .putString("data.string", "SharedPreferences Named!")
                        .putStringSet("data.stringSet", new HashSet<String>(Arrays.asList(new String[]{"alpha", "beta", "cupcake"})))
                        .commit();

                Toast.makeText(getApplicationContext(), getString(R.string.added_shared_prefs_named_dummy), Toast.LENGTH_SHORT).show();
            }
        });

        (findViewById(R.id.add_file_explorer_internal)).setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {

                // Files
                File dir = getFilesDir();
                writeText(new File(dir, "dummy.txt"), "This is created by Champaca for Files Directory");
                writePng(new File(dir, "dummy.png"), BitmapFactory.decodeResource(getResources(), R.drawable.jp_cayhanecamel_champaca_ic_launcher));

                // Cache
                dir = getCacheDir();
                writeText(new File(dir, "dummy.txt"), "This is created by Champaca for Cache Directory");
                writePng(new File(dir, "dummy.png"), BitmapFactory.decodeResource(getResources(), R.drawable.jp_cayhanecamel_champaca_ic_launcher));

                Toast.makeText(getApplicationContext(), getString(R.string.added_files_dummy_internal), Toast.LENGTH_SHORT).show();
            }
        });

        (findViewById(R.id.add_file_explorer_external)).setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {

                // Files
                File dir = getExternalFilesDir(null);
                writeText(new File(dir, "dummy.txt"), "This is created by Champaca for External Files Directory");
                writePng(new File(dir, "dummy.png"), BitmapFactory.decodeResource(getResources(), R.drawable.jp_cayhanecamel_champaca_ic_launcher));

                // Cache
                dir = getExternalCacheDir();
                writeText(new File(dir, "dummy.txt"), "This is created by Champaca for External Cache Directory");
                writePng(new File(dir, "dummy.png"), BitmapFactory.decodeResource(getResources(), R.drawable.jp_cayhanecamel_champaca_ic_launcher));

                Toast.makeText(getApplicationContext(), getString(R.string.added_files_dummy_external), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_boot) {
            startActivity(new Intent(getApplicationContext(), ChampacaMainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void writeText(File file, String text) {
        writeBytes(file, text.getBytes());
    }

    void writePng(File file, Bitmap bitmap) {
        AtomicFile atomicFile = new AtomicFile(file);
        FileOutputStream output = null;
        try {
            output = atomicFile.startWrite();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, output);
            atomicFile.finishWrite(output);
        } catch (Exception e) {
            if (output != null) {
                atomicFile.finishWrite(output);
            }
            atomicFile.delete();
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    void writeBytes(File file, byte[] bytes) {
        AtomicFile atomicFile = new AtomicFile(file);
        FileOutputStream output = null;
        try {
            output = atomicFile.startWrite();
            output.write(bytes);
            atomicFile.finishWrite(output);
        } catch (IOException e) {
            if (output != null) {
                atomicFile.finishWrite(output);
            }
            atomicFile.delete();
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
