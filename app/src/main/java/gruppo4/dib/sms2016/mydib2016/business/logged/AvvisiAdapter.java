package gruppo4.dib.sms2016.mydib2016.business.logged;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gruppo4.dib.sms2016.mydib2016.R;

/**
 * Created by sergiocorvino on 03/07/16.
 */
public class AvvisiAdapter extends ArrayAdapter<Avvisi> {

    private final int NEW_LAYOUT_RESOURCE;

    public AvvisiAdapter(final Context context, final int NEW_LAYOUT_RESOURCE) {
        super(context, 0);
        this.NEW_LAYOUT_RESOURCE = NEW_LAYOUT_RESOURCE;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // We need to get the best view (re-used if possible) and then
        // retrieve its corresponding ViewHolder, which optimizes lookup efficiency
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        Avvisi newsItem = getItem(position);

        viewHolder.titolo.setText(newsItem.getTitolo());
        viewHolder.descrizione.setText(newsItem.getDescrizione());
        viewHolder.data.setText(newsItem.getData());

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

    /**
     * ViewHolder allows us to avoid re-looking up view references
     * Since views are recycled, these references will never change
     */
    private static class ViewHolder {
        public TextView titolo, descrizione, data;
    }

    /*ArrayList<Avvisi> news = new ArrayList<Avvisi>();

    public AvvisiAdapter(ArrayList<Avvisi> news) {
        this.news = news;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_news_layout, parent, false);
        NewsViewHolder newsViewHolder = new NewsViewHolder(view);

        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {

        Avvisi newsItem = news.get(position);
        holder.titolo.setText(newsItem.getTitolo());
        holder.descrizione.setText(newsItem.getDescrizione());
        holder.data.setText(newsItem.getData());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView titolo, descrizione, data;

        public NewsViewHolder(View itemView) {
            super(itemView);
            titolo = (TextView)itemView.findViewById(R.id.text_cardnews_titolo);
            descrizione = (TextView)itemView.findViewById(R.id.text_cardnews_descrizione);
            data = (TextView)itemView.findViewById(R.id.text_cardnews_data);
        }
    }*/
}
