package gruppo4.dib.sms2016.mydib2016.business.logged.libretto;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import gruppo4.dib.sms2016.mydib2016.DataAccessObject.DAOLibretto;
import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.Autenticazione.Login;
import gruppo4.dib.sms2016.mydib2016.business.homepage.HomePage;
import gruppo4.dib.sms2016.mydib2016.business.system.FAQ;
import gruppo4.dib.sms2016.mydib2016.business.system.UserSetting;
import gruppo4.dib.sms2016.mydib2016.entity.EsameEntity;

public class Grafici extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    Login credenziali = new Login();
    static boolean logged;
    private DAOLibretto db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafici);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarGrafici);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Grafici.this, HomePage.class);
                intent.putExtra("goTo",2);
                startActivity(intent);
            }
        });

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.containerGrafici);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsGrafici);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Grafici.this, HomePage.class);
        intent.putExtra("goTo",2);
        startActivity(intent);
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
        Intent intent;

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_logout:
                getAlertLogout().show();
                break;
            case R.id.action_settings:
                intent = new Intent(this, UserSetting.class);
                startActivity(intent);
                break;
            case R.id.action_faq:
                intent = new Intent(this, FAQ.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.action_home).setVisible(false);
        menu.findItem(R.id.action_accesso).setVisible(false);

        return true;
    }

    private AlertDialog getAlertLogout() {
        db = new DAOLibretto(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Sei sicuro di volerti disconnettere?");
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        TextView noEsamiChart;
        ImageView imageNoEsamiChart;

        private DAOLibretto db;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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
            View rootView = inflater.inflate(R.layout.fragment_grafici, container, false);

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            noEsamiChart = (TextView) getActivity().findViewById(R.id.noEsamiChart);
            imageNoEsamiChart = (ImageView) getActivity().findViewById(R.id.imageNoEsamiChart);
            db = new DAOLibretto(getContext());

            setUI();
        }

        private void setUI() {

            noEsamiChart.setVisibility(View.GONE);
            imageNoEsamiChart.setVisibility(View.GONE);
            LineChart lineChart = (LineChart) getActivity().findViewById(R.id.chart);
            lineChart.setVisibility(View.VISIBLE);
            lineChart.setDescription("Andamento esami");

            List<EsameEntity> esami = db.getEsami();
            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<String>();

            if(esami.size() > 0) {
                int i = 0;
                for (EsameEntity esame : esami) {
                    if(!esame.getVoto().equals("IDO")) {
                        entries.add(new Entry(Float.parseFloat(esame.getVoto()), i++));
                        labels.add("");
                    }
                }

                LineDataSet dataset = new LineDataSet(entries, "");

                LineData data = new LineData(labels, dataset);
                dataset.setColors(ColorTemplate.JOYFUL_COLORS);
                dataset.setDrawCubic(true);
                dataset.setDrawFilled(true);

                lineChart.setData(data);
                lineChart.animateY(6000);
            } else {
                noEsamiChart.setVisibility(View.VISIBLE);
                imageNoEsamiChart.setVisibility(View.VISIBLE);
                lineChart.setVisibility(View.GONE);
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
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
                    return GraficoLineChart.newInstance();
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
                    return "PROGRESSO";
                case 1:
                    return "TORTA";
            }
            return null;
        }
    }
}