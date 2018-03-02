package com.mobile.pid.pid.feed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mobile.pid.pid.R;

public class NovaTurmaActivity extends AppCompatActivity
{
    // TODO: Nova turma código
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_turma);

        Toolbar novaTurmaToolbar = findViewById(R.id.toolbar_nova_turma);

        novaTurmaToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        novaTurmaToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
