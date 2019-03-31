package de.delbertooo.payoff.apiserver.purchases.purchasingapi;

import de.delbertooo.payoff.apiserver.users.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserToNamesTransformer {

    public List<String> toNames(Collection<User> users) {
        return users.stream()
                .sorted(Comparator.comparing(User::getName))
                .map(this::toName)
                .collect(Collectors.toList());
    }

    public String toName(User user) {
        return user.getName();
    }

}
