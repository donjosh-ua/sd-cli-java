package distributed.systems.sd_cli_java.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import distributed.systems.sd_cli_java.model.dto.PlanDTO;
import distributed.systems.sd_cli_java.model.entity.Plan;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    @Mapping(target = "userIds", source = "users", qualifiedByName = "usersToIds")
    @Mapping(target = "expenseIds", source = "expenses", qualifiedByName = "expensesToIds")
    PlanDTO toDto(Plan plan);

    List<PlanDTO> toDtoList(List<Plan> plans);

    @Named("usersToIds")
    default List<Long> usersToIds(List<?> users) {
        return users != null ? users.stream()
                .map(user -> ((distributed.systems.sd_cli_java.model.entity.User) user).getId())
                .collect(Collectors.toList()) : null;
    }

    @Named("expensesToIds")
    default List<Long> expensesToIds(List<?> expenses) {
        return expenses != null ? expenses.stream()
                .map(expense -> ((distributed.systems.sd_cli_java.model.entity.Expense) expense).getId())
                .collect(Collectors.toList()) : null;
    }
}
