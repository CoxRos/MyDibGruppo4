package gruppo4.dib.sms2016.mydib2016.business.logged.sharing;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.entity.NotaEntity;


public class NotesAdapter extends ArrayAdapter<NotaEntity> {

    private final int NEW_LAYOUT_RESOURCE;
    ProgressDialog progressDialog;

    public NotesAdapter(final Context context, final int NEW_LAYOUT_RESOURCE) {
        super(context, 0);
        this.NEW_LAYOUT_RESOURCE = NEW_LAYOUT_RESOURCE;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // We need to get the best view (re-used if possible) and then
        // retrieve its corresponding ViewHolder, which optimizes lookup efficiency
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final NotaEntity entry = getItem(position);

        viewHolder.titolo.setText(entry.getTitolo());
        viewHolder.data.setText(entry.getData());
        viewHolder.descrizione.setText(entry.getDescrizione());
        viewHolder.autore.setText(entry.getAutore());

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DownloadImage().execute(entry.getUrl(), entry.getTitolo());

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


            viewHolder.titolo = (TextView) workingView.findViewById(R.id.nomeAppunti);
            viewHolder.descrizione = (TextView) workingView.findViewById(R.id.descrAppunti);
            viewHolder.autore = (TextView) workingView.findViewById(R.id.autoreAppunti);
            viewHolder.data = (TextView) workingView.findViewById(R.id.dataAppunti);

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
        public TextView titolo;
        public TextView descrizione;
        public TextView autore;
        public TextView data;
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        String titolo, estensione;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(getContext());
            // Set progressdialog title
            progressDialog.setTitle("Download...");
            // Set progressdialog message
            progressDialog.setMessage(getContext().getResources().getString(R.string.progress_titolo));
            progressDialog.setIndeterminate(false);
            // Show progressdialog
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            String imageURL = params[0];
            titolo = params[1];
            estensione = getEstensione(imageURL);

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            saveImage(result, titolo, estensione);
            progressDialog.dismiss();
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.nota),
                    Toast.LENGTH_LONG).show();
        }

        private void saveImage(Bitmap finalBitmap, String titolo, String estensione) {

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/saved_images");
            myDir.mkdirs();

            String fname = titolo + estensione;
            File file = new File(myDir, fname);
            if (file.exists()) file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String getEstensione(String percorso) {
            String estensione = ".";

            for (int i = percorso.length(); i >= 0; i--) {
                char lettera = percorso.charAt(i - 1);
                if (lettera == '.') {
                    estensione = estensione + percorso.substring(i, percorso.length());
                    break;
                }
            }

            return estensione;
        }

    }
}
