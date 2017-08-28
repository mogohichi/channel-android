package co.getchannel.channel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import co.getchannel.channel.responses.CHThreadResponse;
import co.getchannel.channel.R;


public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MovieViewHolder> {

    private List<CHThreadResponse.CHThreadResult.CHThreadData.CHThreadMessage> movies;
    private int rowLayout;
    private Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        LinearLayout moviesLayout;
        TextView movieTitle;
        TextView data;
        TextView movieDescription;
        TextView rating;


        public MovieViewHolder(View v) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            movieTitle = (TextView) v.findViewById(R.id.title);
            data = (TextView) v.findViewById(R.id.subtitle);
            movieDescription = (TextView) v.findViewById(R.id.description);
            rating = (TextView) v.findViewById(R.id.rating);
        }
    }

    public ChatsAdapter(List<CHThreadResponse.CHThreadResult.CHThreadData.CHThreadMessage> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public ChatsAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.movieTitle.setText(movies.get(position).getData().getText());
//        holder.data.setText(movies.get(position).getReleaseDate());
//        holder.movieDescription.setText(movies.get(position).getOverview());
//        holder.rating.setText(movies.get(position).getVoteAverage().toString());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}