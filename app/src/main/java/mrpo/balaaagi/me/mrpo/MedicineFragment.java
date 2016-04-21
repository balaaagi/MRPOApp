package mrpo.balaaagi.me.mrpo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class MedicineFragment extends Fragment {
    private final String LOG_TAG=MedicineFragment.class.getSimpleName();
//    ArrayAdapter<String> climateAdapter;
    ArrayList<MedicineDetails> arrayOfMedicines=new ArrayList<>();
    MedicineAdapter medicineadapter;
    public MedicineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public  void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.forecastfragment,menu);
    }

    @Override
    public void onStart(){
        super.onStart();
        updateweather();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.action_refresh){
            updateweather();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void updateweather() {
        FetchWeatherTask weatherTask=new FetchWeatherTask();
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location=prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_defaultValue));
        String units=prefs.getString("measurement_unit","metric");
        weatherTask.execute("600096","Farenheit");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_main, container, false);
        ArrayList<String> fakeClimateData=new ArrayList<String>();
        MedicineDetails fakeMedicines=new MedicineDetails("Medicine Name");
        arrayOfMedicines.add(fakeMedicines);
        fakeClimateData.add("Medicine Name  - Date of Prescription");
        fakeClimateData.add("Medicine Name - Date of Prescription");
        fakeClimateData.add("Medicine Name - Date of Prescription");
        medicineadapter=new MedicineAdapter(getActivity(),arrayOfMedicines);
//        climateAdapter=new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_prescription_textview,fakeClimateData);
        final ListView climateListView = (ListView) rootView.findViewById(R.id.listview_precriptions);
//        )
        climateListView.setAdapter(medicineadapter);

        climateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String forecast=climateAdapter.getItem(i);
                MedicineDetails singleMedicine= (MedicineDetails) climateListView.getItemAtPosition(i);
                System.out.println("Before Details"+singleMedicine.getName());
                Intent detailIntent=new Intent(getActivity(),DetailActivity.class);
                Bundle b=new Bundle();
                b.putParcelable("MedicineDetails", singleMedicine);
                detailIntent.putExtras(b);
//                detailIntent.putExtra("MedicineDetails", (Parcelable) singleMedicine);
                startActivity(detailIntent);

//                Toast.makeText(getActivity(),"This Item is clickd"+forecast,Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
    public class FetchWeatherTask extends AsyncTask<String,Void,ArrayList<MedicineDetails>>{
        private final String LOG_TAG=FetchWeatherTask.class.getSimpleName();
        private ArrayList<MedicineDetails> getdataFromJson(String jsonFromReader, int countOfDays) throws JSONException {

            JSONArray tempMedicinesArray=new JSONArray(jsonFromReader);
            ArrayList<MedicineDetails> medicinesData=new ArrayList<>();
//            MedicineDetails[] medicinesData = new MedicineDetails[tempMedicinesArray.length()];
            for(int i=0;i<tempMedicinesArray.length();i++){
                JSONObject singleMedicinePrescription=tempMedicinesArray.getJSONObject(i);
                String medicine_name=singleMedicinePrescription.getString("medicine");
                String days=singleMedicinePrescription.getString("days");
                Object daysInfo=singleMedicinePrescription.get("partsofdays");
                String dateOfPrescription=singleMedicinePrescription.getString("dateofprescribe");

                SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy");
                String formattedDateOfPrescription=null;
                try {

                    Date date = formatter.parse(dateOfPrescription);
                    Calendar cal=Calendar.getInstance();
                    cal.setTime(date);
                    formattedDateOfPrescription=cal.get(Calendar.DATE)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.YEAR);
                    System.out.println(formattedDateOfPrescription);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String foodBefore="after";
                if(singleMedicinePrescription.has("foodBefore"))
                    foodBefore=singleMedicinePrescription.getString("foodBefore");
                MedicineDetails currentPatientMedicalDetails=new MedicineDetails(medicine_name,days,daysInfo,foodBefore,formattedDateOfPrescription);
                Log.d(LOG_TAG,"index "+i);
                medicinesData.add(currentPatientMedicalDetails);
//                medicinesData[i]=currentPatientMedicalDetails;
            }
            return medicinesData;
        }

        @Override
        protected void onPostExecute(ArrayList<MedicineDetails> medicines) {
            if(medicines!=null){
//                arrayOfMedicines=medicines;

                arrayOfMedicines.clear();
                for(Object medicine:medicines){
                    arrayOfMedicines.add((MedicineDetails) medicine);
                }
                Toast.makeText(getActivity(),"Prescriptions Synced Successfully!"+arrayOfMedicines.size(),Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(),"No Network Try again!",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected ArrayList<MedicineDetails> doInBackground(String... strings) {
                                HttpURLConnection urlConnection=null;
                    BufferedReader reader=null;
                    String jsonFromReader=null;
            String format="json";
            String units=strings[1];
            int countOfDays=7;
            String[] weatherDataFromJSON=null;
                    try{
                        final String BASE_URL="http://labs.balaaagi.me:6200/getPrescriptions/SA1234";
                        final String QUERY_PARAM="q";
                        final String FORMAT_PARAM="mode";
                        final String UNITS_PARAM="units";
                        final String DAYS_PARAM="cnt";

                        ConnectivityManager cmgr=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo ntwInfo=cmgr.getActiveNetworkInfo();
                        if(ntwInfo!=null && ntwInfo.isConnected()){
                            URL url=new URL(BASE_URL.toString());
                            urlConnection= (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("GET");
                            urlConnection.connect();
                            InputStream inputStream=urlConnection.getInputStream();
                            StringBuffer buffer=new StringBuffer();
                            if(inputStream==null){
                                System.out.println("Stream is null");
                                return null;

                            }
                            reader=new BufferedReader(new InputStreamReader(inputStream));
                            String line;
                            while((line=reader.readLine())!=null){
                                buffer.append(line);
                            }
                            if(buffer.length()==0){
                                return null;

                            }
                            jsonFromReader=buffer.toString();
                        }


                    } catch (MalformedURLException e) {
                        Log.e(LOG_TAG,"error",e);
                    } catch (IOException e) {
                        Log.e(LOG_TAG,"error",e);
                    } finally {
                        if(urlConnection!=null){
                            urlConnection.disconnect();
                        }
                        if(reader!=null){
                            try {
                                reader.close();
                            } catch (IOException e) {
                                Log.e(LOG_TAG,"error while closing Stream",e);
                            }
                        }
                    }

            try {
                if(jsonFromReader!=null)
                    return getdataFromJson(jsonFromReader,countOfDays);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }




    }

}
