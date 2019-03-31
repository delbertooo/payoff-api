package de.delbertooo.payoff.apiserver.common.jpa;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

public class LockRepositoryImpl implements LockRepository {

    private EntityManager entityManager;

    @Autowired
    public LockRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void lock(Object entity, LockModeType lockModeType) {
        entityManager.lock(entity, lockModeType);
    }
}
