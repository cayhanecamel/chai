package jp.cayhanecamel.chai.feature.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.cayhanecamel.chai.R;


public class BlankFragment extends Fragment {

    public static String INFO = "INFO";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.jp_cayhanecamel_chai_fragment_blank, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView comment = (TextView) getView().findViewById(R.id.jp_cayhanecamel_chai_info);

        Bundle extras = getArguments();
        String info = ((String) extras.getSerializable(INFO));
        comment.setText(info);

    }


}
