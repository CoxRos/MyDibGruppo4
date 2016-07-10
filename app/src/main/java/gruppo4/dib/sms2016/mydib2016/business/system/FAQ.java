package gruppo4.dib.sms2016.mydib2016.business.system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.Autenticazione.Login;
import gruppo4.dib.sms2016.mydib2016.business.homepage.HomePage;
import gruppo4.dib.sms2016.mydib2016.business.logged.ricerca.RicercaAdapter;
import gruppo4.dib.sms2016.mydib2016.entity.FAQEntity;

public class FAQ extends AppCompatActivity {

    ListView listFAQ;

    Login credenziali = new Login();
    static boolean logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        listFAQ = (ListView) findViewById(R.id.listFAQ);

        credenziali.getLogged(this);
        logged = credenziali.logged;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FAQ.this, HomePage.class);
                if(logged== true) {
                    intent.putExtra("goTo",1);
                } else { //Non è loggato
                    intent.putExtra("goTo",0);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        FAQAdapter faqAdapter= new FAQAdapter(this,R.layout.list_layout_faq);
        listFAQ.setAdapter(faqAdapter);


        faqAdapter.add(new FAQEntity(getResources().getString(R.string.faq_log_titolo), getResources().getString(R.string.faq_log)));
        faqAdapter.add(new FAQEntity(getResources().getString(R.string.faq_dati_titolo),getResources().getString(R.string.faq_dati)));
        faqAdapter.add(new FAQEntity(getResources().getString(R.string.faq_laurea_titolo),getResources().getString(R.string.faq_laurea)));
        faqAdapter.add(new FAQEntity(getResources().getString(R.string.faq_ristoro_titolo),getResources().getString(R.string.faq_ristoro)));
        faqAdapter.add(new FAQEntity(getResources().getString(R.string.faq_ricerca_titolo),getResources().getString(R.string.faq_ricerca)));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FAQ.this, HomePage.class);
        if(logged== true) {
            intent.putExtra("goTo",1);
        } else { //Non è loggato
            intent.putExtra("goTo",0);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
