package es.apryso.aprysobarcodereader.service;

import android.content.Context;

import es.apryso.aprysobarcodereader.dao.ConfigurationDao;
import es.apryso.aprysobarcodereader.dao.DataBase;
import es.apryso.aprysobarcodereader.entity.ConfigurationEntity;

public class ConfigurationService {

    public void setDefaultConfiguration(Context context) {

        ConfigurationDao configurationDao = DataBase.getInstance(context).configurationDao();
        ConfigurationEntity configurationEntity = configurationDao.get(ConfigurationDao.CONFIGURATION_PK);
        if (configurationEntity == null) {

            configurationEntity = new ConfigurationEntity();
            configurationEntity.id = ConfigurationDao.CONFIGURATION_PK;
            configurationEntity.serverName  = "";
            configurationEntity.serverPort = null;
            configurationEntity.libraryType = ConfigurationEntity.LibraryType.ZXING;
            configurationEntity.sendMode = ConfigurationEntity.SendMode.OFFLINE;
            configurationDao.insert(configurationEntity);
        }
    }

    public ConfigurationEntity getConfiguration(Context context) {

        ConfigurationDao configurationDao = DataBase.getInstance(context).configurationDao();
        return configurationDao.get(ConfigurationDao.CONFIGURATION_PK);
    }
}
