package application.repository;

import application.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

    long countByEmail(String email);

}
