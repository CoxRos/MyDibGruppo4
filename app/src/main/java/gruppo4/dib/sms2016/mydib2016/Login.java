package gruppo4.dib.sms2016.mydib2016;

import android.app.ProgressDialog;
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

import gruppo4.dib.sms2016.mydib2016.homepage.not_logged.NotLogged;
import gruppo4.dib.sms2016.mydib2016.network.CustomRequestObject;
import gruppo4.dib.sms2016.mydib2016.network.Network;

public class Login extends AppCompatActivity {

    private Button login;
    private EditText et_email, et_password;
    private CheckBox credenziali;
    private ImageView imageView;

    private ProgressDialog progressDialog;
    private RequestQueue queue;

    private static final Pattern EMAIL_ADDRESS = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@studenti.uniba.it");

    public static SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        queue = Network.getInstance(getApplicationContext()).getRequestQueue();

        preferences = getSharedPreferences("credenziali", MODE_PRIVATE);
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

        if(!savedEmail.equals("") && !savedPassword.equals("")) {
            et_email.setText(savedEmail);
            et_password.setText(savedPassword);
        }

        final String email = et_email.getText().toString();
        final String password = et_password.getText().toString();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //valido i campi
                if("".equals(email)) {
                    et_email.setError("Inserisci l'email accademica");
                    return;
                }
                if(!isValidEmail(email)) {
                    et_email.setError("Inserisci l'email istituzionale");
                    return;
                }
                if("".equals(password)) {
                    et_password.setError("Inserisci la password");
                    return;
                }

                doRequest("http://mydib2016.altervista.org/api/index.php/login", email, password);
            }
        });
    }

    public void skipAuthentication(View view) {
        Intent intent = new Intent(getApplicationContext(), NotLogged.class);
        intent.putExtra("login", "skip");
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
                        Toast.makeText(getApplicationContext(), "Login effettuato con successo!", Toast.LENGTH_SHORT).show();
                        saveCredential(email, password);
                        changeActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), "Controlla le credenziali e riprova!", Toast.LENGTH_LONG).show();
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
                Log.d("CLASSE LOGIN", "Errore connessione " + error.toString());
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
        progressDialog.setTitle("Attedere");
        progressDialog.setMessage("Verifica dei dati");
        progressDialog.show();
    }

    private void saveCredential(String email, String password) {
        if(credenziali.isChecked()) {
            editor.putString("email", email);
            editor.putString("password", password);
            editor.commit();
        }
    }

    private void changeActivity() {
        //Intent intent = new Intent(getApplicationContext(), ciccio.class);
        //intent.putExtra("login", "login");
        //startActivity(intent);
    }
}