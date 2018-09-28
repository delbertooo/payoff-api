package de.delbertooo.payoff.apiserver.purchases.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchases")
public class PurchasesController {

    @Autowired
    PurchasesService purchasesService;

    @RequestMapping(value = "/by-year", method = RequestMethod.GET)
    public List<PurchasesService.PurchaseYear> findYears() {
        return purchasesService.findYears();
    }

    @RequestMapping(value = "/by-year/{year}", method = RequestMethod.GET)
    public List<PurchasesService.PurchaseView> findPurchases(@PathVariable int year) {
        return purchasesService.findPurchases(year);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createPurchase(@RequestBody @Valid PurchasesService.PurchaseCreate create) {
        purchasesService.createPurchase(create);
        return ResponseEntity.noContent().build();
    }

}
