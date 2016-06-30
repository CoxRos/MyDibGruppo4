package gruppo4.dib.sms2016.mydib2016;

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

import java.util.regex.Pattern;

import gruppo4.dib.sms2016.mydib2016.homepage.NotLogged;

public class Login extends AppCompatActivity {

    private Button login;
    private EditText et_email, et_password;
    private CheckBox credenziali;
    private ImageView imageView;

    private static final Pattern EMAIL_ADDRESS = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@studenti.uniba.it");

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

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //valido i campi
                if("".equals(et_email.getText().toString())) {
                    et_email.setError("Inserisci l'email accademica");
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

                //va messo se l'utente riesce a fare la login
                Log.d(getClass().getName().toString(), "dati inseriti: " + et_email.getText().toString() + " & " + et_password.getText().toString());
                if(credenziali.isChecked()) {
                    editor.putString("email", et_email.getText().toString());
                    editor.putString("password", et_password.getText().toString());
                    editor.commit();
                }
                //chiamo il server sia intent che preferences
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
}
