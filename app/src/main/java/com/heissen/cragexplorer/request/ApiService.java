package com.heissen.cragexplorer.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heissen.cragexplorer.auxiliares.LocalDateTimeDeserializer;
import com.heissen.cragexplorer.models.Favorito;
import com.heissen.cragexplorer.models.Resenia;
import com.heissen.cragexplorer.models.Sector;
import com.heissen.cragexplorer.models.Sesion;
import com.heissen.cragexplorer.models.Usuario;
import com.heissen.cragexplorer.models.Via;
import com.heissen.cragexplorer.models.Zona;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class ApiService {
    public static final String URL_BASE = "http://192.168.1.7:5000/";

    private static ApiInterface apiInterface;

    public static ApiInterface getApiInferface() {

        Gson gson = new GsonBuilder()
                .setLenient()
                 .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())

              /*  .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    }
                })*/
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiInterface = retrofit.create(ApiInterface.class);

        return apiInterface;


    }

    public static void guardarToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.commit();
    }

    public static String leerToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", 0);
        return sp.getString("token", "");
    }

    public interface ApiInterface {
        final String URL_USERS = "api/Usuarios/";
        final String URL_SECTORES = "api/Sectores/";
        final String URL_ZONAS = "api/Zonas/";
        final String URL_VIAS = "api/Vias/";
        final String URL_FOTOS = "api/Fotos/";
        final String URL_SESIONES = "api/Sesiones/";
        final String URL_RESENIAS = "api/Resenias/";
        final String URL_FAVORITOS = "api/Favoritos/";

        //Metodos Usuario
        @FormUrlEncoded
        @POST(URL_USERS + "login")
        Call<String> login(@Field("Correo") String correo, @Field("Clave") String clave);

        @POST(URL_USERS + "register")
        Call<Usuario> register(@Body Usuario usuario);

        @GET(URL_USERS + "user")
        Call<Usuario> obtenerUsuario(@Header("Authorization") String token);

        @GET(URL_USERS + "test")
        Call<String> test(@Header("Authorization") String token);

        @POST(URL_USERS + "login-google")
        Call<String> loginGoogle(@Body Usuario usuario);

        //Metodos Sectores
        @GET(URL_SECTORES + "sectores")
        Call<ArrayList<Sector>> getSectores(@Header("Authorization") String token);

        @GET(URL_SECTORES + "{id}")
        Call<Sector> getSector(@Header("Authorization") String token, @Path("id") int id);

        @GET(URL_SECTORES + "fotos/{id}")
        Call<List<String>> getFotosSector(@Header("Authorization") String token, @Path("id") int id);

        @GET(URL_SECTORES + "foto/{id}")
        Call<ResponseBody> getFotoSector(@Header("Authorization") String token, @Path("id") int id);


        @GET(URL_SECTORES + "calificacion/{id}")
        Call<Double> getCalificacionSector(@Header("Authorization") String token, @Path("id") int id);

        //Metodos Zonas
        @GET(URL_ZONAS + "sector/{idSector}")
        Call<List<Zona>> getZonasSector(@Header("Authorization") String token, @Path("idSector") int id);

        @GET(URL_ZONAS + "{idZona}/vias")
        Call<ArrayList<Via>> getViasZona(@Header("Authorization") String token, @Path("idZona") int id);
        //Metodos Vias

        @GET(URL_VIAS + "{id}/fotos")
        Call<List<String>> getFotosVia(@Header("Authorization") String token, @Path("id") int id);

        @GET(URL_VIAS + "{id}/calificacion")
        Call<Double> getCalificacionVia(@Header("Authorization") String token, @Path("id") int id);

        @GET(URL_VIAS + "{id}/resenias")
        Call<ArrayList<Resenia>> getReseniasVia(@Header("Authorization") String token, @Path("id") int id);

        //Metodos Fotos
        @FormUrlEncoded
        @POST(URL_FOTOS + "agregar/{idVia}")
        Call<String> cargarFotosVia(@Header("Authorization") String token, @Field("fotos") ArrayList<String> fotos, @Path("idVia") int idVia);

        //Metodos sesiones

        @POST(URL_SESIONES + "agregar")
        Call<Sesion> agregarSesion(@Header("Authorization")String token, @Body Sesion sesion);

        //Metodos resenia

        @POST(URL_RESENIAS + "agregar")
        Call<Resenia> agregarResenia(@Header("Authorization") String token, @Body Resenia resenia);

        //Metodos favoritos
        @POST(URL_FAVORITOS + "agregarBorrar/{idVia}")
        Call<Favorito> agregarBorrarFavorito(@Header("Authorization") String token, @Path("idVia") int idVia);
        @GET(URL_FAVORITOS + "chequear/{idVia}")
        Call<Favorito> chequearFavorito(@Header("Authorization") String token, @Path("idVia") int idVia);
    }
}
