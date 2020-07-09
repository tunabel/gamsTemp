package application.controller;

import application.data.PageReq;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/users")
public class UserController implements BaseController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/pages/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> findAllActiveWithPagination(@RequestBody PageReq pageReq, @RequestParam(required = false) String search) {

        try {
            Pageable pageable = PageRequest.of(pageReq.getCurrentPage() - 1, pageReq.getNumberRecord());
            Page<User> userPage;
            if (search == null || search.isEmpty()) {
                userPage = userService.findAllActiveWithPaging(pageable);
            } else {
                userPage = userService.findByQuery(search, pageable);
            }

            List<User> userList = userPage.getContent();

            if (userList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("users", userList);
            response.put("currentPage", userPage.getNumber() + 1);
            response.put("currentNumberOfRecords", userPage.getNumberOfElements());
            response.put("totalPages", userPage.getTotalPages());
            response.put("totalNumberOfRecords", userPage.getTotalElements());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("Get Error", "Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            response.put("Get Error", "Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<Map<String, Object>> insert(@RequestBody UserDTO dto) {
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
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("Post Error", "Bad Request");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> findById(@PathVariable String id) {

        Optional<User> user = userService.findById(id);

        if (user.isPresent()) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User ID is not found.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deactivate(@PathVariable String id) {
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(false);
            userService.update(user);

            return new ResponseEntity<>("User deactivated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User ID is not found.", HttpStatus.NOT_FOUND);
        }
    }


}
