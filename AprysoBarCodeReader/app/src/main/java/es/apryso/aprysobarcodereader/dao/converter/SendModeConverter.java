package es.apryso.aprysobarcodereader.dao.converter;

import android.arch.persistence.room.TypeConverter;

import es.apryso.aprysobarcodereader.entity.ConfigurationEntity;


public class SendModeConverter {

    @TypeConverter
    public static ConfigurationEntity.SendMode toSendMode(int value) {

        if (ConfigurationEntity.SendMode.ONLINE.getCode() == value) {
            return ConfigurationEntity.SendMode.ONLINE;
        } else {
            return ConfigurationEntity.SendMode.OFFLINE;
        }
    }

    @TypeConverter
    public static int toInt(ConfigurationEntity.SendMode sendMode) {
        return sendMode.getCode();
    }

}
