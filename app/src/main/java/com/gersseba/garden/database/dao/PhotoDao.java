package com.gersseba.garden.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.gersseba.garden.database.entity.PhotoEntity;

import java.util.List;

@Dao
public interface PhotoDao {

    @Query("SELECT * FROM photos WHERE plant_id = :plantId ORDER BY id DESC")
    LiveData<List<PhotoEntity>> observePhotosForPlant(long plantId);

    @Insert
    void insertAll(List<PhotoEntity> photos);

    @Query("DELETE FROM photos WHERE id = :photoId")
    void deleteById(long photoId);
}
