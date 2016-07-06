package gruppo4.dib.sms2016.mydib2016.business.not_logged.ristoro;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import gruppo4.dib.sms2016.mydib2016.R;
import gruppo4.dib.sms2016.mydib2016.entity.LocationEntity;

/**
 * Created by Utente on 06/07/2016.
 */
public class CustomMapFragment extends com.google.android.gms.maps.SupportMapFragment {
    private List<LocationEntity> locations;

    private final LatLng POLI = new LatLng(41.107986, 16.880539);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BitmapDescriptor stellaIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_credits);
        BitmapDescriptor stampaIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_stampa);
        BitmapDescriptor marketIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_shopping_cart_black_18dp);
        BitmapDescriptor foodIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_local_dining_black_18dp);
        BitmapDescriptor musicIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_audiotrack_black_18dp);
        BitmapDescriptor strutturaIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_dirigente);
        BitmapDescriptor drinkIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_local_bar_black_18dp);

        locations = new ArrayList<LocationEntity>();
        locations.add(new LocationEntity(new LatLng(41.107986, 16.880539),"Politecnico di Bari","Sede del POLIBA",stellaIcon));
        locations.add(new LocationEntity(new LatLng(41.107560,16.877673),"Caffetteria BarFly","Caffetteria e luogo\ndi incontri per gli\nstudenti che\nfesteggiano le\nloro lauree\nTelefono:080 556 7175",drinkIcon));
        locations.add(new LocationEntity(new LatLng(41.112577, 16.876300),"Tatò Padide S.P.A","Negozio di alimentari\na prezzi modesti.\nTelefono:080 542 9901",marketIcon));
        locations.add(new LocationEntity(new LatLng(41.113002, 16.876279),"Pizzamania ","Pizzeria Pizzamania\ndi Ruccia Vincenzo.\nTelefono:080 542 5036",foodIcon));
        locations.add(new LocationEntity(new LatLng(41.110652,16.876113),"Garden ","Ristopizzeria\ndal Garden.\nTelefono:080 964 0021",foodIcon));
        locations.add(new LocationEntity(new LatLng(41.105060,16.873630),"Gorgeous ","Gorgeous food\n& fun.\nTelefono:080 542 3399",musicIcon));
        locations.add(new LocationEntity(new LatLng(41.108420,16.877200),"Pixel ","Copisteria e negozio\ndi computer\nTelefono:080 556 0007\nSito web: pixelsrl.it",stampaIcon));
        locations.add(new LocationEntity(new LatLng(41.112577, 16.876300),"Centanni","Negozio di alimentari\ndi Costantino.\nTelefono:080 557 5415",marketIcon));
        locations.add(new LocationEntity(new LatLng(41.105776, 16.879522),"Adisu","Supporto della\nregione per gli\nstudenti universitari.\nTelefono:080 543 8111\nSito web:adisupuglia.it",strutturaIcon));

        GoogleMap googleMap = getMap();

        for(LocationEntity l : locations) {
            googleMap.addMarker(new MarkerOptions().position(l.getPosizione()).title(l.getLuogo()).
                    icon(l.getIcona()).snippet(l.getDescrizione()));
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(POLI, 200));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
    }

}
