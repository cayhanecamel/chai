package jp.cayhanecamel.champaca.feature.app_history;

public class AppInfo {

    /**
     * サーバ
     */
    public String server;

    /**
     * API
     */
    public String apiName;


    /**
     * ログ種別
     */
    public Type type;

    /**
     * 内容
     */
    public String content;

    /**
     * json
     */
    public String json;

    /**
     * header情報
     */
    public String header;

    /**
     * シーケンス
     */
    public long seq = -1;

    /**
     * 生成時刻
     */
    long createdAt = System.currentTimeMillis();

    /**
     * ログ種別列挙型
     */
    public enum Type {
        WEB_API_REQUEST, WEB_API_RESPONSE, WEB_VIEW_REQUEST, WEB_VIEW_RESPONSE, GCM, LOG_E, LOG_D, LOG_V, LOG_I, LOG_W;

        public static Type enumOf(String value) {
            if (value.equals(WEB_API_REQUEST.toString())) {
                return WEB_API_REQUEST;

            } else if (value.equals(WEB_API_RESPONSE.toString())) {
                return WEB_API_RESPONSE;

            } else if (value.equals(WEB_VIEW_REQUEST.toString())) {
                return WEB_VIEW_REQUEST;

            } else if (value.equals(WEB_VIEW_RESPONSE.toString())) {
                return WEB_VIEW_RESPONSE;

            } else if (value.equals(GCM.toString())) {
                return GCM;

            } else if (value.equals(LOG_E.toString())) {
                return LOG_E;

            } else if (value.equals(LOG_D.toString())) {
                return LOG_D;

            } else if (value.equals(LOG_V.toString())) {
                return LOG_V;

            } else if (value.equals(LOG_I.toString())) {
                return LOG_I;

            } else if (value.equals(LOG_W.toString())) {
                return LOG_W;
            }

            return null;
        }
    }

}
