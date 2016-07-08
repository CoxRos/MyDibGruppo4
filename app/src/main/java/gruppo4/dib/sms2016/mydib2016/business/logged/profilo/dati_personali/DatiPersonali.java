package gruppo4.dib.sms2016.mydib2016.business.logged.profilo.dati_personali;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gruppo4.dib.sms2016.mydib2016.DataAccessObject.DAOLibretto;
import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.entity.EsameEntity;
import gruppo4.dib.sms2016.mydib2016.utility.Utils;


public class DatiPersonali extends Fragment {

    TextView nome,cognome,email,ponderata,aritmetica,cfu,txtMatricolaProfilo;
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    Utils utility;
    private DAOLibretto db;

    public DatiPersonali() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dati_personali, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        db = new DAOLibretto(getContext());

        utility = new Utils();

        preferences = getActivity().getSharedPreferences("CREDENZIALI", Context.MODE_PRIVATE);
        editor = preferences.edit();

        txtMatricolaProfilo = (TextView) getActivity().findViewById(R.id.matricolaProfilo);
        nome = (TextView) getActivity().findViewById(R.id.editNomeProfilo);
        cognome = (TextView) getActivity().findViewById(R.id.editCognomeProfilo);
        email = (TextView) getActivity().findViewById(R.id.editEmailProfilo);
        ponderata = (TextView) getActivity().findViewById(R.id.mediaPondProfilo);
        aritmetica = (TextView) getActivity().findViewById(R.id.mediaAritmProfilo);
        cfu = (TextView) getActivity().findViewById(R.id.cfuProfilo);

        nome.setText(preferences.getString("nome", ""));
        cognome.setText(preferences.getString("cognome", ""));
        email.setText(preferences.getString("email", ""));
        txtMatricolaProfilo.setText(preferences.getString("matricola", "Dato non disponibile"));

        //Qui avvaloro la lista di esami
        ArrayList<EsameEntity> esami =(ArrayList<EsameEntity>) db.getEsami();
        ponderata.setText(Double.toString(utility.getMediaPonderata(esami)));
        aritmetica.setText(Double.toString(utility.getMediaAritmetica(esami)));
        cfu.setText(Integer.toString(utility.getCFU(esami)));
    }

}
