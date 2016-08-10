package com.abdulaziz.imdbposter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MovieReceiver extends BroadcastReceiver {
    public static final String SHOW_IMAGE = "com.abdulaziz.imdbposter.SHOW_IMAGE";

    public MovieReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(MovieReceiver.SHOW_IMAGE)){
            PutData(context,intent);

        }
    }

    private void PutData(Context context,Intent intent) {
        if (intent.getStringExtra("ERROR").equals("")) {
            Intent infoIntent = new Intent(context,InfoActivity.class);
            infoIntent.putExtra("TITLE",intent.getStringExtra("TITLE"));
            infoIntent.putExtra("DATE",intent.getStringExtra("DATE"));
            infoIntent.putExtra("RATING",intent.getStringExtra("RATING"));
            infoIntent.putExtra("PLOT",intent.getStringExtra("PLOT"));
            infoIntent.putExtra("POSTER",intent.getByteArrayExtra("POSTER"));
            context.startActivity(infoIntent);



//            Log.v("TITLE", title);
//            Log.v("DATE", date);
//            Log.v("RATING", rating);
//            Log.v("PLOT", plot);
//            Log.v("BYTEARRAY", String.valueOf(posterArray.length));
        }
        else{
            Toast.makeText(context,intent.getStringExtra("ERROR"),Toast.LENGTH_SHORT).show();
        }
        Button searchButton = (Button)((Activity)context).findViewById(R.id.search);
        searchButton.setEnabled(true);
        EditText movieQuery = (EditText)((Activity)context).findViewById(R.id.movie_query);
        movieQuery.setText("");

    }
}
