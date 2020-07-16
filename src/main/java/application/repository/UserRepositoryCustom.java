package application.repository;

import application.model.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> findAllActive();

    List<User> findActiveByQuery(String input);

    List<User> findActiveByQueryWithPagination(String input, Pageable pageable);

    long countByField(String field, String value);

    List<User> findByEmail(String email);

    List<User> findByFieldWithFixedValue(String field, String value);

    boolean isConnectionOK();
}
