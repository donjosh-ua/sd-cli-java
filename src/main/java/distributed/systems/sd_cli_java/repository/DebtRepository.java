package distributed.systems.sd_cli_java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import distributed.systems.sd_cli_java.model.entity.Debt;
import distributed.systems.sd_cli_java.model.entity.User;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {

    List<Debt> findByBorrower(User borrower);

    List<Debt> findByLender(User lender);

    List<Debt> findByExpenseExpenseId(Long expenseId);

    List<Debt> findByLenderAndBorrower(User lender, User borrower);

}
