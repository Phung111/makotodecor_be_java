package com.makotodecor.service.impl;

import com.makotodecor.exceptions.WebBadRequestException;
import com.makotodecor.exceptions.base.ErrorMessage;
import com.makotodecor.mapper.UserMapper;
import com.makotodecor.model.UpdateUserRequest;
import com.makotodecor.model.UpdateUsersStatusRequest;
import com.makotodecor.model.UserDetailResponse;
import com.makotodecor.model.UsersPagedResponse;
import com.makotodecor.model.dto.UserPagedCriteria;
import com.makotodecor.model.entity.User;
import com.makotodecor.model.enums.UserStatusEnum;
import com.makotodecor.repository.UserRepository;
import com.makotodecor.service.UserService;
import com.makotodecor.util.PaginationUtils;
import com.makotodecor.util.QuerydslCriteriaUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  private static final Set<String> USER_SORTABLE_COLUMNS = Set.of("id", "username", "name", "email", "status", "role",
      "createdAt");

  @Override
  public UsersPagedResponse getUsersPaged(UserPagedCriteria criteria) {
    var sortCriteria = PaginationUtils.parseSortCriteria(criteria.getOrderBy());
    PaginationUtils.validateSortColumns(sortCriteria, USER_SORTABLE_COLUMNS);

    var pageable = PageRequest
        .of(criteria.getPage(), criteria.getSize())
        .withSort(sortCriteria);

    var predicate = QuerydslCriteriaUtils.buildUserSearchPredicate(criteria)
        .orElse(QuerydslCriteriaUtils.truePredicate());

    var pageResponse = userRepository.findAll(predicate, pageable);

    return UsersPagedResponse.builder()
        .pageInfo(PaginationUtils.toPageInfo(pageResponse))
        .items(pageResponse.getContent().stream()
            .map(userMapper::toUserItemResponse)
            .toList())
        .build();
  }

  @Override
  public UserDetailResponse getUser(Long userId) {
    User user = findUserById(userId);
    return userMapper.toUserDetailResponse(user);
  }

  @Override
  @Transactional
  public UserDetailResponse updateUser(Long userId, UpdateUserRequest request) {
    User user = findUserById(userId);

    // Check if username already exists for different user
    if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
      if (userRepository.existsByUsername(request.getUsername())) {
        throw new WebBadRequestException(ErrorMessage.AUTH_USERNAME_ALREADY_EXISTS);
      }
    }

    // Check if email already exists for different user
    if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
      if (userRepository.existsByEmail(request.getEmail())) {
        throw new WebBadRequestException(ErrorMessage.AUTH_EMAIL_ALREADY_EXISTS);
      }
    }

    user = userMapper.updateUserFromRequest(request, user);

    user = userRepository.save(user);

    return userMapper.toUserDetailResponse(user);
  }

  @Override
  @Transactional
  public void updateUsersStatus(UpdateUsersStatusRequest request) {
    var users = userRepository.findAllById(request.getUserIds());

    if (users.size() != request.getUserIds().size()) {
      throw new WebBadRequestException(ErrorMessage.USER_NOT_FOUND);
    }

    var newStatus = UserStatusEnum.valueOf(request.getStatus().getValue());
    users.forEach(user -> {
      user.setStatus(newStatus);
    });

    userRepository.saveAll(users);
  }

  @Override
  @Transactional
  public void deleteUser(Long userId) {
    User user = findUserById(userId);

    // Check if user has orders
    if (user.getOrders() != null && !user.getOrders().isEmpty()) {
      throw new WebBadRequestException(ErrorMessage.USER_HAS_ORDERS);
    }

    userRepository.delete(user);
  }

  private User findUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new WebBadRequestException(ErrorMessage.USER_NOT_FOUND));
  }
}
