package com.mobile.pid.pid.home.feed;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.classes_e_interfaces.Post;
import com.mobile.pid.pid.R;

import java.util.HashMap;
import java.util.Map;

public class FeedFunctions {

    public FeedFunctions() {}

    public static void criarPost(final Post p) {
        final DatabaseReference dbPosts = FirebaseDatabase.getInstance().getReference("posts").child(p.getUserId());
        final DatabaseReference dbFeed = FirebaseDatabase.getInstance().getReference("feed").child(p.getUserId());
        final DatabaseReference dbPostFeeds = FirebaseDatabase.getInstance().getReference("postFeeds");

        p.setId(dbPosts.push().getKey());

        dbPosts.child(p.getId()).setValue(p);
        dbFeed.child(p.getId()).setValue(p);
        dbPostFeeds.child(p.getId()).child(p.getUserId()).setValue(true);

        FirebaseDatabase.getInstance().getReference("userSeguidores").child(p.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for(DataSnapshot data : dataSnapshot.getChildren()) {
                                FirebaseDatabase.getInstance().getReference("feed").child(data.getKey()).child(p.getId()).setValue(p);
                                dbPostFeeds.child(p.getId()).child(data.getKey()).setValue(true);
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public static void excluirPost(final Post p, Context context) {

        final ProgressDialog progressDialog= new ProgressDialog(context, R.style.DialogTheme);
        progressDialog.setTitle("Excluir post");
        progressDialog.setMessage("Excluindo post...");
        progressDialog.show();

        FirebaseDatabase.getInstance().getReference("posts").child(p.getUserId()).child(p.getId()).removeValue();

        final DatabaseReference dbPostFeeds = FirebaseDatabase.getInstance().getReference("postFeeds").child(p.getId());
        final DatabaseReference dbFeed = FirebaseDatabase.getInstance().getReference("feed");
        final DatabaseReference dbUserLikes = FirebaseDatabase.getInstance().getReference("userLikes");
        final DatabaseReference dbPostLikes = FirebaseDatabase.getInstance().getReference("postLikes").child(p.getId());


        dbPostFeeds.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot data: dataSnapshot.getChildren()) {
                        dbFeed.child(data.getKey()).child(p.getId()).removeValue();
                        data.getRef().removeValue();// PEGA O REF DOS FEEDS QUE TEM ESSE POST
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbPostLikes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot data: dataSnapshot.getChildren()) {
                        dbUserLikes.child(data.getKey()).child(p.getId()).removeValue();
                        data.getRef().removeValue();// PEGA O REF DOS LIKES DOS USUARIOS QUE TEM ESSE POST
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        progressDialog.dismiss();
    }

    public static void excluirPostsFollow(String user, final String userUnfollow) {
        FirebaseDatabase.getInstance().getReference("feed").child(user)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for(DataSnapshot data : dataSnapshot.getChildren()) {

                                Post p = data.getValue(Post.class);

                                if(p.getUserId().equals(userUnfollow))
                                    data.getRef().removeValue();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public static void criarComentario(final Post p, String pid) {
        final DatabaseReference dbComentarios = FirebaseDatabase.getInstance().getReference("postComments").child(pid);

        p.setId(dbComentarios.push().getKey());

        dbComentarios.child(p.getId()).setValue(p);
    }
}
