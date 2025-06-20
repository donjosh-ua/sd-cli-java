package distributed.systems.sd_cli_java.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

import distributed.systems.sd_cli_java.model.dto.UserDTO;
import distributed.systems.sd_cli_java.model.entity.User;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);

    List<UserDTO> toDtoList(List<User> users);

    List<User> toEntityList(List<UserDTO> userDTOs);

}