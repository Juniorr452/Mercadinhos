package com.mobile.pid.pid.home.turmas.detalhes_turma;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.classes_e_interfaces.Turma;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.support.v7.app.AlertDialog.*;

public class EditarTurma extends AppCompatActivity
{
    Toolbar   toolbar;
    ImageView capa;
    EditText  nomeEditText;
    EditText  pinEditText;
    Button    btn_atualizar;
    Button    btn_excluir;

    Turma turma;
    Map<String, Integer> diasDaSemana;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_turma);

        Intent i = getIntent();
        turma    = (Turma) i.getSerializableExtra("turma");

        diasDaSemana = new HashMap<>(7);

        toolbar       = findViewById(R.id.toolbar_turma);
        capa          = findViewById(R.id.turma_capa);
        nomeEditText  = findViewById(R.id.editar_turma_nome);
        pinEditText   = findViewById(R.id.editar_turma_pin);
        btn_atualizar = findViewById(R.id.btn_atualizar_turma);
        btn_excluir   = findViewById(R.id.btn_excluir_turma);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Glide.with(this).load(turma.getCapaUrl()).into(capa);
        nomeEditText.setText(turma.getNome());
        pinEditText.setText(turma.getPin());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setResult(-1);
    }

    public void onCheckboxClicked(View v)
    {
        boolean checado = ((CheckBox) v).isChecked();
        int diaCalendar = 0;

        switch(v.getId())
        {
            case R.id.nova_turma_domingo:
                diaCalendar = Calendar.SUNDAY;
                break;

            case R.id.nova_turma_segunda:
                diaCalendar = Calendar.MONDAY;
                break;

            case R.id.nova_turma_terca:
                diaCalendar = Calendar.TUESDAY;
                break;

            case R.id.nova_turma_quarta:
                diaCalendar = Calendar.WEDNESDAY;
                break;

            case R.id.nova_turma_quinta:
                diaCalendar = Calendar.THURSDAY;
                break;

            case R.id.nova_turma_sexta:
                diaCalendar = Calendar.FRIDAY;
                break;

            case R.id.nova_turma_sabado:
                diaCalendar = Calendar.SATURDAY;
                break;
        }

        addOuRemoverDia(checado, (CheckBox) v, diaCalendar);
    }

    private void addOuRemoverDia(boolean checado, CheckBox checkboxDia, int numeroDia)
    {
        String nomeDia = checkboxDia.getText().toString();

        if (checado)
            diasDaSemana.put(nomeDia, numeroDia);
        else
            diasDaSemana.remove(nomeDia);
    }

    public void atualizarTurma(View view)
    {
        String nome = nomeEditText.getText().toString();
        String pin  = pinEditText.getText().toString();

        try
        {
            validarCampos(nome);
            turma.atualizar(nome, pin, diasDaSemana);
            finish();
        }
        catch(NoSuchFieldException e)
        {
            new AlertDialog.Builder(this)
                .setTitle(R.string.warning)
                .setMessage(e.getMessage())
                .setPositiveButton(R.string.Ok, null)
                .show();
        }
    }

    private void validarCampos(String nome) throws NoSuchFieldException
    {
        if(nome.trim().isEmpty())
            throw new NoSuchFieldException(getString(R.string.preencha_nome_turma));

        if(diasDaSemana.size() == 0)
            throw new NoSuchFieldException(getString(R.string.Preencha_dia_semana));
    }

    public void excluirTurma(View view)
    {
        Builder builder = new Builder(EditarTurma.this);
        builder.setTitle("Aviso");
        builder.setMessage("Tem certeza que deseja excluir a turma " + turma.getNome() + " ?");

        builder.setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
               turma.excluir();
               setResult(DetalhesTurma.RC_TURMA_EXCLUIDA);
               finish();
            }
        });

        builder.setNegativeButton(R.string.cancel, null);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(BUTTON_NEGATIVE).setTextColor(Color.parseColor("#737373"));
    }
}
