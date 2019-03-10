package de.delbertooo.payoff.apiserver.purchases.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PurchasersController {

    @Autowired
    UsersService usersService;

    @RequestMapping(value = "/available-purchasers", method = RequestMethod.GET)
    public List<String> availablePurchasers() {
        return usersService.findPurchaserNames();
    }

}
