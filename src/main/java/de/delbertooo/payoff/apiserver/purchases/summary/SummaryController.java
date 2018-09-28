package de.delbertooo.payoff.apiserver.purchases.summary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SummaryController {

    @Autowired
    SummaryService summaryService;

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public SummaryService.Summary summary() {
        return summaryService.load();
    }

}
