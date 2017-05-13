package jp.cayhanecamel.champaca.widget;


import android.content.Context;
import android.widget.TextView;

import jp.cayhanecamel.champaca.base.AbstractMenuItemView;
import jp.cayhanecamel.champaca.feature.main.ItemDto;
import jp.cayhanecamel.champaca.R;

public class ListHeaderView extends AbstractMenuItemView {

    private TextView title;

    public ListHeaderView(Context context) {
        super(context);
        inflate(getContext(), R.layout.jp_cayhanecamel_champaca_list_header_view, this);
        title = (TextView) findViewById(R.id.jp_cayhanecamel_champaca_title);
    }


    public void bind(ItemDto menu) {
        title.setText(menu.title);
    }

}
