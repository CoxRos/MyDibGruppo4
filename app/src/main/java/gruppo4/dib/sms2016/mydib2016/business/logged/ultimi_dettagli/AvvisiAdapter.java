package gruppo4.dib.sms2016.mydib2016.business.logged.ultimi_dettagli;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.entity.AvvisiEntity;

public class AvvisiAdapter extends ArrayAdapter<AvvisiEntity> {

    private final int NEW_LAYOUT_RESOURCE;

    public AvvisiAdapter(final Context context, final int NEW_LAYOUT_RESOURCE) {
        super(context, 0);
        this.NEW_LAYOUT_RESOURCE = NEW_LAYOUT_RESOURCE;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {


        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        AvvisiEntity newsItem = getItem(position);

        viewHolder.titolo.setText(newsItem.getTitolo());
        viewHolder.descrizione.setText(newsItem.getDescrizione());
        viewHolder.data.setText(newsItem.getData());

        return view;
    }

    private View getWorkingView(final View convertView) {

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

        final Object tag = workingView.getTag();
        ViewHolder viewHolder = null;


        if (null == tag || !(tag instanceof ViewHolder)) {
            viewHolder = new ViewHolder();

            viewHolder.titolo = (TextView)workingView.findViewById(R.id.text_cardnews_titolo);
            viewHolder.descrizione = (TextView)workingView.findViewById(R.id.text_cardnews_descrizione);
            viewHolder.data = (TextView)workingView.findViewById(R.id.text_cardnews_data);

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

    private static class ViewHolder {
        public TextView titolo, descrizione, data;
    }
}
