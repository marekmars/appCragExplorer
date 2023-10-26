package com.heissen.cragexplorer.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heissen.cragexplorer.LocalDateTimeDeserializer;
import com.heissen.cragexplorer.models.Sector;
import com.heissen.cragexplorer.models.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class ApiService {
    public static final String URL_BASE = "http://192.168.1.7:5000/";

    private static ApiInterface apiInterface;

    public static ApiInterface getApiInferface(){

        Gson gson= new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiInterface=retrofit.create(ApiInterface.class);

        return apiInterface;


    }
    public static void guardarToken(Context context, String token){
        SharedPreferences sp=context.getSharedPreferences("token.xml",0);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("token",token);
        editor.commit();
    }
    public static String leerToken(Context context){
        SharedPreferences sp=context.getSharedPreferences("token.xml",0);
        return sp.getString("token","");
    }
    public interface ApiInterface {
        final String URL_USERS = "api/Usuarios/";
        final String URL_SECTORES = "api/Sectores/";
        //Metodos Usuario
        @FormUrlEncoded
        @POST(URL_USERS+"login")
        Call<String> login(@Field("Correo") String correo, @Field("Clave") String clave);

        @POST(URL_USERS+"register")
        Call<Usuario> register(@Body Usuario usuario);

        @GET(URL_USERS+"user")
        Call<Usuario> obtenerUsuario(@Header("Authorization") String token);
        @GET(URL_USERS+"test")
        Call<String> test(@Header("Authorization") String token);
        @POST(URL_USERS+"login-google")
        Call<String> loginGoogle(@Body Usuario usuario);

        //Metodos Sectores
        @GET(URL_SECTORES+"sectores")
        Call<ArrayList<Sector>> getSectores(@Header("Authorization") String token);



    }
}
