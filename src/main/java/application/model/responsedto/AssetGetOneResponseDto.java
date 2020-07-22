package application.model.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetGetOneResponseDto {
    @Id
    String id;
    String assetCode;
    String assetType;
    String assetGroup;
    String name;
    String unit;
    String note;
    Set<AssetShortResponseDto> associatedAssetCode;
    String officeSite;
    UserShortResponseDto pic;
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
    UserShortResponseDto owner;
    LocalDate assignDateStart;
    LocalDate assignDateEnd;
    boolean overdue;
}