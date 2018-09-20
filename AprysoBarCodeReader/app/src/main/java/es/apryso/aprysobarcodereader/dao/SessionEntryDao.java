package es.apryso.aprysobarcodereader.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import es.apryso.aprysobarcodereader.entity.SessionEntryEntity;

@Dao
public interface SessionEntryDao {

    @Insert
    public Long insert(SessionEntryEntity sessionEntry);

    @Delete
    public void delete(SessionEntryEntity sessionEntryEntity);

    @Update
    public void update(SessionEntryEntity sessionEntry);
}
