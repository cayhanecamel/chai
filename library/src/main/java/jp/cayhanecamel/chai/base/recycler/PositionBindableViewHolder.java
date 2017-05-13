package jp.cayhanecamel.chai.base.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class PositionBindableViewHolder extends RecyclerView.ViewHolder {
    public PositionBindableViewHolder(View view) {
        super(view);
    }

    public abstract void bind(int position);
}
