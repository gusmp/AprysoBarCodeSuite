package es.apryso.aprysobarcodereader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import es.apryso.aprysobarcodereader.service.ConfigurationService;
import es.apryso.aprysobarcodereader.service.ServiceFactory;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ConfigurationService.setDefaultConfiguration(getApplicationContext());
        ((ConfigurationService) ServiceFactory.getInstance(ServiceFactory.SERVICE_INSTANCE.CONFIGURATION_SERVICE)).setDefaultConfiguration(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void initSessionBtClickHandler(View view) {

        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
    }

    public void sessionManagerBtClickHandler(View view) {

        Intent intent = new Intent(this, SessionManagerActivity.class);
        startActivity(intent);
    }

    public void configBtClickHandler(View view) {

        Intent intent = new Intent(this, ConfigActivity.class);
        startActivity(intent);
    }

    public void aboutBtClickHandler(View view) {

        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}
