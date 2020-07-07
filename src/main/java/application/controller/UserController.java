package application.controller;

import application.model.User;
import application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value ="/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public List<User> getAllUsers() {
        return userService.findAllByOrderById();
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> insertUser(@RequestBody User user) {
        userService.insertUser(user);
        return new ResponseEntity<>("User added successfully", HttpStatus.OK);
    }

}
