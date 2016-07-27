package jp.cayhanecamel.feature.sqlite;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import jp.cayhanecamel.base.ChampacaBaseActivity;
import jp.cayhanecamel.champaca.R;

public class FilterActivity extends ChampacaBaseActivity {

    public static final String TAG = "TableActivity";

    private TableFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jp_cayhanecamel_champaca_activity_base);
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
