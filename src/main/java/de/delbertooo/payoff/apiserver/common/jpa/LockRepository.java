package de.delbertooo.payoff.apiserver.common.jpa;

import javax.persistence.LockModeType;

public interface LockRepository {
    void lock(Object entity, LockModeType lockModeType);
}
