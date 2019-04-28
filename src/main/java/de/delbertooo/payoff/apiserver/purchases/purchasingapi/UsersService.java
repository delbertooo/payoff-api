package de.delbertooo.payoff.apiserver.purchases.purchasingapi;

import de.delbertooo.payoff.apiserver.users.UserRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UsersService {

    private UserRepository userRepository;
    private UserToNamesTransformer userToNamesTransformer;

    public UsersService(UserRepository userRepository, UserToNamesTransformer userToNamesTransformer) {
        this.userRepository = userRepository;
        this.userToNamesTransformer = userToNamesTransformer;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<String> findPurchaserNames() {
        val users = userRepository.findAll();
        return userToNamesTransformer.toNames(users);
    }


    @Getter
    @Setter
    public static class Purchaser {
        private String shortName;
        private String name;
    }

}
