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
    private MutableLiveData<Boolean> mFlag;

    private MutableLiveData<Boolean> mFlag2;
    private MutableLiveData<Drawable> mImagen;

    private MutableLiveData<Drawable> mImagen2;

    private MutableLiveData<Integer> mInputType;

    private MutableLiveData<Integer> mInputType2;

    private MutableLiveData<Uri> mImgUri;



    public RegisterActivityViewModel(@NonNull Application application) {
        super(application);
        mFlag = new MutableLiveData<>(false);
        mImagen = new MutableLiveData<>(application.getResources().getDrawable(R.drawable.ic_visibility_off));
        mInputType = new MutableLiveData<Integer>();
        mImgUri=new MutableLiveData<>();
        mImagen2=new MutableLiveData<>(application.getResources().getDrawable(R.drawable.ic_visibility_off));
        mInputType2=new MutableLiveData<>();
        mFlag2=new MutableLiveData<>(false);
    }

    public LiveData<Uri> getmImgUri() {
        return mImgUri;
    }

    public LiveData<Drawable> getImagen() {
        return mImagen;
    }

    public LiveData<Boolean> getFlag() {
        return mFlag;
    }

    public MutableLiveData<Integer> getmInputType() {
        return mInputType;
    }

    public LiveData<Drawable> getmImagen2() {
        return mImagen2;
    }

    public LiveData<Integer> getmInputType2() {
        return mInputType2;
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

   /* public void registrarUser(Usuario usuario) {
        Intent intent=new Intent(getApplication(),LoginActivity.class);
        // Cargar la imagen de avatar desde la URI y guardarla en el almacenamiento interno
        try {
            String nombreArchivo = "img_avatar.png";
            File directorioInterno = getApplication().getFilesDir();
            File archivoImagen = new File(directorioInterno, nombreArchivo);

            // Abrir un flujo de entrada para la URI
            InputStream inputStream = getApplication().getContentResolver().openInputStream(Uri.parse(usuario.getAvatar()));

            // Abrir un flujo de salida para el archivo en el almacenamiento interno
            FileOutputStream outputStream = new FileOutputStream(archivoImagen);

            // Copiar los datos desde el flujo de entrada al flujo de salida
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Cerrar los flujos
            inputStream.close();
            outputStream.close();

            // Establecer la ruta del archivo en la propiedad de avatar del usuario
            usuario.setAvatar(archivoImagen.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar cualquier excepción que pueda ocurrir durante la operación
        }

        // Hacer la llamada de registro al servidor
        ApiService.ApiInterface apiService = ApiService.getApiInferface();

        Call<Usuario> llamada = apiService.register(usuario);

        llamada.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Log.d("salida","Respuesta: "+response.body().toString());
                    getApplication().startActivity(intent);
                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });
    }*/


    public void cambiarVisibilidadClave() {
        Log.d("salida", mFlag.getValue().toString());
        if (mFlag.getValue()) {
            mImagen.setValue(getApplication().getResources().getDrawable(R.drawable.ic_visibility_off));
            mInputType.setValue(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            mImagen.setValue(getApplication().getResources().getDrawable(R.drawable.ic_visibility));
            mInputType.setValue(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        mFlag.setValue(!mFlag.getValue());
    }
    public void cambiarVisibilidadClave2() {
        Log.d("salida", mFlag2.getValue().toString());
        if (mFlag2.getValue()) {
            mImagen2.setValue(getApplication().getResources().getDrawable(R.drawable.ic_visibility_off));
            mInputType2.setValue(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            mImagen2.setValue(getApplication().getResources().getDrawable(R.drawable.ic_visibility));
            mInputType2.setValue(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        mFlag2.setValue(!mFlag2.getValue());
    }
}
