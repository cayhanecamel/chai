package jp.cayhanecamel.champaca.feature.sqlite;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jp.cayhanecamel.champaca.base.ProductInfos;
import jp.cayhanecamel.champaca.data.ChampacaConst;
import jp.cayhanecamel.champaca.db.ChampacaOpenHelper;
import jp.cayhanecamel.champaca.db.ProductOpenHelper;
import jp.cayhanecamel.champaca.util.ChampacaUtil;
import jp.cayhanecamel.champaca.R;

public class TableFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    private SQLiteOpenHelper mOpenHelper;

    private String mTableName;

    private List<String> mColumnNames;

    private LinearLayout tableBase;

    private TableLayout tableLayout;

    private LinearLayout pageLayout;

    private TextView titleText;

    private TextView pageTitle;

    private Button next;

    private Button back;

    private FloatingActionsMenu multipleActions;

    private FloatingActionButton export;

    private FloatingActionButton delete;

    private FloatingActionButton insert;

    private FloatingActionButton filter;

    private int currentPage = 1;

    private int allPage = 1;

    private int offset = 0;

    private int recordCount;

    private int limit = 50;

    private SQLiteDatabase db;

    private int lastNo = 0;

    private int currentRoWNum = 0;

    private int mDbVersion;

    private String mDbName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater
                .inflate(R.layout.jp_cayhanecamel_champaca_fragment_list_common, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setupTable();
    }

    private void init() {
        mTableName = getActivity().getIntent().getStringExtra(ChampacaConst.TABLE_NAME);
        mDbName = getActivity().getIntent().getStringExtra(ChampacaConst.DB_NAME);
        mDbVersion = getActivity().getIntent().getIntExtra(ChampacaConst.DB_VERSION, 0);

        if (!mDbName.equals(ChampacaConst.CHAMPACA)) {
            mOpenHelper = new ProductOpenHelper(ChampacaUtil.getApplicationContext(), mDbName, null, mDbVersion);

        } else {
            mOpenHelper = ChampacaOpenHelper.get();
        }

        db = mOpenHelper.getWritableDatabase();


        multipleActions = (FloatingActionsMenu) getView().findViewById(R.id.jp_cayhanecamel_champaca_multiple_actions);
        tableBase = (LinearLayout) getView().findViewById(R.id.jp_cayhanecamel_champaca_table_base);
        tableLayout = (TableLayout) getView().findViewById(R.id.jp_cayhanecamel_champaca_table_layout);
        titleText = (TextView) getView().findViewById(R.id.jp_cayhanecamel_champaca_action_bar_title);

        filter = (FloatingActionButton) getView().findViewById(R.id.jp_cayhanecamel_champaca_filter);
        insert = (FloatingActionButton) getView().findViewById(R.id.insert);
        export = (FloatingActionButton) getView().findViewById(R.id.jp_cayhanecamel_champaca_export);
        delete = (FloatingActionButton) getView().findViewById(R.id.jp_cayhanecamel_champaca_delete);


        filter.findViewById(R.id.jp_cayhanecamel_champaca_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter();
                multipleActions.toggle();
            }
        });

        insert.findViewById(R.id.insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
                multipleActions.toggle();
            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export();
                multipleActions.toggle();
            }
        });

        delete.findViewById(R.id.jp_cayhanecamel_champaca_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
                multipleActions.toggle();
            }
        });
    }


    private void setupTable() {
        recordCount = getRecordCount(db, mTableName);
        titleText.setText(mTableName + "( " + recordCount + " )");

        BigDecimal bigRecordCount = new BigDecimal(recordCount);
        BigDecimal bigLimit = new BigDecimal(limit);
        allPage = bigRecordCount.divide(bigLimit, 0, BigDecimal.ROUND_UP).intValue();

        updateTable(false);
    }

    private void updateTable(boolean isBack) {

        if (currentPage > 1) {
            offset = (currentPage - 1) * limit;
        } else {
            offset = 0;
        }

        tableLayout.removeAllViews();

        Cursor cursor = getCurrentCursor();
        createColumnNames(cursor);

        createHeader();
        createRows(cursor, isBack);

        if (pageTitle == null) {
            tableBase.addView(View.inflate(getActivity(), R.layout.jp_cayhanecamel_champaca_table_page, null));
            pageLayout = (LinearLayout) getView().findViewById(R.id.jp_cayhanecamel_champaca_pageLayout);
            pageTitle = (TextView) getView().findViewById(R.id.jp_cayhanecamel_champaca_pageTitle);
            back = (Button) getView().findViewById(R.id.jp_cayhanecamel_champaca_back);
            next = (Button) getView().findViewById(R.id.jp_cayhanecamel_champaca_next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPage++;
                    updateTable(false);
                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPage--;
                    lastNo = lastNo - currentRoWNum;
                    updateTable(true);
                }
            });
        }

        pageTitle.setText(currentPage + "/" + allPage);


        if (currentPage == 1) {
            back.setEnabled(false);
        } else {
            back.setEnabled(true);
        }

        if (currentPage == allPage) {
            next.setEnabled(false);
        } else {
            next.setEnabled(true);
        }

        if (recordCount == 0) {
            pageLayout.setVisibility(View.GONE);
        }

//        if (recordCount == 0) {
//            pageLayout.setVisibility(View.GONE);
//            delete.setVisibility(View.GONE);
//            export.setVisibility(View.GONE);
//            filter.setVisibility(View.GONE);
//        } else {
//            pageLayout.setVisibility(View.VISIBLE);
//            delete.setVisibility(View.VISIBLE);
//            export.setVisibility(View.VISIBLE);
//            filter.setVisibility(View.VISIBLE);
//        }

    }

    private void createColumnNames(Cursor cursor) {

        mColumnNames = new ArrayList<String>(Arrays.asList(cursor
                .getColumnNames()));

        for (String ColumnName : mColumnNames) {
            String[] args = new String[1];
            args[0] = ColumnName;

            Cursor typeCursor = db.rawQuery("select typeof (?) from "
                    + mTableName, args);

            typeCursor.moveToFirst();

        }
    }

    private Cursor getCurrentCursor() {
        Cursor cursor = db.query(
                mTableName,
                null,
                null,
                null,
                null,
                null,
                null,
                offset + "," + limit);


        return cursor;
    }


    private void createRows(Cursor cursor, boolean isBack) {


        int no = lastNo;
        if (isBack) {
            no -= cursor.getCount();
        }

        currentRoWNum = 0;


        while (cursor.moveToNext()) {
            TableRow row = new TableRow(getActivity());
            no++;
            lastNo = no;
            TextView cellNo = getHeaderView();
            cellNo.setText(String.valueOf(no));
            row.addView(cellNo);
            currentRoWNum++;

            for (String columnName : mColumnNames) {


                TextView cell = getCellView();

                // コンバートが指定されている場合は従ってコンバートする
                for (TableInfo tableInfo : ProductInfos.get().tableInfos) {
                    if (tableInfo.tableName.equals(mTableName)
                            && tableInfo.columName.equals(columnName)) {

                        // 日付加工
                        if (tableInfo.convert == TableInfo.ConvertType.DATE) {
                            long value = cursor.getLong(cursor
                                    .getColumnIndex(columnName))
                                    * tableInfo.multiply;
                            Date date = new Date(value);
                            SimpleDateFormat sim = new SimpleDateFormat(
                                    "yyyy/MM/dd HH:mm:ss:SS");
                            cell.setText(sim.format(date)
                                    + "("
                                    + cursor.getLong(cursor
                                    .getColumnIndex(columnName)) + ")");
                        }
                        continue;
                    }
                }

                if (cell.getText() == null || cell.getText().length() == 0) {
                    cell.setText(cursor.getString(cursor
                            .getColumnIndex(columnName)));
                }

                row.addView(cell);
            }


            tableLayout.addView(row);
        }
    }

    private TextView getCellView() {
        TextView cell = getCellBaseView();
        cell.setBackgroundResource(R.drawable.jp_cayhanecamel_champaca_table_cell_shape);
        return cell;
    }

    private TextView getHeaderView() {
        TextView cell = getCellBaseView();
        cell.setBackgroundResource(R.drawable.jp_cayhanecamel_champaca_table_header_shape);
        return cell;
    }

    private TextView getCellBaseView() {
        TextView cell = new TextView(getActivity());
        Resources resources = getActivity().getResources();
        int cellLRPadding = resources.getDimensionPixelSize(R.dimen.jp_cayhanecamel_champaca_table_cell_lr_padding);
        int cellTBPadding = resources.getDimensionPixelSize(R.dimen.jp_cayhanecamel_champaca_table_cell_tb_padding);
        cell.setGravity(Gravity.CENTER);
        cell.setTextColor(resources.getColor(R.color.jp_cayhanecamel_champaca_txt_color_gy01));
        cell.setPadding(cellLRPadding, cellTBPadding, cellLRPadding, cellTBPadding);
        return cell;
    }

    private void createHeader() {
        TableRow header = new TableRow(getActivity());

        TextView no = getHeaderView();
        no.setText("No");
        header.addView(no);

        for (String columnName : mColumnNames) {
            TextView tableName = getHeaderView();
            tableName.setText(columnName);
            header.addView(tableName);
        }
        tableLayout.addView(header);
    }


    private int getRecordCount(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + tableName,
                null);
        int count = 0;
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    private void filter() {
        Toast.makeText(getActivity(), "TODO : filter！", Toast.LENGTH_LONG).show();
    }

    private void export() {
        Toast.makeText(getActivity(), "TODO : export！", Toast.LENGTH_LONG).show();
    }

    private void insert() {
        Toast.makeText(getActivity(), "TODO : insert！", Toast.LENGTH_LONG).show();
    }

    public void delete() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.delete(mTableName, null, null);
        //TODO 削除の即時UI反映を実装する
        //TODO 削除確認をとる
        Toast.makeText(getActivity(), "削除の即時UI反映を実装する", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (multipleActions == null) return;
        multipleActions.setTranslationY(-appBarLayout.getTotalScrollRange() - verticalOffset);
    }
}
