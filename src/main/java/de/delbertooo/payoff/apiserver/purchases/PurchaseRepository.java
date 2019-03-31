package de.delbertooo.payoff.apiserver.purchases;

import de.delbertooo.payoff.apiserver.common.jpa.FlushRepository;
import de.delbertooo.payoff.apiserver.common.jpa.LockRepository;
import lombok.val;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends CrudRepository<Purchase, Long>, FlushRepository, LockRepository {

    @Deprecated
    @Transactional(Transactional.TxType.SUPPORTS)
    List<Purchase> findByPurchasedAtBetween(LocalDateTime start, LocalDateTime end);

    @Transactional(Transactional.TxType.SUPPORTS)
    @Query("SELECT NEW de.delbertooo.payoff.apiserver.purchases.PurchasesCount(p.purchasedYear, count(p.purchasedYear)) " +
            "FROM Purchase p GROUP BY p.purchasedYear")
    List<PurchasesCount> findYearlyCounts();

    @Transactional(Transactional.TxType.SUPPORTS)
    default List<Purchase> findByYear(int year) {
        val start = LocalDate.ofYearDay(year, 1).atStartOfDay();
        val end = LocalDate.of(year, Month.DECEMBER, 31).atTime(LocalTime.MAX);
        return findByPurchasedAtBetween(start, end);
    }


    @Deprecated
    @Transactional(Transactional.TxType.SUPPORTS)
    @Query("SELECT SUM(p.price) FROM Purchase p")
    Number sumTotalPrice();

    @Transactional(Transactional.TxType.SUPPORTS)
    default double calculateTotalPrice() {
        return Optional.ofNullable(sumTotalPrice()).orElse(0).doubleValue();
    }

}
