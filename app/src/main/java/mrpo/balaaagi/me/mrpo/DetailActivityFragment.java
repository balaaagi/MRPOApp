package mrpo.balaaagi.me.mrpo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private static final String MRPO_HASHTAG=" #MRPO";
    private String mForecastStr;
    TextView medicineName,noOfDays;
    ImageView morning,noon,evening;
    Button reminderButton;

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
            reminderButton= (Button) rootView.findViewById(R.id.setReminderButton);
            final MedicineDetails currentMedicine= (MedicineDetails) intent.getParcelableExtra("MedicineDetails");
            System.out.println(currentMedicine.getName());
            medicineName.setText(currentMedicine.getName());

            noOfDays.setText(String.valueOf(currentMedicine.getNoOfDays()) +" days");
            if(currentMedicine.morning)
                morning.setImageResource(R.drawable.check_mark);

            if(currentMedicine.noon)
                noon.setImageResource(R.drawable.check_mark);

            if(currentMedicine.evening)
                evening.setImageResource(R.drawable.check_mark);

            reminderButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {

                    ArrayList<Integer> partOfDays=new ArrayList<Integer>();
                    HashMap<Integer,Integer> timeMap=new HashMap<Integer, Integer>();
                    timeMap.put(1,9);
                    timeMap.put(2,13);
                    timeMap.put(3,21);


                    //for Single Time of the Day Alone
                    if(currentMedicine.morning)
                        partOfDays.add(1);
                    else if(currentMedicine.noon)
                        partOfDays.add(2);
                    else if(currentMedicine.evening)
                        partOfDays.add(3);

                    for(int i=0;i<partOfDays.size();i++){
                        Calendar remindCalendar=Calendar.getInstance();
                        remindCalendar.setTimeInMillis(SystemClock.currentThreadTimeMillis());
                        remindCalendar.set(Calendar.HOUR_OF_DAY,timeMap.get(partOfDays.get(i)));
                        setReminderFor(currentMedicine.getName(),currentMedicine.getNoOfDays(),remindCalendar);
                    }

                }
            });
        }
        return rootView;
    }

    private void setReminderFor(String name, int noOfDays, Calendar remindCalendar) {
        PendingIntent pendingIntent;
        Intent myIntent=new Intent(getActivity().getApplicationContext(),MyReceivar.class);
        pendingIntent=PendingIntent.getBroadcast(getActivity().getApplicationContext(),0,myIntent,0);
        AlarmManager alarmManager= (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

//      alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,remindCalendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+60*1000,pendingIntent);
        Toast.makeText(getActivity(),"Reminder Set for "+noOfDays +"days",Toast.LENGTH_SHORT).show();
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
