package es.apryso.aprysobarcodereader.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import es.apryso.aprysobarcodereader.entity.ConfigurationEntity;
import es.apryso.aprysobarcodereader.entity.SessionEntity;
import es.apryso.aprysobarcodereader.entity.SessionEntryEntity;

@Database(entities = {ConfigurationEntity.class, SessionEntity.class, SessionEntryEntity.class}, version = 1, exportSchema = true)
public abstract class DataBase extends RoomDatabase {

    private static DataBase INSTANCE;
    private static String DB_NAME = "aprysobarcodereader.db";

    public abstract ConfigurationDao configurationDao();

    public abstract SessionDao sessionDao();

    public abstract SessionEntryDao sessionEntryDao();

    public static DataBase getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE  = Room.databaseBuilder(context, DataBase.class, DB_NAME )
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;

    }
}
