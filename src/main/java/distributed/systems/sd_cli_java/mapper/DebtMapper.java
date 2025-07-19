package distributed.systems.sd_cli_java.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants.ComponentModel;

import distributed.systems.sd_cli_java.model.dto.debt.DebtDTO;
import distributed.systems.sd_cli_java.model.entity.Debt;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DebtMapper {

    DebtDTO toDto(Debt debt);

    Debt toEntity(DebtDTO debtDTO);

    List<DebtDTO> toDtoList(List<Debt> debts);

    List<Debt> toEntityList(List<DebtDTO> debtDTOs);

}
