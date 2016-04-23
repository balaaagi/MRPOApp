package mrpo.balaaagi.me.mrpo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private static final String MRPO_HASHTAG=" #MRPO";
    private String mForecastStr;
    TextView medicineName,noOfDays;
    ImageView morning,noon,evening;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        Intent intent=getActivity().getIntent();

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        if(intent!=null){
            medicineName= (TextView) rootView.findViewById(R.id.medicine_name);
            noOfDays= (TextView) rootView.findViewById(R.id.daysCount);
            morning= (ImageView) rootView.findViewById(R.id.morningCheck);
            noon= (ImageView) rootView.findViewById(R.id.noonCheck);
            evening= (ImageView) rootView.findViewById(R.id.eveningCheck);

            MedicineDetails currentMedicine= (MedicineDetails) intent.getParcelableExtra("MedicineDetails");
            System.out.println(currentMedicine.getName());
            medicineName.setText(currentMedicine.getName());

            noOfDays.setText(String.valueOf(currentMedicine.getNoOfDays()) +" days");
            if(currentMedicine.morning)
                morning.setImageResource(R.drawable.check_mark);

            if(currentMedicine.noon)
                noon.setImageResource(R.drawable.check_mark);

            if(currentMedicine.evening)
                evening.setImageResource(R.drawable.check_mark);
        }
        return rootView;
    }

    private Intent createShareIntent(){
        Intent shareIntent=new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.setType("text/plain");
        String shareMessageText="My Doctor has advised me to take "+medicineName.getText()+" for "+noOfDays.getText()+".I have set a reminder for the same via MRPO app";
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                shareMessageText+MRPO_HASHTAG);
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
