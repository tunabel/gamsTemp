package application.controller;

import application.model.requestdto.PaginationRequestDto;
import application.model.requestdto.UpsertRequestDto;
import application.model.responsedto.UserResponseDto;
import application.model.responsedto.UserShortResponseDto;
import application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Autowired
    private UserService userService;

    @GetMapping(value = "/pages/", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DUL') or hasRole('GRL') or hasRole('ADM') or hasRole('SEP') or hasRole('BOD') or hasRole('SAD')")
    public ResponseEntity<Map<String, Object>> findActiveWithPagination(@RequestBody PaginationRequestDto paginationRequestDto, @RequestParam(required = false) String search) {
        Page<UserResponseDto> userPage = userService.findActiveByQueryWithPagination(search, paginationRequestDto);

        List<UserResponseDto> userList = userPage.getContent();
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
    public ResponseEntity<Map<String, Object>> findActive(@RequestParam(required = false) String search) {
        List<UserResponseDto> userList = userService.findActiveByQuery(search);

        Map<String, Object> response = new HashMap<>();
        response.put("users", userList);
        response.put("totalNumberOfRecords", userList.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('DUL') or hasRole('GRL') or hasRole('ADM') or hasRole('SEP') or hasRole('BOD') or hasRole('SAD')")
    public ResponseEntity<Object> findById(@PathVariable String id) {
        UserResponseDto user = userService.findById(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/")
//    @PreAuthorize("hasRole('ADM') or hasRole('SAD')")
    public ResponseEntity<Map<String, Object>> insert(@RequestBody @Valid UpsertRequestDto request) {
        UserResponseDto user = userService.upsert(request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "New user created.");
        response.put("userId", user.getId());
        response.put("user", user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = "/import/")
//    @PreAuthorize("hasRole('ADM') or hasRole('SAD')")
    public ResponseEntity<Map<String, Object>> insertMany(@RequestBody @Valid List<UpsertRequestDto> requestList) {
        List<UserResponseDto> responseList = userService.insertMany(requestList);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "List user imported.");
        response.put("count", responseList.size());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/")
    @PreAuthorize("hasRole('ADM') or hasRole('SAD')")
    public ResponseEntity<Map<String, Object>> update(@RequestBody @Valid UpsertRequestDto request) {
        UserResponseDto updatedUser = userService.upsert(request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Updated successfully user: " + updatedUser.getFirstName() + " " + updatedUser.getSurName());
        response.put("userId", updatedUser.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADM') or hasRole('SAD')")
    public ResponseEntity<Object> deactivate(@PathVariable String id) {
        String name = userService.deactivate(id);

        return new ResponseEntity<>("User " + name + " deactivated successfully!", HttpStatus.OK);
    }

    @GetMapping(value = "/count/")
    public ResponseEntity<Map<String, Object>> count(@RequestParam String field, @RequestParam String value) {
        long count = userService.countByField(field, value);

        Map<String, Object> response = new HashMap<>();
        response.put("totalCount", count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/short/")
    public ResponseEntity<Map<String, Object>> getShortList(@RequestParam String search) {

        List<UserShortResponseDto> responseDtoList = userService.findShortListByBothName(search);

        Map<String, Object> response = new HashMap<>();
        response.put("results", responseDtoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
