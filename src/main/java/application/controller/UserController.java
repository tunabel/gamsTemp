package application.controller;

import application.controller.exception.*;
import application.model.request.PageReq;
import application.model.common.UserField;
import application.model.entity.User;
import application.model.request.UpsertRequest;
import application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/users")
public class UserController extends BaseController {


    private static final String CONNECTION_ERROR = "Connection error";
    private static final String ID_NOT_FOUND = "Submitted id not found: ";

    @Autowired
    private UserService userService;

    @GetMapping(value = "/pages/", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DUL') or hasRole('GRL') or hasRole('ADM') or hasRole('SEP') or hasRole('BOD') or hasRole('SAD')")
    public ResponseEntity<Map<String, Object>> findAllActiveWithPagination(@RequestBody PageReq pageReq, @RequestParam(required = false) String search) {

        if (!userService.isConnectionOK()) {
            throw new BadConnectionException(CONNECTION_ERROR);
        }

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
    @PreAuthorize("hasRole('DUL') or hasRole('GRL') or hasRole('ADM') or hasRole('SEP') or hasRole('BOD') or hasRole('SAD')")
    public ResponseEntity<Map<String, Object>> findAllActive(@RequestParam(required = false) String search) {

        if (!userService.isConnectionOK()) {
            throw new BadConnectionException(CONNECTION_ERROR);
        }

        List<User> userList;
        if (search == null || search.isEmpty()) {
            userList = userService.findAllActive();
        } else {
            userList = userService.findByQuery(search);
        }

        if (userList.isEmpty()) {
            throw new UserListEmptyException("User List is empty");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("users", userList);
        response.put("totalNumberOfRecords", userList.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('DUL') or hasRole('GRL') or hasRole('ADM') or hasRole('SEP') or hasRole('BOD') or hasRole('SAD')")
    public ResponseEntity<Object> findById(@PathVariable String id) {

        if (!userService.isConnectionOK()) {
            throw new BadConnectionException(CONNECTION_ERROR);
        }

        List<User> userList = userService.findById(id);

        if (userList.isEmpty()) {
            throw new UserNotFoundException(ID_NOT_FOUND + id);
        }

        return new ResponseEntity<>(userList.get(0), HttpStatus.OK);
    }

    @PostMapping(value = "/")
//    @PreAuthorize("hasRole('ADM') or hasRole('SAD')")
    public ResponseEntity<Map<String, Object>> insert(@RequestBody @Valid UpsertRequest request) {
        User user = userService.upsert(request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "New user created.");
        response.put("userId", user.getId());
        response.put("user", user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping(value = "/")
    @PreAuthorize("hasRole('ADM') or hasRole('SAD')")
    public ResponseEntity<Map<String, Object>> update(@RequestBody @Valid UpsertRequest dto) {
        User updatedUser = userService.upsert(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Updated successfully user: " + updatedUser.getFirstName() + " " + updatedUser.getSurName());
        response.put("userId", updatedUser.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADM') or hasRole('SAD')")
    public ResponseEntity<Object> deactivate(@PathVariable String id) {
        if (!userService.isConnectionOK()) {
            throw new BadConnectionException(CONNECTION_ERROR);
        }

        List<User> userList = userService.findById(id);

        if (userList.isEmpty()) {
            throw new UserNotFoundException(ID_NOT_FOUND + id);
        }

        User user = userList.get(0);
        user.setActive(false);
        userService.update(user);

        return new ResponseEntity<>("User deactivated successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/count/")
    public ResponseEntity<Map<String, Object>> count(@RequestParam String field, @RequestParam String value) {

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

        long count = userService.countByField(field, value);
        Map<String, Object> response = new HashMap<>();
        response.put("totalCount", count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
