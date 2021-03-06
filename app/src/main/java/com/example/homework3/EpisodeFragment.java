package com.example.homework3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class EpisodeFragment extends Fragment {

    private String api_url;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private int id;
    private Random random;
    private TextView textView_episode;
    private TextView textView_airDate;
    private ArrayList<String> imageUrls;
    private Button button_moreInfo;
    private RecyclerView recyclerView;
    private ArrayList<Episode> episodeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_episode, container, false);
        random = new Random();
        api_url = "https://rickandmortyapi.com/api/episode/";
        textView_episode = v.findViewById(R.id.textView_episode);
        textView_airDate = v.findViewById(R.id.textView_airDate);
        episodeList = new ArrayList<>();
        recyclerView = v.findViewById(R.id.recyclerView_characters);
        button_moreInfo = v.findViewById(R.id.button_moreInfo);

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

    public void doThing(String api_url) {

        client.get(api_url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    textView_episode.setText(json.getString("episode").toUpperCase() + " " + json.getString("name").toUpperCase());
                    textView_airDate.setText("Aired On: " + json.getString("air_date"));
                    JSONArray characters = json.getJSONArray("characters");
                    for (int i = 0; i < characters.length(); i++) {
                        imageUrls = new ArrayList<>();
                        imageUrls.add(characters.getString(i));
                        doAnotherThing(imageUrls);
                    }
                    createNotificationChannel();
                    String wiki_url = "https://rickandmorty.fandom.com/wiki/" + json.getString("name");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(wiki_url));
                    PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "CHANNEL_ID")
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.notification)
                            .setContentTitle(json.getString("episode").toUpperCase())
                            .setContentText("Click here to learn more:")
                            .setContentIntent(pendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
                    button_moreInfo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            System.out.println("Clicked");
                            notificationManager.notify(100, builder.build());
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }
    public void doAnotherThing(ArrayList<String> imageUrls) {
        for (int i = 0; i < imageUrls.size(); i++) {
            client.get(imageUrls.get(i), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject json = new JSONObject(new String(responseBody));
                        String imageUrl = json.getString("image");
                        Episode episode = new Episode(imageUrl);
                        episodeList.add(episode);
                        EpisodeAdapter adapter = new EpisodeAdapter(episodeList);
                        recyclerView.setAdapter(adapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);

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
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
