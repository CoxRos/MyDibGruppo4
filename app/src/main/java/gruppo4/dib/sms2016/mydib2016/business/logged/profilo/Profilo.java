package gruppo4.dib.sms2016.mydib2016.business.logged.profilo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.homepage.HomePage;
import gruppo4.dib.sms2016.mydib2016.entity.UtenteEntity;

public class Profilo extends AppCompatActivity {

    TextView txtNomeProfilo, txtCognomeProfilo, txtEmailProfilo, txtTipo,
            txtTelProfilo, txtRicevimentoProfilo, txtWebProfilo, labelWeb, labelRicevimento, labelTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profilo.this, HomePage.class);
                intent.putExtra("goTo", 3);
                startActivity(intent);
            }
        });

        //prendo l'oggetto Utente con tutti i dati che mi vengono passati
        Bundle data = getIntent().getExtras();
        UtenteEntity utente = (UtenteEntity) data.getParcelable("utente");


        txtCognomeProfilo = (TextView) findViewById(R.id.txtCognomeProfilo);
        txtEmailProfilo = (TextView) findViewById(R.id.txtEmailProfilo);
        txtNomeProfilo = (TextView) findViewById(R.id.txtNomeProfilo);
        txtRicevimentoProfilo = (TextView) findViewById(R.id.txtRicevimentoProfilo);
        txtTelProfilo = (TextView) findViewById(R.id.txtTelProfilo);
        txtWebProfilo = (TextView) findViewById(R.id.txtWebProfilo);
        labelRicevimento = (TextView) findViewById(R.id.textView23);
        labelWeb = (TextView) findViewById(R.id.textView24);
        labelTel = (TextView) findViewById(R.id.labelTel);
        txtTipo = (TextView) findViewById(R.id.txtRuoloProfilo);

        txtEmailProfilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{txtEmailProfilo.getText().toString()});
                startActivity(Intent.createChooser(emailIntent, "Invio email al docente."));
            }
        });

        txtWebProfilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse(txtWebProfilo.getText().toString());
                Intent i = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(i);
            }
        });

        txtNomeProfilo.setText(utente.getNome());
        txtCognomeProfilo.setText(utente.getCognome());
        txtEmailProfilo.setText(utente.getEmail());
        txtTipo.setText(utente.getTipo());


        if (!utente.getTipo().equalsIgnoreCase("Docente")) {
            txtWebProfilo.setVisibility(View.GONE);
            txtTelProfilo.setVisibility(View.GONE);
            txtRicevimentoProfilo.setVisibility(View.GONE);
            labelTel.setVisibility(View.GONE);
            labelRicevimento.setVisibility(View.GONE);
            labelWeb.setVisibility(View.GONE);
        } else {
            txtWebProfilo.setVisibility(View.VISIBLE);
            txtTelProfilo.setVisibility(View.VISIBLE);
            txtRicevimentoProfilo.setVisibility(View.VISIBLE);
            labelTel.setVisibility(View.VISIBLE);
            labelRicevimento.setVisibility(View.VISIBLE);
            labelWeb.setVisibility(View.VISIBLE);
            txtWebProfilo.setText(utente.getWeb());
            txtRicevimentoProfilo.setText(utente.getRicevimento());
            txtTelProfilo.setText(utente.getTelefono());
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profilo.this, HomePage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("goTo", 3);
        startActivity(intent);
    }
}
