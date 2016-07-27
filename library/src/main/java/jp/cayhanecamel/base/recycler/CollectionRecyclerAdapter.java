package jp.cayhanecamel.base.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public abstract class CollectionRecyclerAdapter<D, V extends View & ItemBindable<D>> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int ITEM_POSITION_HEADER = -1_000;
    protected static final int ITEM_POSITION_FOOTER = -2_000;
    public static final int MAX_HEADER_FOOTER_COUNT = 100;

    private ItemCollection<D> items;
    private HeaderFooterAdapter headerAdapter;
    private HeaderFooterAdapter footerAdapter;

    public CollectionRecyclerAdapter(ItemCollection<D> items) {
        this.items = items;
        headerAdapter = footerAdapter = EmptyHeaderFooterAdapter.getInstance();
    }

    public void setHeaderAdapter(HeaderFooterAdapter headerAdapter) {
        this.headerAdapter = headerAdapter == null ? EmptyHeaderFooterAdapter.getInstance() : headerAdapter;
        if (this.headerAdapter.size() > MAX_HEADER_FOOTER_COUNT)
            throw new IllegalArgumentException();
    }

    public void setFooterAdapter(HeaderFooterAdapter footerAdapter) {
        this.footerAdapter = footerAdapter == null ? EmptyHeaderFooterAdapter.getInstance() : footerAdapter;
        if (this.footerAdapter.size() > MAX_HEADER_FOOTER_COUNT)
            throw new IllegalArgumentException();
    }

    protected int toItemPosition(int rawPosition) {
        int itemPosition = rawPosition - headerAdapter.size();
        if (!headerAdapter.isEmpty() && itemPosition < 0) return ITEM_POSITION_HEADER;
        if (!footerAdapter.isEmpty() && itemPosition >= items.size())
            return ITEM_POSITION_FOOTER;
        return itemPosition;
    }

    protected int toRawPosition(int itemPosition) {
        return itemPosition + headerAdapter.size();
    }

    protected ItemCollection<D> getItems() {
        return items;
    }

    protected abstract V newView(int viewType);

    @Override
    public final int getItemViewType(int position) {
        int itemPosition = toItemPosition(position);
        switch (itemPosition) {
            case ITEM_POSITION_HEADER:
                return itemPosition - headerAdapter.getViewType(position);
            case ITEM_POSITION_FOOTER:
                return itemPosition - footerAdapter.getViewType(position - items.size() - headerAdapter.size());
            default:
                return getItemViewTypeByItemPosition(toItemPosition(position));
        }
    }

    // Override me if needed multi-view type.
    protected int getItemViewTypeByItemPosition(int itemPosition) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType <= ITEM_POSITION_FOOTER) {
            return footerAdapter.onCreateViewHolder(parent, -viewType + ITEM_POSITION_FOOTER);
        } else if (viewType <= ITEM_POSITION_HEADER) {
            return headerAdapter.onCreateViewHolder(parent, -viewType + ITEM_POSITION_HEADER);
        } else {
            return new ItemViewHolder<>(newView(viewType));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemPosition = toItemPosition(position);
        switch (itemPosition) {
            case ITEM_POSITION_HEADER:
                ((PositionBindableViewHolder) holder).bind(position);
                return;
            case ITEM_POSITION_FOOTER:
                ((PositionBindableViewHolder) holder).bind(position - items.size() - headerAdapter.size());
                return;
            default:
                ((ItemViewHolder<D>) holder).getBindable().bind(items.get(itemPosition));
        }
    }

    @Override
    public int getItemCount() {
        // NOTICE: Raw Count!
        return headerAdapter.size() + items.size() + footerAdapter.size();
    }

    public D getItem(int position) {
        return items.get(position);
    }

    public static abstract class HeaderFooterAdapter {
        public abstract int size();

        public boolean isEmpty() {
            return size() == 0;
        }

        public abstract int getViewType(int position);

        public abstract PositionBindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType);
    }

    public static class EmptyHeaderFooterAdapter extends HeaderFooterAdapter {
        private static EmptyHeaderFooterAdapter sInstance;

        public static synchronized EmptyHeaderFooterAdapter getInstance() {
            if (sInstance == null) {
                sInstance = new EmptyHeaderFooterAdapter();
            }
            return sInstance;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public int getViewType(int position) {
            return 0;
        }

        @Override
        public PositionBindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }
    }

    private static class ItemViewHolder<D> extends RecyclerView.ViewHolder {

        public <V extends View & ItemBindable<D>> ItemViewHolder(V itemView) {
            super(itemView);
        }

        @SuppressWarnings("unchecked")
        public ItemBindable<D> getBindable() {
            return (ItemBindable<D>) itemView;
        }
    }
}
