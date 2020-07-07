package application.controller;

import application.data.PageReq;
import application.model.User;
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

@RestController
@RequestMapping(value ="/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/pages/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getAllUsersActiveWithPaging(@RequestBody PageReq pageReq) {

        try {
            int page = pageReq.getCurrentPage();
            int size = pageReq.getNumberRecord();

            Pageable paging = PageRequest.of(page,size);

            Page<User> userPage = userService.findAllActiveWithPaging(paging);
            List<User> userList = userPage.getContent();

            if (userList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            for (User user : userList) {
                user.setBirthYear();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("users",userList);
            response.put("currentPage", userPage.getNumber());
            response.put("currentNumberOfRecords", userPage.getNumberOfElements());
            response.put("totalPages", userPage.getTotalPages());
            response.put("totalNumberOfRecords", userPage.getTotalElements());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e ) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getAllUsersActive() {

        try {
            List<User> userList = userService.findAllActive();

            if (userList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            for (User user : userList) {
                user.setBirthYear();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("users",userList);
            response.put("totalNumberOfRecords", userList.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e ) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> insertUser(@RequestBody UserDTO dto) {
       try {
           User user = new User();
           user.setActive(true);
           user.setFirstName(dto.getFirstName());
           user.setSurName(dto.getSurName());
           user.setEmail(dto.getEmail());
           user.setBirthDay(LocalDate.of(dto.getBirthYear(),1,1));
           user.setBirthPlace(dto.getBirthPlace());
           user.setDepartment(dto.getDepartment());
           user.setRole(dto.getRole());

           userService.insertUser(user);

           return new ResponseEntity<>("New user created with ID: "+user.getId(),HttpStatus.OK);

       } catch (Exception e) {
           return new ResponseEntity<>("Error: BAD REQUEST", HttpStatus.BAD_REQUEST);
       }
    }

    @GetMapping(value ="/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {

        Optional<User> user = Optional.ofNullable(userService.findById(id).orElse(null));

        if (user.isEmpty()) {
            return new ResponseEntity<>("User ID is not found.", HttpStatus.NO_CONTENT);}
        else  {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        User user = userService.findById(id).orElse(null);

        if (user == null) {
            return new ResponseEntity<>("User ID is not found.", HttpStatus.NO_CONTENT);
        } else {
            userService.deleteUser(id);
            return new ResponseEntity<>("User deactivated successfully", HttpStatus.OK);
        }
    }

}
