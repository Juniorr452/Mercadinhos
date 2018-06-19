package com.mobile.pid.pid.home.turmas;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.classes_e_interfaces.Turma;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class NovaTurmaActivity extends AppCompatActivity
{
    private static final String TAG = "NovaTurmaActivity";
    private static final String FOTO_CAPA_TURMA = "https://firebasestorage.googleapis.com/v0/b/pi-ii-2920c.appspot.com/o/turmas%2Ffoto_capa_padrao.png?alt=media&token=1da66206-4687-40d2-8e22-25d47eb08fe8";
    private static final int RC_PHOTO_PICKER = 0;

    FirebaseUser user;

    ImageView imagemCapa;
    EditText nomeTurmaEditText;
    EditText pinEditText;

    Uri imagemUri;
    ProgressDialog progressDialog;
    Map<String, Integer> diasDaSemana;

    DatabaseReference turmasCriadasDatabaseReference;
    DatabaseReference turmasDatabaseReference;
    StorageReference  turmasStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_turma);

        // Referências do FIrebase
        DatabaseReference dbRoot       = FirebaseDatabase.getInstance().getReference();
        turmasCriadasDatabaseReference = dbRoot.child("userTurmasCriadas");
        turmasDatabaseReference        = dbRoot.child("turmas");
        turmasStorageReference         = FirebaseStorage.getInstance().getReference().child("turmas");

        user = FirebaseAuth.getInstance().getCurrentUser();

        // Elementos da UI
        Toolbar novaTurmaToolbar = findViewById(R.id.toolbar_nova_turma);
        imagemCapa               = findViewById(R.id.nova_turma_capa);
        nomeTurmaEditText        = ((TextInputLayout) findViewById(R.id.TILnovaTurmaNome)).getEditText();
        pinEditText              = ((TextInputLayout) findViewById(R.id.TILnovaTurmaPIN)).getEditText();

        novaTurmaToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        novaTurmaToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        diasDaSemana = new HashMap<>(7);
    }

    public void carregarImagem(View v)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.completar_acao)), RC_PHOTO_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK)
        {
            imagemUri = data.getData();
            imagemCapa.setImageURI(imagemUri);
        }
    }

    public void criarTurma(View v)
    {
        // Pegar os dados
        final String nomeTurma = nomeTurmaEditText.getText().toString();
        final String pinTurma  = pinEditText.getText().toString();

        try
        {
            validarCampos(nomeTurma);

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Criando turma...");
            progressDialog.setMessage("Aguarde...");
            progressDialog.show();

            // Pegar o ID da turma e o usuário logado que está criando a turma
            final String turmaId = turmasDatabaseReference.push().getKey();

            // Enviar a imagem selecionada para o  Firebase Storage (Se houver) e pegar a url.
            if (imagemUri != null)
            {
                String nomeImagem = imagemUri.getLastPathSegment();
                final StorageReference capaReference = turmasStorageReference.child(turmaId).child(nomeImagem);

                capaReference.putFile(imagemUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        capaReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task)
                            {
                                String capaUrl = task.getResult().toString();

                                Turma novaTurma = new Turma(nomeTurma, pinTurma, capaUrl, user.getUid(), diasDaSemana);

                                cadastrarTurma(novaTurma, turmaId);
                            }
                        });
                    }
                });
            }
            else // Se não tiver imagem, já pega a URL de capa padrão
            {
                Turma novaTurma = new Turma(nomeTurma, pinTurma, FOTO_CAPA_TURMA, user.getUid(), diasDaSemana);

                cadastrarTurma(novaTurma, turmaId);
            }
        }
        catch(NoSuchFieldException e)
        {
            new AlertDialog.Builder(this)
                .setTitle(R.string.Error)
                .setMessage(e.getMessage())
                .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
        }
    }

    // Inserir os dados da turma no banco de dados
    // Erro aqui ao usar o setValue com a Turma t
    private void cadastrarTurma(Turma t, String turmaId)
    {
        // Cadastrar a turma
        turmasDatabaseReference.child(turmaId).setValue(t);

        // Cadastrar referência no turmas_criadas
        turmasCriadasDatabaseReference.child(user.getUid()).child(turmaId).setValue(true);

        progressDialog.dismiss();
        Toast.makeText(this, "Turma cadastrada com sucesso", Toast.LENGTH_SHORT).show();
        finish();
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

    private void validarCampos(String nomeTurma) throws NoSuchFieldException
    {
        if (nomeTurma == null || nomeTurma.equals(""))
            throw new NoSuchFieldException(getString(R.string.preencha_nome_turma));

        if (diasDaSemana.size() == 0)
            throw new NoSuchFieldException(getString(R.string.Preencha_dia_semana));
    }
}
