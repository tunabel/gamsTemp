package application.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetGetAllResponse extends AssetGetResponse {
    @Id
    String id;
    String assetCode;
    String assetType;
    String assetGroup;
    String name;
    String officeSite;
    String assetStatus;
    int ciaC;
    int ciaI;
    int ciaA;
    int ciaSum;
    String owner;
    boolean overdue;
}
