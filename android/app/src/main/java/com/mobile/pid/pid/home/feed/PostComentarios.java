package com.mobile.pid.pid.home.feed;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.pid.pid.R;
import com.mobile.pid.pid.classes_e_interfaces.Post;

public class PostComentarios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comentarios);

        Intent i = getIntent();

        Post p = i.getParcelableExtra("post");

        FrameLayout f = findViewById(R.id.post_original);
        ImageView foto = f.findViewById(R.id.icon_user_feed);
        TextView nome = f.findViewById(R.id.tv_user_feed);
        TextView postData = f.findViewById(R.id.postTime);
        TextView countReply = f.findViewById(R.id.count_reply);
        CheckBox comment = f.findViewById(R.id.cb_reply);
        TextView countLikes = f.findViewById(R.id.count_like);
        CheckBox like = f.findViewById(R.id.cb_like);
        TextView texto = f.findViewById(R.id.tv_message_feed);

        Button comentar = findViewById(R.id.btn_comentar);
        RecyclerView comentarios = findViewById(R.id.post_comment);

        comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarComentario(v);
            }
        });

    }

    private void criarComentario(View view) {

        Dialog.OnClickListener click = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);
        mBuilder.setPositiveButton(getText(R.string.postar), click)
                .setNegativeButton(getText(R.string.cancel), null)
                .create();

        AlertDialog dialog = mBuilder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.gray_font));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.gray_font));

    }
}
