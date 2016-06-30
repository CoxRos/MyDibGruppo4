package gruppo4.dib.sms2016.mydib2016.entity;

/**
 * Created by Utente on 30/06/2016.
 */
public class BusEntity {
    public String numBus,orarioPartenza,orarioArrivo,partenza,destinazione;

    public BusEntity(String numBus, String orarioPartenza,String orarioArrivo, String partenza, String destinazione) {
        this.numBus = numBus;
        this.orarioPartenza = orarioPartenza;
        this.orarioArrivo = orarioArrivo;
        this.partenza = partenza;
        this.destinazione = destinazione;
    }

    public String getNumBus() {
        return numBus;
    }

    public void setNumBus(String numBus) {
        this.numBus = numBus;
    }

    public String getOrarioPartenza() {
        return orarioPartenza;
    }

    public String getOrarioArrivo() {
        return orarioArrivo;
    }

    public void setOrarioPartenza(String orario) {
        this.orarioPartenza = orario;
    }

    public void setOrarioArrivo(String orario) {
        this.orarioArrivo = orario;
    }

    public String getPartenza() {
        return partenza;
    }

    public void setPartenza(String partenza) {
        this.partenza = partenza;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }
}
