package de.delbertooo.payoff.apiserver.purchases;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PurchasesCount {
    private int year;
    private long purchases;
}
