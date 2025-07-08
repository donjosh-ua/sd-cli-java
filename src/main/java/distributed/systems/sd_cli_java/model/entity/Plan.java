package distributed.systems.sd_cli_java.model.entity;

import java.time.LocalDateTime;
import java.util.List;

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
@Table(name = "plans")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "planId")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime date;

    @Embedded
    private TimestampInfo timestampInfo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "plan_users", joinColumns = @JoinColumn(name = "plan_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<Expense> expenses;

}