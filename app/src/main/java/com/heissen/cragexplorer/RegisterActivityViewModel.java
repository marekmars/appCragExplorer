package com.heissen.cragexplorer;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.heissen.cragexplorer.models.Usuario;
import com.heissen.cragexplorer.request.ApiService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivityViewModel extends AndroidViewModel {


    private MutableLiveData<Uri> mImgUri;



    public RegisterActivityViewModel(@NonNull Application application) {
        super(application);
        mImgUri=new MutableLiveData<>();

    }

    public LiveData<Uri> getmImgUri() {
        return mImgUri;
    }



    public String convertirImgBase64(Uri uri) {
        try {
            ContentResolver contentResolver = getApplication().getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);

            if (inputStream != null) {
                // Decodifica la imagen en un objeto Bitmap
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2; // Puedes ajustar este valor para comprimir más o menos
                Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream, null, options);

                // Comprime la imagen (ajusta la calidad según tus necesidades)
                int quality = 50; // Rango de 0 (más comprimido) a 100 (menos comprimido)
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);

                // Convierte la imagen comprimida a Base64
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                return Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } else {
                Log.d("salida", "Error al cargar la imagen");
                return null;
            }
        } catch (IOException e) {
            // Handle error: Ocurrió un error al leer la imagen.
            e.printStackTrace();
            return null;
        }
    }

    public void registrarUser(Usuario u,String claveNuevamente) {

        if (!u.getClave().equals(claveNuevamente)) {

            Toast.makeText(getApplication(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(u.getNombre()) || TextUtils.isEmpty(u.getCorreo()) || TextUtils.isEmpty(u.getClave())) {
            // Algunos de los campos requeridos están vacíos
            Toast.makeText(getApplication(), "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        Intent intent = new Intent(getApplication(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d("salida", u.toString());
        Usuario usuario = u;

        usuario.setAvatar(convertirImgBase64(Uri.parse(u.getAvatar())));
        Call<Usuario> llamada = apiService.register(usuario);

        llamada.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {

                    if (response.body()==null) {
                        Toast.makeText(getApplication(), "Ya existe un usuario con ese email", Toast.LENGTH_SHORT).show();
                    }else {
                        Log.d("salida", response.body().toString());
                        getApplication().startActivity(intent);
                    }

                } else {
                    Log.d("salida", "ELSE " + response.raw());

                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.d("salida", "ERROR " + t.getMessage());

            }
        });
    }
    public void cargarImg(ActivityResult result){
        if(result.getData()!=null){
            mImgUri.setValue(result.getData().getData());
        }
    }

}
