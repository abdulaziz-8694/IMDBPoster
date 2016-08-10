package com.abdulaziz.imdbposter;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetMovieService extends IntentService {

    public static final String MOVIE_PARAM = "com.abdulaziz.imdbposter.extra.MOVIE";
    private String error = "";
    private String title = "";
    private String date = "";
    private String plot = "";
    private String rating = "";
    private byte[] poster = null;
    public GetMovieService() {
        super("GetMovieService");
    }





    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String mMovie = intent.getStringExtra(MOVIE_PARAM);
            Log.v("MOVIE NAME", mMovie);
            String URL = null;
            try {
                URL = Helper.API_URL + "t="+URLEncoder.encode(mMovie,"UTF-8");
                String result = getMovieData(URL);
                JSONObject jsonResult = new JSONObject(result);
                if (jsonResult.getString("Response").equals("True")) {
                    title = jsonResult.getString("Title");
                    date = jsonResult.getString("Released");
                    rating = jsonResult.getString("imdbRating");
                    plot = jsonResult.getString("Plot");
                    String POSTER_URL = jsonResult.getString("Poster");
                    Bitmap posterImage = getMoviePoster(POSTER_URL);
                    if (posterImage != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        posterImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        poster = stream.toByteArray();
                    }
                }
                else{
                     error = jsonResult.getString("Error");
                 }

            } catch (JSONException e) {
                error = "Something went wrong";
//                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                error = "Something went wrong";
            } finally{
                broadcastData();
            }
        }
    }

    private void broadcastData() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MovieReceiver.SHOW_IMAGE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra("TITLE", title);
        broadcastIntent.putExtra("DATE", date);
        broadcastIntent.putExtra("RATING", rating);
        broadcastIntent.putExtra("PLOT", plot);
        broadcastIntent.putExtra("POSTER", poster);
        broadcastIntent.putExtra("ERROR",error);
        sendBroadcast(broadcastIntent);


    }

    private String getMovieData(String url){
        URL movieURL = null;
        HttpURLConnection urlConnection = null;
        String result = "";
        try {
            movieURL = new URL(url);
            urlConnection = (HttpURLConnection) movieURL.openConnection();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            StringBuilder response = new StringBuilder();
            BufferedReader inReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = inReader.readLine()) != null){
                response.append(line);
            }
            result = response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            result = "{\"Response\": \"False\", \"Error\" : \"URL wrong\"}";


        } catch (IOException e) {
            e.printStackTrace();
            result = "{\"Response\": \"False\", \"Error\" : \"Network Error\"}";

        }
        finally {
            return result;
        }


    }
    @Nullable
    private Bitmap getMoviePoster(String url) {
        URL posterURL = null;
        try {
            posterURL = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) posterURL.openConnection();
            InputStream inputStream = new BufferedInputStream(posterURL.openStream());
            Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
            return imageBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


}
