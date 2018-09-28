package de.delbertooo.payoff.apiserver.purchases.summary;

import de.delbertooo.payoff.apiserver.purchases.PurchaseRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class SummaryService {

    private PurchaseRepository purchaseRepository;

    @Autowired
    public SummaryService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Summary load() {
        val totalPrice = purchaseRepository.calculateTotalPrice();
        return toSummary(totalPrice);
    }

    private Summary toSummary(double totalPrice) {
        return new Summary()
                .setTotalPrice(new BigDecimal(totalPrice).setScale(2, RoundingMode.HALF_UP));
    }

    @Getter
    @Setter
    public static class Summary {
        private BigDecimal totalPrice;
    }
}
