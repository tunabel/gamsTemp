package application.repository;

import application.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{active: true}")
    Page<User> findAllActive(Pageable pageable);

    @Query("{active: true}")
    List<User> findAllActive();

}
