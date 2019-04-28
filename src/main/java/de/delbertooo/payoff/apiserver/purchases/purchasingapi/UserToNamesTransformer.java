package de.delbertooo.payoff.apiserver.purchases.purchasingapi;

import de.delbertooo.payoff.apiserver.users.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserToNamesTransformer {

    public List<String> toNames(Collection<User> users) {
        return users.stream()
                .sorted(User.BY_NAME_COMPARATOR)
                .map(this::toName)
                .collect(Collectors.toList());
    }

    public String toName(User user) {
        return user.getName();
    }

    public UsersService.Purchaser toPurchaser(User user) {
        return new UsersService.Purchaser().setName(user.getName()).setShortName(user.getShortName());
    }

    public List<UsersService.Purchaser> toPurchasers(Collection<User> users) {
        return users.stream()
                .sorted(User.BY_NAME_COMPARATOR)
                .map(this::toPurchaser)
                .collect(Collectors.toList());
    }

}
