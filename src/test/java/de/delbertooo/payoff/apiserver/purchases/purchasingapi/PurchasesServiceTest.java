package de.delbertooo.payoff.apiserver.purchases.purchasingapi;

import de.delbertooo.payoff.apiserver.common.GlobalClock;
import de.delbertooo.payoff.apiserver.purchases.Purchase;
import de.delbertooo.payoff.apiserver.purchases.PurchaseRepository;
import lombok.val;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class PurchasesServiceTest {

    private PurchasesService purchasesService;
    private PurchaseRepository purchaseRepository;
    private PurchaseToPurchaseCreateTransformer purchaseToPurchaseCreateTransformer;

    @Before
    public void setUp() {
        purchaseRepository = mock(PurchaseRepository.class);
        purchaseToPurchaseCreateTransformer = mock(PurchaseToPurchaseCreateTransformer.class);
        purchasesService = new PurchasesService(
                purchaseRepository,
                mock(PurchaseToPurchaseViewTransformer.class),
                mock(PurchasesCountToPurchaseYearTransformer.class),
                purchaseToPurchaseCreateTransformer
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
        purchaseToPurchaseCreateTransformer.updateWith(Mockito.any(), Mockito.any());
        doAnswer(i -> {
            ((Purchase) i.getArgument(0)).setPrice(new BigDecimal("13.37"));
            return null;
        }).when(purchaseToPurchaseCreateTransformer).updateWith(any(), any());

        purchasesService.createPurchase(create);

        verify(purchaseRepository).save(argument.capture());
        assertThat(argument.getValue().getPurchasedAt(), is(LocalDateTime.parse("2018-03-13T12:00")));
        assertThat(argument.getValue().getPurchasedYear(), is(2018));
    }
}