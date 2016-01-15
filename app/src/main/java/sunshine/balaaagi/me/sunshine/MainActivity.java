package sunshine.balaaagi.me.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    String errorMessage="No NetWork";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        ConnectivityManager cMgr= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo=cMgr.getActiveNetworkInfo();
//
//        if(networkInfo!=null&&networkInfo.isConnected()){
//            String mee="NEtwork is available";
//            Toast.makeText(MainActivity.this,"Netwrok is there", Toast.LENGTH_SHORT).show();
//
//        }else{
//            errorMessage="No NetWork";
//            Toast.makeText(MainActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
//        }



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
