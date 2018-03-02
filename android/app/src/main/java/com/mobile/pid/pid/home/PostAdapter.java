package com.mobile.pid.pid.home;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.pid.pid.R;

import java.util.List;

/**
 * Created by jonasramos on 02/03/18.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.post_layout, null);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.textView_user.setText(post.getUser());
        holder.textView_message.setText(post.getPost_message());
        holder.imageView_icon.setImageDrawable(context.getResources().getDrawable(post.getImage()));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView_icon;
        TextView textView_user;
        TextView textView_message;

        public PostViewHolder(View itemView) {
            super(itemView);

            imageView_icon   = itemView.findViewById(R.id.imageView_icon);
            textView_user    = itemView.findViewById(R.id.textView_user);
            textView_message = itemView.findViewById(R.id.textView_message);
        }
    }
}
