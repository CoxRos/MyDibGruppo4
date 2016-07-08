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
                startActivity(intent);
            }
        });

        FAQAdapter faqAdapter= new FAQAdapter(this,R.layout.list_layout_faq);
        listFAQ.setAdapter(faqAdapter);

        faqAdapter.add(new FAQEntity("Impossibile loggarsi","Verificare la connessione ed inserire le credenziali di Esse3. Se il problema persiste contattare il numero verde: 0805405631"));
        faqAdapter.add(new FAQEntity("Cancellato per sbaglio i dati dell'applicazione","Se i dati inseriti precedentemente alla eliminazione sono salvati sul server, l'applicazione recupererà tutti i dati."));
        faqAdapter.add(new FAQEntity("La base di laurea è effettiva?","Alla base di laurea, sulla base della politica uniba, sono da aggiungere 2 punti bonus se lo studente è in corso"));
        faqAdapter.add(new FAQEntity("Impossibile caricare i luoghi di ristoro","Verificare che i Google Play Services siano correttamente installati e aggiornati"));
        faqAdapter.add(new FAQEntity("Non riesco a ricercare un utente","Se la ricerca dell'utente fallisce l'utente non si è registrato alla piattaforma o non ha concesso l'autorizzazione ai propri dati"));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FAQ.this, HomePage.class);
        if(logged== true) {
            intent.putExtra("goTo",1);
        } else { //Non è loggato
            intent.putExtra("goTo",0);
        }
        startActivity(intent);
    }
}
