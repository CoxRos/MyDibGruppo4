package gruppo4.dib.sms2016.mydib2016.business.logged;


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

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.logged.libretto.LibrettoAdapter;
import gruppo4.dib.sms2016.mydib2016.entity.EsameEntity;
import gruppo4.dib.sms2016.mydib2016.network.Network;


public class Libretto extends Fragment {

    RequestQueue queue;
    ProgressDialog progressDialog;

    ListView listaEsami;
    TextView noItem;
    ImageView noEsami;

    public Libretto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_libretto, container, false);

        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        noItem = (TextView) getActivity().findViewById(R.id.messageNoExam);
        listaEsami = (ListView) getActivity().findViewById(R.id.listEsami);
        noEsami = (ImageView) getActivity().findViewById(R.id.no_esami);

        queue = Network.getInstance(getActivity()).
                getRequestQueue();

        progressDialog = new ProgressDialog(getActivity());

        setUI("http://mydib2016.altervista.org/api/index.php/libretto");
    }

    private void setUI(String url) {
        final LibrettoAdapter listAdapter = new LibrettoAdapter(getActivity(), R.layout.layout_list_exams);
        listaEsami.setAdapter(listAdapter);

        JsonArrayRequest request = new JsonArrayRequest
                (url, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        boolean isEmpty = true;
                        String nome, cfu, voto, data;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject oggettoJson = response.getJSONObject(i);

                                nome = oggettoJson.getString("nomeEsame");
                                cfu = oggettoJson.getString("cfuEsame");
                                voto = oggettoJson.getString("votoEsame");
                                data = oggettoJson.getString("dataEsame");
                                isEmpty = false;
                                listAdapter.add(new EsameEntity(nome, cfu, voto, data));

                            } catch (Exception e) {
                                System.out.println("catch: " + e);
                            }
                        }
                        if (isEmpty) {
                            listaEsami.setVisibility(View.GONE);
                            noItem.setVisibility(View.VISIBLE);
                            noEsami.setVisibility(View.VISIBLE);
                        }

                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listaEsami.setVisibility(View.GONE);
                        noItem.setVisibility(View.VISIBLE);
                        noEsami.setVisibility(View.VISIBLE);
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

    /**
     * Il metodo prende gli esami dal database disegnando la view e restituisce true.
     * Se non sono presenti esami nel database allora restituisce false e prova a vedere
     * dal server perché l'utente potrebbe aver cancellato i dati sul telefono.
     * Se non trova niente neanche sul server allora chiede di inserire i dati
     *
     * @return
     */
    private boolean getExamsDB() {
        return false;
    }

}
