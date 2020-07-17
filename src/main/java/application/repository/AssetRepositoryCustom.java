package application.repository;

public interface AssetRepositoryCustom {

    int countByAssetGroup(String groupId);

    int getHighestIdByAssetGroup(String groupId);

    int getHighestId();

}
