package de.delbertooo.payoff.apiserver.purchases.purchasingapi;

import de.delbertooo.payoff.apiserver.common.GlobalClock;
import de.delbertooo.payoff.apiserver.purchases.Purchase;
import de.delbertooo.payoff.apiserver.purchases.PurchaseRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class PurchasesService {

    private PurchaseRepository purchaseRepository;
    private PurchaseToPurchaseViewTransformer purchaseToPurchaseViewTransformer;
    private PurchasesCountToPurchaseYearTransformer purchasesCountToPurchaseYearTransformer;
    private PurchaseToPurchaseCreateTransformer purchaseToPurchaseCreateTransformer;

    @Autowired
    public PurchasesService(PurchaseRepository purchaseRepository,
                            PurchaseToPurchaseViewTransformer purchaseToPurchaseViewTransformer,
                            PurchasesCountToPurchaseYearTransformer purchasesCountToPurchaseYearTransformer,
                            PurchaseToPurchaseCreateTransformer purchaseToPurchaseCreateTransformer) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseToPurchaseViewTransformer = purchaseToPurchaseViewTransformer;
        this.purchasesCountToPurchaseYearTransformer = purchasesCountToPurchaseYearTransformer;
        this.purchaseToPurchaseCreateTransformer = purchaseToPurchaseCreateTransformer;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<PurchaseYear> findYears() {
        val yearlyCounts = purchaseRepository.findYearlyCounts();
        return purchasesCountToPurchaseYearTransformer.toPurchaseYears(yearlyCounts);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<PurchaseView> findPurchases(int year) {
        val purchases = purchaseRepository.findByYear(year);
        return purchaseToPurchaseViewTransformer.toPurchaseViews(purchases);
    }

    @Transactional
    public void createPurchase(PurchaseCreate create) {
        val purchase = new Purchase();
        purchaseToPurchaseCreateTransformer.updateWith(purchase, create);
        lockEntities(purchase);
        purchase.setPurchasedYear(LocalDate.now(GlobalClock.current()).getYear());
        purchase.setPurchasedAt(LocalDateTime.now(GlobalClock.current()));
        purchase.updatePurchaserBalance();


        purchaseRepository.save(purchase);
        purchaseRepository.flush();
    }

    private void lockEntities(Purchase purchase) {
        purchaseRepository.lock(purchase.getPurchaser(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        purchase.getParticipants().forEach(u -> purchaseRepository.lock(u, LockModeType.OPTIMISTIC_FORCE_INCREMENT));
    }

    @Getter
    @Setter
    public static class PurchaseCreate {
        @NotBlank
        private String purchase;
        @NotNull
        private BigDecimal price;
        @NotNull
        @ExistingPurchaser
        private String purchaser;
        @NotNull
        @Size(min = 1)
        private List<@NotNull @ExistingPurchaser String> participants;
    }

    @Getter
    @Setter
    public static class PurchaseYear {
        private Integer year;
        private Long purchases;
    }

    @Getter
    @Setter
    public static class PurchaseView {
        private String item;
        private Share purchaser;
        private ZonedDateTime date;
        private List<Share> participants;
        private String formattedPrice;
        private BigDecimal price;

        @Getter
        @Setter
        public static class Share {
            private UsersService.Purchaser purchaser;
            private BigDecimal shareOfPrice;
            private String formattedShareOfPrice;
        }
    }
}
