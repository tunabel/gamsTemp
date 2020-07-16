package application.service.impl;

import application.controller.exception.BadConnectionException;
import application.controller.exception.EmailExistedException;
import application.controller.exception.RoleNotFoundException;
import application.controller.exception.UserNotFoundException;
import application.model.entity.Role;
import application.model.entity.User;
import application.model.request.UpsertRequest;
import application.repository.RoleRepository;
import application.repository.UserRepository;
import application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private static final String CONNECTION_ERROR = "Connection error";
    private static final String ID_NOT_FOUND = "Submitted id not found: ";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

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
    public long countByUsername(String username) {
        return userRepository.countByEmail(username);
    }

    @Override
    public long countByField(String field, String value) {
        return userRepository.countByField(field, value);
    }

    @Override
    public User upsert(UpsertRequest request) {

        if (!isConnectionOK()) {
            throw new BadConnectionException(CONNECTION_ERROR);
        }

        User user = new User();

        if (request.getId() != null) {
            user.setId(request.getId());

            List<User> userListFoundById = findById(request.getId());

            if (userListFoundById.isEmpty()) {
                throw new UserNotFoundException(ID_NOT_FOUND + request.getId());
            }

            User userFoundById = userListFoundById.get(0);
            List<User> userListFoundByEmail = findByEmail(request.getEmail());

            if (!userListFoundByEmail.isEmpty() && !userListFoundByEmail.get(0).getId().equals(userFoundById.getId())) {
                throw new EmailExistedException("Submitted email of " + request.getEmail() + " existed in the database");
            }
        } else {
            long count = countByUsername(request.getEmail());

            if (count > 0) {
                throw new EmailExistedException("Submitted email of " + request.getEmail() + " existed in the database");
            }
        }

        user.setActive(true);
        user.setFirstName(request.getFirstName());
        user.setSurName(request.getSurName());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setBirthDay(LocalDate.of(request.getBirthYear(), Month.JANUARY, 1));
        user.setBirthPlace(request.getBirthPlace());
        user.setDepartment(request.getDepartment());

        Set<String> strRoles = request.getRoles();
        Set<Role> roles = new HashSet<>();

        for (String strRole : strRoles) {
            Optional<Role> role = roleRepository.findById(strRole);
            if (role.isPresent()) {
                roles.add(role.get());
            } else {
                throw new RoleNotFoundException("Submitted role not found");
            }
        }

        user.setRoles(roles);

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
