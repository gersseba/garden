package com.gersseba.garden.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.gersseba.garden.database.dao.PhotoDao;
import com.gersseba.garden.database.dao.PlantDao;
import com.gersseba.garden.database.entity.PhotoEntity;
import com.gersseba.garden.database.entity.PlantEntity;

@Database(entities = {PlantEntity.class, PhotoEntity.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    public abstract PlantDao plantDao();

    public abstract PhotoDao photoDao();

    @NonNull
    public static AppDatabase getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "garden.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
