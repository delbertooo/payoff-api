package de.delbertooo.payoff.apiserver.purchases.purchasingapi;

import de.delbertooo.payoff.apiserver.purchases.Purchase;
import de.delbertooo.payoff.apiserver.users.User;
import de.delbertooo.payoff.apiserver.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseToPurchaseCreateTransformer {

    private UserRepository userRepository;

    @Autowired
    public PurchaseToPurchaseCreateTransformer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updateWith(Purchase purchase, PurchasesService.PurchaseCreate create) {
        purchase.setName(create.getPurchase());
        purchase.setPrice(create.getPrice());
        purchase.setPurchaser(userRepository.requireByName(create.getPurchaser()));
        convertParticipants(create).forEach(p -> purchase.getParticipants().add(p));
    }

    private List<User> convertParticipants(PurchasesService.PurchaseCreate create) {
        return create.getParticipants()
                .stream()
                .map(userRepository::requireByName)
                .collect(Collectors.toList());
    }
}
