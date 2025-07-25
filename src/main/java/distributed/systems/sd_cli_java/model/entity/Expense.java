package distributed.systems.sd_cli_java.model.entity;

import java.time.LocalDateTime;
import java.util.List;

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
@Table(name = "expenses")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "expenseId")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id", nullable = false)
    @Comment("Unique identifier for the expense")
    private Long expenseId;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    @Comment("The plan associated with the expense")
    private Plan plan;

    @ManyToMany
    @JoinTable(name = "expense_user", joinColumns = @JoinColumn(name = "expense_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Comment("List of users participating in the expense")
    private List<User> participants;

    @Column(nullable = false, length = 30)
    @Comment("Name of the expense")
    private String name;

    @Column(nullable = false)
    @Comment("Amount of the expense")
    private Float amount;

    @Column(nullable = false)
    @Comment("Date of the expense")
    private LocalDateTime date;

    @Column(nullable = false, length = 20)
    @Comment("Type of the expense")
    private String type;

    @Embedded
    private TimestampInfo timestampInfo;

}