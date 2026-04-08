package com.sprint.mission.discodeit.user.mapper;

import com.sprint.mission.discodeit.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.user.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = BinaryContentMapper.class)
public interface UserMapper {

  @Mapping(target = "online", expression = "java(user.getUserStatus() != null && user.getUserStatus().isOnline())")
  UserDto toDto(User user);

  @Mapping(target = "userStatus", ignore = true)
  @Mapping(target = "profile", ignore = true)
  User toEntity(UserCreateRequest request);

  @Mapping(target = "username", source = "request.newUsername")
  @Mapping(target = "password", source = "request.newPassword")
  @Mapping(target = "email", source = "request.newEmail")
  @Mapping(target = "userStatus", ignore = true)
  @Mapping(target = "profile", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void update(UserUpdateRequest request, @MappingTarget User user);
}
