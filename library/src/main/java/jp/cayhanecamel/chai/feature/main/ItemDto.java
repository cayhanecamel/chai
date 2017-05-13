package jp.cayhanecamel.chai.feature.main;

public class ItemDto {

    public String title;
    public String value;
    public String path;
    public Type type;

    public ItemDto(Type type, Object title, Object value, Object path) {
        this.type = type;
        this.title = title.toString();
        this.value = value.toString();
        this.path = path.toString();
    }

    public ItemDto(Type type, Object title, Object value) {
        this.type = type;
        this.title = title.toString();
        this.value = value.toString();
    }

    public enum Type {
        Header(0), Content(1), ContentEnd(1), Blank(2);

        public final int viewIndex;

        Type(int viewIndex) {
            this.viewIndex = viewIndex;
        }
    }
}
