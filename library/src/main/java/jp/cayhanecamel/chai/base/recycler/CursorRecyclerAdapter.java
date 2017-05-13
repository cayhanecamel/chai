package jp.cayhanecamel.chai.base.recycler;

import android.database.Cursor;
import android.view.View;

public abstract class CursorRecyclerAdapter<V extends View & ItemBindable<Cursor>> extends CollectionRecyclerAdapter<Cursor, V> {

    private CursorItemCollection cursorItemCollection;

    public CursorRecyclerAdapter() {
        this(new CursorItemCollection());
    }

    protected CursorRecyclerAdapter(CursorItemCollection cursorItemCollection) {
        super(cursorItemCollection);
        this.cursorItemCollection = cursorItemCollection;
    }

    public Cursor swapCursor(Cursor cursor) {
        Cursor old = cursorItemCollection.cursor;
        cursorItemCollection.cursor = cursor;
        notifyDataSetChanged();
        return old;
    }

    private static class CursorItemCollection implements ItemCollection<Cursor> {
        public Cursor cursor;

        @Override
        public int size() {
            return cursor == null ? 0 : cursor.getCount();
        }

        @Override
        public Cursor get(int position) {
            Cursor cursor = this.cursor;
            if (cursor != null) {
                cursor.moveToPosition(position);
            }
            return cursor;
        }
    }
}
