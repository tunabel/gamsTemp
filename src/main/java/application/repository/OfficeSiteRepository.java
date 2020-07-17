package application.repository;

import application.model.entity.OfficeSite;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OfficeSiteRepository extends MongoRepository<OfficeSite, String> {
}
