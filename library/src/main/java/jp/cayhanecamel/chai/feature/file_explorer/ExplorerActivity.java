package jp.cayhanecamel.chai.feature.file_explorer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import jp.cayhanecamel.chai.base.ChaiBaseActivity;
import jp.cayhanecamel.chai.R;

public class ExplorerActivity extends ChaiBaseActivity implements ExplorerContextHolder {

    private ExplorerContext mExplorerContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jp_cayhanecamel_chai_activity_explorer);

        setupToolBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.jp_cayhanecamel_chai_file_explorer);

        Fragment fragment;
        Intent intent = getIntent();
        if (intent.hasExtra(ExplorerFragment.ARG_DIR_NAME)) {
            fragment = ExplorerFragment.newInstance(intent.getExtras());
        } else {
            fragment = new HomeFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main, fragment)
                .commit();
    }

    @Override
    public ExplorerContext getExplorerContext() {
        if (mExplorerContext == null) {
            mExplorerContext = new ExplorerContext();
        }

        return mExplorerContext;
    }

    public static Intent createIntentForDirectly(Context context, String path, String displayName) {
        return new Intent(context, ExplorerActivity.class)
                .putExtra(ExplorerFragment.ARG_DIR_NAME, path)
                .putExtra(ExplorerFragment.ARG_DISPLAY_NAME, displayName);
    }
}
