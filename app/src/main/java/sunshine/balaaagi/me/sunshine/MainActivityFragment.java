package sunshine.balaaagi.me.sunshine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_main, container, false);
        ArrayList<String> fakeClimateData=new ArrayList<String>();
        fakeClimateData.add("Today- Sunny - 88/63");
        fakeClimateData.add("Tommorow- Sunny -88/99");
        fakeClimateData.add("Wednesday- Sunny -88/99");
        fakeClimateData.add("Thursday- Sunny -88/99");
        fakeClimateData.add("Friday- Sunny -88/99");
        fakeClimateData.add("Saturday- Sunny -88/99");
        fakeClimateData.add("Monday- Sunny - 88/63");
        fakeClimateData.add("Tuesday- Sunny -88/99");
        fakeClimateData.add("Wednesday- Sunny -88/99");
        fakeClimateData.add("Thursday- Sunny -88/99");
        fakeClimateData.add("Friday- Sunny -88/99");
        fakeClimateData.add("Saturday- Sunny -88/99");
        ArrayAdapter<String> climateAdapter=new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_texview,fakeClimateData);
        ListView climateListView = (ListView) rootView.findViewById(R.id.listview_forescast);
        climateListView.setAdapter(climateAdapter);

        return rootView;
    }
}
