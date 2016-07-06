package gruppo4.dib.sms2016.mydib2016.business.logged.libretto;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import gruppo4.dib.sms2016.mydib2016.DataAccessObject.DAOLibretto;
import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.Autenticazione.Login;
import gruppo4.dib.sms2016.mydib2016.business.homepage.HomePage;
import gruppo4.dib.sms2016.mydib2016.entity.EsameEntity;
import gruppo4.dib.sms2016.mydib2016.network.CustomRequestObject;
import gruppo4.dib.sms2016.mydib2016.network.Network;

public class EsameActivity extends AppCompatActivity {

    private EditText edtEsame, edtVoto, edtCfu;
    private EditText edtData;
    private Button btnSalva;
    private TextView txtDescrizione;

    private ProgressDialog progressDialog;
    RequestQueue queue;

    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    Login credenziali = new Login();
    static String matricola;

    private final int DATE_DIALOG_ID = 0;
    private DAOLibretto db;

    private int year;
    private int month;
    private int day;

    private String option;
    private String esame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esame);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = getSharedPreferences("esami", MODE_PRIVATE);
        edit = preferences.edit();

        credenziali.getMatricola(this);
        matricola = credenziali.matricola;

        db = new DAOLibretto(this);

        queue = Network.getInstance(getApplicationContext()).getRequestQueue();

        //prendo la data corrente
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        edtEsame = (EditText) findViewById(R.id.edtEsame);
        edtVoto = (EditText) findViewById(R.id.edtVoto);
        edtCfu = (EditText) findViewById(R.id.edtCFU);
        edtData = (EditText) findViewById(R.id.edtData);
        btnSalva = (Button) findViewById(R.id.buttonAggiungi);
        txtDescrizione = (TextView) findViewById(R.id.descrione_libretto_esame);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EsameActivity.this, HomePage.class);
                intent.putExtra("goTo",2);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        option = intent.getStringExtra("option");
        esame = intent.getStringExtra("esame");

        Log.d("DATI PASSATI", option + " " + esame);

        edtEsame.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //vuoto
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //vuoto
            }
        });

        edtVoto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtCfu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(!option.equals("visualizza")) {
            edtData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(DATE_DIALOG_ID);
                }
            });
        }

        if(option.equals("modifica")) {
            EsameEntity esam = db.getEsame(esame);

            txtDescrizione.setText(getResources().getString(R.string.modificaEsame));

            edtEsame.setText(esam.getNome());
            edtVoto.setText(esam.getVoto());
            edtCfu.setText(esam.getCfu());
            edtData.setText(esam.getData());
        }
        else if(option.equals("visualizza")) {
            EsameEntity esam = db.getEsame(esame);

            btnSalva.setVisibility(View.GONE);
            txtDescrizione.setText(getResources().getString(R.string.visualizzaEsame));

            edtEsame.setText(esam.getNome());
            edtVoto.setText(esam.getVoto());
            edtCfu.setText(esam.getCfu());
            edtData.setText(esam.getData());

            edtEsame.setKeyListener(null);
            edtVoto.setKeyListener(null);
            edtCfu.setKeyListener(null);
        }

        btnSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String materia = edtEsame.getText().toString();
                String cfu = edtCfu.getText().toString();
                String voto = edtVoto.getText().toString();
                String data = edtData.getText().toString();

                if(option.equals("add")) {
                    boolean isInserted = db.insertEsame(materia, cfu, voto, data);

                    if (isInserted) {
                        Toast.makeText(getApplicationContext(), "Dati inseriti con successo", Toast.LENGTH_LONG).show();
                        doRequest("http://mydib2016.altervista.org/api/index.php/insertEsame", "");
                    } else {
                        Toast.makeText(getApplicationContext(), "Non è stato possibile inserire i dati... esame gia presente!", Toast.LENGTH_LONG).show();
                    }
                }
                else if(option.equals("modifica")) {
                    String holdMat = esame;
                    boolean isUpdated = db.updateEsame(materia,cfu, voto, data, holdMat);

                    if(isUpdated) {
                        Toast.makeText(getApplicationContext(), "Dati aggiornati con successo", Toast.LENGTH_LONG).show();
                        doRequest("http://mydib2016.altervista.org/api/index.php/updateEsame", holdMat);
                    } else {
                        Toast.makeText(getApplicationContext(), "Non è stato possibile aggiornare i dati... esame gia presente!", Toast.LENGTH_LONG).show();
                    }
                }
                goToHome();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Rende cliccabile il bottone solo se non stati avvalorati tutti i campi
     */
    private void enableButton() {
        if(edtEsame.getText().length() > 0 && edtVoto.getText().length() > 0
                && edtCfu.getText().length() > 0 && edtData.getText().length() > 0) {
            btnSalva.setEnabled(true);
        }
        else {
            btnSalva.setEnabled(false);
        }
    }

    /**
     * setta la data dopo la scelta nel dialog
     */
    private void updateDisplay() {
        edtData.setText(new StringBuilder()
                // Mese inizia da 0 quindi aggiungo 1
                .append(month + 1).append("-")
                .append(day).append("-")
                .append(year).append(" "));
    }

    /**
     * Setta il listener del dialog del dataPicker
     */
    private DatePickerDialog.OnDateSetListener dataListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int years, int monthOfYear, int dayOfMonth) {
            year = years;
            month = monthOfYear;
            day = dayOfMonth;
            updateDisplay();
        }
    };

    /**
     * Crea il dialog
     * @param id
     *      l'id del dialog
     * @return
     *      il nuovo dialog
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dataListener, year, month, day);
        }
        return null;
    }

    /**
     * chiamare per eliminare aggiungere e modificare i dati anche sul server
     * @param url
     * @param nomeEsame
     */
    private void doRequest(String url, final String nomeEsame) {
        CustomRequestObject jsonRequest = new CustomRequestObject(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                boolean status;
                String message;
                try {
                    status = jsonObject.getBoolean("status");
                    message = jsonObject.getString("message");
                    Log.d("LOG DEL LIBRETTO", "STATO DELLA RICHIESTA " + status + " MESSAGE " + message);
                }
                catch (Exception e) {
                    Log.d("ECCEZIONE LIBRETTO: ", e.getMessage().toString());
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                edit.putInt("EXAMTOSEND", 1);//ESAME DA INVIARE PRESENTE
                edit.commit();
                Log.d("ERRORE LIBRETTO: ", volleyError.getMessage());
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("materia", edtEsame.getText().toString());
                params.put("cfu", edtCfu.getText().toString());
                params.put("voto", edtVoto.getText().toString());
                params.put("data", edtData.getText().toString());
                params.put("holdMat", nomeEsame);
                params.put("matricola",matricola);
                return params;
            }
        };
        Network.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Attendere...");
        progressDialog.setMessage("Aggiunta dei dati");
        progressDialog.show();
    }

    private void goToHome() {
        Intent intent = new Intent(EsameActivity.this, HomePage.class);
        intent.putExtra("goTo",2);
        startActivity(intent);
    }
}
