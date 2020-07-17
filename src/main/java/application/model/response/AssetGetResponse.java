package application.model.response;

import application.model.entity.AbstractObject;
import application.model.entity.Asset;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AssetGetResponse extends AbstractObject {
    String id;
    String assetCode;
    String assetType;
    String assetGroup;
    String name;
    String unit;
    String note;
    Set<Asset> associatedAssetCode;
    String officeSite;
    String pic;
    String manufacturer;
    String supplier;
    double price;
    LocalDate purchaseDate;
    int warrantyInMonth;
    String assetStatus;
    int ciaC;
    int ciaI;
    int ciaA;
    int ciaSum;
    String ciaImportance;
    String ciaNote;
    String owner;
    LocalDate assignDateStart;
    LocalDate assignDateEnd;
    boolean overdue;
}
