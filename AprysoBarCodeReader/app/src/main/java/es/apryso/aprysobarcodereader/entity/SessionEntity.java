package es.apryso.aprysobarcodereader.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;

import es.apryso.aprysobarcodereader.dao.converter.DateConverter;

@Entity(tableName = "session")
public class SessionEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long id;

    @TypeConverters(DateConverter.class)
    public Date timestamp;


}
