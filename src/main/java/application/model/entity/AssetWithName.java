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

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AssetWithName extends AbstractObject {
    @NotNull
    String assetCode;
    @NotNull(message = "Asset Type is required")
    String assetType;
    @NotNull(message = "Asset Group is required")
    String assetGroup;
    @NotBlank(message = "Asset name is required")
    String name;
    String unit;
    String note;
    Set<String> associatedAsset;
    @NotNull(message = "Assigned site for asset is required")
    String officeSite;
    String pic;
    @NotNull(message = "Manufacturer's data is required")
    String manufacturer;
    @NotNull(message = "Supplier's data is required")
    String supplier;
    double price;
    LocalDate purchaseDate;
    int warrantyInMonth;
    @NotNull
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
