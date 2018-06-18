package com.mobile.pid.pid.home.turmas.detalhes_turma;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.classes_e_interfaces.Dialogs;
import com.mobile.pid.pid.classes_e_interfaces.Turma;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v7.app.AlertDialog.*;

public class EditarTurma extends AppCompatActivity
{
    private static String TAG = EditarTurma.class.getSimpleName();

    Toolbar   toolbar;
    ImageView capa;
    EditText  nomeEditText;
    EditText  pinEditText;
    LinearLayout llCheckboxes;

    Button    btn_atualizar;
    Button    btn_excluir;

    Uri   capaUri;
    Turma turma;
    Map<String, Integer>    diasDaSemana;
    List<AppCompatCheckBox> checkBoxes;

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
        llCheckboxes  = findViewById(R.id.ll_editar_turma_checkboxes);
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

        inicializarCheckboxes();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setResult(-1);
    }

    public void inicializarCheckboxes()
    {
        int n = llCheckboxes.getChildCount();
        Log.i(TAG, "QtdCheckboxes: " + n);

        //checkBoxes   = new ArrayList(7);
        diasDaSemana = turma.getDiasDaSemana();

        for(int i = 1; i <= n; i++)
        {
            if(diasDaSemana.containsValue(i))
            {
                AppCompatCheckBox checkBox = (AppCompatCheckBox) llCheckboxes.getChildAt(i - 1);
                checkBox.setChecked(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            if(requestCode == Dialogs.RC_PHOTO_PICKER)
            {
                Uri uri =
            }
        }
    }

    public void onCheckboxClicked(View v)
    {
        boolean checado = ((AppCompatCheckBox) v).isChecked();
        int diaCalendar = 0;

        switch(v.getId())
        {
            case R.id.turma_domingo:
                diaCalendar = Calendar.SUNDAY;
                break;

            case R.id.turma_segunda:
                diaCalendar = Calendar.MONDAY;
                break;

            case R.id.turma_terca:
                diaCalendar = Calendar.TUESDAY;
                break;

            case R.id.turma_quarta:
                diaCalendar = Calendar.WEDNESDAY;
                break;

            case R.id.turma_quinta:
                diaCalendar = Calendar.THURSDAY;
                break;

            case R.id.turma_sexta:
                diaCalendar = Calendar.FRIDAY;
                break;

            case R.id.turma_sabado:
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
