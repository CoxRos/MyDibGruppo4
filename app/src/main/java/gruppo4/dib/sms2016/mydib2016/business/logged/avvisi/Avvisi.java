package gruppo4.dib.sms2016.mydib2016.business.logged.avvisi;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import org.json.JSONException;
import org.json.JSONObject;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.logged.ultimi_dettagli.AvvisiAdapter;
import gruppo4.dib.sms2016.mydib2016.entity.AvvisiEntity;
import gruppo4.dib.sms2016.mydib2016.network.Network;
import gruppo4.dib.sms2016.mydib2016.utility.Costants;

public class Avvisi extends Fragment {

    ProgressDialog progressDialog;
    RequestQueue queue;

    ListView listaAvvisi;
    TextView noItem;
    ImageView noConnection;

    public Avvisi() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_avvisi, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listaAvvisi = (ListView)getActivity().findViewById(R.id.listAvvisi);
        noItem = (TextView)getActivity().findViewById(R.id.verificaConnAvvisi);
        noConnection = (ImageView)getActivity().findViewById(R.id.no_wifiAvvisi);

        queue = Network.getInstance(getContext()).getRequestQueue();
        progressDialog = new ProgressDialog(getActivity());

        setUi(Costants.URL_AVVSI_NEWS);
    }

    private void setUi(String url) {
        final AvvisiAdapter avvisiAdapter = new AvvisiAdapter(getActivity(), R.layout.layout_list_avvisi);
        listaAvvisi.setAdapter(avvisiAdapter);

        JsonArrayRequest jsonRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                boolean isEmpty = true;
                String titolo, descrione, data;

                for(int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(!jsonObject.getString("avviso").equalsIgnoreCase("Y")) {
                            titolo = jsonObject.getString("titolo");
                            descrione = jsonObject.getString("descrizione");
                            data = jsonObject.getString("data");
                            avvisiAdapter.add(new AvvisiEntity(titolo, descrione, data));

                            isEmpty = false;
                        }
                    } catch (JSONException e) {
                        Log.d("CATCH NEWS: ", e.getMessage());
                    }
                }

                if(isEmpty) {
                    listaAvvisi.setVisibility(View.GONE);
                    noItem.setVisibility(View.VISIBLE);
                    noItem.setText(getResources().getString(R.string.no_avvisi));
                    noConnection.setVisibility(View.VISIBLE);
                    noConnection.setImageResource(R.mipmap.ic_dispiaciuto);
                }
                else {
                    listaAvvisi.setVisibility(View.VISIBLE);
                    noItem.setVisibility(View.GONE);
                    noConnection.setVisibility(View.GONE);
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listaAvvisi.setVisibility(View.GONE);
                noItem.setVisibility(View.VISIBLE);
                noConnection.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        });
        Network.getInstance(getContext()).addToRequestQueue(jsonRequest);
        progressDialog.setTitle(getResources().getString(R.string.progress_titolo));
        progressDialog.setMessage(getResources().getString(R.string.progress_message));
        progressDialog.show();
    }
}
