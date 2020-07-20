package application.model.common;

public enum AssetField {
    ID("id"),
    CODE("assetCode"),
    TYPE("assetType"),
    GROUP("assetGroup"),
    NAME("name"),
    UNIT("unit"),
    NOTE("note"),
    ASSOCIATED("associatedAssetCode"),
    OFFICE("officeSite"),
    PIC("pic"),
    MANUFACTURER("manufacturer"),
    SUPPLIER("supplier"),
    PRICE("price"),
    PURCHASE("purchaseDate"),
    WARRANTY("warrantyInMonth"),
    STATUS("assetStatus"),
    CIA_C("ciaC"),
    CIA_I("ciaI"),
    CIA_A("ciaA"),
    CIA_SUM("ciaSum"),
    CIA_IMPORTANCE("ciaImportance"),
    CIA_NOTE("ciaNote"),
    OWNER("owner"),
    ASSIGN_START("assignDateStart"),
    ASSIGN_END("assignDateEnd"),
    OVERDUE("overdue");

    private final String name;

    AssetField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}