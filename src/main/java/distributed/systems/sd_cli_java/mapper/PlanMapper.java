package distributed.systems.sd_cli_java.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import distributed.systems.sd_cli_java.model.dto.PlanDTO;
import distributed.systems.sd_cli_java.model.entity.Plan;

@Mapper(componentModel = ComponentModel.SPRING)
public interface PlanMapper {

    PlanDTO toDto(Plan plan);

    Plan toEntity(PlanDTO planDTO);

    List<PlanDTO> toDtoList(List<Plan> plans);

    List<Plan> toEntityList(List<PlanDTO> planDTOs);

}