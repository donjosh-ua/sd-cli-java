package distributed.systems.sd_cli_java.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import distributed.systems.sd_cli_java.model.dto.UserDTO;
import distributed.systems.sd_cli_java.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "expenseIds", source = "expenses", qualifiedByName = "expensesToIds")
    @Mapping(target = "debtIds", source = "debts", qualifiedByName = "debtsToIds")
    @Mapping(target = "loanIds", source = "loans", qualifiedByName = "debtsToIds")
    @Mapping(target = "planIds", source = "plans", qualifiedByName = "plansToIds")
    UserDTO toDto(User user);

    List<UserDTO> toDtoList(List<User> users);

    @Named("expensesToIds")
    default List<Long> expensesToIds(List<?> expenses) {
        return expenses != null ? expenses.stream()
                .map(expense -> ((distributed.systems.sd_cli_java.model.entity.Expense) expense).getId())
                .collect(Collectors.toList()) : null;
    }

    @Named("debtsToIds")
    default List<Long> debtsToIds(List<?> debts) {
        return debts != null ? debts.stream()
                .map(debt -> ((distributed.systems.sd_cli_java.model.entity.Debt) debt).getId())
                .collect(Collectors.toList()) : null;
    }

    @Named("plansToIds")
    default List<Long> plansToIds(List<?> plans) {
        return plans != null ? plans.stream()
                .map(plan -> ((distributed.systems.sd_cli_java.model.entity.Plan) plan).getId())
                .collect(Collectors.toList()) : null;
    }
}
