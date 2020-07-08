package application.service;

import application.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Page<User> findAllActiveWithPaging(Pageable pageable);

    List<User> findAll();

    List<User> findAllActive();

    Optional<User> findById(String id);

    void insert(User user);

    void update(User user);

    void delete(String id);
}
