package gruppo4.dib.sms2016.mydib2016;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import gruppo4.dib.sms2016.mydib2016.homepage.NotLogged;

public class Login extends AppCompatActivity {

    private Button login;
    private EditText et_email, et_password;
    private CheckBox credenziali;

    public static SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("credenziali", MODE_PRIVATE);
        editor = preferences.edit();

        login = (Button) findViewById(R.id.button_login);
        et_email = (EditText) findViewById(R.id.et_email_login);
        et_password = (EditText) findViewById(R.id.et_password_login);
        credenziali = (CheckBox) findViewById(R.id.check_memorizza_login);

        String savedEmail = preferences.getString("email", "");
        String savedPassword = preferences.getString("password", "");

        if(!savedEmail.equals("") && !savedPassword.equals("")) {
            et_email.setText(savedEmail);
            et_password.setText(savedPassword);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //valido i campi
                if("".equals(et_email.getText().toString())) {
                    et_email.setError("Inserisci l'email accademica");
                    return;
                }
                if("".equals(et_password.getText().toString())) {
                    et_password.setError("Inserisci la password");
                    return;
                }

                //va messo se l'utente riesce a fare la login
                Log.d(getClass().getName().toString(), "dati inseriti: " + et_email.getText().toString() + " & " + et_password.getText().toString());
                if(credenziali.isChecked()) {
                    editor.putString("email", et_email.getText().toString());
                    editor.putString("password", et_password.getText().toString());
                    editor.commit();
                }
                //intent all'HomePage Loggato
            }
        });
    }

    public void skipAuthentication(View view) {
        Intent intent = new Intent(getApplicationContext(), NotLogged.class);
        startActivity(intent);
    }
}
