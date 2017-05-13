package jp.cayhanecamel.chai.widget;


import android.content.Context;

import jp.cayhanecamel.chai.base.AbstractMenuItemView;
import jp.cayhanecamel.chai.feature.main.ItemDto;
import jp.cayhanecamel.chai.R;

public class ListBlankView extends AbstractMenuItemView {


    public ListBlankView(Context context) {
        super(context);
        inflate(getContext(), R.layout.jp_cayhanecamel_chai_list_blank_view, this);
    }


    public void bind(ItemDto menu) {
    }

}
