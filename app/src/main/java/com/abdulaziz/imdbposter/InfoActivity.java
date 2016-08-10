package com.abdulaziz.imdbposter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {

    private TextView title;
    private TextView rating;
    private TextView date;
    private TextView plot;
    private ImageView poster;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        mContext = getApplicationContext();
        Intent receivedIntent = getIntent();
        if(receivedIntent == null){
            Toast.makeText(mContext,"Unable to show Movie Info", Toast.LENGTH_SHORT).show();
            finish();
        }
        title = (TextView)findViewById(R.id.title);
        title.setText(receivedIntent.getStringExtra("TITLE"));

        date = (TextView)findViewById(R.id.date);
        date.setText(receivedIntent.getStringExtra("DATE"));

        rating = (TextView)findViewById(R.id.rating);
        rating.setText(receivedIntent.getStringExtra("RATING"));

        plot = (TextView)findViewById(R.id.plot);
        plot.setText(receivedIntent.getStringExtra("PLOT"));

        poster = (ImageView)findViewById(R.id.poster);
        try {
            byte[] posterArray = receivedIntent.getByteArrayExtra("POSTER");

            Bitmap bmp = BitmapFactory.decodeByteArray(posterArray, 0, posterArray.length);
            poster.setImageBitmap(bmp);
        }catch(NullPointerException e){
            Toast.makeText(mContext,"Unable to fetch movie poster ", Toast.LENGTH_SHORT).show();
        }


    }
}
