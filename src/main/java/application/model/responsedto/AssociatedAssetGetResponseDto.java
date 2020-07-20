package application.model.responsedto;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class AssociatedAssetGetResponseDto extends AssetGetResponseDto {
    @Id
    String id;
    String name;
    String assetCode;
}
