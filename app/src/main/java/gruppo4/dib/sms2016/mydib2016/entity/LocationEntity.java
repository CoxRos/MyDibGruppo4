package gruppo4.dib.sms2016.mydib2016.entity;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

public class LocationEntity {

    LatLng posizione;
    String luogo,descrizione;
    BitmapDescriptor icona;

    public LocationEntity(LatLng posizione, String luogo, String descrizione, BitmapDescriptor icona) {
        this.posizione = posizione;
        this.luogo = luogo;
        this.descrizione = descrizione;
        this.icona = icona;
    }

    public LatLng getPosizione() {
        return posizione;
    }

    public void setPosizione(LatLng posizione) {
        this.posizione = posizione;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public BitmapDescriptor getIcona() {
        return icona;
    }

    public void setIcona(BitmapDescriptor icona) {
        this.icona = icona;
    }
}
