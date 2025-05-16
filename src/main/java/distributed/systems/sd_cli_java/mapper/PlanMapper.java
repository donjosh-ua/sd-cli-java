package distributed.systems.sd_cli_java.mapper;

import org.mapstruct.Mapper;

import distributed.systems.sd_cli_java.model.dto.PlanDTO;
import distributed.systems.sd_cli_java.model.entity.Plan;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    PlanDTO toDto(Plan plan);

}
