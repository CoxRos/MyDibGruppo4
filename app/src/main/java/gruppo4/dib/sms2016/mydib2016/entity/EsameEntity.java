package gruppo4.dib.sms2016.mydib2016.entity;

public class EsameEntity {

    String nome, cfu , voto , data;

    public EsameEntity(String nome, String cfu, String voto, String data) {
        this.nome = nome;
        this.cfu = cfu;
        this.voto = voto;
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCfu() {
        return cfu;
    }

    public void setCfu(String cfu) {
        this.cfu = cfu;
    }

    public String getVoto() {
        return voto;
    }

    public void setVoto(String voto) {
        this.voto = voto;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
