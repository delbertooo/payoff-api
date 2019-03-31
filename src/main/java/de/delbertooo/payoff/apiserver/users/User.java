package de.delbertooo.payoff.apiserver.users;

import de.delbertooo.payoff.apiserver.purchases.PurchaserBalance;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
@ToString(of = {"id"})
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    private String email;

    @OneToMany(mappedBy = "purchaser", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.PRIVATE)
    private List<PurchaserBalance> balances = new ArrayList<>();

    public void addToBalance(User participant, double valueToAdd) {
        val balance = getBalances().stream()
                .filter(b -> participant.equals(b.getParticipant()))
                .findFirst()
                .orElseGet(() -> newBalance(participant));
        balance.setBalance(balance.getBalance() + valueToAdd);
    }

    private PurchaserBalance newBalance(User participant) {
        val purchaserBalance = new PurchaserBalance(this, participant);
        getBalances().add(purchaserBalance);
        return purchaserBalance;
    }

    public Stream<PurchaserBalance> streamNonZeroBalances() {
        return getBalances().stream()
                .filter(x -> Math.abs(x.getBalance()) >= 0.005)
                ;
    }

    public Stream<PurchaserBalance> streamPositiveBalances() {
        return streamNonZeroBalances().filter(x -> x.getBalance() > 0);
    }

    public Stream<PurchaserBalance> streamNegativeBalances() {
        return streamNonZeroBalances().filter(x -> x.getBalance() < 0);
    }
}
