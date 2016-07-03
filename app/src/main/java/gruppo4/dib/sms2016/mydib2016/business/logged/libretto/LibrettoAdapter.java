package gruppo4.dib.sms2016.mydib2016.business.logged.libretto;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.entity.EsameEntity;

public class LibrettoAdapter extends ArrayAdapter<EsameEntity> {

    private final int NEW_LAYOUT_RESOURCE;

    public LibrettoAdapter(final Context context, final int NEW_LAYOUT_RESOURCE) {
        super(context, 0);
        this.NEW_LAYOUT_RESOURCE = NEW_LAYOUT_RESOURCE;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // We need to get the best view (re-used if possible) and then
        // retrieve its corresponding ViewHolder, which optimizes lookup efficiency
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final EsameEntity entry = getItem(position);

        if(entry.getVoto().equalsIgnoreCase("IDO")) {
            viewHolder.scalaVoto.setBackgroundResource(R.mipmap.greenline);
        } else if (Integer.parseInt(entry.getVoto()) < 20) { //Pallino rosso
            viewHolder.scalaVoto.setBackgroundResource(R.mipmap.redline);
        } else if(Integer.parseInt(entry.getVoto()) < 24){ //Pallino arancione
            viewHolder.scalaVoto.setBackgroundResource(R.mipmap.orangeline);
        } else if(Integer.parseInt(entry.getVoto()) < 27) { //Pallino giallo
            viewHolder.scalaVoto.setBackgroundResource(R.mipmap.yellowline);
        } else if(Integer.parseInt(entry.getVoto()) >= 27) { //Pallino verde
            viewHolder.scalaVoto.setBackgroundResource(R.mipmap.greenline);
        }

        viewHolder.cfu.setText(entry.getCfu());
        viewHolder.data.setText(entry.getData());
        viewHolder.nome.setText(entry.getNome());
        viewHolder.voto.setText(entry.getVoto());

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),EsameActivity.class);
                intent.putExtra("esame",entry.getNome());
                getContext().startActivity(intent);
            }
        });

        return view;
    }

    private View getWorkingView(final View convertView) {
        // The workingView is basically just the convertView re-used if possible
        // or inflated new if not possible
        View workingView = null;

        if (null == convertView) {
            final Context context = getContext();
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService
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


        if (null == tag || !(tag instanceof ViewHolder)) {
            viewHolder = new ViewHolder();

            viewHolder.scalaVoto = (ImageView) workingView.findViewById(R.id.scalaVoto);
            viewHolder.nome = (TextView) workingView.findViewById(R.id.nomeEsame);
            viewHolder.voto = (TextView) workingView.findViewById(R.id.votoEsame);
            viewHolder.cfu = (TextView) workingView.findViewById(R.id.cfuEsame);
            viewHolder.data = (TextView) workingView.findViewById(R.id.dataEsame);

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
        public ImageView scalaVoto;
        public TextView nome;
        public TextView cfu;
        public TextView voto;
        public TextView data;
    }
}
