package application.repository;

import application.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> findAllActive();

    Page<User> findAllActive(Pageable pageable);

    Page<User> findByQuery(String input, Pageable pageable);

}
