package application.model.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetCreateRequestDto {

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
    Set<String> associatedAssetCode;
    @NotNull(message = "Assigned Office site for asset is required")
    String officeSiteId;
    @NotBlank(message = "Personnel-In-Charge's data required")
    String pic;
    @NotNull(message = "Manufacturer's data is required")
    String manufacturerId;
    @NotNull(message = "Supplier's data is required")
    String supplierId;
    double price;
    String purchaseDate;
    int warrantyInMonth;
    int ciaC;
    int ciaI;
    int ciaA;
    int ciaSum;
    String ciaImportance;
    String ciaNote;
}
