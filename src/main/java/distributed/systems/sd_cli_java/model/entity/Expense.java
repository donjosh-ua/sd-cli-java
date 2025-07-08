package distributed.systems.sd_cli_java.model.entity;

import java.time.LocalDateTime;

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
@Table(name = "expenses")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "expenseId")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id", nullable = false)
    private Long expenseId;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private Float amount;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false, length = 20)
    private String type;

    @Embedded
    private TimestampInfo timestampInfo;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}