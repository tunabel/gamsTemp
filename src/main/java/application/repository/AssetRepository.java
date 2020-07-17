package application.repository;

import application.model.entity.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AssetRepository extends MongoRepository<Asset, String>, AssetRepositoryCustom {
}
