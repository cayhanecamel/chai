package jp.cayhanecamel.chai.feature.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import jp.cayhanecamel.chai.feature.shared_preference.SharedPrefsActivity;
import jp.cayhanecamel.chai.feature.shared_preference.SharedPrefsFragment;
import jp.cayhanecamel.chai.util.ChaiUtil;
import jp.cayhanecamel.chai.R;

public class SharedPrefsRootFragment extends Fragment {

    private RecyclerView mListView;

    private CommonRecyclerAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.jp_cayhanecamel_chai_fragment_tables, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView = (RecyclerView) getView().findViewById(R.id.jp_cayhanecamel_chai_scroll);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CommonRecyclerAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);

        final List<ItemDto> list = new ArrayList<>();
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));
        list.add(new ItemDto(ItemDto.Type.Header, "SharedPreferences", ""));

        list.addAll(getDirectories());

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

                Intent intent = new Intent(getActivity().getApplicationContext(), SharedPrefsActivity.class);
                intent.putExtra(SharedPrefsFragment.ARG_NAME, itemDto.value);
                intent.putExtra(SharedPrefsFragment.FULL_FILE_NAME, itemDto.path);

                startActivity(intent);

            }
        });

        mAdapter.swapList(list);
    }

    public static Collection<? extends ItemDto> getDirectories() {
        List<ItemDto> list = new ArrayList<>();

        File sharedPrefsDir = new File(ChaiUtil.getApplicationContext().getFilesDir().getParentFile(), "shared_prefs");
        if (!sharedPrefsDir.exists()) {
            return list;
        }

        File[] files = sharedPrefsDir.listFiles();
        if (files == null) {
            return list;
        }

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        for (File file : files) {
            final String fileName = file.getName();
            final String name = fileName.substring(0, fileName.length() - 4);
            list.add(new ItemDto(ItemDto.Type.Content, "", name, fileName));
        }

        return list;
    }
}
