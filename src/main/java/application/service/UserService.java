package application.service;

import application.model.entity.User;
import application.model.request.UpsertRequest;
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

    long countByUsername(String username);

    long countByField(String field, String value);

    User upsert(UpsertRequest request);

    void update(User user);

    boolean isConnectionOK();

    boolean isEmailFormattedCorrectly(String email);
}
