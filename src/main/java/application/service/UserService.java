package application.service;

import application.model.entity.User;
import application.model.request.PageReq;
import application.model.request.UpsertRequest;
import application.model.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    UserResponse findById(String id);

    List<User> findByEmail(String email);

    Page<UserResponse> findActiveByQueryWithPagination(String input, PageReq pageReq);

    List<UserResponse> findActiveByQuery(String input);

    long countByUsername(String username);

    long countByField(String field, String value);

    UserResponse upsert(UpsertRequest request);

    String deactivate(String id);

    boolean isConnectionOK();
}
