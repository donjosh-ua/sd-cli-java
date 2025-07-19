package distributed.systems.sd_cli_java.model.entity;

import org.hibernate.annotations.Comment;

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
    @Comment("Unique identifier for the debt")
    private Long debtId;

    @OneToOne
    @JoinColumn(name = "expense_id", nullable = false)
    @Comment("The expense associated with the debt")
    private Expense expense;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("The user who is lending the money")
    private User lender;

    @ManyToOne
    @JoinColumn(name = "debtor_id", nullable = false)
    @Comment("The user who owes the money")
    private User debtor;

    @Column(nullable = false)
    @Comment("Amount of the debt")
    private Float amount;

    @Embedded
    private TimestampInfo timestampInfo;

}
