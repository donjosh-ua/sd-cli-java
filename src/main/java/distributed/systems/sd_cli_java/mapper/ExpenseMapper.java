package distributed.systems.sd_cli_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import distributed.systems.sd_cli_java.model.dto.ExpenseDTO;
import distributed.systems.sd_cli_java.model.entity.Expense;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExpenseMapper {

    @Mapping(target = "id", source = "expenseId")
    @Mapping(target = "planId", source = "plan.planId")
    ExpenseDTO toDto(Expense expense);

}
