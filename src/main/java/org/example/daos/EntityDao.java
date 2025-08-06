package org.example.daos;

import java.util.Collection;
import java.util.Optional;

public interface EntityDao <TEntity, TId> {
    TEntity add(TEntity newEntity);
    void delete(TId id);
    void update(TEntity updatedEntity);
    Optional<TEntity> get(long id);
    Collection<TEntity> getAll();
}
