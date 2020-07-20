package application.repository;

import application.model.entity.AssetGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AssetGroupRepository extends MongoRepository<AssetGroup, String> {

    List<AssetGroup> findAllByAssetType(int type);

    List<AssetGroup> findAllByCanBeAssociated(boolean canBeAssociated);
}
