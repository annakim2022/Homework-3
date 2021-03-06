package com.example.homework3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class CharacterFragment extends Fragment {


    private String api_url;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private int id;
    private Random random;
    private ImageView imageView_mugShot;
    private TextView textView_character;
    private TextView textView_status;
    private TextView textView_species;
    private TextView textView_gender;
    private TextView textView_origin;
    private TextView textView_location;
    private TextView textView_episodeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_character, container, false);
        random = new Random();
        //id = random.nextInt(670) + 1;
        api_url = "https://rickandmortyapi.com/api/character/";
        textView_character = v.findViewById(R.id.textView_character);
        textView_status = v.findViewById(R.id.textView_status);
        textView_species = v.findViewById(R.id.textView_species);
        textView_gender = v.findViewById(R.id.textView_gender);
        textView_origin = v.findViewById(R.id.textView_origin);
        textView_location = v.findViewById(R.id.textView_location);
        textView_episodeList = v.findViewById(R.id.textView_episodeList);
        imageView_mugShot = v.findViewById(R.id.imageView_mugShot);

        client.get(api_url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    JSONObject info = json.getJSONObject("info");
                    int count = Integer.parseInt(info.getString( "count"));
                    id = random.nextInt(count) + 1;
                    api_url = api_url + id;
                    doThing(api_url);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        return v;
}

 public void doThing(String api_url){

         client.get(api_url, new AsyncHttpResponseHandler() {
         ArrayList<String> list = new ArrayList<String>();

         @Override
         public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
             try {
                 JSONObject json = new JSONObject(new String(responseBody));
                 textView_character.setText(json.getString( "name").toUpperCase());
                 textView_status.setText("Status: " + json.getString("status"));
                 textView_species.setText("Species: " + json.getString( "species"));
                 textView_gender.setText("Gender: " + json.getString("gender"));
                 JSONObject origin = json.getJSONObject("origin");
                 textView_origin.setText("Origin: " + origin.getString( "name"));
                 JSONObject location = json.getJSONObject("location");
                 textView_location.setText("Location: " + location.getString( "name"));
                 JSONArray episodeList = json.getJSONArray("episode");
                 for (int i = 0; i < episodeList.length(); i++){
                     String url = episodeList.getString(i);
                     String episode = url.replace("https://rickandmortyapi.com/api/episode/", "");
                     list.add(episode);
                 }
                 String str = list.get(0);
                 for (int i = 1; i < episodeList.length(); i++) {
                     str = str + ", " + list.get(i);
                 }
                 textView_episodeList.setText("Appeared in Episode(s): " + str);

                 String imageUrl = json.getString("image");
                 Picasso.get().load(imageUrl).into(imageView_mugShot);


             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
         @Override
         public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
         }
     });
 }

}
