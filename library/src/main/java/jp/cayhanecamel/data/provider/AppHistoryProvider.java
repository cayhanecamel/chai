package jp.cayhanecamel.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

import jp.cayhanecamel.db.ChampacaOpenHelper;

/**
 * AppicationHistoryProviderクラス
 */
public class AppHistoryProvider extends ContentProvider {

    private static final UriMatcher uriMatcher;

    public static final int APPLICATION_HISTORY = 1;
    public static final int DISTINCT = 2;

    private static HashMap<String, String> mApplicationHistoryMap;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AppHistory.AUTHORITY, AppHistory.APPLICATION_HISTORY,
                APPLICATION_HISTORY);

        uriMatcher.addURI(AppHistory.AUTHORITY, AppHistory.DISTINCT, DISTINCT);

        mApplicationHistoryMap = new HashMap<String, String>();
        mApplicationHistoryMap.put(AppHistory._ID, "_id");
        mApplicationHistoryMap
                .put(AppHistory.COLUMN_SEQ, AppHistory.COLUMN_SEQ);
        mApplicationHistoryMap.put(AppHistory.COLUMN_CONTENT,
                AppHistory.COLUMN_CONTENT);
        mApplicationHistoryMap.put(AppHistory.COLUMN_NAME,
                AppHistory.COLUMN_NAME);
        mApplicationHistoryMap.put(AppHistory.COLUMN_TYPE,
                AppHistory.COLUMN_TYPE);
        mApplicationHistoryMap.put(AppHistory.COLUMN_CREATED_AT,
                AppHistory.COLUMN_CREATED_AT);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = ChampacaOpenHelper.get().getWritableDatabase();

        db.insert(AppHistory.TABLE_NAME, null, values);

        getContext().getContentResolver().notifyChange(uri, null);

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = ChampacaOpenHelper.get().getWritableDatabase();
        db.delete(AppHistory.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(AppHistory.TABLE_NAME);
        qb.setProjectionMap(mApplicationHistoryMap);

        switch (uriMatcher.match(uri)) {
            case APPLICATION_HISTORY:
                break;

            case DISTINCT:
                qb.setDistinct(true);
                break;

            default:
                break;
        }

        SQLiteDatabase db = ChampacaOpenHelper.get().getWritableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }

}
