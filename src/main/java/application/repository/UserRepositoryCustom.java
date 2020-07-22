package application.repository;

import application.model.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> findAllActive();

    List<User> findActiveByQueryingAllTextFields(String input);

    User findActiveAdminById(String id);

    List<User> findActiveByQueryWithPagination(String input, Pageable pageable);

    long countActiveByField(String field, String value);

    List<User> findByEmail(String email);

    List<User> findActiveByFieldWithFixedValue(String field, String value);

    List<User> findByBothName(String search);

    boolean isConnectionOK();


}
