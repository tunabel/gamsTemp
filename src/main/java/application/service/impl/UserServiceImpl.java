package application.service.impl;

import application.model.entity.User;
import application.repository.UserRepository;
import application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> findAllActiveWithPaging(Pageable pageable) {
        return userRepository.findAllActive(pageable);
    }

    @Override
    public List<User> findAllActive() {
        return userRepository.findAllActive();
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Page<User> findByQuery(String input, Pageable pageable) {
        return userRepository.findByQuery(input, pageable);
    }

    @Override
    public long countByEmail(String email) {
        return userRepository.countByEmail(email);
    }

    @Override
    public long countByField(String field, String value) {
        return userRepository.countByField(field, value);
    }

    @Override
    public void insert(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void update(User user) {
        userRepository.save(user);
    }
}
