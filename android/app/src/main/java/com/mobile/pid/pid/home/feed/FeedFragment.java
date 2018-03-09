package com.mobile.pid.pid.home.feed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.pid.pid.R;

/**
 * A simple {@link Fragment} subclass.
 */

// https://www.youtube.com/watch?v=4mwnhvRzRfw LINK PRA USAR RECYCLER/CARD/FRAGMENT
public class FeedFragment extends Fragment {


    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        RecyclerView recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(new RecyclerViewAdapter());

        return view;

    }

    // METODOS ===========================================================================

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private CardView card_feed;
        private TextView tv_user_feed;
        private TextView tv_message_feed;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.post_layout, container, false));

            card_feed = itemView.findViewById(R.id.card_feed);
            tv_user_feed = itemView.findViewById(R.id.tv_user_feed);
            tv_message_feed = itemView.findViewById(R.id.tv_message_feed);

        }

    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new RecyclerViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

}
