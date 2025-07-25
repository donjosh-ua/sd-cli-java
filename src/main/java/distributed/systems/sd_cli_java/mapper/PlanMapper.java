package distributed.systems.sd_cli_java.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

import distributed.systems.sd_cli_java.model.dto.plan.PlanDTO;
import distributed.systems.sd_cli_java.model.entity.Plan;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlanMapper {

    @Mapping(target = "owner", source = "owner.email")
    PlanDTO toDto(Plan plan);

    @Mapping(target = "owner.email", source = "owner")
    Plan toEntity(PlanDTO planDTO);

    List<PlanDTO> toDtoList(List<Plan> plans);

    List<Plan> toEntityList(List<PlanDTO> planDTOs);

}