package gruppo4.dib.sms2016.mydib2016.business.logged.libretto;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import gruppo4.dib.sms2016.mydib2016.DataAccessObject.DAOLibretto;
import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.Autenticazione.Login;
import gruppo4.dib.sms2016.mydib2016.business.homepage.HomePage;
import gruppo4.dib.sms2016.mydib2016.entity.EsameEntity;
import gruppo4.dib.sms2016.mydib2016.network.CustomRequestObject;
import gruppo4.dib.sms2016.mydib2016.network.Network;
import gruppo4.dib.sms2016.mydib2016.utility.Costants;

public class LibrettoAdapter extends ArrayAdapter<EsameEntity> {

    private final int NEW_LAYOUT_RESOURCE;

    private DAOLibretto db;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    Login credenziali = new Login();
    static String matricola;

    private ProgressDialog progressDialog;
    RequestQueue queue;

    public LibrettoAdapter(final Context context, final int NEW_LAYOUT_RESOURCE) {
        super(context, 0);
        this.NEW_LAYOUT_RESOURCE = NEW_LAYOUT_RESOURCE;
        db = new DAOLibretto(context);
        preferences = context.getSharedPreferences("esami", Context.MODE_PRIVATE);
        edit = preferences.edit();

        credenziali.getMatricola(context);
        matricola = credenziali.matricola;

        queue = Network.getInstance(context).getRequestQueue();

    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final EsameEntity entry = getItem(position);

        if(entry.getVoto().equalsIgnoreCase("IDO")) {
            viewHolder.scalaVoto.setBackgroundResource(R.mipmap.greenline);
        } else if (Integer.parseInt(entry.getVoto()) < 20) { //Pallino rosso
            viewHolder.scalaVoto.setBackgroundResource(R.mipmap.redline);
        } else if(Integer.parseInt(entry.getVoto()) < 24){ //Pallino arancione
            viewHolder.scalaVoto.setBackgroundResource(R.mipmap.orangeline);
        } else if(Integer.parseInt(entry.getVoto()) < 27) { //Pallino giallo
            viewHolder.scalaVoto.setBackgroundResource(R.mipmap.yellowline);
        } else if(Integer.parseInt(entry.getVoto()) >= 27) { //Pallino verde
            viewHolder.scalaVoto.setBackgroundResource(R.mipmap.greenline);
        }

        viewHolder.cfu.setText(entry.getCfu());
        viewHolder.data.setText(entry.getData());
        viewHolder.nome.setText(entry.getNome());
        viewHolder.voto.setText(entry.getVoto());

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),EsameActivity.class);
                intent.putExtra("esame",entry.getNome());
                intent.putExtra("option", "visualizza");
                getContext().startActivity(intent);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getAlertDialog(entry.getNome()).show();
                return true;
            }
        });

        return view;
    }

    private View getWorkingView(final View convertView) {
        View workingView = null;

        if (null == convertView) {
            final Context context = getContext();
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            workingView = inflater.inflate(NEW_LAYOUT_RESOURCE, null);
        } else {
            workingView = convertView;
        }

        return workingView;
    }

    private ViewHolder getViewHolder(final View workingView) {
        // The viewHolder allows us to avoid re-looking up view references
        // Since views are recycled, these references will never change
        final Object tag = workingView.getTag();
        ViewHolder viewHolder = null;


        if (null == tag || !(tag instanceof ViewHolder)) {
            viewHolder = new ViewHolder();

            viewHolder.scalaVoto = (ImageView) workingView.findViewById(R.id.scalaVoto);
            viewHolder.nome = (TextView) workingView.findViewById(R.id.nomeAppunti);
            viewHolder.voto = (TextView) workingView.findViewById(R.id.votoEsame);
            viewHolder.cfu = (TextView) workingView.findViewById(R.id.cfuEsame);
            viewHolder.data = (TextView) workingView.findViewById(R.id.dataAppunti);

            workingView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) tag;
        }

        return viewHolder;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private AlertDialog getAlertDialog(final String nomeEsame) {
        CharSequence[] items = {getContext().getResources().getString(R.string.modifica), getContext().getResources().getString(R.string.elimina)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getContext().getResources().getString(R.string.azione));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectOption(which, nomeEsame);
            }
        });
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    /**
     * Cambia l'activity in base alla scelta effettuata dal menu
     * @param option
     *      0 se viene scelto modifica, 1 se viene scelto elimina
     */
    private void selectOption(int option, String nomeEsame) {
        if(option == 0) {
            Intent intent = new Intent(getContext(), EsameActivity.class);
            intent.putExtra("esame", nomeEsame);
            intent.putExtra("option", "modifica");
            getContext().startActivity(intent);
        }
        else {
            getDialogElimina(getContext().getResources().getString(R.string.conferma_titolo), getContext().getResources().getString(R.string.conferma_testo) + nomeEsame + "?", nomeEsame).show();
        }

    }

    private AlertDialog getDialogElimina(String title, String message, final String nomeEsame) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isDeleted = db.deleteEsame(nomeEsame);

                if(isDeleted) {
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.elimina_dati), Toast.LENGTH_LONG).show();
                    doRequest(Costants.URL_DELETE_ESAMI, nomeEsame);
                } else {
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.elimina_dati_errore), Toast.LENGTH_LONG);
                }
            }
        });
        builder.setNegativeButton("Annulla", null);

        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    private void doRequest(String url, final String nomeEsame) {
        CustomRequestObject requestObject = new CustomRequestObject(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                boolean status;
                String message;
                try {
                    status = jsonObject.getBoolean("status");
                    message = jsonObject.getString("message");
                    Log.d("LOG DEL LIBRETTO", "STATO DELLA RICHIESTA " + status + " MESSAGE " + message);
                }
                catch (Exception e) {
                    Log.d("ECCEZIONE LIBRETTO: ", e.getMessage().toString());
                }
                progressDialog.dismiss();
                goToLibretto();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                edit.putInt("EXAMTOSEND", 1);//ESAME DA INVIARE PRESENTE
                edit.commit();
                progressDialog.dismiss();
                goToLibretto();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("materia", nomeEsame);
                params.put("matricola",matricola);
                return params;
            }
        };
        Network.getInstance(getContext()).addToRequestQueue(requestObject);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getContext().getResources().getString(R.string.progress_titolo));
        progressDialog.setMessage(getContext().getResources().getString(R.string.progress_message));
        progressDialog.show();
    }

    private void goToLibretto() {
        Intent intent = new Intent(getContext(), HomePage.class);
        intent.putExtra("goTo",2);
        getContext().startActivity(intent);
    }

    private static class ViewHolder {
        public ImageView scalaVoto;
        public TextView nome;
        public TextView cfu;
        public TextView voto;
        public TextView data;
    }
}
