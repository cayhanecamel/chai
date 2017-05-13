package jp.cayhanecamel.chai.feature.app_history;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import jp.cayhanecamel.chai.base.ChaiBaseActivity;
import jp.cayhanecamel.chai.R;

public class AppHistoryActivity extends ChaiBaseActivity {

    public static final String TAG = "AppHistoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jp_cayhanecamel_chai_activity_base);

        setupToolBar();
        getSupportActionBar().setTitle(getString(R.string.jp_cayhanecamel_chai_app_history));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            AppHistoryFragment fragment = new AppHistoryFragment();
            ft.add(R.id.main, fragment, TAG);
            ft.commit();
        }
    }

}
