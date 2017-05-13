package jp.cayhanecamel.chai.feature.sqlite;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import jp.cayhanecamel.chai.base.ChaiBaseActivity;
import jp.cayhanecamel.chai.R;

public class FilterActivity extends ChaiBaseActivity {

    public static final String TAG = "TableActivity";

    private TableFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jp_cayhanecamel_chai_activity_base);
        setupToolBar();

        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            fragment = new TableFragment();
            ft.add(R.id.main, fragment, TAG);
            ft.commit();
        }
    }

}
