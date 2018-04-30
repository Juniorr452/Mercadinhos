package com.mobile.pid.pid.home.feed;


import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.adapters.PostAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

// https://www.youtube.com/watch?v=4mwnhvRzRfw LINK PRA USAR RECYCLER/CARD/FRAGMENT
public class FeedFragment extends Fragment {

    private List<Post> posts;
    private FloatingActionButton criarPost;
    private TextView countChars;
    private EditText edit_post;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FrameLayout conteudo;

    private FirebaseUser usuario;
    private DatabaseReference postsRef;
    private ChildEventListener postsChildListener;
    private ImageView sadFace;
    private TextView sadMessage;

    private PostAdapter postAdapter;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        posts = new ArrayList<>();
        postAdapter = new PostAdapter(getActivity(), posts);
        usuario = FirebaseAuth.getInstance().getCurrentUser();

        /*FirebaseDatabase.getInstance().getReference("usuarios").child(usuario.getUid()).child("seguindo")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        recyclerView.getRecycledViewPool().clear();
                        postAdapter.clear();

                        getPosts(usuario.getUid());

                        if(dataSnapshot.exists()) {
                            for(DataSnapshot data : dataSnapshot.getChildren()) {
                                getPosts(data.getKey());
                            }
                        }

                        progressBar.setVisibility(View.GONE);
                        conteudo.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

        /*FirebaseDatabase.getInstance().getReference("usuarios").child(usuario.getUid()).child("posts").orderByKey()
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        if(dataSnapshot.exists()) {
                            recyclerView.getRecycledViewPool().clear();
                            recyclerView.setItemViewCacheSize(0);
                            postAdapter.notifyDataSetChanged();

                            Post post = dataSnapshot.getValue(Post.class);
                            post.setId(dataSnapshot.getKey());
                            postAdapter.add(post);

                            sadFace.setVisibility(View.GONE);
                            sadMessage.setVisibility(View.GONE);
                        } else {
                            sadFace.setVisibility(View.VISIBLE);
                            sadMessage.setVisibility(View.VISIBLE);
                        }

                        progressBar.setVisibility(View.GONE);
                        conteudo.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        recyclerView.getRecycledViewPool().clear();

                        Post p = dataSnapshot.getValue(Post.class);
                        p.setId(dataSnapshot.getKey());
                        postAdapter.removePost(p);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        sadFace      = view.findViewById(R.id.sad_face);
        sadMessage   = view.findViewById(R.id.sad_message);
        recyclerView = view.findViewById(R.id.recycler_view);
        criarPost    = view.findViewById(R.id.fab_add_post);
        progressBar  = view.findViewById(R.id.pb_feed);
        conteudo     = view.findViewById(R.id.fl_feed);

        conteudo.setVisibility(View.GONE);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(postAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy < 0 && !criarPost.isShown())
                    criarPost.show();
                else if(dy > 0 && criarPost.isShown())
                    criarPost.hide();
            }
        });

        criarPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarPost(view);
            }
        });

        return view;

    }

    private void criarPost(View view) {

        final View mView = getLayoutInflater().inflate(R.layout.dialog_criar_post, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(mView)
                .setPositiveButton(R.string.postar, null)
                .setNegativeButton(R.string.cancel, null)
                .create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.gray_font));

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final DatabaseReference db = FirebaseDatabase.getInstance().getReference("usuarios").child(usuario.getUid()).child("posts");
                final Post post = new Post(null,
                                           usuario.getUid(),
                                           edit_post.getText().toString());

                post.setId(db.push().getKey());
                db.child(post.getId()).setValue(post);
                dialog.dismiss();
            }
        });

        countChars = mView.findViewById(R.id.count_chars);
        edit_post = mView.findViewById(R.id.edit_post);

        final TextWatcher textWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2)
            {
                if(s.length() <= 200 && s.length() > 0) {
                    countChars.setTextColor(getResources().getColor(R.color.gray_font));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
                else {
                    countChars.setTextColor(Color.RED);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }

                countChars.setText(String.valueOf(s.length()) + "/" + getText(R.string.limit_chars_post));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        };

        edit_post.addTextChangedListener(textWatcher);
    }

    private String calcularData() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        return sdf.format(c.getTime());
    }

    private void getPosts(final String uid) {

        FirebaseDatabase.getInstance().getReference("usuarios").child(uid).child("posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot db : dataSnapshot.getChildren()) {
                            Post post = db.getValue(Post.class);
                            post.setId(db.getKey());
                            postAdapter.add(post);
                        }

                        if(postAdapter.getItemCount() == 0) {
                            sadFace.setVisibility(View.VISIBLE);
                            sadMessage.setVisibility(View.VISIBLE);
                        } else {
                            sadFace.setVisibility(View.GONE);
                            sadMessage.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
