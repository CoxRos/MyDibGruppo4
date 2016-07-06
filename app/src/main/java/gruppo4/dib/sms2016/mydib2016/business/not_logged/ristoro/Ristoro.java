package gruppo4.dib.sms2016.mydib2016.business.not_logged.ristoro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gruppo4.dib.sms2016.mydib2016.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Ristoro extends Fragment {


    public Ristoro() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ristoro, container, false);

        CustomMapFragment mapFragment = new CustomMapFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.map_container, mapFragment).commit();
        return rootView;

    }

}
