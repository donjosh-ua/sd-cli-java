package distributed.systems.sd_cli_java.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import distributed.systems.sd_cli_java.model.dto.DebtDTO;
import distributed.systems.sd_cli_java.model.entity.Debt;

@Mapper(componentModel = ComponentModel.SPRING)
public interface DebtMapper {

    DebtDTO toDto(Debt debt);

    Debt toEntity(DebtDTO debtDTO);

    List<DebtDTO> toDtoList(List<Debt> debts);

    List<Debt> toEntityList(List<DebtDTO> debtDTOs);

}
