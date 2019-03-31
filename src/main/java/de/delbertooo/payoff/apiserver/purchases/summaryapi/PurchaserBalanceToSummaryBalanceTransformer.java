package de.delbertooo.payoff.apiserver.purchases.summaryapi;

import de.delbertooo.payoff.apiserver.purchases.PurchaserBalance;
import de.delbertooo.payoff.apiserver.purchases.api.UserToNamesTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaserBalanceToSummaryBalanceTransformer {

    private UserToNamesTransformer userToNamesTransformer;

    @Autowired
    public PurchaserBalanceToSummaryBalanceTransformer(UserToNamesTransformer userToNamesTransformer) {
        this.userToNamesTransformer = userToNamesTransformer;
    }


    public List<SummaryService.Summary.Balance> toBalances(List<PurchaserBalance> balances) {
        return balances.stream()
                .map(this::toBalance)
                .sorted(Comparator.comparing(SummaryService.Summary.Balance::getUser))
                .collect(Collectors.toList());
    }

    private SummaryService.Summary.Balance toBalance(PurchaserBalance purchaserBalance) {
        return new SummaryService.Summary.Balance()
                .setUser(userToNamesTransformer.toName(purchaserBalance.getParticipant()))
                .setBalance(new BigDecimal(purchaserBalance.getBalance()).setScale(2, RoundingMode.HALF_UP))
                ;
    }
}
