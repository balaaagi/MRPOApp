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
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    private final String LOG_TAG=ForecastFragment.class.getSimpleName();
    ArrayAdapter<String> climateAdapter;
    public ForecastFragment() {
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

        fakeClimateData.add("Medicine Name - Date of Prescription");
        fakeClimateData.add("Medicine Name - Date of Prescription");
        fakeClimateData.add("Medicine Name - Date of Prescription");
        climateAdapter=new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_texview,fakeClimateData);
        final ListView climateListView = (ListView) rootView.findViewById(R.id.listview_forescast);
//        )
        climateListView.setAdapter(climateAdapter);
        climateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String forecast=climateAdapter.getItem(i);

                Intent detailIntent=new Intent(getActivity(),DetailActivity.class);
                detailIntent.putExtra(Intent.EXTRA_TEXT,forecast);
                startActivity(detailIntent);

                Toast.makeText(getActivity(),"This Item is clickd"+forecast,Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
    public class FetchWeatherTask extends AsyncTask<String,Void,String[]>{
        private final String LOG_TAG=FetchWeatherTask.class.getSimpleName();
        private String[] getdataFromJson(String jsonFromReader, int countOfDays) throws JSONException {
//            String[] weatherData = new String[countOfDays];
//            JSONObject forecastJSON=new JSONObject(jsonFromReader);
//            JSONArray tempforecastArray=forecastJSON.getJSONArray("list");
            JSONArray tempMedicinesArray=new JSONArray(jsonFromReader);
            String[] weatherData = new String[tempMedicinesArray.length()];
            for(int i=0;i<tempMedicinesArray.length();i++){
                JSONObject singleDayForeCast=tempMedicinesArray.getJSONObject(i);
                String Medicine_name=singleDayForeCast.getString("medicine");
                String days=singleDayForeCast.getString("days");
//                String[] morning=singleDayForeCast..getString("morning");
                System.out.println(singleDayForeCast.get("partsofdays"));
//                JSONArray partsofdays=singleDayForeCast.getJSONArray("partsofdays");
//                for(int j=0;i<partsofdays.length();j++){
//                    System.out.println(partsofdays.get(j));
//                }
//                JSONArray weatherDetails=singleDayForeCast.getJSONArray("weather");
//                JSONObject temperatureDetails=singleDayForeCast.getJSONObject("temp");


//                SimpleDateFormat dateFormat=new SimpleDateFormat("EEE MMM dd");
//
//                String date=dateFormat.format(singleDayForeCast.getLong("dt")*1000);
//                String highTemperature=String.valueOf(Math.round(temperatureDetails.getDouble("max")));
//                String lowTemperature=String.valueOf(Math.round(temperatureDetails.getDouble("min")));
//
//                String forecastDescripton=weatherDetails.getJSONObject(0).getString("description");
                Log.d(LOG_TAG,"index "+i);
                weatherData[i]=Medicine_name+"%"+days;
//                weatherData[i]=date+"-"+forecastDescripton+"-"+highTemperature+"/"+lowTemperature;
                Log.d(LOG_TAG,weatherData[i]);
            }
            return weatherData;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            Log.d(LOG_TAG,"Before Loop");
//            System.out.println(strings.length);
            if(strings!=null){
                climateAdapter.clear();
                for(String forecast:strings){
                    Log.d(LOG_TAG,"Inside Post Execute"+" "+forecast);
                    climateAdapter.add(forecast);
                }
            }else{
                Toast.makeText(getActivity(),"No Network Try again!",Toast.LENGTH_LONG).show();
            }
            Log.d(LOG_TAG,"After Loop");
        }

        @Override
        protected String[] doInBackground(String... strings) {
                                HttpURLConnection urlConnection=null;
                    BufferedReader reader=null;
                    String jsonFromReader=null;
            String format="json";
            String units=strings[1];
            int countOfDays=7;
            String[] weatherDataFromJSON=null;
                    try{
//                        http://localhost:3000/getPrescriptions/SA123
                        final String BASE_URL="http://labs.balaaagi.me:6200/getPrescriptions/SA1234";
                        final String QUERY_PARAM="q";
                        final String FORMAT_PARAM="mode";
                        final String UNITS_PARAM="units";
                        final String DAYS_PARAM="cnt";
//                        Uri buildUri=Uri.parse(BASE_URL).buildUpon().build();
//                                .buildUpon()
//                                .appendQueryParameter(QUERY_PARAM,strings[0])
//                                .appendQueryParameter(FORMAT_PARAM,format)
//                                .appendQueryParameter(UNITS_PARAM,units)
//                                .appendQueryParameter(DAYS_PARAM,Integer.toString(countOfDays))
//                                .appendQueryParameter("APPID",BuildConfig.OPEN_WEATHER_MAP_API_KEY)
//                                .build();
//                        String apiKey="&APPID=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY;

                        ConnectivityManager cmgr=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo ntwInfo=cmgr.getActiveNetworkInfo();
                        if(ntwInfo!=null && ntwInfo.isConnected()){
                            URL url=new URL(BASE_URL.toString());
//                        Log.v(LOG_TAG,"Build URI" + buildUri.toString());
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
//                        System.out.println(reader);
                            String line;
                            while((line=reader.readLine())!=null){
                                buffer.append(line);
                            }
                            if(buffer.length()==0){
                                return null;

                            }
                            System.out.println("Before Tro String");
                            jsonFromReader=buffer.toString();
                            System.out.println("After Tro String");
                            countOfDays=buffer.length();
                        }


//                        weatherDataFromJSON=getdataFromJson(jsonFromReader,countOfDays);
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
//            Log.d("Json",jsonFromReader);

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
