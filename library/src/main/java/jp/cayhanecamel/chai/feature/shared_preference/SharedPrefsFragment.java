package jp.cayhanecamel.chai.feature.shared_preference;


import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import jp.cayhanecamel.chai.feature.main.CommonRecyclerAdapter;
import jp.cayhanecamel.chai.feature.main.ItemDto;
import jp.cayhanecamel.chai.util.ChaiUtil;
import jp.cayhanecamel.chai.R;


public class SharedPrefsFragment extends Fragment {

    public static final String ARG_NAME = "name";
    public static final String FULL_FILE_NAME = "full_file_name";
    //    protected ArrayList<String> mTypes;
    private String mArgName;
    private String mFullFileName;
    private RecyclerView mListView;

    public static SharedPrefsFragment newInstance(String name, String fullFileName) {
        SharedPrefsFragment fragment = new SharedPrefsFragment();
        Bundle args = new Bundle();
        args.putString(SharedPrefsFragment.ARG_NAME, name);
        args.putString(SharedPrefsFragment.FULL_FILE_NAME, fullFileName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.jp_cayhanecamel_chai_fragment_tables, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = (RecyclerView) getView().findViewById(R.id.jp_cayhanecamel_champaca_scroll);
        mListView.setLayoutManager(new LinearLayoutManager(ChaiUtil.getApplicationContext()));

        Bundle args = getArguments();
        if (args == null) {
            args = new Bundle();
        }
        // TODO Observableシリーズが廃止された後に再実装する
//        Toolbar toolbar = (Toolbar)((ActionBarActivity)getActivity()).getSupportActionBar();
//        toolbar.inflateMenu(R.menu.menu_main);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                return false;
//            }
//        });

        mArgName = args.getString(ARG_NAME);
        SharedPreferences prefs = getPrefs(mArgName);

        mFullFileName = args.getString(FULL_FILE_NAME);

//        mTypes = new ArrayList<String>();

        String[] from = {
                "type_key", "value"
        };
        int[] to = {
                android.R.id.text1, android.R.id.text2
        };
        ArrayList<Map<String, CharSequence>> data = new ArrayList<Map<String, CharSequence>>();
        Map<String, ?> all = prefs.getAll();
        LinkedList<String> keys = new LinkedList<String>(all.keySet());
        Collections.sort(keys);
        final List<ItemDto> list = new ArrayList();
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));
        list.add(new ItemDto(ItemDto.Type.Header, mFullFileName, ""));

        for (String key : keys) {
            HashMap<String, CharSequence> map = new HashMap<String, CharSequence>();
            Object value = all.get(key);
            String type = value == null ? "-" : value.getClass().getSimpleName();
            map.put("type_key", new SpannableString(String.format("[%s]%s%s", type, Html.fromHtml("<br/>"), key)).toString());
            map.put("value", getDisplayValue(value));
            data.add(map);
            // TODO 一旦利用しないようにする詳細側での実装次第では戻すかも
//            mTypes.add(value == null ? "String" : value.getClass().getSimpleName());
            list.add(new ItemDto(ItemDto.Type.Content, key, value));
        }
        list.get(list.size() - 1).type = ItemDto.Type.ContentEnd;


        CommonRecyclerAdapter adapter = new CommonRecyclerAdapter(getActivity().getApplicationContext(), list, true);
//        adapter.setEnabled(true);

        mListView.setAdapter(adapter);

        // sharedpref編集画面に遷移
        adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, ItemDto itemDto, long id) {
                String stringValue = itemDto.value;
                String stringKey = itemDto.title;
//                String stringType = "hoge";

                SharedPrefsEditFragment fragment = SharedPrefsEditFragment
                        .newInstance(mArgName, stringKey, stringValue, "");
                getFragmentManager().beginTransaction()
                        .replace(getId(), fragment).addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });

        adapter.setOnItemLongClickListener(new CommonRecyclerAdapter.OnItemLongClickListener() {

            @Override
            public void onItemLongClick(View view, ItemDto itemDto, long id) {
                String stringValue = itemDto.value;
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                    ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("shared_prefs_value", stringValue));
                } else {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(stringValue);
                }

                Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        adapter.swapList(list);
    }

    private CharSequence getDisplayValue(Object value) {
        SpannableStringBuilder buf = new SpannableStringBuilder();
        buf.append(String.valueOf(value));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss zzz");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        if (value instanceof Integer) {
            String asDate = formatter.format(new Date(((Integer) value) * DateUtils.SECOND_IN_MILLIS));
            buf.append(Html.fromHtml("<br/>")).append(" (Date: ").append(asDate).append(")");
        } else if (value instanceof Long) {
            long rawValue = (Long) value;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(rawValue));
            if (calendar.get(Calendar.YEAR) == 1970) {
                // Maybe seconds
                rawValue *= DateUtils.SECOND_IN_MILLIS;
            }

            String asDate = formatter.format(new Date(rawValue));
            buf.append(Html.fromHtml("<br/>")).append(" (Date: ").append(asDate).append(")");

            double asDouble = Double.longBitsToDouble(rawValue);
            buf.append(Html.fromHtml("<br/>")).append(" (double: ").append(String.valueOf(asDouble)).append(")");
        }

        return buf.toString();
    }

    private SharedPreferences getPrefs(String name) {
        if (name == null) {
            return PreferenceManager.getDefaultSharedPreferences(getActivity());
        } else {
            return getActivity().getSharedPreferences(name, Context.MODE_PRIVATE);
        }
    }

    private void deletePrefs() {
        new AlertDialog.Builder(getActivity())
                .setMessage(getActivity().getString(R.string.jp_cayhanecamel_chai_shared_prefs_delete_confirm))
                .setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File sharedPrefsDir = new File(getActivity()
                                        .getApplicationContext().getFilesDir()
                                        .getParentFile(), "shared_prefs");
                                if (!sharedPrefsDir.exists()) {
                                    return;
                                }

                                File f = new File(sharedPrefsDir, mFullFileName);
                                if (f.exists()) {
                                    // 直接ファイル削除に成功してもメモリ上に残った情報を書き込まれうるので、それを防ぐ
                                    getPrefs(mArgName).edit().clear().commit();
                                    f.delete();

                                    FragmentManager fm = getActivity().getSupportFragmentManager();
                                    if (fm != null && fm.getBackStackEntryCount() > 0) {
                                        FragmentTransaction ft = fm.beginTransaction();
                                        fm.popBackStack();
                                        ft.commitAllowingStateLoss();
                                    }
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .setCancelable(true)
                .show();
    }
}
