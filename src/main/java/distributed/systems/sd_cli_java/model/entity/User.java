package distributed.systems.sd_cli_java.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;

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
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId", resolver = SimpleObjectIdResolver.class, scope = User.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 15)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Expense> expenses = new ArrayList<>();

    @OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Debt> debts = new ArrayList<>();

    @OneToMany(mappedBy = "lender", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Debt> loans = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    @Builder.Default
    private List<Plan> plans = new ArrayList<>();

}