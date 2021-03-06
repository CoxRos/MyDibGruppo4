package gruppo4.dib.sms2016.mydib2016.business.Autenticazione;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.homepage.HomePage;
import gruppo4.dib.sms2016.mydib2016.network.CustomRequestObject;
import gruppo4.dib.sms2016.mydib2016.network.Network;
import gruppo4.dib.sms2016.mydib2016.utility.Costants;

public class Login extends AppCompatActivity {

    private Button login;
    private EditText et_email, et_password;
    private CheckBox credenziali;
    private ImageView imageView;

    private ProgressDialog progressDialog;
    private RequestQueue queue;

    private static final Pattern EMAIL_ADDRESS = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@studenti.uniba.it");

    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    public static boolean logged;
    public static String matricola, email;
    public static boolean entry = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        queue = Network.getInstance(getApplicationContext()).getRequestQueue();

        preferences = getApplicationContext().getSharedPreferences(Costants.PREFERENCES_CREDENZIALI, MODE_PRIVATE);
        editor = preferences.edit();

        login = (Button) findViewById(R.id.button_login);
        et_email = (EditText) findViewById(R.id.et_email_login);
        et_password = (EditText) findViewById(R.id.et_password_login);
        credenziali = (CheckBox) findViewById(R.id.check_memorizza_login);
        imageView = (ImageView) findViewById(R.id.imageLogin);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.uniba_img);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 350, 350, true);
        imageView.setImageBitmap(scaledBitmap);

        String savedEmail = preferences.getString("email", "");
        String savedPassword = preferences.getString("password", "");
        boolean loggato = preferences.getBoolean("loggato", false);

        if(!loggato) {
            if(!savedEmail.equals("") && !savedPassword.equals("")) {
                et_email.setText(savedEmail);
                et_password.setText(savedPassword);
                credenziali.setChecked(true);
            }

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //valido i campi
                    if("".equals(et_email.getText().toString())) {
                        et_email.setError("Inserisci l'email");
                        return;
                    }
                    if(!isValidEmail(et_email.getText().toString())) {
                        et_email.setError("Inserisci l'email istituzionale");
                        return;
                    }
                    if("".equals(et_password.getText().toString())) {
                        et_password.setError("Inserisci la password");
                        return;
                    }

                    doRequest(Costants.URL_LOGIN, et_email.getText().toString(), et_password.getText().toString());
                }
            });
        }
        else {
            entry = true;
            changeActivity();
        }
    }

    public void skipAuthentication(View view) {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        intent.putExtra("goTo", 0);
        startActivity(intent);
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void doRequest(String url, final String email, final String password) {

        CustomRequestObject jsonObjectRequest = new CustomRequestObject(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int result = response.getInt("loggato");
                    if(result == 1) {
                        editor.putString("nome", response.getString("nome"));
                        editor.putString("cognome", response.getString("cognome"));
                        editor.putBoolean("loggato", true);
                        editor.putString("email", email);
                        if(credenziali.isChecked()) {
                            editor.putString("password", password);
                        }
                        editor.putString("matricola",response.getString("matricola"));
                        editor.commit();

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_successo), Toast.LENGTH_SHORT).show();
                        changeActivity();

                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_errato), Toast.LENGTH_LONG).show();
                        et_password.setText("");
                    }
                } catch (JSONException e) {
                    Log.d("ERRORE CATCH RESPONSE", e.getMessage().toString());
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_connessione), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", et_email.getText().toString());
                params.put("password", et_password.getText().toString());
                return params;
            }
        };
        Network.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.progress_titolo));
        progressDialog.setMessage(getResources().getString(R.string.progress_message));
        progressDialog.show();

    }

    private void changeActivity() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        intent.putExtra("goTo", 1);
        startActivity(intent);
    }


    public void getLogged(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Costants.PREFERENCES_CREDENZIALI, MODE_PRIVATE);
        logged = prefs.getBoolean("loggato", false);
    }

    public void getMatricola(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Costants.PREFERENCES_CREDENZIALI, MODE_PRIVATE);
        matricola = prefs.getString("matricola", "");
    }

    public void removeCredential(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Costants.PREFERENCES_CREDENZIALI, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("loggato");
        editor.remove("matricola");
        editor.commit();
    }

    public void getEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Costants.PREFERENCES_CREDENZIALI, MODE_PRIVATE);
        email = prefs.getString("email", "");
    }
}
