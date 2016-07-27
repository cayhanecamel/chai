package jp.cayhanecamel.champaca.feature.sqlite;

public class TableInfo {

    public String tableName;
    public String columName;
    public ConvertType convert;
    public int multiply = 1;

    public static enum ConvertType {
        DATE;
    }

}
