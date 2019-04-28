package de.delbertooo.payoff.apiserver.purchases;

import de.delbertooo.payoff.apiserver.common.jpa.FlushRepository;
import de.delbertooo.payoff.apiserver.common.jpa.LockRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends CrudRepository<Purchase, Long>, FlushRepository, LockRepository {

    @Transactional(Transactional.TxType.SUPPORTS)
    @Query("SELECT NEW de.delbertooo.payoff.apiserver.purchases.PurchasesCount(p.purchasedYear, count(p.purchasedYear)) " +
            "FROM Purchase p GROUP BY p.purchasedYear")
    List<PurchasesCount> findYearlyCounts();

    @Transactional(Transactional.TxType.SUPPORTS)
    @Query("SELECT DISTINCT p FROM Purchase p JOIN FETCH p.participants JOIN FETCH p.purchaser WHERE p.purchasedYear = :year")
    List<Purchase> findByYear(@Param("year") int year);

    @Deprecated
    @Transactional(Transactional.TxType.SUPPORTS)
    @Query("SELECT SUM(p.price) FROM Purchase p")
    Number sumTotalPrice();

    @Transactional(Transactional.TxType.SUPPORTS)
    default double calculateTotalPrice() {
        return Optional.ofNullable(sumTotalPrice()).orElse(0).doubleValue();
    }

}
