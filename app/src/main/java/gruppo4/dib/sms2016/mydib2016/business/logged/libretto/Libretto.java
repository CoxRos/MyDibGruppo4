package gruppo4.dib.sms2016.mydib2016.business.logged.libretto;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gruppo4.dib.sms2016.mydib2016.DataAccessObject.DAOLibretto;
import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.entity.EsameEntity;
import gruppo4.dib.sms2016.mydib2016.network.CustomRequestArray;
import gruppo4.dib.sms2016.mydib2016.network.Network;
import gruppo4.dib.sms2016.mydib2016.utility.Utils;


public class Libretto extends Fragment {

    RequestQueue queue;
    ProgressDialog progressDialog;

    ListView listaEsami;
    TextView noItem,librettoMedia;
    ImageView noEsami;

    public SharedPreferences preferences;

    private DAOLibretto db;

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

        preferences = getActivity().getSharedPreferences("CREDENZIALI", Context.MODE_PRIVATE);

        db = new DAOLibretto(getContext());

        noItem = (TextView) getActivity().findViewById(R.id.messageNoExam);
        listaEsami = (ListView) getActivity().findViewById(R.id.listEsami);
        noEsami = (ImageView) getActivity().findViewById(R.id.no_esami);
        librettoMedia = (TextView) getActivity().findViewById(R.id.librettoMedia);

        if(!getExamsDB()) {
            queue = Network.getInstance(getActivity()).
                    getRequestQueue();

            progressDialog = new ProgressDialog(getActivity());

            setUI("http://mydib2016.altervista.org/api/index.php/selectEsami");
        }
    }

    private void setUI(String url) {
        final ArrayList<EsameEntity> esamiDaCalcolare = new ArrayList<EsameEntity>();
        final Utils utils = new Utils();
        final LibrettoAdapter listAdapter = new LibrettoAdapter(getActivity(), R.layout.layout_list_exams);
        listaEsami.setAdapter(listAdapter);


        CustomRequestArray request = new CustomRequestArray
                (Request.Method.POST, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("JSON ARRAY", response.toString());
                        boolean isEmpty = true;
                        String nome, cfu, voto, data;
                        int countCfu = 0;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject oggettoJson = response.getJSONObject(i);

                                nome = oggettoJson.getString("materia");
                                cfu = oggettoJson.getString("cfu");
                                voto = oggettoJson.getString("voto");
                                data = oggettoJson.getString("data");
                                isEmpty = false;
                                listAdapter.add(new EsameEntity(nome, cfu, voto, data));
                                if(!voto.equalsIgnoreCase("IDO")) {
                                    esamiDaCalcolare.add(new EsameEntity(nome, cfu, voto, data));
                                }
                                countCfu = countCfu + Integer.parseInt(cfu);
                                db.insertEsame(nome, cfu, voto, data);

                            } catch (Exception e) {
                                System.out.println("catch: " + e);
                            }
                        }
                        if (isEmpty) {
                            //subBar.setVisibility(View.GONE);
                            listaEsami.setVisibility(View.GONE);
                            noItem.setVisibility(View.VISIBLE);
                            noEsami.setVisibility(View.VISIBLE);
                            librettoMedia.setVisibility(View.GONE);
                        } else {
                            librettoMedia.setText("Media: " + utils.getMediaPonderata(esamiDaCalcolare) +
                            " - CFU: " + countCfu + "/180" );
                        }

                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //subBar.setVisibility(View.GONE);
                        listaEsami.setVisibility(View.GONE);
                        noItem.setVisibility(View.VISIBLE);
                        noEsami.setVisibility(View.VISIBLE);
                        Log.d("ATTENZIONE:", error.getMessage());
                        error.printStackTrace();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("matricola", preferences.getString("matricola", "") );
                return params;
            }
        };

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
        ArrayList<EsameEntity> esamiDaCalcolare = new ArrayList<EsameEntity>();
        Utils utils = new Utils();
        LibrettoAdapter listAdapter = new LibrettoAdapter(getActivity(), R.layout.layout_list_exams);
        listaEsami.setAdapter(listAdapter);
        List<EsameEntity> esami = db.getEsami();

        if(esami.isEmpty()) {
            return false;
        }
        else {
            int countCfu = 0;
            for(EsameEntity esam : esami) {
                listAdapter.add(esam);
                if(!esam.getVoto().equalsIgnoreCase("IDO")) {
                    esamiDaCalcolare.add(esam);
                }
                countCfu = countCfu + Integer.parseInt(esam.getCfu());
            }
            librettoMedia.setText("Media: " + utils.getMediaPonderata(esamiDaCalcolare) +
                    " - CFU: " + countCfu + "/180" );
            return true;
        }
    }
}
