package gruppo4.dib.sms2016.mydib2016.business.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.logged.Libretto;
import gruppo4.dib.sms2016.mydib2016.business.logged.libretto.EsameActivity;
import gruppo4.dib.sms2016.mydib2016.business.not_logged.InformazioniUni;
import gruppo4.dib.sms2016.mydib2016.business.not_logged.Ristoro;
import gruppo4.dib.sms2016.mydib2016.business.not_logged.bus.Bus;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_logged);

        //Setto il fragment iniziale
        //0 proviene da skip, 1 da loggato,2 da add esame e vuole andare a libretto.
        Intent intent = getIntent();
        int fromLogin = intent.getIntExtra("goTo",0);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Fab per l'aggiunta di esami in libretto
        fab = (FloatingActionButton) findViewById(R.id.fabAddEsame);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EsameActivity.class);
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
        if(fromLogin == 0) { //Skip
            InformazioniUni fragment = new InformazioniUni();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            navigationView.getMenu().setGroupVisible(R.id.notlogged,true);
            navigationView.getMenu().setGroupVisible(R.id.logged1,false);
            navigationView.getMenu().setGroupVisible(R.id.logged2,false);
            navigationView.getMenu().setGroupVisible(R.id.logged3,false);
            fab.setVisibility(View.GONE);

        } else if(fromLogin == 1)  { //Login QUI DEVO METTERE LE ULTIME NEWS
            Ristoro fragment = new Ristoro();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            navigationView.getMenu().setGroupVisible(R.id.logged1,true);
            navigationView.getMenu().setGroupVisible(R.id.logged2,true);
            navigationView.getMenu().setGroupVisible(R.id.logged3,true);
            navigationView.getMenu().setGroupVisible(R.id.notlogged,false);
            fab.setVisibility(View.GONE);
        } else if(fromLogin == 2) {
            Libretto fragment = new Libretto();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            navigationView.getMenu().setGroupVisible(R.id.logged1,false);
            navigationView.getMenu().setGroupVisible(R.id.logged2,false);
            navigationView.getMenu().setGroupVisible(R.id.logged3,false);
            navigationView.getMenu().setGroupVisible(R.id.librettoDR,true);
            fab.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.informazioniNL) {

            fab.setVisibility(View.GONE);
            InformazioniUni fragment = new InformazioniUni();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.ristoroNL) {
            fab.setVisibility(View.GONE);
            Ristoro fragment = new Ristoro();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.busNL) {
            fab.setVisibility(View.GONE);
            Intent intent = new Intent(this, Bus.class);
            startActivity(intent);
        } else if (id == R.id.homepageL) { //Da fare
            fab.setVisibility(View.GONE);

        } else if (id == R.id.librettoL) { //Da fare

            Libretto fragment = new Libretto();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            navigationView.getMenu().setGroupVisible(R.id.logged1,false);
            navigationView.getMenu().setGroupVisible(R.id.logged2,false);
            navigationView.getMenu().setGroupVisible(R.id.logged3,false);
            navigationView.getMenu().setGroupVisible(R.id.librettoDR,true);
            fab.setVisibility(View.VISIBLE);


        } else if (id == R.id.profiloL) { //Da fare
            fab.setVisibility(View.GONE);

        } else if (id == R.id.informazioniL) {
            fab.setVisibility(View.GONE);
            InformazioniUni fragment = new InformazioniUni();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.ricercaL) { //Da fare
            fab.setVisibility(View.GONE);

        } else if (id == R.id.ristoroL) {

            fab.setVisibility(View.GONE);
            Ristoro fragment = new Ristoro();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.busL) {
            fab.setVisibility(View.GONE);
            Intent intent = new Intent(this, Bus.class);
            startActivity(intent);
        } else if (id == R.id.homepageLi) { //Da fare
            fab.setVisibility(View.GONE);

        } else if (id == R.id.librettoLi) {
            Libretto fragment = new Libretto();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            navigationView.getMenu().setGroupVisible(R.id.logged1,false);
            navigationView.getMenu().setGroupVisible(R.id.logged2,false);
            navigationView.getMenu().setGroupVisible(R.id.logged3,false);
            navigationView.getMenu().setGroupVisible(R.id.librettoDR,true);
            fab.setVisibility(View.VISIBLE);

        } else if (id == R.id.graficiLi) {
            fab.setVisibility(View.GONE);

        } else if (id == R.id.previsioniLi) {
            fab.setVisibility(View.GONE);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
