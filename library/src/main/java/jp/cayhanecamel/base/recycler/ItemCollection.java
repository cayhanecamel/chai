package jp.cayhanecamel.base.recycler;

public interface ItemCollection<D> {
    int size();

    D get(int position);
}
