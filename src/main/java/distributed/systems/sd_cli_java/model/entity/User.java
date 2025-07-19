package distributed.systems.sd_cli_java.model.entity;

import java.util.List;

import org.hibernate.annotations.Comment;

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
@Table(name = "users", indexes = { @Index(name = "idx_nickname", columnList = "nickname") })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "email", resolver = SimpleObjectIdResolver.class, scope = User.class)
public class User {

    @Id
    @Column(name = "email", nullable = false, unique = true, length = 255)
    @Comment("The email of the user, which serves as the unique identifier")
    private String email;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @Comment("List of plans owned by the user")
    private List<Plan> ownedPlans;

    @ManyToMany(mappedBy = "participants", cascade = CascadeType.ALL)
    @Comment("List of expenses where the user is a participant")
    private List<Expense> expenses;

    @ManyToMany(mappedBy = "users")
    @Comment("List of plans associated with the user")
    private List<Plan> plans;

    @OneToMany(mappedBy = "lender", cascade = CascadeType.ALL)
    @Comment("List of debts where the user is the lender")
    private List<Debt> lentDebts;

    @OneToMany(mappedBy = "debtor", cascade = CascadeType.ALL)
    @Comment("List of debts where the user is the debtor")
    private List<Debt> borrowedDebts;

    @Column(name = "nickname", nullable = false, length = 50)
    @Comment("The nickname of the user, used for display purposes")
    private String nickname;

    @Column(name = "photo_url", length = 2048)
    @Comment("URL of the users profile photo")
    private String photoUrl;

    @Embedded
    private TimestampInfo timestampInfo;

    @PrePersist
    public void prePersist() {
        if (email != null) {
            email = email.toLowerCase().trim();
        }
        if (nickname != null) {
            nickname = nickname.trim();
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (nickname != null) {
            nickname = nickname.trim();
        }
    }
}