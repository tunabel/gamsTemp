package application.model.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetCreateRequestDto {

    @NotNull(message = "Asset Type is required")
    String assetTypeId;
    @NotNull(message = "Asset Group is required")
    String assetGroupId;
    @NotBlank(message = "Asset name is required")
    String name;
    String unit;
    String note;
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
}