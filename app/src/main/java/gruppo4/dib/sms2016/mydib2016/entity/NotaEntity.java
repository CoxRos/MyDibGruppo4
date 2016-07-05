package gruppo4.dib.sms2016.mydib2016.entity;

/**
 * Created by Utente on 04/07/2016.
 */
public class NotaEntity {

    String titolo, descrizione, autore, data, url;

    public NotaEntity(String titolo, String descrizione, String autore, String data, String url) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.autore = autore;
        this.data = data;
        this.url = url;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
