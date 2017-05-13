package jp.cayhanecamel.champaca.feature.app_history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jp.cayhanecamel.champaca.base.ChampacaBaseActivity;
import jp.cayhanecamel.champaca.data.ChampacaConfig;
import jp.cayhanecamel.champaca.data.provider.AppHistory;
import jp.cayhanecamel.champaca.util.ChampacaUtil;
import jp.cayhanecamel.champaca.widget.PartsEditView;
import jp.cayhanecamel.champaca.widget.PartsSelectView;
import jp.cayhanecamel.champaca.R;

public class AppHistorySettingActivity extends ChampacaBaseActivity {


    private PartsSelectView typeView;

    private PartsSelectView nameView;

    private PartsEditView limitView;

    private static Map<String, Boolean> mTypeSelectMap = new LinkedHashMap<String, Boolean>();

    private static Map<String, Boolean> mNameSelectMap = new LinkedHashMap<String, Boolean>();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.jp_cayhanecamel_champaca_activity_app_history_setting);

        setupToolBar();
        getSupportActionBar().setTitle(getString(R.string.jp_cayhanecamel_champaca_app_history_filter));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        typeView = (PartsSelectView) findViewById(R.id.jp_cayhanecamel_champaca_app_history_type);
        typeView.title.setText(R.string.jp_cayhanecamel_champaca_type);
        typeView.value.setText(ChampacaConfig.getAppHistoryTypeFilter());
        typeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTypeFilter();
            }
        });

        nameView = (PartsSelectView) findViewById(R.id.jp_cayhanecamel_champaca_app_history_name);
        nameView.title.setText(R.string.jp_cayhanecamel_champaca_name);
        nameView.value.setText(ChampacaConfig.getAppHistoryNameFilter());
        nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNameFilter();
            }
        });

        limitView = (PartsEditView) findViewById(R.id.jp_cayhanecamel_champaca_app_history_limit);
        limitView.title.setText(R.string.jp_cayhanecamel_champaca_limit);
        limitView.value.setInputType(InputType.TYPE_CLASS_NUMBER);

        limitView.value.setText(String.valueOf(ChampacaConfig.getAppHistoryLimit()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.jp_cayhanecamel_champaca_menu_app_history_filter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.jp_cayhanecamel_champaca_done) {
            setCondition();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void setCondition() {

        ChampacaConfig.setAppHistoryTypeFilter(typeView.value.getText().toString());

        ChampacaConfig.setAppHistoryNameFilter(nameView.value.getText().toString());

        ChampacaConfig.setAppHistoryLimit(Integer.parseInt(limitView.value.getText()
                .toString()));
    }

    private void showTypeFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.jp_cayhanecamel_champaca_type);

        Cursor cursor = getApplicationContext().getContentResolver().query(
                AppHistory.CONTENT_URI_DISTINCT,
                new String[]{
                        AppHistory.COLUMN_TYPE
                }, null, null,
                AppHistory.COLUMN_TYPE + " ASC ");

        if (cursor.getCount() == 0) {
            Toast.makeText(ChampacaUtil.getApplicationContext(),
                    R.string.jp_cayhanecamel_champaca_no_app_history, Toast.LENGTH_LONG).show();
            return;
        }

        List<String> selectType = Arrays.asList(typeView.value.getText()
                .toString().split(","));

        List<String> typeList = new LinkedList<String>();
        List<Boolean> selectList = new LinkedList<Boolean>();

        while (cursor.moveToNext()) {
            String type = (cursor.getString(cursor.getColumnIndex("type")));
            if (!typeList.contains(type)) {
                typeList.add(type);
                selectList.add(selectType.contains(type));
            }
        }

        mTypeSelectMap.clear();
        for (int i = 0; i < typeList.size(); i++) {
            mTypeSelectMap.put(typeList.get(i), selectList.get(i));
        }

        String[] items = typeList.toArray(new String[0]);
        boolean[] check = new boolean[selectList.size()];
        for (int i = 0; i < selectList.size(); i++) {
            check[i] = selectList.get(i);
        }

        builder.setMultiChoiceItems(items, check, mTypeCheckListener);
        builder.setPositiveButton(R.string.jp_cayhanecamel_champaca_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder builder = new StringBuilder();
                Iterator<String> iteratorKey = mTypeSelectMap.keySet()
                        .iterator();
                while (iteratorKey.hasNext()) {
                    String key = iteratorKey.next();
                    if (mTypeSelectMap.get(key)) {
                        if (builder.length() > 0) {
                            builder.append(",");
                        }
                        builder.append(key);
                    }
                }
                String value = builder.toString();
                if (value.equals("")) {
                    value = AppHistoryFragment.getNoFilter();
                }
                typeView.value.setText(value);
            }
        });

        builder.setNegativeButton(R.string.jp_cayhanecamel_champaca_deselect_all, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Iterator<String> keys = mTypeSelectMap.keySet().iterator();
                while (keys.hasNext()) {
                    String type = keys.next();
                    mTypeSelectMap.put(type, false);
                }
                typeView.value.setText(AppHistoryFragment.getNoFilter());
            }
        });


        builder.setNeutralButton(R.string.jp_cayhanecamel_champaca_close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showNameFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.jp_cayhanecamel_champaca_name);

        Cursor cursor = getApplicationContext().getContentResolver().query(
                AppHistory.CONTENT_URI_DISTINCT,
                new String[]{
                        AppHistory.COLUMN_NAME
                }, null, null,
                AppHistory.COLUMN_NAME + " ASC ");

        if (cursor.getCount() == 0) {
            Toast.makeText(ChampacaUtil.getApplicationContext(),
                    R.string.jp_cayhanecamel_champaca_no_app_history, Toast.LENGTH_LONG).show();
            return;
        }

        List<String> selectType = Arrays.asList(nameView.value.getText()
                .toString().split(","));

        List<String> nameList = new LinkedList<String>();
        List<Boolean> selectList = new LinkedList<Boolean>();

        while (cursor.moveToNext()) {
            String name = (cursor.getString(cursor.getColumnIndex("name")));
            if (!nameList.contains(name)) {
                nameList.add(name);
                selectList.add(selectType.contains(name));
            }
        }

        mNameSelectMap.clear();
        for (int i = 0; i < nameList.size(); i++) {
            mNameSelectMap.put(nameList.get(i), selectList.get(i));
        }

        String[] items = nameList.toArray(new String[0]);
        boolean[] check = new boolean[selectList.size()];
        for (int i = 0; i < selectList.size(); i++) {
            check[i] = selectList.get(i);
        }

        builder.setMultiChoiceItems(items, check, mNameCheckListener);
        builder.setPositiveButton(R.string.jp_cayhanecamel_champaca_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder builder = new StringBuilder();
                Iterator<String> iteratorKey = mNameSelectMap.keySet()
                        .iterator();
                while (iteratorKey.hasNext()) {
                    String key = iteratorKey.next();
                    if (mNameSelectMap.get(key)) {
                        if (builder.length() > 0) {
                            builder.append(",");
                        }
                        builder.append(key);
                    }
                }
                String value = builder.toString();
                if (value.equals("")) {
                    value = AppHistoryFragment.getNoFilter();
                }
                nameView.value.setText(value);
            }
        });

        builder.setNegativeButton(R.string.jp_cayhanecamel_champaca_deselect_all, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Iterator<String> keys = mNameSelectMap.keySet().iterator();
                while (keys.hasNext()) {
                    String type = keys.next();
                    mNameSelectMap.put(type, false);
                }
                nameView.value.setText(AppHistoryFragment.getNoFilter());
            }
        });

        builder.setNeutralButton(R.string.jp_cayhanecamel_champaca_close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    DialogInterface.OnMultiChoiceClickListener mTypeCheckListener = new DialogInterface.OnMultiChoiceClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {

            mTypeSelectMap.put(
                    (String) mTypeSelectMap.keySet().toArray()[which],
                    isChecked);
        }
    };

    DialogInterface.OnMultiChoiceClickListener mNameCheckListener = new DialogInterface.OnMultiChoiceClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {

            mNameSelectMap.put(
                    (String) mNameSelectMap.keySet().toArray()[which],
                    isChecked);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setCondition();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
