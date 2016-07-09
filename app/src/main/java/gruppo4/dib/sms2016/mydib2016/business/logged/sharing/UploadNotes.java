package gruppo4.dib.sms2016.mydib2016.business.logged.sharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.Autenticazione.Login;
import gruppo4.dib.sms2016.mydib2016.utility.Costants;

public class UploadNotes extends AppCompatActivity {

    EditText titolaNota,descriviNota;
    Button carica;

    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;
    private String upLoadServerUri = null;
    private String imagepath = null;

    Login credenziali = new Login();
    static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadNotes.this, Sharing.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        credenziali.getEmail(getApplicationContext());
        email = credenziali.email;

        titolaNota = (EditText) findViewById(R.id.titolaNota);
        descriviNota = (EditText) findViewById(R.id.descriviNota);
        carica = (Button) findViewById(R.id.btnCarica);

        carica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titolaNota.getText().toString().length() > 0 && descriviNota.getText().toString().length() > 0) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Completa l'operazione"), 1);
                }
            }
        });

        upLoadServerUri = Costants.URL_UPLOAD_FILE;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UploadNotes.this, Sharing.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();
            imagepath = getPath(selectedImageUri);

            dialog = ProgressDialog.show(UploadNotes.this, "", "Caricamento sulla piattaforma...", true);
            new Thread(new Runnable() {
                public void run() {
                    uploadFile(imagepath,titolaNota.getText().toString(),descriviNota.getText().toString(), email);
                }
            }).start();
        }
    }

    private String getPath(Uri selectedImageUri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private int uploadFile(String sourceFileUri, final String titoloNota, String descrizione, String email) {
        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"+imagepath);

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(UploadNotes.this, getResources().getString(R.string.nota_errore),
                            Toast.LENGTH_LONG).show();
                }
            });

            return 0;

        }
        else
        {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                String cavolo = changeName(titoloNota,fileName);
                cavolo = addDescription(descrizione,cavolo);
                cavolo = addEmail(email,cavolo);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + cavolo + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {
                            String msg = "Appunti caricati con successo.";
                            Toast.makeText(UploadNotes.this, msg, Toast.LENGTH_LONG).show();
                            System.out.println("Sto stampando");
                            titolaNota.setText("");
                            descriviNota.setText("");
                        }
                    });
                    /*Intent intent = new Intent(UploadNotes.this, Sharing.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                */
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(UploadNotes.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(UploadNotes.this, "Errore nel caricamento del file.", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server", "Exception : "  + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;
        }
    }

    private String changeName(String parola, String percorso) {
        String percorsoResult = percorso;
        String estensione = ".";
        for(int i= percorso.length(); i>= 0; i--) {
            char lettera = percorso.charAt(i-1);
            if(lettera == '.') {
                estensione = estensione + percorsoResult.substring(i,percorso.length());
                percorsoResult = percorsoResult.substring(0,i-1);
                i--;
            } else if(lettera == '/') {
                percorsoResult = percorsoResult.substring(0,i);
                percorsoResult = percorsoResult + parola + estensione;
                break;
            }
        }
        return  percorsoResult;

    }

    private String addDescription(String parola, String percorso) {
        return percorso + "**??**??" + parola;
    }
    private String addEmail(String parola,String percorso) {
        return percorso + "$$^^$$^" + parola;
    }

}
