package com.mobile.pid.pid.home.feed;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.classes_e_interfaces.Post;

public class Feed {

    public Feed() {}

    public static void criarPost(final Post p) {
        final DatabaseReference dbPosts = FirebaseDatabase.getInstance().getReference("posts").child(p.getUserId());
        final DatabaseReference dbFeed = FirebaseDatabase.getInstance().getReference("feed").child(p.getUserId());

        p.setId(dbPosts.push().getKey());

        dbPosts.child(p.getId()).setValue(p);
        dbFeed.child(p.getId()).setValue(p);

        FirebaseDatabase.getInstance().getReference("userSeguidores").child(p.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for(DataSnapshot data : dataSnapshot.getChildren()) {
                                FirebaseDatabase.getInstance().getReference("feed").child(data.getKey()).child(p.getId()).setValue(p);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
}