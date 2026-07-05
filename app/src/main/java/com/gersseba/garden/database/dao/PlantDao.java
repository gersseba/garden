package com.gersseba.garden.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.gersseba.garden.database.entity.PlantEntity;

import java.util.List;

@Dao
public interface PlantDao {

    @Query("SELECT * FROM plants ORDER BY id DESC")
    LiveData<List<PlantEntity>> observeAllPlants();

    @Query("SELECT * FROM plants WHERE id = :plantId LIMIT 1")
    LiveData<PlantEntity> observePlant(long plantId);

    @Insert
    long insert(PlantEntity plant);

    @Delete
    void delete(PlantEntity plant);

    @Query("DELETE FROM plants WHERE id = :plantId")
    void deleteById(long plantId);
}
