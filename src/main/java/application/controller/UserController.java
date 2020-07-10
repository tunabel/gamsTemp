package application.controller;

import application.controller.exception.UserListEmptyException;
import application.model.common.PageReq;
import application.model.entity.User;
import application.model.dto.UserDTO;
import application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/users")
public class UserController implements BaseController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/pages/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> findAllActiveWithPagination(@RequestBody PageReq pageReq, @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(pageReq.getCurrentPage() - 1, pageReq.getNumberRecord());
        Page<User> userPage;
        if (search == null || search.isEmpty()) {
            userPage = userService.findAllActiveWithPaging(pageable);
        } else {
            userPage = userService.findByQuery(search, pageable);
        }

        if (userPage.isEmpty()) {
            throw new UserListEmptyException("User List is empty");
        }

        List<User> userList = userPage.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("users", userList);
        response.put("currentPage", userPage.getNumber() + 1);
        response.put("currentNumberOfRecords", userPage.getNumberOfElements());
        response.put("totalPages", userPage.getTotalPages());
        response.put("totalNumberOfRecords", userPage.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> findAllActive() {

        try {
            List<User> userList = userService.findAllActive();

            if (userList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("users", userList);
            response.put("totalNumberOfRecords", userList.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("Error", "Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<Map<String, Object>> insert(@RequestBody UserDTO dto) {

        long count = userService.countByEmail(dto.getEmail());

        if (count > 0) {
            Map<String, Object> response = new HashMap<>();
            response.put("Post Error", "Email existed");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            User user = new User();
            user.setActive(true);
            user.setFirstName(dto.getFirstName());
            user.setSurName(dto.getSurName());
            user.setEmail(dto.getEmail());
            user.setBirthDay(LocalDate.of(dto.getBirthYear(), 1, 1));
            user.setBirthPlace(dto.getBirthPlace());
            user.setDepartment(dto.getDepartment());
            user.setRole(dto.getRole());

            userService.insert(user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "New user created.");
            response.put("userId", user.getId());
            response.put("user", user);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("Post Error", "Bad Request");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping(value = "/")
    public ResponseEntity<Object> update(@RequestBody UserDTO dto) {

        try {
            List<User> userListFoundById = userService.findById(dto.getId());

            if (userListFoundById.isEmpty()) {
                return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
            }

            User userFoundById = userListFoundById.get(0);
            List<User> userListFoundByEmail = userService.findByEmail(dto.getEmail());

            if (!userListFoundByEmail.isEmpty() && !userListFoundByEmail.get(0).getId().equals(userFoundById.getId())) {
                return new ResponseEntity<>("Email existed. Try again", HttpStatus.BAD_REQUEST);
            }

            User updatedUser = new User();
            updatedUser.setId(dto.getId());
            updatedUser.setFirstName(dto.getFirstName());
            updatedUser.setSurName(dto.getSurName());
            updatedUser.setEmail(dto.getEmail());
            updatedUser.setBirthDay(LocalDate.of(dto.getBirthYear(), Month.JANUARY, 1));
            updatedUser.setBirthPlace(dto.getBirthPlace());
            updatedUser.setDepartment(dto.getDepartment());
            updatedUser.setRole(dto.getRole());
            updatedUser.setActive(true);
            userService.update(updatedUser);

            return new ResponseEntity<>("Updated successfully user id: " + updatedUser.getId(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> findById(@PathVariable String id) {

        List<User> userList = userService.findById(id);

        if (!userList.isEmpty()) {
            return new ResponseEntity<>(userList.get(0), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User ID is not found.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deactivate(@PathVariable String id) {
        List<User> userList = userService.findById(id);

        if (!userList.isEmpty()) {
            User user = userList.get(0);
            user.setActive(false);
            userService.update(user);

            return new ResponseEntity<>("User deactivated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User ID is not found.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/count/")
    public ResponseEntity<Map<String, Object>> count(@RequestParam String field, @RequestParam String value) {

        String[] fieldList = {"firstName", "surName", "email", "birthPlace", "birthYear", "department", "role"};
        if (!Arrays.asList(fieldList).contains(field)) {
            Map<String, Object> response = new HashMap<>();
            response.put("Get Error", "Invalid Field Name");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            long count = userService.countByField(field, value);
            Map<String, Object> response = new HashMap<>();
            response.put("totalCount", count);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("Get Error", "Bad Request");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }
}
