package jp.cayhanecamel.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import jp.cayhanecamel.champaca.R;


public class PartsEditView extends LinearLayout {

    public TextView title;

    public EditText value;


    public PartsEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PartsEditView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.jp_cayhanecamel_champaca_view_parts_edit, this);
        title = (TextView) findViewById(R.id.title);
        value = (EditText) findViewById(R.id.value);
    }

}
