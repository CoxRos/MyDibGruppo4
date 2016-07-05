package gruppo4.dib.sms2016.mydib2016.business.logged.homenews;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.entity.AvvisiEntity;
import gruppo4.dib.sms2016.mydib2016.network.Network;

public class UltimiEventi extends Fragment {
    ProgressBar progressBar;
    TextView textViewMedia;

    ProgressDialog progressDialog;
    RequestQueue queue;

    ListView listaAvvisi;
    TextView noItem;
    ImageView noConnection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ultimi_eventi, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressBar = (ProgressBar)getActivity().findViewById(R.id.progressMedia);
        textViewMedia = (TextView)getActivity().findViewById((R.id.textProgressMedia));

        listaAvvisi = (ListView)getActivity().findViewById(R.id.listNewsHome);
        noItem = (TextView) getActivity().findViewById(R.id.verificaConnNewsHome);
        noConnection = (ImageView)getActivity().findViewById(R.id.no_wifiNewsHome);

        progressBar.setProgress(getProgressMedia(22)); //qui metto la media intera
        progressBar.setMax(13);
        progressBar.setProgressDrawable(getActivity().getResources().getDrawable(R.drawable.progress_bar));

        textViewMedia.setText("Media Ponderata: 22,50/30"); //qui ci va la reale media

        queue = Network.getInstance(getActivity()).getRequestQueue();
        progressDialog = new ProgressDialog(getActivity());

        setUi("http://mydib2016.altervista.org/api/index.php/avvisi");
    }

    private int getProgressMedia(int media) {
        return (media - 18) + 1;
    }

    private AlertDialog createAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);

        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    private void setUi(String url) {
        final AvvisiAdapter newsAdapter = new AvvisiAdapter(getActivity(), R.layout.card_view_news_layout);
        listaAvvisi.setAdapter(newsAdapter);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray responce) {
                boolean isEmpty = true;
                String titolo, descrione, data;
                int count = 0;
                for (int i = 0; i < responce.length(); i++) {
                    if(count < 3) {
                        try {
                            JSONObject jsonResponce = responce.getJSONObject(i);
                            titolo = jsonResponce.getString("titolo");
                            descrione = jsonResponce.getString("descrizione");
                            data = jsonResponce.getString("data");

                            newsAdapter.add(new AvvisiEntity(titolo, descrione, data));

                            isEmpty = false;
                            count++;
                        } catch (JSONException e) {
                            Log.d("CATCH NEWS: ", e.getMessage().toString());
                        }
                    }
                }
                if(isEmpty) {
                    listaAvvisi.setVisibility(View.GONE);
                    noItem.setVisibility(View.VISIBLE);
                    noItem.setText("Non sono presenti avvisi");
                    noConnection.setVisibility(View.VISIBLE);
                    noConnection.setImageResource(R.mipmap.ic_dispiaciuto);
                } else {
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
                Log.d("ATTENZIONE:", volleyError.getCause().toString());
                progressDialog.dismiss();
            }
        });
        Network.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
        progressDialog.setTitle("Attendere");
        progressDialog.setMessage("Caricamento Avvisi");
        progressDialog.show();
    }
}
