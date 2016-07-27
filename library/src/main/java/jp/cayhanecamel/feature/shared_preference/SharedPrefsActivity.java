package jp.cayhanecamel.feature.shared_preference;

import android.os.Bundle;

import jp.cayhanecamel.base.ChampacaBaseActivity;
import jp.cayhanecamel.champaca.R;

public class SharedPrefsActivity extends ChampacaBaseActivity {

    public static final String TAG = "SharedPrefsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jp_cayhanecamel_champaca_activity_base);

        setupToolBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.jp_cayhanecamel_champaca_shared_prefs);

        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            SharedPrefsFragment fragment = new SharedPrefsFragment();
            fragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main, fragment, TAG)
                    .commit();
        }
    }

}
