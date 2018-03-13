package com.mobile.pid.pid.home.feed;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.pid.pid.R;

import java.util.List;

/**
 * Created by jonasramos on 13/03/18.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.RecyclerViewHolder>{

    private Context context;
    private List<Post> posts;

    public FeedAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new RecyclerViewHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private CardView card_feed;
        private TextView tv_user_feed;
        private TextView tv_message_feed;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.post_layout, container, false));

            card_feed       = itemView.findViewById(R.id.card_feed);
            tv_user_feed    = itemView.findViewById(R.id.tv_user_feed);
            tv_message_feed = itemView.findViewById(R.id.tv_message_feed);

        }
    }
}
