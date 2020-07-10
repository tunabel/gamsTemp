package application.service;

import application.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<User> findAllActiveWithPaging(Pageable pageable);

    List<User> findAllActive();

    List<User> findById(String id);

    List<User> findByEmail(String email);

    Page<User> findByQuery(String input, Pageable pageable);

    List<User> findByQuery(String input);

    long countByEmail(String email);

    long countByField(String field, String value);

    void insert(User user);

    void update(User user);
}
