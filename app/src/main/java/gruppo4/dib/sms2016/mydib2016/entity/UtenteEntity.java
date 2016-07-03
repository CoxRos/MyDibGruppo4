package gruppo4.dib.sms2016.mydib2016.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class UtenteEntity implements Parcelable {
    String nome, cognome, tipo, email, telefono, ricevimento, web;

    public UtenteEntity(String nome, String cognome, String tipo, String email, String telefono, String ricevimento, String web) {
        this.nome = nome;
        this.cognome = cognome;
        this.tipo = tipo;
        this.email = email;
        this.telefono = telefono;
        this.ricevimento = ricevimento;
        this.web = web;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRicevimento() {
        return ricevimento;
    }

    public void setRicevimento(String ricevimento) {
        this.ricevimento = ricevimento;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    // Parcelling part
    public UtenteEntity(Parcel in) {
        String[] data = new String[7];

        in.readStringArray(data);
        this.nome = data[0];
        this.cognome = data[1];
        this.tipo = data[2];
        this.email = data[3];
        this.telefono = data[4];
        this.ricevimento = data[5];
        this.web = data[6];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.nome,
                this.cognome,
                this.tipo,
                this.email,
                this.telefono,
                this.ricevimento,
                this.web});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public UtenteEntity createFromParcel(Parcel in) {
            return new UtenteEntity(in);
        }

        public UtenteEntity[] newArray(int size) {
            return new UtenteEntity[size];
        }
    };
}
