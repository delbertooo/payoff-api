package de.delbertooo.payoff.apiserver.purchases.summaryapi;

import de.delbertooo.payoff.apiserver.purchases.PurchaserBalance;
import de.delbertooo.payoff.apiserver.purchases.purchasingapi.UserToNamesTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class PurchaserBalanceToSummaryBalanceTransformer {

    private UserToNamesTransformer userToNamesTransformer;
    private Locale locale;

    @Autowired
    public PurchaserBalanceToSummaryBalanceTransformer(UserToNamesTransformer userToNamesTransformer, Locale locale) {
        this.userToNamesTransformer = userToNamesTransformer;
        this.locale = locale;
    }


    public List<SummaryService.Summary.Balance> toBalances(List<PurchaserBalance> balances) {
        return balances.stream()
                .sorted(Comparator.comparing(PurchaserBalance::getBalance))
                .map(this::toBalance)
                .collect(Collectors.toList());
    }

    private SummaryService.Summary.Balance toBalance(PurchaserBalance purchaserBalance) {
        return new SummaryService.Summary.Balance()
                .setUser(userToNamesTransformer.toName(purchaserBalance.getParticipant()))
                .setFormattedBalance(NumberFormat.getCurrencyInstance(locale).format(purchaserBalance.getBalance()))
                ;
    }
}
