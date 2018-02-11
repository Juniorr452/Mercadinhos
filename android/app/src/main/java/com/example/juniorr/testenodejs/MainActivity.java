package com.example.juniorr.testenodejs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ConsultaProdutos.ConsultaProdutosListener
{
  private EditText ip;
  private EditText produtoNome;
  private EditText produtoPreco;
  private TextView respostaServer;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ip             = findViewById(R.id.ip);
    produtoNome    = findViewById(R.id.produtoNome);
    produtoPreco   = findViewById(R.id.produtoPreco);
    respostaServer = findViewById(R.id.respostaServer);
  }

  public void enviarProduto(View view)
  {
    String nome = produtoNome.getText().toString();
    double preco = Double.parseDouble(produtoPreco.getText().toString());

    Toast.makeText(MainActivity.this, nome + " " + preco,Toast.LENGTH_SHORT).show();
  }

  public void pegarProdutos(View view)
  {
    respostaServer.setText("Carregando...");
    new ConsultaProdutos(this, ip.getText().toString()).execute();
  }

  @Override
  public void onConsultaConcluida(String resposta)
  {
    respostaServer.setText(resposta);
  }
}
