package com.example.juniorr.testenodejs;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by junio on 11/02/2018.
 */

public class ConsultaProdutos extends AsyncTask<Void, Void, String>
{
  private ConsultaProdutosListener listener;
  private String urlString;

  public ConsultaProdutos(ConsultaProdutosListener listener, String url){
    this.listener = listener;
    this.urlString = url;
  }

  @Override
  protected String doInBackground(Void... params)
  {
    try
    {
      return consultarServidor();
    }
    catch(IOException e)
    {
      e.printStackTrace();
      return e.getMessage();
    }
  }

  public String consultarServidor() throws IOException
  {
    InputStream is = null;
    try
    {
      URL url = new URL(urlString);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      conn.setReadTimeout(10000);
      conn.setConnectTimeout(35000);
      conn.setRequestMethod("GET");
      conn.setDoInput(true);
      conn.connect();
      conn.getResponseCode();

      is = conn.getInputStream();

      Reader reader = new InputStreamReader(is);
      char[] buffer = new char[10240];
      reader.read(buffer);
      return new String(buffer);
    }
    finally
    {
      if (is != null)
        is.close();
    }
  }

  @Override
  protected void onPostExecute(String result)
  {
    listener.onConsultaConcluida(result);
  }

  public interface ConsultaProdutosListener{
    void onConsultaConcluida(String resposta);
  }
}
