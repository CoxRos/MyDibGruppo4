package gruppo4.dib.sms2016.mydib2016.business.logged.ricerca;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.logged.sharing.Sharing;
import gruppo4.dib.sms2016.mydib2016.entity.UtenteEntity;
import gruppo4.dib.sms2016.mydib2016.network.CustomRequestArray;
import gruppo4.dib.sms2016.mydib2016.network.Network;

public class RicercaUtente extends Fragment {

    RequestQueue queue;
    ProgressDialog progressDialog;

    ImageButton search;
    EditText ricercato;
    ListView listRicerca;
    TextView messageNoRicerca;
    ImageView no_utenti;

    public RicercaUtente() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ricerca_utente, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        search = (ImageButton) getActivity().findViewById(R.id.btnSearch);
        ricercato = (EditText) getActivity().findViewById(R.id.editNominativo);
        listRicerca = (ListView) getActivity().findViewById(R.id.listRicerca);
        messageNoRicerca = (TextView) getActivity().findViewById(R.id.messageNoRicerca);
        no_utenti = (ImageView) getActivity().findViewById(R.id.no_utenti);

        queue = Network.getInstance(getActivity()).
                getRequestQueue();

        progressDialog = new ProgressDialog(getActivity());

        final RicercaAdapter ricercaAdapter = new RicercaAdapter(getActivity(),R.layout.layout_list_ricerca);
        listRicerca.setAdapter(ricercaAdapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String textToSearch = ricercato.getText().toString();

                if(textToSearch.length() > 0) {
                    CustomRequestArray jsonObjectRequest = new CustomRequestArray(Request.Method.POST,
                            "http://mydib2016.altervista.org/api/index.php/ricerca",null,new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            boolean isEmpty = true;
                            String nome, cognome, tipo, email, telefono, ricevimento, web;
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject oggettoJson = response.getJSONObject(i);

                                    nome = oggettoJson.getString("nomeUtente");
                                    System.out.println(nome);
                                    cognome = oggettoJson.getString("cognomeUtente");
                                    email = oggettoJson.getString("emailUtente");
                                    tipo = oggettoJson.getString("tipoUtente");
                                    if(tipo.equalsIgnoreCase("Docente")) {
                                        telefono = oggettoJson.getString("telefonoUtente");
                                        ricevimento = oggettoJson.getString("ricevimentoUtente");
                                        web = oggettoJson.getString("webUtente");
                                    } else {
                                        telefono = "";
                                        ricevimento = "";
                                        web = "";
                                    }
                                    isEmpty = false;
                                    ricercaAdapter.add(new UtenteEntity(nome,cognome,tipo,email,telefono,ricevimento,web));


                                } catch (Exception e) {
                                    System.out.println("catch: " + e);
                                }
                            }
                            if (isEmpty) {
                                listRicerca.setVisibility(View.GONE);
                                messageNoRicerca.setVisibility(View.VISIBLE);
                                messageNoRicerca.setText("Non è stato trovato alcun utente con i dati inseriti");
                                no_utenti.setVisibility(View.VISIBLE);
                            }

                            progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("CLASSE RICERCA", "Errore connessione " + error.toString());
                            listRicerca.setVisibility(View.GONE);
                            messageNoRicerca.setVisibility(View.VISIBLE);
                            messageNoRicerca.setText("Verificare la connessione");
                            no_utenti.setVisibility(View.VISIBLE);
                            System.out.println("ERR: " + error.getMessage());
                            Log.d("ATTENZIONE:", error.getCause().toString());
                            error.printStackTrace();
                            progressDialog.dismiss();
                        }

                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("ricerca", textToSearch);
                            return params;
                        }
                    };
                    Network.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Attedere");
                    progressDialog.setMessage("Ricerca in corso...");
                    progressDialog.show();
                }
            }
        });
    }
}
