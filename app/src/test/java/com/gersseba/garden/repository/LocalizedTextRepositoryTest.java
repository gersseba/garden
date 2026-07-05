package com.gersseba.garden.repository;

import com.gersseba.garden.database.dao.LocalizedTextDao;
import com.gersseba.garden.database.entity.LocalizedTextEntity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

public class LocalizedTextRepositoryTest {

    private static class FakeDao implements LocalizedTextDao {
        private final Map<String, LocalizedTextEntity> store = new HashMap<>();
        private long nextId = 1;

        private String key(String entityType, long entityId, String key) {
            return entityType + "|" + entityId + "|" + key;
        }

        @Override
        public long insertOrUpdate(LocalizedTextEntity entity) {
            String k = key(entity.entityType, entity.entityId, entity.key);
            if (entity.id == 0) entity.id = nextId++;
            store.put(k, entity);
            return entity.id;
        }

        @Override
        public androidx.lifecycle.LiveData<LocalizedTextEntity> getByEntityAndKeyLive(String entityType, long entityId, String key) {
            // Return non-null LiveData for consumers; value may be null
            return new androidx.lifecycle.MutableLiveData<>(null);
        }

        @Override
        public LocalizedTextEntity getByEntityAndKeySync(String entityType, long entityId, String key) {
            return store.get(key(entityType, entityId, key));
        }

        @Override
        public void deleteByEntity(String entityType, long entityId) {
            // not needed
        }

        @Override
        public void deleteById(long id) {
            // not needed
        }
    }

    private FakeDao dao;
    private LocalizedTextRepository repository;

    @Before
    public void setup() {
        dao = new FakeDao();
        // Run write operations synchronously for tests
        Executor direct = Runnable::run;
        repository = new LocalizedTextRepository(dao, direct);
    }

    @Test
    public void upsert_and_get_returns_locale_specific_text() {
        repository.upsertLocalizedText("plant", 1L, "general_info", "Hello EN", "Hallo DE");

        String de = repository.getTextForKey("plant", 1L, "general_info", Locale.GERMAN);
        Assert.assertEquals("Hallo DE", de);

        String en = repository.getTextForKey("plant", 1L, "general_info", Locale.ENGLISH);
        Assert.assertEquals("Hello EN", en);
    }

    @Test
    public void fallback_to_english_when_requested_locale_empty() {
        repository.upsertLocalizedText("plant", 1L, "general_info", "Hello EN", "");

        String de = repository.getTextForKey("plant", 1L, "general_info", Locale.GERMAN);
        // German text is empty -> should fall back to English
        Assert.assertEquals("Hello EN", de);
    }

    @Test
    public void repository_scopes_by_entityType_and_id() {
        repository.upsertLocalizedText("plant", 1L, "general_info", "Plant1 EN", "Plant1 DE");
        repository.upsertLocalizedText("plant", 2L, "general_info", "Plant2 EN", "Plant2 DE");

        String p1 = repository.getTextForKey("plant", 1L, "general_info", Locale.ENGLISH);
        String p2 = repository.getTextForKey("plant", 2L, "general_info", Locale.ENGLISH);

        Assert.assertEquals("Plant1 EN", p1);
        Assert.assertEquals("Plant2 EN", p2);
    }
}


