package com.mobile.pid.pid.home.feed;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.classes_e_interfaces.Post;
import com.mobile.pid.pid.home.adapters.ComentariosAdapter;
import com.mobile.pid.pid.home.adapters.PostAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class PostComentarios extends AppCompatActivity {

    private Query comentRef;
    private ValueEventListener comentListener;
    private ComentariosAdapter comentarioAdapter;
    private Post p;
    private String usuarioLogado;

    private FrameLayout f;
    private ImageView foto;
    private TextView nome;
    private TextView postData;
    private TextView countReply;
    private TextView texto;
    private Toolbar toolbar;
    private CheckBox like;
    private TextView countLike;
    private Button comentar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comentarios);

        Intent i = getIntent();

        comentarioAdapter = new ComentariosAdapter(this);

        usuarioLogado = FirebaseAuth.getInstance().getCurrentUser().getUid();

        p = i.getParcelableExtra("post");

        f            = findViewById(R.id.post_original);
        foto         = f.findViewById(R.id.icon_user_feed);
        nome         = f.findViewById(R.id.tv_user_feed);
        postData     = f.findViewById(R.id.postTime);
        countReply   = f.findViewById(R.id.count_reply);
        texto        = f.findViewById(R.id.tv_message_feed);
        toolbar      = findViewById(R.id.toolbar_comentarios);
        comentar     = findViewById(R.id.btn_comentar);
        recyclerView = findViewById(R.id.post_comment);
        like         = f.findViewById(R.id.cb_like);
        countLike    = f.findViewById(R.id.count_like);
        comentRef = FirebaseDatabase.getInstance().getReference("postComments").child(p.getId()).orderByChild("postData");

        like.setVisibility(View.GONE);
        countLike.setVisibility(View.GONE);

        comentListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    countReply.setText(String.valueOf(dataSnapshot.getChildrenCount()));

                    comentarioAdapter.clear();

                    for(DataSnapshot com : dataSnapshot.getChildren()) {
                        Post c = com.getValue(Post.class);
                        c.setId(com.getKey());
                        comentarioAdapter.add(c);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(comentarioAdapter);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarComentario(v);
            }
        });

        Glide.with(this).load(p.getPhotoUrl()).into(foto);
        nome.setText(p.getNomeUser());
        try {
            postData.setText(p.calcularTempo());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        texto.setText(p.getTexto());

    }

    @Override
    protected void onStart() {
        super.onStart();

        comentRef.addValueEventListener(comentListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        comentRef.removeEventListener(comentListener);
    }

    private void criarComentario(View view) {

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_criar_comentario, null);
        final EditText comentario = v.findViewById(R.id.edit_comment);
        final TextView countChars = v.findViewById(R.id.count_chars);

        Dialog.OnClickListener click = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Post post = new Post(null, usuarioLogado, comentario.getText().toString().trim());
                FeedFunctions.criarComentario(post, p.getId());
                dialog.dismiss();

            }
        };

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);

        mBuilder.setView(v);
        mBuilder.setPositiveButton(getText(R.string.postar), click)
                .setNegativeButton(getText(R.string.cancel), null)
                .create();

        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.gray_font));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.gray_font));

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() <= 200 && s.length() > 0) {
                    countChars.setTextColor(getResources().getColor(R.color.gray_font));
                    dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.white));
                }
                else {
                    countChars.setTextColor(getResources().getColor(R.color.colorAccent));
                    dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.gray_font));
                }

                countChars.setText(String.valueOf(s.length()) + "/" + getText(R.string.limit_chars_post));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        comentario.addTextChangedListener(textWatcher);

    }
}
