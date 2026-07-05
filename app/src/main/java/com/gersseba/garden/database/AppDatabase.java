package com.gersseba.garden.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gersseba.garden.database.dao.LocalizedTextDao;
import com.gersseba.garden.database.dao.PhotoDao;
import com.gersseba.garden.database.dao.PlantDao;
import com.gersseba.garden.database.entity.LocalizedTextEntity;
import com.gersseba.garden.database.entity.PhotoEntity;
import com.gersseba.garden.database.entity.PlantEntity;

@Database(entities = {PlantEntity.class, PhotoEntity.class, LocalizedTextEntity.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Create the localized_texts table added in version 2
            database.execSQL("CREATE TABLE IF NOT EXISTS localized_texts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "entity_type TEXT NOT NULL, " +
                    "entity_id INTEGER NOT NULL, " +
                    "`key` TEXT NOT NULL, " +
                    "text_en TEXT, " +
                    "text_de TEXT, " +
                    "updated_at INTEGER NOT NULL"
                    + ")");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_localized_texts_entity_type_entity_id_key ON localized_texts(entity_type, entity_id, `key`)");
        }
    };

    public abstract PlantDao plantDao();

    public abstract PhotoDao photoDao();

    public abstract LocalizedTextDao localizedTextDao();

    @NonNull
    public static AppDatabase getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "garden.db")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return instance;
    }
}
