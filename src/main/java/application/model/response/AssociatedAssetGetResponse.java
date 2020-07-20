package application.model.response;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class AssociatedAssetGetResponse extends AssetGetResponse {
    @Id
    String id;
    String name;
    String assetCode;
}
