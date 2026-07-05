package com.gersseba.garden.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gersseba.garden.database.dao.LocalizedTextDao;
import com.gersseba.garden.database.entity.LocalizedTextEntity;

import java.util.Locale;
import java.util.concurrent.Executor;

/**
 * No-op repository used as deterministic fallback when DB-backed repository is unavailable.
 */
public class NoOpLocalizedTextRepository extends LocalizedTextRepository {

    private static class EmptyDao implements LocalizedTextDao {
        @Override
        public long insertOrUpdate(LocalizedTextEntity entity) { return 0; }

        @Override
        public LiveData<LocalizedTextEntity> getByEntityAndKeyLive(String entityType, long entityId, String key) { return new MutableLiveData<>(null); }

        @Override
        public LocalizedTextEntity getByEntityAndKeySync(String entityType, long entityId, String key) { return null; }

        @Override
        public void deleteByEntity(String entityType, long entityId) { }

        @Override
        public void deleteById(long id) { }
    }

    public NoOpLocalizedTextRepository() {
        super(new EmptyDao(), Runnable::run);
    }
}



