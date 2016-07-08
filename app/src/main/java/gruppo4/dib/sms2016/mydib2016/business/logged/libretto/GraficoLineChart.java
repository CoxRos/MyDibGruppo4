package gruppo4.dib.sms2016.mydib2016.business.logged.libretto;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import gruppo4.dib.sms2016.mydib2016.DataAccessObject.DAOLibretto;
import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.entity.EsameEntity;

public class GraficoLineChart extends Fragment {

    RelativeLayout mainLayout;
    PieChart pieChart;

    private DAOLibretto db;

    TextView noEsamiChart;
    ImageView imageNoEsamiChart;

    ArrayList<Couple> voti;

    List<String> xDataPers;
    List<String> yDataPers;

    public static GraficoLineChart newInstance() {
        GraficoLineChart fragment = new GraficoLineChart();
        return fragment;
    }
    public GraficoLineChart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_grafico_line_chart, container, false);

        // Inflate the layout for this fragment
        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        noEsamiChart = (TextView) getActivity().findViewById(R.id.noEsamiChart2);
        imageNoEsamiChart = (ImageView) getActivity().findViewById(R.id.imageNoEsamiChart2);

        db = new DAOLibretto(getContext());

        xDataPers = new ArrayList<String>();
        yDataPers = new ArrayList<String>();

        List<EsameEntity> esami = db.getEsami();
        setPercentual(esami);

        setUI();
    }

    private void setUI() {
        mainLayout = (RelativeLayout) getActivity().findViewById(R.id.mainLayout);
        pieChart = new PieChart(getContext());
        mainLayout.addView(pieChart);
        mainLayout.setBackgroundColor(Color.WHITE);

        final TextView esame = new TextView(getContext());
        mainLayout.addView(esame);
        esame.setVisibility(View.GONE);
        esame.setTextColor(Color.parseColor("#192370"));
        esame.setPadding(20,20,0,0);
        esame.setTextSize(22);

        pieChart.setUsePercentValues(true);
        pieChart.setDescription("Voti");
        pieChart.setValueTextColor(Color.parseColor("#151515"));

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(5);
        pieChart.setTransparentCircleRadius(10);

        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i) {
                esame.setVisibility(View.VISIBLE);
                esame.setText("Voto: " + xDataPers.get(entry.getXIndex()) + "\nRicorrenza: " + yDataPers.get(entry.getXIndex()));
            }

            @Override
            public void onNothingSelected() {
                esame.setVisibility(View.GONE);
            }
        });

        addData();
    }

    private void setPercentual(List<EsameEntity> esami) {
        if(esami.size()>0) {
            noEsamiChart.setVisibility(View.GONE);
            imageNoEsamiChart.setVisibility(View.GONE);
            voti = new ArrayList<Couple>();
            for (EsameEntity esame : esami) {
                int esameVoto = Integer.parseInt(esame.getVoto());
                boolean thereIs = false;
                for (Couple coppie : voti) {
                    if (coppie.getVoto() == esameVoto) {
                        thereIs = true;
                        coppie.setCount(coppie.getCount() + 1);
                        break;
                    }
                }
                if (!thereIs) {
                    voti.add(new Couple(esameVoto, 1));
                }
            }

            for (Couple v : voti) {
                xDataPers.add(Integer.toString(v.getVoto()));
                yDataPers.add(Integer.toString(v.getCount()));
            }
        } else {
            noEsamiChart.setVisibility(View.VISIBLE);
            imageNoEsamiChart.setVisibility(View.VISIBLE);
        }

    }

    private void addData() {

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for(int i = 0; i < yDataPers.size(); i++) {
            yVals.add(new Entry(Float.parseFloat(yDataPers.get(i)),i));
        }
        ArrayList<String> xVals = new ArrayList<String>();
        for(int i= 0; i< xDataPers.size(); i++) {
            xVals.add(xDataPers.get(i));
        }

        //Crea la torta
        PieDataSet dataSet = new PieDataSet(yVals, "Carriera universitaria");
        dataSet.setSliceSpace(0);
        dataSet.setSelectionShift(10);

        //add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c);
        }

        for(int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }

        for(int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }

        for(int c : ColorTemplate.LIBERTY_COLORS) {
            colors.add(c);
        }
        for(int c : ColorTemplate.PASTEL_COLORS) {
            colors.add(c);
        }

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        //Istanzia la torta
        PieData data = new PieData(xVals,dataSet);
        pieChart.setData(data);

        pieChart.highlightValues(null);
        pieChart.invalidate();
    }

    private class Couple {
        int voto;
        int count;

        public Couple(int voto, int count) {
            this.voto = voto;
            this.count = count;
        }

        public int getVoto() {
            return voto;
        }

        public void setVoto(int voto) {
            this.voto = voto;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }



}