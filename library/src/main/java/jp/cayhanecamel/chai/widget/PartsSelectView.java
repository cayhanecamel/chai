package jp.cayhanecamel.champaca.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import jp.cayhanecamel.champaca.R;


public class PartsSelectView extends LinearLayout {

    public TextView title;

    public TextView value;


    public PartsSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PartsSelectView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.jp_cayhanecamel_champaca_view_parts_select, this);
        title = (TextView) findViewById(R.id.title);
        value = (TextView) findViewById(R.id.value);
    }

}
