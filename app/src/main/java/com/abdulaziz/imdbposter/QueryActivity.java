package com.abdulaziz.imdbposter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class QueryActivity extends AppCompatActivity {

    private Context mContext;
    private EditText mQuery;
    private Button mSearch;
    private String mMovie;
    private BroadcastReceiver receiver;
    IntentFilter filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        mContext = getApplicationContext();
        if (!Helper.isDeviceOnline(mContext)) {
            Toast.makeText(mContext,
                    "Your mobile is not connected to internet. Closing the App",
                    Toast.LENGTH_SHORT).show();
            AlertDialog.Builder mBuilder= new AlertDialog.Builder(mContext)
                                    .setTitle("No Internet")
                                    .setMessage("You have no internet" +
                                            "connection")
                                    .setPositiveButton("Open Wifi", new DialogInterface.OnClickListener(){

                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    });
            mBuilder.show();
        }
        setupUIElements();
        filter = new IntentFilter(MovieReceiver.SHOW_IMAGE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MovieReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume(){
        super.onResume();try {
            registerReceiver(receiver,filter);
        }catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();try {
            unregisterReceiver(receiver);
        }catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        try {
            unregisterReceiver(receiver);
        }catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }



    private void setupUIElements() {
        mSearch = (Button) findViewById(R.id.search);
        mQuery = (EditText) findViewById(R.id.movie_query);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMovie = mQuery.getText().toString();
                if(mMovie.equals("")){
                    Toast.makeText(mContext,
                            "Movie empty. Please enter a movie name",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent serviceIntent = new Intent(mContext, GetMovieService.class);
                    serviceIntent.putExtra(GetMovieService.MOVIE_PARAM, mMovie);
                    startService(serviceIntent);
                    mSearch.setEnabled(false);
                }
            }
        });
    }
}
