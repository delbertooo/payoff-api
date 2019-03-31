package de.delbertooo.payoff.apiserver;

import de.delbertooo.payoff.apiserver.users.User;
import de.delbertooo.payoff.apiserver.users.UserRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class UsersRunner implements ApplicationRunner {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        val delbertooo = new User().setName("delbertooo").setEmail("del@ber.de");
        val foo = new User().setName("foo").setEmail("del@asdf.de");
        val masuuk = new User().setName("masuuk");

        Stream.of(delbertooo, masuuk, foo)
                .peek(u -> u.setPassword(UUID.randomUUID().toString()))
                .forEach(userRepository::save);
    }
}
