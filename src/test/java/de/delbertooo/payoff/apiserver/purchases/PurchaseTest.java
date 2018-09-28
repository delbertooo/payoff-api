package de.delbertooo.payoff.apiserver.purchases;

import lombok.val;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class PurchaseTest {

    @Test
    public void setPurchasedAt_calledWithNull_setsItselfAndPurchasedYear() {
        val purchase = new Purchase();
        purchase.setPurchasedAt(LocalDateTime.parse("2018-03-20T12:00"));

        assertThat(purchase.getPurchasedAt(), is(notNullValue()));
        assertThat(purchase.getPurchasedYear(), is(notNullValue()));

        purchase.setPurchasedAt(null);

        assertThat(purchase.getPurchasedAt(), is(nullValue()));
        assertThat(purchase.getPurchasedYear(), is(nullValue()));
    }

    @Test
    public void setPurchasedAt_calledWithADate_setsItselfAndPurchasedYear() {
        val purchase = new Purchase();
        purchase.setPurchasedAt(LocalDateTime.parse("2018-03-20T12:00"));

        assertThat(purchase.getPurchasedAt(), is(LocalDateTime.parse("2018-03-20T12:00")));
        assertThat(purchase.getPurchasedYear(), is(2018));
        purchase.setPurchasedAt(purchase.getPurchasedAt().minusYears(1));

        assertThat(purchase.getPurchasedAt(), is(LocalDateTime.parse("2017-03-20T12:00")));
        assertThat(purchase.getPurchasedYear(), is(2017));
    }
}