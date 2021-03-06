package gruppo4.dib.sms2016.mydib2016.business.not_logged.bus;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import gruppo4.dib.sms2016.mydib2016.DataAccessObject.DAOLibretto;
import gruppo4.dib.sms2016.mydib2016.business.Autenticazione.Login;
import gruppo4.dib.sms2016.mydib2016.business.system.FAQ;
import gruppo4.dib.sms2016.mydib2016.business.system.UserSetting;
import gruppo4.dib.sms2016.mydib2016.network.Network;
import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.entity.BusEntity;
import gruppo4.dib.sms2016.mydib2016.business.homepage.HomePage;
import gruppo4.dib.sms2016.mydib2016.utility.Costants;

public class Bus extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;

    RequestQueue queue;
    ProgressDialog progressDialog;

    Login credenziali = new Login();
    static boolean logged;
    DAOLibretto db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);

        credenziali.getLogged(this);
        logged = credenziali.logged;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Bus.this, HomePage.class);
                if(logged== true) {
                    intent.putExtra("goTo",1);
                } else { //Non è loggato
                    intent.putExtra("goTo",0);
                }
                startActivity(intent);
            }
        });

        queue = Network.getInstance(getApplicationContext()).
                getRequestQueue();

        progressDialog = new ProgressDialog(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Bus.this, HomePage.class);
        if(logged== true) {
            intent.putExtra("goTo",1);
        } else { //Non è loggato
            intent.putExtra("goTo",0);
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.action_logout:
                getAlertLogout().show();
                break;
            case R.id.action_faq:
                intent = new Intent(this, FAQ.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                intent = new Intent(this, UserSetting.class);
                startActivity(intent);
                break;
            case R.id.action_accesso:
                intent = new Intent(this, Login.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.action_accesso).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(false);

        if(!logged) {
            menu.findItem(R.id.action_accesso).setVisible(true);
            menu.findItem(R.id.action_logout).setVisible(false);
        }
        return true;
    }

    private AlertDialog getAlertLogout() {
        db = new DAOLibretto(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.logout));
        builder.setMessage(getResources().getString(R.string.disconnessione));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                credenziali.removeCredential(getApplicationContext());
                db.deleteAllExams();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Annulla", null);

        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }


    public static class PlaceholderFragment extends Fragment {


        private static final String ARG_SECTION_NUMBER = "section_number";

        RequestQueue queue;
        ProgressDialog progressDialog;

        ListView listaAutobus;
        TextView noItem;
        ImageView noConnection;

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_bus, container, false);

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            noItem = (TextView) getActivity().findViewById(R.id.verificaConnAndata);
            listaAutobus = (ListView) getActivity().findViewById(R.id.listAutobusAndata);
            noConnection = (ImageView) getActivity().findViewById(R.id.no_wifiAndata);

            queue = Network.getInstance(getActivity()).
                    getRequestQueue();

            progressDialog = new ProgressDialog(getActivity());

            setUI(Costants.URL_BUS);
        }

        private void setUI(String url) {

            final BusTimeAdapter listAdapter = new BusTimeAdapter(getActivity(),R.layout.layout_list_bus);
            listaAutobus.setAdapter(listAdapter);

            JsonArrayRequest request = new JsonArrayRequest
                    (url, new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            boolean isEmpty = true;
                            String numero, partenza, orarioPartenza, orarioArrivo;
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject oggettoJson = response.getJSONObject(i);
                                    if(oggettoJson.getString("partenza").equalsIgnoreCase("STAZIONE")) {
                                        numero = oggettoJson.getString("numbus");
                                        orarioPartenza = oggettoJson.getString("orarioPartenza");
                                        orarioArrivo = oggettoJson.getString("orarioArrivo");
                                        partenza = oggettoJson.getString("partenza");
                                        isEmpty = false;
                                        listAdapter.add(new BusEntity(numero, orarioPartenza, orarioArrivo, partenza));
                                    }
                                } catch (Exception e) {
                                    System.out.println("catch: " + e);
                                }
                            }
                            if(isEmpty) {
                                listaAutobus.setVisibility(View.GONE);
                                noItem.setVisibility(View.VISIBLE);
                                noItem.setText(getResources().getString(R.string.no_orari));
                                noConnection.setVisibility(View.VISIBLE);
                                noConnection.setImageResource(R.mipmap.ic_dispiaciuto);
                            } else {
                                listAdapter.add(new BusEntity("","","",""));
                                listaAutobus.setVisibility(View.VISIBLE);
                                noItem.setVisibility(View.GONE);
                                noConnection.setVisibility(View.GONE);
                            }
                            progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            listaAutobus.setVisibility(View.GONE);
                            noItem.setVisibility(View.VISIBLE);
                            noConnection.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        }
                    });
            Network.getInstance(getActivity()).addToRequestQueue(request);
            progressDialog.setTitle(getResources().getString(R.string.progress_titolo));
            progressDialog.setMessage(getResources().getString(R.string.progress_message));
            progressDialog.show();
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PlaceholderFragment.newInstance(position + 1);
                case 1:
                    return RitornoBus.newInstance();
            }
            return null;
        }

        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ANDATA";
                case 1:
                    return "RITORNO";
            }
            return null;
        }
    }
}
