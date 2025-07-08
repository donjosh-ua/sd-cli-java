package distributed.systems.sd_cli_java.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

import distributed.systems.sd_cli_java.model.dto.UserDTO;
import distributed.systems.sd_cli_java.model.dto.UserRequestDTO;
import distributed.systems.sd_cli_java.model.dto.UserResponseDTO;
import distributed.systems.sd_cli_java.model.dto.UserSearchItemDTO;
import distributed.systems.sd_cli_java.model.entity.User;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDTO toDto(User user);

    UserResponseDTO toResponseDTO(User user);
    
    UserSearchItemDTO toSearchItemDTO(User user);
    
    List<UserResponseDTO> toResponseDTOList(List<User> users);
    
    List<UserSearchItemDTO> toSearchItemDTOList(List<User> users);
    
    @Mapping(target = "email", expression = "java(request.getEmail().toLowerCase().trim())")
    @Mapping(target = "nickname", expression = "java(request.getNickname().trim())")
    User toEntity(UserRequestDTO request);
}
