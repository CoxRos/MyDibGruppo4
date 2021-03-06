package gruppo4.dib.sms2016.mydib2016.business.not_logged;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.network.Network;
import gruppo4.dib.sms2016.mydib2016.utility.Costants;


public class InformazioniUni extends Fragment {

    private Intent emailIntent;
    TextView doveSiamoView, pecView, nomeDiretView, emailDiretView, nomeSegretView, emailSegretView;

    RequestQueue queue;
    ProgressDialog progressDialog;


    public InformazioniUni() {

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        queue = Network.getInstance(getActivity()).
                getRequestQueue();
        progressDialog = new ProgressDialog(getActivity());

        emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");

        doveSiamoView = (TextView) getActivity().findViewById(R.id.textDoveSiamo);
        pecView = (TextView) getActivity().findViewById(R.id.textPEC);
        nomeDiretView = (TextView) getActivity().findViewById(R.id.nomeDirettore);
        emailDiretView = (TextView) getActivity().findViewById(R.id.emailDirettore);
        nomeSegretView = (TextView) getActivity().findViewById(R.id.nomeSegretario);
        emailSegretView = (TextView) getActivity().findViewById(R.id.emailSegretario);

        setUI(Costants.URL_INFO_UNI);

        emailDiretView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailDiretView.getText().toString()});
                startActivity(Intent.createChooser(emailIntent, "Invio email al direttore."));
            }
        });

        emailSegretView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailSegretView.getText().toString()});
                startActivity(Intent.createChooser(emailIntent, "Invio email al segretario."));
            }
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_info_uni, container, false);

        return rootview;
    }

    public void setUI(String url) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            doveSiamoView.setText(response.getString("doveSiamo"));
                            pecView.setText(response.getString("pec"));
                            nomeDiretView.setText(response.getString("nomeDir"));
                            emailDiretView.setText(response.getString("emailDir"));
                            nomeSegretView.setText(response.getString("nomeSegr"));
                            emailSegretView.setText(response.getString("emailSegr"));
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                });
        Network.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
        progressDialog.setTitle(getResources().getString(R.string.progress_titolo));
        progressDialog.setMessage(getResources().getString(R.string.progress_message));
        progressDialog.show();
    }

}
