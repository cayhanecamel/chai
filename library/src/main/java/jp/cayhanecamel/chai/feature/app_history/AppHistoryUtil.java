package jp.cayhanecamel.champaca.feature.app_history;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import jp.cayhanecamel.champaca.data.ChampacaConfig;
import jp.cayhanecamel.champaca.data.provider.AppHistory;
import jp.cayhanecamel.champaca.feature.app_history.AppInfo.Type;
import jp.cayhanecamel.champaca.util.ChampacaUtil;

public class AppHistoryUtil {


    /**
     * App履歴を追加する。
     * <p>
     * 連番は{@link AppInfo.Type#WEB_API_REQUEST}または{@link AppInfo.Type#WEB_VIEW_REQUEST}の場合に自動付加<br>
     * 生成時刻は実行時に記録
     * </p>
     *
     * @param appInfo 追加情報
     * @return 付加した連番
     * @see {@link AppHistoryAsync}
     */
    public static long add(AppInfo appInfo) {
        if ((ChampacaConfig.getAppHistoryAddingMode() & ChampacaConfig.CHAMPACA_APP_HISTORY_ADDING_MODE_ALLOW_UTIL) == 0) {
            throw new IllegalStateException("AppHistoryUtil Disallowed");
        }

        return addInternally(appInfo, System.currentTimeMillis());
    }

    static long addInternally(AppInfo info, long createdAt) {
        ContentValues cv = new ContentValues();
        cv.put("type", info.type.toString());

        // サーバリクエストの場合はシーケンスを自動負荷
        long seq = getSeq(info);
        cv.put("seq", seq);
        cv.put("name", getName(info));
        cv.put("content", buildContent(info));
        cv.put("created_at", createdAt);
        ChampacaUtil.getApplicationContext().getContentResolver()
                .insert(AppHistory.CONTENT_URI, cv);

        return seq;
    }

    /**
     * APP履歴：シーケンスを返す。
     *
     * @param info
     * @return
     */
    private static synchronized long getSeq(AppInfo info) {
        if (info.seq > -1) {
            return info.seq;
        }

        if (info.type == Type.WEB_API_REQUEST
                || info.type == Type.WEB_VIEW_REQUEST) {
            long seq = ChampacaConfig.getSeverApiRequestSequence();
            seq++;
            ChampacaConfig.setSeverApiRequestSequence(seq);
            return seq;
        }
        return info.seq;
    }

    /**
     * APP履歴：名前を返す。
     *
     * @param info
     * @return
     */
    private static String getName(AppInfo info) {
        if (!TextUtils.isEmpty(info.apiName)) {
            return info.apiName;
        }

        if (info.json != null) {

            // JSONRPC-Requestの場合はmethodを名前とする。
            if (info.type == Type.WEB_API_REQUEST
                    || info.type == Type.WEB_VIEW_REQUEST) {
                try {
                    JSONObject json = new JSONObject(info.json);
                    if (json.has("jsonrpc") && json.has("method")) {
                        return json.get("method").toString();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (info.type == Type.WEB_API_RESPONSE
                    || info.type == Type.WEB_VIEW_RESPONSE) {

                // JSONRPC-Responseの場合はseqが一致するRequestの名前を利用する。
                String seq = Long.toString(info.seq);
                Cursor cursor = ChampacaUtil
                        .getApplicationContext()
                        .getContentResolver()
                        .query(AppHistory.CONTENT_URI, null, "SEQ=?",
                                new String[]{
                                        seq
                                }, null);
                cursor.moveToFirst();
                return cursor.getString(cursor.getColumnIndex("name"));
            }

        }
        return info.type.toString();
    }

    /**
     * ログ内容を整形する。
     *
     * @param info
     * @return
     */
    private static String buildContent(AppInfo info) {
        StringBuilder builder = new StringBuilder();

        if (!TextUtils.isEmpty(info.server)) {
            builder.append("[server]" + System.getProperty("line.separator"));
            builder.append(info.server + System.getProperty("line.separator"));
            builder.append(System.getProperty("line.separator"));
        }

        if (!TextUtils.isEmpty(info.apiName)) {
            builder.append("[api]" + System.getProperty("line.separator"));
            builder.append(info.apiName + System.getProperty("line.separator"));
            builder.append(System.getProperty("line.separator"));
        }

        if (!TextUtils.isEmpty(info.content)) {
            builder.append("[content]" + System.getProperty("line.separator"));
            builder.append(info.content);
            builder.append(System.getProperty("line.separator"));
        }

        if (!TextUtils.isEmpty(info.json)) {
            try {
                JSONObject json = new JSONObject(info.json);
                builder.append("[json]" + System.getProperty("line.separator"));
                builder.append(json.toString(4) + System.getProperty("line.separator"));
                builder.append(System.getProperty("line.separator"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (!TextUtils.isEmpty(info.header)) {
            builder.append("[header]" + System.getProperty("line.separator"));
            builder.append(info.header + System.getProperty("line.separator"));
        }
        return builder.toString();

    }

}
