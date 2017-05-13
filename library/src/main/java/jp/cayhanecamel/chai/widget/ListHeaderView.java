package jp.cayhanecamel.chai.widget;


import android.content.Context;
import android.widget.TextView;

import jp.cayhanecamel.chai.base.AbstractMenuItemView;
import jp.cayhanecamel.chai.feature.main.ItemDto;
import jp.cayhanecamel.chai.R;

public class ListHeaderView extends AbstractMenuItemView {

    private TextView title;

    public ListHeaderView(Context context) {
        super(context);
        inflate(getContext(), R.layout.jp_cayhanecamel_chai_list_header_view, this);
        title = (TextView) findViewById(R.id.jp_cayhanecamel_champaca_title);
    }


    public void bind(ItemDto menu) {
        title.setText(menu.title);
    }

}
