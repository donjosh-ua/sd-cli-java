package distributed.systems.sd_cli_java.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", indexes = {
    @Index(name = "idx_nickname", columnList = "nickname"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "email", resolver = SimpleObjectIdResolver.class, scope = User.class)
public class User {

    @Id
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "nickname", nullable = false, length = 100)
    private String nickname;

    @Column(name = "photo_url", length = 2048)
    private String photoUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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