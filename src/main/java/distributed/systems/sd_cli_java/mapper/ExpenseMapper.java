package distributed.systems.sd_cli_java.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import distributed.systems.sd_cli_java.model.dto.ExpenseDTO;
import distributed.systems.sd_cli_java.model.entity.Expense;
import distributed.systems.sd_cli_java.model.entity.Plan;
import distributed.systems.sd_cli_java.model.entity.User;
import distributed.systems.sd_cli_java.repository.PlanRepository;
import distributed.systems.sd_cli_java.repository.UserRepository;

@Mapper(componentModel = "spring", uses = { UserRepository.class, PlanRepository.class })
public interface ExpenseMapper {

    @Mapping(target = "planId", source = "plan.id")
    @Mapping(target = "userId", source = "user.id")
    ExpenseDTO toDto(Expense expense);

    List<ExpenseDTO> toDtoList(List<Expense> expenses);

    @Mapping(target = "plan", source = "planId")
    @Mapping(target = "user", source = "userId")
    Expense toEntity(ExpenseDTO dto);

    default Plan mapPlanIdToPlan(Long planId) {
        if (planId == null) {
            return null;
        }
        Plan plan = new Plan();
        plan.setId(planId);
        return plan;
    }

    default User mapUserIdToUser(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
}
