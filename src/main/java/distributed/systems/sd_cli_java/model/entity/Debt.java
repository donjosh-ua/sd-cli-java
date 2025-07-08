package distributed.systems.sd_cli_java.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "debts")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "debtId")
public class Debt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "debt_id", nullable = false)
    private Long debtId;

    @Column(nullable = false)
    private Float amount;

    @Embedded
    private TimestampInfo timestampInfo;

    @ManyToOne
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @ManyToOne
    @JoinColumn(name = "lender_id", nullable = false)
    private User lender;

    @ManyToOne
    @JoinColumn(name = "borrower_id", nullable = false)
    private User borrower;

}
