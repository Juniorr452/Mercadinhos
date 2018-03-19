package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.perfil.FollowItem;


import java.util.List;

/**
 * Created by jonasramos on 19/03/18.
 */

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.RecyclerViewHolder> {

    private Context context;
    private List<FollowItem> follow;
    private int context_cod;

    private final String FOLLOW_STRING;
    private final String FOLLOWING_STRING;

    public FollowAdapter(Context context, List<FollowItem> follow, int context_cod) {
        this.context = context;
        this.follow = follow;
        this.context_cod = context_cod;
        FOLLOW_STRING = context.getResources().getString(R.string.follow);
        FOLLOWING_STRING = context.getResources().getString(R.string.following);
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
        return follow.size() + 10;
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon_user_follow;
        private TextView textView_user_name;
        private TextView textView_user;
        private Button btn_follow;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.follow_layout, container, false));

            icon_user_follow   = (ImageView) itemView.findViewById(R.id.icon_user_follow);
            textView_user_name = (TextView) itemView.findViewById(R.id.textView_user_name);
            textView_user      = (TextView) itemView.findViewById(R.id.textView_user);
            btn_follow         = (Button) itemView.findViewById(R.id.btn_follow);

            if(context_cod == 1) {
                setFollowStateButton(btn_follow);
            }

            btn_follow.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    changeStateButton(btn_follow);
                }
            });
        }


    }

    private void changeStateButton(Button btn_follow) {

        if(btn_follow.getText().toString().equals(FOLLOW_STRING)) {
            setFollowStateButton(btn_follow);
        } else {
            setUnfollowStateButton(btn_follow);
        }
    }

    private void setFollowStateButton(Button btn_follow) {

        btn_follow.setText(FOLLOWING_STRING);
        btn_follow.setBackgroundResource(R.color.white);
        btn_follow.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
    }

    private void setUnfollowStateButton(Button btn_follow) {

        btn_follow.setText(FOLLOW_STRING);
        btn_follow.setBackgroundResource(R.color.colorPrimaryDark);
        btn_follow.setTextColor(ContextCompat.getColor(context, R.color.white));
    }
}

