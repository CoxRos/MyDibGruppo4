package gruppo4.dib.sms2016.mydib2016.business.logged.profilo.dati_personali;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gruppo4.dib.sms2016.mydib2016.R;


public class DatiPersonali extends Fragment {

    TextView nome,cognome,email,ponderata,aritmetica,cfu;

    public DatiPersonali() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dati_personali, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        nome = (TextView) getActivity().findViewById(R.id.editNomeProfilo);
        cognome = (TextView) getActivity().findViewById(R.id.editCognomeProfilo);
        email = (TextView) getActivity().findViewById(R.id.editEmailProfilo);
        ponderata = (TextView) getActivity().findViewById(R.id.mediaPondProfilo);
        aritmetica = (TextView) getActivity().findViewById(R.id.mediaAritmProfilo);
        cfu = (TextView) getActivity().findViewById(R.id.cfuProfilo);
    }

}
