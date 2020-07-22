package application.repository;

import application.model.entity.Asset;
import application.model.entity.AssetWithName;

import java.util.List;

public interface AssetRepositoryCustom {

    List<Asset> getSimpleListOfIdByAssetGroupIdOrAll(String groupId);

    List<AssetWithName> findAllWithName();

    Asset findByIdCustom(String id);

    List<AssetWithName> findListByFields(String code, String name, String group, String owner);

    List<Asset> listAvailableAssocNameByField(String field, String query);

    Asset findAvailableAssocByAssetCode(String assetCode);


}
