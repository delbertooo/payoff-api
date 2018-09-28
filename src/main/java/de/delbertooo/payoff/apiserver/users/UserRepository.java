package de.delbertooo.payoff.apiserver.users;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Transactional(Transactional.TxType.SUPPORTS)
    Collection<User> findAll();

    @Deprecated
    @Transactional(Transactional.TxType.SUPPORTS)
    Optional<User> findByNameIgnoreCase(String name);

    @Transactional(Transactional.TxType.SUPPORTS)
    default Optional<User> findByName(String name) {
        return findByNameIgnoreCase(name);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    default User requireByName(String name) {
        return findByName(name).orElseThrow(() -> new RuntimeException("User not found."));
    }

}
