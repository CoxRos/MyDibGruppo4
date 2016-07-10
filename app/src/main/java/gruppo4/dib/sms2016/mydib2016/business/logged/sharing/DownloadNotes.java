package gruppo4.dib.sms2016.mydib2016.business.logged.sharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import gruppo4.dib.sms2016.mydib2016.entity.NotaEntity;
import gruppo4.dib.sms2016.mydib2016.network.Network;
import gruppo4.dib.sms2016.mydib2016.utility.Costants;

public class DownloadNotes extends AppCompatActivity {

    ListView listaNote;
    ImageView no_appunti;
    TextView messageNoNotes;

    RequestQueue queue;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_notes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DownloadNotes.this, Sharing.class);
                startActivity(intent);
            }
        });

        queue = Network.getInstance(DownloadNotes.this).
                getRequestQueue();

        progressDialog = new ProgressDialog(DownloadNotes.this);

        listaNote = (ListView) findViewById(R.id.listNotes);
        no_appunti = (ImageView) findViewById(R.id.no_appunti);
        messageNoNotes = (TextView) findViewById(R.id.messageNoNotes);

        setUI(Costants.URL_DOWNLOAD_NOTE);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DownloadNotes.this, Sharing.class);
        startActivity(intent);
    }

    private void setUI(String url) {
        final NotesAdapter listAdapter = new NotesAdapter(DownloadNotes.this, R.layout.layout_list_notes);
        listaNote.setAdapter(listAdapter);


        JsonArrayRequest request = new JsonArrayRequest
                (url, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        boolean isEmpty = true;
                        String titolo, autore, descrizione, data,url;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject oggettoJson = response.getJSONObject(i);

                                titolo = oggettoJson.getString("titolo");
                                autore = oggettoJson.getString("autore");
                                descrizione = oggettoJson.getString("descrizione");
                                data = oggettoJson.getString("data");
                                url = oggettoJson.getString("url");
                                isEmpty = false;
                                listAdapter.add(new NotaEntity(titolo, descrizione, autore, data,url));

                            } catch (Exception e) {
                                System.out.println("catch: " + e);
                            }
                        }
                        if (isEmpty) {
                            listaNote.setVisibility(View.GONE);
                            no_appunti.setVisibility(View.VISIBLE);
                            messageNoNotes.setVisibility(View.VISIBLE);
                        }

                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listaNote.setVisibility(View.GONE);
                        no_appunti.setVisibility(View.VISIBLE);
                        messageNoNotes.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }
                });

        Network.getInstance(DownloadNotes.this).addToRequestQueue(request);
        progressDialog.setTitle(getResources().getString(R.string.progress_titolo));
        progressDialog.setMessage(getResources().getString(R.string.progress_message));
        progressDialog.show();
    }
}
