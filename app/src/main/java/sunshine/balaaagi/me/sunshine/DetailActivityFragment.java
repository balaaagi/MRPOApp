package sunshine.balaaagi.me.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private static final String FORECAST_SHARE_HASHTAG=" #SunshineApp";
    private String mForecastStr;


    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent=getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        if(intent!=null && intent.hasExtra(intent.EXTRA_TEXT)){
            mForecastStr=intent.getStringExtra(intent.EXTRA_TEXT);
            ((TextView)rootView.findViewById(R.id.forecast_details)).setText(mForecastStr);
        }
        return rootView;
    }

    private Intent createShareIntent(){
        Intent shareIntent=new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mForecastStr+FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        inflater.inflate(R.menu.detailfragment,menu);

        MenuItem shareItem=menu.findItem(R.id.action_shareaction);

        ShareActionProvider mshareActionProvider= (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        if(mshareActionProvider!=null){
            mshareActionProvider.setShareIntent(createShareIntent());
        }else{
            Log.d("SHareAction","ShareActin is Null!");
        }
    }

}
