package es.apryso.aprysobarcodereader.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public abstract class BaseActivity extends AppCompatActivity {

    protected boolean disableBackButton = false;

    public void disableScreen() {

        this.disableBackButton = true;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void enableScreen() {

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        this.disableBackButton = false;

    }
}
