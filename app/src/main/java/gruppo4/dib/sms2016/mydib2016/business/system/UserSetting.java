package gruppo4.dib.sms2016.mydib2016.business.system;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.business.Autenticazione.Login;
import gruppo4.dib.sms2016.mydib2016.business.homepage.HomePage;

public class UserSetting extends PreferenceActivity {

    Login credenziali = new Login();
    static boolean logged;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        credenziali.getLogged(this);
        logged = credenziali.logged;

        AppBarLayout bar;

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        bar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.toolbar_settings, root, false);
        root.addView(bar, 0);

        Toolbar Tbar = (Toolbar) bar.getChildAt(0);

        Tbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSetting.this, HomePage.class);

                if(logged) {
                    intent.putExtra("goTo",1);
                }
                else {
                    intent.putExtra("goTo", 0);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserSetting.this, HomePage.class);

        if(logged) {
            intent.putExtra("goTo",1);
        }
        else {
            intent.putExtra("goTo", 0);
        }
        startActivity(intent);
    }
}
