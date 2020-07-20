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
public class AssetGetOneResponseDto extends AssetGetResponseDto {
    @Id
    String id;
    String assetCode;
    String assetType;
    String assetGroup;
    String name;
    String unit;
    String note;
    Set<String> associatedAssetCode;
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
