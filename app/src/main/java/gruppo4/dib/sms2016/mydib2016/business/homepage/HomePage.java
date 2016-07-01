package gruppo4.dib.sms2016.mydib2016.business.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.not_logged.InformazioniUni;
import gruppo4.dib.sms2016.mydib2016.business.not_logged.Ristoro;
import gruppo4.dib.sms2016.mydib2016.business.not_logged.bus.Bus;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_logged);

        //Setto il fragment iniziale
        //0 proviene da skip, 1 da loggato.
        Intent intent = getIntent();
        int fromLogin = intent.getIntExtra("login",0);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        } else { //Login QUI DEVO METTERE LE ULTIME NEWS
            Ristoro fragment = new Ristoro();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

            navigationView.getMenu().setGroupVisible(R.id.logged1,true);
            navigationView.getMenu().setGroupVisible(R.id.logged2,true);
            navigationView.getMenu().setGroupVisible(R.id.logged3,true);
            navigationView.getMenu().setGroupVisible(R.id.notlogged,false);
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
            InformazioniUni fragment = new InformazioniUni();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.ristoroNL) {
            Ristoro fragment = new Ristoro();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.busNL) {
            Intent intent = new Intent(this, Bus.class);
            startActivity(intent);
        } else if (id == R.id.homepageL) { //Da fare
        } else if (id == R.id.librettoL) { //Da fare

        } else if (id == R.id.profiloL) { //Da fare

        } else if (id == R.id.informazioniL) {
            InformazioniUni fragment = new InformazioniUni();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.ricercaL) { //Da fare

        } else if (id == R.id.ristoroL) {
            Ristoro fragment = new Ristoro();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.busL) {
            Intent intent = new Intent(this, Bus.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
