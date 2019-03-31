package de.delbertooo.payoff.apiserver.purchases.purchasingapi;

import de.delbertooo.payoff.apiserver.purchases.PurchasesCount;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchasesCountToPurchaseYearTransformer {

    public List<PurchasesService.PurchaseYear> toPurchaseYears(List<PurchasesCount> counts) {
        return counts.stream()
                .map(this::toPurchaseYear)
                .sorted(Comparator.comparing(PurchasesService.PurchaseYear::getYear))
                .collect(Collectors.toList());
    }

    public PurchasesService.PurchaseYear toPurchaseYear(PurchasesCount purchasesCount) {
        return new PurchasesService.PurchaseYear()
                .setPurchases(purchasesCount.getPurchases())
                .setYear(purchasesCount.getYear());
    }
}
