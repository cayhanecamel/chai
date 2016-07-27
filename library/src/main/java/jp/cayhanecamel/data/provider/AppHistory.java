package jp.cayhanecamel.data.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class AppHistory implements BaseColumns {

    public static final String TABLE_NAME = "app_history";

    public static final String APPLICATION_HISTORY = "app_history";

    public static final String DISTINCT = "distinct";

    public static final String AUTHORITY_BASE = ".data.provider.app_history_provider";

    public static String AUTHORITY = "jp.cayhanecamel.champaca" + AUTHORITY_BASE;

    public static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + APPLICATION_HISTORY);

    public static Uri CONTENT_URI_DISTINCT = Uri.parse("content://" + AUTHORITY + "/" + DISTINCT);


    public static final String COLUMN_SEQ = "seq";

    public static final String COLUMN_CONTENT = "content";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_TYPE = "type";

    public static final String COLUMN_CREATED_AT = "created_at";

    public static final String COLUMN_ID = "_id";

    public static void setPackageName(String packageName) {
        AUTHORITY = packageName + AUTHORITY_BASE;

        CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + APPLICATION_HISTORY);

        CONTENT_URI_DISTINCT = Uri.parse("content://" + AUTHORITY + "/" + DISTINCT);
    }

}
