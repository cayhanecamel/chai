package jp.cayhanecamel.feature.file_explorer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import jp.cayhanecamel.feature.main.CommonRecyclerAdapter;
import jp.cayhanecamel.feature.main.ItemDto;
import jp.cayhanecamel.util.Util;
import jp.cayhanecamel.widget.ListItemView;
import jp.cayhanecamel.champaca.R;

public class ExplorerFragment extends Fragment {

    public static final String ARG_DIR_NAME = "dirName";
    public static final String ARG_DISPLAY_NAME = "displayName";
    private ExplorerContext mExplorerContext;
    private PathSegment mPathSegment;
    private RecyclerView mRecyclerView;
    FileItemAdapter mAdapter;

    public static ExplorerFragment newInstance(String dirName) {
        return newInstance(dirName, null);
    }

    public static ExplorerFragment newInstance(String dirName, String displayName) {
        ExplorerFragment fragment = new ExplorerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DIR_NAME, dirName);
        args.putString(ARG_DISPLAY_NAME, displayName);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(Bundle extras) {
        return newInstance(extras.getString(ARG_DIR_NAME), extras.getString(ARG_DISPLAY_NAME));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPathSegment = new PathSegment(getArguments().getString(ARG_DIR_NAME), getArguments().getString(ARG_DISPLAY_NAME));

        mExplorerContext = ((ExplorerContextHolder) activity).getExplorerContext();
        mExplorerContext.pushPathSegment(mPathSegment);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mExplorerContext.popPathSegment(mPathSegment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.jp_cayhanecamel_champaca_fragment_explorer, container, false);
        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.jp_cayhanecamel_champaca_scroll);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        @SuppressWarnings("deprecation")
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.FILL_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT);
        View breadcrumbsView = mExplorerContext.createBreadcrumbs(
                getActivity(),
                new ExplorerContext.OnPathSegmentClickListener() {
                    @Override
                    public void onClick(PathSegment pathSegment, String fullPath) {
                        // For jumping to stack, pop clicked name because the
                        // name is the parent's.
                        getFragmentManager().popBackStack(fullPath, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                });
        breadcrumbsView.setLayoutParams(layoutParams);

        constructList();

        if (mAdapter == null) return;
        mAdapter.setHeaderView(breadcrumbsView);
    }

    private void constructList() {
        File dir = new File(mExplorerContext.getCurrentDirPath());
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.getName().equals("");
            }
        });
        if (files == null || files.length == 0) {
            Toast.makeText(getActivity(), R.string.jp_cayhanecamel_champaca_explorer_data_not_found, Toast.LENGTH_SHORT).show();
            List<FileItem> items = Collections.emptyList();
            mAdapter = new FileItemAdapter(getActivity(), items);
            mRecyclerView.setAdapter(mAdapter);
            return;
        }

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.getName().equals(PathSegment.NAME_PARENT)) {
                    return -1;
                }
                if (rhs.getName().equals(PathSegment.NAME_PARENT)) {
                    return 1;
                }
                if (lhs.isDirectory()) {
                    return -1;
                }
                if (rhs.isDirectory()) {
                    return 1;
                }

                return lhs.getName().compareTo(rhs.getName());
            }
        });

        final LinkedList<FileItem> fileItems = new LinkedList<>();
        for (File file : files) {
            String displayName;
            String info = null;
            if (file.getName().equals(PathSegment.NAME_PARENT)) {
                displayName = file.getParentFile().getName();
                info = getString(R.string.jp_cayhanecamel_champaca_back);
            } else {
                displayName = file.getName();
            }
            fileItems.add(new FileItem(file, displayName, info));
        }

        final FileItemAdapter adapter = new FileItemAdapter(getActivity(), fileItems);
        mAdapter = adapter;
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(
                new CommonRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, ItemDto item, long id) {
                        FileItem fileItem = adapter.getFileItem((Integer) view.getTag(R.id.jp_cayhanecamel_champaca_tag_position));
                        File file = fileItem.getFile();
                        String fileName = file.getName();
                        if (fileName.equals(PathSegment.NAME_PARENT)) {
                            getFragmentManager().popBackStack();
                        } else if (file.isDirectory()) {
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(
                                            getId(),
                                            ExplorerFragment.newInstance(
                                                    fileName,
                                                    fileItem.getDisplayName()))
                                    .addToBackStack(mExplorerContext.getCurrentDirPath())
                                    .commit();
                        } else {
                            openFile(file);
                        }
                    }
                });
        adapter.setOnItemLongClickListener(
                new CommonRecyclerAdapter.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(View view, ItemDto itemDto, long id) {
                        int position = (Integer) view.getTag(R.id.jp_cayhanecamel_champaca_tag_position);
                        FileItem item = adapter.getFileItem(position);
                        FileActionFragment.show(getActivity(),
                                item.getDisplayName(), item.getFile(),
                                position,
                                new FileActionFragment.OnFileActionListener() {
                                    @Override
                                    public void onFileActionDelete(File file, int position) {
                                        if (file.delete()) {
                                            Toast.makeText(
                                                    getActivity(),
                                                    getString(R.string.jp_cayhanecamel_champaca_explorer_deletion_completed, file.getAbsolutePath()),
                                                    Toast.LENGTH_SHORT).show();
                                            adapter.removeItemAt(position);
                                        } else {
                                            Toast.makeText(
                                                    getActivity(),
                                                    getString(R.string.jp_cayhanecamel_champaca_explorer_deletion_failed, file.getAbsolutePath()),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFileActionSend(File file, int position) {
                                        sendFile(file);
                                    }

                                    @Override
                                    public void onFileActionOpen(File file, int position) {
                                        openFile(file);
                                    }
                                });
                    }
                });
    }

    protected void openFile(File file) {
        startAnotherActivity(Intent.ACTION_VIEW, file);
    }

    protected void sendFile(File file) {
        startAnotherActivity(Intent.ACTION_SEND, file);
    }

    private void startAnotherActivity(String action, File file) {
        if (!file.getAbsolutePath().startsWith(
                Environment.getExternalStorageDirectory().getAbsolutePath())) {
            // TODO Implement to show private file content

            for (String imageExtension : imageExtensions) {
                if (file.getAbsolutePath().indexOf(imageExtension) > 0) {
                    showImage(file);
                    return;
                }
            }

            if (Util.isTextFile(file.getAbsolutePath())) {
                showText(file);
                return;
            }

            Toast.makeText(getActivity(), R.string.jp_cayhanecamel_champaca_explorer_not_implemented_yet, Toast.LENGTH_LONG).show();
            return;
        }

        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');
        String ext = index >= 0 ? fileName.substring(index + 1).toLowerCase(Locale.US) : "";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);

        Intent intent;
        if (Intent.ACTION_VIEW.equals(action)) {
            intent = new Intent(Intent.ACTION_VIEW).setDataAndType(
                    Uri.fromFile(file), mimeType).addFlags(
                    Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent(Intent.ACTION_SEND)
                    .setData(Uri.fromFile(file))
                    .setType(mimeType)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), R.string.jp_cayhanecamel_champaca_explorer_opener_not_found,
                    Toast.LENGTH_LONG).show();
        }
    }

    static class FileItem {
        private File mFile;
        private String mDisplayName;
        private String mInfo;

        public FileItem(File file, String displayName, String info) {
            mFile = file;
            mDisplayName = displayName;
            mInfo = info;
        }

        public String getDisplayName() {
            return TextUtils.isEmpty(mDisplayName) ? mFile.getName() : mDisplayName;
        }

        public String getInfo(SimpleDateFormat dateFormat) {
            if (!TextUtils.isEmpty(mInfo)) {
                return mInfo;
            }

            return (mFile.isDirectory() ? "-" : formatSize(mFile.length())) + ", " + dateFormat.format(new Date(mFile.lastModified()));
        }

        public boolean hasIcon() {
            return mFile.isDirectory();
        }

        public Drawable loadIcon(Context context) {
            if (!mFile.isDirectory()) {
                return null;
            }

            Drawable drawable = context.getResources().getDrawable(R.drawable.jp_cayhanecamel_champaca_ic_arrow_next);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            return drawable;
        }

        public File getFile() {
            return mFile;
        }

        private static String[] UNITS = {"B", "KB", "MB", "GB", "TB", "PB"};

        @SuppressLint("DefaultLocale")
        private static String formatSize(long size) {
            int unitIndex = 0;
            long value = size;
            while (value > 1024L) {
                value /= 1024L;
                unitIndex++;
            }

            int displayUnitIndex = Math.min(unitIndex, UNITS.length - 1);
            String unit = UNITS[displayUnitIndex];
            if (unitIndex == 0) {
                return String.format("%d%s", value, unit);
            }
            // Ignore value is over peta
            double displayValue = size / Math.pow(1024, unitIndex);
            return String.format("%.2f%s", displayValue, unit);
        }

        public static List<ItemDto> toItemDtoList(List<FileItem> fileItems) {
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPAN);
            dataFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));

            ArrayList<ItemDto> result = new ArrayList<>(fileItems.size());
            for (FileItem fileItem : fileItems) {
                result.add(new ItemDto(ItemDto.Type.Content, fileItem.getDisplayName(), fileItem.getInfo(dataFormat)));
            }

            if (!result.isEmpty()) {
                result.get(result.size() - 1).type = ItemDto.Type.ContentEnd;
            }

            return result;
        }
    }

    public static final String[] imageExtensions = {".jpg", ".bmp", ".gif", ".png"};

    private void showImage(File file) {
        ImageViewFragment fragment = new ImageViewFragment();
        fragment.setTargetFragment(this, 0);
        fragment.setImageFile(file);
        fragment.show(getActivity(), null);
    }

    private void showText(File file) {
        TextViewFragment fragment = new TextViewFragment();
        fragment.setTargetFragment(this, 0);
        fragment.setTextFile(file);
        fragment.show(getActivity(), null);
    }

    private static class FileItemAdapter extends CommonRecyclerAdapter {

        private final List<FileItem> fileItems;

        public FileItemAdapter(Context context, List<FileItem> fileItems) {
            super(context, FileItem.toItemDtoList(fileItems), true);
            this.fileItems = fileItems;
        }

        public FileItem getFileItem(int position) {
            return fileItems.get(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            int itemPosition = toItemPosition(position);
            if (itemPosition < 0) return;
            ListItemView itemView = (ListItemView) holder.itemView;
            // TODO Notify itemPosition to listener
            itemView.setTag(R.id.jp_cayhanecamel_champaca_tag_position, itemPosition);

            // Show an icon for a directory
            FileItem item = fileItems.get(itemPosition);
            if (item.hasIcon()) {
                itemView.setCompoundDrawableRight(item.loadIcon(itemView.getContext()));
            } else {
                itemView.setCompoundDrawableRight(null);
            }
        }

        public void removeItemAt(int itemPosition) {
            fileItems.remove(itemPosition);
            getItemList().remove(itemPosition);
            // Ugh! notifyItemRemoved does not rebind other items.
            notifyDataSetChanged();
        }
    }
}
