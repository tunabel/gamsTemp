package application.service.impl;

import application.model.dto.UserDTO;
import application.model.entity.User;
import application.repository.UserRepository;
import application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public List<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findById(String id) {
        return userRepository.findByFieldWithFixedValue("id", id);
    }

    @Override
    public Page<User> findByQuery(String input, Pageable pageable) {
        return userRepository.findByQuery(input, pageable);
    }

    @Override
    public List<User> findByQuery(String input) {
        return userRepository.findByQuery(input);
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
    public User upsertWithDTO(UserDTO dto) {

        String id = dto.getId();
        User user = new User();

        if (id != null) {
            user.setId(id);
        }

        user.setActive(true);
        user.setFirstName(dto.getFirstName());
        user.setSurName(dto.getSurName());
        user.setEmail(dto.getEmail());
        user.setBirthDay(LocalDate.of(dto.getBirthYear(), Month.JANUARY, 1));
        user.setBirthPlace(dto.getBirthPlace());
        user.setDepartment(dto.getDepartment());
        user.setRole(dto.getRole());

        userRepository.save(user);

        return user;
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean isConnectionOK() {
        return userRepository.isConnectionOK();
    }

    @Override
    public boolean isEmailFormattedCorrectly(String email) {
        Pattern pattern = Pattern.compile("@cmc.com.vn$");
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

}
