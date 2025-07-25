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
@Table(name = "plans")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "planId")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id", nullable = false)
    @Comment("Unique identifier for the plan")
    private Long planId;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @Comment("The user who created the plan")
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "plan_user", joinColumns = @JoinColumn(name = "plan_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Comment("List of users associated with the plan")
    private List<User> participants;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    @Comment("List of expenses associated with the plan")
    private List<Expense> expenses;

    @Column(nullable = false)
    @Comment("Name of the plan")
    private String name;

    @Column(nullable = true)
    @Comment("Description of the plan")
    private String description;

    @Column(nullable = false)
    @Comment("Category of the plan")
    private String category;

    @Column(nullable = false)
    @Comment("Date of the plan")
    private LocalDateTime date;

    @Column(nullable = false)
    @Comment("Status of the plan")
    private Boolean status;

    @Embedded
    private TimestampInfo timestampInfo;

}