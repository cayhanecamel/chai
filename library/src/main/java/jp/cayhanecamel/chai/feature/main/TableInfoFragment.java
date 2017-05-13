package jp.cayhanecamel.champaca.feature.main;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jp.cayhanecamel.champaca.data.ChampacaConst;
import jp.cayhanecamel.champaca.db.ChampacaOpenHelper;
import jp.cayhanecamel.champaca.db.ProductOpenHelper;
import jp.cayhanecamel.champaca.feature.sqlite.TableActivity;
import jp.cayhanecamel.champaca.util.ChampacaUtil;
import jp.cayhanecamel.champaca.R;

public class TableInfoFragment extends Fragment {

    private ProductOpenHelper mProductOpenHelper;

    private ChampacaOpenHelper mChampacaOpenHelper;

    private RecyclerView mListView;

    private int mDbVersion;

    private String mDbName;

    private CommonRecyclerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args_ = getArguments();
        if (args_ != null) {
            if (args_.containsKey(ChampacaConst.DB_NAME)) {
                mDbVersion = ((int) args_.getSerializable(ChampacaConst.DB_VERSION));
                mDbName = ((String) args_.getSerializable(ChampacaConst.DB_NAME));
                if (!mDbName.equals(ChampacaConst.CHAMPACA)) {
                    mProductOpenHelper = new ProductOpenHelper(ChampacaUtil.getApplicationContext(), mDbName, null, mDbVersion);
                } else {
                    mChampacaOpenHelper = ChampacaOpenHelper.get();
                }
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.jp_cayhanecamel_champaca_fragment_tables, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView = (RecyclerView) getView().findViewById(R.id.jp_cayhanecamel_champaca_scroll);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CommonRecyclerAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);


        final List<ItemDto> list = new ArrayList<>();
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));
        list.add(new ItemDto(ItemDto.Type.Header, "Tables", ""));

        list.addAll(getTables());

        list.get(list.size() - 1).type = ItemDto.Type.ContentEnd;

        // Space for Floating Action Button
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));

        mAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ItemDto itemDto, long id) {

                if (itemDto.type == ItemDto.Type.Content
                        || itemDto.type == ItemDto.Type.ContentEnd) {

                    Intent intent = new Intent(getActivity()
                            .getApplicationContext(), TableActivity.class);
                    intent.putExtra(ChampacaConst.TABLE_NAME, itemDto.value);
                    intent.putExtra(ChampacaConst.DB_NAME, mDbName);
                    intent.putExtra(ChampacaConst.DB_VERSION, mDbVersion);

                    startActivity(intent);
                }


            }
        });

        mAdapter.swapList(list);

    }

    private List<ItemDto> getTables() {
        List<ItemDto> list = new ArrayList<>();
        SQLiteDatabase productDb;
        if (!mDbName.equals(ChampacaConst.CHAMPACA)) {
            productDb = mProductOpenHelper.getWritableDatabase();
        } else {
            productDb = mChampacaOpenHelper.getWritableDatabase();
        }


        Cursor cursor2 = productDb.rawQuery(
                "SELECT * FROM sqlite_master WHERE type='table' order by name", null);
        try {

            while (cursor2.moveToNext()) {
                final String tableName = cursor2.getString(cursor2
                        .getColumnIndex("name"));
                ItemDto itemDto = new ItemDto(ItemDto.Type.Content, "", tableName);
                list.add(itemDto);

            }


        } finally {
            cursor2.close();
        }

        return list;
    }


}
