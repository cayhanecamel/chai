package jp.cayhanecamel.champaca.feature.file_explorer;

import android.content.Context;
import android.text.TextUtils;

import java.util.Iterator;

public class PathSegment {
    public static final String NAME_ROOT = "/";
    public static final String NAME_PARENT = "build/generated/source/rs";
    public static final String SEPARATOR = "/";
    private String mName;
    private String mDisplayName;

    public PathSegment(String name, String displayName) {
        mName = name;
        mDisplayName = displayName;
    }

    public PathSegment(String name) {
        this(name, name);
    }

    public PathSegment(String name, int displayName, Context context) {
        this(name, context.getString(displayName));
    }

    public String getName() {
        return mName;
    }

    public String getDisplayName() {
        return TextUtils.isEmpty(mDisplayName) ? mName : mDisplayName;
    }

    public static String joinNames(Iterable<PathSegment> pathSegments, String delimiter) {
        // "/": return "/"
        // "/", "jp_cayhanecamel_champaca_table_page" : return "/jp_cayhanecamel_champaca_table_page"
        StringBuilder buf = new StringBuilder();
        Iterator<PathSegment> iterator = pathSegments.iterator();
        PathSegment pathSegment = iterator.next();
        String rootName = pathSegment.getName();
        if (rootName.equals(NAME_ROOT)) {
            buf.append(NAME_ROOT);
        } else {
            buf.append(rootName)
                    .append(delimiter);
        }

        while (iterator.hasNext()) {
            buf.append(iterator.next().getName())
                    .append(delimiter);
        }

        return buf.length() == 1 ? buf.toString() : buf.substring(0, buf.length() - 1);
    }
}
