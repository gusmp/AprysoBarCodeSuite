package es.apryso.aprysobarcodereader.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import java.util.Date;

import es.apryso.aprysobarcodereader.dao.converter.DateConverter;

@Entity(tableName = "session_entry",
        foreignKeys = @ForeignKey(entity =  SessionEntity.class,
                                    parentColumns = "id",
                                    childColumns = "sessionId",
                                    onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "sessionId", unique = false)
)

public class SessionEntryEntity {

    @PrimaryKey(autoGenerate = true)
    public Long id;

    public Long sessionId;

    public String content;

    public String barcodeFormat;

    public Integer numberOfItems;

    @TypeConverters(DateConverter.class)
    public Date timestamp;
}
