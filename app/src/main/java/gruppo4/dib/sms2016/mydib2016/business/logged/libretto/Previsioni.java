package gruppo4.dib.sms2016.mydib2016.business.logged.libretto;

import android.content.Context;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import gruppo4.dib.sms2016.mydib2016.DataAccessObject.DAOLibretto;
import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.entity.EsameEntity;
import gruppo4.dib.sms2016.mydib2016.utility.Utils;

/**
 * Created by sergiocorvino on 08/07/16.
 */
public class Previsioni extends Fragment {

    private EditText voto, cfu;
    private TextView txtMediaPonderata, txtMediaAritmetica, txtBaseLaurea, mediaPonderata, mediaAritmetica, baseLaurea;
    private Button calcolaPrevisione;

    private DAOLibretto db;
    private Utils utils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_previsioni_esami, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        db = new DAOLibretto(getContext());
        utils = new Utils();

        voto = (EditText)getActivity().findViewById(R.id.votoEsamePrevisto);
        cfu = (EditText)getActivity().findViewById(R.id.cfuEsamePrevisto);
        txtMediaPonderata = (TextView)getActivity().findViewById(R.id.media_pon_txt);
        txtMediaAritmetica = (TextView)getActivity().findViewById(R.id.media_arit_txt);
        txtBaseLaurea = (TextView)getActivity().findViewById(R.id.base_laurea_txt);
        mediaPonderata = (TextView)getActivity().findViewById(R.id.media_pon);
        mediaAritmetica = (TextView)getActivity().findViewById(R.id.media_arit);
        baseLaurea = (TextView)getActivity().findViewById(R.id.base_laurea);
        calcolaPrevisione = (Button)getActivity().findViewById(R.id.button_calcola_previsione);

        voto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //vuoto
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //vuoto
            }
        });

        cfu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //vuoto
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //vuoto
            }
        });

        calcolaPrevisione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //nascondo la tastiera al click del bottone
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                //controllo l'input inserito
                int votoIns = Integer.parseInt(voto.getText().toString());
                int cfuIns = Integer.parseInt(cfu.getText().toString());

                if(votoIns < 18 || votoIns > 30) {
                    voto.setError("Inserisci un voto valido");
                    return;
                }

                if(cfuIns > 15) {
                    cfu.setError("Inserisci dei CFU validi");
                    return;
                }

                ArrayList<EsameEntity> esami = new ArrayList<EsameEntity>();
                EsameEntity prossimoEsame = new EsameEntity("", cfu.getText().toString(), voto.getText().toString(), "");
                esami = (ArrayList<EsameEntity>) db.getEsami();
                esami.add(prossimoEsame);

                double mediaPonderataPrevista = utils.getMediaPonderata(esami);

                txtMediaAritmetica.setVisibility(View.VISIBLE);
                mediaAritmetica.setVisibility(View.VISIBLE);
                mediaAritmetica.setText(String.valueOf(utils.getMediaAritmetica(esami)));
                txtMediaPonderata.setVisibility(View.VISIBLE);
                mediaPonderata.setVisibility(View.VISIBLE);
                mediaPonderata.setText(String.valueOf(utils.getMediaPonderata(esami)));
                txtBaseLaurea.setVisibility(View.VISIBLE);
                baseLaurea.setVisibility(View.VISIBLE);
                baseLaurea.setText(String.valueOf(utils.getBaseLaurea(mediaPonderataPrevista)));
            }
        });
    }

    /**
     * Rende cliccabile il bottone solo se non stati avvalorati tutti i campi
     */
    private void enableButton() {
        if(voto.getText().length() > 0 && cfu.getText().length() > 0) {
            calcolaPrevisione.setEnabled(true);
        }
        else {
            calcolaPrevisione.setEnabled(false);
        }
    }
}
