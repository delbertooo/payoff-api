package de.delbertooo.payoff.apiserver.purchases.purchasingapi;

import de.delbertooo.payoff.apiserver.purchases.Purchase;
import de.delbertooo.payoff.apiserver.purchases.purchasingapi.PurchasesService.PurchaseView;
import de.delbertooo.payoff.apiserver.users.User;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                .setPrice(purchase.getPrice())
                .setPurchaser(toShare(purchase.getPurchaser(), purchase.getPriceForPurchaser()))
                .setParticipants(purchase.getParticipants().stream()
                        .sorted(User.BY_NAME_COMPARATOR)
                        .map(purchaser -> toShare(purchaser, purchase.getPricePerParticipant()))
                        .collect(Collectors.toList())
                )
                ;
    }

    private PurchasesService.PurchaseView.Share toShare(User purchaser, double shareOfPrice) {
        val roundedShareOfPrice = BigDecimal.valueOf(shareOfPrice).setScale(2, RoundingMode.HALF_UP);
        return new PurchaseView.Share()
                .setPurchaser(userToNamesTransformer.toPurchaser(purchaser))
                .setShareOfPrice(roundedShareOfPrice)
                .setFormattedShareOfPrice(NumberFormat.getCurrencyInstance(locale).format(roundedShareOfPrice))
                ;
    }

}
