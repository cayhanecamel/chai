package jp.cayhanecamel.champaca.feature.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jp.cayhanecamel.champaca.base.AbstractMenuItemView;
import jp.cayhanecamel.champaca.base.recycler.CollectionRecyclerAdapter;
import jp.cayhanecamel.champaca.base.recycler.ItemCollection;
import jp.cayhanecamel.champaca.base.recycler.PositionBindableViewHolder;
import jp.cayhanecamel.champaca.widget.ListBlankView;
import jp.cayhanecamel.champaca.widget.ListHeaderView;
import jp.cayhanecamel.champaca.widget.ListItemView;
import jp.cayhanecamel.champaca.widget.ListTableItemView;
import jp.cayhanecamel.champaca.R;

public class CommonRecyclerAdapter extends CollectionRecyclerAdapter<ItemDto, AbstractMenuItemView> {
    private final Context context;
    private final CommonItemCollection items;
    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;
    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;
    private boolean isSimple;

    public CommonRecyclerAdapter(Context context, List<ItemDto> items) {
        this(context, new CommonItemCollection().withItems(items), false);
    }

    public CommonRecyclerAdapter(Context context, List<ItemDto> items, boolean isSimple) {
        this(context, new CommonItemCollection().withItems(items), isSimple);
    }

    private CommonRecyclerAdapter(Context context, CommonItemCollection items, boolean isSimple) {
        super(items);
        this.context = context;
        this.items = items;
        this.isSimple = isSimple;
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, (ItemDto) v.getTag(R.id.jp_cayhanecamel_champaca_tag_item), v.getId());
                }
            }
        };
        onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(v, (ItemDto) v.getTag(R.id.jp_cayhanecamel_champaca_tag_item), v.getId());
                }
                return false;
            }
        };
    }

    @Override
    protected AbstractMenuItemView newView(int viewType) {
        if (viewType == ItemDto.Type.Content.ordinal() || viewType == ItemDto.Type.ContentEnd.ordinal()) {
            AbstractMenuItemView itemView;
            if (isSimple) {
                itemView = new ListItemView(context);
            } else {
                itemView = new ListTableItemView(context);
            }
            itemView.setOnClickListener(onClickListener);
            itemView.setOnLongClickListener(onLongClickListener);

            return itemView;
        } else if (viewType == ItemDto.Type.Header.ordinal()) {
            return new ListHeaderView(context);
        } else {
            return new ListBlankView(context);
        }
    }

    @Override
    protected int getItemViewTypeByItemPosition(int itemPosition) {
        return getItem(itemPosition).type.ordinal();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void swapList(List<ItemDto> items) {
        this.items.withItems(items);
        notifyDataSetChanged();
    }

    public void setHeaderView(final View headerView) {
        setHeaderAdapter(new HeaderFooterAdapter() {
            @Override
            public int size() {
                return 1;
            }

            @Override
            public int getViewType(int position) {
                return 0;
            }

            @Override
            public PositionBindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new PositionBindableViewHolder(headerView) {
                    @Override
                    public void bind(int position) {
                    }
                };
            }
        });
    }

    protected List<ItemDto> getItemList() {
        return items.items;
    }

    private static class CommonItemCollection implements ItemCollection<ItemDto> {
        List<ItemDto> items;

        public CommonItemCollection withItems(List<ItemDto> items) {
            this.items = items;
            return this;
        }

        @Override
        public int size() {
            return items == null ? 0 : items.size();
        }

        @Override
        public ItemDto get(int position) {
            return items.get(position);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ItemDto item, long id);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, ItemDto item, long id);
    }
}
