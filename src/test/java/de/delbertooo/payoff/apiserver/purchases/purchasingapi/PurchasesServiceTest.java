package de.delbertooo.payoff.apiserver.purchases.purchasingapi;

import de.delbertooo.payoff.apiserver.common.GlobalClock;
import de.delbertooo.payoff.apiserver.purchases.Purchase;
import de.delbertooo.payoff.apiserver.purchases.PurchaseRepository;
import lombok.val;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PurchasesServiceTest {

    private PurchasesService purchasesService;
    private PurchaseRepository purchaseRepository;

    @Before
    public void setUp() {
        purchaseRepository = mock(PurchaseRepository.class);
        purchasesService = new PurchasesService(
                purchaseRepository,
                mock(PurchaseToPurchaseViewTransformer.class),
                mock(PurchasesCountToPurchaseYearTransformer.class),
                mock(PurchaseToPurchaseCreateTransformer.class)
        );
    }

    @After
    public void tearDown() {
        GlobalClock.unfreeze();
    }

    @Test
    public void createPurchase_setsPurchasedAtAndPurchasedYear() {
        val argument = ArgumentCaptor.forClass(Purchase.class);
        val create = new PurchasesService.PurchaseCreate();
        GlobalClock.freeze(LocalDateTime.parse("2018-03-13T12:00"));

        purchasesService.createPurchase(create);

        verify(purchaseRepository).save(argument.capture());
        assertThat(argument.getValue().getPurchasedAt(), is(LocalDateTime.parse("2018-03-13T12:00")));
        assertThat(argument.getValue().getPurchasedYear(), is(2018));
    }
}