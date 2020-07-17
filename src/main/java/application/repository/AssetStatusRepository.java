package application.repository;

import application.model.entity.AssetStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AssetStatusRepository extends MongoRepository<AssetStatus, String> {
}
