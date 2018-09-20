package es.apryso.aprysobarcodereader.activity;

import android.arch.persistence.room.Database;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import es.apryso.aprysobarcodereader.dao.ConfigurationDao;
import es.apryso.aprysobarcodereader.dao.DataBase;
import es.apryso.aprysobarcodereader.entity.ConfigurationEntity;
import es.apryso.aprysobarcodereader.service.SendService;
import es.apryso.aprysobarcodereader.service.ServiceFactory;
import es.apryso.aprysobarcodereader.utils.PopUpUtils;


public class ConfigActivity extends BaseActivity {

    private TextView config_server_name_editText;
    private TextView config_server_port_editText;
    private TextView config_barcode_library_info_textview;
    private Switch config_barcode_library_switch;
    private Switch config_send_mode_switch;
    private Button config_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        this.config_server_name_editText = findViewById(R.id.config_server_name_editText);
        this.config_server_port_editText = findViewById(R.id.config_server_port_editText);
        this.config_barcode_library_info_textview = findViewById(R.id.config_barcode_library_info_textview);
        this.config_barcode_library_info_textview.setText(getResources().getString(R.string.config_zxing_description));
        this.config_barcode_library_switch = findViewById(R.id.config_barcode_library_switch);
        this.config_send_mode_switch = findViewById(R.id.config_send_mode_switch);
        this.config_save = findViewById(R.id.config_save);

        if (savedInstanceState == null) {
            ConfigurationDao configurationDao = DataBase.getInstance(getApplicationContext()).configurationDao();
            ConfigurationEntity configurationEntity = configurationDao.get(ConfigurationDao.CONFIGURATION_PK);

            this.config_server_name_editText.setText(configurationEntity.serverName);
            if (configurationEntity.serverPort != null) {
                this.config_server_port_editText.setText(String.valueOf(configurationEntity.serverPort));
            }

            if (configurationEntity.libraryType == ConfigurationEntity.LibraryType.ZXING) {
                this.config_barcode_library_switch.setChecked(true);
                this.config_barcode_library_info_textview.setText(getString(R.string.config_zxing_description));
            } else {
                this.config_barcode_library_switch.setChecked(false);
                this.config_barcode_library_info_textview.setText(getString(R.string.config_firebase_description));
            }

            if (configurationEntity.sendMode == ConfigurationEntity.SendMode.ONLINE) {
                this.config_send_mode_switch.setChecked(true);
            } else {
                this.config_send_mode_switch.setChecked(false);
            }


        } else {

            this.config_barcode_library_switch.setChecked(savedInstanceState.getBoolean("config_barcode_library_switch"));
            this.config_send_mode_switch.setChecked(savedInstanceState.getBoolean("config_send_mode_switch"));
            this.config_server_name_editText.setText(savedInstanceState.getCharSequence("config_server_name_editText"));
            this.config_server_port_editText.setText(savedInstanceState.getCharSequence("config_server_port_editText"));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState) {

        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putBoolean("config_barcode_library_switch", this.config_barcode_library_switch.isChecked());
        saveInstanceState.putBoolean("config_send_mode_switch", this.config_send_mode_switch.isChecked());
        saveInstanceState.putCharSequence("config_server_name_editText", this.config_server_name_editText.getText());
        saveInstanceState.putCharSequence("config_server_port_editText", this.config_server_port_editText.getText());
    }

    @Override
    public void onBackPressed() {

        if (this.disableBackButton == false) goBack();
    }

    public void barcodeLibrarySwClickHandler(View view) {

        if (this.config_barcode_library_switch.isChecked()) {
            this.config_barcode_library_info_textview.setText(getResources().getString(R.string.config_zxing_description));
        } else {
            this.config_barcode_library_info_textview.setText(getResources().getString(R.string.config_firebase_description));
        }
    }

    private ConfigurationEntity setConfigurationEntity(ConfigurationEntity configurationEntity) {

        if (this.config_barcode_library_switch.isChecked() == true) {
            configurationEntity.libraryType = ConfigurationEntity.LibraryType.ZXING;
        } else {
            configurationEntity.libraryType = ConfigurationEntity.LibraryType.FIREBASE;
        }

        if (this.config_send_mode_switch.isChecked() == true) {
            configurationEntity.serverName = this.config_server_name_editText.getText().toString();
            configurationEntity.serverPort = Integer.parseInt(this.config_server_port_editText.getText().toString());
            configurationEntity.sendMode = ConfigurationEntity.SendMode.ONLINE;

        } else {
            configurationEntity.sendMode = ConfigurationEntity.SendMode.OFFLINE;
        }

        return configurationEntity;

    }

    private void serverDataValidation(SendService.SendDataCallback callback) {


        if (this.config_server_name_editText.getText().toString().length() < 4) {
            PopUpUtils.showPopup(getApplicationContext(), getString(R.string.config_error_server_name));
        } else if (this.config_server_port_editText.getText() == null) {
            PopUpUtils.showPopup(getApplicationContext(), getString(R.string.config_error_server_port));
        } else if ((this.config_server_port_editText.getText().toString().length() < 2) ||
                    (this.config_server_port_editText.getText().toString().length() > 5)) {
            PopUpUtils.showPopup(getApplicationContext(), getString(R.string.config_error_server_port_length));
        } else {
            if (Integer.valueOf(this.config_server_port_editText.getText().toString()).intValue() > 65536) {
                PopUpUtils.showPopup(getApplicationContext(), getString(R.string.config_error_server_port_value));
            }
        }

        SendService sendService = (SendService) ServiceFactory.getInstance(ServiceFactory.SERVICE_INSTANCE.SEND_SERVICE);

        sendService.checkServerStatus(getApplicationContext(),
                this.config_server_name_editText.getText().toString(),
                this.config_server_port_editText.getText().toString(),
                callback);

    }

    public void saveBtClickHandler(View view) {

        if (this.config_send_mode_switch.isChecked() == false) {

            ConfigurationDao configurationDao = DataBase.getInstance(getApplicationContext()).configurationDao();
            ConfigurationEntity configurationEntity = configurationDao.get(ConfigurationDao.CONFIGURATION_PK);
            configurationEntity = setConfigurationEntity(configurationEntity);
            configurationDao.update(configurationEntity);
            goBack();
        } else {


            disableScreen();
            this.config_save.setText(getString(R.string.config_validation));

            SendService.SendDataCallback callback = (success) -> {
                if (success == true) {

                    ConfigurationDao configurationDao = DataBase.getInstance(getApplicationContext()).configurationDao();
                    ConfigurationEntity configurationEntity = configurationDao.get(ConfigurationDao.CONFIGURATION_PK);
                    configurationEntity = setConfigurationEntity(configurationEntity);
                    configurationDao.update(configurationEntity);
                    goBack();
                } else {
                    PopUpUtils.showPopup(getApplicationContext(), getString(R.string.config_error_server_no_connectivity), Toast.LENGTH_LONG);
                }

                this.config_save.setText(getString(R.string.config_save));
                enableScreen();
            };

            serverDataValidation(callback);
        }
    }

    public void backBtClickHandler(View view) {

        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
