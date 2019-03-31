package de.delbertooo.payoff.apiserver.purchases.purchasingapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
