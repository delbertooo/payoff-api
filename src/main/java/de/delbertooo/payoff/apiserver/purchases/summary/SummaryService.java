package de.delbertooo.payoff.apiserver.purchases.summary;

import de.delbertooo.payoff.apiserver.purchases.PurchaseRepository;
import de.delbertooo.payoff.apiserver.purchases.PurchaserBalance;
import de.delbertooo.payoff.apiserver.users.UserRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

@Service
public class SummaryService {

    private PurchaseRepository purchaseRepository;
    private UserRepository userRepository;
    private PurchaserBalanceToSummaryBalanceTransformer purchaserBalanceToSummaryBalanceTransformer;

    @Autowired
    public SummaryService(PurchaseRepository purchaseRepository, UserRepository userRepository, PurchaserBalanceToSummaryBalanceTransformer purchaserBalanceToSummaryBalanceTransformer) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.purchaserBalanceToSummaryBalanceTransformer = purchaserBalanceToSummaryBalanceTransformer;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Summary load(String forUsername) {
        val totalPrice = purchaseRepository.calculateTotalPrice();
        List<PurchaserBalance> balances = forUsername != null
                ? userRepository.requireByName(forUsername).getBalances()
                : Collections.emptyList();
        return toSummary(totalPrice, balances);
    }

    private Summary toSummary(double totalPrice, List<PurchaserBalance> balances) {
        return new Summary()
                .setTotalPrice(new BigDecimal(totalPrice).setScale(2, RoundingMode.HALF_UP))
                .setBalances(purchaserBalanceToSummaryBalanceTransformer.toBalances(balances))
                ;
    }


    @Getter
    @Setter
    public static class Summary {
        private BigDecimal totalPrice;
        private List<Balance> balances;

        @Getter
        @Setter
        public static class Balance {
            private String user;
            private BigDecimal balance;
        }
    }
}
