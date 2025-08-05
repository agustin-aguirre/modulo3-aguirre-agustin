package org.example.daos;

import java.util.Collection;
import java.util.Optional;

public interface EntityDao <TEntity, TId> {
    void add(TEntity newEntity);
    void delete(TId id);
    void update(TId id, TEntity updatedEntity);
    Optional<TEntity> get(long id);
    Collection<TEntity> getAll();
}
