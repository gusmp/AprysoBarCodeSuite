package es.apryso.aprysobarcodereader.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import es.apryso.aprysobarcodereader.entity.ConfigurationEntity;

@Dao
public interface ConfigurationDao {

    public static final int CONFIGURATION_PK = 1;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ConfigurationEntity configurationEntity);

    @Update
    void update(ConfigurationEntity configurationEntity);

    @Query("SELECT * FROM  configuration WHERE id=:id")
    ConfigurationEntity get(Integer id);


}
