package jp.cayhanecamel.feature.file_explorer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;

import java.io.File;

public class FileActionFragment extends DialogFragment {

    public static final String ARG_FILE = "file";
    public static final String ARG_POSITION = "position";
    public static final String ARG_TITLE = "title";

    private static final String[] ITEMS_FILE = {
            "削除", "送る", "開く"
    };
    private static final String[] ITEMS_DIR = {
            "削除"
    };

    private OnFileActionListener mOnFileActionListener;

    public static FileActionFragment show(FragmentActivity activity, String title, File file, int position, OnFileActionListener onFileActionListener) {
        FileActionFragment fragment = new FileActionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putSerializable(ARG_FILE, file);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        fragment.show(activity.getSupportFragmentManager(), "property");
        fragment.setOnFileActionListener(onFileActionListener);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final int position = args.getInt(ARG_POSITION);
        final File file = (File) args.getSerializable(ARG_FILE);
        String title = args.getString(ARG_TITLE);
        if (title == null) {
            title = file.getAbsolutePath();
        }

        final String[] items = file.isDirectory() ? ITEMS_DIR : ITEMS_FILE;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        if (which < 0) {
                            return;
                        }

                        String item = items[which];
                        if ("削除".equals(item)) {
                            mOnFileActionListener.onFileActionDelete(file, position);
                        } else if ("送る".equals(item)) {
                            mOnFileActionListener.onFileActionSend(file, position);
                        } else if ("開く".equals(item)) {
                            mOnFileActionListener.onFileActionOpen(file, position);
                        }
                    }
                })
                .setCancelable(true)
                .create();
    }

    public void setOnFileActionListener(OnFileActionListener onFileActionListener) {
        mOnFileActionListener = onFileActionListener;
    }

    public interface OnFileActionListener {
        void onFileActionDelete(File file, int position);

        void onFileActionSend(File file, int position);

        void onFileActionOpen(File file, int position);
    }
}
