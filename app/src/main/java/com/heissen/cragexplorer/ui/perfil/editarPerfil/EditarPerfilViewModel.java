package com.heissen.cragexplorer.ui.perfil.editarPerfil;

import android.app.Application;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Api;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.models.Sesion;
import com.heissen.cragexplorer.models.Usuario;
import com.heissen.cragexplorer.request.ApiService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilViewModel extends AndroidViewModel {
    private String token;
    private ApiService.ApiInterface apiService;

    private MutableLiveData<Boolean> mFlag;
    private MutableLiveData<Boolean> mFlag2;
    private MutableLiveData<Boolean> mFlagEditado;
    private MutableLiveData<Boolean> mFlagRelogueo;
    private MutableLiveData<Uri> mImgUri;
    private MutableLiveData<Boolean> mGoogleUser;

    public EditarPerfilViewModel(@NonNull Application application) {
        super(application);
        apiService = ApiService.getApiInferface();
        token = ApiService.leerToken(application);
        mFlag = new MutableLiveData<>(false);
        mImgUri = new MutableLiveData<>();
        mFlag2 = new MutableLiveData<>(false);
        mFlagRelogueo = new MutableLiveData<>();
        mFlagEditado = new MutableLiveData<>();
        mGoogleUser = new MutableLiveData<>();

    }

    public LiveData<Uri> getmImgUri() {
        return mImgUri;
    }


    public LiveData<Boolean> getmGoogleUser() {
        return mGoogleUser;
    }


    public LiveData<Boolean> getmFlagEditado() {
        return mFlagEditado;
    }

    public LiveData<Boolean> getmFlagRelogueo() {
        return mFlagRelogueo;
    }

    public void chequearUsuario(Usuario usuario) {
        if (usuario.isGoogle()) {
            mGoogleUser.setValue(false);
        } else {
            mGoogleUser.setValue(true);
        }
    }

    public void editarUsuario(Usuario usuario, String claveNuevamente) {

        Log.d("salida", "usuario editado: " + usuario.toString());
        if (!usuario.getClave().equals(claveNuevamente)) {

            Toast.makeText(getApplication(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }
        if (usuario.getAvatar() != null && !usuario.getAvatar().isEmpty()) {
            usuario.setAvatar(convertirImgBase64(Uri.parse(usuario.getAvatar())));
        }


        Call<ResponseBody> llamada = apiService.editarUsuario(token, usuario);

        llamada.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Glide.get(getApplication()).clearMemory();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.get(getApplication()).clearDiskCache();
                        }
                    }).start();

                    mFlagEditado.setValue(true);
                    Log.d("salida", "respuesta: " + response.body());
                } else {
                    if (response.code() == 401) {
                        mFlagRelogueo.setValue(true);
                        Log.d("salida", "relogueo: " + response.body());
                    }
                    Log.d("salida", "ELSE : " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("salida", "ERROR Sesion: " + t.getMessage());
            }
        });
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

    public void cargarImg(ActivityResult result) {
        if (result.getData() != null) {
            mImgUri.setValue(result.getData().getData());
        }
    }

}