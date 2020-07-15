package application.repository;

import application.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> findAllActive();

    Page<User> findAllActive(Pageable pageable);

    Page<User> findByQuery(String input, Pageable pageable);

    List<User> findByQuery(String input);

    long countByField(String field, String value);

    List<User> findByUsername(String username);

    List<User> findByFieldWithFixedValue(String field, String value);

    boolean isConnectionOK();
}
