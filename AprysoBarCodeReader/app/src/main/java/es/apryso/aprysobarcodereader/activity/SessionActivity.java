package es.apryso.aprysobarcodereader.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import es.apryso.aprysobarcodereader.dao.DataBase;
import es.apryso.aprysobarcodereader.entity.ConfigurationEntity;
import es.apryso.aprysobarcodereader.entity.SessionEntity;
import es.apryso.aprysobarcodereader.entity.SessionEntryEntity;
import es.apryso.aprysobarcodereader.service.ConfigurationService;
import es.apryso.aprysobarcodereader.service.SendService;
import es.apryso.aprysobarcodereader.service.ServiceFactory;
import es.apryso.aprysobarcodereader.utils.PopUpUtils;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;


import java.io.File;
import java.util.Date;
import java.util.List;


public class SessionActivity extends BaseActivity {

    // UI
    private boolean launchBarCodeReader;
    private ConfigurationEntity.LibraryType libraryType;

    static final int ZXING_BARCODE_REQUEST = 49374;
    static final int FIREBASE_BARCODE_REQUEST = 2;

    private FirebaseVisionBarcodeDetector firebase_detector;
    private static int firebase_captureLastId;

    private TextView session_data_textview;
    private TextView session_barcode_type_textview;
    private TextView session_read_number_textview;
    private Button session_add_button;
    private Button session_substract_button;

    // persistence
    private Long sessionId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        this.session_data_textview = (TextView) findViewById(R.id.session_data_textview);
        this.session_barcode_type_textview = (TextView) findViewById(R.id.session_barcode_type_textview);
        session_read_number_textview = (TextView) findViewById(R.id.session_read_number_textview);

        this.session_add_button = (Button) findViewById(R.id.session_add_button);
        this.session_substract_button = (Button) findViewById(R.id.session_substract_button);

        if (savedInstanceState == null) {

            this.launchBarCodeReader = true;
            this.libraryType = ((ConfigurationService) ServiceFactory.getInstance(ServiceFactory.SERVICE_INSTANCE.CONFIGURATION_SERVICE)).getConfiguration(getApplicationContext()).libraryType;

            if (this.libraryType == ConfigurationEntity.LibraryType.FIREBASE) {
                this.firebase_detector = FirebaseVision.getInstance().getVisionBarcodeDetector();
            }
        } else {
            this.sessionId = savedInstanceState.getLong("sessionId");
            this.launchBarCodeReader = savedInstanceState.getBoolean("launchBarCodeReader");
            this.session_data_textview.setText(savedInstanceState.getCharSequence("session_data_textview"));
            this.session_barcode_type_textview.setText(savedInstanceState.getCharSequence("session_barcode_type_textview"));
            this.session_read_number_textview.setText(savedInstanceState.getCharSequence("session_read_number_textview"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState) {

        super.onSaveInstanceState(saveInstanceState);
        if (this.sessionId != null) saveInstanceState.putLong("sessionId",this.sessionId);
        saveInstanceState.putBoolean("launchBarCodeReader", this.launchBarCodeReader);
        saveInstanceState.putCharSequence("session_data_textview", this.session_data_textview.getText());
        saveInstanceState.putCharSequence("session_barcode_type_textview", this.session_barcode_type_textview.getText());
        saveInstanceState.putCharSequence("session_read_number_textview", this.session_read_number_textview.getText());
    }

    @Override
    public void onBackPressed() {

        PopUpUtils.showPopup(getApplicationContext(), getString(R.string.session_no_back_button));
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (this.launchBarCodeReader == true) {
            startBarCodeReader();
        }
    }

    private void startBarCodeReader() {

        if (this.libraryType == ConfigurationEntity.LibraryType.ZXING) {

            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setPrompt(getString(R.string.session_barcode_read_label));
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(true);
            integrator.setOrientationLocked(true);
            integrator.initiateScan();

        } else {

            this.firebase_captureLastId = getLastImageId();
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, FIREBASE_BARCODE_REQUEST);
            }
        }

    }

    private void goBack() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void updateViewWithoutRead() {

        this.session_data_textview.setText("");
        this.session_barcode_type_textview.setText("");
        this.session_read_number_textview.setText("0");

        this.session_add_button.setEnabled(false);
        this.session_substract_button.setEnabled(false);

        PopUpUtils.showPopup(getApplicationContext(), getString(R.string.session_without_read));

    }

    private String getFirebaseDescriptionBarcodeFormat(int codeFormat) {

        if (codeFormat == FirebaseVisionBarcode.FORMAT_CODE_128) {
            return "FORMAT_CODE_128";
        } else if (codeFormat == FirebaseVisionBarcode.FORMAT_CODE_39) {
            return "FORMAT_CODE_39";
        } else if (codeFormat == FirebaseVisionBarcode.FORMAT_CODE_93) {
            return "FORMAT_CODE_93";
        } else if (codeFormat == FirebaseVisionBarcode.FORMAT_CODABAR) {
            return "FORMAT_CODABAR";
        } else if (codeFormat == FirebaseVisionBarcode.FORMAT_DATA_MATRIX) {
            return "FORMAT_DATA_MATRIX";
        } else if (codeFormat == FirebaseVisionBarcode.FORMAT_EAN_13) {
            return "FORMAT_EAN_13";
        } else if (codeFormat == FirebaseVisionBarcode.FORMAT_EAN_8) {
            return "FORMAT_EAN_8";
        } else if (codeFormat == FirebaseVisionBarcode.FORMAT_ITF) {
            return "FORMAT_ITF";
        } else if (codeFormat == FirebaseVisionBarcode.FORMAT_QR_CODE) {
            return "FORMAT_QR_CODE";
        } else if (codeFormat == FirebaseVisionBarcode.FORMAT_UPC_A) {
            return "FORMAT_UPC_A";
        }  else if (codeFormat == FirebaseVisionBarcode.FORMAT_UPC_E) {
            return "FORMAT_UPC_E";
        } else if (codeFormat == FirebaseVisionBarcode.FORMAT_PDF417) {
            return "FORMAT_PDF417";
        } else if (codeFormat == FirebaseVisionBarcode.FORMAT_AZTEC) {
            return "FORMAT_AZTEC";
        } else {
            return getString(R.string.session_firebase_unknown_barcode_format);
        }
    }

    private int getLastImageId() {

        final String[] imageColumns = { MediaStore.Images.Media._ID };
        final String imageOrderBy = MediaStore.Images.Media._ID+" DESC";
        final String imageWhere = null;
        final String[] imageArguments = null;
        Cursor imageCursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, imageWhere, imageArguments, imageOrderBy);

        if(imageCursor.moveToFirst()){
            int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
            imageCursor.close();
            return id;
        }else{
            return 0;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == ZXING_BARCODE_REQUEST) {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();

                if (scanFormat != null) {

                    this.session_data_textview.setText(scanContent);
                    this.session_barcode_type_textview.setText(scanFormat);
                    this.session_read_number_textview.setText("1");

                    this.session_add_button.setEnabled(true);
                    this.session_substract_button.setEnabled(true);
                } else {
                    updateViewWithoutRead();
                }
            } else {

                updateViewWithoutRead();
            }
        } else if (requestCode == FIREBASE_BARCODE_REQUEST) {

            if (resultCode == RESULT_OK) {

                Bundle extras = intent.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                final String[] imageColumns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.SIZE, MediaStore.Images.Media._ID };
                final String imageOrderBy = MediaStore.Images.Media._ID+" DESC";
                final String imageWhere = MediaStore.Images.Media._ID+">?";
                final String[] imageArguments = { Integer.toString(SessionActivity.this.firebase_captureLastId) };
                Cursor imageCursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, imageWhere, imageArguments, imageOrderBy);

                if(imageCursor.getCount()>0){
                    while(imageCursor.moveToNext()){

                        //int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
                        String path = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        //Long takenTimeStamp = imageCursor.getLong(imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                        //Long size = imageCursor.getLong(imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE));

                        File f = new File(path);
                        f.delete();
                    }
                }

                FirebaseApp.initializeApp(this);
                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
                Task<List<FirebaseVisionBarcode>> result = this.firebase_detector.detectInImage(image);

                result.addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> barcodes) {

                        if (barcodes.size() > 0) {

                            session_data_textview.setText(barcodes.get(0).getRawValue());
                            session_barcode_type_textview.setText(getFirebaseDescriptionBarcodeFormat(barcodes.get(0).getFormat()));
                            session_read_number_textview.setText("1");

                            session_add_button.setEnabled(true);
                            session_substract_button.setEnabled(true);
                        }
                        else {
                            updateViewWithoutRead();
                        }
                    }
                });
                result.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        updateViewWithoutRead();
                    }
                });
            }
            else {
                updateViewWithoutRead();
            }
        }

        this.launchBarCodeReader = false;
    }

    public void addReadClickHandler(View view) {

        int readNumber = Integer.parseInt(this.session_read_number_textview.getText().toString());
        readNumber = readNumber + 1;
        this.session_read_number_textview.setText(String.valueOf(readNumber));

        if (readNumber > 0) {
            this.session_substract_button.setEnabled(true);
        }
    }

    public void substractReadClickHandler(View view) {

        int readNumber = Integer.parseInt(this.session_read_number_textview.getText().toString());
        readNumber = readNumber - 1;
        this.session_read_number_textview.setText(String.valueOf(readNumber));

        if (readNumber == 0) {
            this.session_substract_button.setEnabled(false);
        }
    }

    private void saveData() {

        int numberOfReads = Integer.parseInt(this.session_read_number_textview.getText().toString());

        if ((this.sessionId == null) && (numberOfReads > 0)) {
            SessionEntity sessionEntity = new SessionEntity();
            sessionEntity.timestamp = new Date();

            this.sessionId = DataBase.getInstance(getApplicationContext()).sessionDao().insert(sessionEntity);
        }

        if (numberOfReads > 0) {
            SessionEntryEntity sessionEntryEntity = new SessionEntryEntity();
            sessionEntryEntity.content = this.session_data_textview.getText().toString();
            sessionEntryEntity.barcodeFormat = this.session_barcode_type_textview.getText().toString();
            sessionEntryEntity.numberOfItems = numberOfReads;
            sessionEntryEntity.timestamp = new Date();
            sessionEntryEntity.sessionId = this.sessionId;

            DataBase.getInstance(getApplicationContext()).sessionEntryDao().insert(sessionEntryEntity);
        }
    }

    private void sendToServer() {

        if ((this.sessionId != null) &&
                (((ConfigurationService) ServiceFactory.getInstance(ServiceFactory.SERVICE_INSTANCE.CONFIGURATION_SERVICE)).getConfiguration(getApplicationContext()).sendMode == ConfigurationEntity.SendMode.ONLINE)) {

            this.disableScreen();
            PopUpUtils.showPopup(getApplicationContext(), getString(R.string.session_sending));

            SendService sendService =  (SendService) ServiceFactory.getInstance(ServiceFactory.SERVICE_INSTANCE.SEND_SERVICE);

            sendService.sendSession(this.sessionId,
                (success) -> {

                    if (success == false) {

                        PopUpUtils.showPopup(getApplicationContext(), getString(R.string.session_error_sending), Toast.LENGTH_LONG);
                    }

                    enableScreen();
                    goBack();

                },
                getApplicationContext());
        } else {
            goBack();
        }

    }

    public void saveAndFinishClickHandler(View view) {

        saveData();
        sendToServer();
    }

    public void saveAndContinueClickHandler(View view) {

        saveData();
        startBarCodeReader();
    }

    public void cancelAndFinishClickHandler(View view) {

        sendToServer();
    }

    public void cancelAndContinueClickHandler(View view) {

        startBarCodeReader();
    }

}
