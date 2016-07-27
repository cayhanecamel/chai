package jp.cayhanecamel.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import jp.cayhanecamel.base.recycler.ItemBindable;
import jp.cayhanecamel.feature.main.ItemDto;


public abstract class AbstractMenuItemView extends LinearLayout implements ItemBindable<ItemDto> {

    public AbstractMenuItemView(Context context) {
        super(context);
    }

    public AbstractMenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractMenuItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public abstract void bind(ItemDto menu);
}
