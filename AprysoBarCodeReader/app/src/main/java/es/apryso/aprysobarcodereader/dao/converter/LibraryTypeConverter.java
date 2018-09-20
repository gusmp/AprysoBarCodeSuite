package es.apryso.aprysobarcodereader.dao.converter;

import android.arch.persistence.room.TypeConverter;

import es.apryso.aprysobarcodereader.entity.ConfigurationEntity;

public class LibraryTypeConverter {

    @TypeConverter
    public static ConfigurationEntity.LibraryType toLibrayType(int value) {

        if (ConfigurationEntity.LibraryType.ZXING.getCode() == value) {
            return ConfigurationEntity.LibraryType.ZXING;
        } else {
            return ConfigurationEntity.LibraryType.FIREBASE;
        }
    }

    @TypeConverter
    public static int toInt(ConfigurationEntity.LibraryType libraryType) {
        return libraryType.getCode();
    }

}
