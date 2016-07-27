package jp.cayhanecamel.base;

public class DbInfo {

    public String name;
    
    public int version = -1;

    public DbInfo(String name, int version) {
        this.name = name;
        this.version = version;
    }
}
