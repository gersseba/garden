package com.gersseba.garden.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.gersseba.garden.database.AppDatabase;
import com.gersseba.garden.database.dao.LocalizedTextDao;
import com.gersseba.garden.database.entity.LocalizedTextEntity;
import com.gersseba.garden.i18n.LocalizedTextDataSource;

import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Repository for long localized texts stored in the DB.
 */
public class LocalizedTextRepository implements LocalizedTextDataSource {

    private final LocalizedTextDao dao;
    private final Executor writeExecutor;

    public LocalizedTextRepository(@NonNull Application application) {
        this(AppDatabase.getInstance(application).localizedTextDao(), Executors.newSingleThreadExecutor());
    }

    // Visible for tests
    public LocalizedTextRepository(@NonNull LocalizedTextDao dao, @NonNull Executor writeExecutor) {
        this.dao = dao;
        this.writeExecutor = writeExecutor;
    }

    /**
     * Returns a LiveData wrapping the localized text following fallback order:
     * requested locale -> english -> null
     */
    public LiveData<String> getLocalizedTextLive(@NonNull String entityType, long entityId, @NonNull String key, @NonNull Locale locale) {
        LiveData<LocalizedTextEntity> source = dao.getByEntityAndKeyLive(entityType, entityId, key);
        return Transformations.map(source, entity -> {
            if (entity == null) return null;
            String lang = locale.getLanguage();
            if ("de".equalsIgnoreCase(lang) && entity.textDe != null && !entity.textDe.isEmpty()) return entity.textDe;
            if (entity.textEn != null && !entity.textEn.isEmpty()) return entity.textEn;
            return null;
        });
    }

    /**
     * Upsert a localized text record.
     */
    public void upsertLocalizedText(@NonNull String entityType, long entityId, @NonNull String key,
                                    String textEn, String textDe) {
        long now = Instant.now().toEpochMilli();
        LocalizedTextEntity entity = new LocalizedTextEntity(entityType, entityId, key, textEn, textDe, now);
        writeExecutor.execute(() -> dao.insertOrUpdate(entity));
    }

    public void deleteLocalizedTextsForEntity(@NonNull String entityType, long entityId) {
        writeExecutor.execute(() -> dao.deleteByEntity(entityType, entityId));
    }

    public void deleteById(long id) {
        writeExecutor.execute(() -> dao.deleteById(id));
    }

    @Override
    public String getTextForKey(String key, Locale locale) {
        // Maintain backwards compatibility for callers without entity context: delegate
        return getTextForKey("", 0L, key, locale);
    }

    /**
     * Synchronous lookup allowing callers to scope by entity type and id.
     * Fallback order: requested locale -> english -> null
     */
    public String getTextForKey(@NonNull String entityType, long entityId, @NonNull String key, Locale locale) {
        LocalizedTextEntity e = dao.getByEntityAndKeySync(entityType, entityId, key);
        if (e == null) return null;
        if (locale != null && "de".equalsIgnoreCase(locale.getLanguage()) && e.textDe != null && !e.textDe.isEmpty()) return e.textDe;
        if (e.textEn != null && !e.textEn.isEmpty()) return e.textEn;
        return null;
    }

}
