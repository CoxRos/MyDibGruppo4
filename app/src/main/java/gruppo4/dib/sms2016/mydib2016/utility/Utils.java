package gruppo4.dib.sms2016.mydib2016.utility;


import java.util.ArrayList;

import gruppo4.dib.sms2016.mydib2016.entity.EsameEntity;

public class Utils {

    public double getBaseLaurea(double media) {
        return (media*11)/3;
    }

    public double getMediaPonderata(ArrayList<EsameEntity> esami) {
        double result = 0;
        int sommCFU = 0;
        for(EsameEntity esame : esami) {
            result = result + (Integer.parseInt(esame.getVoto())*Integer.parseInt(esame.getCfu()));
            sommCFU += Integer.parseInt(esame.getCfu());
        }
        return troncamento(result/sommCFU);
    }

    public double getMediaAritmetica(ArrayList<EsameEntity> esami) {
        double result = 0;
        for(EsameEntity esame : esami) {
            result = result + Integer.parseInt(esame.getVoto());
        }
        return troncamento(result/esami.size());
    }

    public static double troncamento(double x){
        x = Math.floor(x*100);
        x = x/100;
        return x;
    }
}
