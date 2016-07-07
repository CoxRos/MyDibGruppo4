package gruppo4.dib.sms2016.mydib2016.business.logged.libretto;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import gruppo4.dib.sms2016.mydib2016.DataAccessObject.DAOLibretto;
import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.entity.EsameEntity;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraficoLineChart extends Fragment {

    RelativeLayout mainLayout;
    PieChart pieChart;

    private DAOLibretto db;

    //float[] yData = {5,10,15,40};
    //String[] xData = {"Uno","Due","Tre","Quattro"};
    float[] yData;
    String[] xData;

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

        //noItem = (TextView) getActivity().findViewById(R.id.verificaConnRitorno);
        //noItemImage = (ImageView) getActivity().findViewById(R.id.no_wifiRitorno);

        db = new DAOLibretto(getContext());

        xDataPers = new ArrayList<String>();
        yDataPers = new ArrayList<String>();


        setPercentual();

        setUI();
    }

    private void setUI() {
        mainLayout = (RelativeLayout) getActivity().findViewById(R.id.mainLayout);
        pieChart = new PieChart(getContext());
        mainLayout.addView(pieChart);
        mainLayout.setBackgroundColor(Color.WHITE);

        pieChart.setUsePercentValues(true);
        pieChart.setDescription("Voti");

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(5);
        pieChart.setTransparentCircleRadius(10);

        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);

        addData();
    }

    private void setPercentual() {
        List<EsameEntity> esami = db.getEsami();
        ArrayList<Couple> voti = new ArrayList<Couple>();
        for(EsameEntity esame : esami) {
            int esameVoto = Integer.parseInt(esame.getVoto());
            boolean thereIs = false;
            for(Couple coppie : voti) {
                if(coppie.getVoto() == esameVoto) {
                    thereIs = true;
                    coppie.setCount(coppie.getCount() +1);
                    break;
                }
            }
            if(!thereIs) {
                voti.add(new Couple(esameVoto,1));
            }
        }

        for(Couple v : voti) {
            xDataPers.add(Integer.toString(v.getVoto()));
            yDataPers.add(Integer.toString(v.getCount()));
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
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

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
