package gruppo4.dib.sms2016.mydib2016.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gruppo4.dib.sms2016.mydib2016.DataAccessObject.DAOLibretto;
import gruppo4.dib.sms2016.mydib2016.business.Autenticazione.Login;
import gruppo4.dib.sms2016.mydib2016.entity.EsameEntity;
import gruppo4.dib.sms2016.mydib2016.network.CustomRequestObject;
import gruppo4.dib.sms2016.mydib2016.network.Network;
import gruppo4.dib.sms2016.mydib2016.utility.Costants;
import gruppo4.dib.sms2016.mydib2016.utility.Utils;

public class RSSPullService extends Service {

    private boolean isRunning = false;
    private static final String TAG = "Service Database";

    private DAOLibretto db;
    List<EsameEntity> esami;
    Utils utility;
    List<String> queries;

    RequestQueue queue;
    final String URLREQUEST = Costants.URL_UPLOAD_DB;

    Login credenziali = new Login();
    static String matricola;


    @Override
    public void onCreate() {

        isRunning = true;

        db = new DAOLibretto(getApplicationContext());
        esami = db.getEsami();
        utility = new Utils();
        queries = utility.getQueries(esami, getApplicationContext());

        credenziali.getMatricola(getApplicationContext());
        matricola = credenziali.matricola;

        queue = Network.getInstance(getApplicationContext()).getRequestQueue();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning) {
                    try {
                        requestDB(URLREQUEST);
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        Log.d("ECCEZIONE SERVICE-DB", "Eccezione nel thread: " + e.getMessage());
                    }

                    if (isRunning) {
                        Log.i(TAG, "Service running");
                    }
                }
            }
        }).start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    private void requestDB(String url) {
        CustomRequestObject jsonRequest = new CustomRequestObject(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                boolean status;
                String message;
                try {
                    status = jsonObject.getBoolean("status");
                    message = jsonObject.getString("message");
                    if (status && message.equalsIgnoreCase("OKDB")) {

                        stopSelf();

                    }
                } catch (Exception e) {
                    Log.d("ECCEZIONE SERVIZIO-DB: ", e.getMessage().toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("ERRORE SERVICE-DB", "servizio fallito");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("matricola",matricola);
                params.put("querylength", Integer.toString(esami.size()));
                int i = 0;
                for (String query : queries) {
                    params.put("query" + i++, query);
                }
                return params;
            }
        };
        Network.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
    }

}