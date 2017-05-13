package jp.cayhanecamel.champaca.feature.main;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import jp.cayhanecamel.champaca.base.CacheFragmentStatePagerAdapter;
import jp.cayhanecamel.champaca.base.ChampacaBaseActivity;
import jp.cayhanecamel.champaca.base.DbInfo;
import jp.cayhanecamel.champaca.base.ProductInfos;
import jp.cayhanecamel.champaca.data.ChampacaConfig;
import jp.cayhanecamel.champaca.data.ChampacaConst;
import jp.cayhanecamel.champaca.feature.app_history.AppHistoryFragment;
import jp.cayhanecamel.champaca.feature.file_explorer.HomeFragment;
import jp.cayhanecamel.champaca.util.ChampacaUtil;
import jp.cayhanecamel.champaca.R;

public class ChampacaMainActivity extends ChampacaBaseActivity {

    public static final String TAG = "ChampacaMainActivity";

    private AppBarLayout mHeaderView;
    private ViewPager mPager;
    private NavigationAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jp_cayhanecamel_champaca_activity_main);

        setupToolBar();
        getSupportActionBar().setTitle("Champaca Debug Menu");

        mHeaderView = (AppBarLayout) findViewById(R.id.jp_cayhanecamel_champaca_header);
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.jp_cayhanecamel_champaca_toolbar_elevation));
        mPagerAdapter = new NavigationAdapter(getSupportFragmentManager(), getApplicationContext(), mHeaderView);
        mPager = (ViewPager) findViewById(R.id.jp_cayhanecamel_champaca_pager);
        mPager.setAdapter(mPagerAdapter);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.jp_cayhanecamel_champaca_tab_indicator, android.R.id.text1);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.jp_cayhanecamel_champaca_tab_accent));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mPager);

        int lastSelectTabIndex = ChampacaConfig.getLastSelectTabIndex();
        if (lastSelectTabIndex < mPagerAdapter.getCount()) {
            mPager.setCurrentItem(lastSelectTabIndex);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChampacaConfig.setLastSelectTabIndex(mPager.getCurrentItem());
    }

    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private List<String> mTitles;
        private List<Fragment> mFragments;

        public NavigationAdapter(FragmentManager fm, Context context, AppBarLayout appBarLayout) {
            super(fm);

            mTitles = new ArrayList<>();
            mFragments = new ArrayList<>();

            mTitles.add(context.getString(R.string.jp_cayhanecamel_champaca_app_history));
            putFragment(new AppHistoryFragment(), appBarLayout);


            mTitles.add(context.getString(R.string.jp_cayhanecamel_champaca_app_info));
            putFragment(new AppInfoFragment(), appBarLayout);


            mTitles.add(context.getString(R.string.jp_cayhanecamel_champaca_shared_prefs));
            if (SharedPrefsRootFragment.getDirectories().size() > 0) {
                putFragment(new SharedPrefsRootFragment(), appBarLayout);
            } else {
                BlankFragment fragment = new BlankFragment();
                Bundle bundle = new Bundle();
                bundle.putString(BlankFragment.INFO, ChampacaUtil.getApplicationContext().getString(R.string.jp_cayhanecamel_champaca_shared_prefs_not_use));
                fragment.setArguments(bundle);
                putFragment(fragment, appBarLayout);
            }


            mTitles.add(context.getString(R.string.jp_cayhanecamel_champaca_file_explorer));
            putFragment(new HomeFragment(), appBarLayout);


            for (DbInfo info : ProductInfos.get().dbInfos) {
                if (TextUtils.isEmpty(info.name)) {
                    mTitles.add(context.getString(R.string.jp_cayhanecamel_champaca_sqlite_none));

                    BlankFragment fragment = new BlankFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(BlankFragment.INFO, ChampacaUtil.getApplicationContext().getString(R.string.jp_cayhanecamel_champaca_sqlite_not_specified));
                    fragment.setArguments(bundle);
                    putFragment(fragment, appBarLayout);

                } else {
                    mTitles.add(context.getString(R.string.jp_cayhanecamel_champaca_sqlite, info.name));

                    TableInfoFragment fragment = new TableInfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(ChampacaConst.DB_NAME, info.name);
                    bundle.putInt(ChampacaConst.DB_VERSION, info.version);
                    fragment.setArguments(bundle);
                    putFragment(fragment, appBarLayout);
                }
            }

            mTitles.add(context.getString(R.string.jp_cayhanecamel_champaca_sqlite, ChampacaConst.CHAMPACA));

            TableInfoFragment fragment = new TableInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ChampacaConst.DB_NAME, ChampacaConst.CHAMPACA);
            bundle.putInt(ChampacaConst.DB_VERSION, 1);
            fragment.setArguments(bundle);
            putFragment(fragment, appBarLayout);

        }

        private void putFragment(Fragment fragment, AppBarLayout appBarLayout) {
            mFragments.add(fragment);
            if (fragment instanceof AppBarLayout.OnOffsetChangedListener) {
                appBarLayout.addOnOffsetChangedListener((AppBarLayout.OnOffsetChangedListener) fragment);
            }
        }

        @Override
        protected Fragment createItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }

}
