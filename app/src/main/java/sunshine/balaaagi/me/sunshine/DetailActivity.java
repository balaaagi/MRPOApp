package sunshine.balaaagi.me.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.action_settings){
            startActivity(new Intent(this,SettingsActivity.class));
        }
        if (id == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        if(id==R.id.action_mapview){
            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
            String location=prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_defaultValue));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri geolocation =Uri.parse("geo:0,0?").buildUpon()
                    .appendQueryParameter("q",location)
                    .build();
            intent.setData(geolocation);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            return true;
        }

        return true;
    }



}
