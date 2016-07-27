package jp.cayhanecamel.widget;


import android.content.Context;

import jp.cayhanecamel.base.AbstractMenuItemView;
import jp.cayhanecamel.feature.main.ItemDto;
import jp.cayhanecamel.champaca.R;

public class ListBlankView extends AbstractMenuItemView {


    public ListBlankView(Context context) {
        super(context);
        inflate(getContext(), R.layout.jp_cayhanecamel_champaca_list_blank_view, this);
    }


    public void bind(ItemDto menu) {
    }

}
