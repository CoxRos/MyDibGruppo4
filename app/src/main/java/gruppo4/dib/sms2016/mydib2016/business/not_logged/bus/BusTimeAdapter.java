package gruppo4.dib.sms2016.mydib2016.business.not_logged.bus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.entity.BusEntity;

public class BusTimeAdapter extends ArrayAdapter<BusEntity> {

    private final int NEW_LAYOUT_RESOURCE;

    public BusTimeAdapter(final Context context, final int NEW_LAYOUT_RESOURCE) {
        super(context, 0);
        this.NEW_LAYOUT_RESOURCE = NEW_LAYOUT_RESOURCE;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // We need to get the best view (re-used if possible) and then
        // retrieve its corresponding ViewHolder, which optimizes lookup efficiency
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final BusEntity entry = getItem(position);

        if(!entry.getNumBus().equals("")) {

            if (entry.getNumBus().equalsIgnoreCase("21")) {
                viewHolder.numbus.setImageResource(R.mipmap.ic_bus21);
            } else {
                viewHolder.numbus.setImageResource(R.mipmap.ic_bus22);
            }

            viewHolder.arrivoOrario.setText(entry.getOrarioArrivo());

            viewHolder.partenzaOrario.setText(entry.getOrarioPartenza());
        } else {
            viewHolder.separatorBus.setVisibility(View.GONE);
            viewHolder.trattinoBus.setVisibility(View.GONE);
            viewHolder.partenzaOrario.setVisibility(View.GONE);
            viewHolder.arrivoOrario.setVisibility(View.GONE);
            viewHolder.numbus.setVisibility(View.GONE);
        }
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

            viewHolder.numbus = (ImageView) workingView.findViewById(R.id.numeroBus);
            viewHolder.arrivoOrario = (TextView) workingView.findViewById(R.id.arrivoOrario);
            viewHolder.partenzaOrario = (TextView) workingView.findViewById(R.id.partenzaOrario);
            viewHolder.separatorBus = (View) workingView.findViewById(R.id.separatorBus);
            viewHolder.trattinoBus = (TextView) workingView.findViewById(R.id.trattinoBus);


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
        public ImageView numbus;
        public TextView partenzaOrario;
        public TextView arrivoOrario;
        public View separatorBus;
        public TextView trattinoBus;
    }
}
