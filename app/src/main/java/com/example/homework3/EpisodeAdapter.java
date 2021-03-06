package com.example.homework3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {

    private List<Episode> episodes;

    public EpisodeAdapter(List<Episode> episodes){
        this.episodes = episodes;
    }


    @NonNull
    @Override
    public EpisodeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View villagerView = inflater.inflate(R.layout.item_episode, parent, false);
        EpisodeAdapter.ViewHolder viewHolder = new EpisodeAdapter.ViewHolder(villagerView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeAdapter.ViewHolder holder, int position) {
        Episode episode = episodes.get(position);
        Picasso.get().load(episode.getImageUrl()).into(holder.imageView_image);


    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView_image;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView_image = itemView.findViewById(R.id.imageView_mugShots);
        }
    }


}
