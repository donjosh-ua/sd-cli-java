package distributed.systems.sd_cli_java.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

import distributed.systems.sd_cli_java.model.dto.expense.ExpenseDTO;
import distributed.systems.sd_cli_java.model.entity.Expense;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExpenseMapper {

    ExpenseDTO toDto(Expense expense);

    Expense toEntity(ExpenseDTO expenseDTO);

    List<ExpenseDTO> toDtoList(List<Expense> expenses);

    List<Expense> toEntityList(List<ExpenseDTO> expenseDTOs);

}