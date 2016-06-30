package gruppo4.dib.sms2016.mydib2016.homepage.not_logged.bus;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import gruppo4.dib.sms2016.mydib2016.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RitornoBus extends ListFragment {

    ArrayList<HashMap<String,String>> listaBus = new ArrayList<HashMap<String, String>>();
    SimpleAdapter simpleAdapter;

    public static RitornoBus newInstance() {
        RitornoBus fragment = new RitornoBus();
        return fragment;
    }
    public RitornoBus() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_ritorno_bus, container, false);

        HashMap<String,String> map = new HashMap<String,String>();



        String[] from = {"OrarioPartenza","OrarioArrivo"};
        int[] to = {R.id.partenzaOrario,R.id.arrivoOrario};


        map.put("OrarioPartenza","07:30");
        map.put("OrarioArrivo","09:43");


        listaBus.add(map);
        map = new HashMap<String,String>();
        map.put("OrarioPartenza","10:15");
        map.put("OrarioArrivo","11:26");

        listaBus.add(map);
        map = new HashMap<String,String>();
        map.put("OrarioPartenza","18:15");
        map.put("OrarioArrivo","19:26");
        listaBus.add(map);

        simpleAdapter = new SimpleAdapter(getActivity(),listaBus,R.layout.layout_list_bus,from,to);

        setListAdapter(simpleAdapter);

        // Inflate the layout for this fragment
        return rootview;
    }

}
