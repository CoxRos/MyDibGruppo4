package gruppo4.dib.sms2016.mydib2016.business.logged.sharing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.homepage.HomePage;

public class Sharing extends AppCompatActivity {

    private ImageButton btnUpload, btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sharing.this, HomePage.class);
                intent.putExtra("goTo",1);
                startActivity(intent);
            }
        });

        btnUpload = (ImageButton)findViewById(R.id.btnupload);
        btnDownload = (ImageButton)findViewById(R.id.btndownload);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sharing.this,UploadNotes.class);
                startActivity(intent);
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(Sharing.this,DownloadNotes.class);
               startActivity(intent);
            }
        });
    }

}
