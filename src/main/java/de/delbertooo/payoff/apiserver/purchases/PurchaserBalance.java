package de.delbertooo.payoff.apiserver.purchases;

import de.delbertooo.payoff.apiserver.users.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
public class PurchaserBalance {

    @EmbeddedId
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private PrimaryKey primaryKey = new PrimaryKey();

    @ManyToOne
    @MapsId("purchaserId")
    private User purchaser;

    @ManyToOne
    @MapsId("participantId")
    private User participant;

    @Version
    @Column(nullable = false)
    @Setter(AccessLevel.PRIVATE)
    private long version;

    @Column(nullable = false)
    private double balance = 0d;

    public PurchaserBalance() {
    }

    public PurchaserBalance(@NonNull User purchaser, @NonNull User participant) {
        this.purchaser = purchaser;
        this.participant = participant;
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    @Embeddable
    public static class PrimaryKey implements Serializable {
        private Long purchaserId;
        private Long participantId;
    }


}
