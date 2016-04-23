package mrpo.balaaagi.me.mrpo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

public class MainActivity extends AppCompatActivity {
    String errorMessage="No NetWork";
    private ShareActionProvider mshareActionProvider;

    @Override
    protected void onStart(){
        Log.i("LifeCycle","OnStart");
        super.onStart();
    }

    @Override
    protected void onStop(){
        Log.i("LifeCycle","OnStop");
        super.onStop();
    }

    @Override
    protected void onResume(){
        Log.i("LifeCycle","OnResume");
        super.onResume();
    }

    @Override
    protected void onPause(){
        Log.i("LifeCycle","OnPause");
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        Log.i("LifeCycle","OnDestroy");
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("LifeCycle","OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent newSettings=new Intent(this,SettingsActivity.class);
            startActivity(newSettings);
            return true;
        }




        return super.onOptionsItemSelected(item);
    }

}
