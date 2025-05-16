package distributed.systems.sd_cli_java.mapper;

import org.mapstruct.Mapper;

import distributed.systems.sd_cli_java.model.dto.UserDTO;
import distributed.systems.sd_cli_java.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);

}
