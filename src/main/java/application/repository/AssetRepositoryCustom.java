package application.repository;

import application.model.response.AssetGetResponse;

import java.util.List;

public interface AssetRepositoryCustom {

    int countByAssetGroup(String groupId);

    int getHighestIdByAssetGroup(String groupId);

    int getHighestId();

    List<AssetGetResponse> findAllAssetGetResponse();

}
