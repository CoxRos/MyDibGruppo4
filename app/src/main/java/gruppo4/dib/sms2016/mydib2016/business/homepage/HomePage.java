package gruppo4.dib.sms2016.mydib2016.business.homepage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import gruppo4.dib.sms2016.mydib2016.DataAccessObject.DAOLibretto;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.Autenticazione.Login;
import gruppo4.dib.sms2016.mydib2016.business.logged.avvisi.Avvisi;

import gruppo4.dib.sms2016.mydib2016.business.logged.libretto.Libretto;
import gruppo4.dib.sms2016.mydib2016.business.logged.libretto.EsameActivity;
import gruppo4.dib.sms2016.mydib2016.business.logged.profilo.dati_personali.DatiPersonali;
import gruppo4.dib.sms2016.mydib2016.business.logged.ricerca.RicercaUtente;
import gruppo4.dib.sms2016.mydib2016.business.logged.sharing.Sharing;
import gruppo4.dib.sms2016.mydib2016.business.logged.ultimi_dettagli.UltimiEventi;
import gruppo4.dib.sms2016.mydib2016.business.not_logged.InformazioniUni;
import gruppo4.dib.sms2016.mydib2016.business.not_logged.ristoro.Ristoro;
import gruppo4.dib.sms2016.mydib2016.business.not_logged.bus.Bus;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;
    FloatingActionButton fab;

    boolean isInHome = true;

    DAOLibretto db;
    Login credenziali = new Login();
    static boolean logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_logged);

        db = new DAOLibretto(this);

        if (savedInstanceState == null) {
            credenziali.getLogged(this);
            logged = credenziali.logged;

            //Setto il fragment iniziale
            //0 proviene da skip, 1 da loggato,2 da add esame e vuole andare a libretto.
            Intent intent = getIntent();
            int fromLogin = intent.getIntExtra("goTo", -1);

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            //Fab per l'aggiunta di esami in libretto
            fab = (FloatingActionButton) findViewById(R.id.fabAddEsame);
            fab.setVisibility(View.GONE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), EsameActivity.class);
                    intent.putExtra("option", "add");
                    startActivity(intent);
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

        /*
         * Verifico se ha fatto la login oppure ha saltato la login
         */

            if (fromLogin == 0) { //Skip
                setTitle(R.string.title_fragment_informazioni);
                fab.setVisibility(View.GONE);
                InformazioniUni fragment = new InformazioniUni();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();

                if (fragment != null && fragment.isVisible()) {
                    isInHome = true;
                }

                navigationView.getMenu().setGroupVisible(R.id.notlogged, true);
                navigationView.getMenu().setGroupVisible(R.id.logged1, false);
                navigationView.getMenu().setGroupVisible(R.id.logged2, false);
                navigationView.getMenu().setGroupVisible(R.id.logged3, false);

            } else if (fromLogin == 1) { //Login QUI DEVO METTERE LE ULTIME NEWS
                setTitle(R.string.title_activity_homepage);
                fab.setVisibility(View.GONE);
                UltimiEventi fragment = new UltimiEventi();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, "HOMELOGGED");
                fragmentTransaction.commit();

                if (fragment != null && fragment.isVisible()) {
                    isInHome = true;
                }

                navigationView.getMenu().setGroupVisible(R.id.logged1, true);
                navigationView.getMenu().setGroupVisible(R.id.logged2, true);
                navigationView.getMenu().setGroupVisible(R.id.logged3, true);
                navigationView.getMenu().setGroupVisible(R.id.notlogged, false);

            } else if (fromLogin == 2) { //vai a libretto

                fab.setVisibility(View.VISIBLE);
                Libretto fragment = new Libretto();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();

                isInHome = false;

                navigationView.getMenu().setGroupVisible(R.id.logged1, false);
                navigationView.getMenu().setGroupVisible(R.id.logged2, false);
                navigationView.getMenu().setGroupVisible(R.id.logged3, false);
                navigationView.getMenu().setGroupVisible(R.id.librettoDR, true);
                navigationView.getMenu().setGroupVisible(R.id.notlogged, false);

            } else if (fromLogin == 3) { //Da profilo voglio tornare a ricerca
                setTitle(R.string.title_fragment_ricerca);
                fab.setVisibility(View.GONE);
                RicercaUtente fragment = new RicercaUtente();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();

                navigationView.getMenu().setGroupVisible(R.id.notlogged, false);

                isInHome = false;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isInHome) {
                super.onBackPressed();
            } else {
                if (logged) {
                    fab.setVisibility(View.GONE);
                    UltimiEventi fragment = new UltimiEventi();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.commit();

                    isInHome = true;

                    navigationView.getMenu().setGroupVisible(R.id.logged1, true);
                    navigationView.getMenu().setGroupVisible(R.id.logged2, true);
                    navigationView.getMenu().setGroupVisible(R.id.logged3, true);
                    navigationView.getMenu().setGroupVisible(R.id.notlogged, false);
                    navigationView.getMenu().setGroupVisible(R.id.librettoDR, false);
                } else {
                    fab.setVisibility(View.GONE);
                    InformazioniUni fragment = new InformazioniUni();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.commit();

                    isInHome = true;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.action_faq:
                break;
            case R.id.action_accesso:
                goToLogin();
                break;
            case R.id.action_logout:
                getAlertLogout().show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.informazioniNL) {
            setTitle(R.string.title_fragment_informazioni);
            fab.setVisibility(View.GONE);
            InformazioniUni fragment = new InformazioniUni();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            isInHome = true;

        } else if (id == R.id.ristoroNL) {

            fab.setVisibility(View.GONE);
            Ristoro fragment = new Ristoro();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            isInHome = false;

        } else if (id == R.id.busNL) {
            fab.setVisibility(View.GONE);
            Intent intent = new Intent(this, Bus.class);
            startActivity(intent);

            isInHome = false;
        } else if (id == R.id.homepageL) {
            setTitle(R.string.title_activity_homepage);
            fab.setVisibility(View.GONE);
            UltimiEventi fragment = new UltimiEventi();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            isInHome = true;

            navigationView.getMenu().setGroupVisible(R.id.logged1, true);
            navigationView.getMenu().setGroupVisible(R.id.logged2, true);
            navigationView.getMenu().setGroupVisible(R.id.logged3, true);
            navigationView.getMenu().setGroupVisible(R.id.notlogged, false);
            navigationView.getMenu().setGroupVisible(R.id.librettoDR, false);

        } else if (id == R.id.librettoL) {
            setTitle(R.string.title_fragment_libretto);
            Libretto fragment = new Libretto();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            isInHome = false;

            navigationView.getMenu().setGroupVisible(R.id.logged1, false);
            navigationView.getMenu().setGroupVisible(R.id.logged2, false);
            navigationView.getMenu().setGroupVisible(R.id.logged3, false);
            navigationView.getMenu().setGroupVisible(R.id.librettoDR, true);
            fab.setVisibility(View.VISIBLE);

        } else if (id == R.id.profiloL) {
            setTitle(R.string.title_activity_profilo);
            fab.setVisibility(View.GONE);
            DatiPersonali fragment = new DatiPersonali();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            isInHome = false;

        } else if (id == R.id.informazioniL) {
            setTitle(R.string.title_fragment_informazioni);
            fab.setVisibility(View.GONE);
            InformazioniUni fragment = new InformazioniUni();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            isInHome = false;

        } else if (id == R.id.ricercaL) {
            setTitle(R.string.title_fragment_ricerca);
            fab.setVisibility(View.GONE);
            RicercaUtente fragment = new RicercaUtente();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            isInHome = false;

        } else if (id == R.id.ristoroL) {
            fab.setVisibility(View.GONE);
            setTitle(R.string.title_fragment_ristoro);
            Ristoro fragment = new Ristoro();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            isInHome = false;

        } else if (id == R.id.busL) {
            fab.setVisibility(View.GONE);
            Intent intent = new Intent(this, Bus.class);
            startActivity(intent);

            isInHome = false;
        } else if (id == R.id.homepageLi) { //------------------------------------
            setTitle(R.string.title_activity_homepage);
            fab.setVisibility(View.GONE);
            UltimiEventi fragment = new UltimiEventi();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            isInHome = true;

            navigationView.getMenu().setGroupVisible(R.id.logged1, true);
            navigationView.getMenu().setGroupVisible(R.id.logged2, true);
            navigationView.getMenu().setGroupVisible(R.id.logged3, true);
            navigationView.getMenu().setGroupVisible(R.id.notlogged, false);
            navigationView.getMenu().setGroupVisible(R.id.librettoDR, false);

        } else if (id == R.id.librettoLi) {
            setTitle(R.string.title_fragment_libretto);
            Libretto fragment = new Libretto();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            isInHome = false;

            navigationView.getMenu().setGroupVisible(R.id.logged1, false);
            navigationView.getMenu().setGroupVisible(R.id.logged2, false);
            navigationView.getMenu().setGroupVisible(R.id.logged3, false);
            navigationView.getMenu().setGroupVisible(R.id.librettoDR, true);
            fab.setVisibility(View.VISIBLE);

        } else if (id == R.id.graficiLi) {
            setTitle(R.string.title_fragment_grafici);
            fab.setVisibility(View.GONE);
            isInHome = false;

        } else if (id == R.id.previsioniLi) {
            setTitle(R.string.title_fragment_previsioni);
            fab.setVisibility(View.GONE);
            isInHome = false;

        } else if (id == R.id.avvisiL) {
            setTitle(R.string.title_activity_avvisi);
            fab.setVisibility(View.GONE);
            Avvisi fragment = new Avvisi();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            isInHome = false;

        } else if (id == R.id.sharingL) {
            fab.setVisibility(View.GONE);
            Intent intent = new Intent(this, Sharing.class);
            startActivity(intent);

            isInHome = false;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (logged) {
            menu.findItem(R.id.action_accesso).setVisible(false);
        } else {
            menu.findItem(R.id.action_logout).setVisible(false);
        }
        return true;
    }

    private AlertDialog getAlertLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Sei sicuro di volerti disconnettere?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                credenziali.removeCredential(getApplicationContext());
                db.deleteAllExams();
                goToLogin();
            }
        });
        builder.setNegativeButton("Annulla", null);

        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    private void goToLogin() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }


}
