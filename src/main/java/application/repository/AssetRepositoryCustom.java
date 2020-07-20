package application.repository;

import application.model.responsedto.AssetGetResponseDto;
import application.model.responsedto.AssociatedAssetGetResponseDto;

import java.util.List;

public interface AssetRepositoryCustom {

    int countByAssetGroup(String groupId);

    int getHighestIdByAssetGroup(String groupId);

    int getHighestId();

    List<? extends AssetGetResponseDto> findAllAssetGetResponse();

    AssetGetResponseDto findAssetGetResponseById(String id);

    List<AssociatedAssetGetResponseDto> findAssocByName(String name);

}
