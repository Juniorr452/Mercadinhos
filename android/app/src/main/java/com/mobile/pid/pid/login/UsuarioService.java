package com.mobile.pid.pid.login;

import com.mobile.pid.pid.objetos.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsuarioService
{
    @GET("getRecomendacoesUsuarios")
    Call<List<Usuario>> getRecomendacoesUsuarios(@Query("uid") String uid);
}
