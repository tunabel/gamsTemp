package application.repository;

import application.model.entity.AssetType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AssetTypeRepository extends MongoRepository<AssetType, String> {
}
