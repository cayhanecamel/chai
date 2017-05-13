package jp.cayhanecamel.chai.feature.main;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.cayhanecamel.chai.base.ProductInfos;
import jp.cayhanecamel.chai.data.ChaiConfig;
import jp.cayhanecamel.chai.util.ChaiLog;
import jp.cayhanecamel.chai.R;

public class AppInfoFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    private RecyclerView mListView;

    private FloatingActionsMenu mMultipleActions;

    private CommonRecyclerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.jp_cayhanecamel_chai_fragment_app_info, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMultipleActions = (FloatingActionsMenu) getView().findViewById(R.id.jp_cayhanecamel_chai_multiple_actions);
        mListView = (RecyclerView) getView().findViewById(R.id.jp_cayhanecamel_chai_scroll);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CommonRecyclerAdapter(getActivity().getApplicationContext(), null, true);
        mListView.setAdapter(mAdapter);

        getView().findViewById(R.id.jp_cayhanecamel_chai_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export();
                mMultipleActions.toggle();
            }
        });

        List<ItemDto> list = new ArrayList<>();

        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));
        list.add(new ItemDto(ItemDto.Type.Header, getString(R.string.jp_cayhanecamel_chai_apk_info), ""));
        list.addAll(getApkInfos());
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));


        list.add(new ItemDto(ItemDto.Type.Header, getString(R.string.jp_cayhanecamel_chai_device_info), ""));
        list.addAll(getDeviceInfos());
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));


        setupAppInfos(list);


        // Space for Floating Action Button
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));
        list.add(new ItemDto(ItemDto.Type.Blank, "", ""));

        mAdapter.swapList(list);
    }

    private void setupAppInfos(List<ItemDto> list) {
        Map<String, String> appInfos = ProductInfos.get().infos;
        if (appInfos == null || appInfos.size() == 0) {
            return;
        }

        list.add(new ItemDto(ItemDto.Type.Header, getString(R.string.jp_cayhanecamel_chai_app_info), ""));
        list.addAll(getAppInfos());
        list.get(list.size() - 1).type = ItemDto.Type.ContentEnd;
    }


    private List<ItemDto> getApkInfos() {
        List<ItemDto> list = new ArrayList<>();
        try {

            PackageInfo packageInfo = getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(),
                            PackageManager.GET_ACTIVITIES);
            list.add(new ItemDto(ItemDto.Type.Content, "Version Name", packageInfo.versionName));
            list.add(new ItemDto(ItemDto.Type.Content, "Version Code", packageInfo.versionCode));
            list.add(new ItemDto(ItemDto.Type.Content, "Version Code History", ChaiConfig
                    .getVersionCodeHistory()));
            list.add(new ItemDto(ItemDto.Type.ContentEnd, "Package", getActivity().getPackageName()));


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }


    private List<ItemDto> getDeviceInfos() {
        List<ItemDto> list = new ArrayList<>();
        list.add(new ItemDto(ItemDto.Type.Content, "Model", Build.MODEL));
        list.add(new ItemDto(ItemDto.Type.Content, "OS", Build.VERSION.RELEASE + "(API level:"
                + Build.VERSION.SDK_INT + ")"));

        // TODO この辺りは更に外部ライブラリに出す！！

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        list.add(new ItemDto(ItemDto.Type.Content, "Resolution Pixel(W:H)", metrics.widthPixels + "px" + " : " + metrics.heightPixels + "px"));

        float density = metrics.density;
        String densityStr = "";
        if (density == 0.75) {
            densityStr = "ldpi";

        } else if (density == 1.0) {
            densityStr = "mdpi";

        } else if (density == 1.5) {
            densityStr = "hdpi";

        } else if (density == 2.0) {
            densityStr = "xhdpi";

        } else if (density == 3.0) {
            densityStr = "xxhdpi";

        } else if (density == 4.0) {
            densityStr = "xxxhdpi";
        }
        list.add(new ItemDto(ItemDto.Type.ContentEnd, "Density", densityStr));
        return list;
    }

    private List<ItemDto> getAppInfos() {
        List<ItemDto> list = new ArrayList<>();
        Map<String, String> appInfos = ProductInfos.get().infos;
        Set<String> keys = appInfos.keySet();
        for (String key : keys) {
            list.add(new ItemDto(ItemDto.Type.Content, key, appInfos.get(key)));
        }
        return list;
    }

    private void export() {
        List<ItemDto> appInfos = getApkInfos();
        appInfos.addAll(getDeviceInfos());
        appInfos.addAll(getAppInfos());

        File file = new File(Environment.getExternalStorageDirectory(),
                "app_info.txt");

        try {
            file.delete();
            Writer writer = new FileWriter(file);

            try {
                SimpleDateFormat sim = new SimpleDateFormat(
                        "yyyy/MM/dd  HH:mm:ss.SSS");
                writer.append("This data has been export to " + sim.format(Calendar.getInstance().getTime()));
                writer.append(System.getProperty("line.separator"));
                writer.append(System.getProperty("line.separator"));

                for (ItemDto item : appInfos) {
                    writer.append(System.getProperty("line.separator"));
                    writer.append(item.title);
                    writer.append(" : ");
                    writer.append(item.value);
                    writer.append(System.getProperty("line.separator"));
                }

            } finally {
                writer.close();
            }
        } catch (IOException e) {
            ChaiLog.e(e);
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


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMultipleActions == null) return;
        mMultipleActions.setTranslationY(-appBarLayout.getTotalScrollRange() - verticalOffset);
    }
}
