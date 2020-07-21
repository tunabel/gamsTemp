package application.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Document(collection = "assets")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AssetFromProjection extends AbstractObject {
    @NotNull
    String assetCode;
    @NotNull(message = "Asset Type is required")
    String assetTypeId;
    @NotNull(message = "Asset Group is required")
    String assetGroupId;
    @NotBlank(message = "Asset name is required")
    String name;
    String unit;
    String note;
    Set<AssetFromProjection> associatedAsset;
    @NotNull(message = "Assigned site for asset is required")
    String officeSiteId;
    User pic;
    @NotNull(message = "Manufacturer's data is required")
    String manufacturerId;
    @NotNull(message = "Supplier's data is required")
    String supplierId;
    double price;
    LocalDate purchaseDate;
    int warrantyInMonth;
    @NotNull
    String assetStatusId;
    int ciaC;
    int ciaI;
    int ciaA;
    int ciaSum;
    String ciaImportance;
    String ciaNote;
    User owner;
    LocalDate assignDateStart;
    LocalDate assignDateEnd;
    boolean overdue;
}
