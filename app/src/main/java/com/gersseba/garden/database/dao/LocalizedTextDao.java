package com.gersseba.garden.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gersseba.garden.database.entity.LocalizedTextEntity;

import java.util.List;

@Dao
public interface LocalizedTextDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOrUpdate(LocalizedTextEntity entity);

    @Query("SELECT * FROM localized_texts WHERE entity_type = :entityType AND entity_id = :entityId AND `key` = :key LIMIT 1")
    LiveData<LocalizedTextEntity> getByEntityAndKeyLive(String entityType, long entityId, String key);

    @Query("SELECT * FROM localized_texts WHERE entity_type = :entityType AND entity_id = :entityId AND `key` = :key LIMIT 1")
    LocalizedTextEntity getByEntityAndKeySync(String entityType, long entityId, String key);

    @Query("DELETE FROM localized_texts WHERE entity_type = :entityType AND entity_id = :entityId")
    void deleteByEntity(String entityType, long entityId);

    @Query("DELETE FROM localized_texts WHERE id = :id")
    void deleteById(long id);
}
