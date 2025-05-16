package distributed.systems.sd_cli_java.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import distributed.systems.sd_cli_java.model.dto.DebtDTO;
import distributed.systems.sd_cli_java.model.entity.Debt;
import distributed.systems.sd_cli_java.model.entity.Expense;
import distributed.systems.sd_cli_java.model.entity.User;
import distributed.systems.sd_cli_java.repository.ExpenseRepository;
import distributed.systems.sd_cli_java.repository.UserRepository;

@Mapper(componentModel = "spring", uses = { UserRepository.class, ExpenseRepository.class })
public interface DebtMapper {

    @Mapping(target = "expenseId", source = "expense.id")
    @Mapping(target = "lenderId", source = "lender.id")
    @Mapping(target = "borrowerId", source = "borrower.id")
    DebtDTO toDto(Debt debt);

    List<DebtDTO> toDtoList(List<Debt> debts);

    @Mapping(target = "expense", source = "expenseId")
    @Mapping(target = "lender", source = "lenderId")
    @Mapping(target = "borrower", source = "borrowerId")
    Debt toEntity(DebtDTO dto);

    default Expense mapExpenseIdToExpense(Long expenseId) {
        if (expenseId == null) {
            return null;
        }
        Expense expense = new Expense();
        expense.setId(expenseId);
        return expense;
    }

    default User mapLenderIdToUser(Long lenderId) {
        if (lenderId == null) {
            return null;
        }
        User user = new User();
        user.setId(lenderId);
        return user;
    }

    default User mapBorrowerIdToUser(Long borrowerId) {
        if (borrowerId == null) {
            return null;
        }
        User user = new User();
        user.setId(borrowerId);
        return user;
    }
}
