package gruppo4.dib.sms2016.mydib2016.business.not_logged.bus;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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

import gruppo4.dib.sms2016.mydib2016.network.Network;
import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.entity.BusEntity;
import gruppo4.dib.sms2016.mydib2016.utility.Costants;


public class RitornoBus extends Fragment {

    RequestQueue queue;
    ProgressDialog progressDialog;

    ListView listaAutobus;
    TextView noItem;
    ImageView noConnection;

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

        // Inflate the layout for this fragment
        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        noItem = (TextView) getActivity().findViewById(R.id.verificaConnRitorno);
        listaAutobus = (ListView) getActivity().findViewById(R.id.listAutobusRitorno);
        noConnection = (ImageView) getActivity().findViewById(R.id.no_wifiRitorno);

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
                                if(oggettoJson.getString("partenza").equalsIgnoreCase("UNI")) {
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
                            noItem.setText("Non sono presenti orari");
                            noConnection.setVisibility(View.VISIBLE);
                            noConnection.setImageResource(R.mipmap.ic_dispiaciuto);
                        } else {
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
                        System.out.println("ERR: " + error.getMessage());
                        Log.d("ATTENZIONE:", error.getCause().toString());
                        error.printStackTrace();
                        progressDialog.dismiss();
                    }
                });

        Network.getInstance(getActivity()).addToRequestQueue(request);
        progressDialog.setTitle("Attendere");
        progressDialog.setMessage("Caricamento richiesta");
        progressDialog.show();
    }
}
