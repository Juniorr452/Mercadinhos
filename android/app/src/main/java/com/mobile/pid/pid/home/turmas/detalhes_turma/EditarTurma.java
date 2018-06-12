package com.mobile.pid.pid.home.turmas.detalhes_turma;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.turmas.Turma;

import static android.support.v7.app.AlertDialog.*;

public class EditarTurma extends AppCompatActivity {

    ImageView capa;
    EditText nome;
    EditText pin;
    Button btn_atualizar;
    Button btn_excluir;
    Turma t;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_turma);

        Intent i = getIntent();
        t        = (Turma) i.getSerializableExtra("turma");

        capa          = findViewById(R.id.turma_capa);
        nome          = findViewById(R.id.editar_turma_nome);
        pin           = findViewById(R.id.editar_turma_pin);
        btn_atualizar = findViewById(R.id.btn_atualizar_turma);
        btn_excluir   = findViewById(R.id.btn_excluir_turma);
        toolbar       = findViewById(R.id.toolbar_turma);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Glide.with(this).load(t.getCapaUrl()).into(capa);
        nome.setText(t.getNome());
        pin.setText(t.getPin());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void onCheckboxClicked() {

    }

    public void atualizarTurma(View view) {
        //TODO ATUALIZAR TURMA NO FIREBASE
    }

    public void excluirTurma(View view) {

        Builder builder = new Builder(EditarTurma.this);
        builder.setTitle("Aviso");
        builder.setMessage("Tem certeza que deseja excluir a turma " + t.getNome() + " ?");

        builder.setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               
            }
        });

        builder.setNegativeButton(R.string.cancel, null);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(BUTTON_NEGATIVE).setTextColor(Color.parseColor("#737373"));
    }
}
