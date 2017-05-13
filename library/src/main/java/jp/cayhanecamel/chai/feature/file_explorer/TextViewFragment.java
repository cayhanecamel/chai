package jp.cayhanecamel.chai.feature.file_explorer;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import jp.cayhanecamel.chai.util.ChaiLog;
import jp.cayhanecamel.chai.util.ChaiUtil;
import jp.cayhanecamel.chai.R;

public class TextViewFragment extends DialogFragment {

    private File textFile;

    public TextViewFragment() {
        super();
        // do nothing. for rotate display etc
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (!textFile.exists()) {
            dismissAllowingStateLoss();
            Toast.makeText(ChaiUtil.getApplicationContext(), "file not found!", Toast.LENGTH_LONG).show();
        }


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.jp_cayhanecamel_chai_fragment_text_view, null,
                false);

        TextView text = (TextView) view.findViewById(R.id.jp_cayhanecamel_champaca_text);
        StringBuilder builder = new StringBuilder(text.getText());

        String line = "";
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(textFile);
            br = new BufferedReader(fr);


            while ((line = br.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }


        } catch (FileNotFoundException e) {
            ChaiLog.e(e);
        } catch (IOException e) {
            ChaiLog.e(e);
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                ChaiLog.e(e);
            }

        }

        // TODO 巨大すぎるテキストファイルを開きたくなったときにページングとか考える
        text.setText(builder.toString());


        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle(textFile.getName());
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;

    }

    public void show(FragmentActivity hostActivity, String tag) {
        assert (hostActivity != null);
        FragmentTransaction transaction = hostActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(this, tag);
        transaction.show(this);
        transaction.commitAllowingStateLoss();
    }

    public void setTextFile(File textFile) {
        this.textFile = textFile;
    }

}
