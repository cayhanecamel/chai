package jp.cayhanecamel.champaca.feature.app_history;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.cayhanecamel.champaca.base.ProductInfos;
import jp.cayhanecamel.champaca.base.recycler.CollectionRecyclerAdapter;
import jp.cayhanecamel.champaca.base.recycler.CursorRecyclerAdapter;
import jp.cayhanecamel.champaca.base.recycler.ItemBindable;
import jp.cayhanecamel.champaca.base.recycler.PositionBindableViewHolder;
import jp.cayhanecamel.champaca.data.ChampacaConfig;
import jp.cayhanecamel.champaca.data.provider.AppHistory;
import jp.cayhanecamel.champaca.util.ChampacaLog;
import jp.cayhanecamel.champaca.util.ChampacaUtil;
import jp.cayhanecamel.champaca.R;


public class AppHistoryFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    private String mCondtion;

    FloatingActionsMenu mMultipleActions;

    private View mEmpty;

    AppHistoryAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.jp_cayhanecamel_champaca_fragment_app_history, container, false);

        mMultipleActions = (FloatingActionsMenu) view.findViewById(R.id.jp_cayhanecamel_champaca_multiple_actions);
        FloatingActionButton filter = (FloatingActionButton) view.findViewById(R.id.jp_cayhanecamel_champaca_filter);
        FloatingActionButton export = (FloatingActionButton) view.findViewById(R.id.jp_cayhanecamel_champaca_export);
        FloatingActionButton delete = (FloatingActionButton) view.findViewById(R.id.jp_cayhanecamel_champaca_delete);
        RecyclerView listView = (RecyclerView) view.findViewById(R.id.jp_cayhanecamel_champaca_scroll);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new AppHistoryAdapter(getActivity().getApplicationContext());

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter();
                mMultipleActions.toggle();
            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export();
                mMultipleActions.toggle();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
                mMultipleActions.toggle();
            }
        });

        mEmpty = view.findViewById(R.id.jp_cayhanecamel_champaca_empty);

        listView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AppHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                Cursor cursor = mAdapter.getItem(position);
                final String message = cursor.getString(cursor.getColumnIndex("content"));

                new AlertDialog.Builder(getActivity())
                        .setMessage(message)
                        .setPositiveButton(R.string.jp_cayhanecamel_champaca_close,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                        .setNeutralButton(R.string.jp_cayhanecamel_champaca_share,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        String[] addresses = {
                                                ProductInfos.get().debugMailAddress
                                        };
                                        ShareCompat.IntentBuilder
                                                .from(getActivity())
                                                .setChooserTitle("Choose App")
                                                .setEmailTo(addresses)
                                                .setSubject("Application History")
                                                .setText(message)
                                                .setType("text/plain")
                                                .startChooser();
                                    }
                                }).show();

            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(1, null, mAppHistoryCallback);

    }


    private void filter() {
        Intent i = new Intent(getActivity(), AppHistorySettingActivity.class);
        startActivityForResult(i, 1);
    }

    private void export() {
        Cursor query = ChampacaUtil
                .getApplicationContext()
                .getContentResolver()
                .query(AppHistory.CONTENT_URI, null, null, null,
                        AppHistory.COLUMN_CREATED_AT + " DESC ");

        if (!query.moveToFirst()) {
            query.close();
            return;
        }

        File file = new File(Environment.getExternalStorageDirectory(), "app_hisotry.txt");

        try {
            file.delete();
            Writer writer = new FileWriter(file);

            try {
                SimpleDateFormat sim = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss.SSS", Locale.US);
                while (query.moveToNext()) {
                    Date date = new Date(query.getLong(query.getColumnIndex("created_at")));

                    writer.append(sim.format(date) + " ");
                    writer.append(query.getString(query.getColumnIndex("content")));
                    writer.append(System.getProperty("line.separator"));
                }
            } finally {
                writer.close();
            }
        } catch (IOException e) {
            ChampacaLog.e(e);
        } finally {
            query.close();
        }

        String[] addresses = {
                ProductInfos.get().debugMailAddress
        };
        ShareCompat.IntentBuilder.from(getActivity())
                .setChooserTitle("Choose App")
                .setEmailTo(addresses)
                .setSubject("Application History Logs")
                .setText("See attachments")
                .setType("text/plain")
                .setStream(Uri.fromFile(file)).startChooser();
    }

    private void delete() {
        ChampacaUtil.getApplicationContext().getContentResolver()
                .delete(AppHistory.CONTENT_URI, null, null);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mAppHistoryCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

            StringBuilder selection = new StringBuilder();
            String[] selectionArgs = null;

            List<String> selectLikeList = null;
            List<String> selectTypeList = null;
            List<String> selectNameList = null;

            if (mCondtion != null && mCondtion.length() > 0) {
                selection.append(AppHistory.COLUMN_CONTENT + " LIKE ? ");
                selectLikeList = new ArrayList<>();
                selectLikeList.add("%" + mCondtion + "%");
            }

            if (!ChampacaConfig.getAppHistoryTypeFilter().equals(getNoFilter())) {
                selectTypeList = Arrays.asList(ChampacaConfig.getAppHistoryTypeFilter().split(","));

                StringBuilder typesPrepare = new StringBuilder();
                for (String type : selectTypeList) {
                    if (typesPrepare.length() > 0) {
                        typesPrepare.append(",");
                    }
                    typesPrepare.append("?");
                }

                if (selectTypeList.size() > 0) {
                    if (selection.length() > 0) {
                        selection.append(" AND ");
                    }

                    selection.append(AppHistory.COLUMN_TYPE + " IN (" + typesPrepare.toString() + ")");
                    if (selectionArgs != null) {
                        selectTypeList.add(0, selectionArgs[0]);
                    }
                }
            }

            if (!ChampacaConfig.getAppHistoryNameFilter().equals(getNoFilter())) {
                selectNameList = Arrays.asList(ChampacaConfig.getAppHistoryNameFilter().split(","));

                StringBuilder namesPrepare = new StringBuilder();
                for (String name : selectNameList) {
                    if (namesPrepare.length() > 0) {
                        namesPrepare.append(",");
                    }
                    namesPrepare.append("?");
                }

                if (selectNameList.size() > 0) {
                    if (selection.length() > 0) {
                        selection.append(" AND ");
                    }

                    selection.append(AppHistory.COLUMN_NAME + " IN (" + namesPrepare.toString() + ")");
                }
            }

            List<String> selectionArgsList = new ArrayList<>();

            if (selectLikeList != null) {
                selectionArgsList.addAll(selectLikeList);
            }

            if (selectTypeList != null) {
                selectionArgsList.addAll(selectTypeList);
            }

            if (selectNameList != null) {
                selectionArgsList.addAll(selectNameList);
            }

            selectionArgs = selectionArgsList.toArray(new String[selectionArgsList.size()]);

            return new CursorLoader(ChampacaUtil.getApplicationContext(),
                    AppHistory.CONTENT_URI, null, selection.toString(),
                    selectionArgs,
                    AppHistory.COLUMN_ID + " DESC LIMIT " + ChampacaConfig.getAppHistoryLimit());
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            mAdapter.swapCursor(cursor);
            mEmpty.setVisibility(cursor == null || cursor.getCount() == 0 ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }

    };

    private static class AppHistoryAdapter extends CursorRecyclerAdapter<AppHistoryItemView> implements View.OnClickListener {

        private final Context mContext;
        private OnItemClickListener onItemClickListener;

        public AppHistoryAdapter(Context context) {
            this.mContext = context;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        protected AppHistoryItemView newView(int viewType) {
            AppHistoryItemView itemView = new AppHistoryItemView(mContext);
            itemView.setOnClickListener(this);
            return itemView;
        }

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag(R.id.jp_cayhanecamel_champaca_tag_position);
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, position, v.getId());
            }
        }

        public void setHeaderView(final View headerView) {
            setHeaderAdapter(new CollectionRecyclerAdapter.HeaderFooterAdapter() {
                @Override
                public int size() {
                    return 1;
                }

                @Override
                public int getViewType(int position) {
                    return 0;
                }

                @Override
                public PositionBindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return new PositionBindableViewHolder(headerView) {
                        @Override
                        public void bind(int position) {
                        }
                    };
                }
            });
        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position, long id);
        }
    }

    private static class AppHistoryItemView extends LinearLayout implements ItemBindable<Cursor> {

        public AppHistoryItemView(Context context) {
            super(context);
            inflate(context, R.layout.jp_cayhanecamel_champaca_list_item_app_history, this);
        }

        @Override
        public void bind(Cursor cursor) {
            View view = this;
            view.setTag(R.id.jp_cayhanecamel_champaca_tag_position, cursor.getPosition());
            ImageView icon = (ImageView) view.findViewById(R.id.jp_cayhanecamel_champaca_icon);

            TextView txtType = (TextView) view.findViewById(R.id.jp_cayhanecamel_champaca_type);
            TextView txtName = (TextView) view.findViewById(R.id.jp_cayhanecamel_champaca_name);
            TextView txtCreateDate = (TextView) view.findViewById(R.id.jp_cayhanecamel_champaca_createdAt);
            TextView txtContent = (TextView) view.findViewById(R.id.jp_cayhanecamel_champaca_content);

            AppInfo.Type type = AppInfo.Type.enumOf(cursor.getString(cursor.getColumnIndex("type")));

            if (type == AppInfo.Type.WEB_API_REQUEST || type == AppInfo.Type.WEB_VIEW_REQUEST) {
                icon.setBackgroundResource(R.drawable.jp_cayhanecamel_champaca_ic_request);

            } else if (type == AppInfo.Type.WEB_API_RESPONSE
                    || type == AppInfo.Type.WEB_VIEW_RESPONSE) {
                icon.setBackgroundResource(R.drawable.jp_cayhanecamel_champaca_ic_response);

            } else if (type == AppInfo.Type.GCM) {
                icon.setBackgroundResource(R.drawable.jp_cayhanecamel_champaca_ic_gcm);

            } else {
                icon.setBackgroundResource(R.drawable.jp_cayhanecamel_champaca_ic_local);
            }

            txtType.setText(cursor.getString(cursor.getColumnIndex("type")));

            long seq = cursor.getLong(cursor.getColumnIndex("seq"));
            if (seq > 0) {
                txtName.setText(cursor.getString(cursor.getColumnIndex("name")) + "(" + seq + ")");
            } else {
                txtName.setText(cursor.getString(cursor.getColumnIndex("name")));
            }

            Date date = new Date(cursor.getLong(cursor.getColumnIndex("created_at")));
            SimpleDateFormat sim = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss.SSS", Locale.US);
            txtCreateDate.setText(sim.format(date));

            String content = cursor.getString(cursor.getColumnIndex("content"));
            content = content.replace(System.getProperty("line.separator"), "");
            txtContent.setText(content);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getActivity().getSupportLoaderManager().restartLoader(1, null, mAppHistoryCallback);
        }
    }

    public static String getNoFilter() {
        return ChampacaUtil.getApplicationContext().getString(R.string.jp_cayhanecamel_champaca_unselect);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMultipleActions == null) return;
        mMultipleActions.setTranslationY(-appBarLayout.getTotalScrollRange() - verticalOffset);
    }
}
