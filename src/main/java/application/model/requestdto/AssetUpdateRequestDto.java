package application.model.requestdto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
public class AssetUpdateRequestDto  {

    @Id
    String id;
    @NotNull(message = "Asset Type is required")
    String assetTypeId;
    @NotNull(message = "Asset Group is required")
    String assetGroupId;
    @NotBlank(message = "Asset name is required")
    String name;
    String unit;
    String note;
    Set<String> associatedAsset;
    @NotNull(message = "Assigned site for asset is required")
    String officeSiteId;
    String pic;
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
    String owner;
    LocalDate assignDateStart;
    LocalDate assignDateEnd;
    boolean overdue;

}