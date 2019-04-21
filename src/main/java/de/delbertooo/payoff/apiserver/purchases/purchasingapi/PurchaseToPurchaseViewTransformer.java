package de.delbertooo.payoff.apiserver.purchases.purchasingapi;

import de.delbertooo.payoff.apiserver.purchases.Purchase;
import de.delbertooo.payoff.apiserver.purchases.purchasingapi.PurchasesService.PurchaseView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class PurchaseToPurchaseViewTransformer {

    private UserToNamesTransformer userToNamesTransformer;
    private Locale locale;

    @Autowired
    public PurchaseToPurchaseViewTransformer(UserToNamesTransformer userToNamesTransformer, Locale locale) {
        this.userToNamesTransformer = userToNamesTransformer;
        this.locale = locale;
    }

    public List<PurchaseView> toPurchaseViews(Collection<Purchase> purchases) {
        return purchases.stream()
                .sorted(Comparator.comparing(Purchase::getPurchasedAt).reversed())
                .map(this::toPurchaseView)
                .collect(Collectors.toList());
    }

    public PurchaseView toPurchaseView(Purchase purchase) {
        return new PurchaseView()
                .setDate(purchase.getPurchasedAt().atZone(ZoneId.systemDefault()))
                .setItem(purchase.getName())
                .setFormattedPrice(NumberFormat.getCurrencyInstance(locale).format(purchase.getPrice()))
                .setPurchaser(userToNamesTransformer.toName(purchase.getPurchaser()))
                .setParticipants(userToNamesTransformer.toNames(purchase.getParticipants()))
                ;
    }

}
