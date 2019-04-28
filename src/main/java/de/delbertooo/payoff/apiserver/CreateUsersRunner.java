package de.delbertooo.payoff.apiserver;

import de.delbertooo.payoff.apiserver.users.User;
import de.delbertooo.payoff.apiserver.users.UserRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;
import java.util.stream.Stream;

@Profile("dev")
@Component
public class CreateUsersRunner implements ApplicationRunner {
    private UserRepository userRepository;

    @Autowired
    public CreateUsersRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        if (!args.containsOption("create-users")) {
            return;
        }
        val delbertooo = new User().setName("delbertooo").setEmail("del@payoff");
        val foo = new User().setName("foo").setEmail("foo@payoff");
        val masuuk = new User().setName("masuuk");

        Stream.of(delbertooo, masuuk, foo)
                .peek(u -> u.setPassword(UUID.randomUUID().toString()))
                .forEach(userRepository::save);
    }
}
