package gruppo4.dib.sms2016.mydib2016.business.logged.ricerca;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.logged.libretto.EsameActivity;
import gruppo4.dib.sms2016.mydib2016.business.logged.profilo.Profilo;
import gruppo4.dib.sms2016.mydib2016.entity.UtenteEntity;


public class RicercaAdapter extends ArrayAdapter<UtenteEntity> {

    private final int NEW_LAYOUT_RESOURCE;

    public RicercaAdapter(final Context context, final int NEW_LAYOUT_RESOURCE) {
        super(context, 0);
        this.NEW_LAYOUT_RESOURCE = NEW_LAYOUT_RESOURCE;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // We need to get the best view (re-used if possible) and then
        // retrieve its corresponding ViewHolder, which optimizes lookup efficiency
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final UtenteEntity entry = getItem(position);

        viewHolder.nomeRicercatoList.setText(entry.getNome());

        viewHolder.cognomeRicercatoList.setText(entry.getCognome());

        viewHolder.tipoRicercatoList.setText(entry.getTipo());
        if(entry.getTipo().equalsIgnoreCase("Studente")) {
            viewHolder.tipoRicercatoList.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.mipmap.ic_libretto),null,null,null);
        } else if(entry.getTipo().equalsIgnoreCase("Docente")) {
            viewHolder.tipoRicercatoList.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.mipmap.ic_docente),null,null,null);
        } else if(entry.getTipo().equalsIgnoreCase("Dirigente")) {
            viewHolder.tipoRicercatoList.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.mipmap.ic_dirigente),null,null,null);
        }

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Profilo.class);
                intent.putExtra("utente",new UtenteEntity(entry.getNome(),entry.getCognome(),entry.getTipo(),entry.getEmail(),entry.getTelefono(),entry.getRicevimento(),entry.getWeb()));
                getContext().startActivity(intent);
            }
        });

        return view;
    }

    private View getWorkingView(final View convertView) {
        // The workingView is basically just the convertView re-used if possible
        // or inflated new if not possible
        View workingView = null;

        if(null == convertView) {
            final Context context = getContext();
            final LayoutInflater inflater = (LayoutInflater)context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            workingView = inflater.inflate(NEW_LAYOUT_RESOURCE, null);
        } else {
            workingView = convertView;
        }

        return workingView;
    }

    private ViewHolder getViewHolder(final View workingView) {
        // The viewHolder allows us to avoid re-looking up view references
        // Since views are recycled, these references will never change
        final Object tag = workingView.getTag();
        ViewHolder viewHolder = null;


        if(null == tag || !(tag instanceof ViewHolder)) {
            viewHolder = new ViewHolder();

            viewHolder.nomeRicercatoList = (TextView) workingView.findViewById(R.id.nomeRicercatoView);
            viewHolder.cognomeRicercatoList = (TextView) workingView.findViewById(R.id.cognomeRicercatoView);
            viewHolder.tipoRicercatoList = (TextView) workingView.findViewById(R.id.tipoRicercatoView);

            workingView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) tag;
        }

        return viewHolder;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    /**
     * ViewHolder allows us to avoid re-looking up view references
     * Since views are recycled, these references will never change
     */
    private static class ViewHolder {
        public TextView nomeRicercatoList;
        public TextView cognomeRicercatoList;
        public TextView tipoRicercatoList;

    }

}
