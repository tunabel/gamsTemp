package application.repository;

import application.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

    long countByEmail(String email);

    Optional<User> findByEmail(String email);



}
