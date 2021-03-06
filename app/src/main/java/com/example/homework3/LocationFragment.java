package com.example.homework3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class LocationFragment extends Fragment {

    private String api_url = "https://rickandmortyapi.com/api/location/1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20";
    private static AsyncHttpClient client = new AsyncHttpClient();
    private ArrayList<Location> locationList;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_location, container, false);
        locationList = new ArrayList<>();

        client.get(api_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray json = new JSONArray(new String(responseBody));

                    for (int i = 0; i < 20; i++) {
                        Location location = new Location(json.getJSONObject(i).getString("name").toUpperCase(),
                                json.getJSONObject(i).getString("type"),
                                json.getJSONObject(i).getString("dimension"));
                        locationList.add(location);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recyclerView = v.findViewById(R.id.recyclerView_locations);
                LocationAdapter adapter = new LocationAdapter(locationList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));


            }



            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

        });

        return v;
    }
}

