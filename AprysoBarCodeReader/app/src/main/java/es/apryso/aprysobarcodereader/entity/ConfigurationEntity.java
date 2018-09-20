package es.apryso.aprysobarcodereader.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import es.apryso.aprysobarcodereader.dao.converter.LibraryTypeConverter;
import es.apryso.aprysobarcodereader.dao.converter.SendModeConverter;

@Entity(tableName = "configuration")
public class ConfigurationEntity {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    public int id;

    public String serverName;

    public Integer serverPort;

    @TypeConverters(LibraryTypeConverter.class)
    public LibraryType libraryType;

    @TypeConverters(SendModeConverter.class)
    public SendMode sendMode;

    public enum LibraryType {
        ZXING(0),
        FIREBASE(1);

        private int code;

        LibraryType(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }

    public enum  SendMode {
        ONLINE(0),
        OFFLINE(1);

        private int code;

        SendMode(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }

}
