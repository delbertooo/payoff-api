package de.delbertooo.payoff.apiserver.purchases;

import de.delbertooo.payoff.apiserver.users.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(of = {"id"})
@Table(indexes = {
        @Index(columnList = "purchasedYear"),
        @Index(columnList = "purchasedAt"),
})
@Entity
public class Purchase {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, updatable = false)
    private Integer purchasedYear;

    @Column(nullable = false)
    private LocalDateTime purchasedAt;

    @Column(nullable = false, precision = 0, scale = 6)
    private BigDecimal price;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    private User purchaser;

    @ManyToMany
    @Setter(AccessLevel.PRIVATE)
    private List<User> participants = new ArrayList<>();

    public Purchase setPurchasedAt(LocalDateTime purchasedAt) {
        purchasedYear = purchasedAt != null ? purchasedAt.getYear() : null;
        this.purchasedAt = purchasedAt;
        return this;
    }


    public double getPricePerParticipant() {
        return getPrice().doubleValue() / getParticipants().size();
    }
}
