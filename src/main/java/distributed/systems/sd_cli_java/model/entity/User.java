package distributed.systems.sd_cli_java.model.entity;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "email", resolver = SimpleObjectIdResolver.class, scope = User.class)
public class User {

    @Id
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "nickname", nullable = false, length = 100)
    private String nickname;

    @Column(name = "photo_url", length = 2048)
    private String photoUrl;

    @Embedded
    private TimestampInfo timestampInfo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Expense> expenses;

    @OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL)
    private List<Debt> debts;

    @OneToMany(mappedBy = "lender", cascade = CascadeType.ALL)
    private List<Debt> loans;

    @ManyToMany(mappedBy = "users")
    private List<Plan> plans;

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