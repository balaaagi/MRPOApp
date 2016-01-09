package sunshine.balaaagi.me.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.text.SimpleDateFormat;
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
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.action_refresh){
            FetchWeatherTask weatherTask=new FetchWeatherTask();
            weatherTask.execute("94093");
            return true;

        }
        return super.onOptionsItemSelected(item);
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
        climateAdapter=new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_texview,fakeClimateData);
        final ListView climateListView = (ListView) rootView.findViewById(R.id.listview_forescast);
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
            String[] weatherData = new String[countOfDays];
            JSONObject forecastJSON=new JSONObject(jsonFromReader);
            JSONArray tempforecastArray=forecastJSON.getJSONArray("list");
            for(int i=0;i<tempforecastArray.length();i++){
                JSONObject singleDayForeCast=tempforecastArray.getJSONObject(i);
                JSONArray weatherDetails=singleDayForeCast.getJSONArray("weather");
                JSONObject temperatureDetails=singleDayForeCast.getJSONObject("temp");


                SimpleDateFormat dateFormat=new SimpleDateFormat("EEE MMM dd");

                String date=dateFormat.format(singleDayForeCast.getLong("dt")*1000);
                String highTemperature=String.valueOf(Math.round(temperatureDetails.getDouble("max")));
                String lowTemperature=String.valueOf(Math.round(temperatureDetails.getDouble("min")));

                String forecastDescripton=weatherDetails.getJSONObject(0).getString("description");
                weatherData[i]=date+"-"+forecastDescripton+"-"+highTemperature+"/"+lowTemperature;
                Log.d(LOG_TAG,weatherData[i]);
            }
            return weatherData;
        }

        @Override
        protected void onPostExecute(String[] strings) {

            if(strings!=null){
                climateAdapter.clear();
                for(String forecast:strings){
                    climateAdapter.add(forecast);
                }
            }
        }

        @Override
        protected String[] doInBackground(String... strings) {
                                HttpURLConnection urlConnection=null;
                    BufferedReader reader=null;
                    String jsonFromReader=null;
            String format="json";
            String units="metric";
            int countOfDays=7;
            String[] weatherDataFromJSON=null;
                    try{
                        final String BASE_URL="http://api.openweathermap.org/data/2.5/forecast/daily?";
                        final String QUERY_PARAM="q";
                        final String FORMAT_PARAM="mode";
                        final String UNITS_PARAM="units";
                        final String DAYS_PARAM="cnt";
                        Uri buildUri=Uri.parse(BASE_URL)    .buildUpon()
                                .appendQueryParameter(QUERY_PARAM,strings[0])
                                .appendQueryParameter(FORMAT_PARAM,format)
                                .appendQueryParameter(UNITS_PARAM,units)
                                .appendQueryParameter(DAYS_PARAM,Integer.toString(countOfDays))
                                .appendQueryParameter("APPID",BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                                .build();
//                        String apiKey="&APPID=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY;

                        URL url=new URL(buildUri.toString());
                        Log.v(LOG_TAG,"Build URI" + buildUri.toString());
                        urlConnection= (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.connect();
                        InputStream inputStream=urlConnection.getInputStream();
                        StringBuffer buffer=new StringBuffer();
                        if(inputStream==null){
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
            Log.d("Json",jsonFromReader);

            try {
                return getdataFromJson(jsonFromReader,countOfDays);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }




    }

}
