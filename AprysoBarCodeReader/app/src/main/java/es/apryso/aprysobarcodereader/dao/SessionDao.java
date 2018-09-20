package es.apryso.aprysobarcodereader.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import es.apryso.aprysobarcodereader.entity.SessionEntity;
import es.apryso.aprysobarcodereader.entity.SessionEntryEntity;
import es.apryso.aprysobarcodereader.entity.SessionWithItems;

@Dao
public interface SessionDao {

    @Insert
    public Long insert(SessionEntity session);

    @Update
    public void update(SessionEntity session);

    @Delete
    public void delete(SessionEntity session);

    @Transaction
    @Query("SELECT * FROM session WHERE id=:id")
    public SessionWithItems loadSessionWithItems(Long id);

    @Transaction
    @Query("SELECT * FROM session ORDER BY timestamp DESC")
    public List<SessionWithItems> loadAllSessionsWithItems();


}
