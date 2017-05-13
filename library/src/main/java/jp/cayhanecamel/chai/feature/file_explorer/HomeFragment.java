package jp.cayhanecamel.chai.feature.file_explorer;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.cayhanecamel.chai.feature.main.CommonRecyclerAdapter;
import jp.cayhanecamel.chai.feature.main.ItemDto;
import jp.cayhanecamel.chai.util.ChaiLog;
import jp.cayhanecamel.chai.R;

public class HomeFragment extends Fragment {
    public static final String KEY_NAME = "name";
    public static final String KEY_PATH = "path";


    private RecyclerView mListView;

    private CommonRecyclerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.jp_cayhanecamel_chai_fragment_tables, container, false);
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
        list.add(new ItemDto(ItemDto.Type.Header, "Directories", ""));

        list.addAll(getProductInofs());

        list.get(list.size() - 1).type = ItemDto.Type.ContentEnd;

        // Space for Floating Action Button
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));

        mAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ItemDto itemDto, long id) {
                if (!(itemDto.type == ItemDto.Type.Content || itemDto.type == ItemDto.Type.ContentEnd)) {
                    return;
                }

                startActivity(ExplorerActivity.createIntentForDirectly(view.getContext(), itemDto.path, itemDto.value));

            }
        });

        mAdapter.swapList(list);
    }


    public List<ItemDto> getProductInofs() {
        List<ItemDto> list = new ArrayList<>();
        final List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        appendItems(data, getActivity());

        for (Map line : data) {
            list.add(new ItemDto(ItemDto.Type.Content, "", line.get(KEY_NAME), line.get(KEY_PATH)));
        }
        return list;
    }

    protected boolean isAttachedExplorerContextHolder() {
        return getActivity() instanceof ExplorerContextHolder;
    }

    private static void appendItems(List<Map<String, String>> data, Context context) {
        PackageManager m = context.getPackageManager();
        String appDirpath = context.getPackageName();
        PackageInfo p = null;
        try {
            p = m.getPackageInfo(appDirpath, 0);
        } catch (PackageManager.NameNotFoundException e) {
            ChaiLog.e(e);
        }
        if (p != null) {
            appDirpath = p.applicationInfo.dataDir;
            appendItem(data, "App Root Dir", new File(appDirpath));
        }


        appendItem(data, "App Files", context.getFilesDir());
        appendItem(data, "App Cache", context.getCacheDir());
        appendItem(data, "External Root", Environment.getExternalStorageDirectory());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            appendItem(data, "External App Files", context.getExternalFilesDir(null));
            appendItem(data, "External App Cache", context.getExternalCacheDir());
        }
    }

    private static void appendItem(List<Map<String, String>> data, String name, File file) {
        if (file == null || !file.exists()) {
            return;
        }
        String[] files = file.list();
        if (files == null || files.length == 0) {
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(KEY_NAME, name);
        map.put(KEY_PATH, file.getAbsolutePath());
        data.add(map);
    }
}
