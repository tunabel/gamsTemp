package application.service;

import application.model.entity.User;
import application.model.requestdto.PaginationRequestDto;
import application.model.requestdto.UpsertRequestDto;
import application.model.responsedto.UserResponseDto;
import application.model.responsedto.UserShortResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    UserResponseDto findById(String id);

    List<User> findByEmail(String email);

    Page<UserResponseDto> findActiveByQueryWithPagination(String input, PaginationRequestDto paginationRequestDto);

    List<UserResponseDto> findActiveByQuery(String input);

    long countByUsername(String username);

    long countByField(String field, String value);

    UserResponseDto upsert(UpsertRequestDto request);

    List<UserResponseDto> insertMany(List<UpsertRequestDto> requestList);

    String deactivate(String id);

    boolean isConnectionOK();

    List<UserShortResponseDto> findShortListByBothName(String search);


}
