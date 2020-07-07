package application.service;

import application.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Page<User> findAllActiveWithPaging(Pageable pageable);

    List<User> findAll();

    List<User> findAllActive();

    Optional<User> findById(String id);

    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(String id);
}
