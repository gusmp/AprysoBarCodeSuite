package es.apryso.aprysobarcodereader.service;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import es.apryso.aprysobarcodereader.dao.DataBase;
import es.apryso.aprysobarcodereader.dao.SessionDao;
import es.apryso.aprysobarcodereader.dao.SessionEntryDao;
import es.apryso.aprysobarcodereader.entity.ConfigurationEntity;
import es.apryso.aprysobarcodereader.entity.SessionEntryEntity;
import es.apryso.aprysobarcodereader.entity.SessionWithItems;
import es.apryso.aprysobarcodereader.utils.Logger;

public class SendService {

    private Gson gson = new Gson();
    private final int TIMEOUT = 10000;

    public interface  SendDataCallback {

        void execute(Boolean success);
    }

    private Map<String,String> sendData(String url, String method, String data) {

        StringBuffer stringBuffer = new StringBuffer();
        OutputStream os = null;
        InputStream is = null;
        BufferedReader br = null;
        HttpURLConnection httpURLConnection = null;
        Map<String,String> response = null;

        try {

            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setConnectTimeout(TIMEOUT);
            httpURLConnection.setReadTimeout(TIMEOUT);
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setDoInput(true);
            if (data != null) {
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                os = httpURLConnection.getOutputStream();
                os.write(data.getBytes());
                os.close();
            }


            is = new BufferedInputStream(httpURLConnection.getInputStream());
            br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                stringBuffer.append(inputLine);
            }

            Type type = new TypeToken<Map<String, String>>(){}.getType();
            response = gson.fromJson(stringBuffer.toString(), type);


        } catch(Exception exc) {
            Logger.error("Error sending data " + exc.toString());
            if (data != null) {
                Logger.error("Data: " + data);
            } else {
                Logger.error("Data: No data");
            }
        }
        finally {

            if (os != null) try { os.close(); } catch (Exception exc) {};
            if (br != null) try { br.close(); } catch (Exception exc) {};
            if (is != null) try { is.close(); } catch (Exception exc) {};
            if (httpURLConnection != null) try { httpURLConnection.disconnect(); } catch (Exception exc) {};
        }

        return response;

    }

    private class CheckServerStatusAsyncTask extends AsyncTask<String, Void, Map<String,String>> {

        private SendDataCallback callback;

        public CheckServerStatusAsyncTask(SendDataCallback callback) {

            this.callback = callback;
        }

        @Override
        protected Map<String,String> doInBackground(String... url) {

            return sendData(url[0],"GET", null);
        }

        @Override
        protected void onPostExecute(Map<String, String> responseMap) {

            if ((responseMap != null) && (responseMap.get("status") != null) && (responseMap.get("status").equalsIgnoreCase("OK") == true))
                this.callback.execute(true);
            else
                this.callback.execute(false);
        }
    }

    public void checkServerStatus(Context context, String serverName, String serverPort, SendDataCallback callback) {

        new CheckServerStatusAsyncTask(callback).execute("http://" + serverName + ":" + serverPort + "/aprysobarcodeserver/device/status");
    }


    private class SendItem {

        public String model;
        public String manufacturer;
        public String deviceId;

        public Long sessionTimestamp;

        public String barcodeFormat;
        public String content;
        public Integer numberOfItems;
        public Long entryTimestamp;

    }

    private class SessionSendResult {

        public Boolean deletedAllEnties;
        public Boolean deletedSession;
        public Boolean success;

        public SessionSendResult() {}

        public SessionSendResult(Boolean success, Boolean deletedSession, Boolean deletedAllEnties) {
            this.success = success;
            this.deletedSession = deletedSession;
            this.deletedAllEnties = deletedAllEnties;
        }

    }

    private class SendSessionAsyncTask extends AsyncTask<Void, Void, SessionSendResult> {

        private Long sessionId;
        private SendDataCallback callback;
        private Context context;

        public SendSessionAsyncTask(Long sessionId, SendDataCallback callback, Context context) {

            this.sessionId = sessionId;
            this.callback = callback;
            this.context = context;
        }

        @Override
        protected SessionSendResult doInBackground(Void... voids) {

            SessionWithItems sessionWithItems = DataBase.getInstance(this.context).sessionDao().loadSessionWithItems(this.sessionId);
            ConfigurationEntity configurationEntity = ((ConfigurationService) ServiceFactory.getInstance(ServiceFactory.SERVICE_INSTANCE.CONFIGURATION_SERVICE)).getConfiguration(this.context);
            SessionEntryDao sessionEntryDao = DataBase.getInstance(this.context).sessionEntryDao();
            Boolean sendAllEntriesOK = true;

            if ((configurationEntity.serverName == null) || (configurationEntity.serverPort == null)) {
                return new  SessionSendResult(false, false, false);
            }

            String url = "http://" +  configurationEntity.serverName + ":" + configurationEntity.serverPort + "/aprysobarcodeserver/device/save" ;

            SendItem sendItem = new SendItem();

            sendItem.model = Build.MODEL;
            sendItem.manufacturer = Build.MANUFACTURER;
            sendItem.deviceId =  Settings.Secure.getString(this.context.getContentResolver(), Settings.Secure.ANDROID_ID);

            sendItem.sessionTimestamp = sessionWithItems.session.timestamp.getTime();

            for(int i=0; i< sessionWithItems.entryList.size(); i++) {

                SessionEntryEntity see = sessionWithItems.entryList.get(i);
                sendItem.barcodeFormat = see.barcodeFormat;
                sendItem.content = see.content;
                sendItem.numberOfItems = see.numberOfItems;
                sendItem.entryTimestamp = see.timestamp.getTime();

                Map<String,String> response = sendData(url, "POST", gson.toJson(sendItem));

                if ((response != null) && (response.get("status") != null) && (response.get("status").equalsIgnoreCase("OK") == true)) {
                    sessionEntryDao.delete(see);
                } else {
                    sendAllEntriesOK = false;
                }
            }

            SessionSendResult result = new  SessionSendResult();
            result.success = false;
            result.deletedSession = false;
            result.deletedAllEnties = sendAllEntriesOK;

            if (sendAllEntriesOK == true) {
                SessionDao sessionDao = DataBase.getInstance(this.context).sessionDao();
                sessionDao.delete(sessionWithItems.session);

                result.success = true;
                result.deletedSession = true;
            }

            return result;
        }

        @Override
        protected void onPostExecute(SessionSendResult sessionSendResult) {

            this.callback.execute(sessionSendResult.success);
        }

    }

    public void sendSession(Long sessionId, SendDataCallback callback, Context context) {

        new SendSessionAsyncTask(sessionId, callback, context).execute();
    }

}
