package gruppo4.dib.sms2016.mydib2016.DataAccessObject;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import gruppo4.dib.sms2016.mydib2016.entity.EsameEntity;
import gruppo4.dib.sms2016.mydib2016.utility.Costants;

/**
 * Created by sergiocorvino on 03/07/16.
 */
public class DAOLibretto {

    private DatabaseHelper db;

    public  DAOLibretto(Context context) {
        db = new DatabaseHelper(context);
    }

    public List<EsameEntity> getEsami() {
        List<EsameEntity> result = new ArrayList<EsameEntity>();

        Cursor resultSet = db.getAllData(Costants.TABLE_LIBRETTO);

        while (resultSet.moveToNext()) {
            EsameEntity esame = new EsameEntity(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
            result.add(esame);
        }
        return result;
    }

    public boolean insertEsame(String materia, String cfu, String voto, String data) {

        List<Coppia> param = new ArrayList<Coppia>();
        param.add(new Coppia("materia", materia));
        param.add(new Coppia("CFU", cfu));
        param.add(new Coppia("voto", voto));
        param.add(new Coppia("data", data));

        return db.insertData(param, Costants.TABLE_LIBRETTO);
    }

    public boolean updateEsame(String materia, String cfu, String voto, String data, String holderMat) {

        List<Coppia> param = new ArrayList<Coppia>();
        param.add(new Coppia("materia", materia));
        param.add(new Coppia("CFU", cfu));
        param.add(new Coppia("voto", voto));
        param.add(new Coppia("data", data));

        return db.updateData(param, Costants.TABLE_LIBRETTO, Costants.CAMPO_MATERIA, holderMat);
    }

    public boolean deleteEsame(String materia) {
        return db.deleteData(Costants.TABLE_LIBRETTO, Costants.CAMPO_MATERIA, materia);
    }

    public boolean deleteAllExams() {
        return db.deleteAllData(Costants.TABLE_LIBRETTO);
    }

    public EsameEntity getEsame(String materia) {
        EsameEntity esame = null;
        Cursor resultSet = db.selectData(Costants.TABLE_LIBRETTO, materia);

        if(resultSet.moveToNext()) {
            esame = new EsameEntity(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
        }

        return esame;
    }
}
