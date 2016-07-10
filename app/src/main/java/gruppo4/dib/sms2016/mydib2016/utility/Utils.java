package gruppo4.dib.sms2016.mydib2016.utility;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import gruppo4.dib.sms2016.mydib2016.business.Autenticazione.Login;
import gruppo4.dib.sms2016.mydib2016.entity.EsameEntity;

public class Utils {

    Login credenziali = new Login();
    static String matricola;

    public double getBaseLaurea(double media) {
        return troncamento((media*11)/3);
    }

    public double getMediaPonderata(ArrayList<EsameEntity> esami) {
        double result = 0;
        int sommCFU = 0;
        for(EsameEntity esame : esami) {
            if(!esame.getVoto().equals("IDO")) {
                result = result + (Integer.parseInt(esame.getVoto()) * Integer.parseInt(esame.getCfu()));
                sommCFU += Integer.parseInt(esame.getCfu());
            }
        }

        if(result == 0) {
            return 0;
        }
        else {
            return troncamento(result / sommCFU);
        }
    }

    public double getMediaAritmetica(ArrayList<EsameEntity> esami) {
        double result = 0;
        for(EsameEntity esame : esami) {
            if(!esame.getVoto().equals("IDO")) {
                result = result + Integer.parseInt(esame.getVoto());
            }
        }

        if(result == 0) {
            return 0;
        }
        else {
            return troncamento(result / esami.size());
        }
    }

    public int getCFU(ArrayList<EsameEntity> esami) {
        int result = 0;
        for(EsameEntity esame : esami) {
            result = result + Integer.parseInt(esame.getCfu());
        }
        return result;
    }

    public static double troncamento(double x){
        x = Math.floor(x*100);
        x = x/100;
        return x;
    }

    public List<String> getQueries(List<EsameEntity> esami,Context context) {
        List<String> queries = new ArrayList<String>();
        credenziali.getMatricola(context);
        matricola = credenziali.matricola;

        for(EsameEntity esame : esami) {
            queries.add("INSERT INTO libretto(materia, CFU, voto, data, matricola) VALUES('"
                    + esame.getNome() +"','"+ esame.getCfu() +"','"+ esame.getVoto()+"','"+esame.getData() +"','" + matricola + "')");
        }

        return queries;

    }
}
