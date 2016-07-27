package jp.cayhanecamel.base;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import jp.cayhanecamel.feature.sqlite.TableInfo;

public class ProductInfos {

    private static ProductInfos self = new ProductInfos();

    private ProductInfos() {
        self = this;
    }

    public static ProductInfos get() {
        return self;
    }

    public List<DbInfo> dbInfos;
    public boolean isDebug;
    public List<String> types;
    public List<String> values;
    public String debugMailAddress;
    public List<TableInfo> tableInfos = new ArrayList<TableInfo>();

    public LinkedHashMap<String, String> infos;

}
