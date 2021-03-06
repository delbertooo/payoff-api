package de.delbertooo.payoff.apiserver.purchases.summaryapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SummaryController {

    @Autowired
    SummaryService summaryService;

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public SummaryService.Summary summary(@RequestParam(required = false) String user) {
        return summaryService.load(user);
    }

}
