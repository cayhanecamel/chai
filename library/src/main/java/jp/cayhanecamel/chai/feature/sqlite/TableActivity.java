package jp.cayhanecamel.chai.feature.sqlite;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;

import jp.cayhanecamel.chai.base.ChaiBaseActivity;
import jp.cayhanecamel.chai.data.ChaiConst;
import jp.cayhanecamel.chai.R;

public class TableActivity extends ChaiBaseActivity {

    public static final String TAG = "TableActivity";

    private int mDbVersion;

    private String mDbName;

    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jp_cayhanecamel_chai_activity_base);
        appBarLayout = (AppBarLayout) findViewById(R.id.jp_cayhanecamel_chai_header);
        ViewCompat.setElevation(appBarLayout, getResources().getDimension(R.dimen.jp_cayhanecamel_chai_toolbar_elevation));

        setupToolBar();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ChaiConst.DB_NAME)) {
                mDbVersion = ((int) savedInstanceState.getSerializable(ChaiConst.DB_VERSION));
                mDbName = ((String) savedInstanceState.getSerializable(ChaiConst.DB_NAME));

                getSupportActionBar().setTitle(getString(R.string.jp_cayhanecamel_chai_sqlite, mDbName));
            }
        } else {
            getSupportActionBar().setTitle(getString(R.string.jp_cayhanecamel_chai_sqlite_none));
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TableFragment fragment = (TableFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment == null) {
            final FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            fragment = new TableFragment();
            ft.add(R.id.main, fragment, TAG);
            ft.commit();
        }

        // Re-adding is checked in the method
        appBarLayout.addOnOffsetChangedListener(fragment);
    }

}
