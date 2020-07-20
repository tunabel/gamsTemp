package application.service.impl;

import application.controller.exception.*;
import application.model.common.UserField;
import application.model.entity.Role;
import application.model.entity.User;
import application.model.request.PageReq;
import application.model.request.UpsertRequest;
import application.model.response.UserResponse;
import application.repository.RoleRepository;
import application.repository.UserRepository;
import application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final String CONNECTION_ERROR = "Connection error";
    private static final String ID_NOT_FOUND = "Submitted User Id not found: ";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    UserResponse createUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getSurName(),
                user.getEmail(),
                user.getBirthYear(),
                user.getBirthPlace(),
                user.getDepartment(),
                user.getRoles()
        );
    }

    @Override
    public List<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserResponse findById(String id) {
        if (!isConnectionOK()) {
            throw new BadConnectionException(CONNECTION_ERROR);
        }

        List<User> userList = userRepository.findByFieldWithFixedValue("id", id);

        if (userList.isEmpty()) {
            throw new ItemNotFoundException(ID_NOT_FOUND + id);
        }

        return createUserResponse(userList.get(0));
    }

    @Override
    public Page<UserResponse> findActiveByQueryWithPagination(String input, PageReq pageReq) {
        if (!isConnectionOK()) {
            throw new BadConnectionException(CONNECTION_ERROR);
        }
        if (pageReq.getCurrentPage() <= 0) {
            throw new PageRequestInvalidException("Page index must not be less than zero");
        }
        Pageable pageable = PageRequest.of(pageReq.getCurrentPage() - 1, pageReq.getNumberRecord());
        List<User> userList;

        if (input == null || input.isEmpty()) {
            userList = userRepository.findAllActive();
        } else {
            userList = userRepository.findActiveByQueryWithPagination(input, pageable);
        }

        int fetchedListSize = userList.size();
        int pageNoReq = pageable.getPageNumber();
        int pageSzReq = pageable.getPageSize();

        int totalFetchedPage = fetchedListSize / pageSzReq + ((fetchedListSize % pageSzReq) > 0 ? 1 : 0);

        if (pageNoReq >= totalFetchedPage) {
            throw new PageRequestInvalidException("Page number exceeds limit");
        }

        int startingIndex = (pageNoReq == 0) ? 0 : pageNoReq * pageSzReq;
        int endIndex = (pageNoReq + 1 == totalFetchedPage) ? fetchedListSize : (pageNoReq + 1) * pageSzReq;

        List<User> pageList = new ArrayList<>();

        for (int i = startingIndex; i < endIndex; i++) {
            pageList.add(userList.get(i));
        }

        if (pageList.isEmpty()) {
            throw new UserListEmptyException("User List is empty");
        }

        List<UserResponse> userResponseList = new ArrayList<>();
        for (User user : pageList) {
            userResponseList.add(createUserResponse(user));
        }

        return new PageImpl<UserResponse>(userResponseList, pageable, fetchedListSize);
    }

    @Override
    public List<UserResponse> findActiveByQuery(String input) {

        if (!isConnectionOK()) {
            throw new BadConnectionException(CONNECTION_ERROR);
        }

        List<User> userList;
        if (input == null || input.isEmpty()) {
            userList = userRepository.findAllActive();
        } else {
            userList = userRepository.findActiveByQuery(input);
        }

        if (userList.isEmpty()) {
            throw new UserListEmptyException("User List is empty");
        }

        List<UserResponse> userResponseList = new ArrayList<>();

        for (User user : userList) {
            userResponseList.add(createUserResponse(user));
        }

        return userResponseList;
    }

    @Override
    public long countByUsername(String username) {
        return userRepository.countByEmail(username);
    }

    @Override
    public long countByField(String field, String value) {
        String[] fieldList = {
                UserField.FIRSTNAME.getName(),
                UserField.SURNAME.getName(),
                UserField.EMAIL.getName(),
                UserField.DEPARTMENT.getName(),
                UserField.BIRTHYEAR.getName(),
                UserField.BIRTHPLACE.getName(),
                UserField.ROLE.getName(),
        };

        if (!Arrays.asList(fieldList).contains(field)) {
            throw new UserFieldIncorrectException("Incorrect parameter of user field");
        }

        return userRepository.countByField(field, value);
    }

    @Override
    public UserResponse upsert(UpsertRequest request) {

        if (!isConnectionOK()) {
            throw new BadConnectionException(CONNECTION_ERROR);
        }

        User user = new User();

        if (request.getId() != null) {
            user.setId(request.getId());

            UserResponse userFoundById = findById(request.getId());

            if (userFoundById == null) {
                throw new ItemNotFoundException(ID_NOT_FOUND + request.getId());
            }

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

        return createUserResponse(user);
    }

    @Override
    public String deactivate(String id) {

        if (!isConnectionOK()) {
            throw new BadConnectionException(CONNECTION_ERROR);
        }

        List<User> userList = userRepository.findByFieldWithFixedValue("id", id);

        if (userList.isEmpty()) {
            throw new ItemNotFoundException(ID_NOT_FOUND + id);
        }
        User user = userList.get(0);
        user.setActive(false);

        userRepository.save(user);

        return user.getFirstName() + " " + user.getSurName();
    }

    @Override
    public boolean isConnectionOK() {
        return userRepository.isConnectionOK();
    }
}
