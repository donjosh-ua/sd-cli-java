package distributed.systems.sd_cli_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import distributed.systems.sd_cli_java.model.entity.Debt;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {

}
