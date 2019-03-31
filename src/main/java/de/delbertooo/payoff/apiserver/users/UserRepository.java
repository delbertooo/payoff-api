package de.delbertooo.payoff.apiserver.users;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
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
    @Query("SELECT DISTINCT u.id FROM User u JOIN u.balances b WHERE u.email IS NOT NULL AND ABS(b.balance) >= 0.005")
    List<Long> findUserIdsForReport();

    @Transactional(Transactional.TxType.SUPPORTS)
    default User requireByName(String name) {
        return findByName(name).orElseThrow(() -> new RuntimeException("User not found."));
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    default User requireById(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("User not found."));
    }

}
