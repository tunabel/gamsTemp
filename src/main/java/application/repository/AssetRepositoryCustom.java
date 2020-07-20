package application.repository;

import application.model.response.AssetGetResponse;
import application.model.response.AssociatedAssetGetResponse;

import java.util.List;

public interface AssetRepositoryCustom {

    int countByAssetGroup(String groupId);

    int getHighestIdByAssetGroup(String groupId);

    int getHighestId();

    List<? extends AssetGetResponse> findAllAssetGetResponse();

    AssetGetResponse findAssetGetResponseById(String id);

    List<AssociatedAssetGetResponse> findAssocByName(String name);

}
