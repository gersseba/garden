package com.gersseba.garden.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "localized_texts",
        indices = {@Index(value = {"entity_type", "entity_id", "key"})}
)
public class LocalizedTextEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    @ColumnInfo(name = "entity_type")
    public String entityType;

    @ColumnInfo(name = "entity_id")
    public long entityId;

    @NonNull
    @ColumnInfo(name = "key")
    public String key;

    @ColumnInfo(name = "text_en")
    public String textEn;

    @ColumnInfo(name = "text_de")
    public String textDe;

    @ColumnInfo(name = "updated_at")
    public long updatedAt;

    public LocalizedTextEntity(@NonNull String entityType,
                               long entityId,
                               @NonNull String key,
                               String textEn,
                               String textDe,
                               long updatedAt) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.key = key;
        this.textEn = textEn;
        this.textDe = textDe;
        this.updatedAt = updatedAt;
    }
}

