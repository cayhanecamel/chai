package jp.cayhanecamel.champaca.feature.file_explorer;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.cayhanecamel.champaca.R;

public class ExplorerContext {

    private List<PathSegment> mPathSegments = new ArrayList<PathSegment>();

    public void init() {
        mPathSegments = new ArrayList<PathSegment>();
    }

    public void pushPathSegment(PathSegment... pathSegments) {
        mPathSegments.addAll(Arrays.asList(pathSegments));
    }

    public void popPathSegment(PathSegment... pathSegments) {
        // Check for fail safe
        int firstLocation = mPathSegments.size() - pathSegments.length;
        int location = firstLocation;
        if (location < 0) {
            Log.d(getClass().getSimpleName(), "Pop Failed!");
            return;
        }

        for (PathSegment pathSegment : pathSegments) {
            if (!mPathSegments.get(location).getName().equals(pathSegment.getName())) {
                Log.d(getClass().getSimpleName(), "Pop Failed!");
                return;
            }
        }

        for (location = mPathSegments.size() - 1; location >= firstLocation; location--) {
            mPathSegments.remove(location);
        }
    }

    public String getCurrentDirPath() {
        return PathSegment.joinNames(mPathSegments, PathSegment.SEPARATOR);
    }

    public View createBreadcrumbs(Context context, final OnPathSegmentClickListener onPathSegmentClickListener) {
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.HORIZONTAL);

        SpannableStringBuilder buf = new SpannableStringBuilder();
        int index = 0;
        for (PathSegment pathSegment : mPathSegments.subList(0, mPathSegments.size() - 1)) {
            String fullPath = PathSegment.joinNames(mPathSegments.subList(0, ++index), PathSegment.SEPARATOR);
            buf.append(createClickableSpan(pathSegment, fullPath, onPathSegmentClickListener))
                    .append(" > ");
        }
        buf.append(mPathSegments.get(mPathSegments.size() - 1).getDisplayName());

        TextView breadcrumb = new TextView(context);
        breadcrumb.setText(buf);
        breadcrumb.setMovementMethod(LinkMovementMethod.getInstance());
        breadcrumb.setTextColor(context.getResources().getColor(R.color.jp_cayhanecamel_champaca_half_black));
        container.addView(breadcrumb);

        horizontalScrollView.addView(container);
        int padding = Math.round(context.getResources().getDisplayMetrics().density * 4);
        horizontalScrollView.setPadding(padding, padding, padding, padding);
        return horizontalScrollView;
    }

    private static Spannable createClickableSpan(final PathSegment pathSegment, final String fullPath,
                                                 final OnPathSegmentClickListener onPathSegmentClickListener) {
        String link = pathSegment.getDisplayName();
        Spannable spannable = Spannable.Factory.getInstance().newSpannable(link);
        spannable.setSpan(
                new ClickableSpan() {

                    @Override
                    public void onClick(View widget) {
                        onPathSegmentClickListener.onClick(pathSegment, fullPath);
                    }
                }, 0, link.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public interface OnPathSegmentClickListener {
        void onClick(PathSegment pathSegment, String fullPath);
    }
}
