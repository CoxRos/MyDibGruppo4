package gruppo4.dib.sms2016.mydib2016;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import gruppo4.dib.sms2016.mydib2016.homepage.NotLogged;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = new Intent(this, NotLogged.class);
        startActivity(intent);
    }
}
