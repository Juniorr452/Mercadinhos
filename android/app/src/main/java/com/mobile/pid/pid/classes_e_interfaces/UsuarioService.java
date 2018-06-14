package com.mobile.pid.pid.classes_e_interfaces;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsuarioService
{
    @GET("getRecomendacoesUsuarios")
    Call<List<Usuario>> getRecomendacoesUsuarios(@Query("uid") String uid);
}
