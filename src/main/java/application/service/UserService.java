package application.service;

import application.model.entity.User;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Page<User> findAllActiveWithPaging(Pageable pageable);

    List<User> findAllActive();

    Optional<User> findById(String id);

    Page<User> findByQuery(String input, Pageable pageable);

    long countByEmail(String email);

    long countByField(String field, String value);

    void insert(User user);

    Optional<User> findByEmail(String email);

    void update(User user);
}
