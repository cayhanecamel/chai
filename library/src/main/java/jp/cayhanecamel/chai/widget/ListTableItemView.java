package jp.cayhanecamel.chai.widget;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.cayhanecamel.chai.base.AbstractMenuItemView;
import jp.cayhanecamel.chai.feature.main.ItemDto;
import jp.cayhanecamel.chai.R;

public class ListTableItemView extends AbstractMenuItemView {


    private TextView value;
    private View divider;

    public ListTableItemView(Context context) {
        super(context);
        inflate(getContext(), R.layout.jp_cayhanecamel_chai_list_item_common, this);
        value = (TextView) findViewById(R.id.jp_cayhanecamel_champaca_value);
        divider = (View) findViewById(R.id.jp_cayhanecamel_champaca_divider);

    }


    public void bind(ItemDto itemDto) {
        value.setText(itemDto.value);
        ViewGroup.LayoutParams layoutParams = divider.getLayoutParams();
        if (itemDto.type == ItemDto.Type.ContentEnd) {
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.jp_cayhanecamel_champaca_divider_end);
        } else {
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.jp_cayhanecamel_champaca_divider);
        }
        // For on click
        setTag(R.id.jp_cayhanecamel_champaca_tag_item, itemDto);
    }

}
