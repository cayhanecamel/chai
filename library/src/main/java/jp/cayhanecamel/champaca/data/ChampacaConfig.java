package jp.cayhanecamel.champaca.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.cayhanecamel.champaca.feature.app_history.AppHistoryAsync;
import jp.cayhanecamel.champaca.feature.app_history.AppHistoryFragment;
import jp.cayhanecamel.champaca.feature.app_history.AppHistoryUtil;
import jp.cayhanecamel.champaca.util.ChampacaUtil;

public class ChampacaConfig {

    /**
     * App履歴の追加に{@link AppHistoryUtil}の利用を許可
     */
    public static final int CHAMPACA_APP_HISTORY_ADDING_MODE_ALLOW_UTIL = 0x01;

    /**
     * App履歴の追加に{@link AppHistoryAsync}の利用を許可
     */
    public static final int CHAMPACA_APP_HISTORY_ADDING_MODE_ALLOW_ASYNC = 0x02;

    private static final String SPKEY_CHAMPACA_VERSION_CODE_HISTORY = "SPKEY_CHAMPACA_VERSION_CODE_HISTORY";

    private static final String SPKEY_CHAMPACA_SEVER_API_REQUEST_SEQUENCE = "SPKEY_CHAMPACA_SEVER_API_REQUEST_SEQUENCE";

    private static final String SPKEY_CHAMPACA_APP_HISTORY_LIMIT = "SPKEY_CHAMPACA_APP_HISTORY_LIMIT";

    private static final String SPKEY_CHAMPACA_APP_HISTORY_TYPE_FILTER = "SPKEY_CHAMPACA_APP_HISTORY_TYPE_FILTER";

    private static final String SPKEY_CHAMPACA_APP_HISTORY_NAME_FILTER = "SPKEY_CHAMPACA_APP_HISTORY_NAME_FILTER";

    private static final String SPKEY_CHAMPACA_APP_HISTORY_ADDING_MODE = "SPKEY_CHAMPACA_APP_HISTORY_MODE";

    private static final String SPKEY_CHAMPACA_LAST_SELETED_TAB_INDEX = "SPKEY_CHAMPACA_LAST_SELETED_TAB_INDEX";

    private static SharedPreferences getSP() {
        return ChampacaUtil.getApplicationContext().getSharedPreferences(
                "ChampacaConfig", Context.MODE_PRIVATE);
    }

    /**
     * Shared PreferenceからStringの設定値を取得する<br/>
     * デフォルト値はNull
     *
     * @param sp  Shared Preferenceインスタンス
     * @param key 設定値の検索キー
     * @return 検索キーに対応する設定値
     */
    protected static String getString(SharedPreferences sp, String key) {
        return getString(sp, key, null);
    }

    /**
     * Shared PreferenceからStringの設定値を取得する
     *
     * @param sp           Shared Preferenceインスタンス
     * @param key          設定値の検索キー
     * @param defaultValue 初期値。Shared Preferenceに値が無かった時はこれが返る。
     * @return 検索キーに対応する設定値
     */
    protected static String getString(SharedPreferences sp, String key,
                                      String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    protected static void set(SharedPreferences sp, String key, String value) {
        Editor e = sp.edit();
        e.putString(key, value);
        e.commit();
    }

    /**
     * Shared PreferenceからLongの設定値を取得する<br/>
     * デフォルト値は0
     *
     * @param sp  Shared Preferenceインスタンス
     * @param key 設定値の検索キー
     * @return 検索キーに対応する設定値
     */
    protected static Long getLong(SharedPreferences sp, String key) {
        return sp.getLong(key, 0);
    }

    /**
     * Shared PreferenceからLongの設定値を取得する
     *
     * @param sp           Shared Preferenceインスタンス
     * @param key          設定値の検索キー
     * @param defaultValue 初期値。Shared Preferenceに値が無かった時はこれが返る。
     * @return 検索キーに対応する設定値
     */
    protected static Long getLong(SharedPreferences sp, String key,
                                  long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    protected static void set(SharedPreferences sp, String key, Long value) {
        Editor e = sp.edit();
        e.putLong(key, value);
        e.commit();
    }

    /**
     * Legacyな関数。getBoolean(B大文字)を使いましょう
     *
     * @param sp
     * @param key
     * @return
     */
    protected static boolean getboolean(SharedPreferences sp, String key) {
        return sp.getBoolean(key, false);
    }

    /**
     * Shared PreferenceからBooleanの設定値を取得する<br/>
     * デフォルト値はfalse
     *
     * @param sp  Shared Preferenceインスタンス
     * @param key 設定値の検索キー
     * @return 検索キーに対応する設定値
     */
    protected static boolean getBoolean(SharedPreferences sp, String key) {
        return sp.getBoolean(key, false);
    }

    /**
     * Shared PreferenceからBooleanの設定値を取得する
     *
     * @param sp           Shared Preferenceインスタンス
     * @param key          設定値の検索キー
     * @param defaultValue 初期値。Shared Preferenceに値が無かった時はこれが返る。
     * @return 検索キーに対応する設定値
     */
    protected static boolean getBoolean(SharedPreferences sp, String key,
                                        Boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    protected static void set(SharedPreferences sp, String key, boolean value) {
        Editor e = sp.edit();
        e.putBoolean(key, value);
        e.commit();
    }

    /**
     * Shared Preferenceからintの設定値を取得する<br/>
     * デフォルト値は-1
     *
     * @param sp  Shared Preferenceインスタンス
     * @param key 設定値の検索キー
     * @return 検索キーに対応する設定値
     */
    protected static int getInt(SharedPreferences sp, String key) {
        return sp.getInt(key, -1);
    }

    /**
     * Shared Preferenceからintの設定値を取得する
     *
     * @param sp           Shared Preferenceインスタンス
     * @param key          設定値の検索キー
     * @param defaultValue 初期値。Shared Preferenceに値が無かった時はこれが返る。
     * @return 検索キーに対応する設定値
     */
    protected static int getInt(SharedPreferences sp, String key,
                                int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    protected static void set(SharedPreferences sp, String key, int value) {
        Editor e = sp.edit();
        e.putInt(key, value);
        e.commit();
    }

    /**
     * 削除
     *
     * @param sp
     * @param key
     */
    protected static void remove(SharedPreferences sp, String key) {
        Editor e = sp.edit();
        e.remove(key);
        e.commit();
    }

    /**
     * 起動したversionCode履歴を返す。
     */
    public static String getVersionCodeHistory() {
        return getString(getSP(), SPKEY_CHAMPACA_VERSION_CODE_HISTORY, "");
    }

    /**
     * 起動したversionCode履歴リストを返す。
     */
    public static List<Integer> getVersionCodeHistoryList() {
        List<Integer> list = new ArrayList<Integer>();

        String base = getString(getSP(), SPKEY_CHAMPACA_VERSION_CODE_HISTORY, "");
        if (base.length() == 0) {
            return list;
        }

        String[] versionUpHistorys = getString(getSP(),
                SPKEY_CHAMPACA_VERSION_CODE_HISTORY, "").split(",");
        for (String ver : versionUpHistorys) {
            list.add(Integer.parseInt(ver));
        }

        return list;
    }

    /**
     * 起動したversionCode履歴を設定する。
     *
     * @param values
     */
    public static void setVersionCodeHistory(List<Integer> values) {
        StringBuilder appendable = new StringBuilder();
        Iterator<Integer> parts = values.iterator();

        if (parts.hasNext()) {
            appendable.append(parts.next().toString());
            while (parts.hasNext()) {
                appendable.append(",");
                appendable.append(parts.next().toString());
            }
        }

        set(getSP(), SPKEY_CHAMPACA_VERSION_CODE_HISTORY, appendable.toString());
    }

    /**
     * サーバAPIのリクエストシーケンスを返す。
     */
    public static long getSeverApiRequestSequence() {
        return getLong(getSP(), SPKEY_CHAMPACA_SEVER_API_REQUEST_SEQUENCE);
    }

    /**
     * サーバAPIのリクエストシーケンスを設定する。
     */
    public static void setSeverApiRequestSequence(long value) {
        set(getSP(), SPKEY_CHAMPACA_SEVER_API_REQUEST_SEQUENCE, value);
    }

    /**
     * APP履歴の表示件数を返す。
     */
    public static long getAppHistoryLimit() {
        return getLong(getSP(), SPKEY_CHAMPACA_APP_HISTORY_LIMIT, 100);
    }

    /**
     * APP履歴の表示件数を設定する。
     */
    public static void setAppHistoryLimit(long value) {
        set(getSP(), SPKEY_CHAMPACA_APP_HISTORY_LIMIT, value);
    }

    /**
     * APP履歴の名前フィルターを返す。
     */
    public static String getAppHistoryNameFilter() {
        return getString(getSP(), SPKEY_CHAMPACA_APP_HISTORY_NAME_FILTER,
                AppHistoryFragment.getNoFilter());
    }

    /**
     * APP履歴の名前フィルターを設定する。
     */
    public static void setAppHistoryNameFilter(String value) {
        set(getSP(), SPKEY_CHAMPACA_APP_HISTORY_NAME_FILTER, value);
    }

    /**
     * APP履歴のタイプフィルターを返す。
     */
    public static String getAppHistoryTypeFilter() {
        return getString(getSP(), SPKEY_CHAMPACA_APP_HISTORY_TYPE_FILTER,
                AppHistoryFragment.getNoFilter());
    }

    /**
     * APP履歴のタイプフィルターを設定する。
     */
    public static void setAppHistoryTypeFilter(String value) {
        set(getSP(), SPKEY_CHAMPACA_APP_HISTORY_TYPE_FILTER, value);
    }


    public static int getLastSelectTabIndex() {
        return getInt(getSP(), SPKEY_CHAMPACA_LAST_SELETED_TAB_INDEX, 0);
    }


    public static void setLastSelectTabIndex(int value) {
        set(getSP(), SPKEY_CHAMPACA_LAST_SELETED_TAB_INDEX, value);
    }

    public static int getAppHistoryAddingMode() {
        return getInt(getSP(), SPKEY_CHAMPACA_APP_HISTORY_ADDING_MODE, CHAMPACA_APP_HISTORY_ADDING_MODE_ALLOW_UTIL | CHAMPACA_APP_HISTORY_ADDING_MODE_ALLOW_ASYNC);
    }

    public static void setAppHistoryAddingMode(int value) {
        set(getSP(), SPKEY_CHAMPACA_APP_HISTORY_ADDING_MODE, value);
    }
}